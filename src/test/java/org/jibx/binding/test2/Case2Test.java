package org.jibx.binding.test2;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Assert;

import org.jibx.binding.Compile;
import org.jibx.binding.util.IOUtils;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.JiBXException;
import org.junit.Test;

/**
 * 
 * @author Passyt
 *
 */
public class Case2Test {

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

		IBindingFactory factory = BindingDirectory.getFactory("message", TestMapping.class);
		StringWriter writer = new StringWriter();
		factory.createMarshallingContext().marshalDocument(mapping, "UTF-8", null, writer);
		Assert.assertEquals(IOUtils.toString(Case2Test.class.getResourceAsStream("output.1.xml")), writer.toString());
		Assert.assertEquals(mapping, factory.createUnmarshallingContext()
				.unmarshalDocument(new StringReader(IOUtils.toString(Case2Test.class.getResourceAsStream("output.2.xml")))));
		try {
			factory.createUnmarshallingContext().unmarshalDocument(new StringReader(IOUtils.toString(Case2Test.class.getResourceAsStream("output.3.xml"))));
			Assert.fail("expect JiBXException");
		} catch (JiBXException e) {
			e.getMessage().startsWith("Duplicate element \"{http://test/}Age\"");
		}
	}

	protected void autoBind() throws Exception {
		String outputPath = Case2Test.class.getClassLoader().getResource("").getPath();
		String bindingFile = Case2Test.class.getResource("binding.xml").getPath();
		new Compile(true, true).compile(new String[] { outputPath }, bindingFile);
	}

}
