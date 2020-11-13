package sample;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    //vorerst default:
    //roomID = 1;
    //port = 1969;
    private int serverPort;
    private String serverName;
    private boolean connected;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private String userName;
    private int roomID;

    public Client(String serverName, int port) {
        this.serverPort = port;
        this.serverName = serverName;
        this.connected = false;
    }

    public int getServerPort() { return serverPort; }

    public String getServerName() { return serverName; }

    public String getUserName() { return userName; }

    public void setUserName(String userName){this.userName = userName; }

    public int getRoomID() { return roomID; }

    public void setRoomID(int roomID) { this.roomID = roomID; }

    public void setConnected(boolean connected){this.connected = connected; }

    public static void main(String[]args){
        Client client = new Client("localhost", 1969);
        if(!client.connected) {
            client.connect();
        }
    }

    private void connect() {
        try {
            Socket socket = new Socket(this.serverName, this.serverPort);
            setConnected(true);
            System.out.println("connected to " + this.serverName + "port " + this.serverPort);

            readingThread threadIn = new readingThread(socket, this);
            threadIn.start();
            writingThread threadOut = new writingThread(socket, this);
            threadOut.start();
        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    void disconnect(){
        try {
            socket.close();
            setConnected(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
