package task2.exceptions;

public class NoEmptyConstructorException extends RuntimeException{
    public NoEmptyConstructorException(String message) {
        super(message);
    }
}
