package clockwork;

import clockwork.exception.InvalidFieldException;

/**
 *
 * //TODO
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Message 
{
    private String number;
    private String to;
    private String msg;
    private String msgID;
    public Message(String number, String to, String msg, String msgID) throws InvalidFieldException
    {
        this.number = number;
        this.to     = to;
        this.msg    = msg;
        this.msgID  = msgID;        
        
        if(!isValidField(number))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid number: number = " + number);            
        }
        if(!isValidField(to))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid to: tor = " + to);            
        }
        if(!isValidField(msg))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid msg: msg = " + msg);            
        }
        if(!isValidField(msgID))
        {
            throw new InvalidFieldException("Controller.createNewMessage recieved invalid msgID: msgID = " + msgID);            
        }
        
    }
    public String getTo()
    {
        return to;
    }
    public String getNumber()
    {
        return number;
    }
    
    public String getMessage()
    {
        return msg;
    }
    
    public String getMessageID()
    {
        return msgID;
    }
    
    
    private boolean isValidField(String fieldValue)
    {
        if(fieldValue == null || fieldValue.length() == 0)
        {
            return false;
        }
        return true;
    }        
    
    @Override
    public String toString() {
        return "Message{" + "number=" + number + ", msg=" + msg + '}';
    }        
}
