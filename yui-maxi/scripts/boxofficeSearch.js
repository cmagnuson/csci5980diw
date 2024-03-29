function get_trynt_data(movie, callback, callback2) {
	var url = './proxies/trynt.cgi?' + movie.title;
    YAHOO.util.Connect.asyncRequest('GET', url, {success: function(o) {
        movie.tryntData = YAHOO.lang.JSON.parse(o.responseText).trynt['movie-boxoffice'];
        movie.tryntData.id = movie.tryntData['matched-id'];
        movie.tryntData.img = "http://www.boxofficemojo.com/images/" + movie.tryntData.id + "_poster.jpg";
        if (callback) callback(movie, callback2);
    }})
}

function get_box_office_data(movie, callback) {
	var url = './proxies/boxoffice.cgi?' + movie.tryntData['matched-id'];
    YAHOO.util.Connect.asyncRequest('GET', url, {success: function(o) {
        data = YAHOO.lang.JSON.parse(o.responseText);
        if (data.results.length > 0) {
            data.totalIncome = data.results[data.results.length-1].grosstd;
        }
        movie.boxOfficeMojoData = data;
        if (callback) callback(movie);
    }});
}
