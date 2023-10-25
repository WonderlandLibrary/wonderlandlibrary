/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.MpscLinkedQueuePad1;
import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef<E>
extends MpscLinkedQueuePad1<E> {
    private static final long serialVersionUID = 8717072462993327429L;
    private volatile transient MpscLinkedQueueNode<E> tailRef;
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;

    protected final MpscLinkedQueueNode<E> tailRef() {
        return this.tailRef;
    }

    MpscLinkedQueueTailRef() {
    }

    protected final void setTailRef(MpscLinkedQueueNode<E> mpscLinkedQueueNode) {
        this.tailRef = mpscLinkedQueueNode;
    }

    protected final MpscLinkedQueueNode<E> getAndSetTailRef(MpscLinkedQueueNode<E> mpscLinkedQueueNode) {
        return UPDATER.getAndSet(this, mpscLinkedQueueNode);
    }

    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, Object> atomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
        if (atomicReferenceFieldUpdater == null) {
            atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
        }
        UPDATER = atomicReferenceFieldUpdater;
    }
}

