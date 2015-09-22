package org.jibx.binding.parse;

import java.util.List;

import org.jibx.binding.model.IdentType;
import org.jibx.binding.model.MappingElement;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.model.ValueElement.Style;
import org.jibx.binding.model.ValueStyle;
import org.jibx.binding.model.attributes.AttributeBase;
import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.model.attributes.StringAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class ValueXmlParser extends AbstractXmlParser {

	protected ValueXmlParser(IXMLReader reader) {
		super(reader);
	}

	@SuppressWarnings("unchecked")
	public ValueElement parse(MappingElement mappingElement, ValueStyle defaultValueStyle) throws JiBXException {
		NameAttributesParser nameAttributesParser = new NameAttributesParser(new NameAttributes());
		PropertyAttributesParser propertyAttributesParser = new PropertyAttributesParser(new PropertyAttributes());
		StringAttributesParser stringAttributesParser = new StringAttributesParser(new StringAttributes());
		List<AbstractAttributesParser<? extends AttributeBase>> parsers = JibxUtils.newArrayList(nameAttributesParser, propertyAttributesParser, stringAttributesParser);

		ValueElement element = new ValueElement();
		if (ValueStyle.Attribute == defaultValueStyle) {
			element.setStyle(Style.Attribute);
		} else if (ValueStyle.Element == defaultValueStyle) {
			element.setStyle(Style.Element);
		}

		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("constant".equals(attributeName)) {
				element.setConstant(attributeValue);
			} else if ("ident".equals(attributeName)) {
				element.setIdent(IdentType.valueOf(JibxUtils.enumName(attributeValue)));
			} else if ("style".equals(attributeName)) {
				element.setStyle(Style.valueOf(JibxUtils.enumName(attributeValue)));
			} else {
				boolean found = false;
				for (AttributesParser<?> parser : parsers) {
					if (parser.allow(attributeName)) {
						found = true;
						parser.setValue(attributeName, attributeValue);
						break;
					}
				}

				if (!found) {
					throw new JiBXException("Invalid attribute [" + attributeName + "] at " + buildPositionString());
				}
			}
		}

		reader.next();
		if (IXMLReader.END_TAG != reader.getEventType()) {
			throw new JiBXException("Expected end tag, " + "found tag " + currentNameString() + " " + buildPositionString());
		}

		element.setNameAttributes(nameAttributesParser.getAttributes());
		element.setPropertyAttributes(propertyAttributesParser.getAttributes());
		element.setStringAttributes(stringAttributesParser.getAttributes());

		return element;
	}

}
