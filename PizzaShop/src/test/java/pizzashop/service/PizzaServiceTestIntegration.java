package pizzashop.service;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PizzaServiceTestIntegration {

    private MenuRepository menuRepository;
    private PaymentRepository paymentRepository;
    private PizzaService pizzaService;
    private static final String FILENAME="data/paymentsTest-Mockito";
    private Payment payment;
    private Payment payment1;

    @BeforeEach
    void setUp() {
        BasicConfigurator.configure();
        menuRepository=new MenuRepository();
        paymentRepository= new PaymentRepository(FILENAME);
        pizzaService=new PizzaService(menuRepository,paymentRepository);
        payment=mock(Payment.class);
        payment1=mock(Payment.class);
    }

    @AfterEach
    void tearDown() {
        try(BufferedWriter bw=new BufferedWriter(new FileWriter("target/test-classes/data/paymentsTest-Mockito"))){
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void test1_step2_add_payment(){

        Mockito.when(payment.getTableNumber()).thenReturn(4);
        Mockito.when(payment.getType()).thenReturn(PaymentType.Card);
        Mockito.when(payment.getAmount()).thenReturn(20.5);

        pizzaService.addPayment(payment.getTableNumber(),payment.getType(),payment.getAmount());

        int tableNr = pizzaService.getPayments().get(0).getTableNumber();
        assertEquals(tableNr,payment.getTableNumber());

    }

    @Test
    void test2_step2_get_total_amount(){

        Mockito.when(payment.getTableNumber()).thenReturn(4);
        Mockito.when(payment.getType()).thenReturn(PaymentType.Card);
        Mockito.when(payment.getAmount()).thenReturn(20.5);

        Mockito.when(payment1.getTableNumber()).thenReturn(2);
        Mockito.when(payment1.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(payment1.getAmount()).thenReturn(15.5);

        pizzaService.addPayment(payment.getTableNumber(),payment.getType(),payment.getAmount());
        pizzaService.addPayment(payment1.getTableNumber(),payment1.getType(),payment1.getAmount());


        double totalAmountCash = pizzaService.getTotalAmount(PaymentType.Cash);
        double totalAmountCard =  pizzaService.getTotalAmount(PaymentType.Card);

        assertEquals(totalAmountCard,payment.getAmount());
        assertEquals(totalAmountCash,payment1.getAmount());

    }

    @Test
    void test3_step3_add_payment(){
        Payment payment = new Payment(1,PaymentType.Card,21.5);

        pizzaService.addPayment(payment.getTableNumber(),payment.getType(),payment.getAmount());

        int tableNr = pizzaService.getPayments().get(0).getTableNumber();
        assertEquals(tableNr,payment.getTableNumber());

    }

    @Test
    void test4_step3_get_total_amount(){

        Payment payment=new Payment(1,PaymentType.Card,12.0);
        Payment payment1 = new Payment(2,PaymentType.Cash,20.5);

        pizzaService.addPayment(payment.getTableNumber(),payment.getType(),payment.getAmount());
        pizzaService.addPayment(payment1.getTableNumber(),payment1.getType(),payment1.getAmount());


        double totalAmountCash = pizzaService.getTotalAmount(PaymentType.Cash);
        double totalAmountCard =  pizzaService.getTotalAmount(PaymentType.Card);

        assertEquals(totalAmountCard,payment.getAmount());
        assertEquals(totalAmountCash,payment1.getAmount());

    }
}