// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;
import java.io.Serializable;

@ElementTypesAreNonnullByDefault
@Beta
@GwtCompatible
final class FunctionalEquivalence<F, T> extends Equivalence<F> implements Serializable
{
    private static final long serialVersionUID = 0L;
    private final Function<? super F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;
    
    FunctionalEquivalence(final Function<? super F, ? extends T> function, final Equivalence<T> resultEquivalence) {
        this.function = Preconditions.checkNotNull(function);
        this.resultEquivalence = Preconditions.checkNotNull(resultEquivalence);
    }
    
    @Override
    protected boolean doEquivalent(final F a, final F b) {
        return this.resultEquivalence.equivalent((T)this.function.apply((Object)a), (T)this.function.apply((Object)b));
    }
    
    @Override
    protected int doHash(final F a) {
        return this.resultEquivalence.hash((T)this.function.apply((Object)a));
    }
    
    @Override
    public boolean equals(@CheckForNull final Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionalEquivalence) {
            final FunctionalEquivalence<?, ?> that = (FunctionalEquivalence<?, ?>)obj;
            return this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.function, this.resultEquivalence);
    }
    
    @Override
    public String toString() {
        final String value = String.valueOf(this.resultEquivalence);
        final String value2 = String.valueOf(this.function);
        return new StringBuilder(13 + String.valueOf(value).length() + String.valueOf(value2).length()).append(value).append(".onResultOf(").append(value2).append(")").toString();
    }
}
