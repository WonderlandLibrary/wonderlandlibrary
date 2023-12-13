// 
// Decompiled by Procyon v0.5.30
// 

package slavikcodd3r.rainbow.option.types;

import java.lang.reflect.Field;

import slavikcodd3r.rainbow.module.Module;
import slavikcodd3r.rainbow.option.Option;

import java.lang.annotation.Annotation;

public class StringOption extends Option<String>
{
    public StringOption(final String id, final String name, final String value, final Module module) {
        super(id, name, value, module);
    }
    
    @Override
    public void setValue(final String value) {
        super.setValue(value);
        Field[] declaredFields;
        for (int length = (declaredFields = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.getId())) {
                try {
                    if (field.getType().isAssignableFrom(String.class)) {
                        field.set(this.getModule(), value);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
