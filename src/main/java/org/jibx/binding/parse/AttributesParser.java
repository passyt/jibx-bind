package org.jibx.binding.parse;

import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 * @param <T>
 */
public interface AttributesParser<T> {

	boolean allow(String attributeName);

	void setValue(String attributeName, String attributeValue) throws JiBXException;

	T getAttributes();

}
