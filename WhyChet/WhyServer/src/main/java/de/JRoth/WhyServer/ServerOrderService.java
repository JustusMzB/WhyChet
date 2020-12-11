package de.JRoth.WhyServer;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
public class ServerOrderService extends OrderService {
    Server target;
    public ServerOrderService(Server target) {
        this.target = target;
    }

    @Override
    public void instruct() {

    }

    @Override
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
}
