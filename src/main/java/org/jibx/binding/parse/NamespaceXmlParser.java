package org.jibx.binding.parse;

import org.jibx.binding.model.NamespaceElement;
import org.jibx.binding.model.NamespaceStyle;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class NamespaceXmlParser extends AbstractXmlParser {

	public NamespaceXmlParser(IXMLReader reader) {
		super(reader);
	}

	public NamespaceElement parse() throws JiBXException {
		NamespaceElement element = new NamespaceElement();
		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("uri".equals(attributeName)) {
				element.setUri(attributeValue);
			} else if ("default".equals(attributeName)) {
				element.setDefaultStyle(NamespaceStyle.valueOf(JibxUtils.enumName(attributeValue)));
			} else if ("prefix".equals(attributeName)) {
				element.setPrefix(attributeValue);
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
