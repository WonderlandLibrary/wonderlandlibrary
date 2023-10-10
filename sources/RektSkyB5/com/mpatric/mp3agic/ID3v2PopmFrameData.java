/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ID3v2PopmFrameData
extends AbstractID3v2FrameData {
    protected static final String WMP9_ADDRESS = "Windows Media Player 9 Series";
    protected String address = "";
    protected int rating = -1;
    private static final Map<Byte, Integer> byteToRating = new HashMap<Byte, Integer>(5);
    private static final byte[] wmp9encodedRatings = new byte[]{0, 1, 64, -128, -60, -1};

    public ID3v2PopmFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    public ID3v2PopmFrameData(boolean bl, int n2) {
        super(bl);
        this.address = WMP9_ADDRESS;
        this.rating = n2;
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        try {
            this.address = BufferTools.byteBufferToString(byArray, 0, byArray.length - 2);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            this.address = "";
        }
        byte by = byArray[byArray.length - 1];
        this.rating = byteToRating.containsKey(by) ? byteToRating.get(by) : -1;
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray = this.address.getBytes();
        byArray = Arrays.copyOf(byArray, this.address.length() + 2);
        byArray[byArray.length - 2] = 0;
        byArray[byArray.length - 1] = wmp9encodedRatings[this.rating];
        return byArray;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String string) {
        this.address = string;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int n2) {
        this.rating = n2;
    }

    @Override
    protected int getLength() {
        return this.address.length() + 2;
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + (this.address == null ? 0 : this.address.hashCode());
        n2 = 31 * n2 + this.rating;
        return n2;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!super.equals(object)) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        ID3v2PopmFrameData iD3v2PopmFrameData = (ID3v2PopmFrameData)object;
        if (this.address == null ? iD3v2PopmFrameData.address != null : !this.address.equals(iD3v2PopmFrameData.address)) {
            return false;
        }
        return this.rating == iD3v2PopmFrameData.rating;
    }

    static {
        for (int i2 = 0; i2 < 6; ++i2) {
            byteToRating.put(wmp9encodedRatings[i2], i2);
        }
    }
}

