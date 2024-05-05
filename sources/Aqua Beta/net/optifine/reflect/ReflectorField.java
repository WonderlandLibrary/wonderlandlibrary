package net.optifine.reflect;

import java.lang.reflect.Field;
import net.optifine.reflect.FieldLocatorFixed;
import net.optifine.reflect.FieldLocatorName;
import net.optifine.reflect.FieldLocatorType;
import net.optifine.reflect.IFieldLocator;
import net.optifine.reflect.IResolvable;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorClass;
import net.optifine.reflect.ReflectorResolver;

public class ReflectorField
implements IResolvable {
    private IFieldLocator fieldLocator = null;
    private boolean checked = false;
    private Field targetField = null;

    public ReflectorField(ReflectorClass reflectorClass, String targetFieldName) {
        this((IFieldLocator)new FieldLocatorName(reflectorClass, targetFieldName));
    }

    public ReflectorField(ReflectorClass reflectorClass, Class targetFieldType) {
        this(reflectorClass, targetFieldType, 0);
    }

    public ReflectorField(ReflectorClass reflectorClass, Class targetFieldType, int targetFieldIndex) {
        this((IFieldLocator)new FieldLocatorType(reflectorClass, targetFieldType, targetFieldIndex));
    }

    public ReflectorField(Field field) {
        this((IFieldLocator)new FieldLocatorFixed(field));
    }

    public ReflectorField(IFieldLocator fieldLocator) {
        this.fieldLocator = fieldLocator;
        ReflectorResolver.register((IResolvable)this);
    }

    public Field getTargetField() {
        if (this.checked) {
            return this.targetField;
        }
        this.checked = true;
        this.targetField = this.fieldLocator.getField();
        if (this.targetField != null) {
            this.targetField.setAccessible(true);
        }
        return this.targetField;
    }

    public Object getValue() {
        return Reflector.getFieldValue(null, (ReflectorField)this);
    }

    public void setValue(Object value) {
        Reflector.setFieldValue(null, (ReflectorField)this, (Object)value);
    }

    public void setValue(Object obj, Object value) {
        Reflector.setFieldValue((Object)obj, (ReflectorField)this, (Object)value);
    }

    public boolean exists() {
        return this.getTargetField() != null;
    }

    public void resolve() {
        Field field = this.getTargetField();
    }
}
