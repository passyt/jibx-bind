package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.List;

import org.jibx.binding.IBindingContext;

/**
 * The <b>collection</b> element defines the binding for a Java collection. Many
 * variations of list-like collections are supported, including user-defined
 * collection types. Arrays are also supported. Maps are not supported directly
 * in JiBX 1.0 (but see the JiBX extras description for custom
 * marshaller/unmarshaller classes which can help with this types of
 * structures).<br/>
 * <br/>
 * 
 * Collections may consist of a single type of object or multiple types. The
 * simplest form of collection just uses a single mapped item type. This may
 * either be defined using the item-type attribute, in which case no child
 * definitions are necessary, or implied by the absence of child definitions
 * (equivalent to using <b>item-type="java.lang.Object"</b>).<br/>
 * <br/>
 * 
 * Collections consisting of multiple types are defined using multiple child
 * definitions within the <b>collection</b> element. These may be ordered (where
 * the different types occur in a particular sequence) or unordered (where all
 * are mixed together), as determined by the <b>ordered</b> attribute of the
 * structure attribute group. Child definitions within collections must define
 * elements rather than text or attribute values (though the elements defined
 * may themselves have attributes and/or text values). Child definitions within
 * collections are always treated as optional, with any number of occurrances of
 * the corresponding element allowed (zero or more).<br/>
 * <br/>
 * 
 * The <b>collection</b> element supports several unique attributes along with
 * several common attribute groups, listed below. The unique attributes are used
 * for special types of data access to the collection. These are all optional,
 * and have defaults for common collection types, including java.util.Vector and
 * java.util.ArrayList (as well as subclasses of these classes) along with
 * collection classes implementing the java.util.Collection interface. You can
 * use these attributes to select methods of the containing object for accessing
 * data within a collection (with no property definition for the actual
 * collection object).<br/>
 * <br/>
 * 
 * One potential issue in working with collections is that JiBX generally needs
 * a way to create an instance of the collection when unmarshalling. If the
 * collection is defined using an interface such as java.util.List you'll need
 * to either define a concrete implementation type with a no-argument
 * constructor to be used for the collection (using the <b>type</b> attribute of
 * the property attribute group) or use the <b>factory</b> attribute of the
 * object attribute group to define a factory method to call when an instance of
 * the collection is needed. The org.jibx.runtime.Utility.arrayListFactory is an
 * example of such a factory method, which can be used directly to supply
 * instances of the java.util.ArrayList class.<br/>
 * <br/>
 * 
 * As with all object-valued properties, if the collection property is already
 * initialized when JiBX begins unmarshalling the existing collection instance
 * will be used to hold the unmarshalled items of the collection. JiBX does not
 * clear items from the existing collection before unmarshalling, so if you want
 * to reuse existing data structures with a collection you should clear the
 * collection yourself before unmarshalling (one easy way of doing this is with
 * a pre-set method on the containing object class). This is only necessary when
 * reusing objects, not when unmarshalling to a new instance of an object (where
 * any collections created by the object constructor will initially be empty in
 * any case). If an element name is used with an optional collection, and that
 * name is missing from an input XML document, the collection property will be
 * set to null. If you don't want the collection property to ever be set to
 * null, use a wrapper <b>structure</b> element for the optional element name
 * around the <b>collection</b> element.
 * 
 * @author Passyt
 *
 */
public class CollectionElement extends StructureElementBase implements NestingChild {

	/**
	 * This is an indexed load item method for the collection. If used, the
	 * value must be the name of a member method of the collection class taking
	 * a single int argument and returning the indexed item value from the
	 * collection (which must be an instance of java.lang.Object unless the
	 * item-type attribute is used to specify the type of items in the
	 * collection). This attribute is only allowed in combination with
	 * size-method. The generated code will use the specified method for loading
	 * values from the collection when marshalling.
	 */
	private String loadMethodName;
	/**
	 * This is an item count method for the collection. If used, the value must
	 * be the name of a no-argument member method of the collection class
	 * returning an int value giving the number of items in the collection. This
	 * attribute is only allowed in combination with load-method. The generated
	 * code will use the specified method for finding the count of items present
	 * in the collection when marshalling.
	 */
	private String sizeMethodName;
	/**
	 * This is an indexed store item method for the collection. If used, the
	 * value must be the name of a member method of the collection class taking
	 * an int argument and a java.lang.Object argument (or the type given by the
	 * item-type attribute, if present), with no return value. The generated
	 * code will use the specified method for storing values to the collection
	 * when unmarshalling..
	 */
	private String storeMethodName;
	/**
	 * This is an append item method for the collection. If used, the value must
	 * be the name of a member method of the collection class taking a single
	 * java.lang.Object argument (or the type given by the item-type attribute,
	 * if present). Any return value from the method is ignored. The generated
	 * code will use the specified method to append values to the collection
	 * when unmarshalling..
	 */
	private String addMethodName;
	/**
	 * This is an iterator method for the collection. If used, the value must be
	 * the name of a member method of the collection class taking no arguments
	 * and returning a java.lang.Iterator or java.lang.Enumeration object for
	 * the items in the collection. The generated code will use the specified
	 * method to iterate through the values in the collection when marshalling.
	 */
	private String iterMethodName;
	/**
	 * If this attribute is used it must be the fully-qualified class name for
	 * items contained in the collection. If the specified type is an interface
	 * or a class with subclasses any of the implementations of that type can be
	 * used in the collection. The default is java.lang.Object, allowing any
	 * type of objects to be present in the collection.
	 */
	private String itemTypeName;

