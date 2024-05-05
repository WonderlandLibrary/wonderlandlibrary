// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.option.types;

import java.lang.reflect.Field;

import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option;

import java.lang.annotation.Annotation;

public class BooleanOption extends Option<Boolean>
{
    private boolean isModType;
    
    public BooleanOption(final String id, final String name, final boolean value, final Module module) {
        super(id, name, value, module);
    }
    
    @Override
    public void setValue(final Boolean value) {
        super.setValue(value);
        Field[] declaredFields;
        for (int length = (declaredFields = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.getId())) {
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
        Field[] declaredFields;
        for (int length = (declaredFields = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.getId())) {
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
