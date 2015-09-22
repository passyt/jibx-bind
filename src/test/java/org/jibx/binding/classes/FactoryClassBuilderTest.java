package org.jibx.binding.classes;

import java.util.ArrayList;
import java.util.HashMap;

import org.easymock.EasyMock;
import org.jibx.binding.IBindingContext;
import org.jibx.binding.IClassFile;
import org.jibx.binding.IConstructor;
import org.jibx.binding.IField;
import org.jibx.binding.IMethod;
import org.jibx.binding.classes.javassist.JavassistBuildFactory;
import org.jibx.binding.classes.javassist.builder.FactoryClassBuilder;
import org.jibx.binding.classes.javassist.builder.NamespaceDefinition;
import org.jibx.binding.model.BindingElement;
import org.junit.Test;

/**
 * 
 * @author Passyt
 *
 */
public class FactoryClassBuilderTest {

	@Test
	public void test() throws Exception {
		BindingElement bindingElement = new BindingElement();
		bindingElement.setName("test");

		IBindingContext context = EasyMock.createMock(IBindingContext.class);
		EasyMock.expect(context.getBuildFactory()).andReturn(new JavassistBuildFactory()).anyTimes();
		EasyMock.expect(context.getBindingRoot()).andReturn(bindingElement).anyTimes();
		EasyMock.expect(context.getMappednames()).andReturn(null).anyTimes();
		EasyMock.expect(context.getUmarnames()).andReturn(null).anyTimes();
		EasyMock.expect(context.getMarnames()).andReturn(null).anyTimes();
		EasyMock.expect(context.getGmapnames()).andReturn(null).anyTimes();
		EasyMock.expect(context.getGmapuris()).andReturn(null).anyTimes();
		EasyMock.expect(context.getAbmapdetails()).andReturn(null).anyTimes();
		EasyMock.expect(context.getAbmapnss()).andReturn(null).anyTimes();
		EasyMock.expect(context.getClassFiles()).andReturn(new ArrayList<IClassFile<? extends IConstructor,? extends IMethod,? extends IField>>()).anyTimes();
		EasyMock.expect(context.getNamespaces()).andReturn(new HashMap<String, NamespaceDefinition>()).anyTimes();
		EasyMock.expect(context.getOutputPath()).andReturn(System.getProperty("java.io.tmpdir")).anyTimes();
		EasyMock.replay(context);

		FactoryClassBuilder builder = new FactoryClassBuilder(context);
		builder.build();
	}

}
