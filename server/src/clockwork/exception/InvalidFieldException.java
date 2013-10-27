package clockwork.exception;

/**
 * Exception that is thrown when there is an InvalidField attempting to be set
 * or has not been set.
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class InvalidFieldException extends Exception
{
    public InvalidFieldException(String msg)
    {
        super(msg);
    }
}
