/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.collection;

public interface IntObjectMap<V> {
    public V remove(int var1);

    public boolean containsKey(int var1);

    public V[] values(Class<V> var1);

    public boolean containsValue(V var1);

    public V get(int var1);

    public V put(int var1, V var2);

    public Iterable<Entry<V>> entries();

    public int[] keys();

    public void putAll(IntObjectMap<V> var1);

    public boolean isEmpty();

    public void clear();

    public int size();

    public static interface Entry<V> {
        public int key();

        public void setValue(V var1);

        public V value();
    }
}

