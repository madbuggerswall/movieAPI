import java.io.*;

public class SingletonLogger {

    private static SingletonLogger singletonLogger = null;
    private String fileName = "log.txt";
    private PrintWriter writer;

    public SingletonLogger(){

        try {
            FileOutputStream fo =  new FileOutputStream(new File(fileName), true);
            writer = new PrintWriter(fo);
        }
        catch(FileNotFoundException ex) {
            System.out.println( "Unable to open file '" + fileName + "'");
        }

    }

    public static SingletonLogger getInstance(){
        if (singletonLogger ==null)
            singletonLogger = new SingletonLogger();
        return singletonLogger;
    }

    public void log(String message){
        System.out.println("message = [" + message + "]");
            writer.append(message);
            writer.flush();
    }
}
