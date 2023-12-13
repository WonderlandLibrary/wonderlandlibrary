/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.InvalidDataException;

public class ID3v24Frame
extends ID3v2Frame {
    public ID3v24Frame(byte[] byArray, int n2) throws InvalidDataException {
        super(byArray, n2);
    }

    public ID3v24Frame(String string, byte[] byArray) {
        super(string, byArray);
    }

    @Override
    protected void unpackDataLength(byte[] byArray, int n2) {
        this.dataLength = BufferTools.unpackSynchsafeInteger(byArray[n2 + 4], byArray[n2 + 4 + 1], byArray[n2 + 4 + 2], byArray[n2 + 4 + 3]);
    }

    @Override
    protected byte[] packDataLength() {
        return BufferTools.packSynchsafeInteger(this.dataLength);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ID3v24Frame)) {
            return false;
        }
        return super.equals(object);
    }
}

