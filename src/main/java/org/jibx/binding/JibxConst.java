package org.jibx.binding;

/**
 * 
 * @author Passyt
 *
 */
public interface JibxConst {

	/** Prefix used in all code generation for methods and classes. */
	String GENERATE_PREFIX = "JiBX_";
	/** Name of <code>String[]</code> field giving binding factory name list. */
	String BINDINGLIST_NAME = "JiBX_bindingList";
	/** Suffix of binding factory name. */
	String BINDINGFACTORY_SUFFIX = "Factory";
	String MUNGEADAPTER_SUFFIX = "MungeAdapter";
	/** Binding factory method to get instance of factory. */
	String FACTORY_INSTMETHOD = "getInstance";
	/**
	 * 
	 */
	String COMPILER_DISTRIBUTION = "jibx_1_2_3_Derby";
	/**
	 * Current binary version number. This is a byte-ordered value, allowing for
	 * two levels of major and two levels of minor version.
	 */
	int CURRENT_VERSION_NUMBER = 0x00030000;
	String ADAPTERCLASS_SUFFIX = "_access";

}