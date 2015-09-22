package org.jibx.binding.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.util.Lists;

/**
 * <h3><a name="intro">&lt;structure&gt; Element Definition</a></h3>
 * 
 * <p>
 * The <b>structure</b> element defines a structure component of a binding. This
 * can take any of several forms. The first is where the structure is a complex
 * XML element (one with attributes or child elements) that's linked to an
 * object property of the containing object type. This is the most common form
 * of usage. It's essentially equivalent to an &quot;in-line&quot; mapping
 * definition. This variation applies when both an element name and an object
 * property are defined by the <b>structure</b> element (or when the property is
 * implied rather than defined directly, as when the structure element is the
 * child of a collection element).
 * </p>
 * 
 * <p>
 * Variations of the <b>structure</b> element that define either an element name
 * or a linked property value (but not both) are used for <a
 * href="tutorial/binding-structures.html#intro"><b>structure mapping</b></a>.
 * Each of these variations works differently depending on whether the
 * <b>structure</b> element is empty (does not have any child elements). The
 * full set of structure mapping variations is shown in the table below.
 * </p>
 * 
 * <h4><a name="variations">Structure Mapping Variations</a></h4>
 * <table cellpadding="3" cellspacing="2" border="1" width="100%">
 * <tr class="b">
 * <th>Defines</th>
 * <th>Empty?</th>
 * <th>What it Does</th>
 * </tr>
 * <tr class="a">
 * <td rowspan="2">
 * <p>
 * Element
 * </p>
 * </td>
 * <td>
 * <p>
 * yes
 * </p>
 * </td>
 * <td>
 * <p>
 * Adds an empty element to the XML when marshalling (unless optional, in which
 * case it's ignored when marshalling), discards and ignores the named element
 * when unmarshalling.
 * </p>
 * </td>
 * </tr>
 * <tr class="b">
 * <td>
 * <p>
 * no
 * </p>
 * </td>
 * <td>
 * <p>
 * Structure mapping that defines a complex element (one with attributes or
 * child elements) in the XML representation with no corresponding object. Child
 * attribute or element values are in this case linked directly to properties of
 * the containing object type.
 * </p>
 * </td>
 * </tr>
 * <tr class="a">
 * <td rowspan="2">
 * <p>
 * Property
 * </p>
 * </td>
 * <td>
 * <p>
 * yes
 * </p>
 * </td>
 * <td>
 * <p>
 * References the mapping for the property object type, which must be defined
 * within some enclosing context of this definition. If the property type
 * exactly matches a defined mapping, that mapping will be used; if not, the
 * mapping will be determined by the element name when unmarshalling or the
 * actual object type when marshalling. This allows generic
 * <code>java.lang.Object</code> references to be used, as long as a mapping is
 * defined for the actual type of the item. The only restriction is that if this
 * type of reference is defined as optional it must be the last element in a
 * list (otherwise, if the expected element is missing in a document being
 * unmarshalled the next sibling element will be used instead).
 * </p>
 * </td>
 * </tr>
 * <tr class="b">
 * <td>
 * <p>
 * no
 * </p>
 * </td>
 * <td>
 * <p>
 * Structure mapping that defines an object with no corresponding XML element.
 * Attributes or child elements defined within this <b>structure</b> are part of
 * the XML element defined by the containing structure or mapping. Optional
 * structures with no associated element name are subject to some unmarshalling
 * limitations. If the structure defines both attribute and element values, the
 * presence or absence of the structure is determined based only on the
 * attributes - if one of the defined attributes is present on the containing
 * element the structure is considered to be present, otherwise it is not and
 * the property will be set to <code>null</code>.
 * </p>
 * </td>
 * </tr>
 * </table>
 * 
 * <p>
 * Structures with child definitions may be ordered (where the XML components
 * bound to the child definitions occur in a particular sequence) or unordered
 * (where the XML components can occur in any order), as determined by the
 * <b>ordered</b> attribute of the <a
 * href="binding-attributes.html#structure">structure attribute group</a>. In
 * the case of unordered structures only optional components are allowed as
 * child definitions.
 * </p>
 * 
 * <p>
 * A <b>structure</b> element can be used without either an element name or a
 * linked property value when you just want to say that some child elements make
 * up an unordered set:
 * </p>
 * <div id="source">
 * 
 * <pre>
 * &lt;mapping name="alphas" class="AlphabetBean">
 *     &lt;value name="a" field="a"/>
 *     &lt;structure ordered="false">
 *       &lt;value name="b" field="b" usage="optional"/>
 *       &lt;value name="c" field="c" usage="optional"/>
 *     &lt;/structure>
 *     &lt;value name="d" field="d"/>
 *   &lt;/mapping>
 * </pre>
 * 
 * </div>
 * <p>
 * The above binding definition unmarshals both the element sequences (a, b, c,
 * d) and (a, c, b, d). Since the unordered elements are (and have to be)
 * optional, though, the binding will also allow (a, b, d), (a, c, d), and just
 * (a, d). The unordered elements may also be duplicated without an error, so
 * (a, c, b, c, d) would also be accepted (with repeated values stored in order,
 * so that the final value for a property would be that of the last instance of
 * the element). When marshalling an unordered group the sequence used in the
 * binding definition will always be followed (so (a, c, b, d) would marshal as
 * (a, b, c, d)). If you need to control the order of elements (or preserve the
 * order, when starting from an unmarshalled document) you'll need to instead
 * work with objects that can be held in a <a
 * href="collection-element.html">collection</a>.
 * </p>
 * 
 * <p>
 * When a <b>structure</b> element is used with an object property JiBX will
 * reuse an existing instance of the object during unmarshalling. The way this
 * works is that JiBX only creates a new instance of the object for use in
 * unmarshalling if the property value is <code>null</code>. If an element name
 * is used with an optional structure, and that name is missing from an input
 * XML document, the object property for that structure will always be set to
 * <code>null</code> (since the representation is missing from the XML). If you
 * don't want the property to ever be set to <code>null</code>, use a wrapper
 * <b>structure</b> element for the optional element name around the actual
 * structure definition.
 * </p>
 * 
 * <p>
 * The <b>structure</b> element supports one unique attribute along with several
 * common attribute groups, listed below. The unique attribute is used to
 * reference <b>mapping</b> definitions for objects. It may only be used when a
 * property is supplied (or implied).
 * </p>
 * 
 * @author Passyt
 *
 */
