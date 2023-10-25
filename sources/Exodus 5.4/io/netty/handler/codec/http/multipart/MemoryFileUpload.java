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

    @Override
    public void setFilename(String string) {
        if (string == null) {
            throw new NullPointerException("filename");
        }
        this.filename = string;
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        if (!(interfaceHttpData instanceof FileUpload)) {
            throw new ClassCastException("Cannot compare " + (Object)((Object)this.getHttpDataType()) + " with " + (Object)((Object)interfaceHttpData.getHttpDataType()));
        }
        return this.compareTo((FileUpload)interfaceHttpData);
    }

    @Override
    public int compareTo(FileUpload fileUpload) {
        int n = this.getName().compareToIgnoreCase(fileUpload.getName());
        if (n != 0) {
            return n;
        }
        return n;
    }

    @Override
    public FileUpload retain() {
        super.retain();
        return this;
    }

    @Override
    public String getFilename() {
        return this.filename;
    }

    @Override
    public void setContentType(String string) {
        if (string == null) {
            throw new NullPointerException("contentType");
        }
        this.contentType = string;
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return InterfaceHttpData.HttpDataType.FileUpload;
    }

    public String toString() {
        return "Content-Disposition: form-data; name=\"" + this.getName() + "\"; " + "filename" + "=\"" + this.filename + "\"\r\n" + "Content-Type" + ": " + this.contentType + (this.charset != null ? "; charset=" + this.charset + "\r\n" : "\r\n") + "Content-Length" + ": " + this.length() + "\r\n" + "Completed: " + this.isCompleted() + "\r\nIsInMemory: " + this.isInMemory();
    }

    @Override
    public FileUpload retain(int n) {
        super.retain(n);
        return this;
    }

    public MemoryFileUpload(String string, String string2, String string3, String string4, Charset charset, long l) {
        super(string, charset, l);
        this.setFilename(string2);
        this.setContentType(string3);
        this.setContentTransferEncoding(string4);
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribute)) {
            return false;
        }
        Attribute attribute = (Attribute)object;
        return this.getName().equalsIgnoreCase(attribute.getName());
    }

    public int hashCode() {
        return this.getName().hashCode();
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
    public void setContentTransferEncoding(String string) {
        this.contentTransferEncoding = string;
    }

    @Override
    public FileUpload copy() {
        MemoryFileUpload memoryFileUpload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                memoryFileUpload.setContent(byteBuf.copy());
                return memoryFileUpload;
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return memoryFileUpload;
    }

    @Override
    public FileUpload duplicate() {
        MemoryFileUpload memoryFileUpload = new MemoryFileUpload(this.getName(), this.getFilename(), this.getContentType(), this.getContentTransferEncoding(), this.getCharset(), this.size);
        ByteBuf byteBuf = this.content();
        if (byteBuf != null) {
            try {
                memoryFileUpload.setContent(byteBuf.duplicate());
                return memoryFileUpload;
            }
            catch (IOException iOException) {
                throw new ChannelException(iOException);
            }
        }
        return memoryFileUpload;
    }
}

