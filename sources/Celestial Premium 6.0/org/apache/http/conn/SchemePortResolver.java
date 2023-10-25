/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.conn;

import org.apache.http.HttpHost;
import org.apache.http.conn.UnsupportedSchemeException;

public interface SchemePortResolver {
    public int resolve(HttpHost var1) throws UnsupportedSchemeException;
}

