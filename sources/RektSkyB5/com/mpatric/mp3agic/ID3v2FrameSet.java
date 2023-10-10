/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.ID3v2Frame;
import java.util.ArrayList;
import java.util.List;

public class ID3v2FrameSet {
    private String id;
    private ArrayList<ID3v2Frame> frames;

    public ID3v2FrameSet(String string) {
        this.id = string;
        this.frames = new ArrayList();
    }

    public String getId() {
        return this.id;
    }

    public void clear() {
        this.frames.clear();
    }

    public void addFrame(ID3v2Frame iD3v2Frame) {
        this.frames.add(iD3v2Frame);
    }

    public List<ID3v2Frame> getFrames() {
        return this.frames;
    }

    public String toString() {
        return this.id + ": " + this.frames.size();
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + (this.frames == null ? 0 : this.frames.hashCode());
        n2 = 31 * n2 + (this.id == null ? 0 : this.id.hashCode());
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
        ID3v2FrameSet iD3v2FrameSet = (ID3v2FrameSet)object;
        if (this.frames == null ? iD3v2FrameSet.frames != null : !this.frames.equals(iD3v2FrameSet.frames)) {
            return false;
        }
        return !(this.id == null ? iD3v2FrameSet.id != null : !this.id.equals(iD3v2FrameSet.id));
    }
}

