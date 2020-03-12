package pizzashop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import pizzashop.controller.MainGUIController;
import pizzashop.gui.KitchenGUI;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;
import pizzashop.service.PizzaService;

import java.util.Optional;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1109","java:S1107"})
public class Main extends Application {
    private static final Logger LOGGER=Logger.getLogger(Main.class);


    @Override
    public void start(Stage primaryStage) throws Exception{

        MenuRepository repoMenu=new MenuRepository();
        PaymentRepository payRepo= new PaymentRepository();
        PizzaService service = new PizzaService(repoMenu, payRepo);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mainFXML.fxml"));
        Parent box = loader.load();
        MainGUIController ctrl = loader.getController();
        ctrl.setService(service);
        primaryStage.setTitle("PizeriaX");
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Would you like to exit the Main window?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                LOGGER.info("Incasari cash: "+service.getTotalAmount(PaymentType.Cash));
                LOGGER.info("Incasari card: "+service.getTotalAmount(PaymentType.Card));primaryStage.close();
            }
            // consume event
            else if (result.get() == ButtonType.NO){ event.consume(); }
            else { event.consume(); }});
        primaryStage.setScene(new Scene(box));
        primaryStage.show();
        KitchenGUI kitchenGUI = new KitchenGUI();
        kitchenGUI.kitchenGui();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch(args);
    }
}
