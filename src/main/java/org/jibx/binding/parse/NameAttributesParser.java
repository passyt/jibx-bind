package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.util.JibxUtils;

/**
 * 
 * @author Passyt
 *
 */
public class NameAttributesParser extends AbstractAttributesParser<NameAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("name", "ns");

	public NameAttributesParser(NameAttributes t) {
		super(t);
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

	@Override
	public void setValue(String attributeName, String attributeValue) {
		if ("name".equals(attributeName)) {
			t.setName(attributeValue);
		} else if ("ns".equals(attributeName)) {
			t.setUri(attributeValue);
		}
	}

}
