function requestAwsData(movie){

	  var title = movie.title;

      function successHandler(o) {

          var resultArray = new Array();
          var root = o.responseXML.documentElement;
          var titles = root.getElementsByTagName('Item');
          
          if(movie.awsData === undefined){
           movie.awsData = {}
		  }
			
          for(var i=0; i<1; i++){
              movie.awsData.url = titles[i].getElementsByTagName('DetailPageURL')[0].firstChild.nodeValue;
              movie.awsData.title = titles[i].getElementsByTagName('Title')[0].firstChild.nodeValue;
			  movie.awsData.reviewScore = titles[i].getElementsByTagName('AverageRating')[0].firstChild.nodeValue;
			  movie.awsData.totalReviews = titles[i].getElementsByTagName('TotalReviews')[0].firstChild.nodeValue;
			  movie.awsData.reviews = new Array();
              movie.awsData.actors = new Array();
              var actors = titles[i].getElementsByTagName('Actor');
			  var reviews = titles[i].getElementsByTagName('Review');
			  
              for(var j=0; j<actors.length; j++){
                  movie.awsData.actors.push(actors[j].firstChild.nodeValue);
              }
              for(var j=0; j<reviews.length; j++){
                 movie.awsData.reviews.push(reviews[j].getElementsByTagName('Content')[0].firstChild.nodeValue);
              }

          }
      }

      function failureHandler(o) {
      }

      // A variable for the AWS account on which the requests will be made.
      // Your AWS ID as a string.
      var awsID = '1EMG6J1BK77DK46SF3G2';
      // The URL for the asynchronous request, make sure this points to
      // your own web proxy!
      var sUrl = 'TechnoratiProxy.cgi?http://ecs.amazonaws.com/onca/xml?Service=AWSECommerceService&'+
      'AWSAccessKeyId=1EMG6J1BK77DK46SF3G2&Operation=ItemSearch&SearchIndex=DVD'+
       '&ResponseGroup=Large&Count=1&Title=' + title;

      var request = YAHOO.util.Connect.asyncRequest('GET', sUrl,  {success:successHandler, failure:failureHandler});
  
}