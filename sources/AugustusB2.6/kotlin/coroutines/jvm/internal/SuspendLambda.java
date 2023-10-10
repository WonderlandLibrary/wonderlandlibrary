// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.coroutines.jvm.internal;

import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.jvm.internal.FunctionBase;

public abstract class SuspendLambda extends ContinuationImpl implements FunctionBase<Object>
{
    private final int arity;
    
    @Override
    public String toString() {
        if (this.getCompletion() == null) {
            final String renderLambdaToString = Reflection.renderLambdaToString(this);
            Intrinsics.checkExpressionValueIsNotNull(renderLambdaToString, "Reflection.renderLambdaToString(this)");
            return renderLambdaToString;
        }
        return super.toString();
    }
    
    @Override
    public final int getArity() {
        return this.arity;
    }
    
    public SuspendLambda(final int arity, final Continuation<Object> completion) {
        super(completion);
        this.arity = 2;
    }
}
