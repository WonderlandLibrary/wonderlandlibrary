/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.params.HttpParams;

@Deprecated
public interface ClientConnectionManagerFactory {
    public ClientConnectionManager newInstance(HttpParams var1, SchemeRegistry var2);
}

