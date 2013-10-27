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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
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
    
    private Properties credentials;
    
    private static final String driver = "com.mysql.jdbc.Driver";
    
    private static final Logger LOG = Logger.getLogger("MainLogger");    
    public Phone(String number, Properties credentials)
    {
        this.credentials = credentials;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch(ClassNotFoundException excep)
        {
            //TODO
        }
        
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
    public void sendMessage(String key, Message msg)
    {        
        try
        {
            if(key == null)
            {
                //TODO Key is null.
            }
                        
            //Check if we have a bot instance
            if(botSession == null)
            {
                ChatterBotFactory cbFactory = new ChatterBotFactory();
                ChatterBot botInstance = cbFactory.create(ChatterBotType.CLEVERBOT);
                botSession = botInstance.createSession();
            }
            
            //Send Message to AI and get response.
            String botResponse = botSession.think(msg.getMessage());            
            //LOG Response
            LOG.log(Level.INFO, "Bot Response: " + botResponse);
            
            //Store Message In, in database.
            storeResponse(msg.getNumber(), msg.getTo(), msg.getMessage(), msg.getMessageID());
                        
            //Store Message out, in database.
            storeResponse("BOT", msg.getNumber(), botResponse, msg.getMessageID());
            
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
    
    private void storeResponse(String from, String to, String msg, String msgID)
    {
        LOG.log(Level.INFO, "Storing bot response in database");
        
        try
        {           
            Connection conn = DriverManager.getConnection("jdbc:mysql://"+ credentials.getProperty("DATABASE_HOST") +"/" + credentials.getProperty("DATABASE_NAME"), credentials.getProperty("DATABASE_USER"), credentials.getProperty("DATABASE_PASS"));
            PreparedStatement preppedStatement = conn.prepareCall("INSERT INTO `log` (`from`, `to`, `content`, `msg_id`, `time`) VALUES (?, ?, ?, ?, ?)");
            
            //from
            preppedStatement.setString(1, from);
            //to
            preppedStatement.setString(2, to);
            //content
            preppedStatement.setString(3, msg);
            //msg_id
            preppedStatement.setString(4, msgID);
            //time
            preppedStatement.setLong(5, System.currentTimeMillis());
            
            preppedStatement.execute();
                                    
            preppedStatement.close();

            conn.close();

        }
        catch(SQLException excep)
        {
            LOG.log(Level.SEVERE, "SQLException...", excep);
        }
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
