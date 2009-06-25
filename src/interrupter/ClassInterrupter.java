package interrupter;
import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Forwards all calls to a visitor
 * @author rlmw
 *
 */
public class ClassInterrupter extends ClassAdapter {
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {

		super.visit(version, access, name, signature, superName, interfaces);
	}

	private final int id;

	public ClassInterrupter(ClassVisitor cv, final int id) {
		super(cv);
		this.id = id;
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		// forwards the methods to the interrupting adapter
		return new MethodInterrupter(super.visitMethod(access, name, desc, signature, exceptions),id);
	}
}