<?php
include_once('header.php');
include("nav.php");
global $conn;

$result = $conn->query("SELECT max(id) as 'id' FROM tablice");

if (!$result) {
    die('Could not query:' . mysqli_error($conn));
}

$row = mysqli_fetch_array($result);
$id = $row['id'];

if ($id == "")
    $id = 0;

echo $id;
echo "  ";
$tar_id = $id + 1;

$name = "tablica";
$end = ".jpg";
$target_dir = "upload/";
$target_file = $target_dir . $name . strval($tar_id) . $end;
$tar = $name . strval($tar_id) . $end;
echo $tar_id;
echo "  ";

$lon = 46.5590392;
$lat = 15.6358848;

echo "tu";
echo "  ";

// Upload file
if (rename($name . $end, $target_file)) {
    // Insert record
    $query = "insert into tablice(name, tablica, lat, lng, FK_user) values('" . $name . strval($tar_id) . "','" . $tar . "','" . $lat . "','" . $lon . "','" . $_SESSION["USER_ID"] . "')";
    if ($conn->query($query)) {
        echo "dela";
        echo "  ";

    } else {
        echo "ne dela!";
        echo "  ";

        echo mysqli_error($conn);
    }
}
echo "<br>";
include("primaryBtns.php");

include("footer.php");

?>