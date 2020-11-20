package sample;

public class ConsoleLog implements LogService {
    @Override
    public void log(String logEntry) {
        System.out.println(logEntry);
    }

    @Override
    public void errLog(String logEntry) {
        System.err.println(logEntry);
    }
}
