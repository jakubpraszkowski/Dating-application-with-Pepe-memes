<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if ($db->dbConnect()) {

    echo $db->getCategories();

}
else echo "Error: Database connection";
?>
