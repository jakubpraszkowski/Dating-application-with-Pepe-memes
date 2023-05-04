<?php
require "LoginRegister/DataBase.php";
$db = new DataBase();
if (isset($_POST['m_id']) && isset($_POST['u_id']) && isset($_POST['reaction']) ) {
    
    if ($db->dbConnect()) {
         
        echo $db->addReaction($_POST['m_id'], $_POST['u_id'], $_POST['reaction']);
        
    } 
    else echo "Error: Database connection";
} 
else echo "All fields are required";
?>
