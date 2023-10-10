/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunctions$SynchronizedFunction
 *  com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunctions$UnmodifiableFunction
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Function;
import com.viaversion.viaversion.libs.fastutil.ints.AbstractInt2IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunction;
import com.viaversion.viaversion.libs.fastutil.ints.Int2IntFunctions;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public final class Int2IntFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Int2IntFunctions() {
    }

    public static Int2IntFunction singleton(int key, int value) {
        return new Singleton(key, value);
    }

    public static Int2IntFunction singleton(Integer key, Integer value) {
        return new Singleton(key, value);
    }

    public static Int2IntFunction synchronize(Int2IntFunction f2) {
        return new SynchronizedFunction(f2);
    }

    public static Int2IntFunction synchronize(Int2IntFunction f2, Object sync) {
        return new SynchronizedFunction(f2, sync);
    }

    public static Int2IntFunction unmodifiable(Int2IntFunction f2) {
        return new UnmodifiableFunction(f2);
    }

    public static Int2IntFunction primitive(java.util.function.Function<? super Integer, ? extends Integer> f2) {
        Objects.requireNonNull(f2);
        if (f2 instanceof Int2IntFunction) {
            return (Int2IntFunction)f2;
        }
        if (f2 instanceof IntUnaryOperator) {
            return ((IntUnaryOperator)((Object)f2))::applyAsInt;
        }
        return new PrimitiveFunction(f2);
    }

    public static class Singleton
    extends AbstractInt2IntFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final int key;
        protected final int value;

        protected Singleton(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean containsKey(int k2) {
            return this.key == k2;
        }

        @Override
        public int get(int k2) {
            return this.key == k2 ? this.value : this.defRetValue;
        }

        @Override
        public int getOrDefault(int k2, int defaultValue) {
            return this.key == k2 ? this.value : defaultValue;
        }

        @Override
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static class PrimitiveFunction
    implements Int2IntFunction {
        protected final java.util.function.Function<? super Integer, ? extends Integer> function;

        protected PrimitiveFunction(java.util.function.Function<? super Integer, ? extends Integer> function) {
            this.function = function;
        }

        @Override
        public boolean containsKey(int key) {
            return this.function.apply((Integer)key) != null;
        }

        @Override
        @Deprecated
        public boolean containsKey(Object key) {
            if (key == null) {
                return false;
            }
            return this.function.apply((Integer)key) != null;
        }

        @Override
        public int get(int key) {
            Integer v2 = this.function.apply((Integer)key);
            if (v2 == null) {
                return this.defaultReturnValue();
            }
            return v2;
        }

        @Override
        public int getOrDefault(int key, int defaultValue) {
            Integer v2 = this.function.apply((Integer)key);
            if (v2 == null) {
                return defaultValue;
            }
            return v2;
        }

        @Override
        @Deprecated
        public Integer get(Object key) {
            if (key == null) {
                return null;
            }
            return this.function.apply((Integer)key);
        }

        @Override
        @Deprecated
        public Integer getOrDefault(Object key, Integer defaultValue) {
            if (key == null) {
                return defaultValue;
            }
            Integer v2 = this.function.apply((Integer)key);
            return v2 == null ? defaultValue : v2;
        }

        @Override
        @Deprecated
        public Integer put(Integer key, Integer value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class EmptyFunction
    extends AbstractInt2IntFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public int get(int k2) {
            return 0;
        }

        @Override
        public int getOrDefault(int k2, int defaultValue) {
            return defaultValue;
        }

        @Override
        public boolean containsKey(int k2) {
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

