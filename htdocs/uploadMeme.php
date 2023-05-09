<?php

require "LoginRegister/DataBase.php";

if($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_FILES['file']) && isset($_POST['u_id']) && isset($_POST['cat_id']) && isset($_POST['title'])) {    

    $db = new DataBase();

    if ($db->dbConnect()) {        
        $file = $_FILES['file'];

        // Get file properties
        $file_name = $file['name'];
        $file_tmp = $file['tmp_name'];
        $file_size = $file['size'];
        $file_error = $file['error'];
    
        // Get file extension
        $file_ext = explode('.', $file_name);
        $file_ext = strtolower(end($file_ext));
    
        // Allowed file types
        $allowed = array('jpg', 'png', 'jpeg','gif');
    
        // Check if the file type is allowed
        if(in_array($file_ext, $allowed)) {
            // Check for errors
            if($file_error === 0) {
                // Check file size
                if($file_size <= 2097152) {
                    // File name
                    $file_name_new = uniqid('', true) . '.' . $file_ext;
    
                    // File destination
                    $file_destination = 'img/' . $file_name_new;
                    $localurl = 'https://meme-dating.one.pl/'. $file_destination;
    
                    // Move the file Tu jest git ten plik
                    if(move_uploaded_file($file_tmp, $file_destination)) {
                        
                        
                        $db->sql = "INSERT INTO memes VALUES(null, '".$localurl."', '".$_POST['title']."', ".$_POST['cat_id'].", ".$_POST['u_id'].", null);";
                        echo $db->sql;
                        // if (mysqli_query($db->connect, $db->sql)) {
                        //     header("Content-Type: application/json");
                        //     echo json_encode(["success" => true]);
                        // } else {
                        //     header("Content-Type: application/json");
                        //     echo json_encode(["success" => false, "message" => "BŁĄD! Nie można przesłać zapytania!."]);
                        // }
                        
                    } else {
                        header("Content-Type: application/json");
                        echo json_encode(["success" => false, "message" => "BŁĄD! Plik nie został przesłany!."]);
                    }
                } else {
                    header("Content-Type: application/json");
                    echo json_encode(["success" => false, "message" => "Rozmiar pliku musi być mniejszy od 2MB."]);
                }
            } else {
                header("Content-Type: application/json");
                echo json_encode(["success" => false, "message" => "Wystąpił problem z przesłaniem pliku!"]);
            }
        } else {
            header("Content-Type: application/json");
            echo json_encode(["success" => false, "message" => "Nieobsługiwany typ pliku!"]);
        }
        } else {
        header("Content-Type: application/json");
        echo json_encode(["success" => false, "message" => "Error: Database connection."]);
    }

} else {
    
    header("Content-Type: application/json");
    echo json_encode(["success" => false, "message" => "Invalid request"]);
}
?>
