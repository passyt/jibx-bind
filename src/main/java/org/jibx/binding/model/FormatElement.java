package org.jibx.binding.model;

import org.jibx.binding.model.attributes.StringAttributes;

/**
 * The format element defines a conversion between some primitive or object type
 * and the text representation used for that type. Any type with a format
 * defined can be used with simple element (one with no child elements), text,
 * or attribute values. During unmarshalling, the text content of the element or
 * attribute is passed to the deserializer defined for that format, which
 * returns a value of the appropriate type. During marshalling, the typed value
 * is passed to the serializer for that format, which returns a text string used
 * as the element content or attribute value.<br/>
 * <br/>
 * 
 * This flexibility for conversion handling is also available without using a
 * format element definition, by directly specifying the serializer and/or
 * deserializer in a value definition. The advantage to using a format
 * definition is that it can be easily reused. There are two ways of doing this.
 * When a format element does not have a label attribute, it automatically
 * becomes the default for values of the associated type. When a format element
 * is defined with a label, that label can be used to reference it as needed.<br/>
 * <br/>
 * 
 * <b>format</b> elements are subject to the restriction that labels must be
 * unique, and unlabeled formats with the same type cannot be defined in the
 * same context.
 * 
 * @author Passyt
 *
 */
public class FormatElement extends ElementBase implements BindingChild, TemplateChild {

	/**
	 * The label for this format. If supplied, the format can only be used when
	 * referenced by label. If not supplied, this format automatically becomes
	 * the default for handling the specified type within the context. As of
	 * JiBX 1.1, the value of this attribute is interpreted as namespace
	 * qualified.
	 */
	private String label;

	/**
	 * This required attribute gives the fully qualified class name or the
	 * primitive type for the values handled by this format. For instance, for a
	 * format handling instances of the java.util.Date class this would be
	 * 'java.util.Date', and for one handling int primitive values it would be
	 * 'int'.
	 */
	private String type;

	/**
	 * Attributes from the string group set the default conversion of a value
	 * type for all contained elements. See the string attribute group
	 * description for usage details.
	 */
	private StringAttributes attributes;

	public FormatElement() {
		super(ElementType.Format);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public StringAttributes getAttributes() {
		return attributes;
	}

	public void setAttributes(StringAttributes attributes) {
		this.attributes = attributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FormatElement other = (FormatElement) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FormatElement [label=").append(label).append(", type=").append(type).append(", attributes=").append(attributes).append(", comment=").append(comment)
				.append("]");
		return builder.toString();
	}

}
