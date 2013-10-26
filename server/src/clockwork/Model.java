package clockwork;

import clockwork.exception.InvalidKeyException;
import java.util.HashMap;

/**
 *
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Model 
{
    public static long DEFAULT_MAX_TIMEOUT = 10000l;
    
    //TODO Comment
    private long   maxTimeout;
    
    //TODO Comment. 
    private String key;
    /**
     * Will store Phone objects that hold an instance. 
     */
    private HashMap<String, Phone> phones; 
    
    public Model(String key) throws InvalidKeyException
    {
        if(key == null || key.length() == 0)
        {
            throw new InvalidKeyException("Attempting to create Model with invalid_key: Key = " + key);
        }
        this.key    = key;
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
            //Create phone
            phone = new Phone(number);
            
            //Store phone.
            phones.put(phone.getNumber(), phone);            
        }        
        return phone;
    }
    
    public void setMaxTimeout(long maxTimeout)
    {
        this.maxTimeout = maxTimeout;
    }
    
    //TODO
    public long getMaxTimeout()
    {
        return maxTimeout;
    }
}
