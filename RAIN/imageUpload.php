<?php
$img = file_get_contents("slikca.jpg");
$data = base64_encode($img);
echo $data;