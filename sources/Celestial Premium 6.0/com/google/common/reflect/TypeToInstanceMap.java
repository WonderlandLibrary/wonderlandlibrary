/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.reflect;

import com.google.common.annotations.Beta;
import com.google.common.reflect.TypeToken;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import javax.annotation.Nullable;

@Beta
public interface TypeToInstanceMap<B>
extends Map<TypeToken<? extends B>, B> {
    @Nullable
    public <T extends B> T getInstance(Class<T> var1);

    @Nullable
    @CanIgnoreReturnValue
    public <T extends B> T putInstance(Class<T> var1, @Nullable T var2);

    @Nullable
    public <T extends B> T getInstance(TypeToken<T> var1);

    @Nullable
    @CanIgnoreReturnValue
    public <T extends B> T putInstance(TypeToken<T> var1, @Nullable T var2);
}

