/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.auth;

import org.apache.http.auth.AuthScheme;
import org.apache.http.params.HttpParams;

@Deprecated
public interface AuthSchemeFactory {
    public AuthScheme newInstance(HttpParams var1);
}

