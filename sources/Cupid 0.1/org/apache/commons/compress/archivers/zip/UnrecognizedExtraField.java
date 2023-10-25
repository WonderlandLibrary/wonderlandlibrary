package org.apache.commons.compress.archivers.zip;

public class UnrecognizedExtraField implements ZipExtraField {
  private ZipShort headerId;
  
  private byte[] localData;
  
  private byte[] centralData;
  
  public void setHeaderId(ZipShort headerId) {
    this.headerId = headerId;
  }
  
  public ZipShort getHeaderId() {
    return this.headerId;
  }
  
  public void setLocalFileDataData(byte[] data) {
    this.localData = ZipUtil.copy(data);
  }
  
  public ZipShort getLocalFileDataLength() {
    return new ZipShort((this.localData != null) ? this.localData.length : 0);
  }
  
  public byte[] getLocalFileDataData() {
    return ZipUtil.copy(this.localData);
  }
  
  public void setCentralDirectoryData(byte[] data) {
    this.centralData = ZipUtil.copy(data);
  }
  
  public ZipShort getCentralDirectoryLength() {
    if (this.centralData != null)
      return new ZipShort(this.centralData.length); 
    return getLocalFileDataLength();
  }
  
  public byte[] getCentralDirectoryData() {
    if (this.centralData != null)
      return ZipUtil.copy(this.centralData); 
    return getLocalFileDataData();
  }
  
  public void parseFromLocalFileData(byte[] data, int offset, int length) {
    byte[] tmp = new byte[length];
    System.arraycopy(data, offset, tmp, 0, length);
    setLocalFileDataData(tmp);
  }
  
  public void parseFromCentralDirectoryData(byte[] data, int offset, int length) {
    byte[] tmp = new byte[length];
    System.arraycopy(data, offset, tmp, 0, length);
    setCentralDirectoryData(tmp);
    if (this.localData == null)
      setLocalFileDataData(tmp); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\UnrecognizedExtraField.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */