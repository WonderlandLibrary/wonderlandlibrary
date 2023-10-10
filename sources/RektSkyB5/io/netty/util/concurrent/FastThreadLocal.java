/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.internal.PlatformDependent;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class FastThreadLocal<V> {
    private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();
    private final int index = InternalThreadLocalMap.nextVariableIndex();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void removeAll() {
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return;
        }
        try {
            Object v2 = threadLocalMap.indexedVariable(variablesToRemoveIndex);
            if (v2 != null && v2 != InternalThreadLocalMap.UNSET) {
                FastThreadLocal[] variablesToRemoveArray;
                Set variablesToRemove = (Set)v2;
                for (FastThreadLocal tlv : variablesToRemoveArray = variablesToRemove.toArray(new FastThreadLocal[variablesToRemove.size()])) {
                    tlv.remove(threadLocalMap);
                }
            }
        }
        finally {
            InternalThreadLocalMap.remove();
        }
    }

    public static int size() {
        InternalThreadLocalMap threadLocalMap = InternalThreadLocalMap.getIfSet();
        if (threadLocalMap == null) {
            return 0;
        }
        return threadLocalMap.size();
    }

    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }

    private static void addToVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
        Set variablesToRemove;
        Object v2 = threadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (v2 == InternalThreadLocalMap.UNSET || v2 == null) {
            variablesToRemove = Collections.newSetFromMap(new IdentityHashMap());
            threadLocalMap.setIndexedVariable(variablesToRemoveIndex, variablesToRemove);
        } else {
            variablesToRemove = (Set)v2;
        }
        variablesToRemove.add(variable);
    }

    private static void removeFromVariablesToRemove(InternalThreadLocalMap threadLocalMap, FastThreadLocal<?> variable) {
        Object v2 = threadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (v2 == InternalThreadLocalMap.UNSET || v2 == null) {
            return;
        }
        Set variablesToRemove = (Set)v2;
        variablesToRemove.remove(variable);
    }

    public final V get() {
        return this.get(InternalThreadLocalMap.get());
    }

    public final V get(InternalThreadLocalMap threadLocalMap) {
        Object v2 = threadLocalMap.indexedVariable(this.index);
        if (v2 != InternalThreadLocalMap.UNSET) {
            return (V)v2;
        }
        return this.initialize(threadLocalMap);
    }

    private V initialize(InternalThreadLocalMap threadLocalMap) {
        V v2 = null;
        try {
            v2 = this.initialValue();
        }
        catch (Exception e2) {
            PlatformDependent.throwException(e2);
        }
        threadLocalMap.setIndexedVariable(this.index, v2);
        FastThreadLocal.addToVariablesToRemove(threadLocalMap, this);
        return v2;
    }

    public final void set(V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            this.set(InternalThreadLocalMap.get(), value);
        } else {
            this.remove();
        }
    }

    public final void set(InternalThreadLocalMap threadLocalMap, V value) {
        if (value != InternalThreadLocalMap.UNSET) {
            if (threadLocalMap.setIndexedVariable(this.index, value)) {
                FastThreadLocal.addToVariablesToRemove(threadLocalMap, this);
            }
        } else {
            this.remove(threadLocalMap);
        }
    }

    public final boolean isSet() {
        return this.isSet(InternalThreadLocalMap.getIfSet());
    }

    public final boolean isSet(InternalThreadLocalMap threadLocalMap) {
        return threadLocalMap != null && threadLocalMap.isIndexedVariableSet(this.index);
    }

    public final void remove() {
        this.remove(InternalThreadLocalMap.getIfSet());
    }

    public final void remove(InternalThreadLocalMap threadLocalMap) {
        if (threadLocalMap == null) {
            return;
        }
        Object v2 = threadLocalMap.removeIndexedVariable(this.index);
        FastThreadLocal.removeFromVariablesToRemove(threadLocalMap, this);
        if (v2 != InternalThreadLocalMap.UNSET) {
            try {
                this.onRemoval(v2);
            }
            catch (Exception e2) {
                PlatformDependent.throwException(e2);
            }
        }
    }

    protected V initialValue() throws Exception {
        return null;
    }

    protected void onRemoval(V value) throws Exception {
    }
}

