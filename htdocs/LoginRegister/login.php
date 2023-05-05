<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("users", $_POST['username'], $_POST['password'])) {
            echo "Zalogowano";
        } else echo "Błędny login lub hasło";
    } else echo "Błąd połączenia z bazą danych";
} else echo "Wszystkie pola są wymagane!";
?>
