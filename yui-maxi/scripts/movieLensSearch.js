function requestMovieLensData(movie, callback){
    var callbacks = { 
	    // Successful XHR response handler 
	    success : function (o) { 
	    
	        var movieLensRaw = eval("("+o.responseText+")");
	 		var movieLensData = ((movieLensRaw["ResultSet"])["Result"])[0];
	 		        
		       movie.movieLensData = {};
			   movie.movieLensData.rating = movieLensData.avgRating;
			   movie.movieLensData.popularity = movieLensData.popularity;
			   if (callback) callback(movie);
	    }, 
	 
	  failure : function (o) {
	     if (callback) callback(movie);
	  }
	};
	 
	// Make the call to the server for JSON data 
	YAHOO.util.Connect.asyncRequest('GET',"./proxies/MovieLensProxy.cgi?limit=1&format=json&title="+escape(movie.title), callbacks); 
}