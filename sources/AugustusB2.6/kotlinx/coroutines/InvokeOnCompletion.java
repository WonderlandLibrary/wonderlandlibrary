// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

final class InvokeOnCompletion extends JobNode<Job>
{
    private final Function1<Throwable, Unit> handler;
    
    @Override
    public final void invoke(final Throwable cause) {
        this.handler.invoke(cause);
    }
    
    @Override
    public final String toString() {
        return "InvokeOnCompletion[" + Pixmap.getClassSimpleName(this) + '@' + Pixmap.getHexAddress(this) + ']';
    }
    
    public InvokeOnCompletion(final Job job, final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(job, "job");
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        super(job);
        this.handler = (Function1<Throwable, Unit>)handler;
    }
}
