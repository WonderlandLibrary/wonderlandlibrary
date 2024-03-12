// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import net.optifine.Log;
import net.optifine.util.ArrayUtils;
import java.lang.reflect.Constructor;

public class ReflectorConstructor implements IResolvable
{
    private ReflectorClass reflectorClass;
    private Class[] parameterTypes;
    private boolean checked;
    private Constructor targetConstructor;
    
    public ReflectorConstructor(final ReflectorClass reflectorClass, final Class[] parameterTypes) {
        this.reflectorClass = null;
        this.parameterTypes = null;
        this.checked = false;
        this.targetConstructor = null;
        this.reflectorClass = reflectorClass;
        this.parameterTypes = parameterTypes;
        ReflectorResolver.register(this);
    }
    
    public Constructor getTargetConstructor() {
        if (this.checked) {
            return this.targetConstructor;
        }
        this.checked = true;
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            this.targetConstructor = findConstructor(oclass, this.parameterTypes);
            if (this.targetConstructor == null) {
                Log.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + ArrayUtils.arrayToString(this.parameterTypes));
            }
            if (this.targetConstructor != null) {
                this.targetConstructor.setAccessible(true);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return this.targetConstructor;
    }
    
    private static Constructor findConstructor(final Class cls, final Class[] paramTypes) {
        final Constructor[] aconstructor = cls.getDeclaredConstructors();
        for (int i = 0; i < aconstructor.length; ++i) {
            final Constructor constructor = aconstructor[i];
            final Class[] aclass = constructor.getParameterTypes();
            if (Reflector.matchesTypes(paramTypes, aclass)) {
                return constructor;
            }
        }
        return null;
    }
    
    public boolean exists() {
        return this.checked ? (this.targetConstructor != null) : (this.getTargetConstructor() != null);
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetConstructor = null;
    }
    
    public Object newInstance(final Object... params) {
        return Reflector.newInstance(this, params);
    }
    
    @Override
    public void resolve() {
        final Constructor constructor = this.getTargetConstructor();
    }
}
