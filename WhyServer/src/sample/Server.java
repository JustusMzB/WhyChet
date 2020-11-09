package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private List<Room> rooms = Collections.synchronizedList(new LinkedList<Room>());
    private Set<User> users = Collections.synchronizedSet(new HashSet<User>());
    private ServerSocket serverSocket;

    private void clientHandlerCleanup(){
        ArrayList<Integer> deleteHere = new ArrayList<>();
        for(int i = 0; i < clientHandlers.size(); i++){
            if(!clientHandlers.get(i).isAlive()){
                deleteHere.add(i);
            }
        }
        Collections.sort(deleteHere);
        for(int i = deleteHere.size()-1; i>= 0; i--){
            clientHandlers.remove(deleteHere.get(i).intValue());
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(100000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rooms.add(new Room(1));
    }

    public void clientListener(){
        Socket newClient;
        while (true){
            try{
                System.out.println("[SERVER] Waiting for Client....");
                newClient = serverSocket.accept();
                clientHandlers.add(new ClientHandler(newClient, this));
                clientHandlers.get(clientHandlers.size() - 1).start();
                System.out.println("New Client connected.");
                clientHandlerCleanup();
            } catch (IOException e) {
                System.err.println("[SERVER] Connection Error in main Loop / Timeout");
                e.printStackTrace();
                break;
            }
        }
    }

    public String onlineUserString(){
        String result = "";
        for(User i : users){
            if (i.isOnline()){
                result += i.getName() + "\n";
            }
        }
        if(result.length() == 0){
            result += "None";
        }
        return result;
    }
}


/* TO DO
    *Cleaning Routine of clienthandlers
    *Message for users on new Login
 */