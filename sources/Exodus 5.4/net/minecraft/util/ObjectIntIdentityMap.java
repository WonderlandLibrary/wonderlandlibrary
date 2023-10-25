/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 */
package net.minecraft.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.IObjectIntIterable;

public class ObjectIntIdentityMap<T>
implements IObjectIntIterable<T> {
    private final IdentityHashMap<T, Integer> identityMap = new IdentityHashMap(512);
    private final List<T> objectList = Lists.newArrayList();

    public int get(T t) {
        Integer n = this.identityMap.get(t);
        return n == null ? -1 : n;
    }

    public void put(T t, int n) {
        this.identityMap.put(t, n);
        while (this.objectList.size() <= n) {
            this.objectList.add(null);
        }
        this.objectList.set(n, t);
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.filter(this.objectList.iterator(), (Predicate)Predicates.notNull());
    }

    public final T getByValue(int n) {
        return n >= 0 && n < this.objectList.size() ? (T)this.objectList.get(n) : null;
    }
}

