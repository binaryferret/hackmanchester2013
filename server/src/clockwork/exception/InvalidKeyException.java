package clockwork.exception;

/**
 * Exception that is generally thrown when an InvalidKey is attempted to be used.
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class InvalidKeyException extends Exception {
    public InvalidKeyException(String msg)
    {
        super(msg);
    }
}
