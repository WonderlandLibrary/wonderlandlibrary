// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;

public interface Job extends Element
{
    public static final Key Key = Job.Key.$$INSTANCE;
    
    boolean isActive();
    
    CancellationException getCancellationException();
    
    boolean start();
    
    ChildHandle attachChild(final ChildJob p0);
    
    DisposableHandle invokeOnCompletion(final Function1<? super Throwable, Unit> p0);
    
    DisposableHandle invokeOnCompletion(final boolean p0, final boolean p1, final Function1<? super Throwable, Unit> p2);
    
    public static final class Key implements CoroutineContext.Key<Job>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        private Key() {
        }
        
        static {
            $$INSTANCE = new Key();
            final CoroutineExceptionHandler.Key key = CoroutineExceptionHandler.Key;
        }
    }
    
    public static final class DefaultImpls
    {
    }
}
