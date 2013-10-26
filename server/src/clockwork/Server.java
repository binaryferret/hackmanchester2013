package clockwork;

import clockwork.exception.InvalidFieldException;
import clockwork.exception.InvalidKeyException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Server 
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Server 
{
    
    private static final Logger LOG = Logger.getLogger(Server.class.getName());        
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) 
    {   
        try
        {
            Model       model       = new Model("");
            Controller  controller  = new Controller(model);
        
            //Create a listener socket this will take incoming messages from the URL 
            //side and pass them onto the Controller.         
            ServerSocket listener = new ServerSocket(6969);
            while(!listener.isClosed())
            {
                Socket incoming = listener.accept();
                try
                {
                    controller.acceptIncoming(incoming);                
                }
                catch(InvalidFieldException excep)
                {
                    //Just continue for now. //TODO Handle better
                }
            }                        
        }        
        catch(InvalidKeyException excep)
        {
            LOG.log(Level.SEVERE, "Unable to start. Invalid Key", excep);
        }
        catch(IOException excep)
        {
            LOG.log(Level.SEVERE, "IOException thrown in main. ", excep);           
        }
    }
}
