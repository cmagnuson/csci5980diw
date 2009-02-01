
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
    showBasicInfo(movie, "basicInfo1");
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
    showBasicInfo(movie, "basicInfo2");
    showReviewTable(movie, "reviewsDataTable2");
        create_box_office_chart(movie, "boxOfficeChart2");
  }
  
  function createChartTabs(boxOfficeDiv, blogDiv, displayDiv){
     var tabView = new YAHOO.widget.TabView(); 
     tabView.addTab( new YAHOO.widget.Tab({ 
	    label: 'Box Office vs. Time', 
	    content:  "<div id=\"boxOfficeChart1\"></div>", 
	    active: true 
	})); 
	 
	tabView.addTab( new YAHOO.widget.Tab({ 
	    label: 'Blog Posts vs. Time', 
	    content: "<div id=\"blogMentionsChart1\"></div>" 
	 
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
  
  function showBasicInfo(movie, div) {
   if(movie.awsData === undefined){
   }
   else{
      var html = "<center><h1>"+movie.title+"</h1></center><br>";
      html+= "Actors: ";
      var actors = movie.awsData.actors;
      for(var i=0; i<actors.length-1; i++) {
          html += actors[i]+", ";
      }
      html += actors[i];
      html+="<br>";
  
  	 html+="Studio: "+movie.awsData.studio;
  	 html+="<br>";
  	 html+="Release Date: "+movie.awsData.releaseDate;
  	 html+="<br>";
     html+="Rating: "+movie.awsData.reviewScore;
	 html+="<br>";
     html+="Amazon Reviews: "+movie.awsData.totalReviews;
     html+="<br>";
    
     document.getElementById(div).innerHTML = html;
   
   }
 }
 
 function showReviewTable(movie, div) { 
 		if(movie.awsData === undefined){
		 }
	      else{  var myColumnDefs = [ 
	            {key:"rating", sortable:true, resizeable:true}, 
	            {key:"date", formatter:YAHOO.widget.DataTable.formatDate, sortable:true, sortOptions:{defaultDir:YAHOO.widget.DataTable.CLASS_DESC},resizeable:true}, 
	            {key:"votes", formatter:YAHOO.widget.DataTable.formatNumber, sortable:true, resizeable:true}, 
	            {key:"content", sortable:true, resizeable:true} 
	        ]; 
	 
	        var myDataSource = new YAHOO.util.DataSource(movie.awsData.reviews); 
	        myDataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY; 
	        myDataSource.responseSchema = { 
	            fields: ["rating","date","votes","content"] 
	        }; 
	 
	        var myDataTable = new YAHOO.widget.DataTable(div, 
	                myColumnDefs, myDataSource, {caption:"Amazon Reviews"}); 
	                 
	        return { 
	            oDS: myDataSource, 
	            oDT: myDataTable 
	        }; 
	        }
	    }