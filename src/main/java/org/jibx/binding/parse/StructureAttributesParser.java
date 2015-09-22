package org.jibx.binding.parse;

import java.util.Set;

import org.jibx.binding.model.attributes.StructureAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.Utility;

/**
 * 
 * @author Passyt
 *
 */
public class StructureAttributesParser extends AbstractAttributesParser<StructureAttributes> {

	private static Set<String> ALLOWED_ATTRIBUTES = JibxUtils.newHashSet("allow-repeats", "choice", "flexible", "label", "ordered", "using");

	public StructureAttributesParser(StructureAttributes t) {
		super(t);
	}

	@Override
	protected Set<String> getAllowAttributes() {
		return ALLOWED_ATTRIBUTES;
	}

	@Override
	public void setValue(String attributeName, String attributeValue) throws JiBXException {
		if ("allow-repeats".equals(attributeName)) {
			t.setAllowRepeats(Utility.deserializeBoolean(attributeValue));
		} else if ("choice".equals(attributeName)) {
			t.setChoice(Utility.deserializeBoolean(attributeValue));
		} else if ("flexible".equals(attributeName)) {
			t.setFlexible(Utility.deserializeBoolean(attributeValue));
		} else if ("label".equals(attributeName)) {
			t.setLabelName(attributeValue);
		} else if ("ordered".equals(attributeName)) {
			t.setOrdered(Utility.deserializeBoolean(attributeValue));
		} else if ("using".equals(attributeName)) {
			t.setUsingName(attributeValue);
		}
	}
}
