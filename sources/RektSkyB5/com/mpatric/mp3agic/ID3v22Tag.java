/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NoSuchTagException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ID3v22Tag
extends AbstractID3v2Tag {
    public static final String VERSION = "2.0";

    public ID3v22Tag() {
        this.version = VERSION;
    }

    public ID3v22Tag(byte[] byArray) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
        super(byArray);
    }

    public ID3v22Tag(byte[] byArray, boolean bl) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
        super(byArray, bl);
    }

    @Override
    protected void unpackFlags(byte[] byArray) {
        this.unsynchronisation = BufferTools.checkBit(byArray[5], 7);
        this.compression = BufferTools.checkBit(byArray[5], 6);
    }

    @Override
    protected void packFlags(byte[] byArray, int n2) {
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 7, this.unsynchronisation);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 6, this.compression);
    }
}

