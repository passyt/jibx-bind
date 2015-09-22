package org.jibx.binding;

import org.jibx.binding.model.MappingElementBase;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public interface IMappingBuilder {

	/**
	 * 
	 * @param mappingElement
	 * @param context
	 */
	void build(MappingElementBase mappingElement, IBindingContext context) throws JiBXException;

}
