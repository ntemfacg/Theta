<?php
 // 1- connect to db

require("connection.php");
 // define quesry 

$query="SELECT * FROM topics WHERE 1";  // $usename=$_GET['username'];

$result=  mysqli_query($connect, $query);

if(! $result)

{ die("Error in query");}
 //get data from database

 $output=array();
while($row=  mysqli_fetch_assoc($result))
{
	$output[]=$row;  //$row['id']
	//break;
}

 if ($output) {
print( "{'msg':'pass'". ",'info':'". json_encode($output) ."'}");// this will print the output in json
 }

 else{
 	print("{'msg':'error'}");
 }

 //print (json_encode($output));

// 4 clear
mysqli_free_result($result);
//5- close connection
mysqli_close($connect);
?>