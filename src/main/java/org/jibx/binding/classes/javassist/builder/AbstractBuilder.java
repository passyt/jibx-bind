package org.jibx.binding.classes.javassist.builder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Passyt
 *
 */
public abstract class AbstractBuilder {

	protected void putParameter(Map<String, String> parameters, String name, String value) {
		if (value == null) {
			parameters.put(name, "null");
		} else {
			parameters.put(name, "\"" + value + "\"");
		}
	}

	protected void putParameter(Map<String, String> parameters, String name, Collection<String> value) {
		putParameter(parameters, name, value.toArray(new String[0]));
	}

	protected void putParameter(Map<String, String> parameters, String name, String[] value) {
		if (value == null || value.length == 0) {
			parameters.put(name, "new String[0]");
			return;
		}

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < value.length; i++) {
			String each = value[i];
			if (each == null) {
				builder.append(each);
			} else {
				builder.append("\"").append(each).append("\"");
			}

			if (i < value.length - 1) {
				builder.append(", ");
			}
		}
		parameters.put(name, "new String[]{" + builder.toString() + "}");
	}

	protected Map<String, String> newParameters(Map<String, String> parameters, String name, String value) {
		Map<String, String> newParameters = new HashMap<String, String>();
		if (parameters != null) {
			newParameters.putAll(parameters);
		}
		newParameters.put(name, value);
		return newParameters;
	}

	protected Map<String, String> newParameters(Map<String, String> parameters, String name1, String value1, String name2, String value2) {
		Map<String, String> newParameters = new HashMap<String, String>();
		if (parameters != null) {
			newParameters.putAll(parameters);
		}
		newParameters.put(name1, value1);
		newParameters.put(name2, value2);
		return newParameters;
	}

}
