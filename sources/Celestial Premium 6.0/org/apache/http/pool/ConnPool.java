/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import java.util.concurrent.Future;
import org.apache.http.concurrent.FutureCallback;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ConnPool<T, E> {
    public Future<E> lease(T var1, Object var2, FutureCallback<E> var3);

    public void release(E var1, boolean var2);
}

