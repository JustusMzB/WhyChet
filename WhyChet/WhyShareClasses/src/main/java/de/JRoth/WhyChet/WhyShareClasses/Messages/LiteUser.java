package de.JRoth.WhyChet.WhyShareClasses.Messages;


import java.io.Serializable;

public class LiteUser implements Serializable {
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

    public void setOnline(boolean online) {
        this.isOnline = online;
    }
}
