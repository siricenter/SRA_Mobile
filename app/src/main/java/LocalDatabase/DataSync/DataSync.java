package LocalDatabase.DataSync;
 import quickconnectfamily.json.JSONInputStream;
 import quickconnectfamily.json.JSONInputStream;
 import java.net.Socket;
 import quickconnectfamily.json.JSONInputStream;
 import quickconnectfamily.json.JSONOutputStream;
 import java.lang.ref.WeakReference;
 import java.io.*;
 import android.os.Handler;

 import com.example.chad.sra_mobile.MyActivity;


public class DataSync implements Runnable {
    private String host = "https://www.sra-api.com";
    private String data = "default";
    private int socket = 80;
    private WeakReference theReference = null;
    private android.os.Handler theHandler = null;


    public DataSync (String host, int socket, String data, WeakReference theReference, Handler theHandler){
        this.host = host;
        this.socket = socket;
        this.data = data;
        this.theReference = theReference;
        this.theHandler = theHandler;
    }

    @Override
    public void run() {
        try{
            Socket toServer = new Socket(host, socket);
            final JSONInputStream inFromServer = new JSONInputStream(toServer.getInputStream());
            JSONOutputStream outToServer = new JSONOutputStream(toServer.getOutputStream());
            outToServer.writeObject(data);
            theHandler.post(new Runnable() {
                @Override
                public void run() {
                    MyActivity theActivity = (MyActivity) theReference.get();
                    //theActivity.getFromThread(inFromServer.readObject()); //System.out.println(inFromServer.readObject());
                }
            });
        }
        catch(Exception e){
            System.out.println("Fail");
            e.printStackTrace();
        }
    }
}
