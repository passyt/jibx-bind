package org.jibx.binding.classes.javassist.builder;

import java.lang.reflect.Constructor;

/**
 * 
 * @author Passyt
 *
 */
public class MugeAdapterClassExample {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object defaultDeserialize(String object, String type) {
		try {
			Class clazz = Class.forName(type);
			if (clazz.isEnum()) {
				return Enum.valueOf(clazz, object);
			}

			Constructor<?> c = clazz.getConstructor(Class.forName("java.lang.String"));
			return c.newInstance(object);
		} catch (Exception e) {
			throw new java.lang.IllegalStateException("Unknown type " + type + " by value " + object);
		}
	}

}
