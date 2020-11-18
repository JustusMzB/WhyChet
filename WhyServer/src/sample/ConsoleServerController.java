package sample;

import java.io.InputStream;
import java.util.Scanner;

public class ConsoleServerController extends OrderService {
    private final Scanner in;
    private boolean closeSignal;
    private Server target;

    public ConsoleServerController(Server target) {
        super(target);
        this.target = target;
        in = new Scanner(System.in);
    }

    public ConsoleServerController(Server target, InputStream is) {
        super(target);
        in = new Scanner(is);
    }

    public void giveOrder(Message order){
        switch ((int) order.getRoomID()) {
            case -1:
                target.disconnectUser(order.getContent());
                break;
            case -2:
                target.terminate();
                break;
            default:
                target.logType().log("[SERVER] Received unknown order");
        }
    }
    @Override
    public void instruct() {
        String entry = in.nextLine();
        entry = entry.trim();
        Message order;
        String[] tokenizedEntry = entry.split(" ");
        if (tokenizedEntry[0].startsWith("/")) {
            switch (tokenizedEntry[0]) {
                case "/disconnect":
                    order = new Message("Console", tokenizedEntry[1], -1);
                    giveOrder(order);
                    break;
                case "/close":
                    order = new Message("Console", "", -2);
                    giveOrder(order);
                    closeSignal = true;
                    break;
                default:
                    target.logType().log("[ORDERSERVICE] unknown order was issued from console");
            }
        }
    }
}
