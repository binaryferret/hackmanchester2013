$(document).ready(function(){

  window.$form = $("#phoneform")
  window.$phone = $("#inputPhone")
  window.$conversation = $("#conversation")
  window.$msgHist = $("#msgHist")

  $form.on("submit", function(){
    get_conversation( $phone.val() )
    return false
  })
})

function get_conversation( phone )
{
  $msgHist.text(phone)

  $.ajax({
    url: "../log.php",
    data: {phone:phone},
    dataType: "json",
    success: render_conversation,
    error: function( jqxhr, status, error ){
      alert("Error getting conversation for '"+phone+"' ("+error+")")
    }
  })
}

function render_conversation( data )
{
  console.log(data);
  $conversation.empty()
  for( var i=0, msg; msg=data[i]; i++ )
  {
    var date = new Date(parseInt(msg.time))
    var hours = date.getHours()
    var minutes = date.getMinutes()
    hours = hours < 10 ? "0"+hours : hours
    minutes = minutes < 10 ? "0"+minutes : minutes

    var $msg = $("<div/>").addClass("message")
    msg.from == "BOT" && $msg.addClass("bot")

    $("<span/>").addClass("from").text(msg.from).appendTo($msg)
    $("<span/>").addClass("time").text( hours+":"+minutes+(hours<12?"am":"pm") ).appendTo($msg)
    $("<p/>").addClass("content").text(msg.content).appendTo($msg)

    $conversation.append($msg)
  }
}
