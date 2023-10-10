package org.apache.commons.compress.archivers.zip;

public final class UnparseableExtraFieldData implements ZipExtraField {
   private static final ZipShort HEADER_ID = new ZipShort(44225);
   private byte[] localFileData;
   private byte[] centralDirectoryData;

   public ZipShort getHeaderId() {
      return HEADER_ID;
   }

   public ZipShort getLocalFileDataLength() {
      return new ZipShort(this.localFileData == null ? 0 : this.localFileData.length);
   }

   public ZipShort getCentralDirectoryLength() {
      return this.centralDirectoryData == null ? this.getLocalFileDataLength() : new ZipShort(this.centralDirectoryData.length);
   }

   public byte[] getLocalFileDataData() {
      return ZipUtil.copy(this.localFileData);
   }

   public byte[] getCentralDirectoryData() {
      return this.centralDirectoryData == null ? this.getLocalFileDataData() : ZipUtil.copy(this.centralDirectoryData);
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) {
      this.localFileData = new byte[var3];
      System.arraycopy(var1, var2, this.localFileData, 0, var3);
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) {
      this.centralDirectoryData = new byte[var3];
      System.arraycopy(var1, var2, this.centralDirectoryData, 0, var3);
      if (this.localFileData == null) {
         this.parseFromLocalFileData(var1, var2, var3);
      }

   }
}
