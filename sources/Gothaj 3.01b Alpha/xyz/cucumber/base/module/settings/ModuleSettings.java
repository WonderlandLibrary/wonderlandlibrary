package xyz.cucumber.base.module.settings;

import java.util.function.Supplier;

public class ModuleSettings {
	
	String name;
	String category;
	Supplier<Boolean> visibility = () -> true;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public Supplier<Boolean> getVisibility() {
		return visibility;
	}
	public void setVisibility(Supplier<Boolean> visibility) {
		this.visibility = visibility;
	}
	
}
