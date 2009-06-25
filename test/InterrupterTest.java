import interrupter.Interrupter;
import interrupter.InterruptingClassLoader;

import java.lang.reflect.Method;


public class InterrupterTest {

	public static void main(String[] args) throws Throwable {
		InterruptingClassLoader cl = new InterruptingClassLoader(1);
		Interrupter.setLimit(1, 20);
		
		Class<?> test = cl.loadClass("Test");
		final Method[] methods = test.getMethods();
		for (Method method : methods) {
			if(method.getName().equals("main")) {
				final String[] invokeArgs = {};
				method.invoke(null,new Object[]{invokeArgs});
			}
		}
	}

}
