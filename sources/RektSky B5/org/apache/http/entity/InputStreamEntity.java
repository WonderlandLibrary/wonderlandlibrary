/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class InputStreamEntity
extends AbstractHttpEntity {
    private final InputStream content;
    private final long length;

    public InputStreamEntity(InputStream instream) {
        this(instream, -1L);
    }

    public InputStreamEntity(InputStream instream, long length) {
        this(instream, length, null);
    }

    public InputStreamEntity(InputStream instream, ContentType contentType) {
        this(instream, -1L, contentType);
    }

    public InputStreamEntity(InputStream instream, long length, ContentType contentType) {
        this.content = Args.notNull(instream, "Source input stream");
        this.length = length;
        if (contentType != null) {
            this.setContentType(contentType.toString());
        }
    }

    @Override
    public boolean isRepeatable() {
        return false;
    }

    @Override
    public long getContentLength() {
        return this.length;
    }

    @Override
    public InputStream getContent() throws IOException {
        return this.content;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        block7: {
            Args.notNull(outstream, "Output stream");
            InputStream instream = this.content;
            try {
                int l2;
                byte[] buffer = new byte[4096];
                if (this.length < 0L) {
                    int l3;
                    while ((l3 = instream.read(buffer)) != -1) {
                        outstream.write(buffer, 0, l3);
                    }
                    break block7;
                }
                for (long remaining = this.length; remaining > 0L; remaining -= (long)l2) {
                    l2 = instream.read(buffer, 0, (int)Math.min(4096L, remaining));
                    if (l2 == -1) {
                        break;
                    }
                    outstream.write(buffer, 0, l2);
                }
            }
            finally {
                instream.close();
            }
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
}

