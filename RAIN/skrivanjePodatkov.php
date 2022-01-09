<?php
include_once('header.php');
include("nav.php");


if(array_key_exists('shraniSlikoNaPC', $_POST)) {
    shraniSlikoNaPC();
}

if(array_key_exists('shraniPodatkeNaPC', $_POST)) {
    shraniPodatkeNaPC();
}

if(array_key_exists('skrijPodatke', $_POST)) {
    skrijPodatke();
}



function skrijPodatke()
{
    #$command = escapeshellcmd('C:\xampp\htdocs\CPSystems\ORV\prepoznavaTabliceZan.py C:\xampp\htdocs\CPSystems\RAIN\slika.jpg');
    $command = escapeshellcmd('py C:\xampp\htdocs\CPSystems\RV\skrijBesedilo.py');
    shell_exec($command);
    #$output = shell_exec($command);
    #echo $output;
}



function shraniSlikoNaPC()
{
    global $conn;
    $sql = "select id, image from cesta where FK_user like '" . $_SESSION["USER_ID"] . "' and id=(SELECT max(id) FROM cesta)";
    $result = mysqli_query($conn, $sql);
    $row = $result->fetch_assoc();
    //echo $row['id'];
    $b64 = $row['image'];
    $output_file = "C:\\xampp\\htdocs\\CPSystems\\RV\\cistaSlika.png";
    $ifp = fopen($output_file, 'wb');
    $data = explode(',', $b64);
    fwrite($ifp, base64_decode($data [0]));
    fclose($ifp);
}

function shraniPodatkeNaPC()
{
    global $conn;
    $sql = "select id, lat, lng, opis from cesta where FK_user like '" . $_SESSION["USER_ID"] . "' and id=(SELECT max(id) FROM cesta)";
    $result = mysqli_query($conn, $sql);
    $row = $result->fetch_assoc();
    //echo $row['id'];
    $lat = $row['lat'];
    $lng = $row['lng'];
    $opis = $row['opis'];
    $output_file = "C:\\xampp\\htdocs\\CPSystems\\RV\\podatki.txt";
    $ifp = fopen($output_file, 'wb');  
    $data = "( " . $lat . " | " . $lng . " ) " . $opis;
    fwrite($ifp, $data);
    fclose($ifp);
}

?>

<div style="height:400px; width: 800px; display: inline-block; padding-left:15px;">
    <?php 
            if(array_key_exists('najdiPodatke', $_POST)) {
                najdiPodatke();
            }

            function najdiPodatke()
            {
                $command = escapeshellcmd('py C:\xampp\htdocs\CPSystems\RV\najdiBesedilo.py');
                shell_exec($command);
                echo "<br>";
                echo file_get_contents("C:\\xampp\\htdocs\\CPSystems\\RV\\odkodiraniPodatki.txt");
                echo "<br>";
            }
    ?>
    <img src="../RV/cistaSlika.png" alt='slika' width='600px' height='300px'>
    <p><object width="800" height="200" data="../RV/podatki.txt"></object></p>
    
</div>
    
<div style="height:400px; width: 1000px; display: inline-block; float: right;">
    <?php
        include_once('tabButtons.php');
    ?>
    <br>
    <br>

    <form method="post">
        <input type="submit" role="button" class="btn btn-primary" value="Shrani sliko na računalnik" name="shraniSlikoNaPC"/>
    </form>
    
    <form method="post">
        <input type="submit" role="button" class="btn btn-primary" value="Shrani podatke na računalnik" name="shraniPodatkeNaPC"/>
    </form>
    
    <form method="post">
        <input type="submit" role="button" class="btn btn-primary" value="Skrij podatke" name="skrijPodatke"/>
    </form>

    <form method="post">
        <input type="submit" role="button" class="btn btn-primary" value="Najdi podatke" name="najdiPodatke"/>
    </form>
</div>

<?php
include("footer.php");
?>