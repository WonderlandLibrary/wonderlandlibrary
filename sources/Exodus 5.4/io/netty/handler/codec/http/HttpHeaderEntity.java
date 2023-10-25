/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

final class HttpHeaderEntity
implements CharSequence {
    private final int hash;
    private final byte[] bytes;
    private final String name;
    private final int separatorLen;

    @Override
    public int length() {
        return this.bytes.length - this.separatorLen;
    }

    @Override
    public CharSequence subSequence(int n, int n2) {
        return new HttpHeaderEntity(this.name.substring(n, n2));
    }

    public HttpHeaderEntity(String string) {
        this(string, null);
    }

    public HttpHeaderEntity(String string, byte[] byArray) {
        this.name = string;
        this.hash = HttpHeaders.hash(string);
        byte[] byArray2 = string.getBytes(CharsetUtil.US_ASCII);
        if (byArray == null) {
            this.bytes = byArray2;
            this.separatorLen = 0;
        } else {
            this.separatorLen = byArray.length;
            this.bytes = new byte[byArray2.length + byArray.length];
            System.arraycopy(byArray2, 0, this.bytes, 0, byArray2.length);
            System.arraycopy(byArray, 0, this.bytes, byArray2.length, byArray.length);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }

    int hash() {
        return this.hash;
    }

    boolean encode(ByteBuf byteBuf) {
        byteBuf.writeBytes(this.bytes);
        return this.separatorLen > 0;
    }

    @Override
    public char charAt(int n) {
        if (this.bytes.length - this.separatorLen <= n) {
            throw new IndexOutOfBoundsException();
        }
        return (char)this.bytes[n];
    }
}

