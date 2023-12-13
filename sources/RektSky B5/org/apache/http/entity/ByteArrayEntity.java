/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class ByteArrayEntity
extends AbstractHttpEntity
implements Cloneable {
    @Deprecated
    protected final byte[] content;
    private final byte[] b;
    private final int off;
    private final int len;

    public ByteArrayEntity(byte[] b2, ContentType contentType) {
        Args.notNull(b2, "Source byte array");
        this.content = b2;
        this.b = b2;
        this.off = 0;
        this.len = this.b.length;
        if (contentType != null) {
            this.setContentType(contentType.toString());
        }
    }

    public ByteArrayEntity(byte[] b2, int off, int len, ContentType contentType) {
        Args.notNull(b2, "Source byte array");
        if (off < 0 || off > b2.length || len < 0 || off + len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b2.length);
        }
        this.content = b2;
        this.b = b2;
        this.off = off;
        this.len = len;
        if (contentType != null) {
            this.setContentType(contentType.toString());
        }
    }

    public ByteArrayEntity(byte[] b2) {
        this(b2, null);
    }

    public ByteArrayEntity(byte[] b2, int off, int len) {
        this(b2, off, len, null);
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return this.len;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(this.b, this.off, this.len);
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        outstream.write(this.b, this.off, this.len);
        outstream.flush();
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

