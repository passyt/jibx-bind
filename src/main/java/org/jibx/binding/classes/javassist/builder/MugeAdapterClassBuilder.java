package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CtMethod;
import javassist.CtNewMethod;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.classes.javassist.JavassistBuildFactory;
import org.jibx.binding.classes.javassist.JavassistClassFile;
import org.jibx.binding.classes.javassist.JavassistMethod;
import org.jibx.binding.model.FormatElement;
import org.jibx.binding.util.IOUtils;
import org.jibx.binding.util.Joiner;
import org.jibx.binding.util.Templates;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class MugeAdapterClassBuilder extends AbstractBuilder {

	private final String className;
	private final List<FormatElement> formarts;
	private final IBindingContext context;
	private final Templates templates = new Templates("org/jibx/binding/classes/javassist/template/mugeadapter");
	private final Map<String, String> parameters = new HashMap<String, String>();

	public MugeAdapterClassBuilder(String className, List<FormatElement> formarts, IBindingContext context) {
		super();
		this.className = className;
		this.formarts = formarts;
		this.context = context;
	}

	public void build() throws JiBXException {
		try {
			JavassistBuildFactory buildFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile classFile = buildFactory.makeClassFile(className);

			classFile.addMethod(new JavassistMethod(templates.parseByResource("newinstance", parameters), classFile));
			initSerializeMethod(classFile);
			initDeserializeMethod(classFile);

			classFile.write(context.getOutputPath());
		} catch (Exception e) {
			throw new JiBXException("Build muge adapter class faild on [" + className + "]", e);
		}
	}

	private void initSerializeMethod(JavassistClassFile classFile) throws Exception {
		List<String> codes = new ArrayList<String>();
		for (FormatElement format : formarts) {
			String type = format.getType();
			String serializerName = format.getAttributes().getSerializerName();
			if (serializerName != null) {
				codes.add("if(object instanceof " + type + "){");
				codes.add("return " + serializerName + "((" + type + ")object);");
				codes.add("}");
			}
		}

		String extendCode = "";
		try {
			Class.forName("org.joda.time.LocalDate");
			extendCode = IOUtils.toString(Templates.class.getClassLoader().getResourceAsStream("org/jibx/binding/classes/javassist/template/mugeadapter/joda.serialize.template"));
		} catch (ClassNotFoundException e) {
		}

		classFile.addMethod(new JavassistMethod(templates
				.parseByResource("serialize", newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes), "ExtendCode", extendCode)), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_double", new HashMap<String, String>()), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_float", new HashMap<String, String>()), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_int", new HashMap<String, String>()), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_long", new HashMap<String, String>()), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_boolean", new HashMap<String, String>()), classFile));
		classFile.addMethod(new JavassistMethod(templates.parseByResource("serialize_char", new HashMap<String, String>()), classFile));
	}

	private void initDeserializeMethod(JavassistClassFile classFile) throws Exception {
		JavassistBuildFactory buildFactory = (JavassistBuildFactory) context.getBuildFactory();
		JavassistClassFile exampleClassFile = buildFactory.getClassFile("org.jibx.binding.classes.javassist.builder.MugeAdapterClassExample");
		CtMethod exampleMethod = exampleClassFile.getClazz().getMethod("defaultDeserialize", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;");
		CtMethod defaultDeserializeMethod = CtNewMethod.copy(exampleMethod, classFile.getClazz(), null);
		classFile.addMethod(new JavassistMethod(defaultDeserializeMethod));

		List<String> codes = new ArrayList<String>();
		for (FormatElement format : formarts) {
			String type = format.getType();
			String deserializerName = format.getAttributes().getDeserializerName();
			if (deserializerName != null) {
				codes.add("if(\"" + type + "\".equals(type)){");
				codes.add("return " + deserializerName + "(object);");
				codes.add("}");
			}
		}

		String extendCode = "";
		try {
			Class.forName("org.joda.time.LocalDate");
			extendCode = IOUtils
					.toString(Templates.class.getClassLoader().getResourceAsStream("org/jibx/binding/classes/javassist/template/mugeadapter/joda.deserialize.template"));
		} catch (ClassNotFoundException e) {
		}

		classFile.addMethod(new JavassistMethod(templates.parseByResource("deserialize",
				newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes), "ExtendCode", extendCode)), classFile));
	}

}
