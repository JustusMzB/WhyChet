package sample;

public class Main /*extends Application*/ {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }*/


    public static void main(String[] args) {
        //launch(args);
        Server myServer = new Server(1969);
        myServer.start();
        OrderService orderService = new StreamOrderService(myServer);
        while (!orderService.closeSignal) {
            orderService.giveOrder();
        }
    }
}
