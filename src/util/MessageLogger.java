package util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * This class facilitates event logging to one unified log file.
 * The output of this logger does not have to be a file output, but in this
 * implementation it is.
 * 
 */
public class MessageLogger {

    public final static Logger myLogger = Logger.getLogger("Test");

    private static MessageLogger instance = null;

    public static MessageLogger getInstance ()  {
        if (instance == null) {
            //Cannot possibly add handling, as we are unable to log the error if the logger fails!
            try {
                prepareLogger();
            }
            catch (SecurityException e) {
                // 
                e.printStackTrace();
            }
            catch (IOException e) {                
                e.printStackTrace();
            }
            instance = new MessageLogger();
        }
        return instance;
    }

    private static void prepareLogger () throws SecurityException, IOException {
        Handler myFileHandler = new FileHandler("log.txt");
        myFileHandler.setFormatter(new SimpleFormatter());
        myLogger.addHandler(myFileHandler);
        myLogger.setUseParentHandlers(false);
        myLogger.setLevel(Level.FINEST);
    }

}
