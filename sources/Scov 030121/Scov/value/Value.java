package Scov.value;

import Scov.Client;
import Scov.module.Module;
import Scov.value.impl.BooleanValue;
import Scov.value.impl.EnumValue;
import Scov.value.impl.NumberValue;

public abstract class Value<O> {

    protected O value;
    private O defaultValue;
    private String label;
    private Value parentValueObject;
    private String parentValue;
    private Value.ValueType type;

    public Value(String label, O value, ValueType type) {
        this.value = value;
        this.defaultValue = value;
        this.label = label;
        this.type = type;
    }

    public O getValue() {
        return this.value;
    }

    public O getDefaultValue() {
        return this.defaultValue;
    }

    public String getLabel() {
        return this.label;
    }

    public void setValue(O value) {
        this.value = value;
    }

    public Value getParentValueObject() {
        return parentValueObject;
    }

    public String getParentValue() {
        return parentValue;
    }
    
    public abstract void setValue(String value);

    public String getValueAsString() {
        return String.valueOf(value);
    }
    
    public enum ValueType {
    	Boolean, Number, Enum, Color;
    }

	public ValueType getValueType() {
		// TODO Auto-generated method stub
		return type;
	}
}
