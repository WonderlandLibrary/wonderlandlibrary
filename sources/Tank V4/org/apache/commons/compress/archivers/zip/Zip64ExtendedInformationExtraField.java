package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public class Zip64ExtendedInformationExtraField implements ZipExtraField {
   static final ZipShort HEADER_ID = new ZipShort(1);
   private static final String LFH_MUST_HAVE_BOTH_SIZES_MSG = "Zip64 extended information must contain both size values in the local file header.";
   private static final byte[] EMPTY = new byte[0];
   private ZipEightByteInteger size;
   private ZipEightByteInteger compressedSize;
   private ZipEightByteInteger relativeHeaderOffset;
   private ZipLong diskStart;
   private byte[] rawCentralDirectoryData;

   public Zip64ExtendedInformationExtraField() {
   }

   public Zip64ExtendedInformationExtraField(ZipEightByteInteger var1, ZipEightByteInteger var2) {
      this(var1, var2, (ZipEightByteInteger)null, (ZipLong)null);
   }

   public Zip64ExtendedInformationExtraField(ZipEightByteInteger var1, ZipEightByteInteger var2, ZipEightByteInteger var3, ZipLong var4) {
      this.size = var1;
      this.compressedSize = var2;
      this.relativeHeaderOffset = var3;
      this.diskStart = var4;
   }

   public ZipShort getHeaderId() {
      return HEADER_ID;
   }

   public ZipShort getLocalFileDataLength() {
      return new ZipShort(this.size != null ? 16 : 0);
   }

   public ZipShort getCentralDirectoryLength() {
      return new ZipShort((this.size != null ? 8 : 0) + (this.compressedSize != null ? 8 : 0) + (this.relativeHeaderOffset != null ? 8 : 0) + (this.diskStart != null ? 4 : 0));
   }

   public byte[] getLocalFileDataData() {
      if (this.size == null && this.compressedSize == null) {
         return EMPTY;
      } else if (this.size != null && this.compressedSize != null) {
         byte[] var1 = new byte[16];
         this.addSizes(var1);
         return var1;
      } else {
         throw new IllegalArgumentException("Zip64 extended information must contain both size values in the local file header.");
      }
   }

   public byte[] getCentralDirectoryData() {
      byte[] var1 = new byte[this.getCentralDirectoryLength().getValue()];
      int var2 = this.addSizes(var1);
      if (this.relativeHeaderOffset != null) {
         System.arraycopy(this.relativeHeaderOffset.getBytes(), 0, var1, var2, 8);
         var2 += 8;
      }

      if (this.diskStart != null) {
         System.arraycopy(this.diskStart.getBytes(), 0, var1, var2, 4);
         var2 += 4;
      }

      return var1;
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) throws ZipException {
      if (var3 != 0) {
         if (var3 < 16) {
            throw new ZipException("Zip64 extended information must contain both size values in the local file header.");
         } else {
            this.size = new ZipEightByteInteger(var1, var2);
            var2 += 8;
            this.compressedSize = new ZipEightByteInteger(var1, var2);
            var2 += 8;
            int var4 = var3 - 16;
            if (var4 >= 8) {
               this.relativeHeaderOffset = new ZipEightByteInteger(var1, var2);
               var2 += 8;
               var4 -= 8;
            }

            if (var4 >= 4) {
               this.diskStart = new ZipLong(var1, var2);
               var2 += 4;
               var4 -= 4;
            }

         }
      }
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) throws ZipException {
      this.rawCentralDirectoryData = new byte[var3];
      System.arraycopy(var1, var2, this.rawCentralDirectoryData, 0, var3);
      if (var3 >= 28) {
         this.parseFromLocalFileData(var1, var2, var3);
      } else if (var3 == 24) {
         this.size = new ZipEightByteInteger(var1, var2);
         var2 += 8;
         this.compressedSize = new ZipEightByteInteger(var1, var2);
         var2 += 8;
         this.relativeHeaderOffset = new ZipEightByteInteger(var1, var2);
      } else if (var3 % 8 == 4) {
         this.diskStart = new ZipLong(var1, var2 + var3 - 4);
      }

   }

   public void reparseCentralDirectoryData(boolean var1, boolean var2, boolean var3, boolean var4) throws ZipException {
      if (this.rawCentralDirectoryData != null) {
         int var5 = (var1 ? 8 : 0) + (var2 ? 8 : 0) + (var3 ? 8 : 0) + (var4 ? 4 : 0);
         if (this.rawCentralDirectoryData.length < var5) {
            throw new ZipException("central directory zip64 extended information extra field's length doesn't match central directory data.  Expected length " + var5 + " but is " + this.rawCentralDirectoryData.length);
         }

         int var6 = 0;
         if (var1) {
            this.size = new ZipEightByteInteger(this.rawCentralDirectoryData, var6);
            var6 += 8;
         }

         if (var2) {
            this.compressedSize = new ZipEightByteInteger(this.rawCentralDirectoryData, var6);
            var6 += 8;
         }

         if (var3) {
            this.relativeHeaderOffset = new ZipEightByteInteger(this.rawCentralDirectoryData, var6);
            var6 += 8;
         }

         if (var4) {
            this.diskStart = new ZipLong(this.rawCentralDirectoryData, var6);
            var6 += 4;
         }
      }

   }

   public ZipEightByteInteger getSize() {
      return this.size;
   }

   public void setSize(ZipEightByteInteger var1) {
      this.size = var1;
   }

   public ZipEightByteInteger getCompressedSize() {
      return this.compressedSize;
   }

   public void setCompressedSize(ZipEightByteInteger var1) {
      this.compressedSize = var1;
   }

   public ZipEightByteInteger getRelativeHeaderOffset() {
      return this.relativeHeaderOffset;
   }

   public void setRelativeHeaderOffset(ZipEightByteInteger var1) {
      this.relativeHeaderOffset = var1;
   }

   public ZipLong getDiskStartNumber() {
      return this.diskStart;
   }

   public void setDiskStartNumber(ZipLong var1) {
      this.diskStart = var1;
   }

   private int addSizes(byte[] var1) {
      int var2 = 0;
      if (this.size != null) {
         System.arraycopy(this.size.getBytes(), 0, var1, 0, 8);
         var2 += 8;
      }

      if (this.compressedSize != null) {
         System.arraycopy(this.compressedSize.getBytes(), 0, var1, var2, 8);
         var2 += 8;
      }

      return var2;
   }
}
