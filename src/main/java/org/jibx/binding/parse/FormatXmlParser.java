package org.jibx.binding.parse;

import org.jibx.binding.model.FormatElement;
import org.jibx.binding.model.attributes.StringAttributes;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class FormatXmlParser extends AbstractXmlParser {

	protected FormatXmlParser(IXMLReader reader) {
		super(reader);
	}

	public FormatElement parse() throws JiBXException {
		StringAttributesParser parser = new StringAttributesParser(new StringAttributes());

		FormatElement element = new FormatElement();
		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("label".equals(attributeName)) {
				element.setLabel(attributeValue);
			} else if ("type".equals(attributeName)) {
				element.setType(attributeValue);
			} else if (parser.allow(attributeName)) {
				parser.setValue(attributeName, attributeValue);
			} else {
				throw new JiBXException("Invalid attribute [" + attributeName + "] at " + buildPositionString());
			}
		}
		element.setAttributes(parser.getAttributes());
		while (true) {
			reader.next();
			if (IXMLReader.TEXT == reader.getEventType()) {
				String text = reader.getText().trim();
				if (text.length() == 0) {
					continue;
				}

				throw new JiBXException("Invalid text [" + text + "] at " + buildPositionString());
			}

			if (IXMLReader.COMMENT == reader.getEventType()) {
				continue;
			}

			if (IXMLReader.END_TAG == reader.getEventType()) {
				break;
			} else {
				throw new JiBXException("Expected end tag, " + "found tag " + currentNameString() + " " + buildPositionString());
			}
		}
		return element;
	}
}
