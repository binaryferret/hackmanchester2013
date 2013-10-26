package clockwork;

/**
 *
 * //TODO
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Message 
{
    private String number;
    private String msg;
    public Message(String number, String msg)
    {
        this.number = number;
        this.msg    = msg;
    }
    
    public String getNumber()
    {
        return number;
    }
    
    public String getMessage()
    {
        return msg;
    }

    @Override
    public String toString() {
        return "Message{" + "number=" + number + ", msg=" + msg + '}';
    }        
}
