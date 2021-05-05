<?php
    session_start();
    //Seja poteče po 30 minutah - avtomatsko odjavi neaktivnega uporabnika
    if (isset($_SESSION['LAST_ACTIVITY']) && time() - $_SESSION['LAST_ACTIVITY'] < 1800) {
        session_regenerate_id(true);
    }
    $_SESSION['LAST_ACTIVITY'] = time();

    //Poveži se z bazo
    $conn = new mysqli('localhost', 'root', '', 'projekt');
    //Nastavi kodiranje znakov, ki se uporablja pri komunikaciji z bazo
    $conn->set_charset("UTF8");
?>
<html>
<head>
    <title>Projekt CPSystems</title>
</head>
<body style="background-color: gray; margin-top: 0">
<div style="background-color: lightgray; padding: 0 10px 30px; border: 1px solid black; border-bottom: 5px solid black; margin-bottom: 10px">
    <h1 style="text-align: center" ">Projekt prepoznava tablic in cestišča</h1>
    <nav style="text-align: center">
        <ul>
            <li style="display: inline; float: left"><a href="index.php">Domov</a></li>
            <?php
            //spreminjanje menija ce si prijavljen ali ne
            if (isset($_SESSION["USER_ID"])) {
                ?>
                <li style="display: inline; float: right; padding-right: 10px"><a href="logout.php">Odjava</a></li>
                <?php
            } else {
                ?>
                <li style="display: inline; float: right; padding-right: 10px"><a href="login.php">Prijava</a></li>
                <?php
            }
            ?>
        </ul>
    </nav>
</div>
<!--<hr style="border-color: black; background-color: lightgray">-->
