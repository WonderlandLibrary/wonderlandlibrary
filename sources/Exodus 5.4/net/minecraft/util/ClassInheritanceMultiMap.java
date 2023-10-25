/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 */
package net.minecraft.util;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassInheritanceMultiMap<T>
extends AbstractSet<T> {
    private final Map<Class<?>, List<T>> map = Maps.newHashMap();
    private final List<T> field_181745_e;
    private final Class<T> baseClass;
    private final Set<Class<?>> knownKeys = Sets.newIdentityHashSet();
    private static final Set<Class<?>> field_181158_a = Sets.newHashSet();

    @Override
    public boolean contains(Object object) {
        return Iterators.contains(this.getByClass(object.getClass()).iterator(), (Object)object);
    }

    @Override
    public Iterator<T> iterator() {
        return this.field_181745_e.isEmpty() ? Iterators.emptyIterator() : Iterators.unmodifiableIterator(this.field_181745_e.iterator());
    }

    @Override
    public boolean remove(Object object) {
        Object object2 = object;
        boolean bl = false;
        for (Class<?> clazz : this.knownKeys) {
            List<T> list;
            if (!clazz.isAssignableFrom(object2.getClass()) || (list = this.map.get(clazz)) == null || !list.remove(object2)) continue;
            bl = true;
        }
        return bl;
    }

    public ClassInheritanceMultiMap(Class<T> clazz) {
        this.field_181745_e = Lists.newArrayList();
        this.baseClass = clazz;
        this.knownKeys.add(clazz);
        this.map.put(clazz, this.field_181745_e);
        for (Class<?> clazz2 : field_181158_a) {
            this.createLookup(clazz2);
        }
    }

    protected Class<?> func_181157_b(Class<?> clazz) {
        if (this.baseClass.isAssignableFrom(clazz)) {
            if (!this.knownKeys.contains(clazz)) {
                this.createLookup(clazz);
            }
            return clazz;
        }
        throw new IllegalArgumentException("Don't know how to search for " + clazz);
    }

    @Override
    public boolean add(T t) {
        for (Class<?> clazz : this.knownKeys) {
            if (!clazz.isAssignableFrom(t.getClass())) continue;
            this.func_181743_a(t, clazz);
        }
        return true;
    }

    @Override
    public int size() {
        return this.field_181745_e.size();
    }

    public <S> Iterable<S> getByClass(final Class<S> clazz) {
        return new Iterable<S>(){

            @Override
            public Iterator<S> iterator() {
                List list = (List)ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.func_181157_b(clazz));
                if (list == null) {
                    return Iterators.emptyIterator();
                }
                Iterator iterator = list.iterator();
                return Iterators.filter(iterator, (Class)clazz);
            }
        };
    }

    protected void createLookup(Class<?> clazz) {
        field_181158_a.add(clazz);
        for (T t : this.field_181745_e) {
            if (!clazz.isAssignableFrom(t.getClass())) continue;
            this.func_181743_a(t, clazz);
        }
        this.knownKeys.add(clazz);
    }

    private void func_181743_a(T t, Class<?> clazz) {
        List<T> list = this.map.get(clazz);
        if (list == null) {
            this.map.put(clazz, Lists.newArrayList((Object[])new Object[]{t}));
        } else {
            list.add(t);
        }
    }
}

