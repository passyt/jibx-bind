package org.jibx.binding.classes.javassist;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.LinkedHashSet;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.jibx.binding.IClassFile;

/**
 * 
 * @author Passyt
 *
 */
public class JavassistClassFile implements IClassFile<JavassistConstructor, JavassistMethod, JavassistField> {

	private final ClassPool classPool;
	private final CtClass clazz;
	private final boolean debug;

	public JavassistClassFile(ClassPool classPool, CtClass clazz, boolean debug) {
		super();
		this.classPool = classPool;
		this.clazz = clazz;
		this.debug = debug;
	}

	public ClassPool getClassPool() {
		return classPool;
	}

	public CtClass getClazz() {
		return clazz;
	}

	public boolean isDebug() {
		return debug;
	}

	@Override
	public String getClassName() {
		return this.clazz.getName();
	}

	public String getPackageName() {
		return this.clazz.getPackageName();
	}

	@Override
	public void setSuperclass(String className) throws Exception {
		clazz.setSuperclass(classPool.get("org.jibx.runtime.impl.BindingFactoryBase"));
	}

	@Override
	public void addInterfaces(String... interfaceClassNames) throws Exception {
		CtClass[] interfaces = clazz.getInterfaces();
		if (interfaces == null) {
			interfaces = new CtClass[0];
		}

		Set<String> addinterfaceClassNames = new LinkedHashSet<String>();
		for (String interfaceClassName : interfaceClassNames) {
			boolean existing = false;
			for (CtClass _interface : interfaces) {
				if (_interface.getName().equals(interfaceClassName)) {
					existing = true;
					break;
				}
			}

			if (!existing) {
				addinterfaceClassNames.add(interfaceClassName);
			}
		}

		for (String interfaceClassName : addinterfaceClassNames) {
			clazz.addInterface(classPool.get(interfaceClassName));
		}
	}

	@Override
	public void addConstructor(JavassistConstructor constructor) throws Exception {
		CtConstructor actualConstructor = constructor.getConstructor();
		CtConstructor existingConstructor = null;
		try {
			existingConstructor = clazz.getConstructor(actualConstructor.getSignature());
			if (existingConstructor != null) {
				if (debug) {
					System.out.println("Remove existing constructor " + existingConstructor + " in class " + clazz.getName());
				}
				clazz.removeConstructor(existingConstructor);
			}
		} catch (NotFoundException e) {
		}

		clazz.addConstructor(constructor.getConstructor());
	}

	@Override
	public void addMethod(JavassistMethod method) throws Exception {
		CtMethod actualMethod = method.getMethod();

		CtMethod existingMethod = null;
		try {
			existingMethod = clazz.getMethod(actualMethod.getName(), actualMethod.getSignature());
			if (existingMethod != null) {
				if (debug) {
					System.out.println("Remove existing method " + existingMethod + " in class " + clazz.getName());
				}
				clazz.removeMethod(existingMethod);
			}
		} catch (NotFoundException e) {
		}

		clazz.addMethod(method.getMethod());
	}

	@Override
	public void addField(JavassistField field) throws Exception {
		CtField actualField = field.getField();

		CtField existingField = null;
		try {
			existingField = clazz.getField(actualField.getName(), actualField.getSignature());
			if (existingField != null) {
				if (debug) {
					System.out.println("Remove existing field " + existingField + " in class " + clazz.getName());
				}
				clazz.removeField(existingField);
			}
		} catch (NotFoundException e) {
		}

		clazz.addField(actualField);
	}

	@Override
	public void write(String directory) throws Exception {
		clazz.stopPruning(true);
		clazz.writeFile(directory);
		clazz.detach();
	}

	@Override
	public void write(OutputStream out) throws Exception {
		clazz.toBytecode(new DataOutputStream(out));
	}

	@Override
	public Class<?> toClass() throws Exception {
		return clazz.toClass();
	}

}
