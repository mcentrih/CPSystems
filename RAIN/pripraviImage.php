<?php
include_once('header.php');
include("nav.php");
global $conn;

$sql = "select id, image from tablice where FK_user like '" . $_SESSION["USER_ID"] . "' and id=(SELECT max(id) FROM tablice)";
$result = mysqli_query($conn, $sql);
$row = $result->fetch_assoc();
//echo $row['id'];
$b64 = $row['image'];
$output_file = "slika.jpg";
$ifp = fopen($output_file, 'wb');
$data = explode(',', $b64);
fwrite($ifp, base64_decode($data [0]));
fclose($ifp);
?>