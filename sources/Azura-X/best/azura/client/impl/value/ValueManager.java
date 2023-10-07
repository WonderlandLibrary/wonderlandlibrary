package best.azura.client.impl.value;

import best.azura.client.api.value.Value;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ValueManager {

	private HashMap<Object, ArrayList<Value<?>>> valueHash;

	public ValueManager() {
		start();
	}

	public void start() {
		valueHash = new HashMap<>();
	}

	public void register(Object obj) {
		ArrayList<Value<?>> values = new ArrayList<>();
		for(Field f : obj.getClass().getDeclaredFields()) {
			try {
				f.setAccessible(true);
				Object o = f.get(obj);
				if(o instanceof Value) values.add((Value<?>)o);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		valueHash.put(obj, values);
	}

	public void register(Object obj, Value<?> value) {
		if(!valueHash.containsKey(obj)) register(obj);
		if(valueHash.containsKey(obj) && valueHash.get(obj) != null && !valueHash.get(obj).contains(value)) valueHash.get(obj).add(value);
	}

	public ArrayList<Value<?>> getValues(Object obj) {
		return valueHash.get(obj);
	}

	public HashMap<Object, ArrayList<Value<?>>> getValueHash() {
		return valueHash;
	}

	public Value<?> getValue(Object obj, String name) {
		for(Value<?> v : valueHash.get(obj)) {
			if(v instanceof CategoryValue) continue;
			if(v.getName().equalsIgnoreCase(name)) return v;
		}
		for(Value<?> v : valueHash.get(obj)) {
			if(v instanceof CategoryValue) continue;
			if(v.getName().replace(" ", "").equalsIgnoreCase(name)) return v;
		}
		return null;
	}

	public Value<?> getValueByType(Object obj, Class<?> checkingClass) {
		for(Value<?> v : valueHash.get(obj)) {
			if(v.getClass() == checkingClass) return v;
		}
		return null;
	}

	public boolean isRegistered(Object obj) {
		return valueHash.containsKey(obj);
	}

}