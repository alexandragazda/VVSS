package pizzashop.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import  javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pizzashop.gui.OrdersGUI;
import pizzashop.service.PizzaService;



@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1109"})
public class MainGUIController  {
    @FXML
    private Button table1;
    @FXML
    private Button table2;
    @FXML
    private Button table3;
    @FXML
    private Button table4;
    @FXML
    private Button table5;
    @FXML
    private Button table6;
    @FXML
    private Button table7;
    @FXML
    private Button table8;
    @FXML
    private MenuItem help;

    private OrdersGUI table1Orders = new OrdersGUI();
    private OrdersGUI  table2Orders = new OrdersGUI();
    private OrdersGUI  table3Orders = new OrdersGUI();
    private OrdersGUI  table4Orders = new OrdersGUI();
    private OrdersGUI  table5Orders = new OrdersGUI();
    private OrdersGUI  table6Orders = new OrdersGUI();
    private OrdersGUI  table7Orders = new OrdersGUI();
    private OrdersGUI  table8Orders = new OrdersGUI();

    private PizzaService service;

    private static  final  Integer CONST1=1;
    private static  final  Integer CONST2=2;
    private static  final  Integer CONST3=3;
    private static  final  Integer CONST4=4;
    private static  final  Integer CONST5=5;
    private static  final  Integer CONST6=6;
    private static  final  Integer CONST7=7;
    private static  final  Integer CONST8=8;
    private static  final  Integer CONST15=15;



    public MainGUIController(){
        //Implicit constructor
    }

    public void setService(PizzaService service){
        this.service=service;
        tableHandlers();
    }

    private void tableHandlers(){
        table1.setOnAction((ActionEvent event) -> {
            table1Orders.setTableNumber(CONST1);
            table1Orders.displayOrdersForm(service);
        });
        table2.setOnAction((ActionEvent event) -> {
            table2Orders.setTableNumber(CONST2);
            table2Orders.displayOrdersForm(service);
        });
        table3.setOnAction((ActionEvent event) -> {
            table3Orders.setTableNumber(CONST3);
            table3Orders.displayOrdersForm(service);
        });
        table4.setOnAction((ActionEvent event) -> {
            table4Orders.setTableNumber(CONST4);
            table4Orders.displayOrdersForm(service);
        });
        table5.setOnAction((ActionEvent event) -> {
            table5Orders.setTableNumber(CONST5);
            table5Orders.displayOrdersForm(service);
        });
        table6.setOnAction((ActionEvent event) -> {
            table6Orders.setTableNumber(CONST6);
            table6Orders.displayOrdersForm(service);
        });
        table7.setOnAction((ActionEvent event) -> {
            table7Orders.setTableNumber(CONST7);
            table7Orders.displayOrdersForm(service);
        });
        table8.setOnAction((ActionEvent event) -> {
            table8Orders.setTableNumber(CONST8);
            table8Orders.displayOrdersForm(service);
        });

    }


    public void initialize(){
        String s1="1. Click on a Table button - a table order form will pop up. ";
        String s2="2.Choose a Menu item from the menu list, choose Quantity from drop down list,";
        String s3="press Add to order button and Click on the Menu list (Repeat)";
        String s4="3.Press Place order button, the order will appear in the Kitchen window";
        String s5="4.On the Kitchen window Click on the order in the Orders List and Press the Cook button, ";
        String s6="then after Click on the order in the Orders list and then on the Ready button";
        String s7="5.On the Table order form press the Order served button, at the end press";
        String s8="the Pay order button ";
        String s=System.lineSeparator();
        help.setOnAction((ActionEvent event) -> {
            Stage stage = new Stage(); stage.setTitle("How to use..");
            final Group rootGroup = new Group();
            final Scene scene = new Scene(rootGroup, 600, 250);
            final Text text1 = new Text(25, 25,
                    s1 + s + s + s2 + s + s3 + s +s + s4 + s+s + s5 + s +s6 + s +s + s7+ s+ s8 +s);
            text1.setFont(Font.font(java.awt.Font.SERIF, CONST15 ) );
            rootGroup.getChildren().add(text1 );
            stage.setScene(scene); stage.setResizable(false); stage.showAndWait();});
    }
}
