/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
final class FunctionalEquivalence<F, T>
extends Equivalence<F>
implements Serializable {
    private static final long serialVersionUID = 0L;
    private final Function<F, ? extends T> function;
    private final Equivalence<T> resultEquivalence;

    FunctionalEquivalence(Function<F, ? extends T> function, Equivalence<T> resultEquivalence) {
        this.function = Preconditions.checkNotNull(function);
        this.resultEquivalence = Preconditions.checkNotNull(resultEquivalence);
    }

    @Override
    protected boolean doEquivalent(F a2, F b2) {
        return this.resultEquivalence.equivalent(this.function.apply(a2), this.function.apply(b2));
    }

    @Override
    protected int doHash(F a2) {
        return this.resultEquivalence.hash(this.function.apply(a2));
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof FunctionalEquivalence) {
            FunctionalEquivalence that = (FunctionalEquivalence)obj;
            return this.function.equals(that.function) && this.resultEquivalence.equals(that.resultEquivalence);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hashCode(this.function, this.resultEquivalence);
    }

    public String toString() {
        return this.resultEquivalence + ".onResultOf(" + this.function + ")";
    }
}

