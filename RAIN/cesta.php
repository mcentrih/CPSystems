<?php
include_once('header.php');
include("nav.php");
include("showMap.php");
?>
<div style="height:400px; width: 400px; display: inline-block;">
    <?php
    global $conn;
    $sql = "select opis, lng, lat from cesta where FK_user like '" . $_SESSION["USER_ID"] . "'";
    $result = mysqli_query($conn, $sql);
    echo "<script>init();</script>";
    while ($row = $result->fetch_assoc()) {
        $lat = $row['lat'];
        $lng = $row['lng'];
        $opis = $row['opis'];

        echo "<p><strong>$opis</strong><span> $lat</span> <span>$lng</span></p>";
        echo "<script>cestaMarker($lat, $lng,  '$opis');</script>";
    }
    echo "<script>cestaMarker($lat, $lng, '$opis');</script>";
    echo "<br>";
    echo "<br>";
    echo "<br>";
    echo "</div>";
    ?>

    <?php
    include("footer.php");
    ?>