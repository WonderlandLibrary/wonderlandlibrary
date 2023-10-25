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
    private final int index = InternalThreadLocalMap.nextVariableIndex();
    private static final int variablesToRemoveIndex = InternalThreadLocalMap.nextVariableIndex();

    public final boolean isSet() {
        return this.isSet(InternalThreadLocalMap.getIfSet());
    }

    public final void set(V v) {
        if (v != InternalThreadLocalMap.UNSET) {
            this.set(InternalThreadLocalMap.get(), v);
        } else {
            this.remove();
        }
    }

    public final boolean isSet(InternalThreadLocalMap internalThreadLocalMap) {
        return internalThreadLocalMap != null && internalThreadLocalMap.isIndexedVariableSet(this.index);
    }

    public final V get() {
        return this.get(InternalThreadLocalMap.get());
    }

    private static void addToVariablesToRemove(InternalThreadLocalMap internalThreadLocalMap, FastThreadLocal<?> fastThreadLocal) {
        Set set;
        Object object = internalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (object == InternalThreadLocalMap.UNSET || object == null) {
            set = Collections.newSetFromMap(new IdentityHashMap());
            internalThreadLocalMap.setIndexedVariable(variablesToRemoveIndex, set);
        } else {
            set = (Set)object;
        }
        set.add(fastThreadLocal);
    }

    protected void onRemoval(V v) throws Exception {
    }

    public static void destroy() {
        InternalThreadLocalMap.destroy();
    }

    public static int size() {
        InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.getIfSet();
        if (internalThreadLocalMap == null) {
            return 0;
        }
        return internalThreadLocalMap.size();
    }

    public final void remove(InternalThreadLocalMap internalThreadLocalMap) {
        if (internalThreadLocalMap == null) {
            return;
        }
        Object object = internalThreadLocalMap.removeIndexedVariable(this.index);
        FastThreadLocal.removeFromVariablesToRemove(internalThreadLocalMap, this);
        if (object != InternalThreadLocalMap.UNSET) {
            try {
                this.onRemoval(object);
            }
            catch (Exception exception) {
                PlatformDependent.throwException(exception);
            }
        }
    }

    private static void removeFromVariablesToRemove(InternalThreadLocalMap internalThreadLocalMap, FastThreadLocal<?> fastThreadLocal) {
        Object object = internalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (object == InternalThreadLocalMap.UNSET || object == null) {
            return;
        }
        Set set = (Set)object;
        set.remove(fastThreadLocal);
    }

    public final V get(InternalThreadLocalMap internalThreadLocalMap) {
        Object object = internalThreadLocalMap.indexedVariable(this.index);
        if (object != InternalThreadLocalMap.UNSET) {
            return (V)object;
        }
        return this.initialize(internalThreadLocalMap);
    }

    protected V initialValue() throws Exception {
        return null;
    }

    private V initialize(InternalThreadLocalMap internalThreadLocalMap) {
        V v = null;
        try {
            v = this.initialValue();
        }
        catch (Exception exception) {
            PlatformDependent.throwException(exception);
        }
        internalThreadLocalMap.setIndexedVariable(this.index, v);
        FastThreadLocal.addToVariablesToRemove(internalThreadLocalMap, this);
        return v;
    }

    public static void removeAll() {
        InternalThreadLocalMap internalThreadLocalMap = InternalThreadLocalMap.getIfSet();
        if (internalThreadLocalMap == null) {
            return;
        }
        Object object = internalThreadLocalMap.indexedVariable(variablesToRemoveIndex);
        if (object != null && object != InternalThreadLocalMap.UNSET) {
            FastThreadLocal[] fastThreadLocalArray;
            Set set = (Set)object;
            for (FastThreadLocal fastThreadLocal : fastThreadLocalArray = set.toArray(new FastThreadLocal[set.size()])) {
                fastThreadLocal.remove(internalThreadLocalMap);
            }
        }
        InternalThreadLocalMap.remove();
    }

    public final void remove() {
        this.remove(InternalThreadLocalMap.getIfSet());
    }

    public final void set(InternalThreadLocalMap internalThreadLocalMap, V v) {
        if (v != InternalThreadLocalMap.UNSET) {
            if (internalThreadLocalMap.setIndexedVariable(this.index, v)) {
                FastThreadLocal.addToVariablesToRemove(internalThreadLocalMap, this);
            }
        } else {
            this.remove(internalThreadLocalMap);
        }
    }
}

