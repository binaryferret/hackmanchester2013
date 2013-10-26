package clockwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author BuckleWoods
 */
public class Controller 
{
    private Model model;
    
    private static final Logger LOG = Logger.getLogger(Controller.class.getName());
    
    /**
     * Controller Constructor that takes a Model instance that it will use.
     * @param model - the model that the Controller will use. 
     */
    public Controller(Model model)
    {
        if(model == null)
        {
            //TODO Handle this!
        }
        
        this.model = model;
    }
    
    /**
     * Takes a socket. Gets data from it which should be sent in a particular
     * format, initilises or grabs existing Phone object that should be associated
     * with the message. Then closes the socket.
     * 
     * //TODO tidy up
     * @param incoming 
     */
    public void acceptIncoming(Socket socket) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        while(!socket.isClosed())
        {
            String inLine; 
            while((inLine = in.readLine())!=null)
            {
                LOG.log(Level.INFO, "Message In: " + inLine);
                Message msg = createNewMessage(inLine);
                socket.close();
                handleMessage(msg);                
            }
        }
    }
    
    private Message createNewMessage(String incomingMsg)
    {
        if(incomingMsg == null)
        {
            //TODO handle null.
        }

        String number   = null;
        String msg      = null;
        
        String[] fields = incomingMsg.split("&");

        for(String field : fields)
        {
            String[] pair = field.split("=");
            if(pair.length == 2)
            {
                if(pair[0].toLowerCase().equals("from"))
                {
                    number = pair[1];
                }
                else if(pair[0].toLowerCase().equals("msg"))
                {
                    msg = pair[1];
                }                    
            }
        }
        
        //TODO Handle is valid message? If not then throw something.
        return new Message(number, incomingMsg);
    }
    
    private void handleMessage(Message msg)
    {
        //Create/Get Phone
        model.getPhone(msg.getNumber());
                
        
        //TODO Remove Dummy data
        
        
        //Send Message To Phone
         
    }
}
