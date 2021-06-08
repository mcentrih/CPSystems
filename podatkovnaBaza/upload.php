<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
    $image = $_POST['image'];
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];
    require_once('dbConnectImage.php');
    echo $image;
    
    //get FK_user
    $sql = "INSERT INTO tablice VALUES (DEFAULT, NULL ,'$image', NULL, '$latitude', '$longitude', '12')";
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
        echo "Image Uploaded Successfully";
    }
    else{
        echo "Error Uploading Image";
    }
    mysqli_close($con);
    }
    else{
    echo "Error";
 }