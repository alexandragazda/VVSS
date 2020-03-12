package pizzashop.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.Optional;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1107"})
public class KitchenGUI {
    private static final Logger LOGGER=Logger.getLogger(KitchenGUI.class);


    public void kitchenGui() {
        VBox vBoxKitchen = null;

        try {
            vBoxKitchen = FXMLLoader.load(getClass().getResource("/fxml/kitchenGUIFXML.fxml"));
        } catch (IOException e) {
            LOGGER.error(e);
        }

        Stage stage = new Stage();
        stage.setTitle("Kitchen");
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent event) -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Would you like to exit Kitchen window?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){ stage.close(); }
            else if (result.get() == ButtonType.NO){ event.consume(); }
            else { event.consume(); }
        });
        stage.setAlwaysOnTop(false);
        stage.setScene(new Scene(vBoxKitchen));
        stage.show();
        stage.toBack();
    }
}

