<?php

 // 1- connect to db

$host="localhost";

$user="root";

$password="";

$database="theta";

$connect=  mysqli_connect($host, $user, $password, $database);

if(mysqli_connect_errno())

{ die("cannot connect to database field:". mysqli_connect_error());   }
?>