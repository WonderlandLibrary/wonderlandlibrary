/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.AbstractID3v2FrameData;
import com.mpatric.mp3agic.ByteBufferUtils;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ID3v2ChapterTOCFrameData
extends AbstractID3v2FrameData {
    protected boolean isRoot;
    protected boolean isOrdered;
    protected String id;
    protected String[] children;
    protected ArrayList<ID3v2Frame> subframes = new ArrayList();

    public ID3v2ChapterTOCFrameData(boolean bl) {
        super(bl);
    }

    public ID3v2ChapterTOCFrameData(boolean bl, boolean bl2, boolean bl3, String string, String[] stringArray) {
        super(bl);
        this.isRoot = bl2;
        this.isOrdered = bl3;
        this.id = string;
        this.children = stringArray;
    }

    public ID3v2ChapterTOCFrameData(boolean bl, byte[] byArray) throws InvalidDataException {
        super(bl);
        this.synchroniseAndUnpackFrameData(byArray);
    }

    @Override
    protected void unpackFrameData(byte[] byArray) throws InvalidDataException {
        ID3v2Frame iD3v2Frame;
        int n2;
        ByteBuffer byteBuffer = ByteBuffer.wrap(byArray);
        this.id = ByteBufferUtils.extractNullTerminatedString(byteBuffer);
        byte by = byteBuffer.get();
        if ((by & 1) == 1) {
            this.isRoot = true;
        }
        if ((by & 2) == 2) {
            this.isOrdered = true;
        }
        int n3 = byteBuffer.get();
        this.children = new String[n3];
        for (n2 = 0; n2 < n3; ++n2) {
            this.children[n2] = ByteBufferUtils.extractNullTerminatedString(byteBuffer);
        }
        for (n2 = byteBuffer.position(); n2 < byArray.length; n2 += iD3v2Frame.getLength()) {
            iD3v2Frame = new ID3v2Frame(byArray, n2);
            this.subframes.add(iD3v2Frame);
        }
    }

    public void addSubframe(String string, AbstractID3v2FrameData abstractID3v2FrameData) {
        this.subframes.add(new ID3v2Frame(string, abstractID3v2FrameData.toBytes()));
    }

    @Override
    protected byte[] packFrameData() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.getLength());
        byteBuffer.put(this.id.getBytes());
        byteBuffer.put((byte)0);
        byteBuffer.put(this.getFlags());
        byteBuffer.put((byte)this.children.length);
        for (String string : this.children) {
            byteBuffer.put(string.getBytes());
            byteBuffer.put((byte)0);
        }
        for (ID3v2Frame iD3v2Frame : this.subframes) {
            try {
                byteBuffer.put(iD3v2Frame.toBytes());
            }
            catch (NotSupportedException notSupportedException) {
                notSupportedException.printStackTrace();
            }
        }
        return byteBuffer.array();
    }

    private byte getFlags() {
        byte by = 0;
        if (this.isRoot) {
            by = (byte)(by | 1);
        }
        if (this.isOrdered) {
            by = (byte)(by | 2);
        }
        return by;
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public void setRoot(boolean bl) {
        this.isRoot = bl;
    }

    public boolean isOrdered() {
        return this.isOrdered;
    }

    public void setOrdered(boolean bl) {
        this.isOrdered = bl;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String string) {
        this.id = string;
    }

    public String[] getChildren() {
        return this.children;
    }

    public void setChildren(String[] stringArray) {
        this.children = stringArray;
    }

    @Deprecated
    public String[] getChilds() {
        return this.children;
    }

    @Deprecated
    public void setChilds(String[] stringArray) {
        this.children = stringArray;
    }

    public ArrayList<ID3v2Frame> getSubframes() {
        return this.subframes;
    }

    public void setSubframes(ArrayList<ID3v2Frame> arrayList) {
        this.subframes = arrayList;
    }

    @Override
    protected int getLength() {
        int n2 = 3;
        if (this.id != null) {
            n2 += this.id.length();
        }
        if (this.children != null) {
            n2 += this.children.length;
            for (String string : this.children) {
                n2 += string.length();
            }
        }
        if (this.subframes != null) {
            for (ID3v2Frame iD3v2Frame : this.subframes) {
                n2 += iD3v2Frame.getLength();
            }
        }
        return n2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ID3v2ChapterTOCFrameData [isRoot=");
        stringBuilder.append(this.isRoot);
        stringBuilder.append(", isOrdered=");
        stringBuilder.append(this.isOrdered);
        stringBuilder.append(", id=");
        stringBuilder.append(this.id);
        stringBuilder.append(", children=");
        stringBuilder.append(Arrays.toString(this.children));
        stringBuilder.append(", subframes=");
        stringBuilder.append(this.subframes);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        int n2 = super.hashCode();
        n2 = 31 * n2 + Arrays.hashCode(this.children);
        n2 = 31 * n2 + (this.id == null ? 0 : this.id.hashCode());
        n2 = 31 * n2 + (this.isOrdered ? 1231 : 1237);
        n2 = 31 * n2 + (this.isRoot ? 1231 : 1237);
        n2 = 31 * n2 + (this.subframes == null ? 0 : this.subframes.hashCode());
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
        ID3v2ChapterTOCFrameData iD3v2ChapterTOCFrameData = (ID3v2ChapterTOCFrameData)object;
        if (!Arrays.equals(this.children, iD3v2ChapterTOCFrameData.children)) {
            return false;
        }
        if (this.id == null ? iD3v2ChapterTOCFrameData.id != null : !this.id.equals(iD3v2ChapterTOCFrameData.id)) {
            return false;
        }
        if (this.isOrdered != iD3v2ChapterTOCFrameData.isOrdered) {
            return false;
        }
        if (this.isRoot != iD3v2ChapterTOCFrameData.isRoot) {
            return false;
        }
        return !(this.subframes == null ? iD3v2ChapterTOCFrameData.subframes != null : !this.subframes.equals(iD3v2ChapterTOCFrameData.subframes));
    }
}

