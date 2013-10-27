package clockwork;

import clockwork.exception.InsufficiantBalanceException;
import clockwork.exception.InvalidFieldException;
import clockwork.exception.InvalidKeyException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * Entry Class for HackManchester Project
 * 
 * 
 * 
 * @author BuckleWoods (Nathan Buckley, Andrew Isherwood, Joe Westwood)
 */
public class Server 
{    
    private static final Logger LOG = Logger.getLogger("MainLogger");        
    /**
     * Main entry point into the program. 
     * 
     * @param args the command line arguments
     */    
    public static void main(String[] args) 
    {           
        try
        {
            FileHandler fHnd = new FileHandler("log.txt");
            LOG.addHandler(fHnd);
        }
        catch(IOException | SecurityException excep)
        {
            //Continue but obv logs won't be recorded on file.
            LOG.log(Level.SEVERE, "Unable To Add FileHandler to MainLogger", excep);
        }
        LOG.log(Level.INFO, "Starting Server...");
        try
        {
            LOG.log(Level.INFO, "Collecting credentials...");
            //Create Properties from properties file. 
            Properties  credentials = new Properties();
            try (FileInputStream fIn = new FileInputStream("db.properties")) {
                credentials.load(fIn);
            }
            catch(IOException excep)
            {
                LOG.log(Level.SEVERE, "Unable to open db.properties.");
                System.exit(-1);
            }
            
            Model       model       = new Model(credentials);
            Controller  controller  = new Controller(model);
            controller.startCuller();
        
            //Create a listener socket this will take incoming messages from the URL 
            //side and pass them onto the Controller.                     
            ServerSocket listener = new ServerSocket(Integer.parseInt((String)credentials.get("DATABASE_PORT")));
            
            LOG.log(Level.INFO, "ServerSocket listerning on port " + credentials.getProperty("DATABASE_PORT"));
                        
            while(!listener.isClosed())
            {
                Socket incoming = listener.accept();
                try
                {
                    controller.acceptIncoming(incoming);                
                }
                catch(InvalidFieldException excep)
                {
                    //LOG ISSUE but continue to run.
                    LOG.log(Level.SEVERE, "InvalidFieldException from incoming socket.", excep);
                }
            }                        
        }        
        catch(InvalidKeyException excep)
        {
            LOG.log(Level.SEVERE, "Unable to start. Invalid Key", excep);
        }
        catch(InsufficiantBalanceException excep)
        {
            LOG.log(Level.SEVERE, "InsufficiantBalanceException thrown. Stopping Service");            
        }
        catch(IOException excep)
        {
            LOG.log(Level.SEVERE, "IOException thrown in main. ", excep);           
        }   
        catch(Exception excep)
        {
            LOG.log(Level.SEVERE, "Exception thrown" + excep.getMessage(), excep);
        }
        LOG.log(Level.INFO, "Server shutting down...");
    }
}
