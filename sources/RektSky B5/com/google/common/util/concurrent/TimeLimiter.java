/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Beta
public interface TimeLimiter {
    public <T> T newProxy(T var1, Class<T> var2, long var3, TimeUnit var5);

    public <T> T callWithTimeout(Callable<T> var1, long var2, TimeUnit var4, boolean var5) throws Exception;
}

