package org.jibx.binding.parse;

import java.util.Set;

/**
 * 
 * @author Passyt
 *
 * @param <T>
 */
public abstract class AbstractAttributesParser<T> implements AttributesParser<T> {

	protected T t;

	public AbstractAttributesParser(T t) {
		this.t = t;
	}

	@Override
	public boolean allow(String attributeName) {
		return getAllowAttributes().contains(attributeName);
	}

	@Override
	public T getAttributes() {
		return t;
	}

	protected abstract Set<String> getAllowAttributes();

}
