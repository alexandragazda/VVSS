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
    private static PaymentRepository paymentRepositoryEmptyFile;
    private static PizzaService pizzaServiceEmptyFile;
    private static PizzaService pizzaService;
    private static final String SERVICEEXEPTIONMSG="Type has to be specified!";
    private static final Double totalAmountCard = 170.0;
    private static final Double totalAmountCash = 221.0;

    @BeforeAll
    static void init(){
        BasicConfigurator.configure();
        menuRepository= new MenuRepository();

        paymentRepositoryEmptyFile= new PaymentRepository("data/paymentsTestEmpty-WBT");
        pizzaServiceEmptyFile= new PizzaService(menuRepository,paymentRepositoryEmptyFile);

        paymentRepository= new PaymentRepository("data/paymentsTest-WBT");
        pizzaService= new PizzaService(menuRepository,paymentRepository);
    }

    @Test
    @DisplayName("WBT-non-valid")
    void test1() {
        try {
           pizzaService.getTotalAmount(null);
        } catch (ServiceException ex) {
            assertTrue(ex.getMessage().equals(SERVICEEXEPTIONMSG));
        }
    }

    @Test
    @DisplayName("WBT-valid1")
    void test2() {
        assertTrue(pizzaServiceEmptyFile.getPayments().isEmpty());
        assertTrue(pizzaServiceEmptyFile.getTotalAmount(PaymentType.Card) == 0.0F);
    }

    @Test
    @DisplayName("WBT-valid2")
    void test3() {
        assertTrue(pizzaService.getTotalAmount(PaymentType.Card) == totalAmountCard);
        assertTrue(pizzaService.getTotalAmount(PaymentType.Cash) == totalAmountCash);
    }
}