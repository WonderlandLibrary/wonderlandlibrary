/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.lang.reflect.Method;
import optfine.Config;
import optfine.Reflector;
import optfine.ReflectorClass;

public class ReflectorMethod {
    private ReflectorClass reflectorClass = null;
    private String targetMethodName = null;
    private Class[] targetMethodParameterTypes = null;
    private boolean checked = false;
    private Method targetMethod = null;

    public ReflectorMethod(ReflectorClass p_i59_1_, String p_i59_2_) {
        this(p_i59_1_, p_i59_2_, null);
    }

    public ReflectorMethod(ReflectorClass p_i60_1_, String p_i60_2_, Class[] p_i60_3_) {
        this.reflectorClass = p_i60_1_;
        this.targetMethodName = p_i60_2_;
        this.targetMethodParameterTypes = p_i60_3_;
        Method method = this.getTargetMethod();
    }

    public Method getTargetMethod() {
        Method method;
        if (this.checked) {
            return this.targetMethod;
        }
        this.checked = true;
        Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        Method[] amethod = oclass.getDeclaredMethods();
        int i2 = 0;
        while (true) {
            Class[] aclass;
            if (i2 >= amethod.length) {
                Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
                return null;
            }
            method = amethod[i2];
            if (method.getName().equals(this.targetMethodName) && (this.targetMethodParameterTypes == null || Reflector.matchesTypes(this.targetMethodParameterTypes, aclass = method.getParameterTypes()))) break;
            ++i2;
        }
        this.targetMethod = method;
        if (!this.targetMethod.isAccessible()) {
            this.targetMethod.setAccessible(true);
        }
        return this.targetMethod;
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
}

