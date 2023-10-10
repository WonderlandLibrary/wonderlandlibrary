// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Use an instance of one of the Finalizable*Reference classes")
@ElementTypesAreNonnullByDefault
@GwtIncompatible
public interface FinalizableReference
{
    void finalizeReferent();
}
