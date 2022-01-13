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
$target_file = $target_dir . $name . strval($id) . $end;
$tar = $name . strval($id) . $end;
//echo $tar_id;
//echo "  ";

$lon = 46.5590392;
$lat = 15.6358848;

$img = file_get_contents($name . $end);
$data = base64_encode($img);

echo "tu";
echo "  ";

$myfile = fopen("tablica_text.txt", "r") or die("Unable to open file!");
$tablicaTxt = fread($myfile,filesize("tablica_text.txt"));
fclose($myfile);

// Upload file
if (rename($name . $end, $target_file)) {
    // Insert record
//    $query = "insert into tablice(id, name, tablica/*, lat, lng, FK_user*/) values('".$id."','" . $tar . "','" . $data . "')";
//    ,'" . $lat . "','" . $lon . "','" . $_SESSION["USER_ID"] . "')";
    $query = "UPDATE tablice SET name = '" . $tar . "', tablica = '" . $data . "', tablica_txt = '" . $tablicaTxt . "' WHERE id = '" . $id . "'";
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
if (file_exists("slika.jpg")) {
    unlink("slika.jpg");
}
include("primaryBtns.php");

include("footer.php");

?>