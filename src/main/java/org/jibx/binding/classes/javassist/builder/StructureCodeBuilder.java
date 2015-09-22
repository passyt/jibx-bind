package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.binding.model.NestingChild;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.TemplateElementBase;
import org.jibx.binding.model.Usage;
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
public class StructureCodeBuilder extends AbstractBuilder {

	private final StructureElement element;
	private final IBindingContext context;
	private final Map<String, String> parameters;

	public StructureCodeBuilder(StructureElement element, IBindingContext context) {
		super();
		this.element = element;
		this.context = context;
		this.parameters = initParameters();
	}

	protected Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		if (element.getNameAttributes().getUri() != null) {
			putParameter(parameters, "uri", element.getNameAttributes().getUri());
		} else {
			putParameter(parameters, "uri", context.currentDefaultElementNamespace());
		}
		putParameter(parameters, "structureName", element.getNameAttributes().getName());
		parameters.put("fieldName", element.getPropertyAttributes().getFieldName());
		parameters.put("setMethodName", element.getPropertyAttributes().getSetMethodName());
		parameters.put("getMethodName", element.getPropertyAttributes().getGetMethodName());
		parameters.put("className", element.getPropertyAttributes().getType());
		return parameters;
	}

	public List<String> buildUnmarshall(String variableName, boolean handleAttributes) throws JiBXException {
		List<String> codes = new ArrayList<String>();
		Map<String, String> newParameters = newParameters(parameters, "variable", variableName);

		String type = element.getPropertyAttributes().getType();
		if (type == null) {
			type = element.getMapAsName();
		}
		if (type != null || element.nestingchildSize() > 0 || element.getObjectAttributes().getUnmarshallerName() != null) {
			boolean appendIf = false;
			if (element.getNameAttributes().getName() != null && Usage.Optional == element.getPropertyAttributes().getUsage()) {
				codes.add(Templates.parse("if(context.isAt($uri$, $structureName$)){", parameters));
				appendIf = true;
			}

			boolean appendStart = false;
			if ((type != null && !type.startsWith("java")) || element.getObjectAttributes().getUnmarshallerName() != null) {
				newParameters = newParameters(newParameters, "className", type);

				if (element.nestingchildSize() == 0) {
					MappingElementBase mappingElement = context.getMappingElement(element.getMapAsName());
					if (mappingElement == null) {
						mappingElement = context.getMappingElement(type);
					}
					if (mappingElement != null) {
						newParameters = newParameters(newParameters, "className", mappingElement.getClassName());
						type = mappingElement.getClassName();

						String unmarshalVariableName = JibxUtils.newVariableName();
						if (element.getPropertyAttributes().getFieldName() == null && element.getPropertyAttributes().getSetMethodName() == null) {
							unmarshalVariableName = variableName;
							newParameters = newParameters(newParameters, "unmarshalVariableName", unmarshalVariableName);
						} else {
							newParameters = newParameters(newParameters, "unmarshalVariableName", unmarshalVariableName);
							// TODO
							codes.add(Templates.parse("$className$ $unmarshalVariableName$ = new $className$();", newParameters));
						}

						if (!mappingElement.isAbstract()) {
							if (element.getNameAttributes().getName() != null) {
								appendStart = true;
								codes.add(Templates.parse("context.parsePastStartTag($uri$, $structureName$);", newParameters));
							}
							if (element.getPropertyAttributes().getSetMethodName() != null) {
								codes.add(Templates
										.parse("$variable$.$setMethodName$(context.getUnmarshaller(\"$className$\").isPresent(context) ? ($className$)context.getUnmarshaller(\"$className$\").unmarshal($unmarshalVariableName$, context) : null);",
												newParameters));
							} else if (element.getPropertyAttributes().getFieldName() != null) {
								codes.add(Templates
										.parse("$variable$.$fieldName$ = context.getUnmarshaller(\"$className$\").isPresent(context) ? ($className$)context.getUnmarshaller(\"$className$\").unmarshal($unmarshalVariableName$, context) : null;",
												newParameters));
							} else {
								codes.add(Templates
										.parse("$variable$ = context.getUnmarshaller(\"$className$\").isPresent(context) ? ($className$)context.getUnmarshaller(\"$className$\").unmarshal($unmarshalVariableName$, context) : null;",
												newParameters));
							}
						} else {
							ClassGenerateDefinition classDefinition = context.getClassDefinition(element.getMapAsName() == null ? type : element.getMapAsName(), type);
							if (handleAttributes && mappingElement.hasAttributes(context)) {
								if (element.getNameAttributes().getName() != null) {
									codes.add(Templates.parse("context.parseToStartTag($uri$, $structureName$);", newParameters));
								}
								codes.add(Templates.parse("$className$." + classDefinition.getUnmarshalAttrMethodName() + "(($className$)" + unmarshalVariableName + ", context);",
										newParameters));
							}

							if (element.getNameAttributes().getName() != null) {
								appendStart = true;
								codes.add(Templates.parse("context.parsePastStartTag($uri$, $structureName$);", newParameters));
							}
							if (mappingElement.hasElements(context)) {
								codes.add(Templates.parse("$className$." + classDefinition.getUnmarshalMethodName() + "(($className$)" + unmarshalVariableName + ", context);",
										newParameters));
							}

							if (element.getPropertyAttributes().getSetMethodName() != null) {
								codes.add(Templates.parse("$variable$.$setMethodName$($unmarshalVariableName$);", newParameters));
							} else if (element.getPropertyAttributes().getFieldName() != null) {
								codes.add(Templates.parse("$variable$.$fieldName$ = $unmarshalVariableName$;", newParameters));
							}
						}
					} else {
						codes.add("if(!context.isEnd()){");

						String resultCode = "(" + type + ")context.unmarshalElement()";
						if (element.getObjectAttributes().getUnmarshallerName() != null) {
							String newVariableName = JibxUtils.newVariableName();

							if (element.getObjectAttributes().isSimpleUnmarshallerConstructor()) {
								codes.add(element.getObjectAttributes().getUnmarshallerName() + " " + newVariableName + " = new "
										+ element.getObjectAttributes().getUnmarshallerName() + "();");
							} else {
								codes.add(element.getObjectAttributes().getUnmarshallerName() + " " + newVariableName + " = new "
										+ element.getObjectAttributes().getUnmarshallerName() + "(\"" + element.getNamespace(context) + "\", " + element.getNamespaceIndex(context)
										+ ",\"" + element.getNameAttributes().getName() + "\");");
							}

							String instanceVariableName = JibxUtils.newVariableName();
							if (element.getPropertyAttributes().getSetMethodName() != null || element.getPropertyAttributes().getFieldName() != null) {
								codes.add(type + " " + instanceVariableName + " = " + context.getMugeAdapterClassName() + ".newInstance(\"" + type + "\");");
								resultCode = newVariableName + ".isPresent(context) ? (" + type + ")" + newVariableName + ".unmarshal(" + instanceVariableName
										+ ", context) : null";
							} else {
								resultCode = newVariableName + ".isPresent(context) ? (" + type + ")" + newVariableName + ".unmarshal(" + variableName + ", context) : null";
							}
						}

						if (element.getPropertyAttributes().getSetMethodName() != null) {
							codes.add(variableName + "." + element.getPropertyAttributes().getSetMethodName() + "(" + resultCode + ");");
						} else if (element.getPropertyAttributes().getFieldName() != null) {
							codes.add(variableName + "." + element.getPropertyAttributes().getFieldName() + " = " + resultCode + ";");
						} else {
							codes.add(variableName + " = " + resultCode + ";");
						}
						codes.add("}");
					}
				} else {
					String newVariableName = JibxUtils.newVariableName();
					if (element.getPropertyAttributes().getFieldName() != null || element.getPropertyAttributes().getSetMethodName() != null) {
						codes.add(element.getPropertyAttributes().getType() + " " + newVariableName + " = new " + element.getPropertyAttributes().getType() + "();");
						if (element.getPropertyAttributes().getSetMethodName() != null) {
							codes.add(variableName + "." + element.getPropertyAttributes().getSetMethodName() + "(" + newVariableName + ");");
						} else {
							codes.add(variableName + "." + element.getPropertyAttributes().getFieldName() + " = " + newVariableName + ";");
						}
					} else {
						newVariableName = variableName;
					}

					ClassGenerateDefinition generateDefinition = new InternalClassBuilder(element.getPropertyAttributes().getType(), element.getNestingChildren(), context)
							.buildUnmarshall();
					if (element.hasAttributes(context)) {
						if (element.getNameAttributes().getName() != null) {
							codes.add(Templates.parse("context.parseToStartTag($uri$, $structureName$);", newParameters));
						}

						codes.add(element.getPropertyAttributes().getType() + "." + generateDefinition.getUnmarshalAttrMethodName() + "(" + newVariableName + ", context);");
					}

					if (element.getNameAttributes().getName() != null) {
						appendStart = true;
						codes.add(Templates.parse("context.parsePastStartTag($uri$, $structureName$);", newParameters));
					}
					codes.add(element.getPropertyAttributes().getType() + "." + generateDefinition.getUnmarshalMethodName() + "(" + newVariableName + ", context);");
				}
			} else if (element.nestingchildSize() > 0) {
				if (element.getPropertyAttributes().getFieldName() == null && element.getPropertyAttributes().getSetMethodName() == null) {
					if (element.hasAttributes(context)) {
						if (element.getNameAttributes().getName() != null) {
							codes.add(Templates.parse("context.parseToStartTag($uri$, $structureName$);", newParameters));
						}
						for (ValueElement each : element.attributes(context)) {
							codes.addAll(new ValueCodeBuilder(each, context).buildUnmarshallAttr(variableName));
						}
					}
				}

				if (element.getNameAttributes().getName() != null) {
					appendStart = true;
					codes.add(Templates.parse("context.parsePastStartTag($uri$, $structureName$);", newParameters));
				}

				if (element.getPropertyAttributes().getFieldName() != null || element.getPropertyAttributes().getSetMethodName() != null) {
					String newVariableName = JibxUtils.newVariableName();
					codes.add(element.getPropertyAttributes().getType() + " " + newVariableName + " = new " + element.getPropertyAttributes().getType() + "();");
					if (element.getPropertyAttributes().getSetMethodName() != null) {
						codes.add(variableName + "." + element.getPropertyAttributes().getSetMethodName() + "(" + newVariableName + ");");
					} else {
						codes.add(variableName + "." + element.getPropertyAttributes().getFieldName() + " = " + newVariableName + ";");
					}

					InternalClassBuilder internalClassBuilder = new InternalClassBuilder(element.getPropertyAttributes().getType(), element.getNestingChildren(), context);
					ClassGenerateDefinition generateDefinition = internalClassBuilder.buildUnmarshall();
					codes.add(element.getPropertyAttributes().getType() + "." + generateDefinition.getUnmarshalMethodName() + "(" + newVariableName + ", context);");
				} else {
					if (!element.getStructureAttributes().isOrdered() && element.hasElements(context)) {
						String variable = "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
						codes.add("boolean " + variable + "[] = new boolean[" + element.nestingchildSize() + "];");
						codes.add("do{");
						int index = 0;
						for (Iterator<NestingChild> it = element.nestingChildIterator(); it.hasNext();) {
							NestingChild child = it.next();
							if (child instanceof ValueElement) {
								ValueElement valueElement = (ValueElement) child;
								codes.addAll(wrap(new ValueCodeBuilder(valueElement, context).buildUnmarshall(variableName), ((ValueElement) child).getNamespace(context),
										((ValueElement) child).getNameAttributes().getName(), variable, index));
							} else if (child instanceof StructureElement) {
								StructureElement structureElement = (StructureElement) child;
								codes.addAll(wrap(new StructureCodeBuilder(structureElement, context).buildUnmarshall(variableName, true),
										((StructureElement) child).getNamespace(context), ((StructureElement) child).getNameAttributes().getName(), variable, index));
							} else if (child instanceof CollectionElement) {
								CollectionElement collectionElement = (CollectionElement) child;
								codes.addAll(new CollectionCodeBuilder(collectionElement, context).buildUnmarshall());
							}
							index++;
						}
						codes.add("{");
						codes.add("break;");
						codes.add("}");
						codes.add("} while(true);");
					} else {
						for (Iterator<NestingChild> it = element.nestingChildIterator(); it.hasNext();) {
							NestingChild child = it.next();
							if (child instanceof ValueElement) {
								ValueElement valueElement = (ValueElement) child;
								codes.addAll(new ValueCodeBuilder(valueElement, context).buildUnmarshall(variableName));
							} else if (child instanceof StructureElement) {
								StructureElement structureElement = (StructureElement) child;
								codes.addAll(new StructureCodeBuilder(structureElement, context).buildUnmarshall(variableName, true));
							} else if (child instanceof CollectionElement) {
								CollectionElement collectionElement = (CollectionElement) child;
								codes.addAll(new CollectionCodeBuilder(collectionElement, context).buildUnmarshall());
							}
						}
					}
				}
			} else if (type != null) {
				codes.add("if(!context.isEnd()){");
				if (element.getPropertyAttributes().getSetMethodName() != null) {
					codes.add(variableName + "." + element.getPropertyAttributes().getSetMethodName() + "((" + type + ")context.unmarshalElement());");
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					codes.add(variableName + "." + element.getPropertyAttributes().getFieldName() + " = (" + type + ")context.unmarshalElement();");
				} else {
					codes.add(variableName + " = (" + type + ")context.unmarshalElement();");
				}
				codes.add("}");
			}

			if (appendStart) {
				codes.add(Templates.parse("context.parsePastCurrentEndTag($uri$, $structureName$);", newParameters));
			}

			if (appendIf) {
				codes.add("}");
			}
		} else {
			if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
				codes.add("do{");
				codes.add(Templates.parse("if(!context.isAt($uri$, $structureName$)){", parameters));
				codes.add("break;");
				codes.add("}");
				codes.add(Templates.parse("context.parsePastElement($uri$, $structureName$);", parameters));
				codes.add("} while(true);");
			} else {
				codes.add(Templates.parse("context.parsePastElement($uri$, $structureName$);", parameters));
			}
		}

		return codes;
	}

	public List<String> buildMarshall(String variableName, boolean handleAttributes) throws JiBXException {
		List<String> codes = new ArrayList<String>();

		String operation = variableName;
		if (element.getPropertyAttributes().getGetMethodName() != null) {
			operation = variableName + "." + element.getPropertyAttributes().getGetMethodName() + "()";
		} else if (element.getPropertyAttributes().getFieldName() != null) {
			operation = variableName + "." + element.getPropertyAttributes().getFieldName();
		}

		boolean appendIf = false;
		if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			appendIf = true;
			if (element.getPropertyAttributes().getTestMethodName() != null) {
				codes.add("if(" + variableName + "." + element.getPropertyAttributes().getTestMethodName() + "()){");
			} else {
				codes.add("if(" + operation + " != null){");
			}
		}

		String type = element.getPropertyAttributes().getType();
		if (type == null) {
			type = element.getMapAsName();
		}

		boolean appendStart = false;
		int nsIndex = element.getNamespaceIndex(context);
		if (element.getNameAttributes().getName() != null
				&& element.getObjectAttributes().getMarshallerName() == null
				&& (element.getPropertyAttributes().getUsage() == Usage.Required || element.getPropertyAttributes().getTestMethodName() != null || (type != null || element
						.nestingchildSize() > 0))) {
			TemplateElementBase template = element.getTemplate();
			if (element.hasNamespace(context) && template != null) {
				List<Integer> nsIndexes = template.getAllNamespaceIndexes(context);
				List<String> nses = template.getAllNamespacePrefixes(context);
				codes.add(Templates.parse("((org.jibx.runtime.impl.MarshallingContext)context).startTagNamespaces(" + nsIndex + ", $structureName$, new int[] {"
						+ Joiner.on(",").join(nsIndexes) + "}, new String[] {\"" + Joiner.on("\",\"").join(nses) + "\"});", parameters));
			} else {
				codes.add(Templates.parse("((org.jibx.runtime.impl.MarshallingContext)context).startTag(" + nsIndex + ", $structureName$);", parameters));
			}
			appendStart = true;
		}

		// TODO
		if ((type != null && !type.startsWith("java")) || "java.lang.Object".equals(type) || element.getObjectAttributes().getMarshallerName() != null) {
			if (element.nestingchildSize() == 0) {
				MappingElementBase mappingElement = context.getMappingElement(element.getMapAsName());
				if (mappingElement == null) {
					mappingElement = context.getMappingElement(type);
				}
				if (mappingElement != null) {
					type = mappingElement.getClassName();
					if (!mappingElement.isAbstract()) {
						codes.add("context.getMarshaller(\"" + type + "\").marshal(" + operation + ", context);");
					} else {
						ClassGenerateDefinition classDefinition = context.getClassDefinition(element.getMapAsName() == null ? type : element.getMapAsName(), type);
						if (classDefinition != null) {
							if (handleAttributes && mappingElement.hasAttributes(context)) {
								codes.add(type + "." + classDefinition.getMarshalAttrMethodName() + "((" + type + ")" + operation + ", context);");
								codes.add("context.closeStartContent();");
							}
							if (mappingElement.hasElements(context)) {
								codes.add(type + "." + classDefinition.getMarshalMethodName() + "((" + type + ")" + operation + ", context);");
							}
						}
					}
				} else {
					if (element.getObjectAttributes().getMarshallerName() != null) {
						String newVariableName = JibxUtils.newVariableName();

						if (element.getObjectAttributes().isSimpleMarshallerConstructor()) {
							codes.add(element.getObjectAttributes().getMarshallerName() + " " + newVariableName + " = new " + element.getObjectAttributes().getMarshallerName()
									+ "();");
						} else {
							codes.add(element.getObjectAttributes().getMarshallerName() + " " + newVariableName + " = new " + element.getObjectAttributes().getMarshallerName()
									+ "(\"" + element.getNamespace(context) + "\", " + element.getNamespaceIndex(context) + ",\"" + element.getNameAttributes().getName() + "\");");
						}

						if (element.getPropertyAttributes().getGetMethodName() != null) {
							codes.add(newVariableName + ".marshal(" + variableName + "." + element.getPropertyAttributes().getGetMethodName() + "(), context);");
						} else if (element.getPropertyAttributes().getFieldName() != null) {
							codes.add(newVariableName + ".marshal(" + variableName + "." + element.getPropertyAttributes().getFieldName() + ", context);");
						} else {
							codes.add(newVariableName + ".marshal(" + variableName + ", context);");
						}
					} else {
						if (element.getPropertyAttributes().getGetMethodName() != null) {
							codes.add("((org.jibx.runtime.IMarshallable)" + variableName + "." + element.getPropertyAttributes().getGetMethodName() + "()).marshal(context);");
						} else if (element.getPropertyAttributes().getFieldName() != null) {
							codes.add("((org.jibx.runtime.IMarshallable)" + variableName + "." + element.getPropertyAttributes().getFieldName() + ").marshal(context);");
						} else {
							codes.add("((org.jibx.runtime.IMarshallable)" + variableName + ").marshal(context);");
						}
					}
				}
			} else {
				ClassGenerateDefinition generateDefinition = new InternalClassBuilder(type, element.getNestingChildren(), context).buildMarshall();
				if (element.hasAttributes(context)) {
					codes.add(element.getPropertyAttributes().getType() + "." + generateDefinition.getMarshalAttrMethodName() + "((" + type + ")" + operation + ", context);");
					codes.add("context.closeStartContent();");
				}
				codes.add(element.getPropertyAttributes().getType() + "." + generateDefinition.getMarshalMethodName() + "((" + type + ")" + operation + ", context);");
			}
		} else {
			if (element.hasAttributes(context)) {
				for (ValueElement valueElement : element.attributes(context)) {
					codes.addAll(new ValueCodeBuilder(valueElement, context).buildMarshallAttr(variableName));
				}
				codes.add("context.closeStartContent();");
			}

			for (Iterator<NestingChild> it = element.nestingChildIterator(); it.hasNext();) {
				NestingChild child = it.next();
				if (child instanceof ValueElement) {
					ValueElement valueElement = (ValueElement) child;
					codes.addAll(new ValueCodeBuilder(valueElement, context).buildMarshall(variableName));
				} else if (child instanceof StructureElement) {
					StructureElement structureElement = (StructureElement) child;
					codes.addAll(new StructureCodeBuilder(structureElement, context).buildMarshall(variableName, true));
				} else if (child instanceof CollectionElement) {
					CollectionElement collectionElement = (CollectionElement) child;
					codes.addAll(new CollectionCodeBuilder(collectionElement, context).buildMarshall());
				}
			}
		}

		if (appendStart) {
			codes.add(Templates.parse("context.endTag(" + nsIndex + ", $structureName$);", parameters));
		}

		if (appendIf) {
			codes.add("}");
		}
		return codes;
	}

	protected List<String> wrap(List<String> codes, String namespace, String name, String variable, int index) {
		if (codes.isEmpty()) {
			return codes;
		}

		Map<String, String> parameters = new HashMap<String, String>();
		putParameter(parameters, "namespace", namespace);
		putParameter(parameters, "name", name);
		parameters.put("variable", variable);
		parameters.put("index", String.valueOf(index));

		List<String> newCodes = new ArrayList<String>();
		newCodes.add(Templates.parse("if(context.isAt($namespace$, $name$)){", parameters));
		newCodes.add(Templates.parse("if($variable$[$index$]){", parameters));
		newCodes.add(Templates.parse("context.throwNameException(\"Duplicate element \", $namespace$, $name$);", parameters));
		newCodes.add("}");
		newCodes.add(Templates.parse("$variable$[$index$] = true;", parameters));
		newCodes.addAll(codes);
		newCodes.add("} else ");
		return newCodes;
	}
}
