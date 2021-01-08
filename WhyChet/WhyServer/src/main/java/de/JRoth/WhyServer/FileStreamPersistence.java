package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* public class FileStreamPersistence implements PersistenceService{
    File roomsFile;
    File loginsFile;

    FileStreamPersistence(){
        this.roomsFile = new File("rooms");
        this.loginsFile = new File("lgns");
    }
    @Override
    public void storeRooms(List<Room> rooms) {

    }

    @Override
    public void storeUsers(List<User> users){

        LinkedList<Login> loginList = new LinkedList<>();
        for (User i : users){
            loginList.add(new Login(i));
        }
        try {
            //Clearing old data
            loginsFile.delete();
            loginsFile.createNewFile();

            //Writing new data
            ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(loginsFile));
            objOut.writeObject(loginList);
        } catch (IOException e) {
            System.out.println("Failed to store UserData");
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, User> loadUsers(Server server) {
        List<Login> logins = new LinkedList<>();
        Map<String, User> results = new HashMap<>();

        try {
            loginsFile.createNewFile(); //Making sure the file exists
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(loginsFile));

            logins = (List<Login>) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            server.displayType().errLog("[PERSISTENCE] Users could not be loaded, returning empty user map");
            e.printStackTrace();
            return results; //Returning empty Users list
        }
        for (Login i : logins){
            results.put(i.getUserName(), new User(i.getUserName(), i.passHash, server));
        }
        return results;
    }

    @Override
    public List<Room> loadRooms(Server server) {
        return null;
    }

    private class SerializeableRoom implements Serializable {
        public LinkedList<LiteUser> getMembers() {
            return members;
        }

        public LinkedList<Message> getMessages() {
            return messages;
        }

        SerializeableRoom(Room room){
            this.messages = new LinkedList<Message>(room.getChat());
            this.members = new LinkedList<LiteUser>(LiteObjectFactory.makeLite(room.getMembers()));
        }

        private LinkedList<LiteUser> members;
        private LinkedList<Message> messages;
    }
    private class Login implements Serializable {
        private int passHash;
        private String userName;

        public Login(User user) {
            this.passHash = user.getPassHash();
            this.userName = user.getName();
        }

        public String getUserName() {
            return userName;
        }

        public int getPassHash() {
            return passHash;
        }
    }
}
*/