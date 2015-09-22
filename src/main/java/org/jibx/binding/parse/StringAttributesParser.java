package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.attributes.StringAttributes;
import org.jibx.binding.util.JibxUtils;

/**
 * 
 * @author Passyt
 *
 */
public class StringAttributesParser extends AbstractAttributesParser<StringAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("default", "deserializer", "enum-value-method", "serializer", "whitespace");

	public StringAttributesParser(StringAttributes t) {
		super(t);
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

	@Override
	public void setValue(String attributeName, String attributeValue) {
		if ("default".equals(attributeName)) {
			t.setDefaultText(attributeValue);
		} else if ("deserializer".equals(attributeName)) {
			t.setDeserializerName(attributeValue);
		} else if ("enum-value-method".equals(attributeName)) {
			t.setEnumValueMethodName(attributeValue);
		} else if ("serializer".equals(attributeName)) {
			t.setSerializerName(attributeValue);
		} else if ("whitespace".equals(attributeName)) {
			t.setWhitespaceName(attributeValue);
		}
	}

	@Override
	public StringAttributes getAttributes() {
		return t;
	}

}
