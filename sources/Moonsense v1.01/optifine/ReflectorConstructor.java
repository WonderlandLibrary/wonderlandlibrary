// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Constructor;

public class ReflectorConstructor
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
        final Constructor c = this.getTargetConstructor();
    }
    
    public Constructor getTargetConstructor() {
        if (this.checked) {
            return this.targetConstructor;
        }
        this.checked = true;
        final Class cls = this.reflectorClass.getTargetClass();
        if (cls == null) {
            return null;
        }
        try {
            this.targetConstructor = findConstructor(cls, this.parameterTypes);
            if (this.targetConstructor == null) {
                Config.dbg("(Reflector) Constructor not present: " + cls.getName() + ", params: " + Config.arrayToString(this.parameterTypes));
            }
            if (this.targetConstructor != null) {
                this.targetConstructor.setAccessible(true);
            }
        }
        catch (Throwable var3) {
            var3.printStackTrace();
        }
        return this.targetConstructor;
    }
    
    private static Constructor findConstructor(final Class cls, final Class[] paramTypes) {
        final Constructor[] cs = cls.getDeclaredConstructors();
        for (int i = 0; i < cs.length; ++i) {
            final Constructor c = cs[i];
            final Class[] types = c.getParameterTypes();
            if (Reflector.matchesTypes(paramTypes, types)) {
                return c;
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
}
