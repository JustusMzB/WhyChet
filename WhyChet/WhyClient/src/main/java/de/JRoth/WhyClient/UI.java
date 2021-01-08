package de.JRoth.WhyClient;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;

public interface UI {
    String[] getLoginData();
    void out(String notice);
    void errOut(String errorNotice);
    void displayMessage(Message message);
    void userUpdate(LiteUser user);
    void roomUpdate(LiteRoom room);
    void addRoom(LiteRoom room);


    void deleteRoom(LiteRoom room);

    void logOff();

    void setUsername(String userName);
}
