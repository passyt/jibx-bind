package org.jibx.binding.classes.javassist.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.binding.model.NestingChild;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.Usage;
import org.jibx.binding.model.ValueElement;
import org.jibx.binding.model.attributes.PropertyAttributes;
import org.jibx.binding.util.JibxUtils;
import org.jibx.binding.util.Templates;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class CollectionCodeBuilder extends AbstractBuilder {

	private final CollectionElement element;
	private final IBindingContext context;
	private final Map<String, String> parameters;

	public CollectionCodeBuilder(CollectionElement element, IBindingContext context) {
		super();
		this.element = element;
		this.context = context;
		this.parameters = initParameters();
	}

	protected Map<String, String> initParameters() {
		Map<String, String> parameters = new HashMap<String, String>();
		putParameter(parameters, "uri", element.getNamespace(context));
		putParameter(parameters, "name", element.getNameAttributes().getName());
		parameters.put("nsIndex", String.valueOf(element.getNamespaceIndex(context)));
		parameters.put("fieldName", element.getPropertyAttributes().getFieldName());
		parameters.put("setMethodName", element.getPropertyAttributes().getSetMethodName());
		parameters.put("getMethodName", element.getPropertyAttributes().getGetMethodName());
		parameters.put("iterMethodName", element.getIterMethodName());
		parameters.put("addMethodName", element.getAddMethodName());
		parameters.put("sizeMethodName", element.getSizeMethodName());
		parameters.put("testMethodName", element.getPropertyAttributes().getTestMethodName());
		return parameters;
	}

	public List<String> buildUnmarshall() throws JiBXException {
		List<String> codes = new ArrayList<String>();
		String collectionVariableName = JibxUtils.newVariableName();
		String createType = null;
		if (element.getObjectAttributes().getCreateType() != null) {
			createType = element.getObjectAttributes().getCreateType();
			codes.add(element.getObjectAttributes().getCreateType() + " " + collectionVariableName + " = null;");
		} else {
			createType = element.getPropertyAttributes().getType();
			codes.add(element.getPropertyAttributes().getType() + " " + collectionVariableName + " = null;");
		}

		boolean appendIf = false;
		String itemTypeClassName = element.getItemTypeName();
		if (itemTypeClassName != null) {
			MappingElementBase mappingElement = context.getMappingElement(itemTypeClassName);
			if (mappingElement != null) {
				itemTypeClassName = mappingElement.getClassName();
			}
		}
		if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			if (element.getNameAttributes().getName() != null) {
				codes.add(Templates.parse("if(context.isAt($uri$, $name$)){", parameters));
				appendIf = true;
			}
		}

		if (element.getObjectAttributes().getFactoryName() != null) {
			codes.add(collectionVariableName + " = " + element.getObjectAttributes().getFactoryName() + "();");
		} else if (createType != null) {
			String mugeAdapterClassName = context.getMugeAdapterClassName();
			codes.add(collectionVariableName + " =  (" + createType + ")" + mugeAdapterClassName + ".newInstance(\"" + createType + "\");");
		}

		if (element.getNameAttributes().getName() != null) {
			codes.add(Templates.parse("context.parsePastStartTag($uri$, $name$);", parameters));
		}

		if (element.nestingchildSize() == 0) {
			if (itemTypeClassName != null) {
				codes.add("while(context.getUnmarshaller(\"" + itemTypeClassName + "\").isPresent(context)){");
			} else {
				codes.add("while(!context.isEnd()){");
			}
		} else {
			NestingChild child = element.nestingChildIterator().next();
			Map<String, String> tempParameters = new HashMap<String, String>();
			putParameter(tempParameters, "uri", child.getNamespace(context));
			putParameter(tempParameters, "name", child.getNameAttributes().getName());

			if (child.getNameAttributes().getName() != null) {
				codes.add(Templates.parse("while(context.isAt($uri$, $name$)){", tempParameters));
			} else {
				codes.add("while(context.getUnmarshaller(\"" + itemTypeClassName + "\").isPresent(context)){");
			}
		}
		String itemVariableName = JibxUtils.newVariableName();
		if (itemTypeClassName != null) {
			if (element.isExistDefaultConstructorOnItemType()) {
				codes.add(itemTypeClassName + " " + itemVariableName + " = new " + itemTypeClassName + "();");
			} else if (!itemTypeClassName.equals(element.getItemTypeName())) {
				codes.add(itemTypeClassName + " " + itemVariableName + " = new " + itemTypeClassName + "();");
			} else {
				codes.add(itemTypeClassName + " " + itemVariableName + " = null;");
			}
		} else {
			if (element.nestingchildSize() > 0 && element.nestingChildIterator().next() instanceof StructureElement
					&& ((StructureElement) element.nestingChildIterator().next()).getPropertyAttributes().getType() != null) {
				StructureElement structureElement = (StructureElement) element.nestingChildIterator().next();
				codes.add(structureElement.getPropertyAttributes().getType() + " " + itemVariableName + " = new " + structureElement.getPropertyAttributes().getType() + "();");
			} else {
				codes.add("Object " + itemVariableName + " = null;");
			}
		}

		if (element.nestingchildSize() > 0) {
			NestingChild child = element.nestingChildIterator().next();
			if (child instanceof ValueElement) {
				codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildUnmarshall(itemVariableName));
			} else if (child instanceof StructureElement) {
				StructureElement structureElement = (StructureElement) child;
				if (structureElement.getPropertyAttributes().getType() == null && itemTypeClassName != null) {
					structureElement.getPropertyAttributes().setType(itemTypeClassName);
				}

				codes.addAll(new StructureCodeBuilder(structureElement, context).buildUnmarshall(itemVariableName, true));
			} else if (child instanceof CollectionElement) {
				codes.addAll(new CollectionCodeBuilder((CollectionElement) child, context).buildUnmarshall());
			}
		} else if (itemTypeClassName != null) {
			codes.add(itemVariableName + " = context.getUnmarshaller(\"" + itemTypeClassName + "\").unmarshal(" + itemVariableName + ", context);");
		} else {
			codes.add(itemVariableName + " = context.unmarshalElement();");
		}

		if (element.getAddMethodName() != null) {
			codes.add(Templates.parse("object.$addMethodName$(" + itemVariableName + ");", parameters));
		} else {
			codes.add(Templates.parse(collectionVariableName + ".add(" + itemVariableName + ");", parameters));
		}
		codes.add("}");

		if (element.getNameAttributes().getName() != null) {
			codes.add(Templates.parse("context.parsePastCurrentEndTag($uri$, $name$);", parameters));
		} else if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			codes.add("if(" + collectionVariableName + "!=null && " + collectionVariableName + ".size() == 0){");
			codes.add(collectionVariableName + " = null;");
			codes.add("}");
		}

		if (appendIf) {
			codes.add("} ");
		}

		if (element.getPropertyAttributes().getSetMethodName() != null) {
			codes.add(Templates.parse("object.$setMethodName$(" + collectionVariableName + ");", parameters));
		} else if (element.getPropertyAttributes().getFieldName() != null) {
			codes.add(Templates.parse("object.$fieldName$ = " + collectionVariableName + ";", parameters));
		}
		return codes;
	}

	public List<String> buildMarshall() throws JiBXException {
		List<String> codes = new ArrayList<String>();
		if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			if (element.getPropertyAttributes().getTestMethodName() != null) {
				codes.add(Templates.parse("if(object.$testMethodName$()){", parameters));
			} else if (element.getPropertyAttributes().getGetMethodName() != null) {
				codes.add(Templates.parse("if(object.$getMethodName$() != null){", parameters));
			} else if (element.getPropertyAttributes().getFieldName() != null) {
				codes.add(Templates.parse("if(object.$fieldName$ != null){", parameters));
			}
		}

		if (element.getNameAttributes().getName() != null) {
			codes.add(Templates.parse("context.startTag($nsIndex$, $name$);", parameters));
		}

		if (element.getIterMethodName() != null) {
			codes.add(Templates.parse("for(java.util.Iterator iterator = object.$iterMethodName$(); iterator.hasNext();){", parameters));
		} else if (element.getPropertyAttributes().getGetMethodName() != null) {
			codes.add(Templates.parse("for(java.util.Iterator iterator = object.$getMethodName$().iterator(); iterator.hasNext();){", parameters));
		} else if (element.getPropertyAttributes().getFieldName() != null) {
			codes.add(Templates.parse("for(java.util.Iterator iterator = object.$fieldName$.iterator(); iterator.hasNext();){", parameters));
		}
		String itemVariableName = JibxUtils.newVariableName();
		String itemTypeClassName = element.getItemTypeName();
		if (itemTypeClassName != null) {
			MappingElementBase mappingElement = context.getMappingElement(itemTypeClassName);
			if (mappingElement != null) {
				itemTypeClassName = mappingElement.getClassName();
			}

			codes.add(itemTypeClassName + " " + itemVariableName + " = (" + itemTypeClassName + ")iterator.next();");
		} else {
			codes.add("Object " + itemVariableName + " = iterator.next();");
		}

		if (element.nestingchildSize() == 0) {
			if (itemTypeClassName != null) {
				StructureElement structureElement = new StructureElement();
				structureElement.setPropertyAttributes(new PropertyAttributes(itemTypeClassName));

				codes.addAll(new StructureCodeBuilder(structureElement, context).buildMarshall(itemVariableName, true));
			} else {
				codes.add("((org.jibx.runtime.IMarshallable)" + itemVariableName + ").marshal(context);");
			}
		} else {
			for (Iterator<NestingChild> it = element.nestingChildIterator(); it.hasNext();) {
				NestingChild child = it.next();
				if (child instanceof ValueElement) {
					codes.addAll(new ValueCodeBuilder((ValueElement) child, context).buildMarshall(itemVariableName));
				} else if (child instanceof StructureElement) {
					StructureElement structureElement = (StructureElement) child;
					if (structureElement.getPropertyAttributes().getType() == null && itemTypeClassName != null) {
						structureElement.getPropertyAttributes().setType(itemTypeClassName);
					}

					codes.addAll(new StructureCodeBuilder(structureElement, context).buildMarshall(itemVariableName, true));
				} else if (child instanceof CollectionElement) {
					codes.addAll(new CollectionCodeBuilder((CollectionElement) child, context).buildMarshall());
				}
			}
		}

		codes.add("}");

		if (element.getNameAttributes().getName() != null) {
			codes.add(Templates.parse("context.endTag($nsIndex$, $name$);", parameters));
		}

		if (Usage.Optional == element.getPropertyAttributes().getUsage()) {
			codes.add("}");
		}
		return codes;
	}
}
