package pizzashop.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.log4j.Logger;
import pizzashop.model.PaymentType;

import java.util.Optional;

@SuppressWarnings({"java:S1106","java:S1120","java:S1108"})
public class PaymentAlert implements PaymentOperation {
    private PizzaService service;
    private static final Logger LOGGER=Logger.getLogger(PaymentAlert.class);

    private static final String DELIMITER="--------------------------";

    public PaymentAlert(PizzaService service){
        this.service=service;
    }

    @Override
    public void cardPayment() {
        LOGGER.info(DELIMITER);
        LOGGER.info("Paying by card...");
        LOGGER.info("Please insert your card!");
        LOGGER.info(DELIMITER);
    }
    @Override
    public void cashPayment() {
        LOGGER.info(DELIMITER);
        LOGGER.info("Paying cash...");
        LOGGER.info("Please show the cash...!");
        LOGGER.info(DELIMITER);
    }
    @Override
    public void cancelPayment() {
        LOGGER.info(DELIMITER);
        LOGGER.info("Payment choice needed...");
        LOGGER.info(DELIMITER);
    }
      public void showPaymentAlert(int tableNumber, double totalAmount ) {
          Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
          paymentAlert.setTitle("Payment for Table " + tableNumber);
          paymentAlert.setHeaderText("Total amount: " + totalAmount);
          paymentAlert.setContentText("Please choose payment option");
          ButtonType cardPayment = new ButtonType("Pay by Card");
          ButtonType cashPayment = new ButtonType("Pay Cash");
          ButtonType cancel = new ButtonType("Cancel");
          paymentAlert.getButtonTypes().setAll(cardPayment, cashPayment, cancel);
          Optional<ButtonType> result = paymentAlert.showAndWait();
          if (result.isPresent()) {
              if (result.get() == cardPayment) {
                  cardPayment();
                  service.addPayment(tableNumber, PaymentType.Cash, totalAmount);
              } else if (result.get() == cashPayment) {
                  cashPayment();
                  service.addPayment(tableNumber, PaymentType.Cash, totalAmount);
              } else if (result.get() == cancel) {
                  cancelPayment();
              } else {
                  cancelPayment();
              }
          }
      }
}
