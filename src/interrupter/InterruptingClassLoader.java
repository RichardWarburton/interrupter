/*
    Copyright 2009 Richard Warburton (richard.warburton@gmail.com)
    This file is part of Interrupter.

    Interrupter is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Interrupter is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with Interrupter.  If not, see <http://www.gnu.org/licenses/>.
 */
package interrupter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class InterruptingClassLoader extends URLClassLoader {

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
			// TODO: debug name
			return super.loadClass(name);
		}
	}
	
	public Class<?> defineClass(String name, byte[] b) {
		return super.defineClass(name, b, 0, b.length);
	}

	public InterruptingClassLoader(final int id, URL[] urls) {
		super(urls);
		this.id = id;
	}
	
	public InterruptingClassLoader(final int id) {
		this(id,new URL[]{});
	}

	public Class<?> loadInterruptedClassByName(String name) throws IOException {
		// write to a bytearray wrapped inside this
		ClassWriter cw = new ClassWriter(0);
		
		// This modifies the test class adding interrupts
		// TODO: work out where the hell MultiMethodAdapter is
		// TraceClassVisitor tcv = new TraceClassVisitor(new PrintWriter(System.out));
		
		ClassInterrupter ci = new ClassInterrupter(cw,id);
		// this reads the original test code
		ClassReader cr = new ClassReader(name);
		cr.accept(ci, 0);

		return defineClass(name, cw.toByteArray());
	}
	
}