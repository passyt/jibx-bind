package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.CtField;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.classes.javassist.JavassistBuildFactory;
import org.jibx.binding.classes.javassist.JavassistClassFile;
import org.jibx.binding.classes.javassist.JavassistConstructor;
import org.jibx.binding.classes.javassist.JavassistField;
import org.jibx.binding.classes.javassist.JavassistMethod;
import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.binding.model.NestingChild;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.util.JibxUtils;
import org.jibx.binding.util.Joiner;
import org.jibx.binding.util.Templates;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class MappingClassBuilder extends AbstractBuilder {

	private final MappingElementBase mappingElement;
	private final IBindingContext context;

	private final Map<String, String> parameters;
	private final Templates templates = new Templates("org/jibx/binding/classes/javassist/template/mapping");
	private final Templates accessTemplates = new Templates("org/jibx/binding/classes/javassist/template/mapping/access");
	private final ClassGenerateDefinition classDefinition;

	public MappingClassBuilder(MappingElementBase mappingElement, IBindingContext context) {
		context.startMapping(mappingElement);
		this.mappingElement = mappingElement;
		this.context = context;

		this.classDefinition = context.getClassDefinition(mappingElement.getIdentityName(), mappingElement.getClassName());
		this.parameters = initParameters();
	}

	public void build() throws JiBXException {
		try {
			for (String name : mappingElement.getDependsNames(context)) {
				MappingElementBase dependMapping = context.getMappingElement(name);
				if (dependMapping != null) {
					new MappingClassBuilder(dependMapping, context).build();
				}
			}

			if (!mappingElement.isPrecompiled()) {
				buildMappingClass();
			}
			JavassistClassFile mappingAccessClass = null;
			if (!mappingElement.isAbstract()) {
				mappingAccessClass = buildMappingAccessClass();
			} else {
				context.addAbstractMappingClassItem(mappingElement.getIdentityName(), mappingElement.getClassName(), classDefinition.getNewinstanceMethodName(), null, null,
						classDefinition.getAttrTestMethodName(), classDefinition.getUnmarshalAttrMethodName(), classDefinition.getMarshalAttrMethodName(), null,
						classDefinition.getUnmarshalMethodName(), classDefinition.getMarshalMethodName(), mappingElement.getAllNamespaceIndexes(context));
			}
			context.addMappingClassItem(mappingElement.getIdentityName(), mappingElement.getNameAttributes().getName(), mappingAccessClass == null ? null : mappingAccessClass
					.getClazz().getName(), mappingAccessClass == null ? null : mappingAccessClass.getClazz().getName(), mappingElement.getNamespaceIndex(context));
		} finally {
			context.endMapping(mappingElement);
		}
	}

	protected void buildMappingClass() throws JiBXException {
		try {
			JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
			JavassistClassFile mappingClass = classFactory.getClassFile(mappingElement.getClassName());
			if (mappingElement.isPrecompiled()) {
				return;
			}

			if (mappingClass.getClazz().isInterface()) {
				return;
			}

			if (context.isVerbose()) {
				System.out.println("Generating code for mapping " + mappingElement.getClassName());
			}

			if (mappingElement.isAbstract()) {
				initAbstractMapping(mappingClass);
			} else {
				initBasicMapping(mappingClass);
			}

			String factoryClassName = context.getBindingRoot().getFactoryClassName(context.getBindingRoot());
			try {
				CtField field = mappingClass.getClazz().getField("JiBX_bindingList", "Ljava/lang/String;");
				String constantValue = (String) field.getConstantValue();
				String[] strings = constantValue.split("\\|");
				Set<String> factoryClassNames = new LinkedHashSet<String>();
				if (strings != null) {
					for (String each : strings) {
						if (each.trim().length() == 0) {
							continue;
						}
						factoryClassNames.add(each);
					}
				}
				factoryClassNames.add(factoryClassName);
				factoryClassName = Joiner.on("|").join(factoryClassNames);
			} catch (Exception e) {
			}
			mappingClass.addField(new JavassistField("public static final String JiBX_bindingList = \"|" + factoryClassName + "|\";", mappingClass));

			mappingClass.write(context.getOutputPath());
			context.addClassFile(mappingClass);
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build mapping class faild on [" + mappingElement.getClassName() + "]", e);
		}
	}

	protected void initBasicMapping(JavassistClassFile mappingClass) throws Exception {
		mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$.newinstance", parameters), mappingClass));
		List<String> interfaces = new ArrayList<String>();
		if (context.getBindingRoot().getDirection().isInput()) {
			interfaces.add("org.jibx.runtime.IUnmarshallable");
			mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$.JiBX_className", parameters), mappingClass));
			mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$.unmarshal", parameters), mappingClass));

			buildInternalUnmarshallableAttr(mappingClass);
			buildInternalUnmarshallable(mappingClass);
		}
		if (context.getBindingRoot().getDirection().isOutput()) {
			interfaces.add("org.jibx.runtime.IMarshallable");
			mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$.JiBX_getName", parameters), mappingClass));
			mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$.marshal", parameters), mappingClass));

			buildInternalMarshallableAttr(mappingClass);
			buildInternalMarshallable(mappingClass);
		}

		mappingClass.addInterfaces(interfaces.toArray(new String[0]));
	}

	protected void initAbstractMapping(JavassistClassFile mappingClass) throws Exception {
		mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_abstract.newinstance", parameters), mappingClass));
		if (context.getBindingRoot().getDirection().isInput()) {
			buildInternalUnmarshallableAttr(mappingClass);
			buildInternalUnmarshallable(mappingClass);
			if (mappingElement.hasAttributes(context)) {
				List<String> codes = new ArrayList<String>();
				for (ValueElement each : mappingElement.attributes(context)) {
					String ns = each.getNamespace(context);
					ns = ns == null ? "null" : "\"" + ns + "\"";
					codes.add("context.hasAttribute(" + ns + ", \"" + each.getNameAttributes().getName() + "\")");
				}

				if (codes.isEmpty()) {
					codes.add("false");
				}

				mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.attrTest",
						newParameters(parameters, "CodeBody", Joiner.on("||").join(codes))), mappingClass));
			}
		}
		if (context.getBindingRoot().getDirection().isOutput()) {
			buildInternalMarshallableAttr(mappingClass);
			buildInternalMarshallable(mappingClass);
		}
	}

	protected JavassistClassFile buildMappingAccessClass() throws JiBXException {
		JavassistBuildFactory classFactory = (JavassistBuildFactory) context.getBuildFactory();
		BindingElement bindingElement = context.getBindingRoot();
		if (mappingElement.isPrecompiled()) {
			bindingElement = mappingElement.getPrecompiledBindingElement();
		}

		String accessClassName = JibxUtils.accessClassName(bindingElement.getName(), mappingElement.getClassName());
		try {
			if (mappingElement.isPrecompiled()) {
				for (int i = 1; !classFactory.existClass(accessClassName); i++) {
					accessClassName = accessClassName + i;
				}
				return classFactory.getClassFile(accessClassName);
			}

			// for (int i = 1; classFactory.existClass(accessClassName); i++) {
			// accessClassName = accessClassName + i;
			// }

			if (context.isVerbose()) {
				System.out.println("Generating code for mapping access " + accessClassName);
			}

			JavassistClassFile mappingAccessClass = classFactory.makeClassFile(accessClassName);
			mappingAccessClass.addConstructor(JavassistConstructor.defaultConstructor(mappingAccessClass));

			List<String> interfaces = new ArrayList<String>();
			if (context.getBindingRoot().getDirection().isInput()) {
				interfaces.add("org.jibx.runtime.IUnmarshaller");
				mappingAccessClass.addMethod(new JavassistMethod(accessTemplates.parseByResource("JiBX_$Name$$ClassName$_access.isPresent", parameters), mappingAccessClass));
				List<String> codes = new ArrayList<String>();
				codes.add("if(object == null){");
				codes.add("object = $className$.$newinstanceMethodName$(null, (org.jibx.runtime.impl.UnmarshallingContext)context);");
				codes.add("}");

				if (mappingElement.hasAttributes(context)) {
					codes.add("((org.jibx.runtime.impl.UnmarshallingContext)context).parseToStartTag($uri$, $elementName$);");
					codes.add("$className$.$unmarshalAttrMethodName$(($className$)object, (org.jibx.runtime.impl.UnmarshallingContext)context);");
				}
				codes.add("((org.jibx.runtime.impl.UnmarshallingContext)context).parsePastStartTag($uri$, $elementName$);");
				codes.add("Object result = $className$.$unmarshalMethodName$(($className$)object, (org.jibx.runtime.impl.UnmarshallingContext)context);");
				codes.add("((org.jibx.runtime.impl.UnmarshallingContext)context).parsePastCurrentEndTag($uri$, $elementName$);");
				codes.add("return result;");
				mappingAccessClass.addMethod(new JavassistMethod(accessTemplates.parseByResource("JiBX_$Name$$ClassName$_access.unmarshal",
						newParameters(parameters, "CodeBody", Templates.parse(Joiner.on("\n").join(codes), parameters))), mappingAccessClass));
			}
			if (context.getBindingRoot().getDirection().isOutput()) {
				interfaces.add("org.jibx.runtime.IMarshaller");
				mappingAccessClass.addMethod(new JavassistMethod(accessTemplates.parseByResource("JiBX_$Name$$ClassName$_access.isExtension", parameters), mappingAccessClass));

				List<String> codes = new ArrayList<String>();
				int nsIndex = mappingElement.getNamespaceIndex(context);
				if (mappingElement.hasNamespace(context)) {
					List<Integer> nsIndexes = mappingElement.getAllNamespaceIndexes(context);
					List<String> nses = mappingElement.getAllNamespacePrefixes(context);
					codes.add("((org.jibx.runtime.impl.MarshallingContext)context).startTagNamespaces(" + nsIndex + ", $elementName$, new int[] {" + Joiner.on(",").join(nsIndexes)
							+ "}, new String[] {\"" + Joiner.on("\",\"").join(nses) + "\"});");
				} else {
					codes.add("((org.jibx.runtime.impl.MarshallingContext)context).startTag(" + nsIndex + ", $elementName$);");
				}
				if (mappingElement.hasAttributes(context)) {
					codes.add("$className$.$marshalAttrMethodName$(($className$)object, (org.jibx.runtime.impl.MarshallingContext)context);");
				}
				codes.add("((org.jibx.runtime.impl.MarshallingContext)context).closeStartContent();");
				codes.add("$className$.$marshalMethodName$(($className$)object, (org.jibx.runtime.impl.MarshallingContext)context);");
				codes.add("((org.jibx.runtime.impl.MarshallingContext)context).endTag(" + nsIndex + ", $elementName$);");
				mappingAccessClass.addMethod(new JavassistMethod(accessTemplates.parseByResource("JiBX_$Name$$ClassName$_access.marshal",
						newParameters(parameters, "CodeBody", Templates.parse(Joiner.on("\n").join(codes), parameters))), mappingAccessClass));
			}
			mappingAccessClass.addInterfaces(interfaces.toArray(new String[0]));
			mappingAccessClass.write(context.getOutputPath());
			context.addClassFile(mappingAccessClass);
			return mappingAccessClass;
		} catch (JiBXException e) {
			throw e;
		} catch (Exception e) {
			throw new JiBXException("Build mapping class faild on [" + accessClassName + "]", e);
		}
	}

	protected void buildInternalUnmarshallableAttr(JavassistClassFile mappingClass) throws Exception {
		List<String> codes = new ArrayList<String>();
		if (mappingElement.hasAttributes(context)) {
			for (String mapAsName : mappingElement.getMapAsNames()) {
				MappingElementBase element = context.getMappingElement(mapAsName);
				if (element != null && element.hasAttributes(context)) {
					ClassGenerateDefinition extendClassDefinition = context.getClassDefinition(element.getIdentityName(), element.getClassName());
					codes.add(Templates.parse("$className$.$unmarshalAttrMethodName$(object, context);",
							newParameters(parameters, "className", element.getClassName(), "unmarshalAttrMethodName", extendClassDefinition.getUnmarshalAttrMethodName())));
				}
			}

			List<ValueElement> elements = mappingElement.attributes(context);
			for (ValueElement element : elements) {
				codes.addAll(new ValueCodeBuilder(element, context).buildUnmarshallAttr("object"));
			}

			mappingClass.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.unmarshalAttr",
					newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), mappingClass));
		}
	}

	protected void buildInternalUnmarshallable(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();

		for (Iterator<NestingChild> it = mappingElement.nestingChildIterator(); it.hasNext();) {
			NestingChild child = it.next();
			if (child instanceof ValueElement) {
				ValueElement valueElement = (ValueElement) child;
				codes.addAll(new ValueCodeBuilder(valueElement, context).buildUnmarshall("object"));
			} else if (child instanceof StructureElement) {
				StructureElement structureElement = (StructureElement) child;
				if (structureElement.getPropertyAttributes().getFieldName() != null || structureElement.getPropertyAttributes().getSetMethodName() != null) {
					codes.addAll(new StructureCodeBuilder(structureElement, context).buildUnmarshall("object", true));
				} else {
					codes.addAll(new StructureCodeBuilder(structureElement, context).buildUnmarshall("object", false));
				}
			} else if (child instanceof CollectionElement) {
				CollectionElement collectionElement = (CollectionElement) child;
				codes.addAll(new CollectionCodeBuilder(collectionElement, context).buildUnmarshall());
			}
		}

		clazz.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.unmarshal",
				newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

	protected void buildInternalMarshallableAttr(JavassistClassFile clazz) throws Exception {
		if (mappingElement.hasAttributes(context)) {
			List<String> codes = new ArrayList<String>();
			for (String mapAsName : mappingElement.getMapAsNames()) {
				MappingElementBase element = context.getMappingElement(mapAsName);
				if (element != null && element.hasAttributes(context)) {
					ClassGenerateDefinition extendClassDefinition = context.getClassDefinition(element.getIdentityName(), element.getClassName());
					codes.add(Templates.parse("$className$.$marshalAttrMethodName$(object, context);",
							newParameters(parameters, "className", element.getClassName(), "marshalAttrMethodName", extendClassDefinition.getMarshalAttrMethodName())));
				}
			}

			for (ValueElement each : mappingElement.attributes(context)) {
				codes.addAll(new ValueCodeBuilder(each, context).buildMarshallAttr("object"));
			}
			clazz.addMethod(new JavassistMethod(templates.parseByResource("JiBX_$Name$$ClassName$_internal.marshalAttr",
					newParameters(parameters, "CodeBody", Templates.parse(Joiner.on("\n").join(codes), parameters))), clazz));
		}
	}

	protected void buildInternalMarshallable(JavassistClassFile clazz) throws Exception {
		List<String> codes = new ArrayList<String>();

		for (Iterator<NestingChild> it = mappingElement.nestingChildIterator(); it.hasNext();) {
			NestingChild child = it.next();
			if (child instanceof ValueElement) {
				ValueElement valueElement = (ValueElement) child;
				codes.addAll(new ValueCodeBuilder(valueElement, context).buildMarshall("object"));
			} else if (child instanceof StructureElement) {
				StructureElement structureElement = (StructureElement) child;
				if (structureElement.getPropertyAttributes().getFieldName() != null || structureElement.getPropertyAttributes().getGetMethodName() != null) {
					codes.addAll(new StructureCodeBuilder(structureElement, context).buildMarshall("object", true));
				} else {
					codes.addAll(new StructureCodeBuilder(structureElement, context).buildMarshall("object", false));
				}
			} else if (child instanceof CollectionElement) {
				CollectionElement collectionElement = (CollectionElement) child;
				codes.addAll(new CollectionCodeBuilder(collectionElement, context).buildMarshall());
			}
		}

		clazz.addMethod(new JavassistMethod(templates
				.parseByResource("JiBX_$Name$$ClassName$_internal.marshal", newParameters(parameters, "CodeBody", Joiner.on("\n").join(codes))), clazz));
	}

	protected Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();

		putParameter(parameters, "uri", mappingElement.getNamespace(context));
		parameters.put("className", mappingElement.getClassName());
		parameters.put("factoryClassName", context.getBindingRoot().getFactoryClassName(context.getBindingRoot()));
		putParameter(parameters, "elementName", mappingElement.getNameAttributes().getName());
		parameters.put("newinstanceMethodName", classDefinition.getNewinstanceMethodName());
		parameters.put("marshalAttrMethodName", classDefinition.getMarshalAttrMethodName());
		parameters.put("marshalMethodName", classDefinition.getMarshalMethodName());
		parameters.put("unmarshalMethodName", classDefinition.getUnmarshalMethodName());
		parameters.put("unmarshalAttrMethodName", classDefinition.getUnmarshalAttrMethodName());
		parameters.put("attrTestMethodName", classDefinition.getAttrTestMethodName());

		return parameters;
	}

}