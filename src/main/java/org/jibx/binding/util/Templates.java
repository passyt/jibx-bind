package org.jibx.binding.util;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author Passyt
 *
 */
public class Templates {

	private final String rootPath;

	public Templates(String rootPath) {
		super();
		this.rootPath = rootPath;
	}

	public String parseByResource(String resourceName, Map<String, String> parameters) {
		return parse(IOUtils.toString(Templates.class.getClassLoader().getResourceAsStream(rootPath + "/" + resourceName)), parameters);
	}

	public static String parse(String template, Map<String, String> parameters) {
		if (parameters != null) {
			for (Entry<String, String> each : parameters.entrySet()) {
				if (each.getValue() == null) {
					template = template.replace("$" + each.getKey() + "$", "null");
				} else {
					template = template.replace("$" + each.getKey() + "$", each.getValue());
				}
			}
		}

		return template;
	}

}
