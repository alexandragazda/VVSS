package pizzashop.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import java.util.Calendar;


public class KitchenGUIController {
    @FXML
    private ListView kitchenOrdersList;
    @FXML
    public Button cook;
    @FXML
    public Button ready;

    public static  ObservableList<String> order = FXCollections.observableArrayList();
    private Object selectedOrder;
    private Calendar now = Calendar.getInstance();
    private String extractedTableNumberString = new String();
    private int extractedTableNumberInteger;
    //thread for adding data to kitchenOrderList
    public  Thread addOrders = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        kitchenOrdersList.setItems(order);
                        if(order.size() > 0 ) {
                            cook.setDisable(false);
                            ready.setDisable(false);
                        }
                        else{
                            cook.setDisable(true);
                            ready.setDisable(true);
                        }
                    }
                });
                try {
                    Thread.sleep(100);
                  } catch (InterruptedException ex) {
                    break;
                }
            }
        }
    });

    public void initialize() {
        cook.setDisable(true);
        ready.setDisable(true);

        //starting thread for adding data to kitchenOrderList
        addOrders.setDaemon(true);
        addOrders.start();
        //Controller for Cook Button
        cook.setOnAction(event -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            if(selectedOrder != null) {
                kitchenOrdersList.getItems().remove(selectedOrder);
                kitchenOrdersList.getItems().add(selectedOrder.toString()
                        .concat(" Cooking started at: ").toUpperCase()
                        .concat(now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE)));
            }
            else{
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select the order your want to cook!");
                a.show();
            }
        });
        //Controller for Ready Button
        ready.setOnAction(event -> {
            selectedOrder = kitchenOrdersList.getSelectionModel().getSelectedItem();
            if(selectedOrder !=null) {
                kitchenOrdersList.getItems().remove(selectedOrder);
                extractedTableNumberString = selectedOrder.toString().subSequence(5, 6).toString();
                extractedTableNumberInteger = Integer.valueOf(extractedTableNumberString);
                System.out.println("--------------------------");
                System.out.println("Table " + extractedTableNumberInteger + " ready at: " + now.get(Calendar.HOUR) + ":" + now.get(Calendar.MINUTE));
                System.out.println("--------------------------");
            }
            else{
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select the order that is ready!");
                a.show();
            }
        });
    }
}