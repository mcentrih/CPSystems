<?php
$dom = file_get_contents("https://www.promet.si/dc/b2b.dogodki.rss?language=sl_SI&eventtype=incidents", false);
if (!empty($dom))
    echo $dom;
?>