/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.events;

import java.util.logging.Level;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.RecompilableScriptFunctionData;
import jdk.nashorn.internal.runtime.RewriteException;
import jdk.nashorn.internal.runtime.events.RuntimeEvent;

public final class RecompilationEvent
extends RuntimeEvent<RewriteException> {
    private final Object returnValue;

    public RecompilationEvent(Level level, RewriteException rewriteException, Object returnValue) {
        super(level, rewriteException);
        assert (Context.getContext().getLogger(RecompilableScriptFunctionData.class).isEnabled()) : "Unit test/instrumentation purpose only: RecompilationEvent instances should not be created without '--log=recompile', or we will leak memory in the general case";
        this.returnValue = returnValue;
    }

    public Object getReturnValue() {
        return this.returnValue;
    }
}

