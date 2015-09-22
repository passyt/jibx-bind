package org.jibx.binding.model.attributes;

import org.jibx.binding.model.Usage;

/**
 * 
 * @author Passyt
 *
 */
public class PropertyAttributes extends AttributeBase {

	/**
	 * Gives the name of the field within the containing class that supplies the
	 * property value. This is required except for auto-generated identity
	 * fields, for values from a collection, or when both get-method (for output
	 * bindings) and set-method (for input bindings) definitions are supplied.
	 */
	private String fieldName;
	/**
	 * Defines a method to be called by JiBX to indicate the presence or absence
	 * of the associated element (or attribute, though this is mainly useful
	 * with elements). This is the name of a method taking a boolean parameter,
	 * which will be called with the value true if the element is present or the
	 * value false if it is not present. This can be used in combination with
	 * the test-method attribute to implement a presence flag for an optional
	 * element which can be used even if the content of the element is ignored
	 * (by using an empty structure definition).
	 */
	private String flagMethodName;
	/**
	 * Defines a "get" method for retrieving the property value from an instance
	 * of the containing class. This is the name of a no-argument method
	 * returning a value (primitive or object). If a get-method is defined for
	 * an object value represented by some form of structure in the binding (not
	 * just a simple value, in other words), the method will be used to retrieve
	 * the current instance of an object when unmarshalling. This follows the
	 * principle of JiBX reusing existing objects for unmarshalled data where
	 * possible. If you return a null value during unmarshalling, JiBX will
	 * create a new instance of the object for unmarshalled data.
	 */
	private String getMethodName;
	/**
	 * Defines a "set" method for storing the property value in an instance of
	 * the containing class. This is the name of a method with return type void,
	 * taking a single value (primitive or object) as a parameter. If both
	 * get-method and set-method are defined, the set-method parameter type must
	 * be the same as the get-method return value.
	 */
	private String setMethodName;
	/**
	 * Defines a method for checking if an optional property is present in an
	 * instance of the containing class. This is the name of a no-argument
	 * method with return type boolean, which must return true if the property
	 * is present and false if it is not present. This is only allowed in
	 * combination with usage="optional". If not specified, a simple ==
	 * comparison is used with primitive types to check for a value different
	 * from the default, and a equals() comparison for object types with
	 * non-null defaults
	 */
	private String testMethodName;
	/**
	 * Supplies the fully-qualified class name for the property value. This can
	 * be used to force a more specific type for a property value defined by the
	 * field definition or access method signature as either a base class or an
	 * interface.
	 */
	private String type;

	/**
	 * Defines the usage requirement for this property. The value can either be
	 * "required" (property is always present, the default if not specified) or
	 * "optional" (property is optional).
	 */
	private Usage usage = Usage.Required;

	public PropertyAttributes() {
		super();
	}

	public PropertyAttributes(String type) {
		super();
		this.type = type;
	}

	public PropertyAttributes(String fieldName, Usage usage) {
		super();
		this.fieldName = fieldName;
		this.usage = usage;
	}

	public PropertyAttributes(String getMethodName, String setMethodName, Usage usage) {
		super();
		this.getMethodName = getMethodName;
		this.setMethodName = setMethodName;
		this.usage = usage;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFlagMethodName() {
		return flagMethodName;
	}

	public void setFlagMethodName(String flagMethodName) {
		this.flagMethodName = flagMethodName;
	}

	public String getGetMethodName() {
		return getMethodName;
	}

	public void setGetMethodName(String getMethodName) {
		this.getMethodName = getMethodName;
	}

	public String getSetMethodName() {
		return setMethodName;
	}

	public void setSetMethodName(String setMethodName) {
		this.setMethodName = setMethodName;
	}

	public String getTestMethodName() {
		return testMethodName;
	}

	public void setTestMethodName(String testMethodName) {
		this.testMethodName = testMethodName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Usage getUsage() {
		return usage;
	}

	public void setUsage(Usage usage) {
		this.usage = usage;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((flagMethodName == null) ? 0 : flagMethodName.hashCode());
		result = prime * result + ((getMethodName == null) ? 0 : getMethodName.hashCode());
		result = prime * result + ((setMethodName == null) ? 0 : setMethodName.hashCode());
		result = prime * result + ((testMethodName == null) ? 0 : testMethodName.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyAttributes other = (PropertyAttributes) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (flagMethodName == null) {
			if (other.flagMethodName != null)
				return false;
		} else if (!flagMethodName.equals(other.flagMethodName))
			return false;
		if (getMethodName == null) {
			if (other.getMethodName != null)
				return false;
		} else if (!getMethodName.equals(other.getMethodName))
			return false;
		if (setMethodName == null) {
			if (other.setMethodName != null)
				return false;
		} else if (!setMethodName.equals(other.setMethodName))
			return false;
		if (testMethodName == null) {
			if (other.testMethodName != null)
				return false;
		} else if (!testMethodName.equals(other.testMethodName))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (usage != other.usage)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PropertyAttributes [fieldName=" + fieldName + ", flagMethodName=" + flagMethodName + ", getMethodName=" + getMethodName + ", setMethodName=" + setMethodName
				+ ", testMethodName=" + testMethodName + ", type=" + type + ", usage=" + usage + "]";
	}
}
