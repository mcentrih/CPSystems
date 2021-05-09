<?php
session_start(); //Naloži sejo
session_unset(); //Odstrani sejne spremenljivke
session_destroy(); //Uniči sejo
header("Location: login.php"); //Preusmeri na index.php
?>