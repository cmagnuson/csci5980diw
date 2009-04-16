<?php
// Copyright 2007 Facebook Corp.  All Rights Reserved.
//
// Application: Collin Mini
// File: 'index.php'
//   This is a sample skeleton for your application.
//

require_once 'facebook.php';

$appapikey = 'f68449a153a72e6c106c6a9f8e5225bf';
$appsecret = '913140d0f6053888f857ee2601e2b6ea';
$facebook = new Facebook($appapikey, $appsecret);
$user_id = $facebook->require_login();

function pic($uid) { echo "<fb:profile-pic uid=$uid linked=\"true\" />"; }
function name($uid) { echo "<fb:name uid=$uid linked=\"true\" />"; }

echo "<p>Your photo: "; pic("'loggedinuser'"); echo "</p>\n";
echo "<p>Groups:";
$query = "
SELECT gid FROM group WHERE nid = 16777356 and gid IN
  (SELECT gid FROM group_member WHERE uid IN
    (SELECT uid from user WHERE 16777356 in affiliations and uid in
      (SELECT uid2 FROM friend WHERE uid1=$user_id
       LIMIT 50)
     LIMIT 10
    )
   LIMIT 100
  )
 LIMIT 8";
$groups = $facebook->api_client->fql_query($query);
foreach ($groups as $group) {
  $gid = $group['gid'];
  // AND sex="female"
  $query = '
SELECT uid FROM user WHERE (relationship_status = "" OR relationship_status = "Single")
AND uid IN
(SELECT uid FROM group_member WHERE gid IN '.$gid.' LIMIT 100)
and not (uid in (SELECT uid2 FROM friend WHERE uid1='.$user_id.'))
LIMIT 20';
  $singles = $facebook->api_client->fql_query($query);
  if ($singles) {
    echo '<br /><fb:grouplink gid="' . $gid . '" /><br />';
    foreach ($singles as $single) {
      pic($single['uid']);
    }
  }
}
echo "</p>";
