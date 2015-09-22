package org.jibx.binding.classes.javassist;

import javassist.CtMethod;
import javassist.CtNewMethod;

import org.jibx.binding.IMethod;

/**
 * 
 * @author Passyt
 *
 */
public class JavassistMethod implements IMethod {

	private final CtMethod method;

	public JavassistMethod(String content, JavassistClassFile classFile) throws Exception {
		try {
			method = CtNewMethod.make(content, classFile.getClazz());
		} catch (Exception e) {
			if (classFile.isDebug()) {
				throw new DebugException(content, e);
			} else {
				throw e;
			}
		}
	}

	public JavassistMethod(CtMethod method) throws Exception {
		this.method = method;
	}

	public CtMethod getMethod() {
		return method;
	}

}
