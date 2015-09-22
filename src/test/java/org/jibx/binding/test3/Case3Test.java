package org.jibx.binding.test3;

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
public class Case3Test {

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

		IBindingFactory factory = BindingDirectory.getFactory("message", TestMapping.class);
		StringWriter writer = new StringWriter();
		factory.createMarshallingContext().marshalDocument(mapping, "UTF-8", null, writer);
		Assert.assertEquals(IOUtils.toString(Case3Test.class.getResourceAsStream("output.xml")), writer.toString());
		Assert.assertEquals(mapping, factory.createUnmarshallingContext().unmarshalDocument(new StringReader(writer.toString())));
	}

	protected void autoBind() throws Exception {
		String outputPath = Case3Test.class.getClassLoader().getResource("").getPath();
		String bindingFile = Case3Test.class.getResource("binding.xml").getPath();
		new Compile(true, true).compile(new String[] { outputPath }, bindingFile);
	}

}
