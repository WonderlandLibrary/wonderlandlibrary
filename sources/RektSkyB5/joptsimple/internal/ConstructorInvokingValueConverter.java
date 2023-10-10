/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.lang.reflect.Constructor;
import joptsimple.ValueConverter;
import joptsimple.internal.Reflection;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class ConstructorInvokingValueConverter<V>
implements ValueConverter<V> {
    private final Constructor<V> ctor;

    ConstructorInvokingValueConverter(Constructor<V> ctor) {
        this.ctor = ctor;
    }

    @Override
    public V convert(String value) {
        return Reflection.instantiate(this.ctor, value);
    }

    @Override
    public Class<V> valueType() {
        return this.ctor.getDeclaringClass();
    }

    @Override
    public String valuePattern() {
        return null;
    }
}

