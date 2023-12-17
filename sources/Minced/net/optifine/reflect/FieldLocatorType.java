// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import net.optifine.Log;
import java.lang.reflect.Field;

public class FieldLocatorType implements IFieldLocator
{
    private ReflectorClass reflectorClass;
    private Class targetFieldType;
    private int targetFieldIndex;
    
    public FieldLocatorType(final ReflectorClass reflectorClass, final Class targetFieldType) {
        this(reflectorClass, targetFieldType, 0);
    }
    
    public FieldLocatorType(final ReflectorClass reflectorClass, final Class targetFieldType, final int targetFieldIndex) {
        this.reflectorClass = null;
        this.targetFieldType = null;
        this.reflectorClass = reflectorClass;
        this.targetFieldType = targetFieldType;
        this.targetFieldIndex = targetFieldIndex;
    }
    
    @Override
    public Field getField() {
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            final Field[] afield = oclass.getDeclaredFields();
            int i = 0;
            for (int j = 0; j < afield.length; ++j) {
                final Field field = afield[j];
                if (field.getType() == this.targetFieldType) {
                    if (i == this.targetFieldIndex) {
                        field.setAccessible(true);
                        return field;
                    }
                    ++i;
                }
            }
            Log.log("(Reflector) Field not present: " + oclass.getName() + ".(type: " + this.targetFieldType + ", index: " + this.targetFieldIndex + ")");
            return null;
        }
        catch (SecurityException securityexception) {
            securityexception.printStackTrace();
            return null;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
