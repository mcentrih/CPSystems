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

if($id == "")
    $id = 0;

echo $id;
$tar_id = $id+1;

$name = "tablica";
$end = ".jpg";
$target_dir = "upload/";
$target_file = $target_dir . $name . strval($tar_id) . $end;
$tar = $name . strval($tar_id) . $end;
echo $tar_id;

echo "tu";

// Upload file
if (rename($name . $end, $target_file)) {
    // Insert record
    $query = "insert into tablice(name) values('" . $tar . "')";
    if ($conn->query($query)) {
        echo "dela";
    } else {
        echo "ne dela!";
        echo mysqli_error($conn);
    }
}
?>