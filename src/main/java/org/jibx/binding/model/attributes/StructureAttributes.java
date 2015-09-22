package org.jibx.binding.model.attributes;

/**
 * 
 * @author Passyt
 *
 */
public class StructureAttributes extends AttributeBase {

	/**
	 * Determines whether repeated elements within an unordered group should be
	 * allowed. The default is "false", meaning that if a bound element is
	 * repeated the runtime code will throw an exception. Setting this "true"
	 * means repeated elements will be processed the same as in pre-1.1 versions
	 * of JiBX. A "true" value for this attribute is only allowed when all child
	 * definitions are elements (no attributes or text), and requires
	 * ordered="false". It cannot be used in combination with choice="true".
	 * This attribute is ignored on a collection element.
	 */
	private boolean isAllowRepeats;
	/**
	 * Defines whether child binding definitions represent a choice between
	 * alternatives, with only one allowed (value "true") or a set of
	 * possibilities of which one or more may be present ("false", the default).
	 * A "true" value for this attribute is only allowed when all child
	 * definitions are elements (no attributes or text), and requires
	 * ordered="false". It cannot be used in combination with
	 * allow-repeats="true" or flexible="true". This attribute is ignored on a
	 * collection element.
	 */
	private boolean isChoice;
	/**
	 * Defines whether unknown elements within an unordered group should be
	 * ignored. The default is "false", meaning that if an unknown element (one
	 * not allowed by the binding) is found during unmarshalling the runtime
	 * code will throw an exception. Setting this "true" means unknown elements
	 * will be ignored (along with all their content). A "true" value for this
	 * attribute is only allowed when all child definitions are elements (no
	 * attributes or text), and requires ordered="false". It cannot be used in
	 * combination with choice="true". This attribute is ignored on a collection
	 * element.
	 */
	private boolean isFlexible;
	/**
	 * Gives a label allowing the list of child components to be referenced from
	 * elsewhere in the binding definition. Note that this technique has been
	 * deprecated, and will not be supported in JiBX 2.0. In most cases an
	 * abstract mapping can be used as a replacement.
	 */
	@Deprecated
	private String labelName;
	/**
	 * Defines whether child binding definitions represent an ordered list
	 * (value "true", the default) or an unordered set ("false"). When this is
	 * set "true", each child value component must define either an element or
	 * an attribute name (attributes are always unordered, so the ordered
	 * setting of the grouping has no effect on attributes). value elements
	 * defining text values (style="text") are not allowed as direct children of
	 * groups with ordered="false".
	 */
	private boolean isOrdered = true;
	/**
	 * References a list of child components defined elsewhere in the binding
	 * definition. The value must match the label value used on a mapping,
	 * structure, or collection element somewhere in the binding definition. The
	 * child binding components of the referenced element are used as the
	 * content of the element making the reference. The object types associated
	 * with the binding definition element making the reference and that
	 * defining the reference must match, and the order established by the
	 * element that defined the reference determines whether the child
	 * definitions are considered as ordered or unordered. The element with this
	 * attribute must not have any child definitions. Note that this technique
	 * has been deprecated, and will not be supported in JiBX 2.0. In most cases
	 * an abstract mapping can be used as a replacement.
	 */
	@Deprecated
	private String usingName;

	public StructureAttributes() {
		super();
	}

	public StructureAttributes(boolean isOrdered) {
		super();
		this.isOrdered = isOrdered;
	}

	public boolean isAllowRepeats() {
		return isAllowRepeats;
	}

	public void setAllowRepeats(boolean isAllowRepeats) {
		this.isAllowRepeats = isAllowRepeats;
	}

	public boolean isChoice() {
		return isChoice;
	}

	public void setChoice(boolean isChoice) {
		this.isChoice = isChoice;
	}

	public boolean isFlexible() {
		return isFlexible;
	}

	public void setFlexible(boolean isFlexible) {
		this.isFlexible = isFlexible;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public boolean isOrdered() {
		return isOrdered;
	}

	public void setOrdered(boolean isOrdered) {
		this.isOrdered = isOrdered;
	}

	public String getUsingName() {
		return usingName;
	}

	public void setUsingName(String usingName) {
		this.usingName = usingName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAllowRepeats ? 1231 : 1237);
		result = prime * result + (isChoice ? 1231 : 1237);
		result = prime * result + (isFlexible ? 1231 : 1237);
		result = prime * result + (isOrdered ? 1231 : 1237);
		result = prime * result + ((labelName == null) ? 0 : labelName.hashCode());
		result = prime * result + ((usingName == null) ? 0 : usingName.hashCode());
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
		StructureAttributes other = (StructureAttributes) obj;
		if (isAllowRepeats != other.isAllowRepeats)
			return false;
		if (isChoice != other.isChoice)
			return false;
		if (isFlexible != other.isFlexible)
			return false;
		if (isOrdered != other.isOrdered)
			return false;
		if (labelName == null) {
			if (other.labelName != null)
				return false;
		} else if (!labelName.equals(other.labelName))
			return false;
		if (usingName == null) {
			if (other.usingName != null)
				return false;
		} else if (!usingName.equals(other.usingName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StructureAttributes [isAllowRepeats=" + isAllowRepeats + ", isChoice=" + isChoice + ", isFlexible=" + isFlexible + ", labelName=" + labelName + ", isOrdered="
				+ isOrdered + ", usingName=" + usingName + "]";
	}

}
