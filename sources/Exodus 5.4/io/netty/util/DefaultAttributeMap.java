/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.AttributeMap;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class DefaultAttributeMap
implements AttributeMap {
    private volatile AtomicReferenceArray<DefaultAttribute<?>> attributes;
    private static final int MASK = 3;
    private static final AtomicReferenceFieldUpdater<DefaultAttributeMap, AtomicReferenceArray> updater;
    private static final int BUCKET_SIZE = 4;

    private static int index(AttributeKey<?> attributeKey) {
        return attributeKey.id() & 3;
    }

    static {
        AtomicReferenceFieldUpdater<DefaultAttributeMap, Object> atomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(DefaultAttributeMap.class, "attributes");
        if (atomicReferenceFieldUpdater == null) {
            atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultAttributeMap.class, AtomicReferenceArray.class, "attributes");
        }
        updater = atomicReferenceFieldUpdater;
    }

    @Override
    public <T> Attribute<T> attr(AttributeKey<T> attributeKey) {
        int n;
        DefaultAttribute<Object> defaultAttribute;
        if (attributeKey == null) {
            throw new NullPointerException("key");
        }
        AtomicReferenceArray<DefaultAttribute<Object>> atomicReferenceArray = this.attributes;
        if (atomicReferenceArray == null && !updater.compareAndSet(this, null, atomicReferenceArray = new AtomicReferenceArray(4))) {
            atomicReferenceArray = this.attributes;
        }
        if ((defaultAttribute = atomicReferenceArray.get(n = DefaultAttributeMap.index(attributeKey))) == null) {
            defaultAttribute = new DefaultAttribute<T>(attributeKey);
            if (atomicReferenceArray.compareAndSet(n, null, defaultAttribute)) {
                return defaultAttribute;
            }
            defaultAttribute = atomicReferenceArray.get(n);
        }
        DefaultAttribute<?> defaultAttribute2 = defaultAttribute;
        synchronized (defaultAttribute2) {
            DefaultAttribute defaultAttribute3 = defaultAttribute;
            while (defaultAttribute3.removed || defaultAttribute3.key != attributeKey) {
                DefaultAttribute defaultAttribute4 = defaultAttribute3.next;
                if (defaultAttribute4 == null) {
                    DefaultAttribute<T> defaultAttribute5 = new DefaultAttribute<T>(defaultAttribute, attributeKey);
                    defaultAttribute3.next = (DefaultAttribute)defaultAttribute5;
                    ((DefaultAttribute)defaultAttribute5).prev = defaultAttribute3;
                    return defaultAttribute5;
                }
                defaultAttribute3 = defaultAttribute4;
            }
            return defaultAttribute3;
        }
    }

    private static final class DefaultAttribute<T>
    extends AtomicReference<T>
    implements Attribute<T> {
        private static final long serialVersionUID = -2661411462200283011L;
        private DefaultAttribute<?> prev;
        private volatile boolean removed;
        private final DefaultAttribute<?> head;
        private final AttributeKey<T> key;
        private DefaultAttribute<?> next;

        @Override
        public void remove() {
            this.removed = true;
            this.set(null);
            this.remove0();
        }

        DefaultAttribute(DefaultAttribute<?> defaultAttribute, AttributeKey<T> attributeKey) {
            this.head = defaultAttribute;
            this.key = attributeKey;
        }

        @Override
        public T setIfAbsent(T t) {
            while (!this.compareAndSet(null, t)) {
                Object v = this.get();
                if (v == null) continue;
                return (T)v;
            }
            return null;
        }

        @Override
        public T getAndRemove() {
            this.removed = true;
            T t = this.getAndSet(null);
            this.remove0();
            return t;
        }

        private void remove0() {
            DefaultAttribute<?> defaultAttribute = this.head;
            synchronized (defaultAttribute) {
                if (this.prev != null) {
                    this.prev.next = this.next;
                    if (this.next != null) {
                        this.next.prev = this.prev;
                    }
                }
            }
        }

        DefaultAttribute(AttributeKey<T> attributeKey) {
            this.head = this;
            this.key = attributeKey;
        }

        @Override
        public AttributeKey<T> key() {
            return this.key;
        }
    }
}

