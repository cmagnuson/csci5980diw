function display_image(movie, div) {
    var image = YAHOO.util.Dom.get(div);
    image.src = movie.tryntData.img;
}

function create_box_office_chart(movie, div) {
	YAHOO.widget.Chart.SWFURL = "http://yui.yahooapis.com/2.6.0/build//charts/assets/charts.swf";

	var myDataSource = new YAHOO.util.DataSource(movie.boxOfficeMojoData);
	myDataSource.responseSchema =
	{
        resultsList: "results",
        fields: ["day", "date", "gross", "theaters", "avg", "grosstd", "daynum"],
	};

	var seriesDef =
	[
		{ displayName: "Gross", yField: "gross" },
	];

	YAHOO.example.formatCurrencyAxisLabel = function( value )
	{
		return YAHOO.util.Number.format( value,
		{
			prefix: "$",
			thousandsSeparator: ",",
			decimalPlaces: 0
		});
	}

	YAHOO.example.getDataTipText = function( item, index, series )
	{
		var toolTipText = series.displayName + " for " + item['date'];
		toolTipText += "\n" + YAHOO.example.formatCurrencyAxisLabel( item[series.yField] );
		return toolTipText;
	}

	var currencyAxis = new YAHOO.widget.NumericAxis();
	currencyAxis.minimum = 0;
	currencyAxis.labelFunction = YAHOO.example.formatCurrencyAxisLabel;

	var mychart = new YAHOO.widget.LineChart(div, myDataSource,
	{
		series: seriesDef,
		xField: "daynum",
		yAxis: currencyAxis,
		dataTipFunction: YAHOO.example.getDataTipText,
		//only needed for flash player express install
		expressInstall: "assets/expressinstall.swf"
	});
    return mychart;
}
