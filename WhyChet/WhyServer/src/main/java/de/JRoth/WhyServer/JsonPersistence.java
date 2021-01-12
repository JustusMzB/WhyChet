package de.JRoth.WhyServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JsonPersistence implements PersistenceService{
    File rooms;
    File users;

    JsonPersistence(){
        rooms = new File("rooms.txt");
        users = new File("users.txt");

    }

    @Override
    public void storeRooms(List<Room> rooms) {
        try {
            this.rooms.delete(); //ensuring the emptyness of rooms
            FileWriter fw = new FileWriter(this.rooms);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(JsonFactory.makeJsonRooms(rooms).toString(5));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void storeUsers(Map<String, User> users) {
        try {
            this.users.delete(); //Ensuring the emptieness of users
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.users));
            bw.write(JsonFactory.makeJsonUsers(users.values()).toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, User> loadUsers(Server server) {
        try {
            String input = Files.readString(users.toPath());
            if(input.isEmpty()){
                return null;
            }
            JSONArray jusers = new JSONArray(input);
            HashMap<String, User> result = new HashMap<>();
            jusers.forEach(juser -> {
                User user = new User(((JSONObject)juser).getString("name"), ((JSONObject)juser).getInt("pw"), server);
                if(((JSONObject) juser).getBoolean("isBanned")){
                    user.ban();
                }
                result.put(user.getName(), user);
            });
            return result;
        } catch (IOException e) {
            server.displayType().log("Users could not be loaded, probably, none were stored.");
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<Room> loadRooms(Server server) {

        try {
            String input = Files.readString(rooms.toPath());
            if(input.isEmpty()){
                return null;
            }
            LinkedList<Room> result = new LinkedList<>();
            JSONArray jRooms = new JSONArray(input);
            jRooms.forEach(jRoom -> {
                String roomName = ((JSONObject)jRoom).getString("name");
                int roomID = ((JSONObject)jRoom).getInt("id");
                result.add(new Room(roomID,roomName, server));
            });
            return result;
        } catch (IOException e) {
            server.displayType().log("Rooms could not be loaded. Likely, none were stored.");
            e.printStackTrace();
            return null;
        }
    }
}
