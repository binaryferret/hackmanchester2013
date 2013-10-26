package clockwork;

import com.clockworksms.ClockWorkSmsService;
import com.clockworksms.ClockworkException;
import com.clockworksms.ClockworkSmsResult;
import com.clockworksms.SMS;
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
    
    private static final Logger LOG = Logger.getLogger(Phone.class.getName());    
    public Phone(String number)
    {
        this.number = number;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public void sendMessage(String key, String msg)
    {        
        if(key == null)
        {
            //TODO Key is null.
        }
        
        if(number == null)
        {
            //TODO if number is null.
        }
        
        //Send Message to AI
        //Get response from AI
        
        //Send Response from AI via SMS
        try
        {
            SMS sms = new SMS(number, msg);
            ClockWorkSmsService service = new ClockWorkSmsService(key);
            ClockworkSmsResult  result  = service.send(sms);
            
            if(result.isSuccess())
            {
                System.out.println("DEBUG: Message Successful");         
                //TODO Timestamp and log message in database and log.
                
            }
            else
            {
                //TODO Complete log.
                LOG.log(Level.SEVERE, "");
            }            
        }
        catch(ClockworkException excep)
        {
            System.out.println("DEBUG: Err Exception sendMessageTo: " + excep.getMessage());
            excep.printStackTrace();
        }
    }
}
