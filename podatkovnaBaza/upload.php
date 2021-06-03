<?php
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 
    $image = $_POST['image'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    require_once('dbConnectImage.php');
    echo $image;
    
    $sql = "INSERT INTO tablice ('image', 'lat', 'lng') VALUES ('$image', '$lat', '$lng')";
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