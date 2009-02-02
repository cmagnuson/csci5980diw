YAHOO.util.Event.onDOMReady(function() {
    make_draggable("posterImage");
});

function make_draggable(element) {
    var leftArea = YAHOO.util.Dom.get("dragDropArea1");
    var rightArea = YAHOO.util.Dom.get("dragDropArea2");

    var dd = new YAHOO.util.DD(element);
    dd.startDrag = function() {
        this.getEl().style.zIndex = 999;
    }
    dd.onDrag = function(e) {
        check_drag(this, e, leftArea);
        check_drag(this, e, rightArea);
    }
    dd.endDrag = function() {
        check_drop(this, leftArea);
        check_drop(this, rightArea);
    }
}

function check_drag(dd, e, area) {
    if (is_over(e, area)) {
        if (area.oldHTML) return;
        var img = dd.getEl();
        if (!img.movie) return;
        area.oldMovie = area.movie;
        area.movie = img.movie;
        area.oldHTML = area.innerHTML;
        displaySide(img.movie, area);
    } else {
        if (!area.oldHTML) return;
        area.movie = area.oldMovie;
        areaoldMovie = null;
        area.innerHTML = area.oldHTML;
        area.oldHTML = null;
    }
}

function check_drop(dd, area) {
    if (!area.oldHTML) return;
    var img = dd.getEl();
    if (!img.movie) return;
    area.oldHTML = null;
    newhtml = '<img id="posterImage" src="images/noResult.jpg" alt="No result available" />';
    parentNode = img.parentNode;
    parentNode.innerHTML = newhtml;
    img = parentNode.firstChild;
    make_draggable(img);
    return true;
}

function is_over(e, el) {
    var region = YAHOO.util.Dom.getRegion(el);
    return (region.top <= e.pageY && e.pageY < region.bottom
         && region.left <= e.pageX && e.pageX < region.right);
}