	private transient boolean existDefaultConstructorOnItemType = true;

	public CollectionElement() {
		super(ElementType.Collection);
	}

	public void setExistDefaultConstructorOnItemType(boolean value) {
		existDefaultConstructorOnItemType = value;
	}

	public boolean isExistDefaultConstructorOnItemType() {
		return existDefaultConstructorOnItemType;
	}

	public String getLoadMethodName() {
		return loadMethodName;
	}

	public void setLoadMethodName(String loadMethodName) {
		this.loadMethodName = loadMethodName;
	}

	public String getSizeMethodName() {
		return sizeMethodName;
	}

	public void setSizeMethodName(String sizeMethodName) {
		this.sizeMethodName = sizeMethodName;
	}

	public String getStoreMethodName() {
		return storeMethodName;
	}

	public void setStoreMethodName(String storeMethodName) {
		this.storeMethodName = storeMethodName;
	}

	public String getAddMethodName() {
		return addMethodName;
	}

	public void setAddMethodName(String addMethodName) {
		this.addMethodName = addMethodName;
	}

	public String getIterMethodName() {
		return iterMethodName;
	}

	public void setIterMethodName(String iterMethodName) {
		this.iterMethodName = iterMethodName;
	}

	public String getItemTypeName() {
		return itemTypeName;
	}

	public void setItemTypeName(String itemTypeName) {
		this.itemTypeName = itemTypeName;
	}

	@Override
	public List<String> getAllNamespaces(IBindingContext context) {
		List<String> list = new ArrayList<String>();
		if (nameAttributes.getUri() != null) {
			list.add(nameAttributes.getUri());
		}
		for (NestingChild each : nestingChildren) {
			list.addAll(each.getAllNamespaces(context));
		}
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((addMethodName == null) ? 0 : addMethodName.hashCode());
		result = prime * result + ((itemTypeName == null) ? 0 : itemTypeName.hashCode());
		result = prime * result + ((iterMethodName == null) ? 0 : iterMethodName.hashCode());
		result = prime * result + ((loadMethodName == null) ? 0 : loadMethodName.hashCode());
		result = prime * result + ((sizeMethodName == null) ? 0 : sizeMethodName.hashCode());
		result = prime * result + ((storeMethodName == null) ? 0 : storeMethodName.hashCode());
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
		CollectionElement other = (CollectionElement) obj;
		if (addMethodName == null) {
			if (other.addMethodName != null)
				return false;
		} else if (!addMethodName.equals(other.addMethodName))
			return false;
		if (itemTypeName == null) {
			if (other.itemTypeName != null)
				return false;
		} else if (!itemTypeName.equals(other.itemTypeName))
			return false;
		if (iterMethodName == null) {
			if (other.iterMethodName != null)
				return false;
		} else if (!iterMethodName.equals(other.iterMethodName))
			return false;
		if (loadMethodName == null) {
			if (other.loadMethodName != null)
				return false;
		} else if (!loadMethodName.equals(other.loadMethodName))
			return false;
		if (sizeMethodName == null) {
			if (other.sizeMethodName != null)
				return false;
		} else if (!sizeMethodName.equals(other.sizeMethodName))
			return false;
		if (storeMethodName == null) {
			if (other.storeMethodName != null)
				return false;
		} else if (!storeMethodName.equals(other.storeMethodName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CollectionElement [loadMethodName=").append(loadMethodName).append(", sizeMethodName=").append(sizeMethodName).append(", storeMethodName=")
				.append(storeMethodName).append(", addMethodName=").append(addMethodName).append(", iterMethodName=").append(iterMethodName).append(", itemTypeName=")
				.append(itemTypeName).append(", propertyAttributes=").append(propertyAttributes).append(", nameAttributes=").append(nameAttributes).append(", objectAttributes=")
				.append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=").append(attributes).append(", nestingChildren=")
				.append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
