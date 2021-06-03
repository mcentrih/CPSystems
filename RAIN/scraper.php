<?php
$ch = curl_init();
curl_setopt($ch, CURLOPT_URL, "https://www.promet.si/dc/b2b.dogodki.rss?language=sl_SI&eventtype=incidents");
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
$html = curl_exec($ch);
$dom = new DOMDocument();
@ $dom->loadHTML($html);
//$titles = $dom->get("span");
echo $html;
//$titles_array = array();
//foreach ($titles as $title){
//    $title_text = $title->textContent;
//    $titles_array = $title_text;
//    echo $title_text . '<br>';
//}