package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyChet.WhyShareClasses.Messages.RoomMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server extends Terminateable {
    private final List<Room> rooms;
    private Map<String, User> users;
    private DisplayService displayService;
    private PersistenceService persistence;


    private ServerSocket serverSocket;
    private final AtomicBoolean running = new AtomicBoolean(false);

    public Server(int port, DisplayService displayService) {
        //Initialisation of Components
        this.displayService = displayService;
        persistence = new JsonPersistence();

        //Socket Connection
        try {
            serverSocket = new ServerSocket(port);
            //serverSocket.setSoTimeout(500000);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initialisation of rooms
        List<Room> loadedRooms = persistence.loadRooms(this);
        if(loadedRooms == null){
            rooms = Collections.synchronizedList(new LinkedList<>());
            rooms.add(new Room(1, "Global", this));
        } else {
            rooms = Collections.synchronizedList(loadedRooms);
        }

        //Initialisation of Logindata
        Map<String, User> loadedCreds = persistence.loadUsers(this);
        if(loadedCreds != null){
            users = Collections.synchronizedMap(loadedCreds);
        } else {
            users = Collections.synchronizedMap(new HashMap<>());
        }


    }

    private void roomUpdate(Room room){
        for(User i : users.values()){
            if(i.isOnline()){
                Message updateMessage = RoomMessage.setRoomMessage(LiteObjectFactory.makeLite(room));
                try {
                    i.sendMessage(updateMessage);
                    displayService.log("[SERVER] User " + i.getName() + " was updated on changes to room " + room.getId());
                } catch (NullPointerException e) {
                    displayService.errLog("[SERVER] User " + i.getName() + " Could not be updated on changes to room " + room.getId());
                    e.printStackTrace();
                }
            }
        }
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
        //Client-Search will terminate
        running.set(false);

        //Logged on clients are removed
        for (User i : users.values()){
            i.logOff();
        }

        //Socket is closed
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Rooms and Credentials are stored

        persistence.storeRooms(rooms);
        displayService.log("[SERVER] Rooms are being stored");
        persistence.storeUsers(users);
        displayService.log("[SERVER] Credentials are being stored");

    }

    public void disconnectUser(String username) {
        if (users.containsKey(username)) {
            users.get(username).logOff();
        } else {
            displayService.log("[SERVER] disconnectUser Failed: Unknown user");
        }
    }
    public void warnUser(User user){
        user.notify("You have been warned!!!");
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public DisplayService displayType() {
        return displayService;
    }
    public void removeRoom(Room room){
        if(room != rooms.get(0) ) {
            //Cloning the Members list to avoid exception in iteration
            displayService.log("[SERVER] Room " + room.getId() + " is being disbanded");
            LinkedList<User> roomMemberClone = new LinkedList<>(room.getMembers());
            for (User i : roomMemberClone) {
                room.removeMember(i);
                rooms.get(0).addMember(i);
            }
            displayService.removeRoom(room);
        }
    }

    public void editRoom(int id, String newName){
        for (Room i : rooms){
            if(i.getId() == id){
                editRoom(i, newName);
            }
        }
    }
    public void editRoom(Room target, String newName){
        target.setName(newName);
        roomUpdate(target);
        displayService.updateRoom(target);
    }

    public void addRoom(String roomName){
        int newRoomID = rooms.size();
        //Retrieving a free roomID, This only works, if monotony of roomid's is ensured.
        for( Room i : rooms){
            if(i.getId() == newRoomID){
                newRoomID++;
            }
        }
        Room newRoom = new Room(newRoomID, roomName, this );
        rooms.add(newRoom);
        displayService.addRoom(newRoom);

        //Remaining requirement: Update all clients on the new room
        roomUpdate(newRoom);
    }

}


/* TO DO
 *Cleaning Routine of clienthandlers
 *Message for users on new Login
 */