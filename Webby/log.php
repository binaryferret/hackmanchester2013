<pre>
<h2>Relive those charming conversations</h2>
<?php

$phone = isset( $_REQUEST['phone'] ) ? $_REQUEST['phone'] : '441234567890';
?>
<form action="." method="get" >
  <label>Enter your phone number</label>
  <input type="text" name="phone" value="<?php echo $phone; ?>" />
</form>
<?php


include 'db.php';
$connection = mysqli_connect( $db["host"], $db["user"], $db["password"], $db["database"] );
if( mysqli_connect_errno($connection) ):
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
endif;

$phone = mysqli_real_escape_string( $connection, $phone );
$query = "SELECT * FROM `log` WHERE `from` = '".$phone."' OR `to` = '".$phone."'";
$result = mysqli_query( $connection, $query );

echo "Query: ".$query."\n";
echo "<h2>Result:</h2>";
echo "Rows: ".$result->num_rows."\n";

?>
<div id="conversation">
  <?php while( $row = mysqli_fetch_array($result) ):
    $bot = $row['from'] == 'BOT';
  ?>
    <div class="message <?php if( $bot ){ echo 'bot'; } ?>">
      <strong><?php echo $row['from']; ?></strong>
      <p><?php echo $row['content']; ?></p>
      <time><?php echo date( "h:ia", $row['time']/1000 ); ?></time>
    </div>
  <?php endwhile; ?>
</div>
<?php

mysqli_close($connection);

?>
</pre>