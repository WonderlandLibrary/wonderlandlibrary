/*
 * Decompiled with CFR 0.152.
 */
package com.google.common.io;

import java.io.DataInput;

public interface ByteArrayDataInput
extends DataInput {
    @Override
    public void readFully(byte[] var1);

    @Override
    public void readFully(byte[] var1, int var2, int var3);

    @Override
    public int skipBytes(int var1);

    @Override
    public boolean readBoolean();

    @Override
    public byte readByte();

    @Override
    public int readUnsignedByte();

    @Override
    public short readShort();

    @Override
    public int readUnsignedShort();

    @Override
    public char readChar();

    @Override
    public int readInt();

    @Override
    public long readLong();

    @Override
    public float readFloat();

    @Override
    public double readDouble();

    @Override
    public String readLine();

    @Override
    public String readUTF();
}

