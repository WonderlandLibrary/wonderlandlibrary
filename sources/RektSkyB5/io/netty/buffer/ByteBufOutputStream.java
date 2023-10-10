/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ByteBufOutputStream
extends OutputStream
implements DataOutput {
    private final ByteBuf buffer;
    private final int startIndex;
    private final DataOutputStream utf8out = new DataOutputStream(this);

    public ByteBufOutputStream(ByteBuf buffer) {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = buffer;
        this.startIndex = buffer.writerIndex();
    }

    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }

    @Override
    public void write(byte[] b2, int off, int len) throws IOException {
        if (len == 0) {
            return;
        }
        this.buffer.writeBytes(b2, off, len);
    }

    @Override
    public void write(byte[] b2) throws IOException {
        this.buffer.writeBytes(b2);
    }

    @Override
    public void write(int b2) throws IOException {
        this.buffer.writeByte((byte)b2);
    }

    @Override
    public void writeBoolean(boolean v2) throws IOException {
        this.write(v2 ? 1 : 0);
    }

    @Override
    public void writeByte(int v2) throws IOException {
        this.write(v2);
    }

    @Override
    public void writeBytes(String s2) throws IOException {
        int len = s2.length();
        for (int i2 = 0; i2 < len; ++i2) {
            this.write((byte)s2.charAt(i2));
        }
    }

    @Override
    public void writeChar(int v2) throws IOException {
        this.writeShort((short)v2);
    }

    @Override
    public void writeChars(String s2) throws IOException {
        int len = s2.length();
        for (int i2 = 0; i2 < len; ++i2) {
            this.writeChar(s2.charAt(i2));
        }
    }

    @Override
    public void writeDouble(double v2) throws IOException {
        this.writeLong(Double.doubleToLongBits(v2));
    }

    @Override
    public void writeFloat(float v2) throws IOException {
        this.writeInt(Float.floatToIntBits(v2));
    }

    @Override
    public void writeInt(int v2) throws IOException {
        this.buffer.writeInt(v2);
    }

    @Override
    public void writeLong(long v2) throws IOException {
        this.buffer.writeLong(v2);
    }

    @Override
    public void writeShort(int v2) throws IOException {
        this.buffer.writeShort((short)v2);
    }

    @Override
    public void writeUTF(String s2) throws IOException {
        this.utf8out.writeUTF(s2);
    }

    public ByteBuf buffer() {
        return this.buffer;
    }
}

