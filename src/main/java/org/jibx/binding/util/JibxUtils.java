package org.jibx.binding.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.jibx.binding.JibxConst;

/**
 * 
 * @author Passyt
 *
 */
public class JibxUtils {

	public static String buildClassNamesBlob(List<String> mappednames) {
		return buildClassNamesBlob(mappednames.toArray(new String[0]));
	}

	public static <T> T checkNotNull(T reference, Object errorMessage) {
		if (reference == null) {
			throw new NullPointerException(String.valueOf(errorMessage));
		}
		return reference;
	}

	public static <T> T checkNotNull(T reference) {
		if (reference == null) {
			throw new NullPointerException();
		}
		return reference;
	}

	public static String buildClassNamesBlob(String[] names) {
		StringBuilder buff = new StringBuilder();
		String last = "";
		for (int i = 0; i < names.length; i++) {
			if (i > 0) {
				buff.append('|');
			}
			String name = names[i];
			if (name != null) {
				int base = 0;
				int limit = Math.min(last.length(), name.length());
				int scan = -1;
				while (++scan < limit) {
					char chr = last.charAt(scan);
					if (chr == name.charAt(scan)) {
						if (chr == '.') {
							buff.append('.');
							base = scan + 1;
						} else if (chr == '$') {
							buff.append('$');
							base = scan + 1;
						}
					} else {
						break;
					}
				}
				if (scan < limit || (scan == limit && last.length() != name.length())) {
					buff.append(name.substring(base));
				}
				last = name;
			}
		}
		return buff.toString();
	}

	public static String accessClassName(String bindingName, String className) {
		int index = className.lastIndexOf(".");
		String packagePrefix = "";
		String classSimpleName = className;
		if (index > 0) {
			packagePrefix = className.substring(0, index) + ".";
			classSimpleName = className.substring(index + 1);
		}

		return packagePrefix + JibxConst.GENERATE_PREFIX + bindingName + classSimpleName + JibxConst.ADAPTERCLASS_SUFFIX;
	}

	public static String newInstanceMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_newinstance_" + bindingId + "_" + generateId;
	}

	public static String testMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_test_" + bindingId + "_" + generateId;
	}

	public static String marshalAttrMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_marshalAttr_" + bindingId + "_" + generateId;
	}

	public static String marshalMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_marshal_" + bindingId + "_" + generateId;
	}

	public static String unmarshalMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_unmarshal_" + bindingId + "_" + generateId;
	}

	public static String unmarshalAttrMethodName(String bindingName, String className, int bindingId, int generateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_unmarshalAttr_" + bindingId + "_" + generateId;
	}

	public static String attrTestMethodName(String bindingName, String className, int currentBindingId, int currentGenerateId) {
		return JibxConst.GENERATE_PREFIX + bindingName + "_attrTest_" + currentBindingId + "_" + currentGenerateId;
	}

	public static String newVariableName() {
		return "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String enumName(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}

		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> newHashSet(T... objects) {
		Set<T> set = new HashSet<T>();
		for (T each : objects) {
			set.add(each);
		}
		return set;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> newArrayList(T... objects) {
		List<T> set = new ArrayList<T>();
		for (T each : objects) {
			set.add(each);
		}
		return set;
	}

	public static String getResourceName(String bindingFilePath) {
		if (bindingFilePath.indexOf("\\") > 0) {
			return bindingFilePath.substring(bindingFilePath.lastIndexOf("\\") + 1);
		}

		return bindingFilePath.substring(bindingFilePath.lastIndexOf("/") + 1);
	}

	public static InputStream getInputStream(String path, String currentPath) {
		try {
			if (path.startsWith("classpath:")) {
				return JibxUtils.class.getClassLoader().getResourceAsStream(path.substring(10));
			} else if (path.startsWith("file:")) {
				return new FileInputStream(path.substring(5));
			} else if (currentPath != null) {
				String parentPath = currentPath.replace("\\\\", "/").replace("\\", "/");
				parentPath = parentPath.substring(0, parentPath.lastIndexOf("/") + 1);
				if (currentPath.startsWith("classpath:")) {
					return JibxUtils.class.getClassLoader().getResourceAsStream(parentPath.substring(10) + path);
				} else if (path.startsWith("file:")) {
					return new FileInputStream(parentPath.substring(5) + path);
				} else {
					return new FileInputStream(parentPath + path);
				}
			}
			return null;
		} catch (FileNotFoundException e) {
			throw new IllegalStateException(e);
		}
	}

}