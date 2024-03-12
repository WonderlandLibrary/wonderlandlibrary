// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.concurrent.ConcurrentHashMap;
import net.optifine.util.IteratorCache;
import java.util.Collections;
import com.google.common.collect.Iterators;
import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.AbstractSet;

public class ClassInheritanceMultiMap<T> extends AbstractSet<T>
{
    private static final Set<Class<?>> field_181158_a;
    private final Map<Class<?>, List<T>> map;
    private final Set<Class<?>> knownKeys;
    private final Class<T> baseClass;
    private final List<T> values;
    public boolean empty;
    
    public ClassInheritanceMultiMap(final Class<T> baseClassIn) {
        this.map = (Map<Class<?>, List<T>>)Maps.newHashMap();
        this.knownKeys = (Set<Class<?>>)Sets.newIdentityHashSet();
        this.values = (List<T>)Lists.newArrayList();
        this.baseClass = baseClassIn;
        this.knownKeys.add(baseClassIn);
        this.map.put((Class<?>)baseClassIn, (List<?>)this.values);
        for (final Class<?> oclass : ClassInheritanceMultiMap.field_181158_a) {
            this.createLookup(oclass);
        }
        this.empty = (this.values.size() == 0);
    }
    
    protected void createLookup(final Class<?> clazz) {
        ClassInheritanceMultiMap.field_181158_a.add(clazz);
        for (int i = this.values.size(), j = 0; j < i; ++j) {
            final T t = this.values.get(j);
            if (clazz.isAssignableFrom(t.getClass())) {
                this.addForClass(t, clazz);
            }
        }
        this.knownKeys.add(clazz);
    }
    
    protected Class<?> initializeClassLookup(final Class<?> clazz) {
        if (this.baseClass.isAssignableFrom(clazz)) {
            if (!this.knownKeys.contains(clazz)) {
                this.createLookup(clazz);
            }
            return clazz;
        }
        throw new IllegalArgumentException("Don't know how to search for " + clazz);
    }
    
    @Override
    public boolean add(final T p_add_1_) {
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(p_add_1_.getClass())) {
                this.addForClass(p_add_1_, oclass);
            }
        }
        this.empty = (this.values.size() == 0);
        return true;
    }
    
    private void addForClass(final T value, final Class<?> parentClass) {
        final List<T> list = this.map.get(parentClass);
        if (list == null) {
            this.map.put(parentClass, Lists.newArrayList(new Object[] { value }));
        }
        else {
            list.add(value);
        }
        this.empty = (this.values.size() == 0);
    }
    
    @Override
    public boolean remove(final Object p_remove_1_) {
        final T t = (T)p_remove_1_;
        boolean flag = false;
        for (final Class<?> oclass : this.knownKeys) {
            if (oclass.isAssignableFrom(t.getClass())) {
                final List<T> list = this.map.get(oclass);
                if (list == null || !list.remove(t)) {
                    continue;
                }
                flag = true;
            }
        }
        this.empty = (this.values.size() == 0);
        return flag;
    }
    
    @Override
    public boolean contains(final Object p_contains_1_) {
        return Iterators.contains((Iterator)this.getByClass(p_contains_1_.getClass()).iterator(), p_contains_1_);
    }
    
    public <S> Iterable<S> getByClass(final Class<S> clazz) {
        return new Iterable<S>() {
            @Override
            public Iterator<S> iterator() {
                final List<T> list = ClassInheritanceMultiMap.this.map.get(ClassInheritanceMultiMap.this.initializeClassLookup(clazz));
                if (list == null) {
                    return Collections.emptyIterator();
                }
                final Iterator<T> iterator = list.iterator();
                return (Iterator<S>)Iterators.filter((Iterator)iterator, clazz);
            }
        };
    }
    
    @Override
    public Iterator<T> iterator() {
        return (Iterator<T>)(this.values.isEmpty() ? Collections.emptyIterator() : IteratorCache.getReadOnly(this.values));
    }
    
    @Override
    public int size() {
        return this.values.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.empty;
    }
    
    static {
        field_181158_a = Collections.newSetFromMap(new ConcurrentHashMap<Class<?>, Boolean>());
    }
}
