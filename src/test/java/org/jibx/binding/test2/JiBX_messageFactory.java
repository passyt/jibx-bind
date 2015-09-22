package org.jibx.binding.test2;

import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.impl.BindingFactoryBase;

public class JiBX_messageFactory extends BindingFactoryBase implements IBindingFactory {

	private JiBX_messageFactory() {
		super(
				"message",
				0,
				0,
				getClassList(),
				"org.jibx.binding.test2.Mapping|....TestMapping",
				"|org.jibx.binding.test2.JiBX_messageTestMapping_access",
				"|org.jibx.binding.test2.JiBX_messageTestMapping_access",
				new String[] { "", "http://www.w3.org/XML/1998/namespace", "http://www.w3.org/2001/XMLSchema-instance", "http://test/", "http://www/", "http://test.1/" },
				new String[] { "", "xml", "xsi", null, "b", "a" },
				"|Test",
				"\001\007",
				null,
				"org.jibx.binding.test2.Mapping|....|....Mapping.JiBX_message_newinstance_1_0|||.....JiBX_message_attrTest_1_0|.....JiBX_message_unmarshalAttr_1_0|.....JiBX_message_marshalAttr_1_0|||",
				"\003\004\005", "", "", "", new String[0]);
	}

	public int getCompilerVersion() {
		return 0x30000;
	}

	public String getCompilerDistribution() {
		return "jibx_1_2_3_SNAPSHOT";
	}

	public int getTypeIndex(String arg1) {
		return -1;
	}

	public static IBindingFactory getInstance() {
		if (m_inst == null)
			m_inst = new JiBX_messageFactory();
		return m_inst;
	}

	private static String getClassList() {
		return "org.jibx.binding.test2.JiBX_messageFactory|....JiBX_messageTestMapping_access|....Mapping|....TestMapping";
	}

	private static IBindingFactory m_inst;
}