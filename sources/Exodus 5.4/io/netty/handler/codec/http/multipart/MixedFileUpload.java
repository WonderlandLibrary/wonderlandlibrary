/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.DiskFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class MixedFileUpload
implements FileUpload {
    private final long limitSize;
    private FileUpload fileUpload;
    private final long definedSize;

    @Override
    public void setCharset(Charset charset) {
        this.fileUpload.setCharset(charset);
    }

    @Override
    public Charset getCharset() {
        return this.fileUpload.getCharset();
    }

    @Override
    public String getContentType() {
        return this.fileUpload.getContentType();
    }

    @Override
    public String getString() throws IOException {
        return this.fileUpload.getString();
    }

    @Override
    public int refCnt() {
        return this.fileUpload.refCnt();
    }

    @Override
    public String getName() {
        return this.fileUpload.getName();
    }

    @Override
    public InterfaceHttpData.HttpDataType getHttpDataType() {
        return this.fileUpload.getHttpDataType();
    }

    @Override
    public int compareTo(InterfaceHttpData interfaceHttpData) {
        return this.fileUpload.compareTo(interfaceHttpData);
    }

    @Override
    public boolean isCompleted() {
        return this.fileUpload.isCompleted();
    }

    @Override
    public void setContentTransferEncoding(String string) {
        this.fileUpload.setContentTransferEncoding(string);
    }

    @Override
    public ByteBuf content() {
        return this.fileUpload.content();
    }

    @Override
    public ByteBuf getByteBuf() throws IOException {
        return this.fileUpload.getByteBuf();
    }

    @Override
    public File getFile() throws IOException {
        return this.fileUpload.getFile();
    }

    public MixedFileUpload(String string, String string2, String string3, String string4, Charset charset, long l, long l2) {
        this.limitSize = l2;
        this.fileUpload = l > this.limitSize ? new DiskFileUpload(string, string2, string3, string4, charset, l) : new MemoryFileUpload(string, string2, string3, string4, charset, l);
        this.definedSize = l;
    }

    @Override
    public void setFilename(String string) {
        this.fileUpload.setFilename(string);
    }

    @Override
    public ByteBuf getChunk(int n) throws IOException {
        return this.fileUpload.getChunk(n);
    }

    @Override
    public boolean renameTo(File file) throws IOException {
        return this.fileUpload.renameTo(file);
    }

    @Override
    public FileUpload duplicate() {
        return this.fileUpload.duplicate();
    }

    @Override
    public void delete() {
        this.fileUpload.delete();
    }

    @Override
    public byte[] get() throws IOException {
        return this.fileUpload.get();
    }

    @Override
    public FileUpload copy() {
        return this.fileUpload.copy();
    }

    @Override
    public long length() {
        return this.fileUpload.length();
    }

    @Override
    public boolean isInMemory() {
        return this.fileUpload.isInMemory();
    }

    @Override
    public String getString(Charset charset) throws IOException {
        return this.fileUpload.getString(charset);
    }

    @Override
    public boolean release(int n) {
        return this.fileUpload.release(n);
    }

    @Override
    public void setContent(InputStream inputStream) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload) {
            FileUpload fileUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize);
            fileUpload.release();
        }
        this.fileUpload.setContent(inputStream);
    }

    @Override
    public FileUpload retain(int n) {
        this.fileUpload.retain(n);
        return this;
    }

    @Override
    public String getFilename() {
        return this.fileUpload.getFilename();
    }

    @Override
    public String getContentTransferEncoding() {
        return this.fileUpload.getContentTransferEncoding();
    }

    @Override
    public void setContent(File file) throws IOException {
        if (file.length() > this.limitSize && this.fileUpload instanceof MemoryFileUpload) {
            FileUpload fileUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(fileUpload.getName(), fileUpload.getFilename(), fileUpload.getContentType(), fileUpload.getContentTransferEncoding(), fileUpload.getCharset(), this.definedSize);
            fileUpload.release();
        }
        this.fileUpload.setContent(file);
    }

    public String toString() {
        return "Mixed: " + this.fileUpload.toString();
    }

    @Override
    public boolean release() {
        return this.fileUpload.release();
    }

    @Override
    public void addContent(ByteBuf byteBuf, boolean bl) throws IOException {
        if (this.fileUpload instanceof MemoryFileUpload && this.fileUpload.length() + (long)byteBuf.readableBytes() > this.limitSize) {
            DiskFileUpload diskFileUpload = new DiskFileUpload(this.fileUpload.getName(), this.fileUpload.getFilename(), this.fileUpload.getContentType(), this.fileUpload.getContentTransferEncoding(), this.fileUpload.getCharset(), this.definedSize);
            ByteBuf byteBuf2 = this.fileUpload.getByteBuf();
            if (byteBuf2 != null && byteBuf2.isReadable()) {
                diskFileUpload.addContent((ByteBuf)byteBuf2.retain(), false);
            }
            this.fileUpload.release();
            this.fileUpload = diskFileUpload;
        }
        this.fileUpload.addContent(byteBuf, bl);
    }

    @Override
    public void setContentType(String string) {
        this.fileUpload.setContentType(string);
    }

    @Override
    public void setContent(ByteBuf byteBuf) throws IOException {
        if ((long)byteBuf.readableBytes() > this.limitSize && this.fileUpload instanceof MemoryFileUpload) {
            FileUpload fileUpload = this.fileUpload;
            this.fileUpload = new DiskFileUpload(fileUpload.getName(), fileUpload.getFilename(), fileUpload.getContentType(), fileUpload.getContentTransferEncoding(), fileUpload.getCharset(), this.definedSize);
            fileUpload.release();
        }
        this.fileUpload.setContent(byteBuf);
    }

    @Override
    public FileUpload retain() {
        this.fileUpload.retain();
        return this;
    }
}

