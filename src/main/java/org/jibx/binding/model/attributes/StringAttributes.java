package org.jibx.binding.model.attributes;

/**
 * 
 * @author Passyt
 *
 */
public class StringAttributes extends AttributeBase {

	/**
	 * Gives the default value for a conversion. This is only allowed for
	 * optional properties. If not specified, the default for primitive types is
	 * the same as the member variable initial state defined by the JLS, and for
	 * object types is "null".
	 */
	private String defaultText;
	/**
	 * Specifies a method to be used to obtain the XML text representation for a
	 * Java 5 enum class. If specified, this value method is used for both
	 * marshalling and unmarshalling instances of the enum class (in the
	 * unmarshalling case, by checking each instance of the enum in turn until
	 * one is found matching the input text). If not specified, the toString()
	 * method of the enum class is instead used for marshalling, and the static
	 * valueOf enum method is used for unmarshalling, both of which use the enum
	 * value name directly.
	 */
	private String enumValueMethodName;
	/**
	 * Defines a custom deserialization handler method, as the fully-qualified
	 * name of a static method with the signature Target xxxx(String text),
	 * where xxxx is the method name and Target is the type of the property
	 * (primitive or object type). Note that when a custom deserialization
	 * handler method is used for an optional object type with no default value,
	 * that method will be called with a null argument when the corresponding
	 * value is missing in the input document. It's up to the handler method to
	 * handle this case appropriately (by returning either a null or an object
	 * of the expected type).
	 */
	private String deserializerName;
	/**
	 * Defines a custom serialization handler method, as the fully-qualified
	 * name of a static method with the signature String xxxx(Target value),
	 * where xxxx is the method name and Target is the type of the property
	 * (primitive or object type).
	 */
	private String serializerName;
	/**
	 * Selects how whitespace will be handled when deserializing values. This
	 * optional attribute can have the values: "preserve", meaning all text is
	 * processed just as provided by the parser; "replace", meaning all tab,
	 * newline, and carriage return characters in the text are replaced by space
	 * characters before the text is processed; "collapse", meaning that after
	 * "replace" processing is done all leading and trailing space characters
	 * are eliminated and all embedded sequences of multiple spaces are replaced
	 * by single spaces; or "trim", meaning that after "replace" processing is
	 * done all leading and trailing space characters are eliminated.
	 */
	private String whitespaceName;

	public String getDefaultText() {
		return defaultText;
	}

	public void setDefaultText(String defaultText) {
		this.defaultText = defaultText;
	}

	public String getEnumValueMethodName() {
		return enumValueMethodName;
	}

	public void setEnumValueMethodName(String enumValueMethodName) {
		this.enumValueMethodName = enumValueMethodName;
	}

	public String getDeserializerName() {
		return deserializerName;
	}

	public void setDeserializerName(String deserializerName) {
		this.deserializerName = deserializerName;
	}

	public String getSerializerName() {
		return serializerName;
	}

	public void setSerializerName(String serializerName) {
		this.serializerName = serializerName;
	}

	public String getWhitespaceName() {
		return whitespaceName;
	}

	public void setWhitespaceName(String whitespaceName) {
		this.whitespaceName = whitespaceName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((defaultText == null) ? 0 : defaultText.hashCode());
		result = prime * result + ((deserializerName == null) ? 0 : deserializerName.hashCode());
		result = prime * result + ((enumValueMethodName == null) ? 0 : enumValueMethodName.hashCode());
		result = prime * result + ((serializerName == null) ? 0 : serializerName.hashCode());
		result = prime * result + ((whitespaceName == null) ? 0 : whitespaceName.hashCode());
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
		StringAttributes other = (StringAttributes) obj;
		if (defaultText == null) {
			if (other.defaultText != null)
				return false;
		} else if (!defaultText.equals(other.defaultText))
			return false;
		if (deserializerName == null) {
			if (other.deserializerName != null)
				return false;
		} else if (!deserializerName.equals(other.deserializerName))
			return false;
		if (enumValueMethodName == null) {
			if (other.enumValueMethodName != null)
				return false;
		} else if (!enumValueMethodName.equals(other.enumValueMethodName))
			return false;
		if (serializerName == null) {
			if (other.serializerName != null)
				return false;
		} else if (!serializerName.equals(other.serializerName))
			return false;
		if (whitespaceName == null) {
			if (other.whitespaceName != null)
				return false;
		} else if (!whitespaceName.equals(other.whitespaceName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StringAttributes [defaultText=" + defaultText + ", enumValueMethodName=" + enumValueMethodName + ", deserializerName=" + deserializerName + ", serializerName="
				+ serializerName + ", whitespaceName=" + whitespaceName + "]";
	}

}