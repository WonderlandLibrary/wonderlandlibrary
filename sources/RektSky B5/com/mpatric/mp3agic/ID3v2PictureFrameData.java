/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ID3v2PictureFrameData
extends AbstractID3v2FrameData {
    protected String mimeType;
    protected byte pictureType;
    protected EncodedText description;
    protected byte[] imageData;

    public ID3v2PictureFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2PictureFrameData(boolean bl, String string, byte by, EncodedText encodedText, byte[] byArray) {
        super(bl);
        this.mimeType = string;
        this.pictureType = by;
        this.description = encodedText;
        this.imageData = byArray;
    }

    public ID3v2PictureFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        int n2 = BufferTools.indexOfTerminator(byArray, 1, 1);
        if (n2 >= 0) {
            try {
                this.mimeType = BufferTools.byteBufferToString(byArray, 1, n2 - 1);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                this.mimeType = "image/unknown";
            }
        } else {
            this.mimeType = "image/unknown";
        }
        this.pictureType = byArray[n2 + 1];
        int n3 = BufferTools.indexOfTerminatorForEncoding(byArray, n2 += 2, byArray[0]);
        if (n3 >= 0) {
            this.description = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, n2, n3 - n2));
            n3 += this.description.getTerminator().length;
        } else {
            this.description = new EncodedText(byArray[0], "");
            n3 = n2;
        }
        this.imageData = BufferTools.copyBuffer(byArray, n3, byArray.length - n3);
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray = new byte[this.getLength()];
        byArray[0] = this.description != null ? this.description.getTextEncoding() : (byte)0;
        int n2 = 0;
        if (this.mimeType != null && this.mimeType.length() > 0) {
            n2 = this.mimeType.length();
            try {
                BufferTools.stringIntoByteBuffer(this.mimeType, 0, n2, byArray, 1);
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                // empty catch block
            }
        }
        int n3 = n2 + 1;
        byArray[n3++] = 0;
        byArray[n3++] = this.pictureType;
        if (this.description != null && this.description.toBytes().length > 0) {
            byte[] byArray2 = this.description.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(byArray2, 0, byArray2.length, byArray, n3);
            n3 += byArray2.length;
        } else {
            byArray[n3++] = 0;
        }
        if (this.imageData != null && this.imageData.length > 0) {
            BufferTools.copyIntoByteBuffer(this.imageData, 0, this.imageData.length, byArray, n3);
        }
        return byArray;
    }

    @Override
    protected int getLength() {
        int n2 = 3;
        if (this.mimeType != null) {
            n2 += this.mimeType.length();
        }
        n2 = this.description != null ? (n2 += this.description.toBytes(true, true).length) : ++n2;
        if (this.imageData != null) {
            n2 += this.imageData.length;
        }
        return n2;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String string) {
        this.mimeType = string;
    }

    public byte getPictureType() {
        return this.pictureType;
    }

    public void setPictureType(byte by) {
        this.pictureType = by;
    }

    public EncodedText getDescription() {
        return this.description;
    }

    public void setDescription(EncodedText encodedText) {
        this.description = encodedText;
    }

    public byte[] getImageData() {
        return this.imageData;
    }

    public void setImageData(byte[] byArray) {
        this.imageData = byArray;
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + (this.description == null ? 0 : this.description.hashCode());
        n2 = 31 * n2 + Arrays.hashCode(this.imageData);
        n2 = 31 * n2 + (this.mimeType == null ? 0 : this.mimeType.hashCode());
        n2 = 31 * n2 + this.pictureType;
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
        ID3v2PictureFrameData iD3v2PictureFrameData = (ID3v2PictureFrameData)object;
        if (this.description == null ? iD3v2PictureFrameData.description != null : !this.description.equals(iD3v2PictureFrameData.description)) {
            return false;
        }
        if (!Arrays.equals(this.imageData, iD3v2PictureFrameData.imageData)) {
            return false;
        }
        if (this.mimeType == null ? iD3v2PictureFrameData.mimeType != null : !this.mimeType.equals(iD3v2PictureFrameData.mimeType)) {
            return false;
        }
        return this.pictureType == iD3v2PictureFrameData.pictureType;
    }
}

