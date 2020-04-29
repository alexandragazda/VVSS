package pizzashop.service;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PizzaServiceTestWithMock {

    private MenuRepository menuRepository;
    private PaymentRepository paymentRepository;
    private PizzaService pizzaService;

    @BeforeEach
    void setUp() {
        BasicConfigurator.configure();

        menuRepository=new MenuRepository();
        paymentRepository=mock(PaymentRepository.class);
        pizzaService=new PizzaService(menuRepository,paymentRepository);
    }

    @Test
    public void test1_add_payment(){
        Payment payment=new Payment(2, PaymentType.Card,20.0);

        doAnswer((Answer<Void>) invocation->{
            Object[] args=invocation.getArguments();
            if(args != null && args.length == 1 && args[0] != null){
                Payment p=(Payment) args[0];
                System.out.println("Test1 payment: " + p);
            }
            return null;
        }).when(paymentRepository).add(payment);

        Mockito.verify(paymentRepository,never()).add(payment);

        pizzaService.addPayment(payment);

        Mockito.verify(paymentRepository,times(1)).add(payment);
    }

    @Test
    public void test2_get_total_amount(){
        Payment payment1=new Payment(2, PaymentType.Card,20.0);
        Payment payment2=new Payment(4,PaymentType.Cash,35.5);
        Payment payment3=new Payment(3,PaymentType.Card,15.0);

        Mockito.when(paymentRepository.getAll()).thenReturn(Arrays.asList(payment1, payment2, payment3));

        Mockito.verify(paymentRepository,never()).getAll();

        double totalAmountCash=pizzaService.getTotalAmount(PaymentType.Cash);
        double totalAmountCard=pizzaService.getTotalAmount(PaymentType.Card);
        assertEquals(35.5,totalAmountCash);
        assertEquals(35.0,totalAmountCard);

        Mockito.verify(paymentRepository,times(2)).getAll();
    }
}