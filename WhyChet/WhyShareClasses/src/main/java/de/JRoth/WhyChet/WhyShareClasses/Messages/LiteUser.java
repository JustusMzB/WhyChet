package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class LiteUser {
    private String username;
    private boolean isOnline;

    public LiteUser(String username, boolean isOnline){
        this.username = username;
        this.isOnline = isOnline;
    }

    public String getName() {
        return username;
    }

    public boolean isOnline() {
        return isOnline;
    }
}
