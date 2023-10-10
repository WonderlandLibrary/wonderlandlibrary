// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use Interners.new*Interner")
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface Interner<E>
{
    @CanIgnoreReturnValue
    E intern(final E p0);
}
