/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

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

    public void put(T key, int value) {
        this.identityMap.put(key, value);
        while (true) {
            if (this.objectList.size() > value) {
                this.objectList.set(value, key);
                return;
            }
            this.objectList.add(null);
        }
    }

    public int get(T key) {
        Integer integer = this.identityMap.get(key);
        if (integer == null) {
            return -1;
        }
        int n = integer;
        return n;
    }

    public final T getByValue(int value) {
        T t;
        if (value >= 0 && value < this.objectList.size()) {
            t = this.objectList.get(value);
            return t;
        }
        t = null;
        return t;
    }

    @Override
    public Iterator<T> iterator() {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }
}

