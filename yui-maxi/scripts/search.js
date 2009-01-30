var autoComplete = function()
{
    // Instantiate movieLensDataSource
    var movieLensDataSource = new YAHOO.util.ScriptNodeDataSource("http://frost.cs.umn.edu/morten/movielenstitle.php");
    movieLensDataSource.responseType = YAHOO.util.XHRDataSource.TYPE_JSON;
    movieLensDataSource.responseSchema = 
    {
        resultsList: "ResultSet.Result",
        fields: [
            { key: "Title" },
            { key: "imdbId" },
            { key: "movieLensId" },
            { key: "genre" },
            { key: "avgRating" },
            { key: "popularity" },
            { key: "fullTitle" } 
        ],
        metaFields: {
            totalRecords: "ResultSet.totalResultsReturned",
            firstResultPosition: "ResultSet.firstResultPosition"
        }
    };

    // Instantiate movieTitleAutoComplete
    var movieTitleAutoComplete = 
        new YAHOO.widget.AutoComplete("searchMovieTitle", "searchContainer", 
                                      movieLensDataSource);
    movieTitleAutoComplete.resultTypeList = false;
    movieTitleAutoComplete.generateRequest = function(title)
    {
        // Remove "a " from the beginning of a search
        if( (title.length > 4) && 
            (title.substring(0,4).toLowerCase() == "a%20") )
        {
            title = title.substring(4);
        }
        // Remove "the " from the beginning of a search
        else if( (title.length > 6) && 
                 (title.substring(0,6).toLowerCase() == "the%20") )
        {
            title = title.substring(6);
        }

        return "?format=json&title=" + title;
    };

    movieTitleAutoComplete.itemSelectEvent.subscribe(function (el, args) {
        runSearch('searchMovieTitle')
    });

    return {
        oDS: movieLensDataSource,
        oAC: movieTitleAutoComplete
    };
}();    
