/*
 * Decompiled with CFR 0.150.
 */
package optifine;

import java.lang.reflect.Field;
import optifine.IFieldLocator;
import optifine.ReflectorClass;

public class FieldLocatorName
implements IFieldLocator {
    private ReflectorClass reflectorClass = null;
    private String targetFieldName = null;

    public FieldLocatorName(ReflectorClass p_i38_1_, String p_i38_2_) {
        this.reflectorClass = p_i38_1_;
        this.targetFieldName = p_i38_2_;
    }

    @Override
    public Field getField() {
        Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            Field field = this.getDeclaredField(oclass, this.targetFieldName);
            field.setAccessible(true);
            return field;
        }
        catch (NoSuchFieldException var3) {
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

    private Field getDeclaredField(Class p_getDeclaredField_1_, String p_getDeclaredField_2_) throws NoSuchFieldException {
        Field[] afield = p_getDeclaredField_1_.getDeclaredFields();
        for (int i = 0; i < afield.length; ++i) {
            Field field = afield[i];
            if (!field.getName().equals(p_getDeclaredField_2_)) continue;
            return field;
        }
        if (p_getDeclaredField_1_ == Object.class) {
            throw new NoSuchFieldException(p_getDeclaredField_2_);
        }
        return this.getDeclaredField(p_getDeclaredField_1_.getSuperclass(), p_getDeclaredField_2_);
    }
}

