YAHOO.util.Event.onDOMReady(function() {
    new YAHOO.util.DDTarget("dragDropArea1");
    new YAHOO.util.DDTarget("dragDropArea2");
    make_draggable("posterImage");
});

function make_draggable(element) {
    YAHOO.util.DDM.mode = YAHOO.util.DDM.INTERSECT;
    var dd = new YAHOO.util.DD(element);
    dd.orgPos = YAHOO.util.Dom.getXY(dd.getEl());
    dd.getEl().style.zIndex = 999;
    dd.reset = function() {
        YAHOO.util.Dom.setXY(this.getEl(), this.orgPos);
    }
    dd.endDrag = function() {
        this.reset();
    }
    dd.onDragDrop = function(e, id) {
        var oDD;
        if ("string" == typeof id) {
            oDD = YAHOO.util.DDM.getDDById(id);
        } else {
            oDD = YAHOO.util.DDM.getBestMatch(id);
        }
        if (this.getEl().movie) {
          sendMovie(this.getEl().movie, oDD.getEl());
          this.getEl().movie = null;
        }
        this.reset();
    }
}
