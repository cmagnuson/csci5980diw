//Global variables for each of three movies we may be dealing with
  	var searchMovie = {};
  	var leftMovie = {};
  	var rightMovie = {};
  	YAHOO.widget.Chart.SWFURL = "http://yui.yahooapis.com/2.6.0/build//charts/assets/charts.swf";
  	
  	function runSearch(termField) {
        displayLoadingImage("posterImage");
  	    var tempMovie = {};
  	    tempMovie.view = "poster";
	 	tempMovie.fullTitle = document.getElementById(termField).value;
        var tempTitle = tempMovie.fullTitle;

        // truncate title to exclude string after "(", ",", or ":"
        var index = tempTitle.search(/\u0028/);
        if( index > -1 )
            tempTitle = tempTitle.substring(0, index);
        index = tempTitle.search(",");
        if( index > -1 )
            tempTitle = tempTitle.substring(0, index);
        index = tempTitle.search(":");
        if( index > -1 )
            tempTitle = tempTitle.substring(0, index);

        tempMovie.title = tempTitle;

        // make requests for data from our data sources
     	requestAwsData(tempMovie, draw);
     	searchMovie = tempMovie;
     	requestTechnoratiData(tempMovie, draw);
     	requestMovieLensData(tempMovie, draw);
        get_trynt_data(tempMovie, function() {
            get_box_office_data(tempMovie, draw);
        });
	  }

	  function sendMovie(movie, element) {
   		 if (element.id == "dragDropArea1") return sendMovieLeft();
   		 if (element.id == "dragDropArea2") return sendMovieRight();
  	 	 alert("Invalid Side: " + element.id);
 	 }
	  
	  function sendMovieLeft(){
	    searchMovie.view = "left";
	   	leftMovie = searchMovie;
        resetSearch();
	 	displayLeftSide(leftMovie);
	  }
	  function sendMovieRight(){
	    searchMovie.view = "right";
	    rightMovie = searchMovie;
        resetSearch();
	    displayRightSide(rightMovie);
	  }

      function resetSearch()
      {
          searchMovie = {};
          var poster = YAHOO.util.Dom.get("posterImage");
          var searchField = YAHOO.util.Dom.get("searchMovieTitle");

          poster.src = "images/noResult.jpg";
          poster.alt = "No result available";
          searchField.value = "";
      }

      function setACCoordinates()
      {
          var x = YAHOO.util.Dom.getX("searchMovieTitle");
          YAHOO.util.Dom.setX("searchContainer", x);
      }
	
	//From: http://snipplr.com/view/132/detect-ie/  
	function isIE()
	{
  		return /msie/i.test(navigator.userAgent) && !/opera/i.test(navigator.userAgent);
	}


  YAHOO.util.Event.onDOMReady(function() {
	if(isIE()){
	  document.getElementById("sendButtons").innerHTML =
	  " <input id=\"sendLeft\" type=\"submit\" value=\"Send Left\" onclick=\"sendMovieLeft()\" />"+
            "<br />"+
        "<input id=\"sendRight\" type=\"submit\" value=\"Send Right\" onclick=\"sendMovieRight()\" />";
		}
	  });
	           