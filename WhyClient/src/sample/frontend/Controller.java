package sample.frontend;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import sample.backend.Sender;

public class Controller {

    @FXML
    private TextArea ta_Input;
    @FXML
    private TextArea ta_Output;
    @FXML
    private Button btn_Send;
    @FXML
    private TableView tv_UserTable;
    @FXML
    private TableView tv_RoomTable;
    @FXML
    private TableColumn tc_UserTable_user;
    @FXML
    private TableColumn tc_UserTable_room;

    @FXML
    private void SendMessage(ActionEvent event){
        String content = ta_Input.getText();
        //ta_Input.deleteText(message.length());
        ta_Input.selectAll();
        ta_Input.replaceSelection("Enter your message here...");
        Sender.sendMessage(content);
    }

    //wenn listener chat übermittelt
    @FXML
    private void receiveMessage(){

    }

    //wenn listener statusänderungen von usern übermittelt/ wenn listener change in HashMap user übermittelt
    @FXML
    private void updateUserTable(){

    }


    //wenn listener änderung aus linkedlist rooms übermittelt
    @FXML
    private void updateRoomTalbe(){

    }

}
