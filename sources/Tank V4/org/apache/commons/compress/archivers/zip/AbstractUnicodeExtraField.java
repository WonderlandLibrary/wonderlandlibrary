package org.apache.commons.compress.archivers.zip;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;
import java.util.zip.ZipException;

public abstract class AbstractUnicodeExtraField implements ZipExtraField {
   private long nameCRC32;
   private byte[] unicodeName;
   private byte[] data;

   protected AbstractUnicodeExtraField() {
   }

   protected AbstractUnicodeExtraField(String var1, byte[] var2, int var3, int var4) {
      CRC32 var5 = new CRC32();
      var5.update(var2, var3, var4);
      this.nameCRC32 = var5.getValue();

      try {
         this.unicodeName = var1.getBytes("UTF-8");
      } catch (UnsupportedEncodingException var7) {
         throw new RuntimeException("FATAL: UTF-8 encoding not supported.", var7);
      }
   }

   protected AbstractUnicodeExtraField(String var1, byte[] var2) {
      this(var1, var2, 0, var2.length);
   }

   private void assembleData() {
      if (this.unicodeName != null) {
         this.data = new byte[5 + this.unicodeName.length];
         this.data[0] = 1;
         System.arraycopy(ZipLong.getBytes(this.nameCRC32), 0, this.data, 1, 4);
         System.arraycopy(this.unicodeName, 0, this.data, 5, this.unicodeName.length);
      }
   }

   public long getNameCRC32() {
      return this.nameCRC32;
   }

   public void setNameCRC32(long var1) {
      this.nameCRC32 = var1;
      this.data = null;
   }

   public byte[] getUnicodeName() {
      byte[] var1 = null;
      if (this.unicodeName != null) {
         var1 = new byte[this.unicodeName.length];
         System.arraycopy(this.unicodeName, 0, var1, 0, var1.length);
      }

      return var1;
   }

   public void setUnicodeName(byte[] var1) {
      if (var1 != null) {
         this.unicodeName = new byte[var1.length];
         System.arraycopy(var1, 0, this.unicodeName, 0, var1.length);
      } else {
         this.unicodeName = null;
      }

      this.data = null;
   }

   public byte[] getCentralDirectoryData() {
      if (this.data == null) {
         this.assembleData();
      }

      byte[] var1 = null;
      if (this.data != null) {
         var1 = new byte[this.data.length];
         System.arraycopy(this.data, 0, var1, 0, var1.length);
      }

      return var1;
   }

   public ZipShort getCentralDirectoryLength() {
      if (this.data == null) {
         this.assembleData();
      }

      return new ZipShort(this.data != null ? this.data.length : 0);
   }

   public byte[] getLocalFileDataData() {
      return this.getCentralDirectoryData();
   }

   public ZipShort getLocalFileDataLength() {
      return this.getCentralDirectoryLength();
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) throws ZipException {
      if (var3 < 5) {
         throw new ZipException("UniCode path extra data must have at least 5 bytes.");
      } else {
         byte var4 = var1[var2];
         if (var4 != 1) {
            throw new ZipException("Unsupported version [" + var4 + "] for UniCode path extra data.");
         } else {
            this.nameCRC32 = ZipLong.getValue(var1, var2 + 1);
            this.unicodeName = new byte[var3 - 5];
            System.arraycopy(var1, var2 + 5, this.unicodeName, 0, var3 - 5);
            this.data = null;
         }
      }
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) throws ZipException {
      this.parseFromLocalFileData(var1, var2, var3);
   }
}
