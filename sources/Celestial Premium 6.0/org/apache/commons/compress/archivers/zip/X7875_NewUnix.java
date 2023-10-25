/*
 * Decompiled with CFR 0.150.
 */
package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipException;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.ZipShort;
import org.apache.commons.compress.archivers.zip.ZipUtil;

public class X7875_NewUnix
implements ZipExtraField,
Cloneable,
Serializable {
    private static final ZipShort HEADER_ID = new ZipShort(30837);
    private static final BigInteger ONE_THOUSAND = BigInteger.valueOf(1000L);
    private static final long serialVersionUID = 1L;
    private int version = 1;
    private BigInteger uid;
    private BigInteger gid;

    public X7875_NewUnix() {
        this.reset();
    }

    public ZipShort getHeaderId() {
        return HEADER_ID;
    }

    public long getUID() {
        return ZipUtil.bigToLong(this.uid);
    }

    public long getGID() {
        return ZipUtil.bigToLong(this.gid);
    }

    public void setUID(long l) {
        this.uid = ZipUtil.longToBig(l);
    }

    public void setGID(long l) {
        this.gid = ZipUtil.longToBig(l);
    }

    public ZipShort getLocalFileDataLength() {
        int uidSize = X7875_NewUnix.trimLeadingZeroesForceMinLength(this.uid.toByteArray()).length;
        int gidSize = X7875_NewUnix.trimLeadingZeroesForceMinLength(this.gid.toByteArray()).length;
        return new ZipShort(3 + uidSize + gidSize);
    }

    public ZipShort getCentralDirectoryLength() {
        return this.getLocalFileDataLength();
    }

    public byte[] getLocalFileDataData() {
        byte[] uidBytes = this.uid.toByteArray();
        byte[] gidBytes = this.gid.toByteArray();
        uidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(uidBytes);
        gidBytes = X7875_NewUnix.trimLeadingZeroesForceMinLength(gidBytes);
        byte[] data = new byte[3 + uidBytes.length + gidBytes.length];
        ZipUtil.reverse(uidBytes);
        ZipUtil.reverse(gidBytes);
        int pos = 0;
        data[pos++] = ZipUtil.unsignedIntToSignedByte(this.version);
        data[pos++] = ZipUtil.unsignedIntToSignedByte(uidBytes.length);
        System.arraycopy(uidBytes, 0, data, pos, uidBytes.length);
        pos += uidBytes.length;
        data[pos++] = ZipUtil.unsignedIntToSignedByte(gidBytes.length);
        System.arraycopy(gidBytes, 0, data, pos, gidBytes.length);
        return data;
    }

    public byte[] getCentralDirectoryData() {
        return this.getLocalFileDataData();
    }

    public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
        this.reset();
        this.version = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        int uidSize = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        byte[] uidBytes = new byte[uidSize];
        System.arraycopy(data, offset, uidBytes, 0, uidSize);
        offset += uidSize;
        this.uid = new BigInteger(1, ZipUtil.reverse(uidBytes));
        int gidSize = ZipUtil.signedByteToUnsignedInt(data[offset++]);
        byte[] gidBytes = new byte[gidSize];
        System.arraycopy(data, offset, gidBytes, 0, gidSize);
        this.gid = new BigInteger(1, ZipUtil.reverse(gidBytes));
    }

    public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) throws ZipException {
        this.reset();
        this.parseFromLocalFileData(buffer, offset, length);
    }

    private void reset() {
        this.uid = ONE_THOUSAND;
        this.gid = ONE_THOUSAND;
    }

    public String toString() {
        return "0x7875 Zip Extra Field: UID=" + this.uid + " GID=" + this.gid;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public boolean equals(Object o) {
        if (o instanceof X7875_NewUnix) {
            X7875_NewUnix xf = (X7875_NewUnix)o;
            return this.version == xf.version && this.uid.equals(xf.uid) && this.gid.equals(xf.gid);
        }
        return false;
    }

    public int hashCode() {
        int hc = -1234567 * this.version;
        hc ^= Integer.rotateLeft(this.uid.hashCode(), 16);
        return hc ^= this.gid.hashCode();
    }

    static byte[] trimLeadingZeroesForceMinLength(byte[] array) {
        if (array == null) {
            return array;
        }
        int pos = 0;
        for (byte b : array) {
            if (b != 0) break;
            ++pos;
        }
        boolean MIN_LENGTH = true;
        byte[] trimmedArray = new byte[Math.max(1, array.length - pos)];
        int startPos = trimmedArray.length - (array.length - pos);
        System.arraycopy(array, pos, trimmedArray, startPos, trimmedArray.length - startPos);
        return trimmedArray;
    }
}

