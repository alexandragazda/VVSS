package pizzashop.service;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.*;
import pizzashop.model.PaymentType;
import pizzashop.model.validator.ValidationException;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class PizzaServiceTest_WBT {

    private static MenuRepository menuRepository;
    private static PaymentRepository paymentRepository;
    private static PizzaService pizzaService;

    private static final String SERVICEEXEPTIONMSG="Type has to be specified!";

    @BeforeAll
    static void init(){
        BasicConfigurator.configure();
        menuRepository= new MenuRepository();
        paymentRepository= new PaymentRepository("data/paymentsTest-WBT");
        pizzaService= new PizzaService(menuRepository,paymentRepository);
    }


    @AfterEach
    void end(){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter("target/test-classes/data/paymentsTest-WBT"))){
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }

        paymentRepository= new PaymentRepository("data/paymentsTest-WBT");
        pizzaService= new PizzaService(menuRepository,paymentRepository);
    }

    @Test
    @DisplayName("WBT-non-valid")
    void test1() {
        try {
            pizzaService.addPayment(4,PaymentType.Card,10.0);
            assertTrue(pizzaService.getPayments().size() == 1);
            assertTrue(pizzaService.getPayments().get(0).getType() == PaymentType.Card);
            pizzaService.getTotalAmount(null);
        } catch (ServiceException ex) {
            assertTrue(ex.getMessage().equals(SERVICEEXEPTIONMSG));
        }
    }

    @Test
    @DisplayName("WBT-valid1")
    void test2() {
        assertTrue(pizzaService.getPayments().isEmpty());
        assertTrue(pizzaService.getTotalAmount(PaymentType.Card) == 0.0F);
    }

    @Test
    @DisplayName("WBT-valid2")
    void test3() {
        pizzaService.addPayment(3,PaymentType.Card,10.0);
        pizzaService.addPayment(2,PaymentType.Card,13.5);
        pizzaService.addPayment(8,PaymentType.Card,20.0);

        assertTrue(pizzaService.getPayments().size() == 3);
        assertTrue(pizzaService.getPayments().get(1).getType() == PaymentType.Card);

        assertTrue(pizzaService.getTotalAmount(PaymentType.Cash) == 0.0F);
    }

    @Test
    @DisplayName("WBT-valid3")
    void TC4_cash_card_valid() {
        //setup
        pizzaService.addPayment(3,PaymentType.Cash,10.0);
        pizzaService.addPayment(2,PaymentType.Card,13.5);
        pizzaService.addPayment(8,PaymentType.Cash,20.0);

        //act
        int size=pizzaService.getPayments().size();

        //assert
        assertTrue(size == 3);
        assertTrue(pizzaService.getPayments().get(1).getType() == PaymentType.Card);

        assertTrue(pizzaService.getTotalAmount(PaymentType.Card) == 13.5);
    }
}