<?php
include_once('header.php');
include("nav.php");
$username = NULL;
$password = NULL;
$email = NULL;
$fullname = NULL;

//metoda ki preveri ce username ze obstaja
function check_username($username)
{
    global $conn;
    $username = mysqli_real_escape_string($conn, $username);
    $query = "SELECT * FROM users WHERE username='$username'";
    $res = $conn->query($query);
    return mysqli_num_rows($res) > 0;
}

//metoda ki preveri podatke in jih vpise ce so pravilni
function register($usern, $pass)
{
    global $conn;
    $username = mysqli_real_escape_string($conn, $usern);
    $password = password_hash($pass);        //$_POST["registracijaGeslo"];
    $email = $_POST["regMail"];
    $fullname = $_POST["regName"];

    $query = "INSERT INTO users VALUES(NULL,'$fullname', '$username', '$password', '$email')";
    if ($conn->query($query)) {
        return true;
    } else {
        echo "Napaka pri dodajanju uporabnika";
        echo mysqli_error($conn);
        return false;
    }
}

if (isset($_POST["poslji"])) {
    if ($_POST["regPass"] != $_POST["regPass2"])
        echo "Gesli se ne ujemata.";
    else if (check_username($_POST["regUser"]))
        echo "Uporabniško ime je že zasedeno.";
    else if (register($_POST["regUser"], $_POST["regPass"])) {
        header("Location: login.php");
        die();
    } else
        echo "Prišlo je do napake med registracijo uporabnika.";
}

?>
    <div style="height:60%; margin-bottom: 20px; overflow-y: scroll;">
        <div style="border: 5px solid black; background-color: #21364c; width: 50%; margin: 0 auto;padding-top: 30px; padding-bottom: 20px "
             class="form-group">
            <h2 style="text-align: center;">Registracija</h2>
            <form style="padding-left: 38%;" class="form-group" style="width: 250px" action="register.php" method="POST">
                <p>Uporabniško ime: </p><input class="form-control" style="width: 250px" type="text" name="regUser"/><br>
                <p>Geslo: </p><input class="form-control" style="width: 250px" type="password" name="regPass"/><br>
                <p>Ponovi geslo: </p><input class="form-control" style="width: 250px" type="password" name="regPass2"/><br>
                <p>Elektronska posta</p><input class="form-control" style="width: 250px" type="email" name="regMail"/><br>
                <p>Ime in Priimek: </p><input class="form-control" style="width: 250px" type="text" name="regName"/><br><br>
                <input type="submit" name="poslji" value="Registriraj"
                       style="width: 150px; height: 40px; margin-left: 15px;margin-top: 20px;" class="btn btn-success"/>
            </form>
        </div>
    </div>
<?php
include_once('footer.php');
?>