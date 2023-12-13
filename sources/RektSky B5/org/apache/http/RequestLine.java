/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import org.apache.http.ProtocolVersion;

public interface RequestLine {
    public String getMethod();

    public ProtocolVersion getProtocolVersion();

    public String getUri();
}

