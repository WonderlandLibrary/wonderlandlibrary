// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Field;

public class ReflectorField
{
    private ReflectorClass reflectorClass;
    private String targetFieldName;
    private boolean checked;
    private Field targetField;
    
    public ReflectorField(final ReflectorClass reflectorClass, final String targetFieldName) {
        this.reflectorClass = null;
        this.targetFieldName = null;
        this.checked = false;
        this.targetField = null;
        this.reflectorClass = reflectorClass;
        this.targetFieldName = targetFieldName;
        final Field f = this.getTargetField();
    }
    
    public Field getTargetField() {
        if (this.checked) {
            return this.targetField;
        }
        this.checked = true;
        final Class cls = this.reflectorClass.getTargetClass();
        if (cls == null) {
            return null;
        }
        try {
            (this.targetField = cls.getDeclaredField(this.targetFieldName)).setAccessible(true);
        }
        catch (NoSuchFieldException var6) {
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
    
    public void setValue(final Object value) {
        Reflector.setFieldValue(null, this, value);
    }
    
    public boolean exists() {
        return this.checked ? (this.targetField != null) : (this.getTargetField() != null);
    }
}
