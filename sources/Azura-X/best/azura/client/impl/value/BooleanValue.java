package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.api.value.dependency.Dependency;

public class BooleanValue extends Value<Boolean> {
	
	public BooleanValue(String name, String description, Boolean object) {
		super(name, description, object, object);
	}

	public BooleanValue(String name, String description, ValueChangeCallback callback, Boolean object) {
        super(name, description, callback, object, object);
    }

    public BooleanValue(String name, String description, Dependency dependency, boolean object) {
        super(name, description, dependency, object, object);
    }

    public BooleanValue(String name, String description, ValueChangeCallback callback, Dependency dependency, boolean object) {
        super(name, description, callback, dependency, object, object);
    }
}