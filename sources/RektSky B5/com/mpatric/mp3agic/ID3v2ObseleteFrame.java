/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;

public class ID3v2ObseleteFrame
extends ID3v2Frame {
    private static final int HEADER_LENGTH = 6;
    private static final int ID_OFFSET = 0;
    private static final int ID_LENGTH = 3;
    protected static final int DATA_LENGTH_OFFSET = 3;

    public ID3v2ObseleteFrame(byte[] byArray, int n2) throws InvalidDataException {
        super(byArray, n2);
    }

    public ID3v2ObseleteFrame(String string, byte[] byArray) {
        super(string, byArray);
    }

    @Override
    protected int unpackHeader(byte[] byArray, int n2) {
        this.id = BufferTools.byteBufferToStringIgnoringEncodingIssues(byArray, n2 + 0, 3);
        this.unpackDataLength(byArray, n2);
        return n2 + 6;
    }

    @Override
    protected void unpackDataLength(byte[] byArray, int n2) {
        this.dataLength = BufferTools.unpackInteger((byte)0, byArray[n2 + 3], byArray[n2 + 3 + 1], byArray[n2 + 3 + 2]);
    }

    @Override
    public void packFrame(byte[] byArray, int n2) throws NotSupportedException {
        throw new NotSupportedException("Packing Obselete frames is not supported");
    }

    @Override
    public int getLength() {
        return this.dataLength + 6;
    }
}

