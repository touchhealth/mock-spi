package br.com.touchtec.mockspi;

import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.ASMifierClassVisitor;

public class ASMTestDebuger implements Opcodes {

	public static void main(String[] args) throws Exception {

		ASMifierClassVisitor visitor = new ASMifierClassVisitor(
				new PrintWriter(System.out));
		ClassReader reader = new ClassReader(Thread.currentThread()
				.getContextClassLoader().getResourceAsStream(
						"org/simplethought/mockspi/ASMTest.class"));
		reader.accept(visitor, 0);
	}

	public static <T> T getInstance(Class<T> clazz, int index) {
		return null;
	}

	public byte[] dump(Class<?> ifaceClass, int idx) {
		ClassWriter cw = new ClassWriter(0);
		FieldVisitor fv;
		MethodVisitor mv;

		cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER,
				"org/simplethought/mockspi/ASMTest", null, "java/lang/Object",
				new String[] { "org/simplethought/mockspi/TestIface" });

		cw.visitSource("ASMTest.java", null);

		{
			fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "delegate",
					"Lorg/simplethought/mockspi/TestIface;", null, null);
			fv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(8, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitLdcInsn(Type
					.getType("Lorg/simplethought/mockspi/TestIface;"));
			mv.visitIntInsn(SIPUSH, 321);
			mv.visitMethodInsn(INVOKESTATIC, "org/simplethought/mockspi/Bla",
					"getInstance", "(Ljava/lang/Class;I)Ljava/lang/Object;");
			mv.visitTypeInsn(CHECKCAST, "org/simplethought/mockspi/TestIface");
			mv.visitMethodInsn(INVOKESPECIAL,
					"org/simplethought/mockspi/ASMTest", "<init>",
					"(Lorg/simplethought/mockspi/TestIface;)V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(9, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l2, 0);
			mv.visitMaxs(3, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>",
					"(Lorg/simplethought/mockspi/TestIface;)V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(12, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>",
					"()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(13, l1);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitFieldInsn(PUTFIELD, "org/simplethought/mockspi/ASMTest",
					"delegate", "Lorg/simplethought/mockspi/TestIface;");
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(14, l2);
			mv.visitInsn(RETURN);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l3, 0);
			mv.visitLocalVariable("delegate",
					"Lorg/simplethought/mockspi/TestIface;", null, l0, l3, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "voidMethod", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(18, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/simplethought/mockspi/ASMTest",
					"delegate", "Lorg/simplethought/mockspi/TestIface;");
			mv.visitMethodInsn(INVOKEINTERFACE,
					"org/simplethought/mockspi/TestIface", "voidMethod", "()V");
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLineNumber(19, l1);
			mv.visitInsn(RETURN);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l2, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "intMethod", "()I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(23, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/simplethought/mockspi/ASMTest",
					"delegate", "Lorg/simplethought/mockspi/TestIface;");
			mv.visitMethodInsn(INVOKEINTERFACE,
					"org/simplethought/mockspi/TestIface", "intMethod", "()I");
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l1, 0);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "genericMethod",
					"(Ljava/lang/Class;)Ljava/lang/Exception;",
					"<T:Ljava/lang/Exception;>(Ljava/lang/Class<TT;>;)TT;^TT;",
					new String[] { "java/lang/Exception" });
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(28, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/simplethought/mockspi/ASMTest",
					"delegate", "Lorg/simplethought/mockspi/TestIface;");
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEINTERFACE,
					"org/simplethought/mockspi/TestIface", "genericMethod",
					"(Ljava/lang/Class;)Ljava/lang/Exception;");
			mv.visitInsn(ATHROW);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l1, 0);
			mv.visitLocalVariable("c", "Ljava/lang/Class;",
					"Ljava/lang/Class<TT;>;", l0, l1, 1);
			mv.visitMaxs(2, 2);
			mv.visitEnd();
		}
		{
			mv = cw.visitMethod(ACC_PUBLIC, "multiArgs",
					"(ILjava/lang/String;D)I", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(33, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "org/simplethought/mockspi/ASMTest",
					"delegate", "Lorg/simplethought/mockspi/TestIface;");
			mv.visitVarInsn(ILOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(DLOAD, 3);
			mv.visitMethodInsn(INVOKEINTERFACE,
					"org/simplethought/mockspi/TestIface", "multiArgs",
					"(ILjava/lang/String;D)I");
			mv.visitInsn(IRETURN);
			Label l1 = new Label();
			mv.visitLabel(l1);
			mv.visitLocalVariable("this",
					"Lorg/simplethought/mockspi/ASMTest;", null, l0, l1, 0);
			mv.visitLocalVariable("oi", "I", null, l0, l1, 1);
			mv
					.visitLocalVariable("lala", "Ljava/lang/String;", null, l0,
							l1, 2);
			mv.visitLocalVariable("lele", "D", null, l0, l1, 3);
			mv.visitMaxs(5, 5);
			mv.visitEnd();
		}
		cw.visitEnd();

		return cw.toByteArray();
	}
}
