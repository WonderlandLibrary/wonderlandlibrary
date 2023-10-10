/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.InvalidDataException;

public class ID3v2TextFrameData
extends AbstractID3v2FrameData {
    protected EncodedText text;

    public ID3v2TextFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2TextFrameData(boolean bl, EncodedText encodedText) {
        super(bl);
        this.text = encodedText;
    }

    public ID3v2TextFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        this.text = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, 1, byArray.length - 1));
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray = new byte[this.getLength()];
        if (this.text != null) {
            byArray[0] = this.text.getTextEncoding();
            byte[] byArray2 = this.text.toBytes(true, false);
            if (byArray2.length > 0) {
                BufferTools.copyIntoByteBuffer(byArray2, 0, byArray2.length, byArray, 1);
            }
        }
        return byArray;
    }

    @Override
    protected int getLength() {
        int n2 = 1;
        if (this.text != null) {
            n2 += this.text.toBytes(true, false).length;
        }
        return n2;
    }

    public EncodedText getText() {
        return this.text;
    }

    public void setText(EncodedText encodedText) {
        this.text = encodedText;
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + (this.text == null ? 0 : this.text.hashCode());
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
        ID3v2TextFrameData iD3v2TextFrameData = (ID3v2TextFrameData)object;
        return !(this.text == null ? iD3v2TextFrameData.text != null : !this.text.equals(iD3v2TextFrameData.text));
    }
}

