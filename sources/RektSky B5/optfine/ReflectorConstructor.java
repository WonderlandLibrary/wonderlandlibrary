/*
 * Decompiled with CFR 0.152.
 */
package optfine;

import java.lang.reflect.Constructor;
import optfine.Config;
import optfine.Reflector;
import optfine.ReflectorClass;

public class ReflectorConstructor {
    private ReflectorClass reflectorClass = null;
    private Class[] parameterTypes = null;
    private boolean checked = false;
    private Constructor targetConstructor = null;

    public ReflectorConstructor(ReflectorClass p_i57_1_, Class[] p_i57_2_) {
        this.reflectorClass = p_i57_1_;
        this.parameterTypes = p_i57_2_;
        Constructor constructor = this.getTargetConstructor();
    }

    public Constructor getTargetConstructor() {
        if (this.checked) {
            return this.targetConstructor;
        }
        this.checked = true;
        Class oclass = this.reflectorClass.getTargetClass();
        if (oclass == null) {
            return null;
        }
        this.targetConstructor = ReflectorConstructor.findConstructor(oclass, this.parameterTypes);
        if (this.targetConstructor == null) {
            Config.dbg("(Reflector) Constructor not present: " + oclass.getName() + ", params: " + Config.arrayToString(this.parameterTypes));
        }
        if (this.targetConstructor != null && !this.targetConstructor.isAccessible()) {
            this.targetConstructor.setAccessible(true);
        }
        return this.targetConstructor;
    }

    private static Constructor findConstructor(Class p_findConstructor_0_, Class[] p_findConstructor_1_) {
        Constructor<?>[] aconstructor = p_findConstructor_0_.getDeclaredConstructors();
        for (int i2 = 0; i2 < aconstructor.length; ++i2) {
            Constructor<?> constructor = aconstructor[i2];
            Class[] aclass = constructor.getParameterTypes();
            if (!Reflector.matchesTypes(p_findConstructor_1_, aclass)) continue;
            return constructor;
        }
        return null;
    }

    public boolean exists() {
        return this.checked ? this.targetConstructor != null : this.getTargetConstructor() != null;
    }

    public void deactivate() {
        this.checked = true;
        this.targetConstructor = null;
    }
}

