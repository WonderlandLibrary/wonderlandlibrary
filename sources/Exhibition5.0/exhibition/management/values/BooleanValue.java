// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.values;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import exhibition.module.Module;

public class BooleanValue extends Value<Boolean>
{
    private boolean isModType;
    
    public BooleanValue(final String name, final boolean value, final Module module, final boolean isModType) {
        super(name, value, module);
        this.isModType = isModType;
    }
    
    @Override
    public void setValue(final Boolean value) {
        super.setValue(value);
        Field[] arrayOfField;
        for (int j = (arrayOfField = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < j; ++i) {
            final Field field = arrayOfField[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Val.class) && field.getName().equalsIgnoreCase(this.getValueName())) {
                try {
                    if (field.getType().isAssignableFrom(Boolean.TYPE)) {
                        field.setBoolean(this.getModule(), value);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void setValueHard(final Boolean value) {
        super.setValue(value);
        Field[] arrayOfField;
        for (int j = (arrayOfField = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < j; ++i) {
            final Field field = arrayOfField[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Val.class) && field.getName().equalsIgnoreCase(this.getValueName())) {
                try {
                    if (field.getType().isAssignableFrom(Boolean.TYPE)) {
                        field.setBoolean(this.getModule(), value);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void toggle() {
        this.setValue(Boolean.valueOf(!this.getValue()));
    }
    
    public boolean isModType() {
        return this.isModType;
    }
}
