package pizzashop.repository;

import org.apache.log4j.BasicConfigurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PaymentRepositoryTestWithMock {
    private PaymentRepository paymentRepository;
    private static final String FILENAME="data/paymentsTest-Mockito";
    private Payment payment;

    @BeforeEach
    void setUp() {
        BasicConfigurator.configure();

        paymentRepository=new PaymentRepository(FILENAME);
        payment=mock(Payment.class);
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
    public void test1_add(){
        Mockito.when(payment.getTableNumber()).thenReturn(4);
        Mockito.when(payment.getType()).thenReturn(PaymentType.Card);
        Mockito.when(payment.getAmount()).thenReturn(20.5);

        paymentRepository.add(new Payment(payment.getTableNumber(),payment.getType(),payment.getAmount()));

        PaymentType type=paymentRepository.getAll().get(0).getType();
        assertEquals(PaymentType.Card, type);
    }

    @Test
    public void test2_getAll(){
        Mockito.when(payment.getTableNumber()).thenReturn(1);
        Mockito.when(payment.getType()).thenReturn(PaymentType.Cash);
        Mockito.when(payment.getAmount()).thenReturn(25.0);

        paymentRepository.add(new Payment(payment.getTableNumber(),payment.getType(),payment.getAmount()));

        int size=paymentRepository.getAll().size();
        assertEquals(1, size);
    }
}