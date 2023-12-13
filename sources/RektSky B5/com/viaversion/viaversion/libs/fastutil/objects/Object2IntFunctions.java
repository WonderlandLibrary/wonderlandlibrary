/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunctions$SynchronizedFunction
 *  com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunctions$UnmodifiableFunction
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Function;
import com.viaversion.viaversion.libs.fastutil.objects.AbstractObject2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunction;
import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunctions;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.ToIntFunction;

public final class Object2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Object2IntFunctions() {
    }

    public static <K> Object2IntFunction<K> singleton(K key, int value) {
        return new Singleton<K>(key, value);
    }

    public static <K> Object2IntFunction<K> singleton(K key, Integer value) {
        return new Singleton<K>(key, value);
    }

    public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f2) {
        return new SynchronizedFunction(f2);
    }

    public static <K> Object2IntFunction<K> synchronize(Object2IntFunction<K> f2, Object sync) {
        return new SynchronizedFunction(f2, sync);
    }

    public static <K> Object2IntFunction<K> unmodifiable(Object2IntFunction<? extends K> f2) {
        return new UnmodifiableFunction(f2);
    }

    public static <K> Object2IntFunction<K> primitive(java.util.function.Function<? super K, ? extends Integer> f2) {
        Objects.requireNonNull(f2);
        if (f2 instanceof Object2IntFunction) {
            return (Object2IntFunction)f2;
        }
        if (f2 instanceof ToIntFunction) {
            return key -> ((ToIntFunction)((Object)f2)).applyAsInt(key);
        }
        return new PrimitiveFunction<K>(f2);
    }

    public static class Singleton<K>
    extends AbstractObject2IntFunction<K>
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final K key;
        protected final int value;

        protected Singleton(K key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean containsKey(Object k2) {
            return Objects.equals(this.key, k2);
        }

        @Override
        public int getInt(Object k2) {
            return Objects.equals(this.key, k2) ? this.value : this.defRetValue;
        }

        @Override
        public int getOrDefault(Object k2, int defaultValue) {
            return Objects.equals(this.key, k2) ? this.value : defaultValue;
        }

        @Override
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static class PrimitiveFunction<K>
    implements Object2IntFunction<K> {
        protected final java.util.function.Function<? super K, ? extends Integer> function;

        protected PrimitiveFunction(java.util.function.Function<? super K, ? extends Integer> function) {
            this.function = function;
        }

        @Override
        public boolean containsKey(Object key) {
            return this.function.apply(key) != null;
        }

        @Override
        public int getInt(Object key) {
            Integer v2 = this.function.apply(key);
            if (v2 == null) {
                return this.defaultReturnValue();
            }
            return v2;
        }

        @Override
        public int getOrDefault(Object key, int defaultValue) {
            Integer v2 = this.function.apply(key);
            if (v2 == null) {
                return defaultValue;
            }
            return v2;
        }

        @Override
        @Deprecated
        public Integer get(Object key) {
            return this.function.apply(key);
        }

        @Override
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            Integer v2 = this.function.apply(key);
            return v2 == null ? defaultValue : v2;
        }

        @Override
        @Deprecated
        public Integer put(K key, Integer value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class EmptyFunction<K>
    extends AbstractObject2IntFunction<K>
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public int getInt(Object k2) {
            return 0;
        }

        @Override
        public int getOrDefault(Object k2, int defaultValue) {
            return defaultValue;
        }

        @Override
        public boolean containsKey(Object k2) {
            return false;
        }

        @Override
        public int defaultReturnValue() {
            return 0;
        }

        @Override
        public void defaultReturnValue(int defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void clear() {
        }

        public Object clone() {
            return EMPTY_FUNCTION;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o2) {
            if (!(o2 instanceof Function)) {
                return false;
            }
            return ((Function)o2).size() == 0;
        }

        public String toString() {
            return "{}";
        }

        private Object readResolve() {
            return EMPTY_FUNCTION;
        }
    }
}

