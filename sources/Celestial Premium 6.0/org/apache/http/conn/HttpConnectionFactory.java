/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import org.apache.http.HttpConnection;
import org.apache.http.config.ConnectionConfig;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public interface HttpConnectionFactory<T, C extends HttpConnection> {
    public C create(T var1, ConnectionConfig var2);
}

