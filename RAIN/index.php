<?php
include("header.php");
include("nav.php");
//$command = escapeshellcmd('C:\xampp\htdocs\CPSystems\ORV\prepoznavaTabliceMatjaz.py C:\xampp\htdocs\CPSystems\ORV\passat2.jpg');
//$output = shell_exec($command);
//echo $output;

?>
<div style="overflow-y: scroll; height:58%; margin-bottom: 20px;">
    <?php
    $command = escapeshellcmd('C:\wamp64\www\CPSystems\ORV\prepoznavaTabliceMatjaz.py C:\wamp64\www\CPSystems\ORV\passat.jpg');
    $output = shell_exec($command);
    echo $output;
    echo "<br>";
    include("scraper.php");

    ?>
</div>

<?php
include("footer.php");
?>