package org.jibx.binding.classes.javassist;

import javassist.CtConstructor;
import javassist.CtNewConstructor;

import org.jibx.binding.IConstructor;

/**
 * 
 * @author Passyt
 *
 */
public class JavassistConstructor implements IConstructor {

	private final CtConstructor constructor;

	protected JavassistConstructor(CtConstructor constructor) {
		super();
		this.constructor = constructor;
	}

	public JavassistConstructor(String content, JavassistClassFile classFile) throws Exception {
		try {
			constructor = CtNewConstructor.make(content, classFile.getClazz());
		} catch (Exception e) {
			if (classFile.isDebug()) {
				throw new DebugException(content, e);
			} else {
				throw e;
			}
		}
	}

	public CtConstructor getConstructor() {
		return constructor;
	}

	public static JavassistConstructor defaultConstructor(JavassistClassFile classFile) throws Exception {
		return new JavassistConstructor(CtNewConstructor.defaultConstructor(classFile.getClazz()));
	}
}
