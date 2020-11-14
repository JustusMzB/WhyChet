package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private List<Room> rooms = Collections.synchronizedList(new LinkedList<Room>());
    private Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());
    private ServerSocket serverSocket;

    public Server(int port){
        try {
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(500000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rooms.add(new Room(1));
    }

    public void clientSearch(){
        Socket newClient;
        while (true){
            try{
                System.out.println("[SERVER] Waiting for Client....");
                newClient = serverSocket.accept();
                clientHandlers.add(new ClientHandler(newClient, this));
                clientHandlers.get(clientHandlers.size() - 1).start();
                System.out.println("[SERVER] New Client connected.");
                clientHandlerCleanup();
            } catch (IOException e) {
                System.err.println("[SERVER] Connection Error in main Loop / Timeout");
                e.printStackTrace();
                break;
            }
        }
    }

    private void clientHandlerCleanup(){
        System.out.println("[SERVER] Starting Clienthandler-Collection cleanup...");
        ArrayList<Integer> deleteHere = new ArrayList<>();
        for(int i = 0; i < clientHandlers.size(); i++){
            if(!clientHandlers.get(i).isAlive()){
                deleteHere.add(i);
            }
        }
        Collections.sort(deleteHere);
        for(int i = deleteHere.size()-1; i>= 0; i--){
            System.out.println("[SERVER] [CLIENT-CLEANUP] deleting inactive Handler-Thread " +clientHandlers.get(i).getId());
            clientHandlers.remove(deleteHere.get(i).intValue());
        }
        System.out.println("[SERVER] Cleanup finished.");
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<String, User> getUsers() {
        return users;
    }
}


/* TO DO
    *Cleaning Routine of clienthandlers
    *Message for users on new Login
 */