<?php
  $test = "PHP WOO!";
?>
<!DOCTYPE html>
<html>
  <head>
    <title>Artificial Friend - <?php echo $test; ?></title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
    <link rel="stylesheet" href="css/style.css"/>

    <script src="js/jquery.2.0.3.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/main.js"></script>

  </head>

  <body>
    <!-- Header -->
    <div class="container">


      <header>
        <div class="row"><h1>Pinocchio</h1></div>
      </header>
    </div>
    <section class="hero-unit">
      <section class="container">

      <!-- INTRO -->
        <div class="row page">

          <div class="span6">
            <h3>Txt <bold>07860033078</bold></h3>
              <ul>
                <li>When youre bored</li>
                <li>and not near a computer</li>
                <li>and dont have 3g</li>
                <li>... or 4g</li>
                <li>and you only have 2g</li>
                <li> then send a message and have a chat!</li>  
              </ul>
          </div>

          <div class="span6">
            <p>Image of something</p>
          </div>
        </div>

        <hr>
        <div class="text-center">
          <p>Immune to logical paradoxes</p>
        </div>

        <hr>
    <!-- CONVO LIST -->

        <div class="row">

          <div class="span6">
            <h3>Peruse my Exchanges</h3>

            <form class="form-horizontal" action=".">
              <div class="control-group"> 
                <div class="right">
                  <div class="row">
                    <input type="text" id="inputPhone" placeholder="447527156515">  
                    <button type="submit" class="btn">Sign in</button>

                  </div>
                  <p>Enter your number and you can review your message history</p>
                </div>
              </div>
            </form>
          </div>

          <div class="span6">
            <h4>Message History for <span id="msgHist">...</span></h4>
          </div>
        </div>
        <hr>
    <!-- ABOUT -->


      </section>
    </section>



    

    <footer class="text-center">
      <small>
        This is a <a href="http://hackmanchester.com" target="_blank">Hack Manchester</a> 2013 hack using <a href="http://clockworksms.com" target="_blank">Clockwork SMS</a>'s messaging API!<br/>
        This is just a joke ;)<br/>
        &copy; Somebody 2013
      </small>
    </footer>
  </body>
</html>