// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.jvm.internal.Intrinsics;
import com.badlogic.gdx.graphics.Pixmap;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class CompletedExceptionally
{
    private volatile int _handled;
    private static final AtomicIntegerFieldUpdater _handled$FU;
    public final Throwable cause;
    
    public final boolean getHandled() {
        return this._handled != 0;
    }
    
    public final boolean makeHandled() {
        return CompletedExceptionally._handled$FU.compareAndSet(this, 0, 1);
    }
    
    @Override
    public String toString() {
        return Pixmap.getClassSimpleName(this) + '[' + this.cause + ']';
    }
    
    public CompletedExceptionally(final Throwable cause, final boolean handled) {
        Intrinsics.checkParameterIsNotNull(cause, "cause");
        this.cause = cause;
        this._handled = (handled ? 1 : 0);
    }
    
    static {
        _handled$FU = AtomicIntegerFieldUpdater.newUpdater(CompletedExceptionally.class, "_handled");
    }
}
