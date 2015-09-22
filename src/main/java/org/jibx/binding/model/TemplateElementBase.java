package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.classes.javassist.builder.NamespaceDefinition;
import org.jibx.binding.util.Lists;

/**
 * 
 * @author Passyt
 *
 */
public abstract class TemplateElementBase extends ContainerElementBase {

	/**
	 * This required attribute gives the fully qualified class name for the
	 * object type handled by this mapping.
	 */
	protected String className;
	protected List<TemplateChild> templateChildren = new ArrayList<TemplateChild>();

	public TemplateElementBase(ElementType type) {
		super(type);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void addTemplateChild(TemplateChild element) {
		this.templateChildren.add(element);
	}

	public Iterator<TemplateChild> templateChildIterator() {
		return this.templateChildren.iterator();
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
		set.addAll(context.getBindingRoot().getAllNamspaces(false));
		for (Iterator<TemplateChild> it = templateChildIterator(); it.hasNext();) {
			TemplateChild child = it.next();
			if (child instanceof NamespaceElement) {
				NamespaceElement element = (NamespaceElement) child;
				set.add(element.getUri());
			}
		}

		for (Iterator<NestingChild> it = nestingChildIterator(); it.hasNext();) {
			NestingChild child = it.next();
			set.addAll(child.getAllNamespaces(context));
		}
		return Lists.newArrayList(set);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((templateChildren == null) ? 0 : templateChildren.hashCode());
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
		TemplateElementBase other = (TemplateElementBase) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (templateChildren == null) {
			if (other.templateChildren != null)
				return false;
		} else if (!templateChildren.equals(other.templateChildren))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TemplateElementBase [className=").append(className).append(", templateChildren=").append(templateChildren).append(", objectAttributes=")
				.append(objectAttributes).append(", structureAttributes=").append(structureAttributes).append(", attributes=").append(attributes).append(", nestingChildren=")
				.append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
