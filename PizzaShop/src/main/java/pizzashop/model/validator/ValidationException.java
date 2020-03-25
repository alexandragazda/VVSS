package pizzashop.model.validator;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }
}