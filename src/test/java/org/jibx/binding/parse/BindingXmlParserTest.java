package org.jibx.binding.parse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import junit.framework.Assert;

import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.MappingElement;
import org.jibx.binding.model.NamespaceElement;
import org.jibx.binding.model.NamespaceStyle;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.Usage;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.model.ValueElement.Style;
import org.jibx.binding.model.attributes.NameAttributes;
import org.jibx.binding.model.attributes.ObjectAttributes;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.model.attributes.StructureAttributes;
import org.jibx.binding.util.IOUtils;
import org.junit.Test;

/**
 * 
 * @author Passyt
 *
 */
public class BindingXmlParserTest {

	@Test
	public void test() throws Exception {
		NamespaceElement namespace1 = new NamespaceElement();
		namespace1.setUri("http://test/");
		namespace1.setDefaultStyle(NamespaceStyle.Elements);

		NamespaceElement namespace2 = new NamespaceElement();
		namespace2.setUri("http://www/");
		namespace2.setPrefix("b");

		ValueElement value11 = new ValueElement();
		value11.setNameAttributes(new NameAttributes("Version"));
		value11.setPropertyAttributes(new PropertyAttributes("version", Usage.Optional));
		value11.setStyle(Style.Element);

		MappingElement mapping1 = new MappingElement();
		mapping1.setAbstract(true);
		mapping1.setClassName("com.nuke.jibx.test.Mapping");
		mapping1.addNestingChild(value11);

		ValueElement value21 = new ValueElement();
		value21.setNameAttributes(new NameAttributes("Code"));
		value21.setPropertyAttributes(new PropertyAttributes("code", Usage.Required));
		value21.setStyle(Style.Element);

		ValueElement value22 = new ValueElement();
		value22.setNameAttributes(new NameAttributes("Name"));
		value22.setPropertyAttributes(new PropertyAttributes("name", Usage.Required));
		value22.setStyle(Style.Element);

		MappingElement mapping2 = new MappingElement();
		mapping2.setNameAttributes(new NameAttributes("Item", null));
		mapping2.setClassName("com.nuke.jibx.test.Item");
		mapping2.addNestingChild(value21);
		mapping2.addNestingChild(value22);

		NamespaceElement namespace31 = new NamespaceElement();
		namespace31.setUri("http://test.1/");
		namespace31.setPrefix("a");

		StructureElement structure32 = new StructureElement();
		structure32.setMapAsName("com.nuke.jibx.test.Mapping");

		ValueElement value33 = new ValueElement();
		value33.setNameAttributes(new NameAttributes("Name"));
		value33.setPropertyAttributes(new PropertyAttributes("name", Usage.Optional));
		value33.setStyle(Style.Attribute);

		ValueElement value341 = new ValueElement();
		value341.setNameAttributes(new NameAttributes("Age"));
		value341.setPropertyAttributes(new PropertyAttributes("age", Usage.Optional));
		value341.setStyle(Style.Element);

		ValueElement value342 = new ValueElement();
		value342.setNameAttributes(new NameAttributes("Value"));
		value342.setPropertyAttributes(new PropertyAttributes("value", Usage.Optional));
		value342.setStyle(Style.Element);

		StructureElement structure34 = new StructureElement();
		structure34.setNameAttributes(new NameAttributes("Code"));
		structure34.setStructureAttributes(new StructureAttributes(false));
		structure34.addNestingChild(value341);
		structure34.addNestingChild(value342);

		ValueElement value3511 = new ValueElement();
		value3511.setNameAttributes(new NameAttributes("Code"));
		value3511.setPropertyAttributes(new PropertyAttributes("java.lang.String"));
		value3511.setStyle(Style.Element);

		StructureElement structure351 = new StructureElement();
		structure351.setNameAttributes(new NameAttributes("Test"));
		structure351.addNestingChild(value3511);

		CollectionElement collection35 = new CollectionElement();
		collection35.setNameAttributes(new NameAttributes("List1"));
		collection35.setPropertyAttributes(new PropertyAttributes("list1", Usage.Optional));
		collection35.setObjectAttributes(new ObjectAttributes(null, "org.jibx.runtime.Utility.arrayListFactory"));
		collection35.addNestingChild(structure351);

		ValueElement value3611 = new ValueElement();
		value3611.setNameAttributes(new NameAttributes("Code"));
		value3611.setPropertyAttributes(new PropertyAttributes("java.lang.String"));
		value3611.setStyle(Style.Attribute);

		StructureElement structure361 = new StructureElement();
		structure361.setNameAttributes(new NameAttributes("Test"));
		structure361.addNestingChild(value3611);

		CollectionElement collection36 = new CollectionElement();
		collection36.setNameAttributes(new NameAttributes("List2"));
		collection36.setPropertyAttributes(new PropertyAttributes("list2", Usage.Optional));
		collection36.setObjectAttributes(new ObjectAttributes(null, "org.jibx.runtime.Utility.arrayListFactory"));
		collection36.addNestingChild(structure361);

		ValueElement value3711 = new ValueElement();
		value3711.setNameAttributes(new NameAttributes("Code1"));
		value3711.setPropertyAttributes(new PropertyAttributes("code", Usage.Required));
		value3711.setStyle(Style.Element);

		ValueElement value3712 = new ValueElement();
		value3712.setNameAttributes(new NameAttributes("Name1"));
		value3712.setPropertyAttributes(new PropertyAttributes("name", Usage.Required));
		value3712.setStyle(Style.Element);

		StructureElement collection371 = new StructureElement();
		collection371.setNameAttributes(new NameAttributes("Item"));
		collection371.addNestingChild(value3711);
		collection371.addNestingChild(value3712);

		CollectionElement collection37 = new CollectionElement();
		collection37.setNameAttributes(new NameAttributes("List3"));
		collection37.setPropertyAttributes(new PropertyAttributes("list3", Usage.Optional));
		collection37.setObjectAttributes(new ObjectAttributes(null, "org.jibx.runtime.Utility.arrayListFactory"));
		collection37.setItemTypeName("com.nuke.jibx.test.Item");
		collection37.addNestingChild(collection371);

		MappingElement mapping3 = new MappingElement();
		mapping3.setNameAttributes(new NameAttributes("Test", "http://test.1/"));
		mapping3.setClassName("com.nuke.jibx.test.TestMapping");
		mapping3.addTemplateChild(namespace31);
		mapping3.addNestingChild(structure32);
		mapping3.addNestingChild(value33);
		mapping3.addNestingChild(structure34);
		mapping3.addNestingChild(collection35);
		mapping3.addNestingChild(collection36);
		mapping3.addNestingChild(collection37);

		BindingElement expected = new BindingElement();
		expected.setName("message");
		expected.addBindingChild(namespace1);
		expected.addBindingChild(namespace2);
		expected.addBindingChild(mapping1);
		expected.addBindingChild(mapping2);
		expected.addBindingChild(mapping3);

		BindingElement actual = new BindingXmlParser(BindingXmlParserTest.class.getResourceAsStream("message.1.xml"), "message.1.xml").parse();
		System.out.println(String.format("expected: %s", expected));
		System.out.println(String.format("actual  : %s", actual));
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testNormal() throws Exception {
		new BindingXmlParser(BindingXmlParserTest.class.getResourceAsStream("binding-normal.xml"), "binding-normal.xml").parse();
	}

	@Test
	public void testPrecomp() throws Exception {
		String path = System.getProperty("java.io.tmpdir");
		OutputStreamWriter out1 = new OutputStreamWriter(new FileOutputStream(path + File.separator + "binding-precomp.xml"));
		OutputStreamWriter out2 = new OutputStreamWriter(new FileOutputStream(path + File.separator + "binding-shared.xml"));
		try {
			out1.write(IOUtils.toString(BindingXmlParserTest.class.getResourceAsStream("binding-precomp.xml")));
			out2.write(IOUtils.toString(BindingXmlParserTest.class.getResourceAsStream("binding-shared.xml")));
		} finally {
			out1.close();
			out2.close();
		}

		new BindingXmlParser(path + File.separator + "binding-precomp.xml").parse();
	}

	@Test
	public void testShared() throws Exception {
		new BindingXmlParser(BindingXmlParserTest.class.getResourceAsStream("binding-shared.xml"), "binding-shared.xml").parse();
	}

	public void verify2() throws Exception {
		new BindingXmlParser(BindingXmlParserTest.class.getResourceAsStream("message.2.xml"), "message.2.xml").parse();
	}

}
