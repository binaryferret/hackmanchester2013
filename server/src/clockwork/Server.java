package clockwork;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Clockwork 
 * 
 * @author BuckleWoods
 */
public class Server {
    
    private static final Logger LOG = Logger.getLogger(Server.class.getName());    
    
    /**
     * @param args the command line arguments
     */    
    public static void main(String[] args) 
    {        
        Model       model       = new Model();
        Controller  controller  = new Controller(model);
        
        //Create a listener socket this will take incoming messages from the URL 
        //side and pass them onto the Controller. 
        try
        {
            ServerSocket listener = new ServerSocket(6969);
            while(!listener.isClosed())
            {
                Socket incoming = listener.accept();
                controller.acceptIncoming(incoming);
                
                //TODO should close incoming here?
                
            }                        
        }
        catch(IOException excep)
        {
            LOG.log(Level.SEVERE, "IOException thrown in main. ", excep);           
        }
    }
}
