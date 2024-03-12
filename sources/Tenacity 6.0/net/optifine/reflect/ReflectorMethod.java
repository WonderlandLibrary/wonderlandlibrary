// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import java.util.List;
import java.util.ArrayList;
import net.optifine.Log;
import java.lang.reflect.Method;

public class ReflectorMethod implements IResolvable
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
        ReflectorResolver.register(this);
    }
    
    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        final Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            if (this.targetMethodParameterTypes == null) {
                final Method[] amethod = getMethods(oclass, this.targetMethodName);
                if (amethod.length <= 0) {
                    Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                    return null;
                }
                if (amethod.length > 1) {
                    Log.warn("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName);
                    for (int i = 0; i < amethod.length; ++i) {
                        final Method method = amethod[i];
                        Log.warn("(Reflector)  - " + method);
                    }
                    return null;
                }
                this.targetMethod = amethod[0];
            }
            else {
                this.targetMethod = getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
            }
            if (this.targetMethod == null) {
                Log.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                return null;
            }
            this.targetMethod.setAccessible(true);
            return this.targetMethod;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
    
    public boolean exists() {
        return this.checked ? (this.targetMethod != null) : (this.getTargetMethod() != null);
    }
    
    public Class getReturnType() {
        final Method method = this.getTargetMethod();
        return (method == null) ? null : method.getReturnType();
    }
    
    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }
    
    public Object call(final Object... params) {
        return Reflector.call(this, params);
    }
    
    public boolean callBoolean(final Object... params) {
        return Reflector.callBoolean(this, params);
    }
    
    public int callInt(final Object... params) {
        return Reflector.callInt(this, params);
    }
    
    public float callFloat(final Object... params) {
        return Reflector.callFloat(this, params);
    }
    
    public double callDouble(final Object... params) {
        return Reflector.callDouble(this, params);
    }
    
    public String callString(final Object... params) {
        return Reflector.callString(this, params);
    }
    
    public Object call(final Object param) {
        return Reflector.call(this, param);
    }
    
    public boolean callBoolean(final Object param) {
        return Reflector.callBoolean(this, param);
    }
    
    public int callInt(final Object param) {
        return Reflector.callInt(this, param);
    }
    
    public float callFloat(final Object param) {
        return Reflector.callFloat(this, param);
    }
    
    public double callDouble(final Object param) {
        return Reflector.callDouble(this, param);
    }
    
    public String callString1(final Object param) {
        return Reflector.callString(this, param);
    }
    
    public void callVoid(final Object... params) {
        Reflector.callVoid(this, params);
    }
    
    public static Method getMethod(final Class cls, final String methodName, final Class[] paramTypes) {
        final Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(methodName)) {
                final Class[] aclass = method.getParameterTypes();
                if (Reflector.matchesTypes(paramTypes, aclass)) {
                    return method;
                }
            }
        }
        return null;
    }
    
    public static Method[] getMethods(final Class cls, final String methodName) {
        final List list = new ArrayList();
        final Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            final Method method = amethod[i];
            if (method.getName().equals(methodName)) {
                list.add(method);
            }
        }
        final Method[] amethod2 = list.toArray(new Method[list.size()]);
        return amethod2;
    }
    
    @Override
    public void resolve() {
        final Method method = this.getTargetMethod();
    }
}
