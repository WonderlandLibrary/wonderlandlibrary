// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

final class InvokeOnCancelling extends JobCancellingNode<Job>
{
    private volatile int _invoked;
    private static final AtomicIntegerFieldUpdater _invoked$FU;
    private final Function1<Throwable, Unit> handler;
    
    @Override
    public final void invoke(final Throwable cause) {
        if (InvokeOnCancelling._invoked$FU.compareAndSet(this, 0, 1)) {
            this.handler.invoke(cause);
        }
    }
    
    @Override
    public final String toString() {
        return "InvokeOnCancelling[" + Pixmap.getClassSimpleName(this) + '@' + Pixmap.getHexAddress(this) + ']';
    }
    
    public InvokeOnCancelling(final Job job, final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        super(job);
        this.handler = (Function1<Throwable, Unit>)handler;
        this._invoked = 0;
    }
    
    static {
        _invoked$FU = AtomicIntegerFieldUpdater.newUpdater(InvokeOnCancelling.class, "_invoked");
    }
}
