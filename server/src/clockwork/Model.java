package clockwork;

import clockwork.exception.InvalidKeyException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.ls.LSProgressEvent;

/**
 * Model part of MC (sorry V). 
 * 
 * Contains the majority of the major data that the controller can gain access to. 
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Model 
{    
    /**
     * static final long that is the default max timeout allowed
     * for inactivity before pruning phones.
     */
    public static final long DEFAULT_MAX_TIMEOUT = 600000l;
    
    /**
     * Max amount of miliseconds that timeout is allowed 
     * before clearing out a Phone.
     */
    private long   maxTimeout;
    
    /**
     * Var that holds unique Clockwork API Key.
     */
    private String key;
    
    /**
     * Credentials that contain the various settings for the database etc.
     */
    private Properties credentials;
    
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger("MainLogger");
    
    
    
    /**
     * Will store Phone objects.
     * Need to keep hold of them in order for maintaining instance between
     * service and chatterbot api instances.
     */
    private HashMap<String, Phone> phones; 
    
    /**
     * Main constructor for model. Takes credentials which is can use to get the
     * various details on the database etc.
     * @param credentials
     * @throws InvalidKeyException 
     */
    public Model(Properties credentials) throws InvalidKeyException
    {
        key = credentials.getProperty("CLOCKWORK_API_KEY");
        
        this.credentials = credentials;
        
        if(key == null || key.length() == 0)
        {
            throw new InvalidKeyException("Attempting to create Model with invalid_key: Key = " + key);
        }
        
        phones      = new HashMap<>();     
        
        maxTimeout = DEFAULT_MAX_TIMEOUT;
    }
    
    public String getKey()
    {
        return key;
    }
    
    public HashMap<String, Phone> getPhones()
    {
        return phones;
    }
    
    /**
     * Gets a stored phone from the collection of current users - Phone(s)
     * If a phone does not exist, then it needs to be added using the 
     * number that is taken as a parameter. 
     * 
     * Then returns either a newly created Phone or an existing phone. 
     * @param number Unique ID used to identify a Phone. 
     * @return Phone instance.
     */
    public Phone getPhone(String number)
    {        
        Phone phone = null;
        
        //Use number as unique ID to check Phones within the system
        phone = phones.get(number);
        
        //If Phone is null and does not exist within the system, then we
        //need to create it. 
        if(phone == null)
        {
            LOG.log(Level.INFO, "Phone: " + number + "\nDoes Not exist in system. Creating and initiising bot.");
            
            //Create phone
            phone = new Phone(number, credentials);
            
            //Store phone.
            phones.put(phone.getNumber(), phone);            
        }        
        return phone;
    }
    
    public void setMaxTimeout(long maxTimeout)
    {
        this.maxTimeout = maxTimeout;
    }
    
    
    public long getMaxTimeout()
    {
        return maxTimeout;
    }    
    
    public Properties getCredentials()
    {
        return credentials;
    }
    
    public String getDatabaseName()
    {
        return credentials.getProperty("DATABASE_NAME");
    }
    
    public String getDatabaseUser()
    {
        return credentials.getProperty("DATABASE_USER");
    }
    
    public String getDatabasePass()
    {
        return credentials.getProperty("DATABASE_PASS");
    }
    
    public String getDatabaseHost()
    {
        return credentials.getProperty("DATABASE_HOST");
    }
    
    public int getDatabasePort()
    {
        try
        {
            return Integer.parseInt((String)credentials.getProperty("DATABASE_PORT"));
        }
        catch(NumberFormatException excep)
        {
            //Default port
            return 6969;
        }        
    }
}
