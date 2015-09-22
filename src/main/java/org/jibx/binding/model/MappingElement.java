package org.jibx.binding.model;

/**
 * 
 * @author Passyt
 *
 */
public class MappingElement extends MappingElementBase {

	public MappingElement() {
		super(ElementType.Mapping);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MappingElement [className=").append(className).append(", templateChildren=").append(templateChildren).append(", objectAttributes=")
				.append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=").append(attributes).append(", nestingChildren=")
				.append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
