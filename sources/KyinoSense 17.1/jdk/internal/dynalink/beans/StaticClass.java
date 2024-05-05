/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.beans;

import java.io.Serializable;
import java.util.Objects;

public class StaticClass
implements Serializable {
    private static final ClassValue<StaticClass> staticClasses = new ClassValue<StaticClass>(){

        @Override
        protected StaticClass computeValue(Class<?> type) {
            return new StaticClass(type);
        }
    };
    private static final long serialVersionUID = 1L;
    private final Class<?> clazz;

    StaticClass(Class<?> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }

    public static StaticClass forClass(Class<?> clazz) {
        return staticClasses.get(clazz);
    }

    public Class<?> getRepresentedClass() {
        return this.clazz;
    }

    public String toString() {
        return "JavaClassStatics[" + this.clazz.getName() + "]";
    }

    private Object readResolve() {
        return StaticClass.forClass(this.clazz);
    }
}

