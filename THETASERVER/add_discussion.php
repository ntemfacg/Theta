<?php
 // 1- connect to db

require("connection.php");
 // define quesry 
//print($_GET['discussions_picture'] );
$query="insert into discussions(user_id,topic_id,discussion_text,discussion_picture) values (" . $_GET['user_id']. ",'"  . $_GET['topic_id'] . "','"  . $_GET['discussion_text'] . "','".  $_GET['discussion_picture'] .  "')";  // $usename=$_GET['username'];

$result=  mysqli_query($connect, $query);

if(! $result)
{$output ="{'msg':'fail'}";
}

else {
$output ="{'msg':'discussion added'}";
}

print( $output);// this will print the output in json

//5- close connection
mysqli_close($connect);
?>