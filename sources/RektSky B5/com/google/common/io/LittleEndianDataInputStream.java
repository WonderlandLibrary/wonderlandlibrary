/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Beta
public final class LittleEndianDataInputStream
extends FilterInputStream
implements DataInput {
    public LittleEndianDataInputStream(InputStream in) {
        super(Preconditions.checkNotNull(in));
    }

    @Override
    public String readLine() {
        throw new UnsupportedOperationException("readLine is not supported");
    }

    @Override
    public void readFully(byte[] b2) throws IOException {
        ByteStreams.readFully(this, b2);
    }

    @Override
    public void readFully(byte[] b2, int off, int len) throws IOException {
        ByteStreams.readFully(this, b2, off, len);
    }

    @Override
    public int skipBytes(int n2) throws IOException {
        return (int)this.in.skip(n2);
    }

    @Override
    public int readUnsignedByte() throws IOException {
        int b1 = this.in.read();
        if (0 > b1) {
            throw new EOFException();
        }
        return b1;
    }

    @Override
    public int readUnsignedShort() throws IOException {
        byte b1 = this.readAndCheckByte();
        byte b2 = this.readAndCheckByte();
        return Ints.fromBytes((byte)0, (byte)0, b2, b1);
    }

    @Override
    public int readInt() throws IOException {
        byte b1 = this.readAndCheckByte();
        byte b2 = this.readAndCheckByte();
        byte b3 = this.readAndCheckByte();
        byte b4 = this.readAndCheckByte();
        return Ints.fromBytes(b4, b3, b2, b1);
    }

    @Override
    public long readLong() throws IOException {
        byte b1 = this.readAndCheckByte();
        byte b2 = this.readAndCheckByte();
        byte b3 = this.readAndCheckByte();
        byte b4 = this.readAndCheckByte();
        byte b5 = this.readAndCheckByte();
        byte b6 = this.readAndCheckByte();
        byte b7 = this.readAndCheckByte();
        byte b8 = this.readAndCheckByte();
        return Longs.fromBytes(b8, b7, b6, b5, b4, b3, b2, b1);
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(this.readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(this.readLong());
    }

    @Override
    public String readUTF() throws IOException {
        return new DataInputStream(this.in).readUTF();
    }

    @Override
    public short readShort() throws IOException {
        return (short)this.readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        return (char)this.readUnsignedShort();
    }

    @Override
    public byte readByte() throws IOException {
        return (byte)this.readUnsignedByte();
    }

    @Override
    public boolean readBoolean() throws IOException {
        return this.readUnsignedByte() != 0;
    }

    private byte readAndCheckByte() throws IOException, EOFException {
        int b1 = this.in.read();
        if (-1 == b1) {
            throw new EOFException();
        }
        return (byte)b1;
    }
}

