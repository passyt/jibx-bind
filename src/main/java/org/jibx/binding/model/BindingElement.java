/*
Copyright (c) 2004-2005, Dennis M. Sosnoski
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.
 * Neither the name of JiBX nor the names of its contributors may be used
   to endorse or promote products derived from this software without specific
   prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.jibx.binding.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jibx.binding.JibxConst;

/**
 * The binding element is the root of a binding definition. This defines
 * characteristics for the entire binding. It supports several unique attributes
 * as well as two common attribute groups:
 * 
 * @author Passyt
 *
 */
public class BindingElement extends NestingElementBase {

	/**
	 * When this optional attribute is present with value "true", the binding
	 * compiler will add a default constructor to a bound class when necessary
	 * to make it usable in the binding. Default constructors are needed when
	 * there's no unmarshaller or factory method for an object reference.
	 * Constructors can only be added to classes which are available as class
	 * files for modification during the binding compile.
	 */
	private boolean addConstructors;
	/**
	 * Binding direction, which must be "input" (unmarshalling only), "output"
	 * (marshalling only), or "both" (both marshalling and unmarshalling, the
	 * default if this attribute is not given).
	 */
	private BindingDirection direction = BindingDirection.Both;
	/**
	 * When this optional attribute is present with value "true", it forces
	 * generation of marshaller/unmarshaller classes for top-level abstract
	 * mappings which are not extended by other mappings. Normally these classes
	 * would not be generated, since such mappings are never used directly
	 * within the binding. The classes can be used at runtime by custom code,
	 * though, as the equivalent of type mappings. The default value is "false".
	 */
	private boolean forceClasses;
	/**
	 * Controls whether forward references to ids are supported when
	 * unmarshalling, at the cost of some (minor) additional overhead and code
	 * size. If supplied the value must be "true" (forwards supported) or
	 * "false" (ids must be defined before they are referenced). The default is
	 * "true".
	 */
	private boolean forwardReferences = true;
	/**
	 * The name for this binding. The supplied name must consist only of
	 * characters which are valid as part of a Java identifier (so no spaces,
	 * periods, etc.). The default if this attribute is not used is to take the
	 * name of the file containing the definition document, with any file
	 * extension suffix stripped off and invalid characters replaced by
	 * underscores (so a binding definition in a file named "binding-2.xml" has
	 * the default name "binding_2").
	 */
	private String name;
	/**
	 * Java package used for created binding factory class. By default this is
	 * the same package as the class associated with the first mapping child
	 * element. If present, the value must be a Java package name (as in
	 * "org.jibx.runtime")
	 */
	private String targetPackage;
	/**
	 * When this optional attribute is present with value "true", the binding
	 * compiler adds code to each bound object class to implement the
	 * org.jibx.runtime.ITrackSource interface and store source position
	 * information when instance objects are unmarshalled. This interface lets
	 * you retrieve information about the source document and specific line and
	 * column location of the document component associated with that object.
	 * The default value is "false".
	 */
	private boolean trackSource;
	/**
	 * When this optional attribute is present with value "true", whitespace is
	 * trimmed from all simple text values before the value is deserialized. The
	 * effect is the same as if all the built-in conversions were defined using
	 * whitespace="trim" (see the string attribute group). This makes the
	 * conversions compatible with schema types.
	 */
	private boolean trimWhitespace;

	/**
	 * 
	 */
	private List<BindingChild> bindingChildren = new ArrayList<BindingChild>();

	public BindingElement() {
		super(ElementType.Binding);
	}

	public boolean isAddConstructors() {
		return addConstructors;
	}

	public void setAddConstructors(boolean addConstructors) {
		this.addConstructors = addConstructors;
	}

	public BindingDirection getDirection() {
		return direction;
	}

	public void setDirection(BindingDirection direction) {
		this.direction = direction;
	}

	public boolean isForceClasses() {
		return forceClasses;
	}

	public void setForceClasses(boolean forceClasses) {
		this.forceClasses = forceClasses;
	}

	public boolean isForwardReferences() {
		return forwardReferences;
	}

	public void setForwardReferences(boolean forwardReferences) {
		this.forwardReferences = forwardReferences;
	}

