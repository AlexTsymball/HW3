package task2.exceptions;

public class WrongTypeException extends RuntimeException{
    public WrongTypeException(String message) {
        super(message);
    }
}
