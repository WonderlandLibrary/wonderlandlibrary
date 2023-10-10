/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;

public abstract class AbstractID3v2FrameData {
    boolean unsynchronisation;

    public AbstractID3v2FrameData(boolean bl) {
        this.unsynchronisation = bl;
    }

    protected final void synchroniseAndUnpackFrameData(byte[] byArray) throws InvalidDataException {
        if (this.unsynchronisation && BufferTools.sizeSynchronisationWouldSubtract(byArray) > 0) {
            byte[] byArray2 = BufferTools.synchroniseBuffer(byArray);
            this.unpackFrameData(byArray2);
        } else {
            this.unpackFrameData(byArray);
        }
    }

    protected byte[] packAndUnsynchroniseFrameData() {
        byte[] byArray = this.packFrameData();
        if (this.unsynchronisation && BufferTools.sizeUnsynchronisationWouldAdd(byArray) > 0) {
            return BufferTools.unsynchroniseBuffer(byArray);
        }
        return byArray;
    }

    protected byte[] toBytes() {
        return this.packAndUnsynchroniseFrameData();
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + (this.unsynchronisation ? 1231 : 1237);
        return n2;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        AbstractID3v2FrameData abstractID3v2FrameData = (AbstractID3v2FrameData)object;
        return this.unsynchronisation == abstractID3v2FrameData.unsynchronisation;
    }

    protected abstract void unpackFrameData(byte[] var1) throws InvalidDataException;

    protected abstract byte[] packFrameData();

    protected abstract int getLength();
}

