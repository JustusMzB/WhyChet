package de.JRoth.WhyChet.WhyShareClasses.Messages;


import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiteUser liteUser = (LiteUser) o;
        return username.equals(liteUser.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
