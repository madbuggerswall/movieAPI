import java.io.*;

public class Logger {

    private static Logger logger;
    private String fileName = "log.txt";
    private PrintWriter writer;

    public Logger(){

        try {
            FileOutputStream fo =  new FileOutputStream(new File(fileName), true);
            writer = new PrintWriter(fo);
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" + fileName + "'");
        }

    }

    public static Logger getInstance(){
        if (logger==null)
            logger = new Logger();
        return logger;
    }

    public void log(String message){
        System.out.println("message = [" + message + "]");
            writer.append(message);
            writer.flush();
    }
}
