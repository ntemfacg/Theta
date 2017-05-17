<?php
 // 1- connect to db
require("connection.php");

 // define query 
$query="insert into login(first_name,second_name,email,mobile_phone,picture_path,password) values ('" . $_GET['first_name']. "','" . $_GET['second_name']. "','"  . $_GET['email'] . "','" . $_GET['mobile_phone']. "','"  . $_GET['picture_path'] . "','"  . $_GET['password'] . "')";  // $usename=$_GET['username'];

$result=  mysqli_query($connect, $query);
if(! $result)

{$output ="{'msg':'fail'}";
}

else {
$output ="{'msg':'user is added'}";
}

print( $output);// this will print the output in json

//5- close connection
mysqli_close($connect);
?>