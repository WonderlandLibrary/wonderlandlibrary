/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;

public interface HttpEntity {
    public boolean isRepeatable();

    public boolean isChunked();

    public long getContentLength();

    public Header getContentType();

    public Header getContentEncoding();

    public InputStream getContent() throws IOException, UnsupportedOperationException;

    public void writeTo(OutputStream var1) throws IOException;

    public boolean isStreaming();

    @Deprecated
    public void consumeContent() throws IOException;
}

