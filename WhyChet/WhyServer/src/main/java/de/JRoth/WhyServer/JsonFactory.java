package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Collection;
import java.util.List;

public class JsonFactory {

    static public JSONObject makeJson(Room room){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", room.getName());
        jsonObject.put("id", room.getId());

        return jsonObject;
    }

    static public JSONArray makeJsonUsers(Collection<User> users){
        JSONArray jUsers = new JSONArray();
        users.forEach(user -> jUsers.put(makeJson(user)));
        return jUsers;
    }

    private static JSONObject makeJson(User user) {
        JSONObject jUser = new JSONObject();
        jUser.put("name", user.getName());
        jUser.put("pw", user.getPassHash());
        return jUser;
    }

    static public JSONArray makeJsonRooms(List<Room> rooms){
        JSONArray array = new JSONArray();
        rooms.forEach(room -> {
            array.put(makeJson(room));
        });
        return array;
    }
}
