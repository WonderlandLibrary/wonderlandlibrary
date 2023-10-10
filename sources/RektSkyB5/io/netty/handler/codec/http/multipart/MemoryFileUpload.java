/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.multipart.AbstractMemoryHttpData;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.IOException;
import java.nio.charset.Charset;

public class MemoryFileUpload
extends AbstractMemoryHttpData
implements FileUpload {
    private String filename;
    private String contentType;
    private String contentTransferEncoding;

    public MemoryFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size) {
        super(name, charset, size);
        this.setFilename(filename);
        this.setContentType(contentType);
        this.setContentTransferEncoding(contentTransferEncoding);
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.FileUpload;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public void setFilename(String filename) {
        if (filename == null) {
            throw new NullPointerException("filename");
        }
        this.filename = filename;
    }

    public int hashCode() {
        return this.getName().hashCode();
    }

    public boolean equals(Object o2) {
        if (!(o2 instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute)o2;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }

    @Override
    public int compareTo(InterfaceHttpData o2) {
        if (!(o2 instanceof FileUpload)) {
            throw new ClassCastException("Cannot compare " + (Object)((Object)this.getHttpDataType()) + " with " + (Object)((Object)o2.getHttpDataType()));
        }
        return this.compareTo((FileUpload)o2);
    }

    @Override
    public int compareTo(FileUpload o2) {
        int v2 = this.getName().compareToIgnoreCase(o2.getName());
        if (v2 != 0) {
            return v2;
        }
        return v2;
    }

    @Override
    public void setContentType(String contentType) {
        if (contentType == null) {
            throw new NullPointerException("contentType");
        }
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public String getContentTransferEncoding() {
        return this.contentTransferEncoding;
    }

    @Override
    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String toString() {
        return "Content-Disposition: form-data; name=\"" + this.getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + (this.charset != null ? "; charset=" + this.charset + "\r\n" : "\r\n") + "Content-Length" + ": " + this.length() + "\r\n" + "Completed: " + this.isCompleted() + "\r\nIsInMemory: " + this.isInMemory();
    }

    @Override
    public FileUpload copy() {
        MemoryFileUpload upload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        ByteBuf buf = this.content();
        if (buf != null) {
            try {
                upload.setContent(buf.copy());
                return upload;
            }
            catch (IOException e2) {
                throw new ChannelException(e2);
            }
        }
        return upload;
    }

    @Override
    public FileUpload duplicate() {
        MemoryFileUpload upload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        ByteBuf buf = this.content();
        if (buf != null) {
            try {
                upload.setContent(buf.duplicate());
                return upload;
            }
            catch (IOException e2) {
                throw new ChannelException(e2);
            }
        }
        return upload;
    }

    @Override
    public FileUpload retain() {
        super.retain();
        return this;
    }

    @Override
    public FileUpload retain(int increment) {
        super.retain(increment);
        return this;
    }
}

