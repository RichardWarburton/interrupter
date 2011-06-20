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
	public void testSimple() throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException {
		final InterruptingClassLoader cl = new InterruptingClassLoader(1);
		Interrupter.setLimit(1, 20);

		final Class<?> sampleClass = cl.loadClass("Sample");
		final Method method = sampleClass.getMethod("main", String[].class);
		final String[] invokeArgs = {};
		try {
			method.invoke(null, new Object[] { invokeArgs });
			fail("Should've exploded");
		} catch (final InvocationTargetException e) {
			assertEquals(InterruptingError.class, e.getCause().getClass());
		}
	}

}
