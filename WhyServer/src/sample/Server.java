package sample;

import javafx.scene.image.Image;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Terminateable {
    private final List<Room> rooms = Collections.synchronizedList(new LinkedList<Room>());
    private final Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());
    private final LogService log = new ConsoleLog();
    private ServerSocket serverSocket;
    private final AtomicBoolean running = new AtomicBoolean(false);

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
                new ClientHandler(newClient, this).start();
                log.log("[SERVER] New Client connected.");
            } catch (IOException e) {
                log.errLog("[SERVER] Connection Error in main Loop / Timeout");
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void run() {
        running.set(true);
        clientSearch();
    }
    @Override
    public void terminate() {
        for (User i : users.values()){
            i.logOff();
        }
        running.set(false);
    }

    public void disconnectUser(String username) {
        if (users.containsKey(username)) {
            users.get(username).logOff();
        } else {
            log.log("[SERVER] disconnectUser Failed: Unknown user");
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