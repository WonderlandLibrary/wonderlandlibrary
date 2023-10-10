package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;
import java.util.Date;
import java.util.zip.ZipException;

public class X5455_ExtendedTimestamp implements ZipExtraField, Cloneable, Serializable {
   private static final ZipShort HEADER_ID = new ZipShort(21589);
   private static final long serialVersionUID = 1L;
   public static final byte MODIFY_TIME_BIT = 1;
   public static final byte ACCESS_TIME_BIT = 2;
   public static final byte CREATE_TIME_BIT = 4;
   private byte flags;
   private boolean bit0_modifyTimePresent;
   private boolean bit1_accessTimePresent;
   private boolean bit2_createTimePresent;
   private ZipLong modifyTime;
   private ZipLong accessTime;
   private ZipLong createTime;

   public ZipShort getHeaderId() {
      return HEADER_ID;
   }

   public ZipShort getLocalFileDataLength() {
      return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0) + (this.bit1_accessTimePresent && this.accessTime != null ? 4 : 0) + (this.bit2_createTimePresent && this.createTime != null ? 4 : 0));
   }

   public ZipShort getCentralDirectoryLength() {
      return new ZipShort(1 + (this.bit0_modifyTimePresent ? 4 : 0));
   }

   public byte[] getLocalFileDataData() {
      byte[] var1 = new byte[this.getLocalFileDataLength().getValue()];
      byte var2 = 0;
      byte var10001 = var2;
      int var3 = var2 + 1;
      var1[var10001] = 0;
      if (this.bit0_modifyTimePresent) {
         var1[0] = (byte)(var1[0] | 1);
         System.arraycopy(this.modifyTime.getBytes(), 0, var1, var3, 4);
         var3 += 4;
      }

      if (this.bit1_accessTimePresent && this.accessTime != null) {
         var1[0] = (byte)(var1[0] | 2);
         System.arraycopy(this.accessTime.getBytes(), 0, var1, var3, 4);
         var3 += 4;
      }

      if (this.bit2_createTimePresent && this.createTime != null) {
         var1[0] = (byte)(var1[0] | 4);
         System.arraycopy(this.createTime.getBytes(), 0, var1, var3, 4);
         var3 += 4;
      }

      return var1;
   }

   public byte[] getCentralDirectoryData() {
      byte[] var1 = new byte[this.getCentralDirectoryLength().getValue()];
      byte[] var2 = this.getLocalFileDataData();
      System.arraycopy(var2, 0, var1, 0, var1.length);
      return var1;
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) throws ZipException {
      this.reset();
      int var4 = var2 + var3;
      this.setFlags(var1[var2++]);
      if (this.bit0_modifyTimePresent) {
         this.modifyTime = new ZipLong(var1, var2);
         var2 += 4;
      }

      if (this.bit1_accessTimePresent && var2 + 4 <= var4) {
         this.accessTime = new ZipLong(var1, var2);
         var2 += 4;
      }

      if (this.bit2_createTimePresent && var2 + 4 <= var4) {
         this.createTime = new ZipLong(var1, var2);
         var2 += 4;
      }

   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) throws ZipException {
      this.reset();
      this.parseFromLocalFileData(var1, var2, var3);
   }

   private void reset() {
      this.setFlags((byte)0);
      this.modifyTime = null;
      this.accessTime = null;
      this.createTime = null;
   }

   public void setFlags(byte var1) {
      this.flags = var1;
      this.bit0_modifyTimePresent = (var1 & 1) == 1;
      this.bit1_accessTimePresent = (var1 & 2) == 2;
      this.bit2_createTimePresent = (var1 & 4) == 4;
   }

   public byte getFlags() {
      return this.flags;
   }

   public boolean isBit0_modifyTimePresent() {
      return this.bit0_modifyTimePresent;
   }

   public boolean isBit1_accessTimePresent() {
      return this.bit1_accessTimePresent;
   }

   public boolean isBit2_createTimePresent() {
      return this.bit2_createTimePresent;
   }

   public ZipLong getModifyTime() {
      return this.modifyTime;
   }

   public ZipLong getAccessTime() {
      return this.accessTime;
   }

   public ZipLong getCreateTime() {
      return this.createTime;
   }

   public Date getModifyJavaTime() {
      return this.modifyTime != null ? new Date(this.modifyTime.getValue() * 1000L) : null;
   }

   public Date getAccessJavaTime() {
      return this.accessTime != null ? new Date(this.accessTime.getValue() * 1000L) : null;
   }

   public Date getCreateJavaTime() {
      return this.createTime != null ? new Date(this.createTime.getValue() * 1000L) : null;
   }

   public void setModifyTime(ZipLong var1) {
      this.bit0_modifyTimePresent = var1 != null;
      this.flags = (byte)(var1 != null ? this.flags | 1 : this.flags & -2);
      this.modifyTime = var1;
   }

   public void setAccessTime(ZipLong var1) {
      this.bit1_accessTimePresent = var1 != null;
      this.flags = (byte)(var1 != null ? this.flags | 2 : this.flags & -3);
      this.accessTime = var1;
   }

   public void setCreateTime(ZipLong var1) {
      this.bit2_createTimePresent = var1 != null;
      this.flags = (byte)(var1 != null ? this.flags | 4 : this.flags & -5);
      this.createTime = var1;
   }

   public void setModifyJavaTime(Date var1) {
      this.setModifyTime(dateToZipLong(var1));
   }

   public void setAccessJavaTime(Date var1) {
      this.setAccessTime(dateToZipLong(var1));
   }

   public void setCreateJavaTime(Date var1) {
      this.setCreateTime(dateToZipLong(var1));
   }

   private static ZipLong dateToZipLong(Date var0) {
      if (var0 == null) {
         return null;
      } else {
         long var1 = 4294967296L;
         long var3 = var0.getTime() / 1000L;
         if (var3 >= 4294967296L) {
            throw new IllegalArgumentException("Cannot set an X5455 timestamp larger than 2^32: " + var3);
         } else {
            return new ZipLong(var3);
         }
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("0x5455 Zip Extra Field: Flags=");
      var1.append(Integer.toBinaryString(ZipUtil.unsignedIntToSignedByte(this.flags))).append(" ");
      Date var2;
      if (this.bit0_modifyTimePresent && this.modifyTime != null) {
         var2 = this.getModifyJavaTime();
         var1.append(" Modify:[").append(var2).append("] ");
      }

      if (this.bit1_accessTimePresent && this.accessTime != null) {
         var2 = this.getAccessJavaTime();
         var1.append(" Access:[").append(var2).append("] ");
      }

      if (this.bit2_createTimePresent && this.createTime != null) {
         var2 = this.getCreateJavaTime();
         var1.append(" Create:[").append(var2).append("] ");
      }

      return var1.toString();
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof X5455_ExtendedTimestamp)) {
         return false;
      } else {
         X5455_ExtendedTimestamp var2 = (X5455_ExtendedTimestamp)var1;
         return (this.flags & 7) == (var2.flags & 7) && (this.modifyTime == var2.modifyTime || this.modifyTime != null && this.modifyTime.equals(var2.modifyTime)) && (this.accessTime == var2.accessTime || this.accessTime != null && this.accessTime.equals(var2.accessTime)) && (this.createTime == var2.createTime || this.createTime != null && this.createTime.equals(var2.createTime));
      }
   }

   public int hashCode() {
      int var1 = -123 * (this.flags & 7);
      if (this.modifyTime != null) {
         var1 ^= this.modifyTime.hashCode();
      }

      if (this.accessTime != null) {
         var1 ^= Integer.rotateLeft(this.accessTime.hashCode(), 11);
      }

      if (this.createTime != null) {
         var1 ^= Integer.rotateLeft(this.createTime.hashCode(), 22);
      }

      return var1;
   }
}
