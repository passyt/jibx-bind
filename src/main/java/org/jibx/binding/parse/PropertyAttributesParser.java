package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.Usage;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class PropertyAttributesParser extends AbstractAttributesParser<PropertyAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("field", "flag-method", "get-method", "set-method", "test-method", "type", "usage");

	public PropertyAttributesParser(PropertyAttributes t) {
		super(t);
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

	@Override
	public void setValue(String attributeName, String attributeValue) throws JiBXException {
		if ("field".equals(attributeName)) {
			t.setFieldName(attributeValue);
		} else if ("flag-method".equals(attributeName)) {
			t.setFlagMethodName(attributeValue);
		} else if ("get-method".equals(attributeName)) {
			t.setGetMethodName(attributeValue);
		} else if ("set-method".equals(attributeName)) {
			t.setSetMethodName(attributeValue);
		} else if ("test-method".equals(attributeName)) {
			t.setTestMethodName(attributeValue);
		} else if ("type".equals(attributeName)) {
			t.setType(attributeValue);
		} else if ("usage".equals(attributeName)) {
			t.setUsage(Usage.valueOf(JibxUtils.enumName(attributeValue)));
		}
	}

}
