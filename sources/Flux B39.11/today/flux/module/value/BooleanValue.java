package today.flux.module.value;

public class BooleanValue extends Value {

    public BooleanValue(String group, String key, Boolean value, boolean fromAPI) {
        this.group = group;
        this.key = key;
        this.value = value;

        if (!fromAPI) ValueManager.addValue(this);
    }

    public BooleanValue(String group, String key, Boolean value) {
        this(group, key, value, false);
    }

    @Override
    public Boolean getValue() {
        return (Boolean) this.value;
    }

    @Override
    public void setValue(Object value) {
        super.setValue(value);
    }

	public boolean getValueState() {
		return this.getValue();
	}
}
