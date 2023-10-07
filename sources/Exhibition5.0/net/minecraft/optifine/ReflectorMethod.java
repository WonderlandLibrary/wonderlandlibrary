// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import java.lang.reflect.Method;

public class ReflectorMethod
{
    private ReflectorClass reflectorClass;
    private String targetMethodName;
    private Class[] targetMethodParameterTypes;
    private boolean checked;
    private Method targetMethod;
    
    public ReflectorMethod(final ReflectorClass reflectorClass, final String targetMethodName) {
        this(reflectorClass, targetMethodName, null);
    }
    
    public ReflectorMethod(final ReflectorClass reflectorClass, final String targetMethodName, final Class[] targetMethodParameterTypes) {
        this.reflectorClass = null;
        this.targetMethodName = null;
        this.targetMethodParameterTypes = null;
        this.checked = false;
        this.targetMethod = null;
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        final Method m = this.getTargetMethod();
    }
    
    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        final Class cls = this.reflectorClass.getTargetClass();
        if (cls == null) {
            return null;
        }
        final Method[] ms = cls.getDeclaredMethods();
        for (int i = 0; i < ms.length; ++i) {
            final Method m = ms[i];
            if (m.getName().equals(this.targetMethodName)) {
                if (this.targetMethodParameterTypes != null) {
                    final Class[] types = m.getParameterTypes();
                    if (!Reflector.matchesTypes(this.targetMethodParameterTypes, types)) {
                        continue;
                    }
                }
                this.targetMethod = m;
                if (!this.targetMethod.isAccessible()) {
                    this.targetMethod.setAccessible(true);
                }
                return this.targetMethod;
            }
        }
        Config.log("(Reflector) Method not pesent: " + cls.getName() + "." + this.targetMethodName);
        return null;
    }
    
    public boolean exists() {
        return this.checked ? (this.targetMethod != null) : (this.getTargetMethod() != null);
    }
    
    public Class getReturnType() {
        final Method tm = this.getTargetMethod();
        return (tm == null) ? null : tm.getReturnType();
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
}
