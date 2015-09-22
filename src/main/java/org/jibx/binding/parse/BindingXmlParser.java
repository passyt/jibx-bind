package org.jibx.binding.parse;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;

import org.jibx.binding.model.BindingDirection;
import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.IncludeElement;
import org.jibx.binding.model.attributes.NestingAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.Utility;

/**
 * 
 * @author Passyt
 *
 */
public class BindingXmlParser extends AbstractXmlParser {

	private final String name;
	private final String path;

	public BindingXmlParser(String path) throws Exception {
		super(new StAXReaderWrapper(XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream(path)), JibxUtils.getResourceName(path), false));
		this.name = JibxUtils.getResourceName(path);
		this.path = path;
	}

	public BindingXmlParser(InputStream input, String name) throws Exception {
		super(new StAXReaderWrapper(XMLInputFactory.newInstance().createXMLStreamReader(input), name, false));
		this.name = name;
		this.path = null;
	}

	public BindingElement parse() throws JiBXException {
		if (reader.getEventType() != IXMLReader.START_DOCUMENT) {
			throw new JiBXException("Expected a start of XML");
		}

		String start = toStart();
		if (!"binding".equals(start)) {
			throw new JiBXException("Expected start tag, " + "found end tag " + currentNameString() + " " + buildPositionString());
		}

		NestingAttributesParser parser = new NestingAttributesParser(new NestingAttributes());

		BindingElement binding = new BindingElement();
		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("name".equals(attributeName)) {
				binding.setName(attributeValue);
			} else if ("direction".equals(attributeName)) {
				binding.setDirection(BindingDirection.valueOf(JibxUtils.enumName(attributeValue)));
			} else if ("forwards".equals(attributeName)) {
				binding.setForwardReferences(Utility.deserializeBoolean(attributeValue));
			} else if ("package".equals(attributeName)) {
				binding.setTargetPackage(attributeValue);
			} else if ("track-source".equals(attributeName)) {
				binding.setTrackSource(Utility.deserializeBoolean(attributeValue));
			} else if ("force-classes".equals(attributeName)) {
				binding.setForceClasses(Utility.deserializeBoolean(attributeValue));
			} else if ("add-constructors".equals(attributeName)) {
				binding.setAddConstructors(Utility.deserializeBoolean(attributeValue));
			} else if ("trim-whitespace".equals(attributeName)) {
				binding.setTrimWhitespace(Utility.deserializeBoolean(attributeValue));
			} else if ("major-version".equals(attributeName)) {
			} else if ("minor-version".equals(attributeName)) {
			} else if (parser.allow(attributeName)) {
				parser.setValue(attributeName, attributeValue);
			} else {
				throw new JiBXException("Invalid attribute [" + attributeName + "] at " + buildPositionString());
			}
		}
		binding.setNestingAttributes(parser.getAttributes());
		if (binding.getName() == null || binding.getName().trim().length() == 0) {
			String bindingName = this.name;
			if (bindingName != null) {
				if (bindingName.contains(".")) {
					bindingName = bindingName.substring(0, bindingName.indexOf("."));
				}

				binding.setName(bindingName);
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

			if (IXMLReader.START_TAG == reader.getEventType()) {
				if ("namespace".equals(reader.getName())) {
					binding.addBindingChild(new NamespaceXmlParser(reader).parse());
				} else if ("include".equals(reader.getName())) {
					IncludeElement includeElement = new IncludeXmlParser(reader).parse();
					try {
						BindingElement bindingElement = new BindingXmlParser(JibxUtils.getInputStream(includeElement.getPath(), path), JibxUtils.getResourceName(includeElement
								.getPath())).parse();
						bindingElement.setPrecompiled(includeElement.isPrecompiled());
						includeElement.setBindingElement(bindingElement);
						binding.addBindingChild(includeElement);
					} catch (Exception e) {
						throw new JiBXException("Parse include xml failed on " + includeElement.getPath(), e);
					}
				} else if ("format".equals(reader.getName())) {
					binding.addBindingChild(new FormatXmlParser(reader).parse());
				} else if ("mapping".equals(reader.getName())) {
					binding.addBindingChild(new MappingXmlParser(reader).parse(binding.getNestingAttributes().getValueStyle()));
				} else {
					throw new JiBXException("Unsupported element " + reader.getName() + " at " + buildPositionString());
				}
			} else if (IXMLReader.END_TAG == reader.getEventType()) {
				break;
			} else if (IXMLReader.END_DOCUMENT == reader.getEventType()) {
				throw new JiBXException("Unexpected end of XML, found " + currentNameString() + " " + buildPositionString());
			}
		}
		return binding;
	}

	protected String toStart() throws JiBXException {
		if (reader.getEventType() == IXMLReader.START_TAG) {
			return reader.getName();
		}
		while (true) {
			reader.next();
			switch (reader.getEventType()) {

			case IXMLReader.START_TAG:
				return reader.getName();

			case IXMLReader.END_TAG:
				throw new JiBXException("Expected start tag, " + "found end tag " + currentNameString() + " " + buildPositionString());

			case IXMLReader.END_DOCUMENT:
				throw new JiBXException("Expected start tag, " + "found end of document " + buildPositionString());

			}
		}
	}

}
