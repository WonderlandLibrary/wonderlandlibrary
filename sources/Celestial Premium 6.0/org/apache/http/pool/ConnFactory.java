/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.pool;

import java.io.IOException;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface ConnFactory<T, C> {
    public C create(T var1) throws IOException;
}

