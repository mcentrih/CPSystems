<?php
include("header.php");
include("nav.php");
include("showMap.php");
include("pripraviImage.php");

//$command = escapeshellcmd('C:\xampp\htdocs\CPSystems\ORV\prepoznavaTabliceMatjaz.py C:\xampp\htdocs\CPSystems\ORV\passat2.jpg');
//$output = shell_exec($command);
//echo $output;

?>
    <div style=" height:58%; margin-bottom: 20px;">
<?php
if(array_key_exists('findT', $_POST)) {
    findTablica();
}
function findTablica()
{
    $command = escapeshellcmd('C:\wamp64\www\CPSystems\ORV\prepoznavaTabliceMatjaz.py C:\wamp64\www\CPSystems\RAIN\slika.jpg');
    $output = shell_exec($command);
    echo $output;
}

echo "<br>";
echo "<br>";
echo "<script>init();</script>";
include("primaryBtns.php");
?>
<?php
include("footer.php");
?>