package com.enjoytheban.api.value;

public class Mode<V extends Enum> extends Value<V> {

	//Creates a new variable that can hold multiple modes
	private V[] modes;

	/***
	 * issa constructor
	 * @param displayName
	 * @param name
	 * @param modes
	 * @param value
	 */
	public Mode(String displayName, String name, V[] modes, V value) {
		super(displayName, name);
		this.modes = modes;
		this.setValue(value);
	}

	//Returns the mode
	public V[] getModes() {
		return modes;
	}

	//Not true
	//Returns the mode assa string
	public String getModeAsString() {
		return ((Enum) this.getValue()).name();
	}

	//Sets the mode
	public void setMode(String mode) {
		for (Enum e : (Enum[]) this.modes) {
			if (!e.name().equalsIgnoreCase(mode)) {
				continue;
			}
			this.setValue((V) e);
		}
	}
	
	//Check to see if the mode is a valid mode
	public boolean isValid(String name) {
		for (Enum e : (Enum[]) this.modes) {
			if (!e.name().equalsIgnoreCase(name)) {
				continue;
			}
			return true;
		}
		return false;
	}
}