package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.util.color.HSBColor;
import best.azura.client.api.value.dependency.Dependency;

public class ColorValue extends Value<HSBColor> {

	public boolean collapsed = false;
	
	public ColorValue(String name, String description, HSBColor object) {
		super(name, description, object, object);
	}

	public ColorValue(String name, String description, Dependency dependency, HSBColor object) {
		super(name, description, dependency, object, object);
	}

	public ColorValue(String name, String description, ValueChangeCallback callback, HSBColor object) {
		super(name, description, callback, object, object);
	}

	public ColorValue(String name, String description, ValueChangeCallback callback, Dependency dependency, HSBColor object) {
		super(name, description, callback, dependency, object, object);
	}

}