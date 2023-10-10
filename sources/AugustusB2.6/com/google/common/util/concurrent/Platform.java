// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
final class Platform
{
    static boolean isInstanceOfThrowableClass(@CheckForNull final Throwable t, final Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }
    
    private Platform() {
    }
}
