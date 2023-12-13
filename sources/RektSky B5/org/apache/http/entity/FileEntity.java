/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.Args;

public class FileEntity
extends AbstractHttpEntity
implements Cloneable {
    protected final File file;

    @Deprecated
    public FileEntity(File file, String contentType) {
        this.file = Args.notNull(file, "File");
        this.setContentType(contentType);
    }

    public FileEntity(File file, ContentType contentType) {
        this.file = Args.notNull(file, "File");
        if (contentType != null) {
            this.setContentType(contentType.toString());
        }
    }

    public FileEntity(File file) {
        this.file = Args.notNull(file, "File");
    }

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return this.file.length();
    }

    @Override
    public InputStream getContent() throws IOException {
        return new FileInputStream(this.file);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        Args.notNull(outstream, "Output stream");
        FileInputStream instream = new FileInputStream(this.file);
        try {
            int l2;
            byte[] tmp = new byte[4096];
            while ((l2 = ((InputStream)instream).read(tmp)) != -1) {
                outstream.write(tmp, 0, l2);
            }
            outstream.flush();
        }
        finally {
            ((InputStream)instream).close();
        }
    }

    @Override
    public boolean isStreaming() {
        return false;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

