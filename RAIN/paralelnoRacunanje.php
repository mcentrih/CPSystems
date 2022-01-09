<?php
include("header.php");
include("nav.php");

?>
    <div style=" height:58%; margin-bottom: 20px;">
<?php

?>

<div style="height:400px; width: 800px; display: inline-block; padding-left:15px;">
    <?php
        include_once('tabButtons.php');
    ?>
    <br>
    <br>

    <form method="post">
        <input type="submit" role="button" class="btn btn-primary" value="Poženi MPI program" name="mpiStart"/>
        <?php 
            if(array_key_exists('mpiStart', $_POST)) {
                mpiStart();
            }
            function mpiStart()
            {
                $command = escapeshellcmd('mpiexec -n 3 C:\xampp\htdocs\CPSystems\PIPR\projektnaPIPR\Debug\projektnaPIPR.exe C:\xampp\htdocs\CPSystems\PIPR\projektnaPIPR\Debug\prebrano.json');
                $output = shell_exec($command);
                echo $output;
            }
        ?>
    </form>
</div>

<?php
include("footer.php");
?>