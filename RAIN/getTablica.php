<?php
include_once('header.php');
include("nav.php");
global $conn;
$sql = "select name from tablice";
$result = mysqli_query($conn, $sql);

while ($row = $result->fetch_assoc()) {
    $image = $row['name'];
    $image_src = "upload/" . $image;

    ?>
    <img src='<?php echo $image_src; ?>'>
    <?php
}
?>