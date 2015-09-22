package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.Usage;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.model.ValueElement.Style;
import org.jibx.binding.util.JibxUtils;
import org.jibx.binding.util.Templates;

/**
 * 
 * @author Passyt
 *
 */
public class ValueCodeBuilder extends AbstractBuilder {

	private static Set<String> SIMPLE_TYPES = new HashSet<String>();

	private final ValueElement element;
	private final IBindingContext context;
	private final Templates attrTemplate = new Templates("org/jibx/binding/classes/javassist/template/value/attr");
	private final Templates elementTemplate = new Templates("org/jibx/binding/classes/javassist/template/value/element");
	private final Map<String, String> parameters;

	static {
		SIMPLE_TYPES.add("int");
		SIMPLE_TYPES.add("float");
		SIMPLE_TYPES.add("double");
		SIMPLE_TYPES.add("long");
		SIMPLE_TYPES.add("char");
		SIMPLE_TYPES.add("boolean");
	}

	public ValueCodeBuilder(ValueElement element, IBindingContext context) {
		super();
		this.element = element;
		this.context = context;
		this.parameters = initParameters();
	}

	protected Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		if (element.getNameAttributes().getUri() != null) {
			putParameter(parameters, "uri", element.getNameAttributes().getUri());
		} else if (Style.Attribute == element.getStyle()) {
			putParameter(parameters, "uri", context.currentDefaultAttributeNamespace());
		} else {
			putParameter(parameters, "uri", context.currentDefaultElementNamespace());
		}

