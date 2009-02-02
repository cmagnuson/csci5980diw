
YAHOO.util.Event.onDOMReady(function() {
    var leftArea = YAHOO.util.Dom.get("dragDropArea1");
    var rightArea = YAHOO.util.Dom.get("dragDropArea2");

    var dd = new YAHOO.util.DD("posterImage");
    dd.startDrag = function() {
        this.origZ = this.getEl().style.zIndex;
        this.getEl().style.zIndex = 999;
        dragging = this;
    };
    dd.endDrag = function() {
        this.getEl().style.zIndex = this.origZ;
        dragging = null;
    };
    dd.onDrag = function(e) {
        check_drag(this, e, leftArea);
        check_drag(this, e, rightArea);
    };
});

function check_drag(dd, e, area) {
    if (is_over(e, area)) {
        if (area.oldHTML) return;
        area.oldHTML = area.innerHTML;
        var img = dragging.getEl();
        if (!img.movie) return;
        displaySide(img.movie, area);
    } else {
        if (!area.oldHTML) return;
        area.innerHTML = area.oldHTML;
        area.oldHTML = null;
    }
}

function is_over(e, el) {
    var region = YAHOO.util.Dom.getRegion(el);
    return (region.top <= e.pageY && e.pageY < region.bottom
         && region.left <= e.pageX && e.pageX < region.right);
}
