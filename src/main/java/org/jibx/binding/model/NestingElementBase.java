package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.ValueElement.Style;
import org.jibx.binding.model.attributes.NestingAttributes;

/**
 * 
 * @author Passyt
 *
 */
public class NestingElementBase extends ElementBase {

	protected NestingAttributes attributes = new NestingAttributes();
	protected List<NestingChild> nestingChildren = new ArrayList<NestingChild>();

	protected NestingElementBase(ElementType type) {
		super(type);
	}

	public void addNestingChild(NestingChild element) {
		this.nestingChildren.add(element);
	}

	public Iterator<NestingChild> nestingChildIterator() {
		return this.nestingChildren.iterator();
	}

	public int nestingchildSize() {
		return nestingChildren.size();
	}

	public NestingAttributes getNestingAttributes() {
		return attributes;
	}

	public void setNestingAttributes(NestingAttributes attributes) {
		this.attributes = attributes;
	}

	public List<NestingChild> getNestingChildren() {
		return nestingChildren;
	}

	public void setNestingChildren(List<NestingChild> nestingChildren) {
		this.nestingChildren = nestingChildren;
	}

	public boolean hasAttributes(IBindingContext context) {
		for (NestingChild child : nestingChildren) {
			if (child instanceof StructureElement) {
				StructureElement structureElement = (StructureElement) child;
				MappingElementBase mappingElement = context.getMappingElement(structureElement.getMapAsName());
				if (mappingElement != null && mappingElement.hasAttributes(context)) {
					return true;
				}
			} else if (child instanceof ValueElement) {
				ValueElement element = (ValueElement) child;
				if (Style.Attribute == element.getStyle()) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean hasElements(IBindingContext context) {
		for (NestingChild child : nestingChildren) {
			if (child instanceof ValueElement) {
				ValueElement element = (ValueElement) child;
				if (Style.Attribute != element.getStyle()) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	public List<ValueElement> attributes(IBindingContext context) {
		List<ValueElement> attributes = new ArrayList<ValueElement>();
		for (NestingChild child : nestingChildren) {
			if (child instanceof ValueElement) {
				ValueElement element = (ValueElement) child;
				if (Style.Attribute == element.getStyle()) {
					attributes.add(element);
				}
			}
		}

		return attributes;
	}

	public List<String> getDependsNames(IBindingContext context) {
		List<String> names = new ArrayList<String>();
		for (NestingChild child : nestingChildren) {
			if (child instanceof NestingElementBase) {
				NestingElementBase nestingElementBase = (NestingElementBase) child;
				if (nestingElementBase instanceof StructureElement) {
					StructureElement structureElement = (StructureElement) nestingElementBase;
					if (structureElement.getMapAsName() != null) {
						names.add(structureElement.getMapAsName());
						continue;
					} else if (structureElement.nestingchildSize() == 0 && structureElement.getPropertyAttributes().getType() != null) {
						names.add(structureElement.getPropertyAttributes().getType());
						continue;
					}
				}

				names.addAll(nestingElementBase.getDependsNames(context));
			}
		}

		return names;
	}

	public List<String> getMapAsNames() {
		List<String> names = new ArrayList<String>();
		for (NestingChild child : nestingChildren) {
			if (child instanceof StructureElement) {
				StructureElement structureElement = (StructureElement) child;
				if (structureElement.getMapAsName() != null && structureElement.getPropertyAttributes().getFieldName() == null
						&& structureElement.getPropertyAttributes().getGetMethodName() == null && structureElement.getPropertyAttributes().getSetMethodName() == null) {
					names.add(structureElement.getMapAsName());
				}
			}
		}

		return names;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((attributes == null) ? 0 : attributes.hashCode());
		result = prime * result + ((nestingChildren == null) ? 0 : nestingChildren.hashCode());
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
		NestingElementBase other = (NestingElementBase) obj;
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (nestingChildren == null) {
			if (other.nestingChildren != null)
				return false;
		} else if (!nestingChildren.equals(other.nestingChildren))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("NestingElementBase [attributes=").append(attributes).append(", nestingChildren=").append(nestingChildren).append(", type=").append(type)
				.append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
