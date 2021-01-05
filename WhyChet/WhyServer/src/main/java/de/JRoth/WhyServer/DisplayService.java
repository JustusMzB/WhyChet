package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;

public interface DisplayService{
    void log(String logEntry);
    void errLog(String logEntry);

    void memberJoined(Room room, User newMember);
    void memberLeft(Room room, User leavingMember);
    void addUser(User user);

    void chatMessage(Message message, long id);

    void addRoom(Room newRoom);

    void updateRoom(Room target);

    void removeRoom(Room room);
}
