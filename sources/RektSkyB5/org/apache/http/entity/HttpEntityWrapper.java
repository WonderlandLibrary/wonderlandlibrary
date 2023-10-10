/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.util.Args;

public class HttpEntityWrapper
implements HttpEntity {
    protected HttpEntity wrappedEntity;

    public HttpEntityWrapper(HttpEntity wrappedEntity) {
        this.wrappedEntity = Args.notNull(wrappedEntity, "Wrapped entity");
    }

    @Override
    public boolean isRepeatable() {
        return this.wrappedEntity.isRepeatable();
    }

    @Override
    public boolean isChunked() {
        return this.wrappedEntity.isChunked();
    }

    @Override
    public long getContentLength() {
        return this.wrappedEntity.getContentLength();
    }

    @Override
    public Header getContentType() {
        return this.wrappedEntity.getContentType();
    }

    @Override
    public Header getContentEncoding() {
        return this.wrappedEntity.getContentEncoding();
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.wrappedEntity.getContent();
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        this.wrappedEntity.writeTo(outstream);
    }

    @Override
    public boolean isStreaming() {
        return this.wrappedEntity.isStreaming();
    }

    @Override
    @Deprecated
    public void consumeContent() throws IOException {
        this.wrappedEntity.consumeContent();
    }
}

