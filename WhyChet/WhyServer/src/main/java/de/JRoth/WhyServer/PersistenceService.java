package de.JRoth.WhyServer;

import java.util.List;
import java.util.Map;

public interface PersistenceService {
    void storeRooms(List<Room> rooms);
    void storeUsers(List<User> users);

    Map<String, User> loadUsers(Server server);

    List<Room> loadRooms();
}
