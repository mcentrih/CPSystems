<?php
include_once('header.php');
include("nav.php");

function check_credentials($username, $password)
{
    global $conn;
    $username = mysqli_real_escape_string($conn, $username);
    $pass = password_hash($password, PASSWORD_DEFAULT); //uporabi ce dodaš kodiranje
    $query = "SELECT * FROM users  WHERE username='$username' AND password='$pass'";
    $res = $conn->query($query);
    if ($user_obj = $res->fetch_object()) {
        $_SESSION["fullname"] = $user_obj->fullname;
        return $user_obj->id;
    }
    return -1;
}

$error = "";
if (isset($_POST["poslji"])) {
    //preverjanje podatkov za prijavo
    if (($id = check_credentials($_POST["user"], $_POST["pass"])) >= 0) {
        //prijava uporabnika in preusmeritev
        $_SESSION["USER_ID"] = $id;
        header("Location: index.php");
        die();
    } else {
        echo "Login unsuccessful!";
    }
}
?>
    <div  style="height:60%; margin-bottom: 20px; overflow-y: scroll;">
        <div style="border: 5px solid black; background-color: #21364c; width: 50%; margin: 0 auto;padding-top: 30px; padding-bottom: 20px "
             class="form-group">
            <h2 style="text-align: center;">Prijavno okno</h2>
            <form style="padding-left: 38%;" action="login.php" method="POST">
                <p>Uporabniško ime: </p><input class="form-control" style="width: 250px" type="text" name="user"/>
                <p>Geslo: </p><input class="form-control" style="width: 250px" type="password" name="pass"/><br><br>
                <input type="submit" class="btn btn-success" name="poslji" value="Prijava" style="margin-left: 80px">
            </form>
        </div>
    </div>
<?php
include_once('footer.php');
?>