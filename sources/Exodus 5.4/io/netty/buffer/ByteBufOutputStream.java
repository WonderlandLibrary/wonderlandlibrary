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
    private final DataOutputStream utf8out = new DataOutputStream(this);
    private final int startIndex;
    private final ByteBuf buffer;

    @Override
    public void writeChars(String string) throws IOException {
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            this.writeChar(string.charAt(i));
        }
    }

    public ByteBuf buffer() {
        return this.buffer;
    }

    @Override
    public void writeFloat(float f) throws IOException {
        this.writeInt(Float.floatToIntBits(f));
    }

    @Override
    public void write(int n) throws IOException {
        this.buffer.writeByte((byte)n);
    }

    @Override
    public void writeByte(int n) throws IOException {
        this.write(n);
    }

    @Override
    public void writeUTF(String string) throws IOException {
        this.utf8out.writeUTF(string);
    }

    @Override
    public void writeBoolean(boolean bl) throws IOException {
        this.write(bl ? 1 : 0);
    }

    @Override
    public void writeInt(int n) throws IOException {
        this.buffer.writeInt(n);
    }

    @Override
    public void writeChar(int n) throws IOException {
        this.writeShort((short)n);
    }

    @Override
    public void write(byte[] byArray) throws IOException {
        this.buffer.writeBytes(byArray);
    }

    @Override
    public void writeBytes(String string) throws IOException {
        int n = string.length();
        for (int i = 0; i < n; ++i) {
            this.write((byte)string.charAt(i));
        }
    }

    public ByteBufOutputStream(ByteBuf byteBuf) {
        if (byteBuf == null) {
            throw new NullPointerException("buffer");
        }
        this.buffer = byteBuf;
        this.startIndex = byteBuf.writerIndex();
    }

    public int writtenBytes() {
        return this.buffer.writerIndex() - this.startIndex;
    }

    @Override
    public void writeLong(long l) throws IOException {
        this.buffer.writeLong(l);
    }

    @Override
    public void writeShort(int n) throws IOException {
        this.buffer.writeShort((short)n);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        this.writeLong(Double.doubleToLongBits(d));
    }

    @Override
    public void write(byte[] byArray, int n, int n2) throws IOException {
        if (n2 == 0) {
            return;
        }
        this.buffer.writeBytes(byArray, n, n2);
    }
}

