package xyz.cucumber.base.module.settings;

import java.util.function.Supplier;

public class StringSettings extends ModuleSettings {
	
	private String string;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public StringSettings(String name, String string) {
		this.name = name;
		this.category = null;
		this.string = string;
	}
	public StringSettings(String name,Supplier<Boolean> visibility, String string) {
		this.name = name;
		this.category = null;
		this.visibility = visibility;
		this.string = string;
	}
	public StringSettings(String category,String name, String string) {
		this.name = name;
		this.category = category;
		this.string = string;
	}
}
