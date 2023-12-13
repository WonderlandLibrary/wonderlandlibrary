package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.dependency.Dependency;

public class CategoryValue extends Value<String> {

    public CategoryValue(String name) {
        super(name, name, name);
    }

    public CategoryValue(String name, String description, Dependency dependency) {
        super(name, description, dependency, name, name);
    }
}
