package model.Exceptions;

public class OutOfSeasonException extends Exception{
    public OutOfSeasonException(){
        super("Invalid season entered it is out of season");
    }
    public OutOfSeasonException(String message){
        super(message);
    }
}
