package de.JRoth.WhyServer;

import java.util.List;
import java.util.Map;

public interface PersistenceService {
    //This does not yet store the chat
    void storeRooms(List<Room> rooms);

    void storeUsers(Map<String, User> users);

    Map<String, User> loadUsers(Server server);

    List<Room> loadRooms(Server server);
}
