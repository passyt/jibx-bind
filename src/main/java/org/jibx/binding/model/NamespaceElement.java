package org.jibx.binding.model;

/**
 * The namespace element defines a namespace used within the bound XML
 * documents. Each namespace used within the documents must be defined using one
 * of these elements. When marshalling to XML, the namespace definitions
 * included in the binding are automatically added to the appropriate XML
 * elements.<br/>
 * <br/>
 * 
 * This works differently depending upon whether the namespace definition is in
 * the binding element context, or that of a mapping element. If the namespace
 * is the child of a mapping element it will be defined on the element
 * associated with that mapping when it is marshalled. Only mapping elements
 * that define an element name are allowed to have namespace child elements for
 * this reason. If the namespace is a child of the binding element, it applies
 * to all the top-level mappings defined within that binding. The effect is the
 * same as if the namespace were instead present as a child element of every
 * top-level mapping element.<br/>
 * <br/>
 * 
 * Besides defining a namespace for the XML documents, the namespace element can
 * also make that namespace the default for elements and/or attributes within
 * the context of the definition. This is only a shortcut - a namespace URI can
 * always be defined directly for each element or attribute, and a direct
 * definition will always override the default defined by a namespace element.
 * However, using a default namespace definition allows easy handling of common
 * XML document structures. A default namespace for element names applies to the
 * containing mapping element name as well as child element names.<br/>
 * <br/>
 * 
 * <b>namespace</b> elements are subject to the restrictions that namespaces
 * with the same prefix cannot be defined in the same context, and no two
 * namespaces can be defined with conflicting defaults within the same context
 * (i.e., two namespaces each having either <b>default="elements"</b> or
 * <b>default="all"</b>, or two namespaces each having
 * <b>default="attributes"</b> or <b>default="all"</b>).
 * 
 * @author Passyt
 *
 */
public class NamespaceElement extends ElementBase implements BindingChild, TemplateChild {

	/**
	 * This required attribute is the namespace URI being defined.
	 */
	private String uri;
	/**
	 * This is the prefix to map to the namespace when marshalling. It is
	 * ignored when unmarshalling, since the namespace URI is used directly to
	 * identify elements and attributes. This attribute is required for
	 * namespaces in an output binding unless default="elements" is specified.
	 */
	private String prefix;
	/**
	 * Optionally gives the default usage of this namespace, which must be
	 * "none" (only used where specified, the default if this attribute is not
	 * given), "elements" (default for elements only), "attributes" (default for
	 * attributes only), or "all" (both elements and attributes use this
	 * namespace by default).
	 */
	private NamespaceStyle defaultStyle = NamespaceStyle.None;

	public NamespaceElement() {
		super(ElementType.Namespace);
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public NamespaceStyle getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(NamespaceStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((defaultStyle == null) ? 0 : defaultStyle.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
		NamespaceElement other = (NamespaceElement) obj;
		if (defaultStyle != other.defaultStyle)
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NamespaceElement [uri=").append(uri).append(", prefix=").append(prefix).append(", defaultStyle=").append(defaultStyle).append(", type=").append(type)
				.append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
