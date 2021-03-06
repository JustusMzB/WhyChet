package de.JRoth.WhyServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Terminateable {
    private final List<Room> rooms = Collections.synchronizedList(new LinkedList<Room>());
    private final Map<String, User> users = Collections.synchronizedMap(new HashMap<String, User>());
    private DisplayService displayService;
    private ServerSocket serverSocket;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Server(int port, DisplayService displayService) {
        this.displayService = displayService;
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
        while (running.get()) {
            try {
                displayService.log("[SERVER] Waiting for Client....");
                newClient = serverSocket.accept();
                new ClientHandler(newClient, this).start();
                displayService.log("[SERVER] New Client connected.");
            } catch (IOException e) {
                if(running.get()) {
                    displayService.errLog("[SERVER] Connection Error in main Loop / Timeout");
                    e.printStackTrace();
                    break;
                } else {
                    displayService.log("[SERVER] Clientsearch was stopped");
                }
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
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnectUser(String username) {
        if (users.containsKey(username)) {
            users.get(username).logOff();
        } else {
            displayService.log("[SERVER] disconnectUser Failed: Unknown user");
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public DisplayService DisplayType() {
        return displayService;
    }
}


/* TO DO
 *Cleaning Routine of clienthandlers
 *Message for users on new Login
 */