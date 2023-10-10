// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;
import java.util.Map;

@GwtCompatible
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B>
{
    @CanIgnoreReturnValue
     <T extends B> T getInstance(final Class<T> p0);
    
    @CanIgnoreReturnValue
     <T extends B> T putInstance(final Class<T> p0, @Nullable final T p1);
}
