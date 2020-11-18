package sample;

import java.util.concurrent.atomic.AtomicBoolean;

public class Terminateable extends Thread {
    public AtomicBoolean getRunning() {
        return running;
    }

    private AtomicBoolean running = new AtomicBoolean(false);

    /*A Method that safely terminates the thread with all its sub-threads*/
    public void terminate(){
        running.set(false);
    }
}
