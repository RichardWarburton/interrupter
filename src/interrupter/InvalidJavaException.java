package interrupter;

public class InvalidJavaException extends RuntimeException {

	public InvalidJavaException() {
		super("You cannot call interrupter.Interrupter.interupt() within interruptable code.");
	}
	
}
