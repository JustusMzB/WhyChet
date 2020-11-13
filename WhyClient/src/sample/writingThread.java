package sample;

import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class writingThread extends Thread{

    private Socket socket;
    private Client client;
    private PrintWriter writer;

    public writingThread(Socket socket, Client client){
        this.client= client;
        this.socket= socket;

        try{
            OutputStream outputStream = socket.getOutputStream();
            writer = new PrintWriter(outputStream, true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        String content;
        Console console = System.console();
        String userName = console.readLine("\n Enter your name: ");
        client.setUserName(userName);

        do{
            content = console.readLine("["+userName+"]");
            writer.println(content);
        }while(!content.equals("disconnect!"));


            client.disconnect();

    }
}
