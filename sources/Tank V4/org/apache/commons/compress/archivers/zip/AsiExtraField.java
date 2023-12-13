package org.apache.commons.compress.archivers.zip;

import java.util.zip.CRC32;
import java.util.zip.ZipException;

public class AsiExtraField implements ZipExtraField, UnixStat, Cloneable {
   private static final ZipShort HEADER_ID = new ZipShort(30062);
   private static final int WORD = 4;
   private int mode = 0;
   private int uid = 0;
   private int gid = 0;
   private String link = "";
   private boolean dirFlag = false;
   private CRC32 crc = new CRC32();

   public ZipShort getHeaderId() {
      return HEADER_ID;
   }

   public ZipShort getLocalFileDataLength() {
      return new ZipShort(14 + this.getLinkedFile().getBytes().length);
   }

   public ZipShort getCentralDirectoryLength() {
      return this.getLocalFileDataLength();
   }

   public byte[] getLocalFileDataData() {
      byte[] var1 = new byte[this.getLocalFileDataLength().getValue() - 4];
      System.arraycopy(ZipShort.getBytes(this.getMode()), 0, var1, 0, 2);
      byte[] var2 = this.getLinkedFile().getBytes();
      System.arraycopy(ZipLong.getBytes((long)var2.length), 0, var1, 2, 4);
      System.arraycopy(ZipShort.getBytes(this.getUserId()), 0, var1, 6, 2);
      System.arraycopy(ZipShort.getBytes(this.getGroupId()), 0, var1, 8, 2);
      System.arraycopy(var2, 0, var1, 10, var2.length);
      this.crc.reset();
      this.crc.update(var1);
      long var3 = this.crc.getValue();
      byte[] var5 = new byte[var1.length + 4];
      System.arraycopy(ZipLong.getBytes(var3), 0, var5, 0, 4);
      System.arraycopy(var1, 0, var5, 4, var1.length);
      return var5;
   }

   public byte[] getCentralDirectoryData() {
      return this.getLocalFileDataData();
   }

   public void setUserId(int var1) {
      this.uid = var1;
   }

   public int getUserId() {
      return this.uid;
   }

   public void setGroupId(int var1) {
      this.gid = var1;
   }

   public int getGroupId() {
      return this.gid;
   }

   public void setLinkedFile(String var1) {
      this.link = var1;
      this.mode = this.getMode(this.mode);
   }

   public String getLinkedFile() {
      return this.link;
   }

   public void setMode(int var1) {
      this.mode = this.getMode(var1);
   }

   public int getMode() {
      return this.mode;
   }

   public void setDirectory(boolean var1) {
      this.dirFlag = var1;
      this.mode = this.getMode(this.mode);
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) throws ZipException {
      long var4 = ZipLong.getValue(var1, var2);
      byte[] var6 = new byte[var3 - 4];
      System.arraycopy(var1, var2 + 4, var6, 0, var3 - 4);
      this.crc.reset();
      this.crc.update(var6);
      long var7 = this.crc.getValue();
      if (var4 != var7) {
         throw new ZipException("bad CRC checksum " + Long.toHexString(var4) + " instead of " + Long.toHexString(var7));
      } else {
         int var9 = ZipShort.getValue(var6, 0);
         byte[] var10 = new byte[(int)ZipLong.getValue(var6, 2)];
         this.uid = ZipShort.getValue(var6, 6);
         this.gid = ZipShort.getValue(var6, 8);
         if (var10.length == 0) {
            this.link = "";
         } else {
            System.arraycopy(var6, 10, var10, 0, var10.length);
            this.link = new String(var10);
         }

         this.setDirectory((var9 & 16384) != 0);
         this.setMode(var9);
      }
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) throws ZipException {
      this.parseFromLocalFileData(var1, var2, var3);
   }

   protected int getMode(int var1) {
      char var2 = '耀';
      if (this != false) {
         var2 = 'ꀀ';
      } else if (this != false) {
         var2 = 16384;
      }

      return var2 | var1 & 4095;
   }

   public Object clone() {
      try {
         AsiExtraField var1 = (AsiExtraField)super.clone();
         var1.crc = new CRC32();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new RuntimeException(var2);
      }
   }
}
