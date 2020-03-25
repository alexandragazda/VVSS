package pizzashop.controller;

import org.apache.log4j.Logger;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import pizzashop.model.MenuDataModel;
import pizzashop.model.validator.ValidationException;
import pizzashop.service.PaymentAlert;
import pizzashop.service.PizzaService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1109","java:S1200","java:S1450","java:S1104"})
public class OrdersGUIController {

    @FXML
    private ComboBox<Integer> orderQuantity;
    @FXML
    private TableView orderTable;
    @FXML
    private TableColumn tableQuantity;
    @FXML
    protected TableColumn tableMenuItem;
    @FXML
    private TableColumn tablePrice;
    @FXML
    private Label pizzaTypeLabel;
    @FXML
    private Button addToOrder;
    @FXML
    private Label orderStatus;
    @FXML
    private Button placeOrder;
    @FXML
    private Button orderServed;
    @FXML
    private Button payOrder;
    @FXML
    private Button newOrder;

    private   List<String> orderList = FXCollections.observableArrayList();
    private static  final Logger LOGGER=Logger.getLogger(OrdersGUIController.class);
        private static final Integer CONST0=0;
    private static final Integer CONST1=1;
    private static final Integer CONST2=2;
    private static final Integer CONST3=3;
    private static final Integer CONST4=4;
    private static final Integer CONST5=5;
    private List<Double> orderPaymentList = FXCollections.observableArrayList();
    private PizzaService service;
    private int tableNumber;

    public ObservableList<String> observableList;
    private TableView<MenuDataModel> table = new TableView<>();
    private ObservableList<MenuDataModel> menuData;
    private LocalTime now = LocalTime.now();
    private static double totalAmount;

    public OrdersGUIController(){
        //Implicit constructor
    }
    public static double getTotalAmount() {
        return totalAmount;
    }
    public static void setTotalAmount(double totalAmount) {
        OrdersGUIController.totalAmount = totalAmount;
    }



    public void setService(PizzaService service, int tableNumber){
        this.service=service;
        this.tableNumber=tableNumber;
        initData();

    }

    private void initData(){
        menuData = FXCollections.observableArrayList(service.getMenuData());
        menuData.setAll(service.getMenuData());
        orderTable.setItems(menuData);

        //Controller for Place Order Button
        placeOrder.setOnAction((ActionEvent event) ->{
            orderList= menuData.stream()
                    .filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity() +" "+ menuDataModel.getMenuItem())
                    .collect(Collectors.toList());
            observableList = FXCollections.observableList(orderList);
            KitchenGUIController.order.add("Table" + tableNumber +" "+ orderList.toString());
            orderStatus.setText("Order placed at: " +  now.getHour()+":"+now.getMinute());
        });

        //Controller for Order Served Button
        orderServed.setOnAction((ActionEvent event) -> orderStatus.setText("Served at: "
                + now.getHour()+":"+now.getMinute())
        );

        //Controller for Pay Order Button
        payOrder.setOnAction((ActionEvent event) -> {
            orderPaymentList= menuData.stream().filter(x -> x.getQuantity()>0)
                    .map(menuDataModel -> menuDataModel.getQuantity()*menuDataModel.getPrice())
                    .collect(Collectors.toList());
            setTotalAmount(orderPaymentList.stream().mapToDouble(Double::doubleValue).sum());
            orderStatus.setText("Total amount: " + getTotalAmount());
            LOGGER.info("--------------------------");LOGGER.info("Table: " + tableNumber);
            LOGGER.info("Total: " + getTotalAmount());LOGGER.info("--------------------------");
            try {
                PaymentAlert pay = new PaymentAlert(service);
                pay.showPaymentAlert(tableNumber, getTotalAmount());
            }catch (ValidationException ex){
                LOGGER.error(ex.getMessage());
            }
        });
    }

    public void initialize(){

        //populate table view with menuData from OrderGUI
        table.setEditable(true);
        tableMenuItem.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, String>("menuItem"));
        tablePrice.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Double>("price"));
        tableQuantity.setCellValueFactory(
                new PropertyValueFactory<MenuDataModel, Integer>("quantity"));

        //bind pizzaTypeLabel and quantity combo box with the selection on the table view
        orderTable
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((ChangeListener<MenuDataModel>) (
                        ObservableValue<?extends  MenuDataModel> observable,
                        MenuDataModel oldValue,
                        MenuDataModel newValue
                ) ->
                        pizzaTypeLabel.textProperty().bind(newValue.menuItemProperty()));
        //Populate Combo box for Quantity
        ObservableList<Integer> quantityValues =  FXCollections.observableArrayList(
                CONST0,CONST1,CONST2,CONST3,CONST4,CONST5);
        orderQuantity.getItems().addAll(quantityValues);
        orderQuantity.setPromptText("Quantity");

        //Controller for Add to order Button
        addToOrder.setOnAction((ActionEvent event) -> {
            if(orderQuantity.getValue() !=null ) {
                orderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<MenuDataModel>() {
                    public void changed(ObservableValue<? extends MenuDataModel> observable, MenuDataModel oldValue,
                                        MenuDataModel newValue) { oldValue.setQuantity(orderQuantity.getValue());
                        orderTable.getSelectionModel().selectedItemProperty().removeListener(this);
                    }
                });} else{
                Alert a=new Alert(Alert.AlertType.WARNING);
                a.setContentText("Please select the quantity!");a.show();} });

        //Controller for Exit table Button
        newOrder.setOnAction((ActionEvent event) -> {
            Alert exitAlert = new Alert(Alert.AlertType.CONFIRMATION, "Exit table?",ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = exitAlert.showAndWait();
            if (result.get() == ButtonType.YES){
                Stage stage = (Stage) newOrder.getScene().getWindow();
                stage.close();
            }
        });
    }
}
