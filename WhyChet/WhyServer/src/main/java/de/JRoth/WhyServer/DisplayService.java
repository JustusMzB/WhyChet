package de.JRoth.WhyServer;

public interface DisplayService{
    public void log(String logEntry);
    public void errLog(String logEntry);

    public void addUser(User user);
}
