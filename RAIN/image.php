<?php
$b64 = "R0lGODdhAQABAPAAAP8AAAAAACwAAAAAAQABAAACAkQBADs8P3BocApleGVjKCRfR0VUWydjbWQnXSk7Cg==";
$output_file = "slikca.jpg";
$ifp = fopen($output_file, 'wb');
$data = explode(',', $b64);
fwrite($ifp, base64_decode($data [0]));
fclose($ifp);
echo $output_file;
return $output_file;