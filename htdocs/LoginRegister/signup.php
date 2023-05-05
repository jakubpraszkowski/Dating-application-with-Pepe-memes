<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("users", $_POST['username'], $_POST['password'])) {
            echo "Zarejestrowano";
        } else echo "Nieprawidłowo wypełnione pola";
    } else echo "Błąd połączenia z bazą danych";
} else echo "Wszystkie pola są wymagane!";
?>
