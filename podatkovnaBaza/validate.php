<?php
function validate($data) {
    $data = trim($data);
    $data = stripslashes($data);    //odstrani poševnice
    $data = htmlspecialchars($data);    //odstrani posebne znake(proti napadom)
    return $data;
}
?>