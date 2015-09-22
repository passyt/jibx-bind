package org.jibx.binding.test9;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.jibx.binding.Compile;
import org.jibx.binding.util.IOUtils;
import org.jibx.binding.util.Lists;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.junit.Test;

/**
 * 
 * @author Passyt
 *
 */
public class Case9Test {

	@Test
	public void testAutoBind() throws Exception {
		autoBind();
		executeTest();
	}

	protected void executeTest() throws Exception {
		TestMapping mapping = new TestMapping();
		mapping.setName("name");
		mapping.setValue("value");
		mapping.setVersion("version");
		mapping.setAge("8");
		mapping.setList1(Lists.newArrayList("1", "2"));
		mapping.setList2(Lists.newArrayList("2", "3"));
		mapping.setList3(Lists.newArrayList(new Item("code1", "name1"), new Item("code2", "name2")));

		IBindingFactory factory = BindingDirectory.getFactory("message", TestMapping.class);
		StringWriter writer = new StringWriter();
		factory.createMarshallingContext().marshalDocument(mapping, "UTF-8", null, writer);
		Assert.assertEquals(IOUtils.toString(Case9Test.class.getResourceAsStream("output.1.xml")), writer.toString());
		Assert.assertEquals(mapping, factory.createUnmarshallingContext()
				.unmarshalDocument(new StringReader(IOUtils.toString(Case9Test.class.getResourceAsStream("output.2.xml")))));
	}

	protected void autoBind() throws Exception {
		String outputPath = Case9Test.class.getClassLoader().getResource("").getPath();
		String bindingFile = Case9Test.class.getResource("binding.xml").getPath();
		new Compile(true, true).compile(new String[] { outputPath }, bindingFile);
	}

}
