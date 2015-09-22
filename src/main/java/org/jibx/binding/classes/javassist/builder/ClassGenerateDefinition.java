package org.jibx.binding.classes.javassist.builder;

import org.jibx.binding.util.Counter;
import org.jibx.binding.util.JibxUtils;

/**
 * 
 * @author Passyt
 *
 */
public class ClassGenerateDefinition {

	private final String newinstanceMethodName;
	private final String testMethodName;
	private final String attrTestMethodName;
	private final String marshalAttrMethodName;
	private final String marshalMethodName;
	private final String unmarshalMethodName;
	private final String unmarshalAttrMethodName;
	private final String className;

	private final int currentBindingId;
	private final int currentGenerateId;

	public ClassGenerateDefinition(String bindingName, String className) {
		super();
		this.className = className;

		currentBindingId = Counter.get().classId(bindingName, className);
		currentGenerateId = Counter.get().classMethodId(bindingName, className);

		this.newinstanceMethodName = JibxUtils.newInstanceMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.testMethodName = JibxUtils.testMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.attrTestMethodName = JibxUtils.attrTestMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.marshalAttrMethodName = JibxUtils.marshalAttrMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.marshalMethodName = JibxUtils.marshalMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.unmarshalMethodName = JibxUtils.unmarshalMethodName(bindingName, className, currentBindingId, currentGenerateId);
		this.unmarshalAttrMethodName = JibxUtils.unmarshalAttrMethodName(bindingName, className, currentBindingId, currentGenerateId);
	}

	public String getNewinstanceMethodName() {
		return newinstanceMethodName;
	}

	public String getTestMethodName() {
		return testMethodName;
	}

	public String getMarshalAttrMethodName() {
		return marshalAttrMethodName;
	}

	public String getMarshalMethodName() {
		return marshalMethodName;
	}

	public String getUnmarshalMethodName() {
		return unmarshalMethodName;
	}

	public String getUnmarshalAttrMethodName() {
		return unmarshalAttrMethodName;
	}

	public String getAttrTestMethodName() {
		return attrTestMethodName;
	}

	public String getClassName() {
		return className;
	}

	public int getCurrentBindingId() {
		return currentBindingId;
	}

	public int getCurrentGenerateId() {
		return currentGenerateId;
	}

}
