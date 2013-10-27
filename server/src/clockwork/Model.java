package clockwork;

import clockwork.exception.InvalidKeyException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.ls.LSProgressEvent;

/**
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
    
    private Properties credentials;
    
    private static final Logger LOG = Logger.getLogger("MainLogger");
    
    
    
    /**
     * Will store Phone objects.
     * Need to keep hold of them in order for maintaining instance between
     * service and chatterbot api instances.
     */
    private HashMap<String, Phone> phones; 
    
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
}
