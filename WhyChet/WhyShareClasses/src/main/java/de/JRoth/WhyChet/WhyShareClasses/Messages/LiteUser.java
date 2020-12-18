package de.JRoth.WhyChet.WhyShareClasses.Messages;

import de.JRoth.WhyServer.User;

public class LiteUser {
    private String username;
    private boolean isOnline;

    public LiteUser(User user){
        username = user.getName();
        isOnline = user.isOnline();
    }

    public String getName() {
        return username;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