public class StructureElement extends StructureElementBase implements NestingChild {

	/**
	 * This optional attribute can be used to override the type (or type name)
	 * to be used for a mapping reference. If used as a type, the value of this
	 * attribute must be the fully-qualified class name for the mapped type, and
	 * the specified class must be assignment compatible with the actual
	 * property type (so you can't map a java.lang.Integer field as a
	 * java.lang.String, for instance, but can map a field typed
	 * java.lang.Object as a java.lang.String). If used as a type name, the name
	 * must match the type name defined by an abstract mapping. A structure
	 * element with no property reference can use the map-as attribute to invoke
	 * a specified mapping for the this object (where the mapping must be
	 * assignment compatible with the this object type). As of JiBX 1.1, the
	 * value of this attribute is interpreted as namespace qualified.
	 */
	private String mapAsName;

	private TemplateElementBase template;

	public StructureElement() {
		super(ElementType.Structure);
	}

	public String getMapAsName() {
		return mapAsName;
	}

	public void setMapAsName(String mapAsName) {
		this.mapAsName = mapAsName;
	}

	public TemplateElementBase getTemplate() {
		return template;
	}

	public void setTemplate(TemplateElementBase template) {
		this.template = template;
	}

	@Override
	public List<String> getAllNamespaces(IBindingContext context) {
		Set<String> collection = new LinkedHashSet<String>();
		collection.addAll(context.getBindingRoot().getAllNamspaces(false));
		if (nameAttributes.getUri() != null) {
			collection.add(nameAttributes.getUri());
		}
		for (NestingChild each : nestingChildren) {
			collection.addAll(each.getAllNamespaces(context));
		}
		return Lists.newArrayList(collection);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((mapAsName == null) ? 0 : mapAsName.hashCode());
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
		StructureElement other = (StructureElement) obj;
		if (mapAsName == null) {
			if (other.mapAsName != null)
				return false;
		} else if (!mapAsName.equals(other.mapAsName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StructureElement [mapAsName=").append(mapAsName).append(", propertyAttributes=").append(propertyAttributes).append(", nameAttributes=")
				.append(nameAttributes).append(", objectAttributes=").append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=")
				.append(attributes).append(", nestingChildren=").append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}