package pizzashop.service;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pizzashop.model.PaymentType;
import pizzashop.model.validator.ValidationException;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PizzaServiceTest {

    static private MenuRepository menuRepository;
    static private PaymentRepository paymentRepository;
    static private PizzaService pizzaService;
    static  private  Integer initSize;

    @BeforeAll
    static void init(){
        BasicConfigurator.configure();
        menuRepository= new MenuRepository();
        paymentRepository= new PaymentRepository("data/paymentsTest");
        pizzaService= new PizzaService(menuRepository,paymentRepository);
    }

    @AfterAll
    static void end(){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter("src/test/resources/data/paymentsTest"))){
            bw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setUp(){
        initSize=pizzaService.getPayments().size();
    }

    @Test
    @DisplayName("ECP-valid1")
    @Order(1)
    @Tag("ECP")
    void test1_ECP() {
        try {
            pizzaService.addPayment(4, PaymentType.Card, 15);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        } catch (ValidationException ex) { }
    }

    @Test
    @DisplayName("ECP-non-valid1")
    @Order(3)
    @Tag("ECP")
    void test2_ECP(){
        try {
            pizzaService.addPayment(0, PaymentType.Card, -50);
        }catch (ValidationException ex){
            assertTrue(ex.getMessage().equals("Table number must be in [1,8]!Amount must be grater than 0!"));
            assertTrue(initSize==pizzaService.getPayments().size());
        }
    }

    @Test
    @DisplayName("ECP-non-valid2")
    @Order(4)
    @Tag("ECP")
    void test3_ECP(){
        try {
            pizzaService.addPayment(10, PaymentType.Card, 60.5);
        }catch (ValidationException ex){
            assertTrue(ex.getMessage().equals("Table number must be in [1,8]!"));
            assertTrue(initSize==pizzaService.getPayments().size());
        }
    }

    @Test
    @DisplayName("ECP-valid2")
    @Order(2)
    @Tag("ECP")
    void test4_ECP() {
        try {
            pizzaService.addPayment(1, PaymentType.Cash, 40);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        }catch (ValidationException ex) { }
    }


    @DisplayName("BVA-valid(table number)")
    @ParameterizedTest
    @ValueSource(ints = {1,2,7,8})
    @Order(5)
    @Tag("BVA")
    void tableNr_test1_BVA(Integer myInt) {
        try {
            pizzaService.addPayment(myInt, PaymentType.Card, 15);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        } catch (ValidationException ex) { }
    }

    @DisplayName("BVA-non-valid(table number)")
    @ParameterizedTest
    @ValueSource(ints = {0,9})
    @Order(7)
    @Tag("BVA")
    void tableNr_test2_BVA(Integer myInt) {
        try {
            pizzaService.addPayment(myInt, PaymentType.Card, 15);
        } catch (ValidationException ex) {
            assertTrue(ex.getMessage().equals("Table number must be in [1,8]!"));
            assertTrue(initSize == pizzaService.getPayments().size());
        }
    }

    @DisplayName("BVA-valid(amount)")
    @ParameterizedTest
    @ValueSource(doubles = {0.01,0.02,Double.MAX_VALUE-0.01,Double.MAX_VALUE})
    @Order(6)
    @Tag("BVA")
    void amount_test3_BVA(Double myDouble) {
        try {
            pizzaService.addPayment(1, PaymentType.Card, myDouble);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        } catch (ValidationException ex) { }
    }

    @DisplayName("BVA-non-valid1(amount)")
    @Order(8)
    @Tag("BVA")
    @Test
    void amount_test4_BVA() {
        try {
            pizzaService.addPayment(1, PaymentType.Card, 0);
        } catch (ValidationException ex) {
            assertTrue(initSize == pizzaService.getPayments().size());
            assertTrue(ex.getMessage().equals("Amount must be grater than 0!"));
        }
    }

    @DisplayName("BVA-non-valid2(amount)")
    @Disabled
    @Tag("BVA")
    @Test
    //disabled until we find out what is the next value for Double.MAX_VALUE
    void amount_test7_BVA() {
        try {
            pizzaService.addPayment(1, PaymentType.Card, Double.MAX_VALUE+1);
        } catch (ValidationException ex) {
            assertTrue(initSize == pizzaService.getPayments().size());
            assertTrue(ex.getMessage().equals("Amount must be grater than 0!"));
        }
    }

}