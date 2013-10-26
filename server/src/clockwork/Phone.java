package clockwork;

import com.clockworksms.ClockWorkSmsService;
import com.clockworksms.ClockworkException;
import com.clockworksms.ClockworkSmsResult;
import com.clockworksms.SMS;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * A User. //TODO 
 * 
 * Data Object
 * 
 * @author BuckleWoods
 */
public class Phone 
{
    /**
     * The users phone number, and also their unique ID.
     */
    private String number;
    
    /**
     * Session for the Bot.
     */
    private ChatterBotSession botSession;
    
    /**
     * Will be used to determine when last activity was. 
     * 
     * Last activity will be used for culling inactive sessions.
     */
    private long lastActivity;
    //TODO When ending a session, send a bye message.
    
    private static final Logger LOG = Logger.getLogger(Phone.class.getName());    
    public Phone(String number)
    {
        this.number = number;
        botSession  = null;
        //update last message with system current time. 
        lastActivity = System.currentTimeMillis();
    }
    
    /**
     * Returns the phone number associated with this Phone object.
     * @return 
     */
    public String getNumber()
    {
        return number;
    }
    
    /**
     * Returns last active time in long (milis) 
     * 
     * @return last time active.
     */
    public long lastActiveWhen()
    {
        return lastActivity;
    }
    
    /**
     * //TODO
     * @param timeout
     * @return 
     */
    public boolean timedOut(long timeout)
    {
        return false;
    }
    
    /**
     * A message recieved from a phone. 
     * 
     * The message will be put through the chatter api and then the response
     * will be sent to the phone number associated with the first message. 
     *     
     * @param key
     * @param msg 
     */
    public void sendMessage(String key, String msg)
    {        
        try
        {
            if(key == null)
            {
                //TODO Key is null.
            }

            if(number == null)
            {
                //TODO if number is null.
            }
            
            //Check if we have a bot instance
            if(botSession == null)
            {
                ChatterBotFactory cbFactory = new ChatterBotFactory();
                ChatterBot botInstance = cbFactory.create(ChatterBotType.CLEVERBOT);
                botSession = botInstance.createSession();
            }
            
            //Send Message to AI and get response.
            String botResponse = botSession.think(msg);
            
            //Send Response from AI via SMS        
            SMS sms = new SMS(number, botResponse);
            ClockWorkSmsService service = new ClockWorkSmsService(key);
            ClockworkSmsResult  result  = service.send(sms);
            
            if(result.isSuccess())
            {
                //TODO Timestamp and log message in database and log.
                LOG.log(Level.INFO, "SENT");
            }
            else
            {
                //TODO Complete log.
                LOG.log(Level.SEVERE, "");
            }            
        }
        catch(ClockworkException excep)
        {
            //TODO Improve log message, and recover from the exception.
            LOG.log(Level.SEVERE, "Phone Exception", excep);            
        }
        catch(Exception excep)
        {
            //TODO Improve log message, and recover from the exception.
            LOG.log(Level.SEVERE, "Exception with bot", excep);            
        }
    }
    
    /**
     * Say goodbye to the user, and then tidy up and cleanup instances etc.
     * 
     * @param msg 
     */
    public void bye(String msg)
    {        
        //TODO
    }

    @Override
    public String toString() {
        return "Phone{" + "number=" + number + ", botSession=" + botSession + ", lastActivity=" + lastActivity + '}';
    }
        
}