		putParameter(parameters, "attributeName", element.getNameAttributes().getName());
		parameters.put("constant", element.getConstant());
		parameters.put("fieldName", element.getPropertyAttributes().getFieldName());
		parameters.put("setMethodName", element.getPropertyAttributes().getSetMethodName());
		parameters.put("getMethodName", element.getPropertyAttributes().getGetMethodName());
		parameters.put("nsIndex", String.valueOf(element.getNamespaceIndex(context)));
		parameters.put("mugeAdapterClassName", context.getMugeAdapterClassName());
		if (element.getPropertyAttributes().getType() == null) {
			parameters.put("type", "java.lang.String");
			parameters.put("typeName", "java.lang.String");
		} else {
			parameters.put("type", element.getPropertyAttributes().getType());
			parameters.put("typeName", element.getPropertyAttributes().getType());
		}
		if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			parameters.put("defaultValue", ", null");
		} else {
			parameters.put("defaultValue", "");
		}

		if ("int".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".intValue()");
			parameters.put("typeName", "java.lang.Integer");
		} else if ("float".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".floatValue()");
			parameters.put("typeName", "java.lang.Float");
		} else if ("double".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".doubleValue()");
			parameters.put("typeName", "java.lang.Double");
		} else if ("long".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".longValue()");
			parameters.put("typeName", "java.lang.Long");
		} else if ("boolean".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".booleanValue()");
			parameters.put("typeName", "java.lang.Boolean");
		} else if ("char".equals(element.getPropertyAttributes().getType())) {
			parameters.put("unboxingMethod", ".charValue()");
			parameters.put("typeName", "java.lang.Character");
		} else {
			parameters.put("unboxingMethod", "");
		}

		return parameters;
	}

	public List<String> buildUnmarshallAttr(String variableName) {
		List<String> codes = new ArrayList<String>();
		if (Style.Attribute == element.getStyle()) {
			String fieldName = element.getPropertyAttributes().getFieldName();
			String setMethodName = element.getPropertyAttributes().getSetMethodName();
			if (element.getConstant() != null) {
				String constantVariable = JibxUtils.newVariableName();
				codes.add(attrTemplate.parseByResource("defineConstantValue", newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
				codes.add(attrTemplate.parseByResource("checkConstantValue", newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
			} else if (setMethodName != null) {
				codes.add(attrTemplate.parseByResource("setValueBySetMethod", newParameters(parameters, "variable", variableName)));
			} else if (fieldName != null) {
				codes.add(attrTemplate.parseByResource("setValueByFieldName", newParameters(parameters, "variable", variableName)));
			} else {
				codes.add(Templates.parse(
						"$variable$ = (($typeName$)$mugeAdapterClassName$.deserialize(context.attributeText($uri$, $attributeName$ $defaultValue$), \"$type$\"))$unboxingMethod$;",
						newParameters(parameters, "variable", variableName)));
			}
		}
		return codes;
	}

	public List<String> buildMarshallAttr(String variableName) {
		List<String> codes = new ArrayList<String>();
		if (Style.Attribute == element.getStyle()) {
			String fieldName = element.getPropertyAttributes().getFieldName();
			String setMethodName = element.getPropertyAttributes().getSetMethodName();
			if (element.getConstant() != null) {
				codes.add(Templates.parse("context.attribute($nsIndex$, $attributeName$, \"$constant$\");", parameters));
			} else if (setMethodName != null) {
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add(Templates.parse("if($variable$.$getMethodName$() != null){", newParameters(parameters, "variable", variableName)));
				}
				codes.add(attrTemplate.parseByResource("getValueByGetMethod", newParameters(parameters, "variable", variableName)));
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add("}");
				}
			} else if (fieldName != null) {
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add(Templates.parse("if($variable$.$fieldName$ != null){", newParameters(parameters, "variable", variableName)));
				}
				codes.add(attrTemplate.parseByResource("getValueByFieldName", newParameters(parameters, "variable", variableName)));
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add("}");
				}
			} else {
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add(Templates.parse("if($variable$ != null){", newParameters(parameters, "variable", variableName)));
				}
				codes.add(Templates.parse("context.attribute($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$));",
						newParameters(parameters, "variable", variableName)));
				if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
					codes.add("}");
				}
			}
		}
		return codes;
	}

	public List<String> buildUnmarshall(String variableName) {
		List<String> codes = new ArrayList<String>();
		if (Style.Element == element.getStyle()) {
			Templates template = elementTemplate;

			String fieldName = element.getPropertyAttributes().getFieldName();
			String setMethodName = element.getPropertyAttributes().getSetMethodName();
			if (element.getConstant() != null) {
				String constantVariable = JibxUtils.newVariableName();
				codes.add(template.parseByResource("defineConstantValue", newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
				codes.add(template.parseByResource("checkConstantValue", newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
			} else if (setMethodName != null) {
				codes.add(template.parseByResource("setValueBySetMethod", newParameters(parameters, "variable", variableName)));
			} else if (fieldName != null) {
				codes.add(template.parseByResource("setValueByFieldName", newParameters(parameters, "variable", variableName)));
			} else {
				codes.add(Templates.parse(
						"$variable$ = (($typeName$)$mugeAdapterClassName$.deserialize(context.parseElementText($uri$, $attributeName$, null), \"$type$\"))$unboxingMethod$;",
						newParameters(parameters, "variable", variableName)));
			}
		} else if (Style.Text == element.getStyle()) {
			String fieldName = element.getPropertyAttributes().getFieldName();
			String setMethodName = element.getPropertyAttributes().getSetMethodName();
			if (element.getConstant() != null) {
				String constantVariable = JibxUtils.newVariableName();
				codes.add(Templates.parse("String $constantVariable$ = context.parseContentText();",
						newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
				codes.add(elementTemplate.parseByResource("checkConstantValue", newParameters(parameters, "constantVariable", constantVariable, "variable", variableName)));
			} else if (setMethodName != null) {
				codes.add(Templates.parse("$variable$.$setMethodName$((($typeName$)$mugeAdapterClassName$.deserialize(context.parseContentText(), \"$type$\"))$unboxingMethod$);",
						newParameters(parameters, "variable", variableName)));
			} else if (fieldName != null) {
				codes.add(Templates.parse("$variable$.$fieldName$ = (($typeName$)$mugeAdapterClassName$.deserialize(context.parseContentText(), \"$type$\"))$unboxingMethod$;",
						newParameters(parameters, "variable", variableName)));
			} else {
				codes.add(Templates.parse("$variable$ = (($typeName$)$mugeAdapterClassName$.deserialize(context.parseContentText(), \"$type$\"))$unboxingMethod$;",
						newParameters(parameters, "variable", variableName)));
			}
		}
		return codes;
	}

	public List<String> buildMarshall(String variableName) {
		List<String> codes = new ArrayList<String>();
		if (Style.Element == element.getStyle()) {
			String methodName = "element";
			Map<String, String> newParameters = newParameters(parameters, "methodName", methodName, "variable", variableName);
			if (element.getConstant() != null) {
				codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, \"$constant$\");", newParameters));
			} else if (Usage.Optional == element.getPropertyAttributes().getUsage() && !SIMPLE_TYPES.contains(element.getPropertyAttributes().getType())) {
				if (element.getPropertyAttributes().getGetMethodName() != null) {
					codes.add(Templates.parse("if($variable$.$getMethodName$() != null){", newParameters));
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$.$getMethodName$()));",
							newParameters));
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					codes.add(Templates.parse("if($variable$.$fieldName$ != null){", newParameters));
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$.$fieldName$));", newParameters));
				} else {
					codes.add(Templates.parse("if($variable$ != null){", newParameters));
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$));", newParameters));
				}
				codes.add("}");
			} else {
				if (element.getPropertyAttributes().getGetMethodName() != null) {
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$.$getMethodName$()));",
							newParameters));
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$.$fieldName$));", newParameters));
				} else {
					codes.add(Templates.parse("context.$methodName$($nsIndex$, $attributeName$, $mugeAdapterClassName$.serialize($variable$));", newParameters));
				}
			}
		} else if (Style.Text == element.getStyle()) {
			Map<String, String> newParameters = newParameters(parameters, "variable", variableName);
			if (element.getConstant() != null) {
				codes.add("try{");
				codes.add(Templates.parse("context.writeContent(\"$constant$\");", newParameters));
				codes.add("}catch(java.io.IOException e){");
				codes.add("throw new org.jibx.runtime.JiBXException(\"write content failed\", e);");
				codes.add("}");
			} else if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
				if (element.getPropertyAttributes().getGetMethodName() != null) {
					codes.add(Templates.parse("if($variable$.$getMethodName$() != null){", newParameters));
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$.$getMethodName$()));", newParameters));
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					codes.add(Templates.parse("if($variable$.$fieldName$ != null){", newParameters));
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$.$fieldName$));", newParameters));
				} else {
					codes.add(Templates.parse("if($variable$ != null){", newParameters));
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$));", newParameters));
				}
				codes.add("}");
			} else {
				if (element.getPropertyAttributes().getGetMethodName() != null) {
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$.$getMethodName$()));", newParameters));
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$.$fieldName$));", newParameters));
				} else {
					codes.add(Templates.parse("context.writeContent($mugeAdapterClassName$.serialize($variable$));", newParameters));
				}
			}
		}
		return codes;
	}
}
