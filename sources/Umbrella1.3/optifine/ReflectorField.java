/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.lang.reflect.Field;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorClass;

public class ReflectorField {
    private ReflectorClass reflectorClass = null;
    private String targetFieldName = null;
    private boolean checked = false;
    private Field targetField = null;

    public ReflectorField(ReflectorClass reflectorClass, String targetFieldName) {
        this.reflectorClass = reflectorClass;
        this.targetFieldName = targetFieldName;
        Field f = this.getTargetField();
    }

    public Field getTargetField() {
        if (this.checked) {
            return this.targetField;
        }
        this.checked = true;
        Class cls = this.reflectorClass.getTargetClass();
        if (cls == null) {
            return null;
        }
        try {
            this.targetField = cls.getDeclaredField(this.targetFieldName);
            this.targetField.setAccessible(true);
        }
        catch (NoSuchFieldException var3) {
            Config.log("(Reflector) Field not present: " + cls.getName() + "." + this.targetFieldName);
        }
        catch (SecurityException var4) {
            var4.printStackTrace();
        }
        catch (Throwable var5) {
            var5.printStackTrace();
        }
        return this.targetField;
    }

    public Object getValue() {
        return Reflector.getFieldValue(null, this);
    }

    public void setValue(Object value) {
        Reflector.setFieldValue(null, this, value);
    }

    public boolean exists() {
        return this.checked ? this.targetField != null : this.getTargetField() != null;
    }
}

