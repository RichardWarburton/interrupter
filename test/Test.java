import interrupter.Interrupter;
import interrupter.InterruptingError;



public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Interrupter.interrupt(0);
		int x = 0;
		try {
			x = 5;
			if(x == 7) {
				System.out.println("a");
			} else {
				System.out.println("b");
			}
			System.out.println("hello world");
		} catch (InterruptingError e) {}
	}

}
