// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;

public final class JobSupportKt
{
    private static final CoroutineContext.Element.DefaultImpls SEALED$4fdbb1f;
    private static final Empty EMPTY_NEW;
    private static final Empty EMPTY_ACTIVE;
    
    public static final Object boxIncomplete(final Object $this$boxIncomplete) {
        if ($this$boxIncomplete instanceof Incomplete) {
            return new IncompleteStateBox((Incomplete)$this$boxIncomplete);
        }
        return $this$boxIncomplete;
    }
    
    public static final Object unboxState(final Object $this$unboxState) {
        Object o = $this$unboxState;
        if (!($this$unboxState instanceof IncompleteStateBox)) {
            o = null;
        }
        final IncompleteStateBox incompleteStateBox = (IncompleteStateBox)o;
        Object state;
        if (incompleteStateBox == null || (state = incompleteStateBox.state) == null) {
            state = $this$unboxState;
        }
        return state;
    }
    
    static {
        SEALED$4fdbb1f = new CoroutineContext.Element.DefaultImpls("SEALED");
        EMPTY_NEW = new Empty(false);
        EMPTY_ACTIVE = new Empty(true);
    }
}
