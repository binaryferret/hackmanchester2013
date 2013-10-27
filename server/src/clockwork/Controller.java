package clockwork;

import clockwork.exception.InsufficiantBalanceException;
import clockwork.exception.InvalidFieldException;
import com.clockworksms.ClockworkException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main logic class from the MC (Sorry V) pattern. 
 * Contains the bulk of the core logic/backbone etc.
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Controller 
{
    /**
     * Model that the controller will use for data.
     */
    private Model   model;
    /**
     * Thread that every 5 minutes will 
     * call cullPhones to remove inactive 
     * phones based on their timeout state.
     */
    private Culler  culler;
    
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger("MainLogger");
    
    /**
     * Controller Constructor that takes a Model instance that it will use.
     * @param model - the model that the Controller will use. 
     */
    public Controller(Model model) throws Exception
    {        
        if(model == null)
        {
            throw new Exception("Invalid Model attemped to be assigned");
        }
        
        this.model = model;
        
        culler = new Culler();  
        
    }
    
    /**
     * Avoids having to start the culler during construction.
     */
    public void startCuller()
    {
        culler.start();
    }
    
    /**
     * Accepts an incoming socket, reads the data that it should squirt up
     * and then processes this data as a message.
     * @param socket Socket that has just been accepted, and that it get data from.
     
     * @throws IOException
     * @throws InvalidFieldException
     * @throws InsufficiantBalanceException 
     */
    public void acceptIncoming(Socket socket) throws IOException, InvalidFieldException, InsufficiantBalanceException
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
    
    /**
     * When incoming socket is accepted, we new to create a new Message 
     * object that will contain the broken down fields for easy access.
     * 
     * @param incomingMsg
     * @return
     * @throws InvalidFieldException 
     */
    private Message createNewMessage(String incomingMsg) throws InvalidFieldException
    {
        if(incomingMsg == null)
        {
            throw new InvalidFieldException("Controller.createNewMessage incomingMsg parameter is null.");
        }
        
        String number   = null;
        String to       = null;
        String msg      = null;
        String msgId   = null;
        
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
                else if(pair[0].toLowerCase().equals("to"))
                {
                    to = pair[1];
                }
                else if(pair[0].toLowerCase().equals("msg"))
                {
                    msg = pair[1];
                }
                else if(pair[0].toLowerCase().equals("msg_id"))
                {
                    msgId = pair[1];
                }
            }
        }       
        
        return new Message(number, to, msg, msgId);
    }    
    
    /**
     * Once a Message has been created, this method will handle 
     * it by matching matching with a Phone object and then 
     * calling upon the Phones own message handling routine. 
     * 
     * @param msg
     * @throws InsufficiantBalanceException 
     */
    private void handleMessage(Message msg) throws InsufficiantBalanceException
    {
        //Create/Get Phone
        Phone phone = model.getPhone(msg.getNumber());
                        
        //Send Message from phone to ai, and then response to phone.
        phone.sendMessage(model.getKey(), msg);
                
    }
    
    /**
     * Goes through a Collection of phones and notes any that have 
     * expired/timed out. 
     * 
     * It will then remove the phones from a collection and say byebye
     * to them.
     * 
     */
    private synchronized void cullPhones()
    {
        LOG.log(Level.INFO, "Culling Inactive Phones");
        
        /**
         * Used to keep note of any phones to remove. Rather than removing 
         * them from the hashmap during iteration.
         */
        ArrayList<String>      phonesToRemove = new ArrayList<>();
                
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
                    LOG.log(Level.INFO, "Phone: " + phone.getNumber() + "Timed Out. Removing");
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
            try{
                phone.bye(model.getKey(), "This ends the conversation");
            }
            catch(InsufficiantBalanceException excep)
            {
                LOG.log(Level.WARNING, "Insufficiant funds to send Bye SMS.");
            }
            catch(ClockworkException excep)
            {
                //Log exception, but no then continue.
                LOG.log(Level.SEVERE, "Tried to say bye to phone[" + phone.getNumber() + "] threw exception.", excep);
            }
        }
    }
    
    /**
     * Simple Thread that will wait for a certain amount of time
     * and then call cullPhones. 
     */
    class Culler extends Thread
    {
        private boolean running = false;
        private long    lastCull;
        @Override
        public void run() 
        {
            running = true;
            lastCull = System.currentTimeMillis();
            while(running)
            {                
                try
                {
                    Thread.sleep(300000l);
                }
                catch(InterruptedException excep)
                {
                    
                }
                cullPhones();
            }
        }
        
    }
}
