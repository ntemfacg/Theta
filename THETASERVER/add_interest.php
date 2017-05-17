<?php
 // 1- connect to db
require("connection.php");
 // define quesry 
if($_GET['op']==1) //op=1 add, op=2 delete

// add folllowing

{$query="insert into interested(user_id,topic_id) values (" . $_GET['user_id']. ","  . $_GET['topic_id'] . ")";  // $usename=$_GET['username'];

}

else{ // remove following

$query="delete from interested where user_id=" . $_GET['user_id']. " and topic_id="  . $_GET['topic_id']  ; 	

}
$result=  mysqli_query($connect, $query);

if(! $result)

{$output ="{'msg':'fail'}";
}

else {
$output ="{'msg':'updated'}";
}

print( $output);// this will print the output in json

//5- close connection

mysqli_close($connect);
?>