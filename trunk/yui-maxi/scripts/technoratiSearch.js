  function requestTechnoratiData(movie){
  
  	  var title = movie.title;
  	  
      function successHandler(o) {

          var resultArray = new Array();
          var root = o.responseXML.documentElement;
          var titles = root.getElementsByTagName('item');

          for(i=0; i<titles.length; i++){
	      var date = titles[i].getElementsByTagName('date')[0].firstChild.nodeValue;
	      var count = titles[i].getElementsByTagName('count')[0].firstChild.nodeValue;
			resultArray.push({"date":date,"count":parseInt(count)});
          }
          
          if(movie.technoratiData === undefined){
            movie.technoratiData = {};
          }
		
		  movie.technoratiData.postsByDate = resultArray;
		
      }

      function failureHandler(o) {
      }

      // A variable for the AWS account on which the requests will be made.
      // Your AWS ID as a string.
      var apiKey = '1c448f36f2564d9a126ae581078b818e';
      // The URL for the asynchronous request, make sure this points to
      // your own web proxy!
      var sUrl = 'TechnoratiProxy.cgi?http://api.technorati.com/dailycounts?key=' +
                  apiKey + '&q=' + escape(title);


      var request = YAHOO.util.Connect.asyncRequest('GET', sUrl,
           {success:successHandler, failure:failureHandler});
  }