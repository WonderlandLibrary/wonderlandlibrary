/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentProducer;
import org.apache.http.util.Args;

public class EntityTemplate
extends AbstractHttpEntity {
    private final ContentProducer contentproducer;

    public EntityTemplate(ContentProducer contentproducer) {
        this.contentproducer = Args.notNull(contentproducer, "Content producer");
    }

    @Override
    public long getContentLength() {
        return -1L;
    }

    @Override
    public InputStream getContent() throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        this.writeTo(buf);
        return new ByteArrayInputStream(buf.toByteArray());
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        this.contentproducer.writeTo(outstream);
    }

    @Override
    public boolean isStreaming() {
        return false;
    }
}

