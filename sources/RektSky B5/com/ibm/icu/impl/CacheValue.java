/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.util.ICUException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

public abstract class CacheValue<V> {
    private static volatile Strength strength = Strength.SOFT;
    private static final CacheValue NULL_VALUE = new NullValue();

    public static void setStrength(Strength strength) {
        CacheValue.strength = strength;
    }

    public static boolean futureInstancesWillBeStrong() {
        return strength == Strength.STRONG;
    }

    public static <V> CacheValue<V> getInstance(V value) {
        if (value == null) {
            return NULL_VALUE;
        }
        return strength == Strength.STRONG ? new StrongValue<V>(value) : new SoftValue<V>(value);
    }

    public boolean isNull() {
        return false;
    }

    public abstract V get();

    public abstract V resetIfCleared(V var1);

    private static final class SoftValue<V>
    extends CacheValue<V> {
        private volatile Reference<V> ref;

        SoftValue(V value) {
            this.ref = new SoftReference<V>(value);
        }

        @Override
        public V get() {
            return this.ref.get();
        }

        @Override
        public synchronized V resetIfCleared(V value) {
            V oldValue = this.ref.get();
            if (oldValue == null) {
                this.ref = new SoftReference<V>(value);
                return value;
            }
            return oldValue;
        }
    }

    private static final class StrongValue<V>
    extends CacheValue<V> {
        private V value;

        StrongValue(V value) {
            this.value = value;
        }

        @Override
        public V get() {
            return this.value;
        }

        @Override
        public V resetIfCleared(V value) {
            return this.value;
        }
    }

    private static final class NullValue<V>
    extends CacheValue<V> {
        private NullValue() {
        }

        @Override
        public boolean isNull() {
            return true;
        }

        @Override
        public V get() {
            return null;
        }

        @Override
        public V resetIfCleared(V value) {
            if (value != null) {
                throw new ICUException("resetting a null value to a non-null value");
            }
            return null;
        }
    }

    public static enum Strength {
        STRONG,
        SOFT;

    }
}

