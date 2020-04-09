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

    public double getTotalAmount(PaymentType type){                     // entry
        if(type == null){                                               // 1
            throw new ServiceException("Type has to be specified!");    // 2
        }

        double total=0.0F;                                              // 3
        List<Payment> paymentList=getPayments();                        // 3

        if (paymentList.isEmpty()){                                     // 4
            return total;                                               // 8
        }

        for (Payment p:paymentList){                                    // 5
            if (p.getType()==type) {                                    // 6
                total += p.getAmount();                                 // 7
            }
        }

        return total;                                                   // 8
    }                                                                   // exit

}
