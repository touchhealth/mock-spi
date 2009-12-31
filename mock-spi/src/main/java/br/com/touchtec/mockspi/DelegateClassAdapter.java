package br.com.touchtec.mockspi;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKEINTERFACE;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.SIPUSH;
import static org.objectweb.asm.Opcodes.V1_5;

import javax.annotation.Nullable;

import org.objectweb.asm.ClassAdapter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import com.google.inject.internal.asm.ClassWriter;

/**
 * This adapter does not compute any maximums. Use with
 * {@link ClassWriter#COMPUTE_MAXS}.
 */
public class DelegateClassAdapter extends ClassAdapter {

	private final int index;
	private final String ifaceInternalName;
	private final String ifaceDescriptor;
	private final String implInternalName;
	private final String implDescriptor;

	public DelegateClassAdapter(int index, String implName,
			ClassVisitor visitor, Class<?> iface) {
		super(checkNotNull(visitor));
		checkArgument(index >= 0);
		checkNotNull(iface);
		this.index = index;
		ifaceInternalName = Type.getInternalName(iface);
		ifaceDescriptor = Type.getDescriptor(iface);
		implInternalName = checkNotNull(implName).replace('.', '/');
		implDescriptor = "L" + implInternalName + ";";
	}

	@Override
	public void visit(int version, int access, @Nullable String name,
			@Nullable String signature, @Nullable String superName,
			@Nullable String[] interfaces) {
		cv.visit(V1_5, ACC_PUBLIC + ACC_SUPER, implInternalName, null,
				"java/lang/Object", new String[] { ifaceInternalName });
	}

	private int generateReturnOpcode(String desc) {
		assert (desc != null);
		Type returnType = Type.getReturnType(desc);
		return returnType.getOpcode(IRETURN);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			@Nullable String signature, @Nullable String[] exceptions) {
		checkNotNull(name);
		checkNotNull(desc);

		Type[] argTypes = Type.getArgumentTypes(desc);

		// JLS 9.4: Every method declaration in the body of an interface is
		// implicitly public.
		MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, name, desc, signature,
				exceptions);
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);

		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETFIELD, implInternalName, "delegate",
				ifaceDescriptor);
		for (int i = 0; i < argTypes.length; i++) {
			Type type = argTypes[i];
			mv.visitVarInsn(type.getOpcode(ILOAD), i + 1);
		}

		mv.visitMethodInsn(INVOKEINTERFACE, ifaceInternalName, name, desc);
		mv.visitInsn(generateReturnOpcode(desc));

		Label l1 = new Label();
		mv.visitLabel(l1);

		mv.visitLocalVariable("this", implDescriptor, null, l0, l1, 0);
		for (int i = 0; i < argTypes.length; i++) {
			Type type = argTypes[i];
			mv.visitLocalVariable("arg" + i, type.getDescriptor(), null, l0,
					l1, i + 1);
		}

		mv.visitMaxs(1, 1);
		return mv;
	}

	@Override
	public void visitEnd() {
		MethodVisitor mv;
		{
			FieldVisitor fv = cv.visitField(ACC_PRIVATE + ACC_FINAL,
					"delegate", ifaceDescriptor, null, null);
			fv.visitEnd();
		}

		{
			mv = cv.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			// ------------------------------------------------------------------
			mv.visitLdcInsn(Type.getType(ifaceDescriptor));
			mv.visitIntInsn(SIPUSH, index);
			mv.visitMethodInsn(INVOKESTATIC,
					"br/com/touchtec/mockspi/MockSPIClassLoader",
					"getInstance", "(Ljava/lang/Class;I)Ljava/lang/Object;");
			mv.visitTypeInsn(CHECKCAST, ifaceInternalName);
			// ------------------------------------------------------------------
			mv.visitMethodInsn(INVOKESPECIAL, implInternalName, "<init>", "("
					+ ifaceDescriptor + ")V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this", implDescriptor, null, l0, l2, 0);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		{
			mv = cv.visitMethod(ACC_PUBLIC, "<init>", "(" + ifaceDescriptor
					+ ")V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, implInternalName, "delegate",
					ifaceDescriptor);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this", implDescriptor, null, l0, l3, 0);
			mv.visitLocalVariable("delegate", ifaceDescriptor, null, l0, l3, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		super.visitEnd();
	}

}
