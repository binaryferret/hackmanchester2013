<pre>
<?php

$phone = isset( $_REQUEST['phone'] ) ? $_REQUEST['phone'] : '';
echo "<h2>Phone: ".$phone."</h2>";

if( $phone == '' ):
  echo "<strong>Phone is required</strong>";
  exit;
endif;


include 'db.php';
$connection = mysqli_connect( $db["host"], $db["user"], $db["password"], $db["database"] );
if( mysqli_connect_errno($connection) ):
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
endif;

$phone = mysqli_real_escape_string( $connection, $phone );
$query = "SELECT * FROM `log` WHERE `from`='".$phone."'";
$result = mysqli_query( $connection, $query );

echo "Query: ".$query."\n";
echo "<h2>Result:</h2>";
echo "Rows: ".$result->num_rows."\n";

?>
<table>
  <tr>
    <th>ID</th>
    <th>FROM</th>
    <th>TO</th>
    <th>CONTENT</th>
    <th>MSG_ID</th>
    <th>TIME</th>
  </tr>
    <?php
    while( $row = mysqli_fetch_array($result) ):
      echo  "<tr><td>".$row["id"]."</td>\n".
            "<td>".$row["from"]."</td>\n".
            "<td>".$row["to"]."</td>\n".
            "<td>".$row["content"]."</td>\n".
            "<td>".$row["msg_id"]."</td>\n".
            "<td>".$row["time"]."</td>\n</tr>";
    endwhile;
    ?>
</table>
<?php

mysqli_close($connection);

?>
</pre>