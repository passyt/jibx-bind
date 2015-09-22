package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.IClassFile;
import org.jibx.binding.IConstructor;
import org.jibx.binding.IField;
import org.jibx.binding.IMethod;
import org.jibx.binding.classes.javassist.JavassistBuildFactory;
import org.jibx.binding.classes.javassist.JavassistClassFile;
import org.jibx.binding.classes.javassist.JavassistConstructor;
import org.jibx.binding.classes.javassist.JavassistField;
import org.jibx.binding.classes.javassist.JavassistMethod;
import org.jibx.binding.util.JibxUtils;
import org.jibx.binding.util.Templates;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class FactoryClassBuilder extends AbstractBuilder {

	private final IBindingContext context;
	private final String factoryClassName;
	private final Templates templates = new Templates("org/jibx/binding/classes/javassist/template/factory");

	/**
	 * $Package$.JiBX_$Name$Factory
	 * 
	 * @param factoryClassName
	 */
	public FactoryClassBuilder(IBindingContext context) {
		super();
		this.context = context;
		this.factoryClassName = context.getBindingRoot().getFactoryClassName(context.getBindingRoot());
	}

	public void build() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile factoryClass = classFactory.makeClassFile(factoryClassName);

			Map<String, String> parameters = initParameters();

			factoryClass.addInterfaces("org.jibx.runtime.IBindingFactory");
			factoryClass.setSuperclass("org.jibx.runtime.impl.BindingFactoryBase");

			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.combine", parameters), factoryClass));
			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.getClassList", parameters), factoryClass));
			factoryClass.addConstructor(new JavassistConstructor(templates.parseByResource("JiBX_$Name$Factory.constructor", parameters), factoryClass));
			factoryClass.addField(new JavassistField("private static IBindingFactory m_inst;", factoryClass));
			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.getInstance", parameters), factoryClass));
			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.getTypeIndex", parameters), factoryClass));
			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.getCompilerDistribution", parameters), factoryClass));
			factoryClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$Factory.getCompilerVersion", parameters), factoryClass));

			factoryClass.write(context.getOutputPath());
		} catch (Exception e) {
			throw new JiBXException("Build factory class faild on [" + context.getBindingRoot().getFactoryClassName(context.getBindingRoot()) + "]", e);
		}
	}

	private Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", context.getBindingRoot().getName());
		parameters.put("factoryClassName", context.getBindingRoot().getFactoryClassName(context.getBindingRoot()));

		putParameter(parameters, "mappednames", context.getMappednames());
		putParameter(parameters, "umarnames", context.getUmarnames());
		putParameter(parameters, "marnames", context.getMarnames());

		List<String> uris = new ArrayList<String>();
		List<String> prefixes = new ArrayList<String>();
		for (NamespaceDefinition each : context.getNamespaces().values()) {
			uris.add(each.getUri());
			prefixes.add(each.getPrefix());
		}

		putParameter(parameters, "uris", uris);
		putParameter(parameters, "prefixes", prefixes);
		parameters.put("gmapnames", wrap(context.getGmapnames()));
		parameters.put("abmapdetails", wrap(context.getAbmapdetails()));
		if (context.getGmapuris() == null || context.getGmapuris().size() == 0) {
			parameters.put("gmapuris", "new int[0]");
		} else {
			parameters.put("gmapuris", "new int[]{" + join(context.getGmapuris()) + "}");
		}
		if (context.getAbmapnss() == null || context.getAbmapnss().size() == 0) {
			parameters.put("abmapnss", "new int[0]");
		} else {
			parameters.put("abmapnss", "new int[]{" + join(context.getAbmapnss()) + "}");
		}
		putParameter(parameters, "prenames", "");
		putParameter(parameters, "prefacts", "");
		putParameter(parameters, "prehashes", "");

		List<String> boundnames = new ArrayList<String>();
		boundnames.add(factoryClassName);
		Collection<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> classFiles = context.getClassFiles();
		for (IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField> each : classFiles) {
			boundnames.add(each.getClassName());
		}
		putParameter(parameters, "boundnames", JibxUtils.buildClassNamesBlob(boundnames));
		parameters.put("prensmaps", "new String[0]");
		return parameters;
	}

	private static String wrap(String text) {
		int size = 500;
		if(text == null){
			return null;
		}
		if (text.length() <= size) {
			return "\""+text+"\"";
		}

		String result = "new StringBuilder()";
		for (int index = 0; index < text.length(); index+=size) {
			if (index + size > text.length()) {
				result += ".append(\"" + text.substring(index) + "\")";
			} else {
				result += ".append(\"" + text.substring(index, index+size) + "\")";
			}
		}
		return result + ".toString()";
	}

	private String join(List<Integer> values) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < values.size(); i++) {
			Integer integer = values.get(i);
			if (integer == null) {
				builder.append("0");
			} else {
				builder.append(integer);
			}

			if (i < values.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}
	
}
