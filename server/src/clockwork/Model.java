package clockwork;

/**
 *
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Model 
{
    public Model()
    {
        
    }
    
    public Phone getPhone(String number)
    {        
        Phone phone = null;
        
        //Use number as unique ID to check Phones within the system
        //TODO
        
        //If Phone is null and does not exist within the system, then we
        //need to create it. 
        if(phone == null)
        {
            //Create
            phone = new Phone(number);
            
            //Store
            //TODO
        }
        
        return phone;
    }
}
