package org.jibx.binding.classes.javassist;

import java.util.Iterator;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;
import javassist.Modifier;
import javassist.NotFoundException;

import org.jibx.binding.IBindingContext;
import org.jibx.binding.IBuildFactory;
import org.jibx.binding.IFactoryBuilder;
import org.jibx.binding.IMappingBuilder;
import org.jibx.binding.classes.javassist.builder.FactoryClassBuilder;
import org.jibx.binding.classes.javassist.builder.MappingClassBuilder;
import org.jibx.binding.model.BindingChild;
import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.CollectionElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.binding.model.NestingChild;
import org.jibx.binding.model.NestingElementBase;
import org.jibx.binding.model.StructureElement;
import org.jibx.binding.model.ValueElement;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public class JavassistBuildFactory implements IBuildFactory<JavassistClassFile> {

	private final ClassPool classPool;
	private final boolean debug;

	public JavassistBuildFactory() {
		this(ClassPool.getDefault(), false);
	}
	
	public JavassistBuildFactory(String a, int b, String c){
		this(ClassPool.getDefault(), false);
	}

	public JavassistBuildFactory(ClassPool classPool, boolean debug) {
		this.classPool = classPool;
		this.classPool.importPackage("org.jibx.runtime");
		this.debug = debug;
	}

	@Override
	public void init(BindingElement bindingElement) throws JiBXException {
		for (Iterator<BindingChild> iterator = bindingElement.bindingChildIterator(false); iterator.hasNext();) {
			BindingChild bindingChild = iterator.next();
			if (bindingChild instanceof MappingElementBase) {
				MappingElementBase mappingElement = (MappingElementBase) bindingChild;
				init(mappingElement.getClassName(), mappingElement);
			}
		}
	}

	protected void init(String parentClassName, NestingElementBase element) throws JiBXException {
		for (Iterator<NestingChild> iterator = element.nestingChildIterator(); iterator.hasNext();) {
			NestingChild child = iterator.next();
			if (child instanceof CollectionElement) {
				init(parentClassName, (CollectionElement) child);
			} else if (child instanceof StructureElement) {
				init(parentClassName, ((StructureElement) child));
			} else if (child instanceof ValueElement) {
				init(parentClassName, (ValueElement) child);
			}
		}
	}

	protected void init(String parentClassName, CollectionElement element) throws JiBXException {
		if (element.getObjectAttributes().getCreateType() == null && element.getPropertyAttributes().getType() == null) {
			String factoryName = element.getObjectAttributes().getFactoryName();
			String createType = null;
			try {
				if (factoryName != null) {
					factoryName = factoryName.trim();
					String className = factoryName.substring(0, factoryName.lastIndexOf("."));
					String methodName = factoryName.substring(factoryName.lastIndexOf(".") + 1);
					CtClass ctClass = classPool.get(className);
					for (CtMethod method : ctClass.getDeclaredMethods()) {
						if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers())) {
							createType = method.getReturnType().getName();
							break;
						}
					}
				} else if (element.getPropertyAttributes().getGetMethodName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					for (CtMethod method : ctClass.getDeclaredMethods()) {
						if (method.getName().equals(element.getPropertyAttributes().getGetMethodName())) {
							createType = method.getReturnType().getName();
							break;
						}
					}
				} else if (element.getPropertyAttributes().getSetMethodName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					for (CtMethod method : ctClass.getDeclaredMethods()) {
						if (method.getName().equals(element.getPropertyAttributes().getSetMethodName())) {
							createType = method.getParameterTypes()[0].getName();
							break;
						}
					}
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					CtField field = ctClass.getField(element.getPropertyAttributes().getFieldName());
					createType = field.getType().getName();
				}
			} catch (NotFoundException e) {
				throw new JiBXException(factoryName + " is invalide", e);
			}

			if (createType == null) {
				throw new IllegalStateException("colleciton type is not found by " + element);
			}
			element.getObjectAttributes().setCreateType(createType);
		}

		if (element.getItemTypeName() == null) {
			if (element.nestingchildSize() == 1) {
				NestingChild child = element.nestingChildIterator().next();
				if (child instanceof StructureElement) {
					StructureElement structureElement = (StructureElement) child;
					if (structureElement.getMapAsName() != null) {
						element.setItemTypeName(structureElement.getMapAsName());
					} else if (structureElement.getPropertyAttributes().getType() != null) {
						element.setItemTypeName(structureElement.getPropertyAttributes().getType());
					}
				}
			}
		}

		init(element.getItemTypeName(), (NestingElementBase) element);

		if (element.getItemTypeName() != null) {
			try {
				CtClass ctClass = classPool.get(element.getItemTypeName());
				ctClass.getConstructor(CtNewConstructor.defaultConstructor(ctClass).getSignature());
			} catch (Exception e) {
				element.setExistDefaultConstructorOnItemType(false);
				setType(element, element.getItemTypeName());
			}
		}
	}

	protected void setType(NestingElementBase element, String type) {
		if (element.nestingchildSize() > 0) {
			for (NestingChild child : element.getNestingChildren()) {
				if (child instanceof ValueElement) {
					ValueElement valueElement = (ValueElement) child;
					if (valueElement.getPropertyAttributes().getType() == null) {
						valueElement.getPropertyAttributes().setType(type);
					}
					return;
				} else if (child instanceof StructureElement) {
					setType((StructureElement) child, type);
				}
			}
		}
	}

	protected void init(String parentClassName, StructureElement element) throws JiBXException {
		String type = element.getPropertyAttributes().getType();
		if (type == null) {
			try {
				if (element.getPropertyAttributes().getGetMethodName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					for (CtMethod method : ctClass.getDeclaredMethods()) {
						if (method.getName().equals(element.getPropertyAttributes().getGetMethodName())) {
							type = method.getReturnType().getName();
							break;
						}
					}
				} else if (element.getPropertyAttributes().getSetMethodName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					for (CtMethod method : ctClass.getDeclaredMethods()) {
						if (method.getName().equals(element.getPropertyAttributes().getSetMethodName())) {
							type = method.getParameterTypes()[0].getName();
							break;
						}
					}
				} else if (element.getPropertyAttributes().getFieldName() != null) {
					CtClass ctClass = classPool.get(parentClassName);
					CtField field = ctClass.getField(element.getPropertyAttributes().getFieldName());
					type = field.getType().getName();
				}

				element.getPropertyAttributes().setType(type);
			} catch (NotFoundException e) {
				throw new JiBXException(parentClassName + " is invalide", e);
			}
		}
		
		element.getObjectAttributes().setSimpleUnmarshallerConstructor(isSimpleConstructor(element.getObjectAttributes().getUnmarshallerName()));
		element.getObjectAttributes().setSimpleMarshallerConstructor(isSimpleConstructor(element.getObjectAttributes().getMarshallerName()));
		
		if (type != null) {
			init(type, (NestingElementBase) element);
		} else {
			init(parentClassName, (NestingElementBase) element);
		}
	}

	private boolean isSimpleConstructor(String className) {
		if (className == null) {
			return true;
		}
		
		try {
			CtClass clazz = classPool.get(className);
			clazz.getConstructor("(Ljava/lang/String;ILjava/lang/String;)V");
			return false;
		} catch (NotFoundException e) {
			return true;
		}
	}

	protected void init(String parentClassName, ValueElement element) throws JiBXException {
		if (element.getPropertyAttributes().getType() != null || parentClassName == null) {
			return;
		}

		String type = null;
		try {
			if (element.getPropertyAttributes().getGetMethodName() != null) {
				CtClass ctClass = classPool.get(parentClassName);
				for (CtMethod method : ctClass.getDeclaredMethods()) {
					if (method.getName().equals(element.getPropertyAttributes().getGetMethodName())) {
						type = method.getReturnType().getName();
						break;
					}
				}
			} else if (element.getPropertyAttributes().getSetMethodName() != null) {
				CtClass ctClass = classPool.get(parentClassName);
				for (CtMethod method : ctClass.getDeclaredMethods()) {
					if (method.getName().equals(element.getPropertyAttributes().getSetMethodName())) {
						type = method.getParameterTypes()[0].getName();
						break;
					}
				}
			} else if (element.getPropertyAttributes().getFieldName() != null) {
				CtClass ctClass = classPool.get(parentClassName);
				CtField field = ctClass.getField(element.getPropertyAttributes().getFieldName());
				type = field.getType().getName();
			}
		} catch (NotFoundException e) {
			throw new JiBXException(parentClassName + " is invalide", e);
		}

		element.getPropertyAttributes().setType(type);
	}

	@Override
	public boolean existClass(String className) {
		try {
			classPool.get(className);
			return true;
		} catch (NotFoundException e) {
			return false;
		}
	}

	@Override
	public JavassistClassFile getClassFile(String className) throws Exception {
		CtClass clazz = classPool.get(className);
		return new JavassistClassFile(classPool, clazz, debug);
	}

	@Override
	public JavassistClassFile makeClassFile(String className) throws Exception {
		CtClass clazz = classPool.makeClass(className);
		return new JavassistClassFile(classPool, clazz, debug);
	}

	public ClassPool getClassPool() {
		return classPool;
	}

	@Override
	public IFactoryBuilder getFactoryBuilder() {
		return new JavassistFactoryBuilder();
	}

	@Override
	public IMappingBuilder getMappingBuilder() {
		return new MappingBuilder();
	}

	private static class JavassistFactoryBuilder implements IFactoryBuilder {

		@Override
		public void build(IBindingContext context) throws JiBXException {
			new FactoryClassBuilder(context).build();
		}

	}

	private static class MappingBuilder implements IMappingBuilder {

		@Override
		public void build(MappingElementBase mappingElement, IBindingContext context) throws JiBXException {
			new MappingClassBuilder(mappingElement, context).build();
		}

	}
	
}
