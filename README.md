hackmanchester2013
==================

Group: BuckleWoods

- Nathan Buckley
- Andrew Isherwood
- Joe Westwood

Hackmanchester 2013 Hack. 

We entered the ClockworkSMS challenge of 'making something pointless using our API'. ClockworkSMS API is an easy to use API for 
using Clockworks system to send and recieve SMS. It provides a bunch of easy to use wrappers for a number of languages. 

The goal was to make something pointless and we thought it was funny to create a service that you could text and have a conversation
with an AI. A sorta omegle but with SMS and no Penes (Had to google the plural for penis...*sigh*). This of course could 
all be done FREE via going to the number of sites that provide you to talk with them. 

We also use the Chatterbot api to make it easy as pie to talk to the AI. 

Generally how it works is the Clockwork System will be pointing at 'entry.php' and this will get the data from a POST/GET message
and then open a socket with the running Java service. This Java service takes the data, and puts it through chatterbot and then uses
Clockwork API to send an SMS. Logs it in a datbase (Chatlogs).

Chat logs can be displayed onthe website side.


