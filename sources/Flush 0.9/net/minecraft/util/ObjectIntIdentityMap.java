package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class ObjectIntIdentityMap implements IObjectIntIterable {
    private final IdentityHashMap<Object, Integer> identityMap = new IdentityHashMap(512);
    private final List<Object> objectList = Lists.newArrayList();

    public void put(Object key, int value) {
        this.identityMap.put(key, value);

        while (this.objectList.size() <= value)
            this.objectList.add(null);

        this.objectList.set(value, key);
    }

    public int get(Object key) {
        Integer integer = (Integer) this.identityMap.get(key);
        return integer == null ? -1 : integer.intValue();
    }

    public final Object getByValue(int value) {
        return value >= 0 && value < this.objectList.size() ? this.objectList.get(value) : null;
    }

    public @NotNull Iterator iterator() {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }

    public List<Object> getObjectList() {
        return this.objectList;
    }
}