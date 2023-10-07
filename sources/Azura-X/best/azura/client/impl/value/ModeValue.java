package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.api.value.dependency.Dependency;

public class ModeValue extends Value<String> {

	public ModeValue(String name, String description, String mode, String... modes) {
		super(name, description, mode, modes);
	}

	public ModeValue(String name, String description, Dependency dependency, String mode, String... modes) {
		super(name, description, dependency, mode, modes);
	}

	public ModeValue(String name, String description, ValueChangeCallback callback, String mode, String... modes) {
		super(name, description, callback, mode, modes);
	}

	public ModeValue(String name, String description, ValueChangeCallback callback, Dependency dependency, String mode, String... modes) {
		super(name, description, callback, dependency, mode, modes);
	}

	public void switchToNextMode() {
		int index = 0;
		int searched = 0;
		for(String s : getObjects()) {
			if (s.equals(getObject())) searched = index;
			index++;
		}
		if(searched++ >= getObjects().length - 1) searched = 0;
		setObject(getObjects()[searched]);
	}

	public void switchToPreviousMode() {
		int index = 0;
		int searched = 0;
		for(String s : getObjects()) {
			if (s.equals(getObject())) searched = index;
			index++;
		}
		if(searched-- <= 0) searched = getObjects().length - 1;
		setObject(getObjects()[searched]);
	}
}