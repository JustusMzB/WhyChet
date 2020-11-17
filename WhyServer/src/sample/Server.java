package sample;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {
    private final ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
    private final List<Room> rooms = Collections.synchronizedList(new LinkedList<Room>());
    private final Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());
    private final LogService log = new ConsoleLog();
    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(500000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        rooms.add(new Room(1));
    }

    public void clientSearch() {
        Socket newClient;
        while (true) {
            try {
                log.log("[SERVER] Waiting for Client....");
                newClient = serverSocket.accept();
                clientHandlers.add(new ClientHandler(newClient, this));
                clientHandlers.get(clientHandlers.size() - 1).start();
                log.log("[SERVER] New Client connected.");
                clientHandlerCleanup();
            } catch (IOException e) {
                log.errLog("[SERVER] Connection Error in main Loop / Timeout");
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void run() {
        clientSearch();
    }

    public void close() {
        clientHandlerCleanup();
        for (ClientHandler i : clientHandlers) {
            i.disconnect();
        }
    }

    private void clientHandlerCleanup() {
        log.log("[SERVER] Starting Clienthandler-Collection cleanup...");
        ArrayList<Integer> deleteHere = new ArrayList<>();
        for (int i = 0; i < clientHandlers.size(); i++) {
            if (!clientHandlers.get(i).isAlive()) {
                deleteHere.add(i);
            }
        }
        Collections.sort(deleteHere);
        for (int i = deleteHere.size() - 1; i >= 0; i--) {
            log.log("[SERVER] [CLIENT-CLEANUP] deleting inactive Handler-Thread " + clientHandlers.get(i).getId());
            clientHandlers.remove(deleteHere.get(i).intValue());
        }
        log.log("[SERVER] Cleanup finished.");
    }

    public void disconnectUser(String username) {
        if (users.containsKey(username)) {
            for (ClientHandler i : clientHandlers) {
                if (i.getUser().getName().equals(username)) {
                    i.disconnect();
                }
            }
        } else {
            log.log("[SERVER] disconnectUser Failed: Unknown user");
        }
    }

    //Allows some control over Server via Message-Wrapped Orders
    public void takeOrder(Message order) {
        switch ((int) order.getRoomID()) {
            case -1:
                disconnectUser(order.getContent());
                break;
            case -2:
                close();
                break;
            default:
                log.log("[SERVER] Received unknown order");

        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public LogService logType() {
        return log;
    }
}


/* TO DO
 *Cleaning Routine of clienthandlers
 *Message for users on new Login
 */