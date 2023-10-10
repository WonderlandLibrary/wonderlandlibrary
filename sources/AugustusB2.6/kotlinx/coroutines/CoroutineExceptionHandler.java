// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

public interface CoroutineExceptionHandler extends Element
{
    public static final Key Key = CoroutineExceptionHandler.Key.$$INSTANCE;
    
    public static final class Key implements CoroutineContext.Key<CoroutineExceptionHandler>
    {
        static final /* synthetic */ Key $$INSTANCE;
        
        private Key() {
        }
        
        static {
            $$INSTANCE = new Key();
        }
    }
}
