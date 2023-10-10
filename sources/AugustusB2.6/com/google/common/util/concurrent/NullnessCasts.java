// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

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
    static <T> T uncheckedNull() {
        return null;
    }
    
    private NullnessCasts() {
    }
}
