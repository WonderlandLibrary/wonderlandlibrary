/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v2PictureFrameData;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;

public class ID3v2ObseletePictureFrameData
extends ID3v2PictureFrameData {
    public ID3v2ObseletePictureFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2ObseletePictureFrameData(boolean bl, String string, byte by, EncodedText encodedText, byte[] byArray) {
        super(bl, string, by, encodedText, byArray);
    }

    public ID3v2ObseletePictureFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl, byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        String string;
        try {
            string = BufferTools.byteBufferToString(byArray, 1, 3);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            string = "unknown";
        }
        this.mimeType = "image/" + string.toLowerCase();
        this.pictureType = byArray[4];
        int n2 = BufferTools.indexOfTerminatorForEncoding(byArray, 5, byArray[0]);
        if (n2 >= 0) {
            this.description = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, 5, n2 - 5));
            n2 += this.description.getTerminator().length;
        } else {
            this.description = new EncodedText(byArray[0], "");
            n2 = 1;
        }
        this.imageData = BufferTools.copyBuffer(byArray, n2, byArray.length - n2);
    }
}

