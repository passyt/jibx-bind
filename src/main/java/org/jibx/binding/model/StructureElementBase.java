package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.classes.javassist.builder.NamespaceDefinition;
import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.util.Lists;

/**
 * 
 * @author Passyt
 *
 */
public abstract class StructureElementBase extends ContainerElementBase {

	protected PropertyAttributes propertyAttributes = new PropertyAttributes();
	protected NameAttributes nameAttributes = new NameAttributes();

	protected StructureElementBase(ElementType type) {
		super(type);
	}

	public PropertyAttributes getPropertyAttributes() {
		return propertyAttributes;
	}

	public void setPropertyAttributes(PropertyAttributes propertyAttributes) {
		this.propertyAttributes = propertyAttributes;
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

	public List<Integer> getAllNamespaceIndexes(IBindingContext context) {
		List<String> namespaces = getAllNamespaces(context);
		List<Integer> indexes = new ArrayList<Integer>();
		Map<String, NamespaceDefinition> namespaceDefinitions = context.getNamespaces();
		for (String each : namespaces) {
			indexes.add(namespaceDefinitions.get(each).getIndex());
		}
		return indexes;
	}

	public List<String> getAllNamespacePrefixes(IBindingContext context) {
		List<String> namespaces = getAllNamespaces(context);
		List<String> prefixes = new ArrayList<String>();
		Map<String, NamespaceDefinition> namespaceDefinitions = context.getNamespaces();
		for (String each : namespaces) {
			String prefix = namespaceDefinitions.get(each).getPrefix();
			if (prefix == null) {
				prefixes.add("");
			} else {
				prefixes.add(prefix);
			}
		}
		return prefixes;
	}

	protected List<String> getAllNamespaces(IBindingContext context) {
		Set<String> set = new LinkedHashSet<String>();
		set.add("");
		if (this.nameAttributes.getUri() != null) {
			set.add(this.nameAttributes.getUri());
		}

		for (Iterator<NestingChild> it = nestingChildIterator(); it.hasNext();) {
			NestingChild child = it.next();
			set.addAll(child.getAllNamespaces(context));
		}
		return Lists.newArrayList(set);
	}

	public String getNamespace(IBindingContext context) {
		if (nameAttributes.getUri() != null) {
			return nameAttributes.getUri();
		}

		return context.currentDefaultElementNamespace();
	}

	public int getNamespaceIndex(IBindingContext context) {
		String namespace = getNamespace(context);
		if (namespace == null) {
			return 0;
		}

		return context.getNamespaces().get(namespace).getIndex();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((nameAttributes == null) ? 0 : nameAttributes.hashCode());
		result = prime * result + ((propertyAttributes == null) ? 0 : propertyAttributes.hashCode());
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
		StructureElementBase other = (StructureElementBase) obj;
		if (nameAttributes == null) {
			if (other.nameAttributes != null)
				return false;
		} else if (!nameAttributes.equals(other.nameAttributes))
			return false;
		if (propertyAttributes == null) {
			if (other.propertyAttributes != null)
				return false;
		} else if (!propertyAttributes.equals(other.propertyAttributes))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StructureElementBase [propertyAttributes=").append(propertyAttributes).append(", nameAttributes=").append(nameAttributes).append(", objectAttributes=")
				.append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=").append(attributes).append(", nestingChildren=")
				.append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
