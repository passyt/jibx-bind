package org.jibx.binding.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author Passyt
 *
 */
public class ClassUtils {

	public static String trace(Class<?> clazz) {
		StringBuilder builder = new StringBuilder();
		builder.append(clazz);
		if (clazz.getSuperclass() != null) {
			builder.append(" extends ").append(clazz.getSuperclass());
		}
		if (clazz.getInterfaces() != null && clazz.getInterfaces().length > 0) {
			builder.append(" implements ").append(Joiner.on(",").join(clazz.getInterfaces()));
		}
		builder.append("\n  Fields:\n");
		for (Field f : clazz.getDeclaredFields()) {
			builder.append("\t" + f).append("\n");
		}
		builder.append("  Constructors:\n");
		for(Constructor<?> each : clazz.getDeclaredConstructors()){
			builder.append("\t" + each).append("\n");
		}
		builder.append("  Methods:\n");
		for (Method m : clazz.getDeclaredMethods()) {
			builder.append("\t" + m).append("\n");
		}
		return builder.toString();
	}
}
