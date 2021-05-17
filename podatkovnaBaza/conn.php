<?php
$server = "localhost";
$username = "root";
$password = "";
$database = "projekt";
$conn = new mysqli($server, $username, $password, $database);
if(conn->connect_error){
    die("Connection to database failed!", $conn->connect_error);
}
?>