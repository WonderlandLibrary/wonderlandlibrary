package cc.novoline.modules.configurations.property;

import org.jetbrains.annotations.Nullable;

public abstract class AbstractProperty<Type> implements Property<Type> {

	/* fields */
	protected Type value;

	/* constructors */
	protected AbstractProperty(@Nullable Type value) {
		set(value);
	}

	protected AbstractProperty() {
		this(null);
	}

	/* methods */
	@Override
	public Type get() {
		return value;
	}

	@Override
	public void set(@Nullable Type value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value != null ? value.toString() : "null";
	}
}
