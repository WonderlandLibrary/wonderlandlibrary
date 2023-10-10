// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.scheduling;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlinx.coroutines.internal.LockFreeTaskQueueCore;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeTaskQueue;

public final class GlobalQueue extends LockFreeTaskQueue<Task>
{
    public final Task removeFirstWithModeOrNull(final TaskMode mode) {
        Intrinsics.checkParameterIsNotNull(mode, "mode");
        final LockFreeTaskQueue this_$iv = this;
        final LockFreeTaskQueue $this$loop$iv$iv = this;
        Object result$iv = null;
    Label_0265_Outer:
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
                                continue Label_0265_Outer;
                            }
                            o = (remove_FROZEN$4fdbb1f = null);
                        }
                        else if (element$iv$iv instanceof LockFreeTaskQueueCore.Placeholder) {
                            o = (remove_FROZEN$4fdbb1f = null);
                        }
                        else if (((Task)element$iv$iv).getMode() != mode) {
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
                                    continue Label_0265_Outer;
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
                        break Label_0265_Outer;
                    }
                    final LockFreeTaskQueue lockFreeTaskQueue = this_$iv;
                    final AtomicReferenceFieldUpdater cur$FU$internal = LockFreeTaskQueue._cur$FU$internal;
                    final LockFreeTaskQueueCore lockFreeTaskQueueCore2 = cur$iv;
                    cur$FU$internal.compareAndSet(lockFreeTaskQueue, lockFreeTaskQueueCore2, lockFreeTaskQueueCore2.next());
                    continue Label_0265_Outer;
                }
                Object remove_FROZEN$4fdbb1f;
                Object o = remove_FROZEN$4fdbb1f = LockFreeTaskQueueCore.REMOVE_FROZEN$4fdbb1f;
                continue;
            }
        }
        return (Task)result$iv;
    }
    
    public GlobalQueue() {
        super(false);
    }
}
