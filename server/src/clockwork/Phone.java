package clockwork;

import com.clockworksms.ClockWorkSmsService;
import com.clockworksms.ClockworkException;
import com.clockworksms.ClockworkSmsResult;
import com.clockworksms.SMS;
import com.google.code.chatterbotapi.ChatterBot;
import com.google.code.chatterbotapi.ChatterBotFactory;
import com.google.code.chatterbotapi.ChatterBotSession;
import com.google.code.chatterbotapi.ChatterBotType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    
    private static final String driver = "com.mysql.jdbc.Driver";
    
    private static final Logger LOG = Logger.getLogger(Phone.class.getName());    
    public Phone(String number)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException excep)
        {
            //TODO
        }
//            
//            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/hackmanchester?user=hackmanchester&password=adminadmin");
//            Statement statement = conn.createStatement();
//            ResultSet result = statement.executeQuery("SELECT * FROM logs");
//            LOG.log(Level.INFO, "TO STRING = " + result.toString());
//            
//            if(result!=null)
//            {
//                result.close();
//            }
//            
//            if(statement!=null)
//            {
//                statement.close();
//            }
//            
//            if(conn!=null)
//            {
//                conn.close();
//            }
//            
//        }
//        catch(ClassNotFoundException excep)
//        {
//            LOG.log(Level.SEVERE, "Unable to load jdbc driver...");
//        }
//        catch(SQLException excep)
//        {
//            LOG.log(Level.SEVERE, "SQLException...", excep);
//        }
        
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
        long current = System.currentTimeMillis();
        if(current - lastActivity > timeout)
        {
            return true;
        }        
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
            
            //LOG Response
            LOG.log(Level.INFO, "Bot Response: " + botResponse);
            //Store response in database chat logs.
            storeBotResponse(botResponse);
            
            //Send Response from AI via SMS Try three times.
            int i = 0;
            while(!sendSMS(key, botResponse))
            {
                if(i==2)
                {
                    throw new ClockworkException("Unable to send SMS. Tried three times");
                }
                i++;
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
    
    //TODO
    private boolean sendSMS(String key, String msg) throws ClockworkException
    {
        SMS sms = new SMS(number, msg);
        ClockWorkSmsService service = new ClockWorkSmsService(key);
        ClockworkSmsResult  result  = service.send(sms);

        if(result.isSuccess())
        {
            LOG.log(Level.INFO, "SENT");
            return true;
        }
        else
        {
            //TODO Complete log.
            LOG.log(Level.SEVERE, "");
            return false;
        }
    }
    
    private void storeBotResponse(String msg)
    {
        LOG.log(Level.INFO, "Storing bot response in database");
        //TODO Actually store.
    }
    
    /**
     * Say goodbye to the user. 
     * 
     * @param msg 
     */
    public void bye(String key, String msg) throws ClockworkException
    {        
        sendSMS(key, msg);
    }

    @Override
    public String toString() {
        return "Phone{" + "number=" + number + ", botSession=" + botSession + ", lastActivity=" + lastActivity + '}';
    }
        
}
