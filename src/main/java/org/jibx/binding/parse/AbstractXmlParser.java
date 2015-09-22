package org.jibx.binding.parse;

import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;

public class AbstractXmlParser {

	protected final IXMLReader reader;

	protected AbstractXmlParser(IXMLReader reader) {
		super();
		this.reader = reader;
	}

	protected String currentNameString() {
		return buildNameString(reader.getNamespace(), reader.getName());
	}

	protected static String buildNameString(String ns, String name) {
		if (ns == null || "".equals(ns)) {
			return "\"" + name + "\"";
		} else {
			return "\"{" + ns + "}" + name + "\"";
		}
	}

	protected String buildPositionString() {
		return reader.buildPositionString();
	}

	protected String accumulateText() throws JiBXException {
		String text = null;
		StringBuilder buff = null;
		loop: while (true) {
			switch (reader.getEventType()) {

			case IXMLReader.ENTITY_REF:
				if (reader.getText() == null) {
					throw new JiBXException("Unexpanded entity reference in text at " + buildPositionString());
				}
			case IXMLReader.CDSECT:
			case IXMLReader.TEXT:
				if (text == null) {
					text = reader.getText();
				} else {
					if (buff == null) {
						buff = new StringBuilder(text);
					}
					buff.append(reader.getText());
				}
				break;

			case IXMLReader.END_TAG:
			case IXMLReader.START_TAG:
			case IXMLReader.END_DOCUMENT:
				break loop;

			default:
				break;

			}
			reader.nextToken();
		}
		if (buff == null) {
			return (text == null) ? "" : text;
		} else {
			return buff.toString();
		}
	}

}
