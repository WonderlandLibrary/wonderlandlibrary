package org.apache.commons.compress.archivers.zip;

public final class UnparseableExtraFieldData implements ZipExtraField {
  private static final ZipShort HEADER_ID = new ZipShort(44225);
  
  private byte[] localFileData;
  
  private byte[] centralDirectoryData;
  
  public ZipShort getHeaderId() {
    return HEADER_ID;
  }
  
  public ZipShort getLocalFileDataLength() {
    return new ZipShort((this.localFileData == null) ? 0 : this.localFileData.length);
  }
  
  public ZipShort getCentralDirectoryLength() {
    return (this.centralDirectoryData == null) ? getLocalFileDataLength() : new ZipShort(this.centralDirectoryData.length);
  }
  
  public byte[] getLocalFileDataData() {
    return ZipUtil.copy(this.localFileData);
  }
  
  public byte[] getCentralDirectoryData() {
    return (this.centralDirectoryData == null) ? getLocalFileDataData() : ZipUtil.copy(this.centralDirectoryData);
  }
  
  public void parseFromLocalFileData(byte[] buffer, int offset, int length) {
    this.localFileData = new byte[length];
    System.arraycopy(buffer, offset, this.localFileData, 0, length);
  }
  
  public void parseFromCentralDirectoryData(byte[] buffer, int offset, int length) {
    this.centralDirectoryData = new byte[length];
    System.arraycopy(buffer, offset, this.centralDirectoryData, 0, length);
    if (this.localFileData == null)
      parseFromLocalFileData(buffer, offset, length); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\UnparseableExtraFieldData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */