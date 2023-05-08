<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['u_id']) && isset($_POST['cat_id']) && isset($_POST['lastMemeId']) && isset($_POST['count'])) 
{
    if ($db->dbConnect()) {
         
        echo $db->getNewMemes($_POST['u_id'], $_POST['cat_id'], $_POST['lastMemeId'], $_POST['count']);
        
    } 
    else echo "Error: Database connection";
   
} 
else echo "All fields are required";
?>
