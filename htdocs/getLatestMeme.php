<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['cat_id'])) 
{
    if ($db->dbConnect()) {
         
        echo $db->getLatestMeme($_POST['cat_id']);
        
    } 
    else echo "Error: Database connection";
   
} 
else echo "All fields are required";
?>
