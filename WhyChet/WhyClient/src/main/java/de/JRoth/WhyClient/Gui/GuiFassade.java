package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyClient.Client;
import de.JRoth.WhyClient.UI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

public class GuiFassade implements UI, Initializable {
    @FXML
    RadioButton rbOnline;
    @FXML
    VBox vbxUsers;
    @FXML
    ScrollPane scrllpnUsers;
    @FXML
    ScrollPane scrllpnRooms;
    @FXML
    TextArea txtrWriteMessage;
    @FXML
    ScrollPane scrllpnMessages;
    @FXML
    Label txtUsername;
    @FXML
    Label lblRoomName;


    private Messages messageControls = new Messages();
    private RoomControls roomControls;
    private UserControls userControls;
    private Client client;

    public void bootStrap(Client client){
        this.setClient(client);
        roomControls = new RoomControls();
        roomControls.bootStrap(client);
        scrllpnRooms.setContent(roomControls);

        userControls = new UserControls();
        scrllpnUsers.setContent(userControls);
    }

    @FXML
    void sendMessageOnEnter(KeyEvent event){
        //ShiftEnter allows line break, Enter allows message send
        if(event.getCode() == KeyCode.ENTER){
            if(event.isControlDown()){
                sendMessage(event);
            }
        }
    }

    @FXML
    void sendMessage(Event event){
        String content = txtrWriteMessage.getText();
        client.sendMessage(content);
        txtrWriteMessage.setText("");
    }


    @Override
    public String[] getLoginData() {
        TextInputDialog nameGetter = new TextInputDialog();
        nameGetter.setHeaderText("Data for login");
        nameGetter.setContentText("Username");
        TextInputDialog pwGetter = new TextInputDialog();
        pwGetter.setHeaderText("Data for login");
        pwGetter.setContentText("Password");

            nameGetter.showAndWait();
            pwGetter.showAndWait();


        return new String[]{nameGetter.getResult(), pwGetter.getResult()};
    }

    @Override
    public void out(String notice) {
        System.out.println(notice);
    }

    @Override
    public void errOut(String errorNotice) {
        System.err.println(errorNotice);
    }

    @Override
    public void displayMessage(Message message) {
        messageControls.addMessage(message);
    }

    @Override
    public void userUpdate(LiteUser user) {
        userControls.putUser(user);
    }

    @Override
    public void roomUpdate(LiteRoom room) {
        roomControls.updateRoom(room);
    }

    @Override
    public void addRoom(LiteRoom room) {
        roomControls.updateRoom(room);
    }

    @Override
    public void deleteRoom(LiteRoom room) {
        roomControls.deleteRoom(room);
    }

    @Override
    public void logOff() {
        Platform.runLater(() -> {
            txtUsername.setText("Not logged in");
            lblRoomName.setText("");
            rbOnline.setDisable(false);
            messageControls.getChildren().clear();
        });

        roomControls.clear();
        userControls.clear();

    }

    @Override
    public void setUsername(String userName) {
        Platform.runLater(() -> txtUsername.setText(userName));
    }

    @Override
    public void setMyRoom(LiteRoom myRoom) {
        roomUpdate(myRoom);

        Platform.runLater(() -> {
            messageControls.getChildren().clear();
            lblRoomName.setText(myRoom.getRoomName());
        });
        userControls.setVisibleUsers(myRoom.getUsers());

    }

    @Override
    public void deleteUser(LiteUser leavingUser) {
        userControls.deleteUser(leavingUser);
        out(leavingUser.getName() + " User is being removed");
    }

    @FXML
    private void toggleOnline(ActionEvent event) {
        if(!client.isLoggedIn()){
            rbOnline.setSelected(client.isLoggedIn());
            client.login();
            rbOnline.setSelected(client.isLoggedIn());
        } else {
            rbOnline.setSelected(client.isLoggedIn());
            client.closeConnection();
            rbOnline.setSelected(client.isLoggedIn());
        }
    }

    private void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrllpnMessages.setContent(messageControls);
    }
}
