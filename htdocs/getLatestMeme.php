<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['cat_id']) && isset($_POST['u_id'])) 
{
    if ($db->dbConnect()) {
         
        echo $db->getLatestMeme($_POST['cat_id'], $_POST['u_id']);
        
    } 
    else echo "Error: Database connection";
   
} 
else echo "All fields are required";
?>
