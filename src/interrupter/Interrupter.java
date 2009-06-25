package interrupter;
import java.util.HashMap;
import java.util.Map;


public class Interrupter {

	private static Map<Integer, Integer> lookup = new HashMap<Integer, Integer>();
	
	public static synchronized void setLimit(int id, int value) {
		lookup.put(id, value);
	}
	
	public static void interrupt(int id) {
		try {
			final int value = lookup.get(id) - 1;
			if(value <= 0)
				throw new InterruptingError();
			else
				lookup.put(id, value);
		} catch (NullPointerException e) {
			throw new UnknownIdException(id);
		}
	}
	
}
