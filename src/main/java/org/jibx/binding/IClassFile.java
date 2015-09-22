package org.jibx.binding;

import java.io.OutputStream;

/**
 * 
 * @author Passyt
 *
 */
public interface IClassFile<C extends IConstructor, M extends IMethod, F extends IField> {

	/**
	 * 
	 * @return
	 */
	String getClassName();

	/**
	 * 
	 * @param className
	 */
	void setSuperclass(String className) throws Exception;

	/**
	 * 
	 * @param interfaceClassNames
	 */
	void addInterfaces(String... interfaceClassNames) throws Exception;

	/**
	 * 
	 * @param constructor
	 */
	void addConstructor(C constructor) throws Exception;

	/**
	 * 
	 * @param method
	 */
	void addMethod(M method) throws Exception;

	/**
	 * 
	 * @param field
	 */
	void addField(F field) throws Exception;

	/**
	 * 
	 * @param directory
	 */
	void write(String directory) throws Exception;

	/**
	 * 
	 * @param out
	 */
	void write(OutputStream out) throws Exception;

	/**
	 * 
	 * @return
	 */
	Class<?> toClass() throws Exception;

}
