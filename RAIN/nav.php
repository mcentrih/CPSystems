<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
    <div class="container-fluid">
        <a class="navbar-brand" href="index.php">NAVIGACIJA</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDropdown"
                aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDropdown">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="index.php">Domov</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Infrastruktura</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#">Tablice</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button"
                       data-bs-toggle="dropdown" aria-expanded="false">
                        APIs in ostalo
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                        <li><a class="dropdown-item" href="#">Action</a></li>
                        <li><a class="dropdown-item" href="#">Another action</a></li>
                        <li><a class="dropdown-item" href="#">Something else here</a></li>
                    </ul>
                </li>
                <?php
                if (isset($_SESSION["USER_ID"])) {
                    ?>
                    <li class="" style="padding-left: 290%">
                        <a role="button" class="btn btn-danger" href="logout.php">Odjava</a>
                    </li>
                    <?php
                } else {
                    ?>
                    <li class="" style="padding-left: 290%">
                        <a role="button" class="btn btn-primary" href="login.php">Prijava</a>
                    </li>
                    <?php
                }
                ?>
            </ul>
        </div>
    </div>
</nav>