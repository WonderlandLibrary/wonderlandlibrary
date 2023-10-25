/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.errorprone.annotations.CanIgnoreReturnValue
 */
package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
public interface ClassToInstanceMap<B>
extends Map<Class<? extends B>, B> {
    @CanIgnoreReturnValue
    public <T extends B> T getInstance(Class<T> var1);

    @CanIgnoreReturnValue
    public <T extends B> T putInstance(Class<T> var1, @Nullable T var2);
}

