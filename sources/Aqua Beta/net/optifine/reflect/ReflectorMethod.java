package net.optifine.reflect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import net.optifine.Log;
import net.optifine.reflect.IResolvable;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorClass;
import net.optifine.reflect.ReflectorResolver;

public class ReflectorMethod
implements IResolvable {
    private ReflectorClass reflectorClass = null;
    private String targetMethodName = null;
    private Class[] targetMethodParameterTypes = null;
    private boolean checked = false;
    private Method targetMethod = null;

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName) {
        this(reflectorClass, targetMethodName, null);
    }

    public ReflectorMethod(ReflectorClass reflectorClass, String targetMethodName, Class[] targetMethodParameterTypes) {
        this.reflectorClass = reflectorClass;
        this.targetMethodName = targetMethodName;
        this.targetMethodParameterTypes = targetMethodParameterTypes;
        ReflectorResolver.register((IResolvable)this);
    }

    public Method getTargetMethod() {
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        try {
            if (this.targetMethodParameterTypes == null) {
                Method[] amethod = ReflectorMethod.getMethods(oclass, this.targetMethodName);
                if (amethod.length <= 0) {
                    Log.log((String)("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName));
                    return null;
                }
                if (amethod.length > 1) {
                    Log.warn((String)("(Reflector) More than one method found: " + oclass.getName() + "." + this.targetMethodName));
                    for (int i = 0; i < amethod.length; ++i) {
                        Method method = amethod[i];
                        Log.warn((String)("(Reflector)  - " + method));
                    }
                    return null;
                }
                this.targetMethod = amethod[0];
            } else {
                this.targetMethod = ReflectorMethod.getMethod(oclass, this.targetMethodName, this.targetMethodParameterTypes);
            }
            if (this.targetMethod == null) {
                Log.log((String)("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName));
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
        return this.checked ? this.targetMethod != null : this.getTargetMethod() != null;
    }

    public Class getReturnType() {
        Method method = this.getTargetMethod();
        return method == null ? null : method.getReturnType();
    }

    public void deactivate() {
        this.checked = true;
        this.targetMethod = null;
    }

    public Object call(Object ... params) {
        return Reflector.call((ReflectorMethod)this, (Object[])params);
    }

    public boolean callBoolean(Object ... params) {
        return Reflector.callBoolean((ReflectorMethod)this, (Object[])params);
    }

    public int callInt(Object ... params) {
        return Reflector.callInt((ReflectorMethod)this, (Object[])params);
    }

    public float callFloat(Object ... params) {
        return Reflector.callFloat((ReflectorMethod)this, (Object[])params);
    }

    public double callDouble(Object ... params) {
        return Reflector.callDouble((ReflectorMethod)this, (Object[])params);
    }

    public String callString(Object ... params) {
        return Reflector.callString((ReflectorMethod)this, (Object[])params);
    }

    public Object call(Object param) {
        return Reflector.call((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public boolean callBoolean(Object param) {
        return Reflector.callBoolean((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public int callInt(Object param) {
        return Reflector.callInt((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public float callFloat(Object param) {
        return Reflector.callFloat((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public double callDouble(Object param) {
        return Reflector.callDouble((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public String callString1(Object param) {
        return Reflector.callString((ReflectorMethod)this, (Object[])new Object[]{param});
    }

    public void callVoid(Object ... params) {
        Reflector.callVoid((ReflectorMethod)this, (Object[])params);
    }

    public static Method getMethod(Class cls, String methodName, Class[] paramTypes) {
        Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            Class[] aclass;
            Method method = amethod[i];
            if (!method.getName().equals((Object)methodName) || !Reflector.matchesTypes((Class[])paramTypes, (Class[])(aclass = method.getParameterTypes()))) continue;
            return method;
        }
        return null;
    }

    public static Method[] getMethods(Class cls, String methodName) {
        ArrayList list = new ArrayList();
        Method[] amethod = cls.getDeclaredMethods();
        for (int i = 0; i < amethod.length; ++i) {
            Method method = amethod[i];
            if (!method.getName().equals((Object)methodName)) continue;
            list.add((Object)method);
        }
        Method[] amethod1 = (Method[])list.toArray((Object[])new Method[list.size()]);
        return amethod1;
    }

    public void resolve() {
        Method method = this.getTargetMethod();
    }
}
