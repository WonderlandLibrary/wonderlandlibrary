// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class LockFreeTaskQueue<E>
{
    public volatile /* synthetic */ Object _cur$internal;
    public static final /* synthetic */ AtomicReferenceFieldUpdater _cur$FU$internal;
    
    public final void close() {
        final LockFreeTaskQueue $this$loop$iv = this;
        LockFreeTaskQueueCore cur;
        while (!(cur = (LockFreeTaskQueueCore)$this$loop$iv._cur$internal).close()) {
            final AtomicReferenceFieldUpdater cur$FU$internal = LockFreeTaskQueue._cur$FU$internal;
            final LockFreeTaskQueueCore lockFreeTaskQueueCore = cur;
            cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
        }
    }
    
    public final boolean addLast(final E element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        final LockFreeTaskQueue $this$loop$iv = this;
        while (true) {
            final LockFreeTaskQueueCore cur;
            switch ((cur = (LockFreeTaskQueueCore)$this$loop$iv._cur$internal).addLast(element)) {
                case 0: {
                    return true;
                }
                case 2: {
                    return false;
                }
                case 1: {
                    final AtomicReferenceFieldUpdater cur$FU$internal = LockFreeTaskQueue._cur$FU$internal;
                    final LockFreeTaskQueueCore lockFreeTaskQueueCore = cur;
                    cur$FU$internal.compareAndSet(this, lockFreeTaskQueueCore, lockFreeTaskQueueCore.next());
                    continue;
                }
            }
        }
    }
    
    public final E removeFirstOrNull() {
        final LockFreeTaskQueue this_$iv = this;
        final LockFreeTaskQueue $this$loop$iv$iv = this;
        Object result$iv = null;
    Label_0231_Outer:
        while (true) {
            final LockFreeTaskQueueCore cur$iv;
            final LockFreeTaskQueueCore $this$loop$iv$iv$iv;
            final LockFreeTaskQueueCore<Object> lockFreeTaskQueueCore = (LockFreeTaskQueueCore<Object>)($this$loop$iv$iv$iv = (cur$iv = (LockFreeTaskQueueCore)$this$loop$iv$iv._cur$internal));
            while (true) {
                long state$iv$iv;
                while (((state$iv$iv = $this$loop$iv$iv$iv._state$internal) & 0x1000000000000000L) == 0x0L) {
                    final LockFreeTaskQueueCore.Companion companion = LockFreeTaskQueueCore.Companion;
                    final int head$iv$iv$iv = (int)(state$iv$iv & 0x3FFFFFFFL);
                    Object remove_FROZEN$4fdbb1f;
                    Object o;
                    if (((int)((state$iv$iv & 0xFFFFFFFC0000000L) >> 30) & LockFreeTaskQueueCore.access$getMask$p(lockFreeTaskQueueCore)) == (head$iv$iv$iv & LockFreeTaskQueueCore.access$getMask$p(lockFreeTaskQueueCore))) {
                        o = (remove_FROZEN$4fdbb1f = null);
                    }
                    else {
                        final Object element$iv$iv;
                        if ((element$iv$iv = lockFreeTaskQueueCore.array$internal.get(head$iv$iv$iv & LockFreeTaskQueueCore.access$getMask$p(lockFreeTaskQueueCore))) == null) {
                            if (!LockFreeTaskQueueCore.access$getSingleConsumer$p(lockFreeTaskQueueCore)) {
                                continue Label_0231_Outer;
                            }
                            o = (remove_FROZEN$4fdbb1f = null);
                        }
                        else if (element$iv$iv instanceof LockFreeTaskQueueCore.Placeholder) {
                            o = (remove_FROZEN$4fdbb1f = null);
                        }
                        else {
                            final int newHead$iv$iv = head$iv$iv$iv + 1 & 0x3FFFFFFF;
                            if (LockFreeTaskQueueCore._state$FU$internal.compareAndSet(lockFreeTaskQueueCore, state$iv$iv, LockFreeTaskQueueCore.Companion.updateHead(state$iv$iv, newHead$iv$iv))) {
                                lockFreeTaskQueueCore.array$internal.set(head$iv$iv$iv & LockFreeTaskQueueCore.access$getMask$p(lockFreeTaskQueueCore), null);
                                o = (remove_FROZEN$4fdbb1f = element$iv$iv);
                            }
                            else {
                                if (!LockFreeTaskQueueCore.access$getSingleConsumer$p(lockFreeTaskQueueCore)) {
                                    continue Label_0231_Outer;
                                }
                                LockFreeTaskQueueCore cur$iv$iv = lockFreeTaskQueueCore;
                                while (true) {
                                    final LockFreeTaskQueueCore<Object> access$removeSlowPath = LockFreeTaskQueueCore.access$removeSlowPath(cur$iv$iv, head$iv$iv$iv, newHead$iv$iv);
                                    if (access$removeSlowPath == null) {
                                        break;
                                    }
                                    cur$iv$iv = access$removeSlowPath;
                                }
                                o = (remove_FROZEN$4fdbb1f = element$iv$iv);
                            }
                        }
                    }
                    result$iv = remove_FROZEN$4fdbb1f;
                    if (o != LockFreeTaskQueueCore.REMOVE_FROZEN$4fdbb1f) {
                        break Label_0231_Outer;
                    }
                    final LockFreeTaskQueue lockFreeTaskQueue = this_$iv;
                    final AtomicReferenceFieldUpdater cur$FU$internal = LockFreeTaskQueue._cur$FU$internal;
                    final LockFreeTaskQueueCore lockFreeTaskQueueCore2 = cur$iv;
                    cur$FU$internal.compareAndSet(lockFreeTaskQueue, lockFreeTaskQueueCore2, lockFreeTaskQueueCore2.next());
                    continue Label_0231_Outer;
                }
                Object remove_FROZEN$4fdbb1f;
                Object o = remove_FROZEN$4fdbb1f = LockFreeTaskQueueCore.REMOVE_FROZEN$4fdbb1f;
                continue;
            }
        }
        return (E)result$iv;
    }
    
    public LockFreeTaskQueue(final boolean singleConsumer) {
        this._cur$internal = new LockFreeTaskQueueCore(8, false);
    }
    
    static {
        _cur$FU$internal = AtomicReferenceFieldUpdater.newUpdater(LockFreeTaskQueue.class, Object.class, "_cur$internal");
    }
}
