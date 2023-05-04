<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['m_id']) && isset($_POST['u_id']) ) {
    
    if ($db->dbConnect()) {
         
        echo $db->removeReaction($_POST['m_id'], $_POST['u_id']);
        
    } 
    else echo "Error: Database connection";
} 
else echo "All fields are required";
?>
