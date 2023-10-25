/*
 * Decompiled with CFR 0.150.
 */
package com.sun.util.reentrant;

import com.sun.util.reentrant.ReentrantContext;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public abstract class ReentrantContextProvider<K extends ReentrantContext> {
    static final byte USAGE_TL_INACTIVE = 0;
    static final byte USAGE_TL_IN_USE = 1;
    static final byte USAGE_CLQ = 2;
    public static final int REF_HARD = 0;
    public static final int REF_SOFT = 1;
    public static final int REF_WEAK = 2;
    private final int refType;

    protected ReentrantContextProvider(int n) {
        this.refType = n;
    }

    protected abstract K newContext();

    public abstract K acquire();

    public abstract void release(K var1);

    protected final Reference<K> getOrCreateReference(K k) {
        if (((ReentrantContext)k).reference == null) {
            switch (this.refType) {
                case 0: {
                    ((ReentrantContext)k).reference = new HardReference<K>(k);
                    break;
                }
                case 1: {
                    ((ReentrantContext)k).reference = new SoftReference<K>(k);
                    break;
                }
                default: {
                    ((ReentrantContext)k).reference = new WeakReference<K>(k);
                }
            }
        }
        return ((ReentrantContext)k).reference;
    }

    static final class HardReference<V>
    extends WeakReference<V> {
        private final V strongRef;

        HardReference(V v) {
            super(null);
            this.strongRef = v;
        }

        @Override
        public V get() {
            return this.strongRef;
        }
    }
}

