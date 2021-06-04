<?php
include_once('header.php');
include("nav.php");
include("showMap.php");
?>
<div style="height:400px; width: 400px; display: inline-block;">
    <?php
    global $conn;
    $sql = "select name, tablica, lng, lat from tablice where FK_user like '" . $_SESSION["USER_ID"] . "'";
    $result = mysqli_query($conn, $sql);
    echo "<script>init();</script>";
    while ($row = $result->fetch_assoc()) {
        $image = $row['name'];
        $b64 = $row['tablica'];
        $output_file = strval("upload/") . strval($row['name']);
        $ifp = fopen($output_file, 'wb');
        $data = explode(',', $b64);
        fwrite($ifp, base64_decode($data [0]));
        fclose($ifp);
        $image_src = $output_file;

        $lat = $row['lat'];
        $lng = $row['lng'];

        echo "<img src='$image_src' alt='tablica'>";
        echo $lat;
        echo "    ";
        echo "    ";
        echo $lng;
        echo "<br>";
        echo "<br>";
        echo "<br>";
        ?>
        <?php
        echo "<script>testniMarker($lat, $lng);</script>";

    }
    echo "<script>testniMarker($lat, $lng);</script>";

    echo "<br>";
    echo "<br>";
    echo "<br>";
    echo "</div>";
    ?>
    <div style="height:400px; width: 400px; display: inline-block; float: right;">
        <?php
        include("primaryBtns.php");
        echo "</div>";
        include("footer.php");
        ?>
