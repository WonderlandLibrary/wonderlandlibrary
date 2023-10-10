// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlinx.coroutines.DebugKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.TypeCastException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class LockFreeLinkedListNode
{
    volatile Object _next;
    static final AtomicReferenceFieldUpdater _next$FU;
    volatile Object _prev;
    private static AtomicReferenceFieldUpdater _prev$FU;
    private volatile Object _removedRef;
    private static final AtomicReferenceFieldUpdater _removedRef$FU;
    
    private final Removed removed() {
        Removed removed;
        if ((removed = (Removed)this._removedRef) == null) {
            final Removed it = new Removed(this);
            LockFreeLinkedListNode._removedRef$FU.lazySet(this, it);
            removed = it;
        }
        return removed;
    }
    
    public final boolean isRemoved() {
        return this.getNext() instanceof Removed;
    }
    
    public final Object getNext() {
        final LockFreeLinkedListNode $this$loop$iv = this;
        Object next;
        while ((next = $this$loop$iv._next) instanceof OpDescriptor) {
            ((OpDescriptor)next).perform(this);
        }
        return next;
    }
    
    public final LockFreeLinkedListNode getNextNode() {
        return LockFreeLinkedListKt.unwrap(this.getNext());
    }
    
    public final Object getPrev() {
        final LockFreeLinkedListNode $this$loop$iv = this;
        Object prev;
        while (!((prev = $this$loop$iv._prev) instanceof Removed)) {
            if (prev == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
            if (((LockFreeLinkedListNode)prev).getNext() == this) {
                return prev;
            }
            this.correctPrev((LockFreeLinkedListNode)prev, null);
        }
        return prev;
    }
    
    public final LockFreeLinkedListNode getPrevNode() {
        return LockFreeLinkedListKt.unwrap(this.getPrev());
    }
    
    public final boolean addOneIfEmpty(final LockFreeLinkedListNode node) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        LockFreeLinkedListNode._prev$FU.lazySet(node, this);
        LockFreeLinkedListNode._next$FU.lazySet(node, this);
        while (this.getNext() == this) {
            if (LockFreeLinkedListNode._next$FU.compareAndSet(this, this, node)) {
                node.finishAdd(this);
                return true;
            }
        }
        return false;
    }
    
    public final int tryCondAddNext(final LockFreeLinkedListNode node, final LockFreeLinkedListNode next, final CondAddOp condAdd) {
        Intrinsics.checkParameterIsNotNull(node, "node");
        Intrinsics.checkParameterIsNotNull(next, "next");
        Intrinsics.checkParameterIsNotNull(condAdd, "condAdd");
        LockFreeLinkedListNode._prev$FU.lazySet(node, this);
        LockFreeLinkedListNode._next$FU.lazySet(node, next);
        condAdd.oldNext = next;
        if (!LockFreeLinkedListNode._next$FU.compareAndSet(this, next, condAdd)) {
            return 0;
        }
        if (condAdd.perform(this) == null) {
            return 1;
        }
        return 2;
    }
    
    public boolean remove() {
        Object next;
        while (!((next = this.getNext()) instanceof Removed)) {
            if (next == this) {
                return false;
            }
            final Object o = next;
            if (o == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
            }
            final Removed removed = ((LockFreeLinkedListNode)o).removed();
            if (!LockFreeLinkedListNode._next$FU.compareAndSet(this, next, removed)) {
                continue;
            }
            final LockFreeLinkedListNode lockFreeLinkedListNode = (LockFreeLinkedListNode)next;
            LockFreeLinkedListNode lockFreeLinkedListNode2 = null;
            LockFreeLinkedListNode lockFreeLinkedListNode3 = this.markPrev();
            Object next2;
            if ((next2 = this._next) == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Removed");
            }
        Label_0257:
            while (true) {
                final LockFreeLinkedListNode ref = ((Removed)next2).ref;
                Object next3;
                while (!((next3 = ref.getNext()) instanceof Removed)) {
                    final Object next4;
                    if ((next4 = lockFreeLinkedListNode3.getNext()) instanceof Removed) {
                        if (lockFreeLinkedListNode2 != null) {
                            lockFreeLinkedListNode3.markPrev();
                            LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode2, lockFreeLinkedListNode3, ((Removed)next4).ref);
                            lockFreeLinkedListNode3 = lockFreeLinkedListNode2;
                            lockFreeLinkedListNode2 = null;
                        }
                        else {
                            lockFreeLinkedListNode3 = LockFreeLinkedListKt.unwrap(lockFreeLinkedListNode3._prev);
                        }
                    }
                    else if (next4 != this) {
                        lockFreeLinkedListNode2 = lockFreeLinkedListNode3;
                        final LockFreeLinkedListNode lockFreeLinkedListNode4 = (LockFreeLinkedListNode)next4;
                        if (lockFreeLinkedListNode4 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                        }
                        if ((lockFreeLinkedListNode3 = lockFreeLinkedListNode4) != ref) {
                            continue;
                        }
                        break Label_0257;
                    }
                    else {
                        if (!LockFreeLinkedListNode._next$FU.compareAndSet(lockFreeLinkedListNode3, this, ref)) {
                            continue;
                        }
                        break Label_0257;
                    }
                }
                ref.markPrev();
                next2 = next3;
            }
            lockFreeLinkedListNode.correctPrev(LockFreeLinkedListKt.unwrap(this._prev), null);
            return true;
        }
        return false;
    }
    
    private final void finishAdd(final LockFreeLinkedListNode next) {
        final LockFreeLinkedListNode $this$loop$iv = next;
        Object nextPrev = null;
        while (!((nextPrev = $this$loop$iv._prev) instanceof Removed) && this.getNext() == next) {
            if (LockFreeLinkedListNode._prev$FU.compareAndSet(next, nextPrev, this)) {
                if (this.getNext() instanceof Removed) {
                    final Object o = nextPrev;
                    if (o == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                    }
                    next.correctPrev((LockFreeLinkedListNode)o, null);
                }
            }
        }
    }
    
    private final LockFreeLinkedListNode markPrev() {
        final LockFreeLinkedListNode $this$loop$iv = this;
        Object prev;
        while (!((prev = $this$loop$iv._prev) instanceof Removed)) {
            LockFreeLinkedListNode head;
            if (prev == this) {
                head = this.findHead();
            }
            else {
                final Object o = prev;
                if (o == null) {
                    throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                }
                head = (LockFreeLinkedListNode)o;
            }
            final Removed removedPrev = head.removed();
            if (LockFreeLinkedListNode._prev$FU.compareAndSet(this, prev, removedPrev)) {
                return (LockFreeLinkedListNode)prev;
            }
        }
        return ((Removed)prev).ref;
    }
    
    private final LockFreeLinkedListNode findHead() {
        Object cur = this;
        while (!(cur instanceof LockFreeLinkedListHead)) {
            cur = LockFreeLinkedListKt.unwrap(((LockFreeLinkedListNode)(cur = cur)).getNext());
            if (DebugKt.getASSERTIONS_ENABLED() && cur == this) {
                throw new AssertionError();
            }
        }
        return (LockFreeLinkedListNode)cur;
    }
    
    private final LockFreeLinkedListNode correctPrev(LockFreeLinkedListNode _prev, final OpDescriptor op) {
        _prev = _prev;
        LockFreeLinkedListNode last = null;
        Object prevNext;
        while ((prevNext = _prev._next) != null) {
            if (prevNext instanceof OpDescriptor) {
                ((OpDescriptor)prevNext).perform(_prev);
            }
            else if (prevNext instanceof Removed) {
                if (last != null) {
                    _prev.markPrev();
                    LockFreeLinkedListNode._next$FU.compareAndSet(last, _prev, ((Removed)prevNext).ref);
                    _prev = last;
                    last = null;
                }
                else {
                    _prev = LockFreeLinkedListKt.unwrap(_prev._prev);
                }
            }
            else {
                final Object oldPrev;
                if ((oldPrev = this._prev) instanceof Removed) {
                    return null;
                }
                if (prevNext != this) {
                    last = _prev;
                    final Object o = prevNext;
                    if (o == null) {
                        throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.internal.Node /* = kotlinx.coroutines.internal.LockFreeLinkedListNode */");
                    }
                    _prev = (LockFreeLinkedListNode)o;
                }
                else {
                    if (oldPrev == _prev) {
                        return null;
                    }
                    if (LockFreeLinkedListNode._prev$FU.compareAndSet(this, oldPrev, _prev) && !(_prev._prev instanceof Removed)) {
                        return null;
                    }
                    continue;
                }
            }
        }
        return _prev;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '@' + Integer.toHexString(System.identityHashCode(this));
    }
    
    public LockFreeLinkedListNode() {
        this._next = this;
        this._prev = this;
        this._removedRef = null;
    }
    
    static {
        _next$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_next");
        LockFreeLinkedListNode._prev$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_prev");
        _removedRef$FU = AtomicReferenceFieldUpdater.newUpdater(LockFreeLinkedListNode.class, Object.class, "_removedRef");
    }
    
    public abstract static class CondAddOp extends AtomicOp<LockFreeLinkedListNode>
    {
        public LockFreeLinkedListNode oldNext;
        private LockFreeLinkedListNode newNode;
        
        public CondAddOp(final LockFreeLinkedListNode newNode) {
            Intrinsics.checkParameterIsNotNull(newNode, "newNode");
            this.newNode = newNode;
        }
    }
}
