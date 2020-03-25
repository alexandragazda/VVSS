package pizzashop.service;

import pizzashop.model.MenuDataModel;
import pizzashop.model.Payment;
import pizzashop.model.PaymentType;
import pizzashop.model.validator.PaymentValidator;
import pizzashop.model.validator.Validator;
import pizzashop.repository.MenuRepository;
import pizzashop.repository.PaymentRepository;

import java.util.List;

@SuppressWarnings({"java:S1106","java:S1120"})
public class PizzaService {

    private Validator<Payment> validator;
    private MenuRepository menuRepo;
    private PaymentRepository payRepo;

    public PizzaService(MenuRepository menuRepo, PaymentRepository payRepo){
        this.menuRepo=menuRepo;
        this.payRepo=payRepo;
        validator= new PaymentValidator();
    }

    public List<MenuDataModel> getMenuData(){return menuRepo.getMenu();}

    public List<Payment> getPayments(){return payRepo.getAll(); }

    public void addPayment(int table, PaymentType type, double amount){
        Payment payment= new Payment(table, type, amount);
        validator.validate(payment);
        payRepo.add(payment);
    }

    public double getTotalAmount(PaymentType type){
        double total=0.0F;
        List<Payment> paymentList=getPayments();
        if (paymentList.isEmpty()){
            return total;
        }
        for (Payment p:paymentList){
            if (p.getType()==type) {
                total += p.getAmount();
            }
        }
        return total;
    }

}
