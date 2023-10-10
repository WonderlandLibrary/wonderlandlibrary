/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.io;

import org.apache.http.HttpMessage;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.SessionOutputBuffer;

public interface HttpMessageWriterFactory<T extends HttpMessage> {
    public HttpMessageWriter<T> create(SessionOutputBuffer var1);
}

