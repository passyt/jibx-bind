package org.jibx.binding.model;

import java.util.List;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.attributes.NameAttributes;

/**
 * 
 * @author Passyt
 *
 */
public interface NestingChild {

	List<String> getAllNamespaces(IBindingContext context);

	String getNamespace(IBindingContext context);

	NameAttributes getNameAttributes();

}
