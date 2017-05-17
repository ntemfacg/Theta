<?php
 // 1- connect to db

require("connection.php");

 // define quesry  //StartFrom

if ($usename=$_GET['op']==1) { // my following
	$query="select * from user_discussions where topic_id in (select topic_id from interested where user_id=". $_GET['user_id']. ") order by discussion_date DESC"." LIMIT 20". $_GET['StartFrom']  ; 
//or user_id=" . $_GET['user_id'] ."
//$query="select * from user_discussions where user_id in (select user_id from discussions where topic_id in (SELECT topic_id FROM interested where user_id=". $_GET['user_id'] . ")) or user_id=" . $_GET['user_id'] . " order by discussion_date DESC". 

//" LIMIT 20 OFFSET ". $_GET['StartFrom']  ;  // $usename=$_GET['username'];
}
//3ADCFF

elseif ($usename=$_GET['op']==2) { // specific person post
$query="select * from user_discussions where user_id=" . $_GET['user_id'] . " order by discussion_date DESC" . 

" LIMIT 20 OFFSET ". $_GET['StartFrom'] ;  // $usename=$_GET['username'];
}

elseif ($usename=$_GET['op']==3) { // search post
$query="select * from user_discussions where discussion_text like '%" . $_GET['query'] . 
"%' LIMIT 20 OFFSET ". $_GET['StartFrom'] ;  // $usename=$_GET['username'];
}

elseif ($usename=$_GET['op']==4){
	$query="select * from user_discussions where topic_id=". $_GET['topic_id']. " order by discussion_date DESC"." LIMIT 20". $_GET['StartFrom'] ;
}

$result=  mysqli_query($connect, $query);

if(! $result)
{ die("Error  in query");}

 //get data from database
 $output=array();
while($row=  mysqli_fetch_assoc($result))
{
	$output[]=$row;  //$row['id']
}

 if ($output) {
print( "{'msg':'has discussions'". ",'info':'". json_encode($output) ."'}");// this will print the output in json
 }

 else{
 	print("{'msg':'no discussions'}");
 }

print(json_encode($output));// this will print the output in json

// 4 clear
mysqli_free_result($result);
//5- close connection
mysqli_close($connect);
?>