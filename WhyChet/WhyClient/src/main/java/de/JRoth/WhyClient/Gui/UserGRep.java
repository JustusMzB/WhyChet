package de.JRoth.WhyClient.Gui;

import de.JRoth.WhyChet.WhyShareClasses.Messages.LiteUser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class UserGRep {
    @FXML
    RadioButton rbOnline;
    @FXML
    Text txtUsername;

    void updateOnline(boolean online){
        rbOnline.setSelected(online);
    }

    static Component makeUserComponent(LiteUser user){
        FXMLLoader loader = new FXMLLoader(UserGRep.class.getResource("userview.fxml"));
        Node node = null;
        UserGRep controls = null;
        try {
            node = loader.load();
            controls = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        controls.updateOnline(user.isOnline());
        controls.txtUsername.setText(user.getName());
        return new Component(node, controls);
    }


}
