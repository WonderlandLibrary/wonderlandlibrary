package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.util.other.ClickListener;
import best.azura.client.api.value.dependency.Dependency;

public class ClickValue extends Value<ClickListener> {
    public ClickValue(String name, String description, ValueChangeCallback callback, ClickListener object) {
        super(name, description, callback, object, object);
    }

    public ClickValue(String name, String description, ValueChangeCallback callback, Dependency dependency, ClickListener object) {
        super(name, description, callback, dependency, object, object);
    }

    public ClickValue(String name, String description, ClickListener object) {
        super(name, description, object, object);
    }

    public ClickValue(String name, String description, Dependency dependency, ClickListener object) {
        super(name, description, dependency, object, object);
    }
}
