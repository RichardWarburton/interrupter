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

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodAdapter;
import org.objectweb.asm.MethodVisitor;

public class MethodInterrupter extends MethodAdapter {

	@Override
	public void visitLabel(final Label lb) {
		super.visitLabel(lb);
		generate();
	}

	private int stackCounter;
	private final int id;

	public MethodInterrupter(final MethodVisitor mv, final int id) {
		super(mv);
		stackCounter = 0;
		this.id = id;
	}

	private void generate() {
		super.visitLdcInsn(id);
		super.visitMethodInsn(INVOKESTATIC, "interrupter/Interrupter", "interrupt", "(I)V");
		stackCounter++;
	}

	@Override
	public void visitIntInsn(final int opcode, final int operand) {
		super.visitIntInsn(opcode, operand);
		generate();
	}

	@Override
	public void visitVarInsn(final int opcode, final int var) {
		super.visitVarInsn(opcode, var);
		generate();
	}

	@Override
	public void visitTypeInsn(final int opcode, final String desc) {
		super.visitTypeInsn(opcode, desc);
		generate();
	}

	@Override
	public void visitFieldInsn(final int opc, final String owner, final String name, final String desc) {
		super.visitFieldInsn(opc, owner, name, desc);
		generate();
	}

	@Override
	public void visitInsn(final int opcode) {
		super.visitInsn(opcode);
		if (opcode != RETURN) {
			generate();
		}
	}

	@Override
	public void visitMethodInsn(final int opc, final String owner, final String name, final String desc) {
		if (owner.equals("interrupter/Interrupter") && name.equals("interrupt")) {
			throw new InvalidJavaException();
		}
		super.visitMethodInsn(opc, owner, name, desc);
		generate();
	}

	@Override
	public void visitJumpInsn(final int opcode, final Label label) {
		super.visitJumpInsn(opcode, label);
		generate();
	}

	@Override
	public void visitLdcInsn(final Object cst) {
		super.visitLdcInsn(cst);
		generate();
	}

	@Override
	public void visitIincInsn(final int var, final int increment) {
		super.visitIincInsn(var, increment);
		generate();
	}

	@Override
	public void visitLookupSwitchInsn(final Label dflt, final int keys[], final Label labels[]) {
		super.visitLookupSwitchInsn(dflt, keys, labels);
		generate();
	}

	@Override
	public void visitMultiANewArrayInsn(final String desc, final int dims) {
		super.visitMultiANewArrayInsn(desc, dims);
	}

	@Override
	public void visitTryCatchBlock(final Label start, final Label end, final Label handler, final String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		generate();
	}

	@Override
	public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
		generate();
	}

	@Override
	public void visitMaxs(final int maxStack, final int maxLocals) {
		super.visitMaxs(maxStack + stackCounter, maxLocals);
	}

	@Override
	public void visitEnd() {
	}
}