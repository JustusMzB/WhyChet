package sample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class readingThread extends Thread {
    private Socket socket;
    private Client client;
    private BufferedReader reader;

    public readingThread(Socket socket, Client client){
        this.socket = socket;
        this.client = client;

        try{
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void run(){
        while(true){
            try{
                String msg = reader.readLine();
                System.out.println("\n" + msg);
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
        }
    }

}
