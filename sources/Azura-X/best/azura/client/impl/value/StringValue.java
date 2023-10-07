package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.api.value.dependency.Dependency;

public class StringValue extends Value<String> {

    public boolean collapsed = false;

    public StringValue(String name, String description, String object) {
        super(name, description, object, object);
    }

    public StringValue(String name, String description, Dependency dependency, String object) {
        super(name, description, dependency, object, object);
    }

    public StringValue(String name, String description, ValueChangeCallback callback, String object) {
        super(name, description, callback, object, object);
    }

    public StringValue(String name, String description, ValueChangeCallback callback, Dependency dependency, String object) {
        super(name, description, callback, dependency, object, object);
    }

}
