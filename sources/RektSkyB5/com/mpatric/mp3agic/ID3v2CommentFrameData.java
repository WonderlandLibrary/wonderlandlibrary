/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.InvalidDataException;
import java.io.UnsupportedEncodingException;

public class ID3v2CommentFrameData
extends AbstractID3v2FrameData {
    private static final String DEFAULT_LANGUAGE = "eng";
    private String language;
    private EncodedText description;
    private EncodedText comment;

    public ID3v2CommentFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2CommentFrameData(boolean bl, String string, EncodedText encodedText, EncodedText encodedText2) {
        super(bl);
        if (encodedText != null && encodedText2 != null && encodedText.getTextEncoding() != encodedText2.getTextEncoding()) {
            throw new IllegalArgumentException("description and comment must have same text encoding");
        }
        this.language = string;
        this.description = encodedText;
        this.comment = encodedText2;
    }

    public ID3v2CommentFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        try {
            this.language = BufferTools.byteBufferToString(byArray, 1, 3);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            this.language = "";
        }
        int n2 = BufferTools.indexOfTerminatorForEncoding(byArray, 4, byArray[0]);
        if (n2 >= 4) {
            this.description = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, 4, n2 - 4));
            n2 += this.description.getTerminator().length;
        } else {
            this.description = new EncodedText(byArray[0], "");
            n2 = 4;
        }
        this.comment = new EncodedText(byArray[0], BufferTools.copyBuffer(byArray, n2, byArray.length - n2));
    }

    @Override
    protected byte[] packFrameData() {
        byte[] byArray;
        byte[] byArray2 = new byte[this.getLength()];
        byArray2[0] = this.comment != null ? this.comment.getTextEncoding() : (byte)0;
        String string = this.language == null ? DEFAULT_LANGUAGE : (this.language.length() > 3 ? this.language.substring(0, 3) : BufferTools.padStringRight(this.language, 3, '\u0000'));
        try {
            BufferTools.stringIntoByteBuffer(string, 0, 3, byArray2, 1);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
        int n2 = 4;
        if (this.description != null) {
            byArray = this.description.toBytes(true, true);
            BufferTools.copyIntoByteBuffer(byArray, 0, byArray.length, byArray2, n2);
            n2 += byArray.length;
        } else {
            byte[] byArray3;
            if (this.comment != null) {
                byArray3 = this.comment.getTerminator();
            } else {
                byte[] byArray4 = new byte[1];
                byArray3 = byArray4;
                byArray4[0] = 0;
            }
            byArray = byArray3;
            BufferTools.copyIntoByteBuffer(byArray, 0, byArray.length, byArray2, n2);
            n2 += byArray.length;
        }
        if (this.comment != null) {
            byArray = this.comment.toBytes(true, false);
            BufferTools.copyIntoByteBuffer(byArray, 0, byArray.length, byArray2, n2);
        }
        return byArray2;
    }

    @Override
    protected int getLength() {
        int n2 = 4;
        n2 = this.description != null ? (n2 += this.description.toBytes(true, true).length) : (n2 += this.comment != null ? this.comment.getTerminator().length : 1);
        if (this.comment != null) {
            n2 += this.comment.toBytes(true, false).length;
        }
        return n2;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String string) {
        this.language = string;
    }

    public EncodedText getComment() {
        return this.comment;
    }

    public void setComment(EncodedText encodedText) {
        this.comment = encodedText;
    }

    public EncodedText getDescription() {
        return this.description;
    }

    public void setDescription(EncodedText encodedText) {
        this.description = encodedText;
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + (this.comment == null ? 0 : this.comment.hashCode());
        n2 = 31 * n2 + (this.description == null ? 0 : this.description.hashCode());
        n2 = 31 * n2 + (this.language == null ? 0 : this.language.hashCode());
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
        ID3v2CommentFrameData iD3v2CommentFrameData = (ID3v2CommentFrameData)object;
        if (this.comment == null ? iD3v2CommentFrameData.comment != null : !this.comment.equals(iD3v2CommentFrameData.comment)) {
            return false;
        }
        if (this.description == null ? iD3v2CommentFrameData.description != null : !this.description.equals(iD3v2CommentFrameData.description)) {
            return false;
        }
        return !(this.language == null ? iD3v2CommentFrameData.language != null : !this.language.equals(iD3v2CommentFrameData.language));
    }
}

