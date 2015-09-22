package org.jibx.binding.parse;

import org.jibx.binding.model.IncludeElement;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.Utility;

/**
 * 
 * @author Passyt
 *
 */
public class IncludeXmlParser extends AbstractXmlParser {

	protected IncludeXmlParser(IXMLReader reader) {
		super(reader);
	}

	public IncludeElement parse() throws JiBXException {
		IncludeElement element = new IncludeElement();
		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("path".equals(attributeName)) {
				element.setPath(attributeValue);
			} else if ("precompiled".equals(attributeName)) {
				element.setPrecompiled(Utility.deserializeBoolean(attributeValue));
			} else {
				throw new JiBXException("Invalid attribute [" + attributeName + "] at " + buildPositionString());
			}
		}
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