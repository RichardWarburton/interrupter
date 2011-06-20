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

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Forwards all calls to a visitor
 * 
 * @author rlmw
 * 
 */
public class ClassInterrupter extends ClassAdapter {

	@Override
	public void visit(final int version, final int access, final String name, final String signature, final String superName,
			final String[] interfaces) {

		super.visit(version, access, name, signature, superName, interfaces);
	}

	private final int id;

	public ClassInterrupter(final ClassVisitor cv, final int id) {
		super(cv);
		this.id = id;
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		// forwards the methods to the interrupting adapter
		return new MethodInterrupter(super.visitMethod(access, name, desc, signature, exceptions), id);
	}
}