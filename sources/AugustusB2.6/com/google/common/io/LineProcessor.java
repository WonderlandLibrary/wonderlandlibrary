// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.Beta;

@ElementTypesAreNonnullByDefault
@Beta
@GwtIncompatible
public interface LineProcessor<T>
{
    @CanIgnoreReturnValue
    boolean processLine(final String p0) throws IOException;
    
    @ParametricNullness
    T getResult();
}
