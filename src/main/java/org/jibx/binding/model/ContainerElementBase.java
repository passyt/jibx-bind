package org.jibx.binding.model;

import org.jibx.binding.model.attributes.ObjectAttributes;
import org.jibx.binding.model.attributes.StructureAttributes;

/**
 * 
 * @author Passyt
 *
 */
public class ContainerElementBase extends NestingElementBase {

	protected ObjectAttributes objectAttributes = new ObjectAttributes();
	protected StructureAttributes structureAttributes = new StructureAttributes();

	protected ContainerElementBase(ElementType type) {
		super(type);
	}

	public ObjectAttributes getObjectAttributes() {
		return objectAttributes;
	}

	public void setObjectAttributes(ObjectAttributes objectAttributes) {
		this.objectAttributes = objectAttributes;
	}

	public StructureAttributes getStructureAttributes() {
		return structureAttributes;
	}

	public void setStructureAttributes(StructureAttributes structureAttributes) {
		this.structureAttributes = structureAttributes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((objectAttributes == null) ? 0 : objectAttributes.hashCode());
		result = prime * result + ((structureAttributes == null) ? 0 : structureAttributes.hashCode());
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
		ContainerElementBase other = (ContainerElementBase) obj;
		if (objectAttributes == null) {
			if (other.objectAttributes != null)
				return false;
		} else if (!objectAttributes.equals(other.objectAttributes))
			return false;
		if (structureAttributes == null) {
			if (other.structureAttributes != null)
				return false;
		} else if (!structureAttributes.equals(other.structureAttributes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ContainerElementBase [objectAttributes=").append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=")
				.append(attributes).append(", nestingChildren=").append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
