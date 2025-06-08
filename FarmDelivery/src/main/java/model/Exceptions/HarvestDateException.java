package model.Exceptions;

public class HarvestDateException extends Exception {
    public HarvestDateException(){
        super("Invalid harvest date entered");
    }
    public HarvestDateException(String message) {
        super(message);
    }

}
