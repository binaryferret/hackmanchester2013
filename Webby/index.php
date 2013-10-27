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

            <form id="phoneform" class="form-horizontal" action=".">
              <div class="control-group">
                <div class="right">
                  <div class="row">
<<<<<<< HEAD
                    <input type="text" id="inputPhone" placeholder="447860033078">  
=======
                    <input type="text" id="inputPhone" placeholder="447717214711" value="447905309185">
>>>>>>> be531008599cdfc0e8017da841b6c9eb6e47189e
                    <button type="submit" class="btn">Sign in</button>

                  </div>
                  <p>Enter your number and you can review your message history</p>
                </div>
              </div>
            </form>
          </div>

          <div class="span6">
<<<<<<< HEAD
            <h3>Message History for <span id="msgHist">...</span></h4>
=======
            <h4>Message History for <span id="msgHist">...</span></h4>
            <div id="conversation">

            </div>
>>>>>>> be531008599cdfc0e8017da841b6c9eb6e47189e
          </div>
        </div>
        <hr>
        <p class="text-center">MobilePhone --> Clockwork --> WebPage --> Service --> CleverBot --> Service --> Clockwork --> MobilePhone</p>
        <hr>
    <!-- ABOUT -->
        <h3>What does it do?</h3>

        <ol>
          <li>Mobile phone user sends a message</li>  
          <li>Clockwork receives a message and posts it to a hosted page</li>  
          <li>Hosted pages handles and logs it and forwards it to a local service</li>  
          <li>Service manages chat sessions and sends the message to CleverBot</li>  
          <li>CleverBot replys to the message</li>  
          <li>Service sends the message to the message sender via clockwork api</li>  
          <li>Mobile Phone user receives a reply</li>  
        </ol>

<!--
        <p>So it all starts with a text being sent to <strong>07860033078</strong> where Clockwork sends a post containing the relevant data to a url on our server.</p>
        <p>Then our server gets the data that we need from the post, then opens a socket to a service thats also running on the server and sends it the message.</p>
        <p>The server thats running maintains a list of current sessions (coverstations) that are currently active (ones that have had much activity are periodically dropped). If there isn't a live session it requests a new one and store it.</p>
        <p>The message is then sent to CleverBot and waits for a reply. Once it gets a reply it sends it back to the sender using the Clockwork API.</p>
-->

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