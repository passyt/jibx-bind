package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.List;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.model.attributes.StringAttributes;

/**
 * The value element defines a simple value, one that is bound to either an
 * attribute or a simple element (one with no attributes or child elements).
 * This always makes use of a format definition. If no format is explicitly
 * named in the value definition the default format for the property type is
 * used (see Conversions for information on default formats.
 * 
 * @author Passyt
 *
 */
public class ValueElement extends ElementBase implements NestingChild {

	/**
	 * This optional attribute is used to define a constant value. If present,
	 * none of the attributes from the string group and only the usage attribute
	 * from the property group can be present, the format attribute is not
	 * allowed, and the only ident value allowed is "none". If the value is
	 * defined as required it must be present and match the constant on input;
	 * if optional, it is checked for on input and if found must match the
	 * constant. The constant value is always included in the output when
	 * marshalling.
	 */
	private String constant;
	/**
	 * This optional attribute is used to designate a property value as an
	 * identifier type. Possible values are "none" (not an identifier type, the
	 * default if this attribute is not used), "def" (value is a unique
	 * identifier for the containing object), and "ref" (value is an object with
	 * an identifier property and the identifier property of the object is used
	 * in the XML representation rather than the actual object). Only one
	 * property with ident="def" is allowed for a mapping; if one is present the
	 * property must be a String, and must be defined directly as a child of the
	 * mapping element; it is not allowed as a child of a structure element.<br/>
	 * <br/>
	 * 
	 * Values with ident value "ref" cannot be used directly as children of a
	 * collection; they can be used if there is a wrapper object for the
	 * referenced object, as defined by a structure element with an object type
	 * defined. References contained directly in a collection can be handled by
	 * using custom marshaller/unmarshallers - see the JiBX extras description
	 * for a base custom marshaller/unmarshaller you can extend for this
	 * purpose, along with another custom marshaller/unmarshaller you can extend
	 * to include the full representation of an object only at the point of
	 * first use (with a reference if the same object is later used again).
	 */
	private IdentType ident = IdentType.None;
	/**
	 * A style attribute present on the value element determines the type of XML
	 * component used to represent the value. The allowed values are
	 * "attribute", "element", "text", and "cdata". The last two choices are
	 * subject to some restrictions. They cannot be used directly within a
	 * collection definition (in other words, for value elements that have a
	 * collection element as their parent), and also cannot be used for direct
	 * children of an unordered mapping or structure. Even within an ordered
	 * mapping or structure, multiple value elements using these choices must be
	 * separated by a required value using the "element" choice.<br/>
	 * <br/>
	 * 
	 * The default handling for this attribute is also special. If it isn't
	 * specified, it defaults to the value set by the innermost containing
	 * element with a value-style attribute, which will always be either the
	 * "attribute" or "element" choice. See the style attribute group
	 * description for details of this type of usage.
	 */
	private Style style;
	private NameAttributes nameAttributes = new NameAttributes();
	private PropertyAttributes propertyAttributes = new PropertyAttributes();
	private StringAttributes stringAttributes = new StringAttributes();

	public ValueElement() {
		super(ElementType.Value);
	}

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	public IdentType getIdent() {
		return ident;
	}

	public void setIdent(IdentType ident) {
		this.ident = ident;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public NameAttributes getNameAttributes() {
		return nameAttributes;
	}

	public void setNameAttributes(NameAttributes nameAttributes) {
		this.nameAttributes = nameAttributes;
	}

	public PropertyAttributes getPropertyAttributes() {
		return propertyAttributes;
	}

	public void setPropertyAttributes(PropertyAttributes propertyAttributes) {
		this.propertyAttributes = propertyAttributes;
	}

	public StringAttributes getStringAttributes() {
		return stringAttributes;
	}

	public void setStringAttributes(StringAttributes stringAttributes) {
		this.stringAttributes = stringAttributes;
	}

	public static enum Style {
		Attribute, Element, Text, Cdata;
	}

	@Override
	public List<String> getAllNamespaces(IBindingContext context) {
		List<String> list = new ArrayList<String>();
		list.addAll(context.getBindingRoot().getAllNamspaces(false));
		String namespace = getNamespace(context);
		if (namespace != null) {
			list.add(namespace);
		}
		return list;
	}

	public String getNamespace(IBindingContext context) {
		if (nameAttributes.getUri() != null) {
			return nameAttributes.getUri();
		}

		if (Style.Attribute == this.style) {
			return context.currentDefaultAttributeNamespace();
		}
		return context.currentDefaultElementNamespace();
	}

	public int getNamespaceIndex(IBindingContext context) {
		String namespace = getNamespace(context);
		if (namespace == null) {
			return 0;
		}

		return context.getNamespaces().get(namespace).getIndex();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((constant == null) ? 0 : constant.hashCode());
		result = prime * result + ((ident == null) ? 0 : ident.hashCode());
		result = prime * result + ((nameAttributes == null) ? 0 : nameAttributes.hashCode());
		result = prime * result + ((propertyAttributes == null) ? 0 : propertyAttributes.hashCode());
		result = prime * result + ((stringAttributes == null) ? 0 : stringAttributes.hashCode());
		result = prime * result + ((style == null) ? 0 : style.hashCode());
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
		ValueElement other = (ValueElement) obj;
		if (constant == null) {
			if (other.constant != null)
				return false;
		} else if (!constant.equals(other.constant))
			return false;
		if (ident != other.ident)
			return false;
		if (nameAttributes == null) {
			if (other.nameAttributes != null)
				return false;
		} else if (!nameAttributes.equals(other.nameAttributes))
			return false;
		if (propertyAttributes == null) {
			if (other.propertyAttributes != null)
				return false;
		} else if (!propertyAttributes.equals(other.propertyAttributes))
			return false;
		if (stringAttributes == null) {
			if (other.stringAttributes != null)
				return false;
		} else if (!stringAttributes.equals(other.stringAttributes))
			return false;
		if (style != other.style)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValueElement [constant=").append(constant).append(", ident=").append(ident).append(", style=").append(style).append(", nameAttributes=")
				.append(nameAttributes).append(", propertyAttributes=").append(propertyAttributes).append(", stringAttributes=").append(stringAttributes).append(", type=")
				.append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
