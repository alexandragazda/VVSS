package pizzashop.service;


@SuppressWarnings({"java:S1106","java:S1120"})
public interface PaymentOperation {
     void cardPayment();
     void cashPayment();
     void cancelPayment();
}
