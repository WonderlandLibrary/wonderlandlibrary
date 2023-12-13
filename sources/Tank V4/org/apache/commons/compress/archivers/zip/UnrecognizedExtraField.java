package org.apache.commons.compress.archivers.zip;

public class UnrecognizedExtraField implements ZipExtraField {
   private ZipShort headerId;
   private byte[] localData;
   private byte[] centralData;

   public void setHeaderId(ZipShort var1) {
      this.headerId = var1;
   }

   public ZipShort getHeaderId() {
      return this.headerId;
   }

   public void setLocalFileDataData(byte[] var1) {
      this.localData = ZipUtil.copy(var1);
   }

   public ZipShort getLocalFileDataLength() {
      return new ZipShort(this.localData != null ? this.localData.length : 0);
   }

   public byte[] getLocalFileDataData() {
      return ZipUtil.copy(this.localData);
   }

   public void setCentralDirectoryData(byte[] var1) {
      this.centralData = ZipUtil.copy(var1);
   }

   public ZipShort getCentralDirectoryLength() {
      return this.centralData != null ? new ZipShort(this.centralData.length) : this.getLocalFileDataLength();
   }

   public byte[] getCentralDirectoryData() {
      return this.centralData != null ? ZipUtil.copy(this.centralData) : this.getLocalFileDataData();
   }

   public void parseFromLocalFileData(byte[] var1, int var2, int var3) {
      byte[] var4 = new byte[var3];
      System.arraycopy(var1, var2, var4, 0, var3);
      this.setLocalFileDataData(var4);
   }

   public void parseFromCentralDirectoryData(byte[] var1, int var2, int var3) {
      byte[] var4 = new byte[var3];
      System.arraycopy(var1, var2, var4, 0, var3);
      this.setCentralDirectoryData(var4);
      if (this.localData == null) {
         this.setLocalFileDataData(var4);
      }

   }
}
