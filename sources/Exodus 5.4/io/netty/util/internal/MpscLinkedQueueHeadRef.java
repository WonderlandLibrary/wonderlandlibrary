/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.MpscLinkedQueueNode;
import io.netty.util.internal.MpscLinkedQueuePad0;
import io.netty.util.internal.PlatformDependent;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueHeadRef<E>
extends MpscLinkedQueuePad0<E>
implements Serializable {
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;
    private volatile transient MpscLinkedQueueNode<E> headRef;
    private static final long serialVersionUID = 8467054865577874285L;

    protected final void lazySetHeadRef(MpscLinkedQueueNode<E> mpscLinkedQueueNode) {
        UPDATER.lazySet(this, mpscLinkedQueueNode);
    }

    MpscLinkedQueueHeadRef() {
    }

    protected final MpscLinkedQueueNode<E> headRef() {
        return this.headRef;
    }

    protected final void setHeadRef(MpscLinkedQueueNode<E> mpscLinkedQueueNode) {
        this.headRef = mpscLinkedQueueNode;
    }

    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, Object> atomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
        if (atomicReferenceFieldUpdater == null) {
            atomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
        }
        UPDATER = atomicReferenceFieldUpdater;
    }
}

