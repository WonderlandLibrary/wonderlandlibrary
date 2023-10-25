package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.zip.ZipException;

public class X7875_NewUnix implements ZipExtraField, Cloneable, Serializable {
  private static final ZipShort HEADER_ID = new ZipShort(30837);
  
  private static final BigInteger ONE_THOUSAND = BigInteger.valueOf(1000L);
  
  private static final long serialVersionUID = 1L;
  
  private int version = 1;
  
  private BigInteger uid;
  
  private BigInteger gid;
  
  public X7875_NewUnix() {
    reset();
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
    int uidSize = (trimLeadingZeroesForceMinLength(this.uid.toByteArray())).length;
    int gidSize = (trimLeadingZeroesForceMinLength(this.gid.toByteArray())).length;
    return new ZipShort(3 + uidSize + gidSize);
  }
  
  public ZipShort getCentralDirectoryLength() {
    return getLocalFileDataLength();
  }
  
  public byte[] getLocalFileDataData() {
    byte[] uidBytes = this.uid.toByteArray();
    byte[] gidBytes = this.gid.toByteArray();
    uidBytes = trimLeadingZeroesForceMinLength(uidBytes);
    gidBytes = trimLeadingZeroesForceMinLength(gidBytes);
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
    return getLocalFileDataData();
  }
  
  public void parseFromLocalFileData(byte[] data, int offset, int length) throws ZipException {
    reset();
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
    reset();
    parseFromLocalFileData(buffer, offset, length);
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
      return (this.version == xf.version && this.uid.equals(xf.uid) && this.gid.equals(xf.gid));
    } 
    return false;
  }
  
  public int hashCode() {
    int hc = -1234567 * this.version;
    hc ^= Integer.rotateLeft(this.uid.hashCode(), 16);
    hc ^= this.gid.hashCode();
    return hc;
  }
  
  static byte[] trimLeadingZeroesForceMinLength(byte[] array) {
    if (array == null)
      return array; 
    int pos = 0;
    byte[] arr$;
    int len$, i$;
    for (arr$ = array, len$ = arr$.length, i$ = 0; i$ < len$; ) {
      byte b = arr$[i$];
      if (b == 0) {
        pos++;
        i$++;
      } 
    } 
    int MIN_LENGTH = 1;
    byte[] trimmedArray = new byte[Math.max(1, array.length - pos)];
    int startPos = trimmedArray.length - array.length - pos;
    System.arraycopy(array, pos, trimmedArray, startPos, trimmedArray.length - startPos);
    return trimmedArray;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\X7875_NewUnix.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */