package org.jibx.binding.model;

/**
 * 
 * @author Passyt
 *
 */
public enum BindingDirection {

	Input(true, false), Output(false, true), Both(true, true);

	private final boolean input;
	private final boolean output;

	private BindingDirection(boolean input, boolean output) {
		this.input = input;
		this.output = output;
	}

	public boolean isInput() {
		return input;
	}

	public boolean isOutput() {
		return output;
	}

}
