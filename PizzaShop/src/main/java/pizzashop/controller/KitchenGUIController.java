package pizzashop.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.apache.log4j.Logger;

import java.time.LocalTime;
import java.util.Locale;


@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1107","java:S1109","java:S1104","java:S1155"})
public class KitchenGUIController {
    @FXML
    private ListView kitchenOrdersList;
    @FXML
    public Button cook;
    @FXML
    public Button ready;

    private static final Logger LOGGER=Logger.getLogger(KitchenGUIController.class);
    private static final  String DELIMITER="--------------------------";


    protected static final ObservableList<String> order = FXCollections.observableArrayList();
    private Object selectedOrder;
    private LocalTime now = LocalTime.now();
    private String extractedTableNumberString;
    private int extractedTableNumberInteger;

    private static final Integer CONST100=100;
    private static  final Integer CONST5=5;
    private static  final Integer CONST6=6;

    private Runnable runnable = ()->{ while (true) {
        Platform.runLater(() -> { kitchenOrdersList.setItems(order);
            if(order.size() > 0 ) { cook.setDisable(false); ready.setDisable(false);
            } else {
                cook.setDisable(true); ready.setDisable(true); }});
        try {
            Thread.sleep(CONST100);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }}};

    //thread for adding data to kitchenOrderList
    public Thread addOrders = new Thread(runnable);

    public KitchenGUIController() {
        //Implicit constructor
    }


    public void initialize() {
        cook.setDisable(true);
        ready.setDisable(true);

        //starting thread for adding data to kitchenOrderList
        addOrders.setDaemon(true);
        addOrders.start();
        //Controller for Cook Button
        cook.setOnAction((ActionEvent event) -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            if(selectedOrder != null) { kitchenOrdersList.getItems().remove(selectedOrder);
                kitchenOrdersList.getItems().add(selectedOrder.toString()
                        .concat(" Cooking started at: ").
                                toUpperCase(Locale.ENGLISH).concat(now.getHour() + ":" + now.getMinute()));
            } else {
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select the order your want to cook!");a.show();
            }});
        //Controller for Ready Button
        ready.setOnAction((ActionEvent event) -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            if(selectedOrder !=null) {
                kitchenOrdersList.getItems().remove(selectedOrder);
                extractedTableNumberString = selectedOrder.toString().subSequence(CONST5, CONST6).toString();
                extractedTableNumberInteger = Integer.valueOf(extractedTableNumberString);
                LOGGER.info(DELIMITER + "\n" + "Table " + extractedTableNumberInteger
                        + " ready at: " + now.getHour() + ":" + now.getMinute() + "\n" + DELIMITER);
            } else{ Alert a=new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select the order that is ready!"); a.show(); }});
    }
}
