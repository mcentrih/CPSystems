<?php
session_start();
//Seja poteče po 30 minutah - avtomatsko odjavi neaktivnega uporabnika
if (isset($_SESSION['LAST_ACTIVITY']) && time() - $_SESSION['LAST_ACTIVITY'] < 1800) {
    session_regenerate_id(true);
}
$_SESSION['LAST_ACTIVITY'] = time();

//Poveži se z bazo
$conn = new mysqli('localhost', 'root', '', 'projektdb');
//Nastavi kodiranje znakov, ki se uporablja pri komunikaciji z bazo
$conn->set_charset("UTF8");

?>
    <html>
    <head>
        <title>Projekt CPSystems</title>
        <link rel="icon" href="favicon.ico">
        <!-- CSS only -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0"
              crossorigin="anonymous">
        <!-- JavaScript Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8"
                crossorigin="anonymous"></script>
    </head>
    <style>
        .dot {
            height: 15px;
            width: 15px;
            background-color: greenyellow;
            border-radius: 50%;
            display: inline-block;
        }
    </style>
<body style="background-color: #375a7f; color: white;">

<?php
if (isset($_SESSION["USER_ID"])) {
    ?>
    <div style="background-color: #21364c; margin-top: 56px; margin-bottom: 20px; padding-top: 20px; border-bottom: 5px solid black;">
        <img class="rounded" style="display: block; margin-left: auto; margin-right: auto;padding-top: 5px;"
             src="icon.png" width="128"
             height="128">
        <h1 style="text-align: center">Projekt prepoznava tablic in cestišča (2. letnik),<br>steganografija, paralelno in porazdeljeno obdelovanje podatkov, grafika in mikrokrmilniki (3. letnik)</h1>
        <div style="display: inline-block; padding-left: 45%">
            <p>Pozdravljeni, <strong><?php echo $_SESSION["fullname"] ?></strong></p>
        </div>
    </div>
    <?php
} else {
    ?>
    <div style="background-color: #21364c; margin-top: 56px; margin-bottom: 20px; padding-top: 20px; padding-bottom: 20px; border-bottom: 5px solid black;">
        <img style="display: block; margin-left: auto; margin-right: auto;padding-top: 5px;" src="icon.png" width="128"
             height="128">
        <h1 style="text-align: center">Projekt prepoznava tablic in cestišča (2. letnik),<br>steganografija, paralelno in porazdeljeno obdelovanje podatkov, grafika in mikrokrmilniki (3. letnik)</h1>
    </div>
    <?php
}
?>