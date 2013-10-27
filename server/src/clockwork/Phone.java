package clockwork;

import clockwork.exception.InsufficiantBalanceException;
import com.clockworksms.Balance;
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
 * Data object class that represents a user that is using the system. 
 * 
 * Handles it's own writes to the database, along with message sending code. 
 * Maintains an instance with chatter bot api. 
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Phone 
{
    /**
     * The users phone number, and also their unique ID.
     */
    private String number;
    
    /**
     * Session for the Bot that the user will be communicating with. 
     */
    private ChatterBotSession botSession;
    
    /**
     * Will be used to determine when last activity was. 
     * 
     * Last activity will be used for culling inactive sessions.
     */
    private long lastActivity;
    
    /**
     * Credentials that are held for the database etc.
     */
    private Properties credentials;
    
    /**
     * Flag that indicates if the Phone Data object can access database. 
     * If it can't then program continues working, although chat logs will 
     * be lacking.  
     */
    private boolean    canAccessDatabase;
    
    /**
     * Driver string for JDBC Mysql connections.
     */
    private static final String driver = "com.mysql.jdbc.Driver";
    
    /**
     * LOGGING
     */
    private static final Logger LOG = Logger.getLogger("MainLogger");    
    
    /**
     * Constructor for Phone, 
     * 
     * @param number unique number of the Phone in question. 
     * @param credentials Properties instance containing details on the database etc.
     */
    public Phone(String number, Properties credentials)
    {
        this.credentials = credentials;
        
        canAccessDatabase = true;
        try
        {
            Class.forName(driver);
        }
        catch(ClassNotFoundException excep)
        {
            canAccessDatabase = false;
            LOG.log(Level.SEVERE, "ClassNotFoundException when trying to load jdbc driver class. Will continue to run but will not write to database.", excep);
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
     * @return last time active in miliseconds. 
     */
    public long lastActiveWhen()
    {
        return lastActivity;
    }
    
    /**
     * Compares last activity, against current system time and uses the timeout range
     * to determine if the user has no longer with us. 
     * 
     * @param timeout
     * @return if the user has 'timedOut'/Not been active.
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
     * A message received from a phone. 
     * 
     * The message will be put through the chatter api and then the response
     * will be sent to the phone number associated with the first message. 
     *     
     * @param key - Clockwork API Key to use.
     * @param msg Message class containing the details that came through from Clockwork Forwarding.
     */
    public void sendMessage(String key, Message msg) throws InsufficiantBalanceException
    {        
        try
        {                        
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
            LOG.log(Level.SEVERE, "Exception thrown when attempting to send SMG.", excep);            
        }
        catch(InsufficiantBalanceException excep) 
        {
            throw excep;
        }        
        catch(Exception excep)
        {
            LOG.log(Level.SEVERE, "Exception thrown when attempting to communicate with chatter bot api", excep);            
        }
    }
    
    /**
     * Sends an SMS via clockwork API. 
     * 
     * @param key - Clockwork API Key to use. 
     * @param msg - Message to send.
     * @return if the SMS was successfully sent. 
     * 
     * @throws ClockworkException
     * @throws InsufficiantBalanceException this is thrown if the service does not 
     * have enough funds to send the sms.
     */
    private boolean sendSMS(String key, String msg) throws ClockworkException, InsufficiantBalanceException
    {
        SMS sms = new SMS(number, msg);
        ClockWorkSmsService service = new ClockWorkSmsService(key);
        
        //servicve.checkBalance throwing weird issue. 
        //Commented out for now. 
        long credit = service.checkCredit();
        if(credit < 5)
        {
            throw new InsufficiantBalanceException();
        }
                
        ClockworkSmsResult  result  = service.send(sms);

        if(result.isSuccess())
        {
            LOG.log(Level.INFO, "SMS Sent to " + number);
            return true;
        }
        else
        {
            LOG.log(Level.SEVERE, "Was unsuccessful with sending an SMS to " + number);
            return false;
        }
    }
    
    /** 
     * Used to store details into the database. 
     * 
     * @param from - Where the message is from 
     * @param to - Where the message is going
     * @param msg - The actual message
     * @param msgID - Unique message ID.
     */
    private void storeResponse(String from, String to, String msg, String msgID)
    {
        LOG.log(Level.INFO, "Storing Response in database");
        
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
     * @param msg The byebye message.
     */
    public void bye(String key, String msg) throws ClockworkException, InsufficiantBalanceException
    {        
        sendSMS(key, msg);
    }

    @Override
    public String toString() 
    {
        return "Phone{" + "number=" + number + ", botSession=" + botSession + ", lastActivity=" + lastActivity + ", credentials=" + credentials + ", canAccessDatabase=" + canAccessDatabase + '}';
    }           
}
