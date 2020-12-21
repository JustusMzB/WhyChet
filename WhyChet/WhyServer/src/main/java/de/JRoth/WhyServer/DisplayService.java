package de.JRoth.WhyServer;

public interface DisplayService{
    void log(String logEntry);
    void errLog(String logEntry);

    void memberJoined(Room room, User newMember);
    void memberLeft(Room room, User leavingMember);
    void addUser(User user);
}
