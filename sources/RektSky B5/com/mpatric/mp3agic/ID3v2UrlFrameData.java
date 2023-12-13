/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;

public class ID3v2UrlFrameData
extends AbstractID3v2FrameData {
    protected String url;
    protected EncodedText description;

    public ID3v2UrlFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2UrlFrameData(boolean bl, EncodedText encodedText, String string) {
        super(bl);
        this.description = encodedText;
        this.url = string;
    }

    public ID3v2UrlFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        int n2 = BufferTools.indexOfTerminatorForEncoding(byArray, 1, byArray[0]);
        if (n2 >= 0) {
            this.description = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, 1, n2 - 1));
            n2 += this.description.getTerminator().length;
        } else {
            this.description = new EncodedText(byArray[0], "");
            n2 = 1;
        }
        try {
            this.url = BufferTools.byteBufferToString(byArray, n2, byArray.length - n2);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            this.url = "";
        }
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray = new byte[this.getLength()];
        byArray[0] = this.description != null ? this.description.getTextEncoding() : (byte)0;
        int n2 = 1;
        if (this.description != null) {
            byte[] byArray2 = this.description.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(byArray2, 0, byArray2.length, byArray, n2);
            n2 += byArray2.length;
        } else {
            byArray[n2++] = 0;
        }
        if (this.url != null && this.url.length() > 0) {
            try {
                BufferTools.stringIntoByteBuffer(this.url, 0, this.url.length(), byArray, n2);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        return byArray;
    }

    @Override
    protected int getLength() {
        int n2 = 1;
        n2 = this.description != null ? (n2 += this.description.toBytes(true, true).length) : ++n2;
        if (this.url != null) {
            n2 += this.url.length();
        }
        return n2;
    }

    public EncodedText getDescription() {
        return this.description;
    }

    public void setDescription(EncodedText encodedText) {
        this.description = encodedText;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String string) {
        this.url = string;
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + (this.description == null ? 0 : this.description.hashCode());
        n2 = 31 * n2 + (this.url == null ? 0 : this.url.hashCode());
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
        ID3v2UrlFrameData iD3v2UrlFrameData = (ID3v2UrlFrameData)object;
        if (this.description == null ? iD3v2UrlFrameData.description != null : !this.description.equals(iD3v2UrlFrameData.description)) {
            return false;
        }
        return !(this.url == null ? iD3v2UrlFrameData.url != null : !this.url.equals(iD3v2UrlFrameData.url));
    }
}

