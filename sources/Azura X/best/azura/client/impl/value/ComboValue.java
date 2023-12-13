package best.azura.client.impl.value;

import best.azura.client.api.value.Value;
import best.azura.client.api.value.ValueChangeCallback;
import best.azura.client.api.value.dependency.Dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"unchecked", "unused"})
public class ComboValue extends Value<List<ComboSelection>> {

    public ComboValue(String name, String description, ValueChangeCallback callback, ComboSelection... objects) {
        super(name, description, callback, Arrays.asList(objects));
    }

    public ComboValue(String name, String description, ValueChangeCallback callback, Dependency dependency, ComboSelection... objects) {
        super(name, description, callback, dependency, Arrays.asList(objects));
    }

    public ComboValue(String name, String description, ComboSelection... objects) {
        super(name, description, Arrays.asList(objects));
    }

    public ComboValue(String name, String description, Dependency dependency, ComboSelection... objects) {
        super(name, description, dependency, Arrays.asList(objects));
    }

    public List<ComboSelection> getSelected() {
        return getObject().stream().filter(ComboSelection::getObject).collect(Collectors.toList());
    }

    public boolean isSelected(final String name) {
        return getSelected().stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
    }

    @Override
    public List<ComboSelection>[] getObjects() {
        return new ArrayList[] { (ArrayList<ComboSelection>) getObject()};
    }
}