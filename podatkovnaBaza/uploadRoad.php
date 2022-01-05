<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
    $image = $_POST['image'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];
    $opis = $_POST['opis'];
    require_once('dbConnectImage.php');
    echo $image;
    
    //get FK_user
    $sql = "INSERT INTO cesta VALUES (DEFAULT, '$image', '$latitude' , '$longitude', '$opis', '1')";
    if($con -> query($sql)){
        echo "Working!";
        return true;
    }
    else {
        echo "Not working!";
        echo mysqli_error($con);
        return false;
    }
    
    $check = mysqli_stmt_affected_rows($stmt);
    
    if($check == 1){
        echo "Road info uploaded Successfully";
    }
    else{
        echo "Error uploading road info!";
    }
    mysqli_close($con);
    }
    else{
    echo "Error";
 }