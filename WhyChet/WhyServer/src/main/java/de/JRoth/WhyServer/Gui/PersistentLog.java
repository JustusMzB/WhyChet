package de.JRoth.WhyServer.Gui;

import java.io.*;

public class PersistentLog implements LogService{
    private BufferedWriter logstream;
    PersistentLog(){
        try {
            logstream = new BufferedWriter(new FileWriter("./log"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void log(String logEntry) {
        try {
            logstream.write(logEntry + "\n");
            logstream.flush();
        } catch (IOException e) {
            System.out.println("Logfile not accessible");
            System.out.println(logEntry);
        }
    }

    @Override
    public void errLog(String logError) {
        try {
            logstream.write(logError + "\n");
            logstream.flush();
        } catch (IOException e) {
            System.out.println("Logfile not accessible");
            System.out.println(logError);
        }
    }
}
