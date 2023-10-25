/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DiskAttribute;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MixedAttribute
implements Attribute {
    private Attribute attribute;
    private final long limitSize;

    @Override
    public boolean release() {
        return this.attribute.release();
    }

    @Override
    public boolean renameTo(File file) throws IOException {
        return this.attribute.renameTo(file);
    }

    @Override
    public String getValue() throws IOException {
        return this.attribute.getValue();
    }

    @Override
    public Attribute retain() {
        this.attribute.retain();
        return this;
    }

    @Override
    public void setContent(InputStream inputStream) throws IOException {
        if (this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(inputStream);
    }

    @Override
    public Charset getCharset() {
        return this.attribute.getCharset();
    }

    @Override
    public ByteBuf content() {
        return this.attribute.content();
    }

    @Override
    public boolean isCompleted() {
        return this.attribute.isCompleted();
    }

    @Override
    public byte[] get() throws IOException {
        return this.attribute.get();
    }

    @Override
    public Attribute retain(int n) {
        this.attribute.retain(n);
        return this;
    }

    @Override
    public Attribute duplicate() {
        return this.attribute.duplicate();
    }

    @Override
    public File getFile() throws IOException {
        return this.attribute.getFile();
    }

    @Override
    public void setValue(String string) throws IOException {
        this.attribute.setValue(string);
    }

    @Override
    public String getString(Charset charset) throws IOException {
        return this.attribute.getString(charset);
    }

    @Override
    public void setCharset(Charset charset) {
        this.attribute.setCharset(charset);
    }

    @Override
    public boolean isInMemory() {
        return this.attribute.isInMemory();
    }

    @Override
    public Attribute copy() {
        return this.attribute.copy();
    }

    @Override
    public void setContent(ByteBuf byteBuf) throws IOException {
        if ((long)byteBuf.readableBytes() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(byteBuf);
    }

    @Override
    public ByteBuf getChunk(int n) throws IOException {
        return this.attribute.getChunk(n);
    }

    @Override
    public void setContent(File file) throws IOException {
        if (file.length() > this.limitSize && this.attribute instanceof MemoryAttribute) {
            this.attribute = new DiskAttribute(this.attribute.getName());
        }
        this.attribute.setContent(file);
    }

    @Override
    public long length() {
        return this.attribute.length();
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.attribute.getHttpDataType();
    }

    @Override
    public int refCnt() {
        return this.attribute.refCnt();
    }

    public MixedAttribute(String string, long l) {
        this.limitSize = l;
        this.attribute = new MemoryAttribute(string);
    }

    public MixedAttribute(String string, String string2, long l) {
        this.limitSize = l;
        if ((long)string2.length() > this.limitSize) {
            try {
                this.attribute = new DiskAttribute(string, string2);
            }
            catch (IOException iOException) {
                try {
                    this.attribute = new MemoryAttribute(string, string2);
                }
                catch (IOException iOException2) {
                    throw new IllegalArgumentException(iOException);
                }
            }
        } else {
            try {
                this.attribute = new MemoryAttribute(string, string2);
            }
            catch (IOException iOException) {
                throw new IllegalArgumentException(iOException);
            }
        }
    }

    @Override
    public boolean release(int n) {
        return this.attribute.release(n);
    }

    @Override
    public void delete() {
        this.attribute.delete();
    }

    @Override
    public String getName() {
        return this.attribute.getName();
    }

    @Override
    public String getString() throws IOException {
        return this.attribute.getString();
    }

    @Override
    public void addContent(ByteBuf byteBuf, boolean bl) throws IOException {
        if (this.attribute instanceof MemoryAttribute && this.attribute.length() + (long)byteBuf.readableBytes() > this.limitSize) {
            DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName());
            if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
                diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
            }
            this.attribute = diskAttribute;
        }
        this.attribute.addContent(byteBuf, bl);
    }

    public String toString() {
        return "Mixed: " + this.attribute.toString();
    }

    @Override
    public ByteBuf getByteBuf() throws IOException {
        return this.attribute.getByteBuf();
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        return this.attribute.compareTo(interfaceHttpData);
    }
}

