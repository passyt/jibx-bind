package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.ValueStyle;
import org.jibx.binding.model.attributes.NestingAttributes;
import org.jibx.binding.util.JibxUtils;

/**
 * 
 * @author Passyt
 *
 */
public class NestingAttributesParser extends AbstractAttributesParser<NestingAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("value-style");
	private boolean isSetDefaultValueStyle = false;

	public NestingAttributesParser(NestingAttributes t) {
		super(t);
	}

	@Override
	public void setValue(String attributeName, String attributeValue) {
		if ("value-style".equals(attributeName)) {
			isSetDefaultValueStyle = true;
			t.setValueStyle(ValueStyle.valueOf(JibxUtils.enumName(attributeValue)));
		}
	}

	public boolean isSetDefaultValueStyle() {
		return isSetDefaultValueStyle;
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

}
