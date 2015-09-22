package org.jibx.binding;

import org.jibx.binding.model.BindingElement;
import org.jibx.runtime.JiBXException;

/**
 * 
 * @author Passyt
 *
 */
public interface IBuildFactory<ClassFile extends IClassFile<? extends IConstructor, ? extends IMethod, ? extends IField>> {

	/**
	 * 
	 */
	void init(BindingElement bindingElement) throws JiBXException;

	/**
	 * 
	 * @return
	 */
	IFactoryBuilder getFactoryBuilder();

	/**
	 * 
	 * @return
	 */
	IMappingBuilder getMappingBuilder();

	/**
	 * 
	 * @param className
	 * @return
	 */
	boolean existClass(String className);

	/**
	 * 
	 * @param className
	 * @return
	 */
	ClassFile getClassFile(String className) throws Exception;

	/**
	 * 
	 * @param className
	 * @return
	 */
	ClassFile makeClassFile(String className) throws Exception;
}
