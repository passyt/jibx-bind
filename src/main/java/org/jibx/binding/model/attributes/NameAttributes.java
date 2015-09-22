package org.jibx.binding.model.attributes;

/**
 * 
 * @author Passyt
 *
 */
public class NameAttributes extends AttributeBase {

	/**
	 * Local (unqualified) name of element or attribute.
	 */
	private String name;
	/**
	 * Gives the namespace URI for the element or attribute name. If this is not
	 * used the default value is the innermost default namespace for this type,
	 * if any.
	 */
	private String uri;

	public NameAttributes() {
		super();
	}

	public NameAttributes(String name) {
		super();
		this.name = name;
	}

	public NameAttributes(String name, String uri) {
		super();
		this.name = name;
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
		NameAttributes other = (NameAttributes) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return "NameAttributes [name=" + name + ", uri=" + uri + "]";
	}

}
