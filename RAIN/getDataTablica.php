<?php
//Poveži se z bazo
$conn = new mysqli('localhost', 'root', '', 'projektdb');
//Nastavi kodiranje znakov, ki se uporablja pri komunikaciji z bazo
$conn->set_charset("UTF8");

global $conn;

$result = $conn->query("SELECT max(id) AS 'id' FROM tablice WHERE FK_user = 1");

if (!$result) {
    die('Could not query:' . mysqli_error($conn));
}

$row = mysqli_fetch_array($result);
$id = $row['id'];


$result = $conn->query("SELECT tablica_txt AS 'txt' FROM tablice WHERE id = " . $id . "");

if (!$result) {
    die('Could not query:' . mysqli_error($conn));
}

$row = mysqli_fetch_array($result);
$txt = $row['txt'];
echo $txt;

?>