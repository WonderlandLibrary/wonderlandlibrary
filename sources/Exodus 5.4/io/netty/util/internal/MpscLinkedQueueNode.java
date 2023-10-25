/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode<T> {
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> nextUpdater;
    private volatile MpscLinkedQueueNode<T> next;

    public abstract T value();

    final void setNext(MpscLinkedQueueNode<T> mpscLinkedQueueNode) {
        nextUpdater.lazySet(this, mpscLinkedQueueNode);
    }

    void unlink() {
        this.setNext(null);
    }

    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueNode, Object> atomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
        if (atomicReferenceFieldUpdater == null) {
            atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next");
        }
        nextUpdater = atomicReferenceFieldUpdater;
    }

    protected T clearMaybe() {
        return this.value();
    }

    final MpscLinkedQueueNode<T> next() {
        return this.next;
    }
}

