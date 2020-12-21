package de.JRoth.WhyServer;

import java.io.InputStream;
import java.util.Scanner;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
public class ConsoleServerController extends ServerOrderService {
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
                    target.displayType().log("[ORDERSERVICE] unknown order was issued from console");
            }
        }
    }
}
