package today.flux.module.value;

import java.util.ArrayList;
import java.util.List;

public class ValueManager {
    private static final ArrayList<Value> values = new ArrayList<>();

    public static final ArrayList<Value> apiValues = new ArrayList<>();

    public static void addValue(Value value) {
        values.add(value);
    }

    public static void removeValue(Value value) {
        values.remove(value);
    }
    public static ArrayList<Value> getValues() {
        ArrayList<Value> list = new ArrayList<>(values);
        list.addAll(apiValues);
        return list;
    }

    public static Value getValue(final String name) {
        for (final Value value : getValues()) {
            if (value.getKey().equalsIgnoreCase(name)) {
                return value;
            }
        }
        return null;
    }

    public static List<Value> getValueByModName(final String modName) {
        List<Value> result = new ArrayList<>();

        for (final Value value : getValues()) {
            if (value.getGroup().equalsIgnoreCase(modName))
                result.add(value);
        }
        return result;
    }

    public static Value getValueByGroupAndKey(final String group, final String key) {
        for (final Value value : getValues()) {
            if (value.getGroup().equalsIgnoreCase(group) && value.getKey().equals(key))
                return value;
        }
        return null;
    }

    public static List<Value> getValueByModNameForRender(final String modName) {
        List<Value> result = new ArrayList<>();

        for (final Value value : getValues()) {
            if (value.getGroup().equals(modName))
                result.add(value);
        }
        return result;
    }
}
