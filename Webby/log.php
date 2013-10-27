<?php
include 'db.php';

// Get phone number
$phone = isset( $_REQUEST['phone'] ) ? $_REQUEST['phone'] : '';
if( !$phone )
{
  http_response_code(401);
  echo 'No phone number specified!';
  exit;
}

// connect to DB
$con = mysqli_connect( $db["host"], $db["user"], $db["password"], $db["database"] );
if( mysqli_connect_errno($con) ):
  http_response_code(500);
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit;
endif;

// Escape phone number
$phone = mysqli_real_escape_string( $con, $phone );

// Query Database for conversation thread
$query = "SELECT * FROM `log` WHERE `from` = '".$phone."' OR `to` = '".$phone."'";
$result = mysqli_query( $con, $query );
mysqli_close($con);

// Prepeare response data
$output = [];
while( $row = mysqli_fetch_array($result) ):
  $output[] = $row;
endwhile;

echo json_encode($output);