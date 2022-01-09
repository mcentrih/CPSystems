<?php
include_once('header.php');
include("nav.php");
include("showMap.php");
?>
<div style="height:400px; width: 500px; display: inline-block;">
    <?php
    global $conn;
    $sql = "select image, opis, lng, lat from cesta where FK_user like '" . $_SESSION["USER_ID"] . "'";
    $result = mysqli_query($conn, $sql);
    echo "<script>init();</script>";
    while ($row = $result->fetch_assoc()) {
        $lat = $row['lat'];
        $lng = $row['lng'];
        $opis = $row['opis'];
        $b64 = $row['image'];
        echo "<p><img src='data:image/png;base64,$b64' alt='slika' width='100px' height='100px'>     <strong>$opis</strong><span> $lat</span> <span>$lng</span></p>";
        echo "<script>cestaMarker($lat, $lng,  '$opis');</script>";
    }
    echo "<br>";
    echo "<br>";
    echo "<br>";
    echo "</div>";
    ?>
    <div style="height:400px; width: 600px; display: inline-block; float: right;">
<?php
include("primaryBtns.php");
echo "</div>";
include("footer.php");
?>