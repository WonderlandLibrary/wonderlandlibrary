/*
 * Decompiled with CFR 0.152.
 */
package io.netty.buffer;

import io.netty.buffer.ByteBuf;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class ByteBufInputStream
extends InputStream
implements DataInput {
    private final int endIndex;
    private final StringBuilder lineBuf = new StringBuilder();
    private final ByteBuf buffer;
    private final int startIndex;

    @Override
    public int readUnsignedShort() throws IOException {
        return this.readShort() & 0xFFFF;
    }

    @Override
    public int skipBytes(int n) throws IOException {
        int n2 = Math.min(this.available(), n);
        this.buffer.skipBytes(n2);
        return n2;
    }

    @Override
    public void readFully(byte[] byArray) throws IOException {
        this.readFully(byArray, 0, byArray.length);
    }

    @Override
    public void reset() throws IOException {
        this.buffer.resetReaderIndex();
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public String readLine() throws IOException {
        this.lineBuf.setLength(0);
        block4: while (true) {
            if (!this.buffer.isReadable()) {
                return this.lineBuf.length() > 0 ? this.lineBuf.toString() : null;
            }
            short s = this.buffer.readUnsignedByte();
            switch (s) {
                case 10: {
                    break block4;
                }
                case 13: {
                    if (!this.buffer.isReadable() || (char)this.buffer.getUnsignedByte(this.buffer.readerIndex()) != '\n') break block4;
                    this.buffer.skipBytes(1);
                    break block4;
                }
                default: {
                    this.lineBuf.append((char)s);
                    continue block4;
                }
            }
            break;
        }
        return this.lineBuf.toString();
    }

    public int readBytes() {
        return this.buffer.readerIndex() - this.startIndex;
    }

    @Override
    public int available() throws IOException {
        return this.endIndex - this.buffer.readerIndex();
    }

    @Override
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }

    @Override
    public char readChar() throws IOException {
        return (char)this.readShort();
    }

    @Override
    public int readInt() throws IOException {
        this.checkAvailable(4);
        return this.buffer.readInt();
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        int n3 = this.available();
        if (n3 == 0) {
            return -1;
        }
        n2 = Math.min(n3, n2);
        this.buffer.readBytes(byArray, n, n2);
        return n2;
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return this.readByte() & 0xFF;
    }

    public ByteBufInputStream(ByteBuf byteBuf, int n) {
        if (byteBuf == null) {
            throw new NullPointerException("buffer");
        }
        if (n < 0) {
            throw new IllegalArgumentException("length: " + n);
        }
        if (n > byteBuf.readableBytes()) {
            throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + n + ", maximum is " + byteBuf.readableBytes());
        }
        this.buffer = byteBuf;
        this.startIndex = byteBuf.readerIndex();
        this.endIndex = this.startIndex + n;
        byteBuf.markReaderIndex();
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public short readShort() throws IOException {
        this.checkAvailable(2);
        return this.buffer.readShort();
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public boolean readBoolean() throws IOException {
        this.checkAvailable(1);
        return this.read() != 0;
    }

    @Override
    public long readLong() throws IOException {
        this.checkAvailable(8);
        return this.buffer.readLong();
    }

    @Override
    public long skip(long l) throws IOException {
        if (l > Integer.MAX_VALUE) {
            return this.skipBytes(Integer.MAX_VALUE);
        }
        return this.skipBytes((int)l);
    }

    @Override
    public void mark(int n) {
        this.buffer.markReaderIndex();
    }

    private void checkAvailable(int n) throws IOException {
        if (n < 0) {
            throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
        }
        if (n > this.available()) {
            throw new EOFException("fieldSize is too long! Length is " + n + ", but maximum is " + this.available());
        }
    }

    @Override
    public int read() throws IOException {
        if (!this.buffer.isReadable()) {
            return -1;
        }
        return this.buffer.readByte() & 0xFF;
    }

    @Override
    public byte readByte() throws IOException {
        if (!this.buffer.isReadable()) {
            throw new EOFException();
        }
        return this.buffer.readByte();
    }

    public ByteBufInputStream(ByteBuf byteBuf) {
        this(byteBuf, byteBuf.readableBytes());
    }

    @Override
    public void readFully(byte[] byArray, int n, int n2) throws IOException {
        this.checkAvailable(n2);
        this.buffer.readBytes(byArray, n, n2);
    }
}

