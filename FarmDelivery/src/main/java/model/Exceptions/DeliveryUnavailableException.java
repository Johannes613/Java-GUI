package model.Exceptions;

public class DeliveryUnavailableException extends Exception {

    public DeliveryUnavailableException() {
        super("Delivery is unavailable for the provided address.");
    }
    public DeliveryUnavailableException(String message) {
        super(message);
    }
}

