// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlinx.coroutines.DebugKt;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class AtomicOp<T> extends OpDescriptor
{
    private volatile Object _consensus;
    private static final AtomicReferenceFieldUpdater _consensus$FU;
    
    public abstract Object prepare(final T p0);
    
    public abstract void complete(final T p0, final Object p1);
    
    @Override
    public final Object perform(final Object affected) {
        Object decision;
        if ((decision = this._consensus) == AtomicKt.access$getNO_DECISION$p()) {
            final Object prepare = this.prepare(affected);
            decision = this;
            final Object o = prepare;
            if (DebugKt.getASSERTIONS_ENABLED() && o == AtomicKt.access$getNO_DECISION$p()) {
                throw new AssertionError();
            }
            decision = (AtomicOp._consensus$FU.compareAndSet(this, AtomicKt.access$getNO_DECISION$p(), o) ? prepare : ((AtomicOp)decision)._consensus);
        }
        this.complete(affected, decision);
        return decision;
    }
    
    public AtomicOp() {
        this._consensus = AtomicKt.access$getNO_DECISION$p();
    }
    
    static {
        _consensus$FU = AtomicReferenceFieldUpdater.newUpdater(AtomicOp.class, Object.class, "_consensus");
    }
}
