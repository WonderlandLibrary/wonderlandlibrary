/*
 * Decompiled with CFR 0.150.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

final class HttpHeaderEntity
implements CharSequence {
    private final String name;
    private final int hash;
    private final byte[] bytes;

    public HttpHeaderEntity(String name) {
        this.name = name;
        this.hash = HttpHeaders.hash(name);
        this.bytes = name.getBytes(CharsetUtil.US_ASCII);
    }

    int hash() {
        return this.hash;
    }

    @Override
    public int length() {
        return this.bytes.length;
    }

    @Override
    public char charAt(int index) {
        return (char)this.bytes[index];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new HttpHeaderEntity(this.name.substring(start, end));
    }

    @Override
    public String toString() {
        return this.name;
    }

    void encode(ByteBuf buf) {
        buf.writeBytes(this.bytes);
    }
}

