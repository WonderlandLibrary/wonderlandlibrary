/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NoSuchTagException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ID3v23Tag
extends AbstractID3v2Tag {
    public static final String VERSION = "3.0";

    public ID3v23Tag() {
        this.version = VERSION;
    }

    public ID3v23Tag(byte[] byArray) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
        super(byArray);
    }

    @Override
    protected void unpackFlags(byte[] byArray) {
        this.unsynchronisation = BufferTools.checkBit(byArray[5], 7);
        this.extendedHeader = BufferTools.checkBit(byArray[5], 6);
        this.experimental = BufferTools.checkBit(byArray[5], 5);
    }

    @Override
    protected void packFlags(byte[] byArray, int n2) {
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 7, this.unsynchronisation);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 6, this.extendedHeader);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 5, this.experimental);
    }
}

