package pizzashop.repository;

import org.apache.log4j.Logger;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108","java:S1943"})
public class PaymentRepository {
    private static String filename = "data/payments.txt";
    private static  final Logger LOGGER=Logger.getLogger(PaymentRepository.class);

    private List<Payment> paymentList;

    public PaymentRepository(){
        this.paymentList = new ArrayList<>();
        readPayments();
    }

    private void readPayments(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());
        try(BufferedReader br=new BufferedReader(new FileReader(file))) {
            String line = null;
            while((line=br.readLine())!=null){
                Payment payment=getPayment(line);
                paymentList.add(payment);
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private static Payment getPayment(String line){
        if (line==null|| "".equals(line)){
            return null;
        }
        StringTokenizer st=new StringTokenizer(line, ",");
        int tableNumber= Integer.parseInt(st.nextToken());
        String type= st.nextToken();
        double amount = Double.parseDouble(st.nextToken());
        Payment item=null;
        item = new Payment(tableNumber, PaymentType.valueOf(type), amount);
        return item;
    }

    public void add(Payment payment){
        paymentList.add(payment);
        writeAll();
    }

    public List<Payment> getAll(){
        return new ArrayList<>(paymentList);
    }

    public void writeAll(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        try(BufferedWriter bw= new BufferedWriter(new FileWriter(file))) {
            for (Payment p:paymentList) {
                LOGGER.info(p.toString());
                bw.write(p.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

}
