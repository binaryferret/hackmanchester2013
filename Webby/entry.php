<?php
error_reporting(E_ALL);
include 'db.php';

function finish(){
	echo "</pre>";
	file_put_contents( "cache/".time().".html", ob_get_contents() );
	ob_end_flush();
	exit;
}

ob_start();
echo "<pre>";

// Configs
$PORT = 6969;
$HOST = "127.0.0.1";

// Get request fields
$to = isset( $_REQUEST['to'] ) ? $_REQUEST['to'] : '';
$from = isset( $_REQUEST['from'] ) ? $_REQUEST['from'] : '';
$content = isset( $_REQUEST['content'] ) ? $_REQUEST['content'] : '';
$msg_id = isset( $_REQUEST['msg_id'] ) ? $_REQUEST['msg_id'] : '';

echo "<h2>Request Fields:</h2>\n";
echo "to: ".$to."\n";
echo "from: ".$from."\n";
echo "content: ".$content."\n";
echo "msg_id: ".$msg_id."\n";

if( $from == '' || $content == '' ):
	echo "<strong>Error:</strong> 'from' and 'content' fields are both required";
	finish();
endif;

/*
	START SOCKET CONNECTIONING
*/
echo "<h2>TCP/IP Connection</h2>\n";
echo "Creating Socket...";

/* Create a TCP/IP socket. */
$socket = socket_create(AF_INET, SOCK_STREAM, SOL_TCP);
if( $socket === false ):
	echo "\nsocket_create() failed: reason: " . socket_strerror(socket_last_error()) . "\n";
else:
	echo "OK.\n";
endif;


echo "Attempting to connect to '$HOST' on port '$PORT'...";
$result = socket_connect($socket, $HOST, $PORT);
if ($result === false):
	echo "\nsocket_connect() failed.\nReason: ($result) " . socket_strerror(socket_last_error($socket)) . "\n";
else:
	echo "OK.\n";
endif;

$msg = "from=".$from."&msg=".$content."&to=".$to."&msg_id=".$msg_id."\n";

echo "Sending Message...";
socket_write($socket, $msg, strlen($msg));
echo "OK.\n";

// echo "Reading response:\n\n";
// $out = '';
// while ($out = socket_read($socket, 2048)) {
// 	echo $out;
// }

echo "Closing socket...";
socket_close($socket);
echo "OK.\n\n";

/*
	LOGGING TIME !
*/

//$connection = mysqli_connect( $db["host"], $db["user"], $db["password"], $db["database"] );
//if( mysqli_connect_errno($connection) ):
//  echo "Failed to connect to MySQL: " . mysqli_connect_error();
//endif;
//
//// Escape the input data
//$from = mysqli_real_escape_string( $connection, $from );
//$to = mysqli_real_escape_string( $connection, $to );
//$content = mysqli_real_escape_string( $connection, $content );
//$msg_id = mysqli_real_escape_string( $connection, $msg_id );
//
//// Do the query
//$query = "INSERT INTO `log` (`from`, `to`, `content`, `msg_id`, `time`) VALUES ( '".$from."', '".$to."', '".$content."', '".$msg_id."', ".microtime()/1000." )";
//echo "QUERY: ".$query."\n\n";
//if( mysqli_query( $connection, $query ) ):
//	printf( "%d Row inserted.\n", mysqli_affected_rows($connection) );
//else:
//	printf("Error: %s\n", mysqli_sqlstate($connection));
//endif;
//
//mysqli_close( $connection );

finish();