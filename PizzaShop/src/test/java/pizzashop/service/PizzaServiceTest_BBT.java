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
class PizzaServiceTest_BBT {

    private static MenuRepository menuRepository;
    private static PaymentRepository paymentRepository;
    private static PizzaService pizzaService;
    private static  Integer initSize;

    @BeforeAll
    static void init(){
        BasicConfigurator.configure();
        menuRepository= new MenuRepository();
        paymentRepository= new PaymentRepository("data/paymentsTest-BBT");
        pizzaService= new PizzaService(menuRepository,paymentRepository);
    }

    @AfterAll
    static void end(){
        try(BufferedWriter bw=new BufferedWriter(new FileWriter("src/test/resources/data/paymentsTest-BBT"))){
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
    @Order(2)
    @Tag("ECP")
    void test2_ECP(){
        try {
            pizzaService.addPayment(0, PaymentType.Card, 50);
        }catch (ValidationException ex){
            assertTrue(ex.getMessage().equals("Table number must be in [1,8]!"));
            assertTrue(initSize==pizzaService.getPayments().size());
        }
    }

    @Test
    @DisplayName("ECP-non-valid2")
    @Order(3)
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
    @DisplayName("ECP-non-valid3")
    @Order(4)
    @Tag("ECP")
    void test4_ECP(){
        try {
            pizzaService.addPayment(4, PaymentType.Card, 0);
        }catch (ValidationException ex){
            assertTrue(ex.getMessage().equals("Amount must be grater than 0!"));
            assertTrue(initSize==pizzaService.getPayments().size());
        }
    }



    @DisplayName("BVA-valid1")
    @ParameterizedTest
    @ValueSource(ints = {1,2,7,8})
    @Order(5)
    @Tag("BVA")
    void test1_BVA(Integer myInt) {
        try {
            pizzaService.addPayment(myInt, PaymentType.Card, 10);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        } catch (ValidationException ex) { }
    }

    @DisplayName("BVA-non-valid1")
    @ParameterizedTest
    @ValueSource(ints = {0,9})
    @Order(7)
    @Tag("BVA")
    void test2_BVA(Integer myInt) {
        try {
            pizzaService.addPayment(myInt, PaymentType.Card, 10);
        } catch (ValidationException ex) {
            assertTrue(ex.getMessage().equals("Table number must be in [1,8]!"));
            assertTrue(initSize == pizzaService.getPayments().size());
        }
    }

    @DisplayName("BVA-valid2")
    @ParameterizedTest
    @ValueSource(doubles = {0.01,0.02,Double.MAX_VALUE-0.01,Double.MAX_VALUE})
    @Order(6)
    @Tag("BVA")
    void test3_BVA(Double myDouble) {
        try {
            pizzaService.addPayment(4, PaymentType.Card, myDouble);
            assertTrue(initSize + 1 == pizzaService.getPayments().size());
        } catch (ValidationException ex) { }
    }

    @DisplayName("BVA-non-valid2")
    @Order(8)
    @Tag("BVA")
    @Test
    void test4_BVA() {
        try {
            pizzaService.addPayment(4, PaymentType.Card, 0);
        } catch (ValidationException ex) {
            assertTrue(initSize == pizzaService.getPayments().size());
            assertTrue(ex.getMessage().equals("Amount must be grater than 0!"));
        }
    }

    @DisplayName("BVA-non-valid3")
    @Disabled
    @Tag("BVA")
    @Test
    //disabled until we find out what is the next value for Double.MAX_VALUE
    void test5_BVA() {
        try {
            pizzaService.addPayment(4, PaymentType.Card, Double.MAX_VALUE+0.01);
        } catch (ValidationException ex) {
            assertTrue(initSize == pizzaService.getPayments().size());
        }
    }

}