package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.attributes.ObjectAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.Utility;

/**
 * 
 * @author Passyt
 *
 */
public class ObjectAttributesParser extends AbstractAttributesParser<ObjectAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("create-type", "factory", "marshaller", "nillable", "post-set", "pre-get", "pre-set", "unmarshaller");

	public ObjectAttributesParser(ObjectAttributes t) {
		super(t);
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

	@Override
	public void setValue(String attributeName, String attributeValue) throws JiBXException {
		if ("create-type".equals(attributeName)) {
			t.setCreateType(attributeValue);
		} else if ("factory".equals(attributeName)) {
			t.setFactoryName(attributeValue);
		} else if ("marshaller".equals(attributeName)) {
			t.setMarshallerName(attributeValue);
		} else if ("nillable".equals(attributeName)) {
			t.setNillable(Utility.deserializeBoolean(attributeValue));
		} else if ("post-set".equals(attributeName)) {
			t.setPostSetName(attributeValue);
		} else if ("pre-get".equals(attributeName)) {
			t.setPreGetName(attributeValue);
		} else if ("pre-set".equals(attributeName)) {
			t.setPreSetName(attributeValue);
		} else if ("unmarshaller".equals(attributeName)) {
			t.setUnmarshallerName(attributeValue);
		}
	}

}
