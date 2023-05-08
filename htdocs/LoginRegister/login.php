<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("users", $_POST['username'], $_POST['password'])) {
            echo "Logged in";
        } else echo "The username or password is incorrect";
    } else echo "Database connection error";
} else echo "All fields are required!";
?>
