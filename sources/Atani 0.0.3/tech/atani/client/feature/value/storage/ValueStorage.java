package tech.atani.client.feature.value.storage;

import de.florianmichael.rclasses.storage.Storage;
import tech.atani.client.feature.value.Value;

import java.util.*;

public class ValueStorage extends Storage<Value> {

    private static ValueStorage instance;

    @Override
    public void init() {

    }

    private final Map<Object, List<Value>> linkedValues = new HashMap<>();

    public final ArrayList<Value> getValues(Object owner) {
        return this.getValues(owner, true);
    }

    public final ArrayList<Value> getValues(Object owner, boolean allowLinkedValues) {
        ArrayList<Value> values = new ArrayList<>();
        for (Value v : this.getList()) {
            if (v.getOwner() == owner)
                values.add(v);
        }
        if(allowLinkedValues) {
            if(linkedValues.containsKey(owner)) {
                values.addAll(linkedValues.get(owner));
            }
        }
        return values;
    }

    public void clearLinkedValues() {
        this.linkedValues.clear();
    }

    public void clearLinkedValues(Object object) {
        this.linkedValues.remove(object);
    }

    public void removeLinkedValues(Object object, Value... values) {
        if(this.linkedValues.containsKey(object)) {
            for(Value value : values)
                if(linkedValues.get(object).contains(value))
                    this.linkedValues.get(object).remove(value);
        }
    }

    public void addLinkedValues(Object object, ArrayList<Value> arrayList) {
        if(this.linkedValues.containsKey(object)) {
            this.linkedValues.get(object).addAll(arrayList);
        } else {
            this.linkedValues.put(object, arrayList);
        }
    }

    public void addLinkedValues(Object object, Value... values) {
        if(this.linkedValues.containsKey(object)) {
            for(Value value : values)
                this.linkedValues.get(object).add(value);
        } else {
            List<Value> valueArrayList = new ArrayList<>();
            for(Value value : values)
                valueArrayList.add(value);
            this.linkedValues.put(object, valueArrayList);
        }
    }

    public static ValueStorage getInstance() {
        return instance;
    }

    public static void setInstance(ValueStorage instance) {
        ValueStorage.instance = instance;
    }
}
