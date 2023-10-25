/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http;

import org.apache.http.ProtocolVersion;

public interface StatusLine {
    public ProtocolVersion getProtocolVersion();

    public int getStatusCode();

    public String getReasonPhrase();
}

