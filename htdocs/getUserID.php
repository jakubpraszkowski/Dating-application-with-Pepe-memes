<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['u_id'])) 
{
    if ($db->dbConnect()) {
         
        echo $db->getUserID($_POST['username']);        
    } 
    else echo "Error: Database connection";   
} 
else echo "All fields are required";
?>