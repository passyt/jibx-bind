package org.jibx.binding.parse;

import java.util.List;

import org.jibx.binding.model.MappingElement;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.ValueStyle;
import org.jibx.binding.model.attributes.AttributeBase;
import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.model.attributes.NestingAttributes;
import org.jibx.binding.model.attributes.ObjectAttributes;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.model.attributes.StructureAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.runtime.IXMLReader;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class StructureXmlParser extends AbstractXmlParser {

	protected StructureXmlParser(IXMLReader reader) {
		super(reader);
	}

	@SuppressWarnings("unchecked")
	public StructureElement parse(MappingElement mappingElement, ValueStyle defaultValueStyle) throws JiBXException {
		NameAttributesParser nameAttributesParser = new NameAttributesParser(new NameAttributes());
		PropertyAttributesParser propertyAttributesParser = new PropertyAttributesParser(new PropertyAttributes());
		ObjectAttributesParser objectAttributesParser = new ObjectAttributesParser(new ObjectAttributes());
		StructureAttributesParser structureAttributesParser = new StructureAttributesParser(new StructureAttributes());
		NestingAttributesParser nestingAttributesParser = new NestingAttributesParser(new NestingAttributes());
		List<AbstractAttributesParser<? extends AttributeBase>> parsers = JibxUtils.newArrayList(nameAttributesParser, propertyAttributesParser, objectAttributesParser,
				structureAttributesParser, nestingAttributesParser);

		StructureElement element = new StructureElement();
		element.setTemplate(mappingElement);
		int attributeCount = reader.getAttributeCount();
		for (int i = 0; i < attributeCount; i++) {
			String attributeName = reader.getAttributeName(i);
			String attributeValue = reader.getAttributeValue(i);
			if ("map-as".equals(attributeName)) {
				element.setMapAsName(attributeValue);
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

		element.setNameAttributes(nameAttributesParser.getAttributes());
		element.setPropertyAttributes(propertyAttributesParser.getAttributes());
		element.setObjectAttributes(objectAttributesParser.getAttributes());
		element.setStructureAttributes(structureAttributesParser.getAttributes());
		element.setNestingAttributes(nestingAttributesParser.getAttributes());

		if (nestingAttributesParser.isSetDefaultValueStyle()) {
			defaultValueStyle = element.getNestingAttributes().getValueStyle();
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
				if ("collection".equals(reader.getName())) {
					element.addNestingChild(new CollectionXmlParser(reader).parse(mappingElement, defaultValueStyle));
				} else if ("structure".equals(reader.getName())) {
					element.addNestingChild(new StructureXmlParser(reader).parse(mappingElement, defaultValueStyle));
				} else if ("value".equals(reader.getName())) {
					element.addNestingChild(new ValueXmlParser(reader).parse(mappingElement, defaultValueStyle));
				} else {
					throw new JiBXException("Unsupported element " + reader.getName() + " at " + buildPositionString());
				}
			} else if (IXMLReader.END_TAG == reader.getEventType()) {
				break;
			} else if (IXMLReader.END_DOCUMENT == reader.getEventType()) {
				throw new JiBXException("Unexpected end of XML, found " + currentNameString() + " " + buildPositionString());
			}
		}
		return element;
	}

}
