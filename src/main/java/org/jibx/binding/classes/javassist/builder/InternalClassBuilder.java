package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.classes.javassist.JavassistBuildFactory;
import org.jibx.binding.classes.javassist.JavassistClassFile;
import org.jibx.binding.classes.javassist.JavassistMethod;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.NestingChild;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.util.Joiner;
import org.jibx.binding.util.Templates;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class InternalClassBuilder extends AbstractBuilder {

	private final Templates templates = new Templates("org/jibx/binding/classes/javassist/template/internal");

	private final String className;
	private final List<NestingChild> children;
	private final IBindingContext context;
	private final ClassGenerateDefinition classDefinition;
	private final Map<String, String> parameters;

	public InternalClassBuilder(String className, List<NestingChild> children, IBindingContext context) {
		super();
		this.className = className;
		this.children = children;
		this.context = context;
		this.classDefinition = context.nextClassDefinition(className);
		this.parameters = initParameters();
	}

	protected Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put("className", className);
		parameters.put("newinstanceMethodName", classDefinition.getNewinstanceMethodName());
		parameters.put("marshalAttrMethodName", classDefinition.getMarshalAttrMethodName());
		parameters.put("marshalMethodName", classDefinition.getMarshalMethodName());
		parameters.put("unmarshalMethodName", classDefinition.getUnmarshalMethodName());
		parameters.put("unmarshalAttrMethodName", classDefinition.getUnmarshalAttrMethodName());
		parameters.put("attrTestMethodName", classDefinition.getAttrTestMethodName());

		return parameters;
	}

	public ClassGenerateDefinition buildUnmarshall() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile clazz = classFactory.getClassFile(className);
			buildInternalUnmarshallAttr(clazz);
			buildInternalUnmarshall(clazz);

			clazz.write(context.getOutputPath());
			return this.classDefinition;
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build inner class unmarshallable faild on [" + className + "]", e);
		}
	}

	public ClassGenerateDefinition buildMarshall() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile clazz = classFactory.getClassFile(className);
			buildInternalMarshallAttr(clazz);
			buildInternalMarshall(clazz);

			clazz.write(context.getOutputPath());
			context.addClassFile(clazz);

			return this.classDefinition;
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build inner class marshallable faild on [" + className + "]", e);
		}
	}

	protected ClassGenerateDefinition buildUnmarshallAttr() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile clazz = classFactory.getClassFile(className);
			buildInternalUnmarshallAttr(clazz);

			clazz.write(context.getOutputPath());
			return this.classDefinition;
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build inner class unmarshallable faild on [" + className + "]", e);
		}
	}

	protected ClassGenerateDefinition buildMarshallAttr() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile clazz = classFactory.getClassFile(className);
			buildInternalMarshallAttr(clazz);

			clazz.write(context.getOutputPath());
			context.addClassFile(clazz);

			return this.classDefinition;
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build inner class marshallable faild on [" + className + "]", e);
		}
	}

	protected void buildInternalMarshall(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();
		for (NestingChild child : children) {
			if (child instanceof ValueElement) {
				codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildMarshall("object"));
			} else if (child instanceof StructureElement) {
				codes.addAll(new StructureCodeBuilder((StructureElement) child, context).buildMarshall("object", true));
			} else if (child instanceof CollectionElement) {
				codes.addAll(new CollectionCodeBuilder((CollectionElement) child, context).buildMarshall());
			}
		}

		clazz.addMethod(new JavassistMethod(templates
				.parseByResource("JiBX_$Name$$ClassName$_internal.marshal", newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

	protected void buildInternalUnmarshall(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();
		for (NestingChild child : children) {
			if (child instanceof ValueElement) {
				codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildUnmarshall("object"));
			} else if (child instanceof StructureElement) {
				codes.addAll(new StructureCodeBuilder((StructureElement) child, context).buildUnmarshall("object", true));
			} else if (child instanceof CollectionElement) {
				codes.addAll(new CollectionCodeBuilder((CollectionElement) child, context).buildUnmarshall());
			}
		}

		clazz.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.unmarshal",
				newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

	protected void buildInternalMarshallAttr(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();
		for (NestingChild child : children) {
			if (child instanceof ValueElement) {
				codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildMarshallAttr("object"));
			}
		}

		clazz.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.marshalAttr",
				newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

	protected void buildInternalUnmarshallAttr(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();
		for (NestingChild child : children) {
			if (child instanceof ValueElement) {
				codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildUnmarshallAttr("object"));
			}
		}

		clazz.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.unmarshalAttr",
				newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

}
