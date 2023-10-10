// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

final class InvokeOnCancel extends CancelHandler
{
    private final Function1<Throwable, Unit> handler;
    
    @Override
    public final void invoke(final Throwable cause) {
        this.handler.invoke(cause);
    }
    
    @Override
    public final String toString() {
        return "InvokeOnCancel[" + Pixmap.getClassSimpleName(this.handler) + '@' + Pixmap.getHexAddress(this) + ']';
    }
    
    public InvokeOnCancel(final Function1<? super Throwable, Unit> handler) {
        Intrinsics.checkParameterIsNotNull(handler, "handler");
        this.handler = (Function1<Throwable, Unit>)handler;
    }
}
