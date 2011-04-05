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
	public void visitLabel(Label lb) {
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
	public void visitIntInsn(int opcode, int operand) {
		super.visitIntInsn(opcode, operand);
		generate();
	}
	public void visitVarInsn(int opcode, int var) {
		super.visitVarInsn(opcode, var);
		generate();
	}
	public void visitTypeInsn(int opcode, String desc) {
		super.visitTypeInsn(opcode, desc);
		generate();
	}
	public void visitFieldInsn(int opc, String owner, String name, String desc) {
		super.visitFieldInsn(opc, owner, name, desc);
		generate();
	}

	@Override
	public void visitInsn(int opcode) {
		super.visitInsn(opcode);
		if(opcode != RETURN) {
			generate();
		}		
	}

	@Override
	public void visitMethodInsn(int opc, String owner, String name, String desc) {
		if(owner.equals("interrupter/Interrupter") && name.equals("interrupt")) {
			throw new InvalidJavaException();
		}
		super.visitMethodInsn(opc, owner, name, desc);
		generate();
	}

	public void visitJumpInsn(int opcode, Label label) {
		super.visitJumpInsn(opcode, label);
		generate();
	}
	public void visitLdcInsn(Object cst) {
		super.visitLdcInsn(cst);
		generate();
	}
	public void visitIincInsn(int var, int increment) {
		super.visitIincInsn(var, increment);
		generate();
	}
	public void visitLookupSwitchInsn(Label dflt, int keys[], Label labels[]) {
		super.visitLookupSwitchInsn(dflt, keys, labels);
		generate();
	}
	public void visitMultiANewArrayInsn(String desc, int dims) {
		super.visitMultiANewArrayInsn(desc, dims);
	}
	public void visitTryCatchBlock(Label start, Label end, Label handler,
	    String type) {
		super.visitTryCatchBlock(start, end, handler, type);
		generate();
	}
	public void visitLocalVariable(String name, String desc, String signature,
	    Label start, Label end, int index) {
		super.visitLocalVariable(name, desc, signature, start, end, index);
		generate();
	}
	public void visitMaxs(int maxStack, int maxLocals) {
		super.visitMaxs(maxStack+stackCounter,maxLocals);
	}
	public void visitEnd() {
	}
}