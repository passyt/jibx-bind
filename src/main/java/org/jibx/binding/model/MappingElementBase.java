package org.jibx.binding.model;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.attributes.NameAttributes;

/**
 * 
 * @author Passyt
 *
 */
public abstract class MappingElementBase extends TemplateElementBase implements BindingChild, TemplateChild {

	/**
	 * Optional flag for an abstract mapping that can be used directly as a type
	 * mapping. This must be "true" (abstract mapping) or "false" (normal
	 * mapping, the default if the attribute is not specified).
	 */
	private boolean isAbstract;
	/**
	 * Optional attribute giving the fully qualified class name of a base
	 * mapping for which this mapping can be substituted. Generally this is used
	 * with mappings for classes which are subclasses of the class handled by
	 * the base mapping, but it can be also be used with unrelated classes. The
	 * base mapping may itself extend another mapping, in which case the new
	 * extensions also become substitutes for that "inherited" base mapping.
	 * Extension is based on element names and does not necessarily indicate any
	 * kind of inheritance in terms of the Java class structures (though it is
	 * often used for the purpose of representing in XML a tree of subclasses
	 * which can be used as instances of a base type).
	 */
	private String extendsName;
	/**
	 * Optional attribute giving the type name for an abstract mapping. This
	 * allows alternative abstract mappings to be defined for the same class,
	 * and referenced by name with a map-as attribute on a structure element (or
	 * looked up at runtime when force-classes="true" is used on the binding
	 * element). As of JiBX 1.1, the value of this attribute is interpreted as
	 * namespace qualified.
	 */
	private String typeName;
	/**
	 * Attributes from the name group define an element mapped to the object
	 * type handled by this mapping. The name is required on non-abstract
	 * mappings unless a custom marshaller/unmarshaller is supplied (see the
	 * object attribute group, below), in which case it is optional. Abstract
	 * mappings should not define a name, but are currently allowed to do so
	 * with a warning for support of legacy bindings. See the name attribute
	 * group description for name usage details.
	 */
	private NameAttributes nameAttributes = new NameAttributes();

	private transient boolean precompiled = false;
	private BindingElement precompiledBindingElement;

	public MappingElementBase(ElementType type) {
		super(type);
	}

	public BindingElement getPrecompiledBindingElement() {
		return precompiledBindingElement;
	}

	public void setPrecompiledBindingElement(BindingElement precompiledBindingElement) {
		this.precompiledBindingElement = precompiledBindingElement;
	}

	public boolean isAbstract() {
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	public String getExtendsName() {
		return extendsName;
	}

	public void setExtendsName(String extendsName) {
		this.extendsName = extendsName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public NameAttributes getNameAttributes() {
		return nameAttributes;
	}

	public void setNameAttributes(NameAttributes nameAttributes) {
		this.nameAttributes = nameAttributes;
	}

	public boolean hasNamespace(IBindingContext context) {
		if (nameAttributes.getUri() != null) {
			return true;
		}

		return context.currentDefaultElementNamespace() != null;
	}

	public int getNamespaceIndex(IBindingContext context) {
		String namespace = getNamespace(context);
		if (namespace == null) {
			return 0;
		}

		return context.getNamespaces().get(namespace).getIndex();
	}

	public String getNamespace(IBindingContext context) {
		if (nameAttributes.getUri() != null) {
			return nameAttributes.getUri();
		}

		return context.currentDefaultElementNamespace();
	}

	public boolean isPrecompiled() {
		return precompiled;
	}

	public void setPrecompiled(boolean precompiled) {
		this.precompiled = precompiled;
	}

	public String getIdentityName() {
		return getTypeName() == null ? getClassName() : getTypeName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((extendsName == null) ? 0 : extendsName.hashCode());
		result = prime * result + (isAbstract ? 1231 : 1237);
		result = prime * result + ((nameAttributes == null) ? 0 : nameAttributes.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
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
		MappingElementBase other = (MappingElementBase) obj;
		if (extendsName == null) {
			if (other.extendsName != null)
				return false;
		} else if (!extendsName.equals(other.extendsName))
			return false;
		if (isAbstract != other.isAbstract)
			return false;
		if (nameAttributes == null) {
			if (other.nameAttributes != null)
				return false;
		} else if (!nameAttributes.equals(other.nameAttributes))
			return false;
		if (typeName == null) {
			if (other.typeName != null)
				return false;
		} else if (!typeName.equals(other.typeName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MappingElementBase [isAbstract=").append(isAbstract).append(", extendsName=").append(extendsName).append(", typeName=").append(typeName)
				.append(", nameAttributes=").append(nameAttributes).append(", className=").append(className).append(", templateChildren=").append(templateChildren)
				.append(", objectAttributes=").append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=").append(attributes)
				.append(", nestingChildren=").append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
