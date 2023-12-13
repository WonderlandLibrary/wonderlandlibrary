/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2Tag;
import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v24Frame;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.ID3v2FrameSet;
import com.mpatric.mp3agic.ID3v2TextFrameData;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NoSuchTagException;
import com.mpatric.mp3agic.UnsupportedTagException;

public class ID3v24Tag
extends AbstractID3v2Tag {
    public static final String VERSION = "4.0";
    public static final String ID_RECTIME = "TDRC";

    public ID3v24Tag() {
        this.version = VERSION;
    }

    public ID3v24Tag(byte[] byArray) throws NoSuchTagException, UnsupportedTagException, InvalidDataException {
        super(byArray);
    }

    @Override
    protected void unpackFlags(byte[] byArray) {
        this.unsynchronisation = BufferTools.checkBit(byArray[5], 7);
        this.extendedHeader = BufferTools.checkBit(byArray[5], 6);
        this.experimental = BufferTools.checkBit(byArray[5], 5);
        this.footer = BufferTools.checkBit(byArray[5], 4);
    }

    @Override
    protected void packFlags(byte[] byArray, int n2) {
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 7, this.unsynchronisation);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 6, this.extendedHeader);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 5, this.experimental);
        byArray[n2 + 5] = BufferTools.setBit(byArray[n2 + 5], 4, this.footer);
    }

    @Override
    protected boolean useFrameUnsynchronisation() {
        return this.unsynchronisation;
    }

    @Override
    protected ID3v2Frame createFrame(byte[] byArray, int n2) throws InvalidDataException {
        return new ID3v24Frame(byArray, n2);
    }

    @Override
    protected ID3v2Frame createFrame(String string, byte[] byArray) {
        return new ID3v24Frame(string, byArray);
    }

    @Override
    public void setGenreDescription(String string) {
        ID3v2TextFrameData iD3v2TextFrameData = new ID3v2TextFrameData(this.useFrameUnsynchronisation(), new EncodedText(string));
        ID3v2FrameSet iD3v2FrameSet = this.getFrameSets().get("TCON");
        if (iD3v2FrameSet == null) {
            iD3v2FrameSet = new ID3v2FrameSet("TCON");
            this.getFrameSets().put("TCON", iD3v2FrameSet);
        }
        iD3v2FrameSet.clear();
        iD3v2FrameSet.addFrame(this.createFrame("TCON", iD3v2TextFrameData.toBytes()));
    }

    public String getRecordingTime() {
        ID3v2TextFrameData iD3v2TextFrameData = this.extractTextFrameData(ID_RECTIME);
        if (iD3v2TextFrameData != null && iD3v2TextFrameData.getText() != null) {
            return iD3v2TextFrameData.getText().toString();
        }
        return null;
    }

    public void setRecordingTime(String string) {
        if (string != null && string.length() > 0) {
            this.invalidateDataLength();
            ID3v2TextFrameData iD3v2TextFrameData = new ID3v2TextFrameData(this.useFrameUnsynchronisation(), new EncodedText(string));
            this.addFrame(this.createFrame(ID_RECTIME, iD3v2TextFrameData.toBytes()), true);
        }
    }
}

