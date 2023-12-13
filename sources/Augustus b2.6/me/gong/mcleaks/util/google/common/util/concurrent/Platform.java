// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.util.concurrent;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
    static boolean isInstanceOfThrowableClass(@Nullable final Throwable t, final Class<? extends Throwable> expectedClass) {
        return expectedClass.isInstance(t);
    }
    
    private Platform() {
    }
}
