// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;

@DoNotMock("Implement it normally")
@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface ByteProcessor<T>
{
    @CanIgnoreReturnValue
    boolean processBytes(final byte[] p0, final int p1, final int p2) throws IOException;
    
    @ParametricNullness
    T getResult();
}
