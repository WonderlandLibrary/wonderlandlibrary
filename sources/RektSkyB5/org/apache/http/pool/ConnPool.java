/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.pool;

import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;

public interface ConnPool<T, E> {
    public Future<E> lease(T var1, Object var2, FutureCallback<E> var3);

    public void release(E var1, boolean var2);
}

