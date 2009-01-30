  function showBlogChart(movie, div) {
      if(movie.technoratiData === undefined){
      }
      else if(movie.technoratiData.postsByDate === undefined){
      }
      else {
       blogResults = movie.technoratiData.postsByDate;
	var blogData = new YAHOO.util.DataSource(blogResults); 
	blogData.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
	blogData.responseSchema = { fields: [ "date", "count"] }; 

	var mychart = new YAHOO.widget.LineChart( div,  blogData, 
	{ 
            yField: "count",
	    xField: "date"
	}); 
      }				     
  }
  
  function showBasicInfo(movie, div) {
   if(movie.awsData === undefined){
   }
   else{
      var html = movie.title+"<br>";
      html+= "Actors: ";
      var actors = movie.awsData.actors;
      for(var i=0; i<actors.length-1; i++) {
          html += actors[i]+", ";
      }
      html += actors[i];
      html+="<br>";
  
     html+="Rating: "+movie.awsData.reviewScore;
	 html+="<br>";
     html+="# Reviews: "+movie.awsData.totalReviews;
     html+="<br>";
    
     document.getElementById(div).innerHTML = html;
   
   }
 }