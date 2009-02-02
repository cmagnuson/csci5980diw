
  function displayLeftSide(movie){
    var html =  " <div id=\"basicInfo1\">"+
	            "</div>"+
            "<div id=\"tabbedCharts1\">"+
            "</div>"+          
            "<div id=\"reviewsDataTable1\">"+
            "</div>";
    document.getElementById("dragDropArea1").innerHTML = html;
    createChartTabs("boxOfficeChart1","blogMentionsChart1","tabbedCharts1");
    showBlogChart(movie, "blogMentionsChart1");
    showBasicInfo(movie, "basicInfo1", 1);
    displayImage(movie, "poster1");
    showReviewTable(movie, "reviewsDataTable1");
    create_box_office_chart(movie, "boxOfficeChart1");
  }
  
   function displayRightSide(movie){
    var html =  " <div id=\"basicInfo2\">"+
	            "</div>"+      
            "<div id=\"tabbedCharts2\""+
            "</div>"+        
            "<div id=\"reviewsDataTable2\">"+
            "</div>";
    document.getElementById("dragDropArea2").innerHTML = html;
    createChartTabs("boxOfficeChart2","blogMentionsChart2","tabbedCharts2");
    showBlogChart(movie, "blogMentionsChart2");
    showBasicInfo(movie, "basicInfo2", 2);
    displayImage(movie, "poster2");
    showReviewTable(movie, "reviewsDataTable2");
    create_box_office_chart(movie, "boxOfficeChart2");
  }
  
  function createChartTabs(boxOfficeDiv, blogDiv, displayDiv){
     var tabView = new YAHOO.widget.TabView(); 
     tabView.addTab( new YAHOO.widget.Tab({ 
	    label: 'Box Office vs. Time', 
	    content:  "<div id=\""+boxOfficeDiv+"\"></div>", 
	    active: true 
	})); 
	 
	tabView.addTab( new YAHOO.widget.Tab({ 
	    label: 'Blog Posts vs. Time', 
	    content: "<div id=\""+blogDiv+"\"></div>" 
	 
	})); 	 
	
	tabView.appendTo(displayDiv);
  }

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

  function displayImage(movie, div)
  {
    if(movie.awsData === undefined)
    {
    }
    else
    {
        var image = YAHOO.util.Dom.get(div);
        image.src = movie.awsData.imageURL;
    }
  }
  
  function showBasicInfo(movie, div, leftOrRight) {
   if(movie.awsData === undefined){
   }
   else{
      var html = "<img id=\"poster" + leftOrRight + "\" src=\"images/no_result.jpg\" alt=\"Image of " + movie.fullTitle + "\" />";
      html += "<h1 style=\"text-align: center\">"+movie.awsData.title+"</h1>";
      html += "<b>Actors:</b> ";
      var actors = movie.awsData.actors;
      for(var i=0; i<actors.length-1; i++) {
          html += actors[i]+", ";
      }
      html += actors[i];
      html+="<br>";
  
  	 html+="<b>Studio:</b> "+movie.awsData.studio;
  	 html+="<br>";
  	 html+="<b>Release Date:</b> "+movie.awsData.releaseDate;
  	 html+="<br>";
     html+="<b>Amazon Rating:</b> "+movie.awsData.reviewScore;
	 html+="<br>";
     html+="<b>Amazon Reviews:</b> "+movie.awsData.totalReviews;
     html+="<br>";
     
     if(movie.movieLensData === undefined){
     }
     else{
      html+="<b>MovieLens Rating:</b> "+movie.movieLensData.rating;
      html+="<br>";
      html+="<b>MovieLens Popularity:</b> "+movie.movieLensData.popularity;
      html+="<br>";
     }
    
     document.getElementById(div).innerHTML = html;
   }
 }
 
 function showReviewTable(movie, div) { 
 		if(movie.awsData === undefined){
		 }
	      else{  var myColumnDefs = [ 
	            {key:"Rating", sortable:true, resizeable:true}, 
	            //{key:"date", formatter:YAHOO.widget.DataTable.formatDate, sortable:true, sortOptions:{defaultDir:YAHOO.widget.DataTable.CLASS_DESC},resizeable:true}, 
	            //{key:"votes", formatter:YAHOO.widget.DataTable.formatNumber, sortable:true, resizeable:true}, 
	            {key:"Review", sortable:true, resizeable:true} 
	        ]; 
	 
	        var myDataSource = new YAHOO.util.DataSource(movie.awsData.reviews); 
	        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
	        myDataSource.responseSchema = { 
	            fields: ["Rating","Review"] 
	        }; 
	 
	        var myDataTable = new YAHOO.widget.DataTable(div, 
	                myColumnDefs, myDataSource, {caption:"Amazon Reviews"}); 
	                 
	        return { 
	            oDS: myDataSource, 
	            oDT: myDataTable 
	        }; 
	        }
	    }
