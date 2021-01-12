package de.JRoth.WhyServer.Gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersistentLogTest {
    @Test
    void logTest(){
        LogService log = new PersistentLog();
        log.log("Testmessage");
        log.errLog("Testerror");
    }

}