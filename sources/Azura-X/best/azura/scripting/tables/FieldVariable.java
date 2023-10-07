package best.azura.scripting.tables;

import best.azura.client.util.scripting.VariableUtil;
import fr.ducouscous.csl.running.variable.Variable;
import fr.ducouscous.csl.running.variable.VariableValue;
import fr.ducouscous.csl.running.variable.values.Number;
import fr.ducouscous.csl.running.variable.values.StringVar;

import java.lang.reflect.Field;

public class FieldVariable extends Variable {
    private final Field field;
    private final Object object;
    public FieldVariable(final Field field, final Object object) {
        super(null);
        this.field = field;
        if (!field.isAccessible()) field.setAccessible(true);
        this.object = object;
    }

    public boolean isStatic() {
        return object == null;
    }

    public Field getField() {
        return field;
    }

    @Override
    public VariableValue getValue() {
        try {
            Object value = field.get(object);
            if (value == null) {
                return null;
            } else if (value instanceof java.lang.Number) {
                return new Number(((java.lang.Number) value).doubleValue());
            } else if (value instanceof String) {
                return new StringVar(value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return getValue().toString();
    }

    @Override
    public void setValue(VariableValue value) {
        try {
            field.set(object, value.value());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.setValue(value);
    }
}
