/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public interface HttpData
extends ByteBufHolder,
InterfaceHttpData {
    @Override
    public HttpData duplicate();

    public void addContent(ByteBuf var1, boolean var2) throws IOException;

    public File getFile() throws IOException;

    public boolean isCompleted();

    public String getString(Charset var1) throws IOException;

    @Override
    public HttpData retain();

    public void delete();

    public ByteBuf getChunk(int var1) throws IOException;

    @Override
    public HttpData copy();

    public void setContent(ByteBuf var1) throws IOException;

    public long length();

    public ByteBuf getByteBuf() throws IOException;

    public byte[] get() throws IOException;

    public String getString() throws IOException;

    public boolean isInMemory();

    public void setContent(InputStream var1) throws IOException;

    public void setContent(File var1) throws IOException;

    @Override
    public HttpData retain(int var1);

    public void setCharset(Charset var1);

    public boolean renameTo(File var1) throws IOException;

    public Charset getCharset();
}

