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

   public void setUID(long var1) {
      this.uid = ZipUtil.longToBig(var1);
   }

   public void setGID(long var1) {
      this.gid = ZipUtil.longToBig(var1);
   }

   public ZipShort getLocalFileDataLength() {
      int var1 = trimLeadingZeroesForceMinLength(this.uid.toByteArray()).length;
      int var2 = trimLeadingZeroesForceMinLength(this.gid.toByteArray()).length;
      return new ZipShort(3 + var1 + var2);
   }

   public ZipShort getCentralDirectoryLength() {
      return this.getLocalFileDataLength();
   }

   public byte[] getLocalFileDataData() {
      byte[] var1 = this.uid.toByteArray();
      byte[] var2 = this.gid.toByteArray();
      var1 = trimLeadingZeroesForceMinLength(var1);
      var2 = trimLeadingZeroesForceMinLength(var2);
      byte[] var3 = new byte[3 + var1.length + var2.length];
      ZipUtil.reverse(var1);
      ZipUtil.reverse(var2);
      byte var4 = 0;
      int var5 = var4 + 1;
      var3[var4] = ZipUtil.unsignedIntToSignedByte(this.version);
      var3[var5++] = ZipUtil.unsignedIntToSignedByte(var1.length);
      System.arraycopy(var1, 0, var3, var5, var1.length);
      var5 += var1.length;
      var3[var5++] = ZipUtil.unsignedIntToSignedByte(var2.length);
      System.arraycopy(var2, 0, var3, var5, var2.length);
      return var3;
   }

   public byte[] getCentralDirectoryData() {
      return this.getLocalFileDataData();
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) throws ZipException {
      this.reset();
      this.version = ZipUtil.signedByteToUnsignedInt(var1[var2++]);
      int var4 = ZipUtil.signedByteToUnsignedInt(var1[var2++]);
      byte[] var5 = new byte[var4];
      System.arraycopy(var1, var2, var5, 0, var4);
      var2 += var4;
      this.uid = new BigInteger(1, ZipUtil.reverse(var5));
      int var6 = ZipUtil.signedByteToUnsignedInt(var1[var2++]);
      byte[] var7 = new byte[var6];
      System.arraycopy(var1, var2, var7, 0, var6);
      this.gid = new BigInteger(1, ZipUtil.reverse(var7));
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) throws ZipException {
      this.reset();
      this.parseFromLocalFileData(var1, var2, var3);
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

   public boolean equals(Object var1) {
      if (!(var1 instanceof X7875_NewUnix)) {
         return false;
      } else {
         X7875_NewUnix var2 = (X7875_NewUnix)var1;
         return this.version == var2.version && this.uid.equals(var2.uid) && this.gid.equals(var2.gid);
      }
   }

   public int hashCode() {
      int var1 = -1234567 * this.version;
      var1 ^= Integer.rotateLeft(this.uid.hashCode(), 16);
      var1 ^= this.gid.hashCode();
      return var1;
   }

   static byte[] trimLeadingZeroesForceMinLength(byte[] var0) {
      if (var0 == null) {
         return var0;
      } else {
         int var1 = 0;
         byte[] var2 = var0;
         int var3 = var0.length;

         int var4;
         for(var4 = 0; var4 < var3; ++var4) {
            byte var5 = var2[var4];
            if (var5 != 0) {
               break;
            }

            ++var1;
         }

         boolean var6 = true;
         byte[] var7 = new byte[Math.max(1, var0.length - var1)];
         var4 = var7.length - (var0.length - var1);
         System.arraycopy(var0, var1, var7, var4, var7.length - var4);
         return var7;
      }
   }
}
