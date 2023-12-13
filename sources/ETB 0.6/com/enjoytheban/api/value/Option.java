package com.enjoytheban.api.value;

public class Option<V> extends Value<V> {

	/**
	 * issa option
	 * @param displayName
	 * @param name
	 * @param enabled
	 */
	
	public Option(String displayName, String name, V enabled) {
		super(displayName, name);
		this.setValue(enabled);
	}
}