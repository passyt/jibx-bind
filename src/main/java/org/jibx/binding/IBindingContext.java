package org.jibx.binding;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jibx.binding.classes.javassist.builder.ClassGenerateDefinition;
import org.jibx.binding.classes.javassist.builder.NamespaceDefinition;
import org.jibx.binding.model.BindingElement;
import org.jibx.binding.model.MappingElementBase;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public interface IBindingContext {

	/**
	 * report binding details and results
	 * 
	 * @param verbose
	 * @return
	 */
	boolean isVerbose();

	/**
	 * 
	 * @return
	 */
	boolean isDebug();

	/**
	 * 
	 * @return
	 */
	Collection<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> generateCode() throws JiBXException;

	/**
	 * 
	 * @return
	 */
	Collection<IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> getClassFiles();

	/**
	 * 
	 * @param typeName
	 * @param elementName
	 * @param umarshallClassName
	 * @param marshallClassName
	 */
	void addMappingClassItem(String typeName, String elementName, String umarshallClassName, String marshallClassName, Integer nsIndex);

	/**
	 * 
	 * @return
	 */
	String getMugeAdapterClassName();

	/**
	 * 
	 * @param className
	 * @return
	 */
	boolean isBuild(String className);

	/**
	 * 
	 * @param typeName
	 * @param className
	 * @param createMethod
	 * @param completeMethod
	 * @param prepareMethod
	 * @param attributePresentTestMethod
	 * @param attributeUnmarshalMethod
	 * @param attributeMarshalMethod
	 * @param contentPresentTestMethod
	 * @param contentUnmarshalMethod
	 * @param contentMarshalMethod
	 * @param nsIndexes
	 */
	void addAbstractMappingClassItem(String typeName, String className, String createMethod, String completeMethod, String prepareMethod, String attributePresentTestMethod,
			String attributeUnmarshalMethod, String attributeMarshalMethod, String contentPresentTestMethod, String contentUnmarshalMethod, String contentMarshalMethod,
			List<Integer> nsIndexes);

	/**
	 * 
	 * @param classFile
	 */
	void addClassFile(IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField> classFile);

	/**
	 * 
	 * @return
	 */
	BindingElement getBindingRoot();

	/**
	 * 
	 * @param typeName
	 * @return
	 */
	MappingElementBase getMappingElement(String typeName);

	/**
	 * 
	 * @param className
	 * @return
	 */
	ClassGenerateDefinition getClassDefinition(String typeName, String className);

	/**
	 * 
	 * @param className
	 * @return
	 */
	ClassGenerateDefinition nextClassDefinition(String className);

	/**
	 * 
	 * @return
	 */
	String currentDefaultElementNamespace();

	/**
	 * 
	 * @return
	 */
	String currentDefaultAttributeNamespace();

	/**
	 * 
	 * @return
	 */
	Map<String, NamespaceDefinition> getNamespaces();

	/**
	 * 
	 * @return
	 */
	String getOutputPath();

	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	IBuildFactory getBuildFactory();

	/**
	 * 
	 * @return
	 */
	String getMappednames();

	/**
	 * 
	 * @return
	 */
	String getUmarnames();

	/**
	 * 
	 * @return
	 */
	String getMarnames();

	/**
	 * 
	 * @return
	 */
	String getGmapnames();

	/**
	 * 
	 * @return
	 */
	List<Integer> getGmapuris();

	/**
	 * 
	 * @return
	 */
	String getAbmapdetails();

	/**
	 * 
	 * @return
	 */
	List<Integer> getAbmapnss();

	/**
	 * 
	 * @param mapping
	 */
	void startMapping(MappingElementBase mapping);

	/**
	 * 
	 * @param mapping
	 */
	void endMapping(MappingElementBase mapping);

}
