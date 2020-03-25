package pizzashop.model.validator;

public interface Validator<E> {
    void validate(E entity);
}
