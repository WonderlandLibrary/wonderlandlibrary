// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

final class CompletedWithCancellation
{
    public final Object result;
    public final Function1<Throwable, Unit> onCancellation;
    
    @Override
    public final String toString() {
        return "CompletedWithCancellation[" + this.result + ']';
    }
}
