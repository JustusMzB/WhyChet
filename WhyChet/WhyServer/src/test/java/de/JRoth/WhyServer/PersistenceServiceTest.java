package de.JRoth.WhyServer;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceServiceTest {
    private PersistenceService testedService = new JsonPersistence();

    @Test
    void testRoomLoadAndStore() {
        List<Room> testRoomList = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            testRoomList.add(i, new Room((int) (Math.random() * 10000), String.valueOf(Math.random()), null));
        }
        testedService.storeRooms(testRoomList);
        List<Room> resultList = testedService.loadRooms(null);
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(testRoomList.get(i).getName(), resultList.get(i).getName());
            assertEquals(testRoomList.get(i).getId(), resultList.get(i).getId());
        }

    }

    @Test
    void testUserLoadAndStore() {
        HashMap<String, User> testUserList = new HashMap<>();
        testUserList.put("User1", new User("User1", "User1".hashCode(), null));
        for (int i = 0; i <= 100; i++) {
            testUserList.put(String.valueOf(i), new User(String.valueOf(i), String.valueOf(i).hashCode(), null));
        }
        Map<String, User> UserResultList;
        testedService.storeUsers(testUserList);

        UserResultList = testedService.loadUsers(null);

        for (User i : testUserList.values()) {
            assertEquals(UserResultList.get(i.getName()).getPassHash(), i.getPassHash());
        }

    }
}