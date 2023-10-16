package com.enjoytheban.api.value;

public abstract class Value<V> {

	/**
	 * @author Purity
	 * Standard value system
	 */
	
	//Creating variables
	private String displayName, name;
	private V value;

	//Constructor @param displayName, name
	public Value(String displayName, String name) {
		this.displayName = displayName;
		this.name = name;
	}

	//Returns the values "display" name
	public String getDisplayName() {
		return displayName;
	}

	//Returns the values actual name
	public String getName() {
		return name;
	}

	//Returns the value
	public V getValue() {
		return value;
	}

	//Set the value
	public void setValue(V value) {
		this.value = value;
	}
}