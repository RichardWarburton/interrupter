package interrupter;
import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.util.TraceClassVisitor;

public class InterruptingClassLoader extends ClassLoader {

	private static final String[] protectedPackages = {
		"java.",
		"System",
		"interrupter.Interrupter",
	};
	
	private final int id;
	
	public static boolean checkName(String name) {
		for(String pkg : protectedPackages) {
			if(name.startsWith(pkg)) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public synchronized Class<?> loadClass(String name) throws ClassNotFoundException {
		if(checkName(name)) {
			try {
				return loadInterruptedClassByName(name);
			} catch (IOException e) {
				throw new ClassNotFoundException("",e);
			}
		} else {
			System.out.println("IGNORING:"+name);
			return getSystemClassLoader().loadClass(name);
		}
	}

	public InterruptingClassLoader(int id) {
		this.id = id;
	}
	
	private Class<?> defineClass(String name, byte[] b) {
		return this.defineClass(name, b,0,b.length);
	}
	
	public Class<?> loadInterruptedClassByName(String name) throws IOException {
		// write to a bytearray wrapped inside this
		ClassWriter cw = new ClassWriter(0);
		
		// This modifies the test class adding interrupts
		// TODO: work out where the hell MultiMethodAdapter is
		TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(System.out));
		
		ClassInterrupter ci = new ClassInterrupter(cw,id);
		// this reads the original test code
		ClassReader cr = new ClassReader(name);
		cr.accept(ci, 0);

		return defineClass(name, cw.toByteArray());
	}
	
}