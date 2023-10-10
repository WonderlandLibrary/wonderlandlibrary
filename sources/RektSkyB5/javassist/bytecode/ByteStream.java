/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import java.io.IOException;
import java.io.OutputStream;

final class ByteStream
extends OutputStream {
    private byte[] buf;
    private int count;

    public ByteStream() {
        this(32);
    }

    public ByteStream(int size) {
        this.buf = new byte[size];
        this.count = 0;
    }

    public int getPos() {
        return this.count;
    }

    public int size() {
        return this.count;
    }

    public void writeBlank(int len) {
        this.enlarge(len);
        this.count += len;
    }

    @Override
    public void write(byte[] data) {
        this.write(data, 0, data.length);
    }

    @Override
    public void write(byte[] data, int off, int len) {
        this.enlarge(len);
        System.arraycopy(data, off, this.buf, this.count, len);
        this.count += len;
    }

    @Override
    public void write(int b2) {
        this.enlarge(1);
        int oldCount = this.count;
        this.buf[oldCount] = (byte)b2;
        this.count = oldCount + 1;
    }

    public void writeShort(int s2) {
        this.enlarge(2);
        int oldCount = this.count;
        this.buf[oldCount] = (byte)(s2 >>> 8);
        this.buf[oldCount + 1] = (byte)s2;
        this.count = oldCount + 2;
    }

    public void writeInt(int i2) {
        this.enlarge(4);
        int oldCount = this.count;
        this.buf[oldCount] = (byte)(i2 >>> 24);
        this.buf[oldCount + 1] = (byte)(i2 >>> 16);
        this.buf[oldCount + 2] = (byte)(i2 >>> 8);
        this.buf[oldCount + 3] = (byte)i2;
        this.count = oldCount + 4;
    }

    public void writeLong(long i2) {
        this.enlarge(8);
        int oldCount = this.count;
        this.buf[oldCount] = (byte)(i2 >>> 56);
        this.buf[oldCount + 1] = (byte)(i2 >>> 48);
        this.buf[oldCount + 2] = (byte)(i2 >>> 40);
        this.buf[oldCount + 3] = (byte)(i2 >>> 32);
        this.buf[oldCount + 4] = (byte)(i2 >>> 24);
        this.buf[oldCount + 5] = (byte)(i2 >>> 16);
        this.buf[oldCount + 6] = (byte)(i2 >>> 8);
        this.buf[oldCount + 7] = (byte)i2;
        this.count = oldCount + 8;
    }

    public void writeFloat(float v2) {
        this.writeInt(Float.floatToIntBits(v2));
    }

    public void writeDouble(double v2) {
        this.writeLong(Double.doubleToLongBits(v2));
    }

    public void writeUTF(String s2) {
        int sLen = s2.length();
        int pos = this.count;
        this.enlarge(sLen + 2);
        byte[] buffer = this.buf;
        buffer[pos++] = (byte)(sLen >>> 8);
        buffer[pos++] = (byte)sLen;
        for (int i2 = 0; i2 < sLen; ++i2) {
            char c2 = s2.charAt(i2);
            if ('\u0001' > c2 || c2 > '\u007f') {
                this.writeUTF2(s2, sLen, i2);
                return;
            }
            buffer[pos++] = (byte)c2;
        }
        this.count = pos;
    }

    private void writeUTF2(String s2, int sLen, int offset) {
        int size = sLen;
        for (int i2 = offset; i2 < sLen; ++i2) {
            char c2 = s2.charAt(i2);
            if (c2 > '\u07ff') {
                size += 2;
                continue;
            }
            if (c2 != '\u0000' && c2 <= '\u007f') continue;
            ++size;
        }
        if (size > 65535) {
            throw new RuntimeException("encoded string too long: " + sLen + size + " bytes");
        }
        this.enlarge(size + 2);
        int pos = this.count;
        byte[] buffer = this.buf;
        buffer[pos] = (byte)(size >>> 8);
        buffer[pos + 1] = (byte)size;
        pos += 2 + offset;
        for (int j2 = offset; j2 < sLen; ++j2) {
            char c3 = s2.charAt(j2);
            if ('\u0001' <= c3 && c3 <= '\u007f') {
                buffer[pos++] = (byte)c3;
                continue;
            }
            if (c3 > '\u07ff') {
                buffer[pos] = (byte)(0xE0 | c3 >> 12 & 0xF);
                buffer[pos + 1] = (byte)(0x80 | c3 >> 6 & 0x3F);
                buffer[pos + 2] = (byte)(0x80 | c3 & 0x3F);
                pos += 3;
                continue;
            }
            buffer[pos] = (byte)(0xC0 | c3 >> 6 & 0x1F);
            buffer[pos + 1] = (byte)(0x80 | c3 & 0x3F);
            pos += 2;
        }
        this.count = pos;
    }

    public void write(int pos, int value) {
        this.buf[pos] = (byte)value;
    }

    public void writeShort(int pos, int value) {
        this.buf[pos] = (byte)(value >>> 8);
        this.buf[pos + 1] = (byte)value;
    }

    public void writeInt(int pos, int value) {
        this.buf[pos] = (byte)(value >>> 24);
        this.buf[pos + 1] = (byte)(value >>> 16);
        this.buf[pos + 2] = (byte)(value >>> 8);
        this.buf[pos + 3] = (byte)value;
    }

    public byte[] toByteArray() {
        byte[] buf2 = new byte[this.count];
        System.arraycopy(this.buf, 0, buf2, 0, this.count);
        return buf2;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    public void enlarge(int delta) {
        int newCount = this.count + delta;
        if (newCount > this.buf.length) {
            int newLen = this.buf.length << 1;
            byte[] newBuf = new byte[newLen > newCount ? newLen : newCount];
            System.arraycopy(this.buf, 0, newBuf, 0, this.count);
            this.buf = newBuf;
        }
    }
}

