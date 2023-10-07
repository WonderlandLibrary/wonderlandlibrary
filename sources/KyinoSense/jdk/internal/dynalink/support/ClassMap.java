/*
 * Decompiled with CFR 0.152.
 */
package jdk.internal.dynalink.support;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.dynalink.support.ClassLoaderGetterContextProvider;
import jdk.internal.dynalink.support.Guards;

public abstract class ClassMap<T> {
    private final ConcurrentMap<Class<?>, T> map = new ConcurrentHashMap();
    private final Map<Class<?>, Reference<T>> weakMap = new WeakHashMap();
    private final ClassLoader classLoader;

    protected ClassMap(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    protected abstract T computeValue(Class<?> var1);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public T get(final Class<?> clazz) {
        T refv;
        Reference<T> ref;
        Object v = this.map.get(clazz);
        if (v != null) {
            return (T)v;
        }
        Map<Class<?>, Reference<T>> map = this.weakMap;
        synchronized (map) {
            ref = this.weakMap.get(clazz);
        }
        if (ref != null && (refv = ref.get()) != null) {
            return refv;
        }
        T newV = this.computeValue(clazz);
        assert (newV != null);
        ClassLoader clazzLoader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        }, ClassLoaderGetterContextProvider.GET_CLASS_LOADER_CONTEXT);
        if (Guards.canReferenceDirectly(this.classLoader, clazzLoader)) {
            T oldV = this.map.putIfAbsent(clazz, newV);
            return oldV != null ? oldV : newV;
        }
        Map<Class<?>, Reference<T>> map2 = this.weakMap;
        synchronized (map2) {
            T oldV;
            ref = this.weakMap.get(clazz);
            if (ref != null && (oldV = ref.get()) != null) {
                return oldV;
            }
            this.weakMap.put(clazz, new SoftReference<T>(newV));
            return newV;
        }
    }
}

