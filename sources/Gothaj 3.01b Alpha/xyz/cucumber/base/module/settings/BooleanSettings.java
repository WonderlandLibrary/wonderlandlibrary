package xyz.cucumber.base.module.settings;

import java.util.function.Supplier;

public class BooleanSettings extends ModuleSettings{
	
	private boolean enabled;
	

	public BooleanSettings(String name, boolean enabled) {
		this.enabled = enabled;
		this.category = null;
		this.name = name;
	}
	public BooleanSettings(String name,Supplier<Boolean> visibility, boolean enabled) {
		this.enabled = enabled;
		this.category = null;
		this.name = name;
		this.visibility = visibility;
	}
	public BooleanSettings(String category,String name, boolean enabled) {
		this.enabled = enabled;
		this.category = category;
		this.name = name;
	
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	
}
