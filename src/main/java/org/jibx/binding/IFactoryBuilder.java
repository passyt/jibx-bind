package org.jibx.binding;

import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public interface IFactoryBuilder {

	/**
	 * 
	 * @param context
	 */
	void build(IBindingContext context) throws JiBXException;

}
