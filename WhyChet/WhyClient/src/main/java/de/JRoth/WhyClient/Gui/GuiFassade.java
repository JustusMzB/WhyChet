package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteRoom;
import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import de.JRoth.WhyChet.WhyShareClasses.Messages.Message;
import de.JRoth.WhyClient.Client;
import de.JRoth.WhyClient.UI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
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
    VBox vbxRooms;
    @FXML
    TextArea txtrWriteMessage;
    @FXML
    ScrollPane scrllpnMessages;
    @FXML
    Text txtUsername;


    private Messages messageControls = new Messages();
    private RoomControls roomControls;
    private UserControls userControls;
    private Client client;




    @FXML
    void sendMessage(ActionEvent event){
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

    }

    @Override
    public void roomUpdate(LiteRoom room) {

    }

    @Override
    public void addRoom(LiteRoom room) {

    }

    @Override
    public void deleteRoom(LiteRoom room) {

    }

    @Override
    public void logOff() {

    }

    @Override
    public void setUsername(String userName) {
        Platform.runLater(() -> txtUsername.setText(userName));
    }

    @FXML
    private void toggleOnline(ActionEvent event) {
        if(rbOnline.isSelected()){
            client.login();
            rbOnline.setDisable(true);
        }
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrllpnMessages.setContent(messageControls);
    }
}
