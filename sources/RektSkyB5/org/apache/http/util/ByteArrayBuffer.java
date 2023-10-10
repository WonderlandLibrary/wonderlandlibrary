/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.util;

import java.io.Serializable;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

public final class ByteArrayBuffer
implements Serializable {
    private static final long serialVersionUID = 4359112959524048036L;
    private byte[] buffer;
    private int len;

    public ByteArrayBuffer(int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new byte[capacity];
    }

    private void expand(int newlen) {
        byte[] newbuffer = new byte[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
    }

    public void append(byte[] b2, int off, int len) {
        if (b2 == null) {
            return;
        }
        if (off < 0 || off > b2.length || len < 0 || off + len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b2.length);
        }
        if (len == 0) {
            return;
        }
        int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        System.arraycopy(b2, off, this.buffer, this.len, len);
        this.len = newlen;
    }

    public void append(int b2) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        this.buffer[this.len] = (byte)b2;
        this.len = newlen;
    }

    public void append(char[] b2, int off, int len) {
        if (b2 == null) {
            return;
        }
        if (off < 0 || off > b2.length || len < 0 || off + len < 0 || off + len > b2.length) {
            throw new IndexOutOfBoundsException("off: " + off + " len: " + len + " b.length: " + b2.length);
        }
        if (len == 0) {
            return;
        }
        int oldlen = this.len;
        int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        int i1 = off;
        for (int i2 = oldlen; i2 < newlen; ++i2) {
            this.buffer[i2] = (byte)b2[i1];
            ++i1;
        }
        this.len = newlen;
    }

    public void append(CharArrayBuffer b2, int off, int len) {
        if (b2 == null) {
            return;
        }
        this.append(b2.buffer(), off, len);
    }

    public void clear() {
        this.len = 0;
    }

    public byte[] toByteArray() {
        byte[] b2 = new byte[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b2, 0, this.len);
        }
        return b2;
    }

    public int byteAt(int i2) {
        return this.buffer[i2];
    }

    public int capacity() {
        return this.buffer.length;
    }

    public int length() {
        return this.len;
    }

    public void ensureCapacity(int required) {
        if (required <= 0) {
            return;
        }
        int available = this.buffer.length - this.len;
        if (required > available) {
            this.expand(this.len + required);
        }
    }

    public byte[] buffer() {
        return this.buffer;
    }

    public void setLength(int len) {
        if (len < 0 || len > this.buffer.length) {
            throw new IndexOutOfBoundsException("len: " + len + " < 0 or > buffer len: " + this.buffer.length);
        }
        this.len = len;
    }

    public boolean isEmpty() {
        return this.len == 0;
    }

    public boolean isFull() {
        return this.len == this.buffer.length;
    }

    public int indexOf(byte b2, int from, int to) {
        int endIndex;
        int beginIndex = from;
        if (beginIndex < 0) {
            beginIndex = 0;
        }
        if ((endIndex = to) > this.len) {
            endIndex = this.len;
        }
        if (beginIndex > endIndex) {
            return -1;
        }
        for (int i2 = beginIndex; i2 < endIndex; ++i2) {
            if (this.buffer[i2] != b2) continue;
            return i2;
        }
        return -1;
    }

    public int indexOf(byte b2) {
        return this.indexOf(b2, 0, this.len);
    }
}

