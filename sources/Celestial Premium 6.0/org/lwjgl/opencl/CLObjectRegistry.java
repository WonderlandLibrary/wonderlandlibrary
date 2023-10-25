/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;
import org.lwjgl.PointerWrapperAbstract;
import org.lwjgl.opencl.CLObject;
import org.lwjgl.opencl.CLObjectChild;
import org.lwjgl.opencl.FastLongMap;

class CLObjectRegistry<T extends CLObjectChild> {
    private FastLongMap<T> registry;

    CLObjectRegistry() {
    }

    final boolean isEmpty() {
        return this.registry == null || this.registry.isEmpty();
    }

    final T getObject(long id) {
        return (T)(this.registry == null ? null : (CLObjectChild)this.registry.get(id));
    }

    final boolean hasObject(long id) {
        return this.registry != null && this.registry.containsKey(id);
    }

    final Iterable<FastLongMap.Entry<T>> getAll() {
        return this.registry;
    }

    void registerObject(T object) {
        FastLongMap<T> map = this.getMap();
        Long key = ((PointerWrapperAbstract)object).getPointer();
        if (LWJGLUtil.DEBUG && map.containsKey(key)) {
            throw new IllegalStateException("Duplicate object found: " + object.getClass() + " - " + key);
        }
        this.getMap().put(((PointerWrapperAbstract)object).getPointer(), object);
    }

    void unregisterObject(T object) {
        this.getMap().remove(((CLObject)object).getPointerUnsafe());
    }

    private FastLongMap<T> getMap() {
        if (this.registry == null) {
            this.registry = new FastLongMap();
        }
        return this.registry;
    }
}

