// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import net.optifine.Log;
import java.lang.reflect.Field;

public class FieldLocatorName implements IFieldLocator
{
    private ReflectorClass reflectorClass;
    private String targetFieldName;
    
    public FieldLocatorName(final ReflectorClass reflectorClass, final String targetFieldName) {
        this.reflectorClass = null;
        this.targetFieldName = null;
        this.reflectorClass = reflectorClass;
        this.targetFieldName = targetFieldName;
    }
    
    @Override
    public Field getField() {
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            final Field field = this.getDeclaredField(oclass, this.targetFieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException var3) {
            Log.log("(Reflector) Field not present: " + oclass.getName() + "." + this.targetFieldName);
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
    
    private Field getDeclaredField(final Class cls, final String name) throws NoSuchFieldException {
        final Field[] afield = cls.getDeclaredFields();
        for (int i = 0; i < afield.length; ++i) {
            final Field field = afield[i];
            if (field.getName().equals(name)) {
                return field;
            }
        }
        if (cls == Object.class) {
            throw new NoSuchFieldException(name);
        }
        return this.getDeclaredField(cls.getSuperclass(), name);
    }
}
