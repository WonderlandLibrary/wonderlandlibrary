package best.azura.client.api.value;

import best.azura.client.api.value.dependency.Dependency;

public class Value<T> {

	private T object;
	private T[] objects;
	private String name, description;
	private Dependency dependency;
	private ValueChangeCallback callback;
	private long lastValueChange;

	@SafeVarargs
	public Value(String name, String description, ValueChangeCallback callback, T object, T... objects) {
		this.name = name;
		this.description = description;
		this.object = object;
		this.objects = objects;
		this.dependency = null;
		this.callback = callback;
	}

	@SafeVarargs
	public Value(String name, String description, ValueChangeCallback callback, Dependency dependency, T object, T... objects) {
		this.name = name;
		this.description = description;
		if (this.description == null) this.description = "ERROR: No description found!";
		this.object = object;
		this.objects = objects;
		this.dependency = dependency;
		this.callback = callback;
	}

	@SafeVarargs
	public Value(String name, String description, T object, T... objects) {
		this(name, description, null, null, object, objects);
	}

	@SafeVarargs
	public Value(String name, String description, Dependency dependency, T object, T... objects) {
		this(name, description, null, dependency, object, objects);
	}

	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
		if (this.callback != null && System.currentTimeMillis() != lastValueChange) {
			this.callback.onValueChange(this);
			lastValueChange = System.currentTimeMillis();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setObjects(T[] objects) {
		this.objects = objects;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCallback(ValueChangeCallback callback) {
		this.callback = callback;
	}

	public void setDependency(Dependency dependency) {
		this.dependency = dependency;
	}

	public T[] getObjects() {
		return objects;
	}

	public String getName() {
		return name;
	}

	public boolean checkDependency() {
		if(dependency == null) return true;
		return dependency.checkDependency();
	}

	public ValueChangeCallback getCallback() {
		return callback;
	}
}