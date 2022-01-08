<?php
include("header.php");
include("nav.php");
?>

<div style="height:400px; width: 800px; display: inline-block; padding-left:15px; font-size:20px;">
<ul style="list-style-type:none;">
        <li><strong>Strežnik (RAIN)  </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Baza (RAIN)  </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Mobilna aplikacija (URRI)   </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Skripta prepoznava tablic (ORV)  </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Skripta C# vmesnik (NRS)   </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Skripta skrivanje podatkov (RV)  </strong><span class="dot"></span> running</li>
        <br>
        <li><strong>Skripta iskanje podatkov (RV)  </strong><span class="dot"></span> running</li>
    </ul>
</div>
    
<div style="height:400px; width: 1000px; display: inline-block; float: right;">
    <?php
        include_once('tabButtons.php');
    ?>
</div>

<?php
include("footer.php");
?>