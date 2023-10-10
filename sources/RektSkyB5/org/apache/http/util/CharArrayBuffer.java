/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.util;

import java.io.Serializable;
import java.nio.CharBuffer;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.ByteArrayBuffer;

public final class CharArrayBuffer
implements CharSequence,
Serializable {
    private static final long serialVersionUID = -6208952725094867135L;
    private char[] buffer;
    private int len;

    public CharArrayBuffer(int capacity) {
        Args.notNegative(capacity, "Buffer capacity");
        this.buffer = new char[capacity];
    }

    private void expand(int newlen) {
        char[] newbuffer = new char[Math.max(this.buffer.length << 1, newlen)];
        System.arraycopy(this.buffer, 0, newbuffer, 0, this.len);
        this.buffer = newbuffer;
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
        int newlen = this.len + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        System.arraycopy(b2, off, this.buffer, this.len, len);
        this.len = newlen;
    }

    public void append(String str) {
        String s2 = str != null ? str : "null";
        int strlen = s2.length();
        int newlen = this.len + strlen;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        s2.getChars(0, strlen, this.buffer, this.len);
        this.len = newlen;
    }

    public void append(CharArrayBuffer b2, int off, int len) {
        if (b2 == null) {
            return;
        }
        this.append(b2.buffer, off, len);
    }

    public void append(CharArrayBuffer b2) {
        if (b2 == null) {
            return;
        }
        this.append(b2.buffer, 0, b2.len);
    }

    public void append(char ch) {
        int newlen = this.len + 1;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        this.buffer[this.len] = ch;
        this.len = newlen;
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
        int oldlen = this.len;
        int newlen = oldlen + len;
        if (newlen > this.buffer.length) {
            this.expand(newlen);
        }
        int i1 = off;
        for (int i2 = oldlen; i2 < newlen; ++i2) {
            this.buffer[i2] = (char)(b2[i1] & 0xFF);
            ++i1;
        }
        this.len = newlen;
    }

    public void append(ByteArrayBuffer b2, int off, int len) {
        if (b2 == null) {
            return;
        }
        this.append(b2.buffer(), off, len);
    }

    public void append(Object obj) {
        this.append(String.valueOf(obj));
    }

    public void clear() {
        this.len = 0;
    }

    public char[] toCharArray() {
        char[] b2 = new char[this.len];
        if (this.len > 0) {
            System.arraycopy(this.buffer, 0, b2, 0, this.len);
        }
        return b2;
    }

    @Override
    public char charAt(int i2) {
        return this.buffer[i2];
    }

    public char[] buffer() {
        return this.buffer;
    }

    public int capacity() {
        return this.buffer.length;
    }

    @Override
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

    public int indexOf(int ch, int from, int to) {
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
            if (this.buffer[i2] != ch) continue;
            return i2;
        }
        return -1;
    }

    public int indexOf(int ch) {
        return this.indexOf(ch, 0, this.len);
    }

    public String substring(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        }
        if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
        return new String(this.buffer, beginIndex, endIndex - beginIndex);
    }

    public String substringTrimmed(int beginIndex, int endIndex) {
        int beginIndex0;
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        }
        if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
        int endIndex0 = endIndex;
        for (beginIndex0 = beginIndex; beginIndex0 < endIndex && HTTP.isWhitespace(this.buffer[beginIndex0]); ++beginIndex0) {
        }
        while (endIndex0 > beginIndex0 && HTTP.isWhitespace(this.buffer[endIndex0 - 1])) {
            --endIndex0;
        }
        return new String(this.buffer, beginIndex0, endIndex0 - beginIndex0);
    }

    @Override
    public CharSequence subSequence(int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("Negative beginIndex: " + beginIndex);
        }
        if (endIndex > this.len) {
            throw new IndexOutOfBoundsException("endIndex: " + endIndex + " > length: " + this.len);
        }
        if (beginIndex > endIndex) {
            throw new IndexOutOfBoundsException("beginIndex: " + beginIndex + " > endIndex: " + endIndex);
        }
        return CharBuffer.wrap(this.buffer, beginIndex, endIndex);
    }

    @Override
    public String toString() {
        return new String(this.buffer, 0, this.len);
    }
}

