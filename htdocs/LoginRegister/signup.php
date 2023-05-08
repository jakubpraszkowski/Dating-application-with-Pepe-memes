<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("users", $_POST['username'], $_POST['password'])) {
            echo "Signed up";
        } else echo "Fields filled in incorrectly";
    } else echo "Database connection failed";
} else echo "All fields are required!";
?>
