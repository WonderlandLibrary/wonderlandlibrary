// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.management.values;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import exhibition.module.Module;

public class StringValue extends Value<String>
{
    public StringValue(final String name, final String value, final Module module) {
        super(name, value, module);
    }
    
    @Override
    public void setValue(final String value) {
        super.setValue(value);
        Field[] arrayOfField;
        for (int j = (arrayOfField = this.getModule().getClass().getDeclaredFields()).length, i = 0; i < j; ++i) {
            final Field field = arrayOfField[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Val.class) && field.getName().equalsIgnoreCase(this.getValueName())) {
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
