package sample;

public abstract class OrderService {
    protected Server target;
    protected boolean closeSignal;

    public OrderService(Server target) {
        this.target = target;
    }

    public abstract void giveOrder();

    public boolean closeSignal() {
        return closeSignal;
    }
}
