package org.jibx.binding.classes.javassist.builder;

import org.jibx.binding.model.NamespaceStyle;

/**
 * 
 * @author Passyt
 *
 */
public class NamespaceDefinition {

	private String uri;
	private String prefix;
	private NamespaceStyle defaultStyle = NamespaceStyle.None;
	private int index;

	public NamespaceDefinition() {
		super();
	}

	public NamespaceDefinition(String uri, String prefix, NamespaceStyle defaultStyle, int index) {
		super();
		this.uri = uri;
		this.prefix = prefix;
		this.defaultStyle = defaultStyle;
		this.index = index;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public NamespaceStyle getDefaultStyle() {
		return defaultStyle;
	}

	public void setDefaultStyle(NamespaceStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
