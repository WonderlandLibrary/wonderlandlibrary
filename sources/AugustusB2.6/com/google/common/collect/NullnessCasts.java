// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class NullnessCasts
{
    @ParametricNullness
    static <T> T uncheckedCastNullableTToT(@CheckForNull final T t) {
        return t;
    }
    
    @ParametricNullness
    static <T> T unsafeNull() {
        return null;
    }
    
    private NullnessCasts() {
    }
}
