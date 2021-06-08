<div id="demoMap" style="height:400px; width: 800px; display: inline-block; float: left; padding-right: 100px;"></div>
<script src="OpenLayers.js"></script>
<script>

    mapnik = new OpenLayers.Layer.OSM();
    fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
    toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    position = new OpenLayers.LonLat(15.3527785, 46.167495).transform(fromProjection, toProjection);
    zoom = 10;
    size = new OpenLayers.Size(21,25);
    offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    icon = new OpenLayers.Icon("here.png", size, offset);

    function init() {
        map = new OpenLayers.Map("demoMap");
        markers = new OpenLayers.Layer.Markers( "Markers" );
        map.addLayer(markers);
        // testniMarker(25.3527785 ,10.167495)
        map.addLayer(mapnik);
        map.setCenter(position, zoom, map);
    }


    function testniMarker(lng, lat){
        let position = new OpenLayers.LonLat(lng, lat).transform(fromProjection, toProjection);
        let size = new OpenLayers.Size(21,25);
        let offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
        let icon = new OpenLayers.Icon("here.png", size, offset);
        markers.addMarker(new OpenLayers.Marker(position,icon));
    }

    function cestaMarker(lng, lat, opis){
        console.log(opis);
        let position = new OpenLayers.LonLat(lng, lat).transform(fromProjection, toProjection);
        let size = new OpenLayers.Size(21,25);
        let offset = new OpenLayers.Pixel(-(size.w / 2), -size.h);
        let icon = new OpenLayers.Icon("here.png", size, offset);
        popup = new OpenLayers.Popup("Prikaz",
            position,
            new OpenLayers.Size(150,30),
            "RUKER",
            true);

        let html = "<p style='color:black; font-size: 20px;'>" + opis;
        popup.setContentHTML(html);
        let marker = new OpenLayers.Marker(position,icon);
        map.addPopup(popup);
        markers.addMarker(marker);
    }

    // init();
</script>
</div>