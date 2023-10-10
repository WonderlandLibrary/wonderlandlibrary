// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;

@DoNotMock("Use ImmutableClassToInstanceMap or MutableClassToInstanceMap")
@ElementTypesAreNonnullByDefault
@GwtCompatible
public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B>
{
    @CheckForNull
     <T extends B> T getInstance(final Class<T> p0);
    
    @CheckForNull
    @CanIgnoreReturnValue
     <T extends B> T putInstance(final Class<T> p0, final T p1);
}
