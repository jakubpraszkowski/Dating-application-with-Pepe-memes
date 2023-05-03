<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['cat_id']) && isset($_POST['lastMemeId'])) 
{
    if ($db->dbConnect()) {
         
        echo $db->getNewMeme($_POST['cat_id'], $_POST['lastMemeId']);
        
    } 
    else echo "Error: Database connection";
   
} 
else echo "All fields are required";
?>
