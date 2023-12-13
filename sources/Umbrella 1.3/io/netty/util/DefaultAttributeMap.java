/*
 * Decompiled with CFR 0.150.
 */
package io.netty.util;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap
implements AttributeMap {
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, Map> updater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, Map.class, "map");
    private volatile Map<AttributeKey<?>, Attribute<?>> map;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public <T> Attribute<T> attr(AttributeKey<T> key) {
        Map<AttributeKey<?>, Attribute<?>> map = this.map;
        if (map == null && !updater.compareAndSet(this, null, map = new IdentityHashMap(2))) {
            map = this.map;
        }
        Map<AttributeKey<?>, Attribute<?>> map2 = map;
        synchronized (map2) {
            Attribute<?> attr = map.get(key);
            if (attr == null) {
                attr = new DefaultAttribute(map, key);
                map.put(key, attr);
            }
            return attr;
        }
    }

    private static final class DefaultAttribute<T>
    extends AtomicReference<T>
    implements Attribute<T> {
        private static final long serialVersionUID = -2661411462200283011L;
        private final Map<AttributeKey<?>, Attribute<?>> map;
        private final AttributeKey<T> key;

        DefaultAttribute(Map<AttributeKey<?>, Attribute<?>> map, AttributeKey<T> key) {
            this.map = map;
            this.key = key;
        }

        @Override
        public AttributeKey<T> key() {
            return this.key;
        }

        @Override
        public T setIfAbsent(T value) {
            while (!this.compareAndSet(null, value)) {
                Object old = this.get();
                if (old == null) continue;
                return (T)old;
            }
            return null;
        }

        @Override
        public T getAndRemove() {
            T oldValue = this.getAndSet(null);
            this.remove0();
            return oldValue;
        }

        @Override
        public void remove() {
            this.set(null);
            this.remove0();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void remove0() {
            Map<AttributeKey<?>, Attribute<?>> map = this.map;
            synchronized (map) {
                this.map.remove(this.key);
            }
        }
    }
}

