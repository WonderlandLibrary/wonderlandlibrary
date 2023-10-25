/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http.multipart;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelException;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.http.multipart.HttpData;
import io.netty.util.AbstractReferenceCounted;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public abstract class AbstractHttpData
extends AbstractReferenceCounted
implements HttpData {
    private static final Pattern STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
    protected long size;
    protected Charset charset = HttpConstants.DEFAULT_CHARSET;
    protected long definedSize;
    protected boolean completed;
    private static final Pattern REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
    protected final String name;

    @Override
    public ByteBuf content() {
        try {
            return this.getByteBuf();
        }
        catch (IOException iOException) {
            throw new ChannelException(iOException);
        }
    }

    @Override
    protected void deallocate() {
        this.delete();
    }

    @Override
    public void setCharset(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public HttpData retain() {
        super.retain();
        return this;
    }

    @Override
    public long length() {
        return this.size;
    }

    protected AbstractHttpData(String string, Charset charset, long l) {
        if (string == null) {
            throw new NullPointerException("name");
        }
        string = REPLACE_PATTERN.matcher(string).replaceAll(" ");
        if ((string = STRIP_PATTERN.matcher(string).replaceAll("")).isEmpty()) {
            throw new IllegalArgumentException("empty name");
        }
        this.name = string;
        if (charset != null) {
            this.setCharset(charset);
        }
        this.definedSize = l;
    }

    @Override
    public Charset getCharset() {
        return this.charset;
    }

    @Override
    public HttpData retain(int n) {
        super.retain(n);
        return this;
    }
}

