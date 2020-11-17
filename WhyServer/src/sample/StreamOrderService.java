package sample;

import java.io.InputStream;
import java.util.Scanner;

public class StreamOrderService extends OrderService {
    private final Scanner in;

    public StreamOrderService(Server target) {
        super(target);
        in = new Scanner(System.in);
    }

    public StreamOrderService(Server target, InputStream is) {
        super(target);
        in = new Scanner(is);
    }

    @Override
    public void giveOrder() {
        String entry = in.nextLine();
        entry = entry.trim();
        Message order;
        String[] tokenizedEntry = entry.split(" ");
        if (tokenizedEntry[0].startsWith("/")) {
            switch (tokenizedEntry[0]) {
                case "/disconnect":
                    order = new Message("Console", tokenizedEntry[1], -1);
                    target.takeOrder(order);
                    break;
                case "/close":
                    order = new Message("Console", "", -2);
                    target.takeOrder(order);
                    closeSignal = true;
                    break;
                default:
                    target.logType().log("[ORDERSERVICE] unknown order was issued from console");
            }
        }
    }
}
