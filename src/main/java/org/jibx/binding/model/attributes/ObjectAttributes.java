package org.jibx.binding.model.attributes;

/**
 * 
 * @author Passyt
 *
 */
public class ObjectAttributes extends AttributeBase {

	/**
	 * Gives the type to be used when creating instances of the object during
	 * unmarshalling. This gives an alternative to the factory attribute when
	 * all you want to do is use a specific implementation for an interface or
	 * an abstract class.
	 */
	private String createType;
	/**
	 * Defines a factory method for constructing new instances of an object
	 * type. This applies to bindings for unmarshalling only, and if supplied it
	 * must be in the form of a fully-qualified class and method name (e.g.,
	 * "com.sosnoski.jibx.ObjectBuilderFactory.newInstance" specifies the
	 * newInstance() method of the ObjectBuilderFactory class in the
	 * com.sosnoski.jibx package) for a static method returning an instance of
	 * the bound class. As with the other methods in this group (pre-set,
	 * post-set, and pre-get), three different method signatures are allowed: No
	 * arguments; a single argument of type java.lang.Object, in which case the
	 * owning object is passed in the method call; or a single argument of type
	 * org.jibx.runtime.IUnmarshallingContext, in which case the unmarshalling
	 * context is passed in the method call (this allows access to the entire
	 * stack of objects being unmarshalled). If not supplied, instances of the
	 * bound class are constructed using a null argument constructor.
	 */
	private String factoryName;
	/**
	 * Defines a custom serialization handler class, as the fully-qualified name
	 * of a class implementing the org.jibx.runtime.Marshaller interface. This
	 * is only allowed with an output binding; it is required if an unmarshaller
	 * is defined for an input-output binding.
	 */
	private String marshallerName;
	/**
	 * Allows the W3C XML Schema attribute xsi:nil="true" to be used on an
	 * element in instance documents to indicate that the corresponding object
	 * is null. The default value is "false", set this attribute "true" to
	 * enable xsi:nil support. The marshalling behavior when the attribute is
	 * "true" and the object reference is null depends on whether the binding
	 * defines the corresponding element as optional or required. If the element
	 * is optional it will simply be left out of the marshalled document. If the
	 * element is required it will be written with an xsi:nil="true" attribute.
	 * This attribute can only be used with objects that are bound to an element
	 * name.
	 */
	private boolean isNillable;
	/**
	 * Defines a bound class method called on instances of the class after they
	 * are populated with data from unmarshalling. This can be used for any
	 * postprocessing or validation required by the class. Three different
	 * method signatures are supported, as described in the factory attribute
	 * text.
	 */
	private String postSetName;
	/**
	 * Defines a bound class method called on new instances of the class before
	 * they are marshalled. This can be used for any preprocessing or validation
	 * required by the class. Three different method signatures are supported,
	 * as described in the factory attribute text.
	 */
	private String preGetName;
	/**
	 * Defines a bound class method called on new instances of the class before
	 * they are populated with data from unmarshalling. This can be used for any
	 * initialization or special handling required before a constructed instance
	 * is used. Three different method signatures are supported, as described in
	 * the factory attribute text.
	 */
	private String preSetName;
	/**
	 * Defines a custom deserialization handler class, as the fully-qualified
	 * name of a class implementing the org.jibx.runtime.Unmarshaller interface.
	 * This attribute cannot be used in combination with the factory, or pre-set
	 * attributes. It is only allowed with an input binding; it is required if a
	 * marshaller is defined for an input-output binding.
	 */
	private String unmarshallerName;

	private transient boolean simpleUnmarshallerConstructor = true;
	private transient boolean simpleMarshallerConstructor = true;

	public ObjectAttributes() {
		super();
	}

	public ObjectAttributes(String createType, String factoryName) {
		super();
		this.createType = createType;
		this.factoryName = factoryName;
	}

	public String getCreateType() {
		return createType;
	}

	public void setCreateType(String createType) {
		this.createType = createType;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getMarshallerName() {
		return marshallerName;
	}

	public void setMarshallerName(String marshallerName) {
		this.marshallerName = marshallerName;
	}

	public boolean isNillable() {
		return isNillable;
	}

	public void setNillable(boolean isNillable) {
		this.isNillable = isNillable;
	}

	public String getPostSetName() {
		return postSetName;
	}

	public void setPostSetName(String postSetName) {
		this.postSetName = postSetName;
	}

	public String getPreGetName() {
		return preGetName;
	}

	public void setPreGetName(String preGetName) {
		this.preGetName = preGetName;
	}

	public String getPreSetName() {
		return preSetName;
	}

	public void setPreSetName(String preSetName) {
		this.preSetName = preSetName;
	}

	public String getUnmarshallerName() {
		return unmarshallerName;
	}

	public void setUnmarshallerName(String unmarshallerName) {
		this.unmarshallerName = unmarshallerName;
	}

	public boolean isSimpleUnmarshallerConstructor() {
		return simpleUnmarshallerConstructor;
	}

	public void setSimpleUnmarshallerConstructor(boolean simpleUnmarshallerConstructor) {
		this.simpleUnmarshallerConstructor = simpleUnmarshallerConstructor;
	}

	public boolean isSimpleMarshallerConstructor() {
		return simpleMarshallerConstructor;
	}

	public void setSimpleMarshallerConstructor(boolean simpleMarshallerConstructor) {
		this.simpleMarshallerConstructor = simpleMarshallerConstructor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createType == null) ? 0 : createType.hashCode());
		result = prime * result + ((factoryName == null) ? 0 : factoryName.hashCode());
		result = prime * result + (isNillable ? 1231 : 1237);
		result = prime * result + ((marshallerName == null) ? 0 : marshallerName.hashCode());
		result = prime * result + ((postSetName == null) ? 0 : postSetName.hashCode());
		result = prime * result + ((preGetName == null) ? 0 : preGetName.hashCode());
		result = prime * result + ((preSetName == null) ? 0 : preSetName.hashCode());
		result = prime * result + ((unmarshallerName == null) ? 0 : unmarshallerName.hashCode());
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
		ObjectAttributes other = (ObjectAttributes) obj;
		if (createType == null) {
			if (other.createType != null)
				return false;
		} else if (!createType.equals(other.createType))
			return false;
		if (factoryName == null) {
			if (other.factoryName != null)
				return false;
		} else if (!factoryName.equals(other.factoryName))
			return false;
		if (isNillable != other.isNillable)
			return false;
		if (marshallerName == null) {
			if (other.marshallerName != null)
				return false;
		} else if (!marshallerName.equals(other.marshallerName))
			return false;
		if (postSetName == null) {
			if (other.postSetName != null)
				return false;
		} else if (!postSetName.equals(other.postSetName))
			return false;
		if (preGetName == null) {
			if (other.preGetName != null)
				return false;
		} else if (!preGetName.equals(other.preGetName))
			return false;
		if (preSetName == null) {
			if (other.preSetName != null)
				return false;
		} else if (!preSetName.equals(other.preSetName))
			return false;
		if (unmarshallerName == null) {
			if (other.unmarshallerName != null)
				return false;
		} else if (!unmarshallerName.equals(other.unmarshallerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ObjectAttributes [createType=" + createType + ", factoryName=" + factoryName + ", marshallerName=" + marshallerName + ", isNillable=" + isNillable
				+ ", postSetName=" + postSetName + ", preGetName=" + preGetName + ", preSetName=" + preSetName + ", unmarshallerName=" + unmarshallerName + "]";
	}
}
