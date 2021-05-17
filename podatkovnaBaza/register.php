<?php
if(isset($_POST['username']) && isset($_POST['password']) && isset($_POST['imeInPriimek']) && isset($_POST['mail'])){
    require_once "conn.php";
    require_once "validate.php";

    $username = validate($_POST['username']);
    $password = validate($_POST['password']);
    $imeInPriimek = validate($_POST['imeInPriimek']);
    $mail = validate($_POST['mail']);

    $sql = "insert into users values('', '$username', '". md5($password) ."', '$imeInPriimek', '$mail')";
    if($conn->query($sql)){
        echo "Registration sucessfull!";
    }
    else {
        echo "Registration failed!";
    }
}
?>