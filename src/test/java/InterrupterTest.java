import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import interrupter.Interrupter;
import interrupter.InterruptingClassLoader;
import interrupter.InterruptingError;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

public class InterrupterTest {

	@Test
	public void testSimple() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		InterruptingClassLoader cl = new InterruptingClassLoader(1);
		Interrupter.setLimit(1, 20);

		Class<?> test = cl.loadClass("Sample");
		final Method[] methods = test.getMethods();
		for (Method method : methods) {
			if(method.getName().equals("main")) {
				final String[] invokeArgs = {};
				try {
					method.invoke(null,new Object[]{invokeArgs});
					fail("Should've exploded");
				} catch (InvocationTargetException e) {
					assertEquals(InterruptingError.class, e.getCause().getClass());
				}
			}
		}
	}

}
