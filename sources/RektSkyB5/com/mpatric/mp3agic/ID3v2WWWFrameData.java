/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;

public class ID3v2WWWFrameData
extends AbstractID3v2FrameData {
    protected String url;

    public ID3v2WWWFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2WWWFrameData(boolean bl, String string) {
        super(bl);
        this.url = string;
    }

    public ID3v2WWWFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        try {
            this.url = BufferTools.byteBufferToString(byArray, 0, byArray.length);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            this.url = "";
        }
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray = new byte[this.getLength()];
        if (this.url != null && this.url.length() > 0) {
            try {
                BufferTools.stringIntoByteBuffer(this.url, 0, this.url.length(), byArray, 0);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        return byArray;
    }

    @Override
    protected int getLength() {
        int n2 = 0;
        if (this.url != null) {
            n2 = this.url.length();
        }
        return n2;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String string) {
        this.url = string;
    }
}

