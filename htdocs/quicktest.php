<?php
$mysqli = new mysqli("localhost","memeapp","qIxg2edPN1Jujz8BfmFSyA7hmJ0dCG","meme_dating");

if ($mysqli -> connect_errno) {
  echo "Failed to connect to MySQL: " . $mysqli -> connect_error;
  exit();
}

$sql = "SELECT * FROM users";
$result = $mysqli -> query($sql);

// Associative array
$row = $result -> fetch_assoc();
var_dump($row);
//printf ("%s (%s)\n", $row["Lastname"], $row["Age"]);

// Free result set
$result -> free_result();

$mysqli -> close();
?>
