package de.JRoth.WhyChet.WhyShareClasses.Messages;

public class LoginMessage extends Message{
    private String userName;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    private String password;
    public LoginMessage(String sender, String content, long roomID) {
        super(sender, content, roomID);
        this.userName = "";
        this.password = "";
    }
    public LoginMessage(String userName, String password){
        super("Uninitialized Client", userName, -7);
        this.userName = userName;
        this.password = password;
    }
}
