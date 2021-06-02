<div id="demoMap" style="height:400px; width: 400px;"></div>
<script src="OpenLayers.js"></script>
<script>
    map = new OpenLayers.Map("demoMap");
    mapnik = new OpenLayers.Layer.OSM();
    fromProjection = new OpenLayers.Projection("EPSG:4326");   // Transform from WGS 1984
    toProjection = new OpenLayers.Projection("EPSG:900913"); // to Spherical Mercator Projection
    position = new OpenLayers.LonLat(15.3527785, 46.167495).transform(fromProjection, toProjection);
    zoom = 10;

    markers = new OpenLayers.Layer.Markers( "Markers" );
    map.addLayer(markers);

    size = new OpenLayers.Size(21,25);
    offset = new OpenLayers.Pixel(-(size.w/2), -size.h);
    icon = new OpenLayers.Icon("here.png", size, offset);
    markers.addMarker(new OpenLayers.Marker(position,icon));

    map.addLayer(mapnik);
    map.setCenter(position, zoom);
</script>
</div>