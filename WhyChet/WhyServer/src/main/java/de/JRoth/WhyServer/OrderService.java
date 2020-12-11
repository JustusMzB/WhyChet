package de.JRoth.WhyServer;

import de.JRoth.WhyChet.WhyShareClasses.Messages.*;

/* Abstract class that implements a Control source for a server.
Instruct gets orders from somewhere and accesses server functions accordingly.
Each class allows standardized control via giveOrder(). Orders need to follow the Message-Order standards.
*/
public abstract class OrderService {
    private boolean closeSignal;

    public abstract void instruct();
    public abstract void giveOrder(Message order);

    public boolean closeSignal() {
        return closeSignal;
    }

    public void setCloseSignal(boolean closeSignal) {
        this.closeSignal = closeSignal;
    }
}