	public String getName() {
		return this.name == null ? "" : this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTargetPackage() {
		return targetPackage;
	}

	public void setTargetPackage(String targetPackage) {
		this.targetPackage = targetPackage;
	}

	public boolean isTrackSource() {
		return trackSource;
	}

	public void setTrackSource(boolean trackSource) {
		this.trackSource = trackSource;
	}

	public boolean isTrimWhitespace() {
		return trimWhitespace;
	}

	public void setTrimWhitespace(boolean trimWhitespace) {
		this.trimWhitespace = trimWhitespace;
	}

	public void addBindingChild(BindingChild item) {
		this.bindingChildren.add(item);
	}

	public Iterator<BindingChild> bindingChildIterator(boolean includePrecompiled) {
		return bindingChildren(this.bindingChildren, includePrecompiled).iterator();
	}

	public static List<BindingChild> bindingChildren(List<BindingChild> bindingChildren, boolean includePrecompiled) {
		if (bindingChildren == null) {
			return new ArrayList<BindingChild>();
		}

		List<BindingChild> result = new ArrayList<BindingChild>();
		for (BindingChild each : bindingChildren) {
			if (each instanceof IncludeElement) {
				IncludeElement includeElement = (IncludeElement) each;
				if (includeElement.getBindingElement() != null) {
					if (includePrecompiled || !includeElement.isPrecompiled()) {
						result.addAll(bindingChildren(includeElement.getBindingElement().bindingChildren, includePrecompiled));
					}
				}
			}
		}

		for (BindingChild each : bindingChildren) {
			if (!(each instanceof IncludeElement)) {
				result.add(each);
			}
		}

		return result;
	}

	public String getFactoryClassName(BindingElement bindingElement) {
		String prefix = JibxConst.GENERATE_PREFIX + getName();
		String packagePrefix = getFacotryPackage(bindingElement);
		if (packagePrefix.length() > 0) {
			packagePrefix = packagePrefix + ".";
		}
		return packagePrefix + prefix + JibxConst.BINDINGFACTORY_SUFFIX;
	}

	public String getFacotryPackage(BindingElement bindingElement) {
		String pakcage = "";
		if (bindingElement.targetPackage != null && bindingElement.targetPackage.trim().length() > 0) {
			pakcage = bindingElement.targetPackage;
		} else {
			for (BindingChild child : bindingElement.bindingChildren) {
				if (child instanceof MappingElementBase) {
					MappingElementBase firstMapping = (MappingElementBase) child;
					String className = firstMapping.getClassName();
					if (className.indexOf(".") > 0) {
						pakcage = className.substring(0, className.lastIndexOf("."));
					}
					break;
				}
			}
		}

		return pakcage;
	}

	public List<String> getAllNamspaces(boolean includePrecompiled) {
		List<String> result = new ArrayList<String>();
		for (Iterator<BindingChild> it = bindingChildIterator(includePrecompiled); it.hasNext();) {
			BindingChild child = it.next();
			if (child instanceof NamespaceElement) {
				result.add(((NamespaceElement) child).getUri());
			}
		}
		return result;
	}

	public List<FormatElement> getFormats() {
		return getFormats(bindingChildren);
	}

	protected static List<FormatElement> getFormats(List<BindingChild> bindingChildren) {
		List<FormatElement> formats = new ArrayList<FormatElement>();
		for (BindingChild child : bindingChildren) {
			if (child instanceof FormatElement) {
				formats.add((FormatElement) child);
			} else if (child instanceof IncludeElement) {
				IncludeElement includeElement = (IncludeElement) child;
				if (!includeElement.isPrecompiled()) {
					formats.addAll(getFormats(includeElement.getBindingElement().bindingChildren));
				}
			}
		}
		return formats;
	}

	public void setPrecompiled(boolean precompiled) {
		for (BindingChild child : bindingChildren) {
			if (child instanceof MappingElementBase) {
				MappingElementBase mappingElementBase = (MappingElementBase) child;
				mappingElementBase.setPrecompiled(precompiled);
				if (precompiled) {
					mappingElementBase.setPrecompiledBindingElement(this);
				}
			}
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (addConstructors ? 1231 : 1237);
		result = prime * result + ((bindingChildren == null) ? 0 : bindingChildren.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + (forceClasses ? 1231 : 1237);
		result = prime * result + (forwardReferences ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((targetPackage == null) ? 0 : targetPackage.hashCode());
		result = prime * result + (trackSource ? 1231 : 1237);
		result = prime * result + (trimWhitespace ? 1231 : 1237);
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
		BindingElement other = (BindingElement) obj;
		if (addConstructors != other.addConstructors)
			return false;
		if (bindingChildren == null) {
			if (other.bindingChildren != null)
				return false;
		} else if (!bindingChildren.equals(other.bindingChildren))
			return false;
		if (direction != other.direction)
			return false;
		if (forceClasses != other.forceClasses)
			return false;
		if (forwardReferences != other.forwardReferences)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (targetPackage == null) {
			if (other.targetPackage != null)
				return false;
		} else if (!targetPackage.equals(other.targetPackage))
			return false;
		if (trackSource != other.trackSource)
			return false;
		if (trimWhitespace != other.trimWhitespace)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BindingElement [addConstructors=").append(addConstructors).append(", direction=").append(direction).append(", forceClasses=").append(forceClasses)
				.append(", forwardReferences=").append(forwardReferences).append(", name=").append(name).append(", targetPackage=").append(targetPackage).append(", trackSource=")
				.append(trackSource).append(", trimWhitespace=").append(trimWhitespace).append(", bindingChildren=").append(bindingChildren).append(", attributes=")
				.append(attributes).append(", nestingChildren=").append(nestingChildren).append(", type=").append(type).append(", comment=").append(comment).append("]");
		return builder.toString();
	}

}
