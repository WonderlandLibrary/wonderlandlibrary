/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.BufferTools;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class ID3v2Frame {
    private static final int HEADER_LENGTH = 10;
    private static final int ID_OFFSET = 0;
    private static final int ID_LENGTH = 4;
    protected static final int DATA_LENGTH_OFFSET = 4;
    private static final int FLAGS1_OFFSET = 8;
    private static final int FLAGS2_OFFSET = 9;
    private static final int PRESERVE_TAG_BIT = 6;
    private static final int PRESERVE_FILE_BIT = 5;
    private static final int READ_ONLY_BIT = 4;
    private static final int GROUP_BIT = 6;
    private static final int COMPRESSION_BIT = 3;
    private static final int ENCRYPTION_BIT = 2;
    private static final int UNSYNCHRONISATION_BIT = 1;
    private static final int DATA_LENGTH_INDICATOR_BIT = 0;
    protected String id;
    protected int dataLength = 0;
    protected byte[] data = null;
    private boolean preserveTag = false;
    private boolean preserveFile = false;
    private boolean readOnly = false;
    private boolean group = false;
    private boolean compression = false;
    private boolean encryption = false;
    private boolean unsynchronisation = false;
    private boolean dataLengthIndicator = false;

    public ID3v2Frame(byte[] byArray, int n2) throws InvalidDataException {
        this.unpackFrame(byArray, n2);
    }

    public ID3v2Frame(String string, byte[] byArray) {
        this.id = string;
        this.data = byArray;
        this.dataLength = byArray.length;
    }

    protected final void unpackFrame(byte[] byArray, int n2) throws InvalidDataException {
        int n3 = this.unpackHeader(byArray, n2);
        this.sanityCheckUnpackedHeader();
        this.data = BufferTools.copyBuffer(byArray, n3, this.dataLength);
    }

    protected int unpackHeader(byte[] byArray, int n2) {
        this.id = BufferTools.byteBufferToStringIgnoringEncodingIssues(byArray, n2 + 0, 4);
        this.unpackDataLength(byArray, n2);
        this.unpackFlags(byArray, n2);
        return n2 + 10;
    }

    protected void unpackDataLength(byte[] byArray, int n2) {
        this.dataLength = BufferTools.unpackInteger(byArray[n2 + 4], byArray[n2 + 4 + 1], byArray[n2 + 4 + 2], byArray[n2 + 4 + 3]);
    }

    private void unpackFlags(byte[] byArray, int n2) {
        this.preserveTag = BufferTools.checkBit(byArray[n2 + 8], 6);
        this.preserveFile = BufferTools.checkBit(byArray[n2 + 8], 5);
        this.readOnly = BufferTools.checkBit(byArray[n2 + 8], 4);
        this.group = BufferTools.checkBit(byArray[n2 + 9], 6);
        this.compression = BufferTools.checkBit(byArray[n2 + 9], 3);
        this.encryption = BufferTools.checkBit(byArray[n2 + 9], 2);
        this.unsynchronisation = BufferTools.checkBit(byArray[n2 + 9], 1);
        this.dataLengthIndicator = BufferTools.checkBit(byArray[n2 + 9], 0);
    }

    protected void sanityCheckUnpackedHeader() throws InvalidDataException {
        for (int i2 = 0; i2 < this.id.length(); ++i2) {
            if (this.id.charAt(i2) >= 'A' && this.id.charAt(i2) <= 'Z' || this.id.charAt(i2) >= '0' && this.id.charAt(i2) <= '9') continue;
            throw new InvalidDataException("Not a valid frame - invalid tag " + this.id);
        }
    }

    public byte[] toBytes() throws NotSupportedException {
        byte[] byArray = new byte[this.getLength()];
        this.packFrame(byArray, 0);
        return byArray;
    }

    public void toBytes(byte[] byArray, int n2) throws NotSupportedException {
        this.packFrame(byArray, n2);
    }

    public void packFrame(byte[] byArray, int n2) throws NotSupportedException {
        this.packHeader(byArray, n2);
        BufferTools.copyIntoByteBuffer(this.data, 0, this.data.length, byArray, n2 + 10);
    }

    private void packHeader(byte[] byArray, int n2) {
        try {
            BufferTools.stringIntoByteBuffer(this.id, 0, this.id.length(), byArray, 0);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            // empty catch block
        }
        BufferTools.copyIntoByteBuffer(this.packDataLength(), 0, 4, byArray, 4);
        BufferTools.copyIntoByteBuffer(this.packFlags(), 0, 2, byArray, 8);
    }

    protected byte[] packDataLength() {
        return BufferTools.packInteger(this.dataLength);
    }

    private byte[] packFlags() {
        byte[] byArray = new byte[2];
        byArray[0] = BufferTools.setBit(byArray[0], 6, this.preserveTag);
        byArray[0] = BufferTools.setBit(byArray[0], 5, this.preserveFile);
        byArray[0] = BufferTools.setBit(byArray[0], 4, this.readOnly);
        byArray[1] = BufferTools.setBit(byArray[1], 6, this.group);
        byArray[1] = BufferTools.setBit(byArray[1], 3, this.compression);
        byArray[1] = BufferTools.setBit(byArray[1], 2, this.encryption);
        byArray[1] = BufferTools.setBit(byArray[1], 1, this.unsynchronisation);
        byArray[1] = BufferTools.setBit(byArray[1], 0, this.dataLengthIndicator);
        return byArray;
    }

    public String getId() {
        return this.id;
    }

    public int getDataLength() {
        return this.dataLength;
    }

    public int getLength() {
        return this.dataLength + 10;
    }

    public byte[] getData() {
        return this.data;
    }

    public void setData(byte[] byArray) {
        this.data = byArray;
        this.dataLength = byArray == null ? 0 : byArray.length;
    }

    public boolean hasDataLengthIndicator() {
        return this.dataLengthIndicator;
    }

    public boolean hasCompression() {
        return this.compression;
    }

    public boolean hasEncryption() {
        return this.encryption;
    }

    public boolean hasGroup() {
        return this.group;
    }

    public boolean hasPreserveFile() {
        return this.preserveFile;
    }

    public boolean hasPreserveTag() {
        return this.preserveTag;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public boolean hasUnsynchronisation() {
        return this.unsynchronisation;
    }

    public int hashCode() {
        int n2 = 1;
        n2 = 31 * n2 + (this.compression ? 1231 : 1237);
        n2 = 31 * n2 + Arrays.hashCode(this.data);
        n2 = 31 * n2 + this.dataLength;
        n2 = 31 * n2 + (this.dataLengthIndicator ? 1231 : 1237);
        n2 = 31 * n2 + (this.encryption ? 1231 : 1237);
        n2 = 31 * n2 + (this.group ? 1231 : 1237);
        n2 = 31 * n2 + (this.id == null ? 0 : this.id.hashCode());
        n2 = 31 * n2 + (this.preserveFile ? 1231 : 1237);
        n2 = 31 * n2 + (this.preserveTag ? 1231 : 1237);
        n2 = 31 * n2 + (this.readOnly ? 1231 : 1237);
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
        ID3v2Frame iD3v2Frame = (ID3v2Frame)object;
        if (this.compression != iD3v2Frame.compression) {
            return false;
        }
        if (!Arrays.equals(this.data, iD3v2Frame.data)) {
            return false;
        }
        if (this.dataLength != iD3v2Frame.dataLength) {
            return false;
        }
        if (this.dataLengthIndicator != iD3v2Frame.dataLengthIndicator) {
            return false;
        }
        if (this.encryption != iD3v2Frame.encryption) {
            return false;
        }
        if (this.group != iD3v2Frame.group) {
            return false;
        }
        if (this.id == null ? iD3v2Frame.id != null : !this.id.equals(iD3v2Frame.id)) {
            return false;
        }
        if (this.preserveFile != iD3v2Frame.preserveFile) {
            return false;
        }
        if (this.preserveTag != iD3v2Frame.preserveTag) {
            return false;
        }
        if (this.readOnly != iD3v2Frame.readOnly) {
            return false;
        }
        return this.unsynchronisation == iD3v2Frame.unsynchronisation;
    }
}

