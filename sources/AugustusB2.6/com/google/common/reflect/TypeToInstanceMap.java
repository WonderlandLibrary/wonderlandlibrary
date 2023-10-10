// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;

@DoNotMock("Use ImmutableTypeToInstanceMap or MutableTypeToInstanceMap")
@Beta
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B>
{
     <T extends B> T getInstance(final Class<T> p0);
    
     <T extends B> T getInstance(final TypeToken<T> p0);
    
    @CanIgnoreReturnValue
     <T extends B> T putInstance(final Class<T> p0, final T p1);
    
    @CanIgnoreReturnValue
     <T extends B> T putInstance(final TypeToken<T> p0, final T p1);
}
