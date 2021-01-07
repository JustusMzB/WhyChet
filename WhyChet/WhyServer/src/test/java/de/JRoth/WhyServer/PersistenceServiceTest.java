package de.JRoth.WhyServer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceServiceTest {
    private PersistenceService testedService = new FileStreamPersistence();
    @Test
    void testUserLoadAndStore(){
        List<User> testUserList = new ArrayList<>();
        testUserList.add(new User("User1", "User1".hashCode(), null));
        Map<String, User> UserResultList;
        testedService.storeUsers(testUserList);

        UserResultList = testedService.loadUsers(null);

        for(User i : testUserList){
            assertEquals(UserResultList.get(i.getName()).getPassHash(), i.getPassHash());
        }

    }
}