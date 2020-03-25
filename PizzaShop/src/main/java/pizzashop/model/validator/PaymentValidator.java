package pizzashop.model.validator;

import pizzashop.model.Payment;

public class PaymentValidator implements Validator<Payment> {
    @Override
    public void validate(Payment entity) {
        String msg="";
        if(entity.getTableNumber()<1|| entity.getTableNumber()>8){
            msg+="Table number must be in [1,8]!";
        }
        if(entity.getType()==null){
            msg+="Payment type must be cash or card!";
        }
        if(entity.getAmount()<=0){
            msg+="Amount must be grater than 0!";
        }
        if(msg!=""){
            throw new ValidationException(msg);
        }
    }

}
