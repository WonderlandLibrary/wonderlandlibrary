/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.util.Freezable;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Relation<K, V>
implements Freezable<Relation<K, V>> {
    private Map<K, Set<V>> data;
    Constructor<? extends Set<V>> setCreator;
    Object[] setComparatorParam;
    volatile boolean frozen = false;

    public static <K, V> Relation<K, V> of(Map<K, Set<V>> map, Class<?> setCreator) {
        return new Relation<K, V>(map, setCreator);
    }

    public static <K, V> Relation<K, V> of(Map<K, Set<V>> map, Class<?> setCreator, Comparator<V> setComparator) {
        return new Relation<K, V>(map, setCreator, setComparator);
    }

    public Relation(Map<K, Set<V>> map, Class<?> setCreator) {
        this(map, setCreator, null);
    }

    public Relation(Map<K, Set<V>> map, Class<?> setCreator, Comparator<V> setComparator) {
        try {
            Object[] objectArray;
            if (setComparator == null) {
                objectArray = null;
            } else {
                Object[] objectArray2 = new Object[1];
                objectArray = objectArray2;
                objectArray2[0] = setComparator;
            }
            this.setComparatorParam = objectArray;
            if (setComparator == null) {
                this.setCreator = setCreator.getConstructor(new Class[0]);
                this.setCreator.newInstance(this.setComparatorParam);
            } else {
                this.setCreator = setCreator.getConstructor(Comparator.class);
                this.setCreator.newInstance(this.setComparatorParam);
            }
            this.data = map == null ? new HashMap() : map;
        }
        catch (Exception e2) {
            throw (RuntimeException)new IllegalArgumentException("Can't create new set").initCause(e2);
        }
    }

    public void clear() {
        this.data.clear();
    }

    public boolean containsKey(Object key) {
        return this.data.containsKey(key);
    }

    public boolean containsValue(Object value) {
        for (Set<V> values : this.data.values()) {
            if (!values.contains(value)) continue;
            return true;
        }
        return false;
    }

    public final Set<Map.Entry<K, V>> entrySet() {
        return this.keyValueSet();
    }

    public Set<Map.Entry<K, Set<V>>> keyValuesSet() {
        return this.data.entrySet();
    }

    public Set<Map.Entry<K, V>> keyValueSet() {
        LinkedHashSet<Map.Entry<K, V>> result = new LinkedHashSet<Map.Entry<K, V>>();
        for (K key : this.data.keySet()) {
            for (V value : this.data.get(key)) {
                result.add(new SimpleEntry<K, V>(key, value));
            }
        }
        return result;
    }

    public boolean equals(Object o2) {
        if (o2 == null) {
            return false;
        }
        if (o2.getClass() != this.getClass()) {
            return false;
        }
        return this.data.equals(((Relation)o2).data);
    }

    public Set<V> getAll(Object key) {
        return this.data.get(key);
    }

    public Set<V> get(Object key) {
        return this.data.get(key);
    }

    public int hashCode() {
        return this.data.hashCode();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public Set<K> keySet() {
        return this.data.keySet();
    }

    public V put(K key, V value) {
        Set<V> set = this.data.get(key);
        if (set == null) {
            set = this.newSet();
            this.data.put(key, set);
        }
        set.add(value);
        return value;
    }

    public V putAll(K key, Collection<? extends V> values) {
        Set<V> set = this.data.get(key);
        if (set == null) {
            set = this.newSet();
            this.data.put(key, set);
        }
        set.addAll(values);
        return values.size() == 0 ? null : (V)values.iterator().next();
    }

    public V putAll(Collection<K> keys, V value) {
        V result = null;
        for (K key : keys) {
            result = this.put(key, value);
        }
        return result;
    }

    private Set<V> newSet() {
        try {
            return this.setCreator.newInstance(this.setComparatorParam);
        }
        catch (Exception e2) {
            throw (RuntimeException)new IllegalArgumentException("Can't create new set").initCause(e2);
        }
    }

    public void putAll(Map<? extends K, ? extends V> t2) {
        for (Map.Entry<K, V> entry : t2.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public void putAll(Relation<? extends K, ? extends V> t2) {
        for (K key : t2.keySet()) {
            for (V value : t2.getAll(key)) {
                this.put(key, value);
            }
        }
    }

    public Set<V> removeAll(K key) {
        try {
            return this.data.remove(key);
        }
        catch (NullPointerException e2) {
            return null;
        }
    }

    public boolean remove(K key, V value) {
        try {
            Set<V> set = this.data.get(key);
            if (set == null) {
                return false;
            }
            boolean result = set.remove(value);
            if (set.size() == 0) {
                this.data.remove(key);
            }
            return result;
        }
        catch (NullPointerException e2) {
            return false;
        }
    }

    public int size() {
        return this.data.size();
    }

    public Set<V> values() {
        return this.values(new LinkedHashSet());
    }

    public <C extends Collection<V>> C values(C result) {
        for (Map.Entry<K, Set<V>> keyValue : this.data.entrySet()) {
            result.addAll((Collection)keyValue.getValue());
        }
        return result;
    }

    public String toString() {
        return this.data.toString();
    }

    public Relation<K, V> addAllInverted(Relation<V, K> source) {
        for (K value : source.data.keySet()) {
            for (V key : source.data.get(value)) {
                this.put(key, value);
            }
        }
        return this;
    }

    public Relation<K, V> addAllInverted(Map<V, K> source) {
        for (Map.Entry<V, K> entry : source.entrySet()) {
            this.put(entry.getValue(), entry.getKey());
        }
        return this;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public Relation<K, V> freeze() {
        if (!this.frozen) {
            for (K key : this.data.keySet()) {
                this.data.put(key, Collections.unmodifiableSet(this.data.get(key)));
            }
            this.data = Collections.unmodifiableMap(this.data);
            this.frozen = true;
        }
        return this;
    }

    @Override
    public Relation<K, V> cloneAsThawed() {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Relation<K, V> toBeRemoved) {
        boolean result = false;
        for (K key : toBeRemoved.keySet()) {
            try {
                Set<V> values = toBeRemoved.getAll(key);
                if (values == null) continue;
                result |= this.removeAll(key, (Iterable<V>)values);
            }
            catch (NullPointerException nullPointerException) {}
        }
        return result;
    }

    public Set<V> removeAll(K ... keys) {
        return this.removeAll((Collection<K>)Arrays.asList(keys));
    }

    public boolean removeAll(K key, Iterable<V> toBeRemoved) {
        boolean result = false;
        for (V value : toBeRemoved) {
            result |= this.remove(key, value);
        }
        return result;
    }

    public Set<V> removeAll(Collection<K> toBeRemoved) {
        LinkedHashSet<V> result = new LinkedHashSet<V>();
        for (K key : toBeRemoved) {
            try {
                Set<V> removals = this.data.remove(key);
                if (removals == null) continue;
                result.addAll(removals);
            }
            catch (NullPointerException nullPointerException) {}
        }
        return result;
    }

    static class SimpleEntry<K, V>
    implements Map.Entry<K, V> {
        K key;
        V value;

        public SimpleEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public SimpleEntry(Map.Entry<K, V> e2) {
            this.key = e2.getKey();
            this.value = e2.getValue();
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }
}

