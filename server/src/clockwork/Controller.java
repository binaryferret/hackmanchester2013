package clockwork;

import clockwork.exception.InvalidFieldException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
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
    public void acceptIncoming(Socket socket) throws IOException, InvalidFieldException
    {
        if(socket == null)
        {
            LOG.log(Level.INFO, "Incoming socket is null. Throwing IOException");
            throw new IOException("Incoming Socket null. ");
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String inLine; 
        while((inLine = in.readLine())!=null)
        {
            LOG.log(Level.INFO, "Message In: " + inLine);
            Message msg = createNewMessage(inLine);
            handleMessage(msg);                 
        }
    }
    
    private Message createNewMessage(String incomingMsg) throws InvalidFieldException
    {
        if(incomingMsg == null)
        {
            throw new InvalidFieldException("Controller.createNewMessage incomingMsg parameter is null.");
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
        
        if(!isNumberValid(number))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid number: number = " + number);            
        }
        if(!isMsgValid(msg))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid msg: msg = " + msg);            
        }
        
        return new Message(number, msg);
    }
    
    private boolean isNumberValid(String number)
    {
        if(number == null || number.length() == 0)
        {
            return false;
        }
        return true;
    }
    
    private boolean isMsgValid(String msg)
    {
        if(msg == null || msg.length() == 0)
        {
            return false;            
        }
        return true;
    }
    
    private void handleMessage(Message msg)
    {
        //Create/Get Phone
        Phone phone = model.getPhone(msg.getNumber());
                        
        //Send Message from phone to ai, and then response to phone.
        phone.sendMessage(model.getKey(), msg.getMessage());
    }
    
    /**
     * Applies a lock on Phones member, and goes through all phones to see
     * if they have timedOut. Then unlocks. 
     */
    private synchronized void cullPhones()
    {
        
        //TODO Comment
        ArrayList<String>      phonesToRemove = new ArrayList<>();
        
        //TODO Lock
        HashMap<String, Phone> phones = model.getPhones();
        
        //If no phones then just return.
        if(phones.size() <= 0)
        {
            return;
        }
        
        Phone[] phonesArray = phones.values().toArray(new Phone[0]);
        
        if(phonesArray!=null)
        {
            for(Phone phone : phonesArray)
            {
                if(phone.timedOut(model.getMaxTimeout()))
                {
                    phonesToRemove.add(phone.getNumber());
                }
            }
        }
        else
        {
            return;             
        }
        
        for(String number : phonesToRemove)
        {
            Phone phone = phones.remove(number);
            phone.bye("This ends the conversation");
        }
    }
}
