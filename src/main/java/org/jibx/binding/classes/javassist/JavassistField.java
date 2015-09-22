package org.jibx.binding.classes.javassist;

import javassist.CtField;

import org.jibx.binding.IField;

/**
 * 
 * @author Passyt
 *
 */
public class JavassistField implements IField {

	private final CtField field;

	public JavassistField(String content, JavassistClassFile classFile) throws Exception {
		try {
			field = CtField.make(content, classFile.getClazz());
		} catch (Exception e) {
			if (classFile.isDebug()) {
				throw new DebugException(content, e);
			} else {
				throw e;
			}
		}
	}

	public CtField getField() {
		return field;
	}

}
