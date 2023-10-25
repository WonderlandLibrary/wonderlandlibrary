package org.apache.commons.compress.compressors.gzip;

public class GzipParameters {
  private int compressionLevel = -1;
  
  private long modificationTime;
  
  private String filename;
  
  private String comment;
  
  private int operatingSystem = 255;
  
  public int getCompressionLevel() {
    return this.compressionLevel;
  }
  
  public void setCompressionLevel(int compressionLevel) {
    if (compressionLevel < -1 || compressionLevel > 9)
      throw new IllegalArgumentException("Invalid gzip compression level: " + compressionLevel); 
    this.compressionLevel = compressionLevel;
  }
  
  public long getModificationTime() {
    return this.modificationTime;
  }
  
  public void setModificationTime(long modificationTime) {
    this.modificationTime = modificationTime;
  }
  
  public String getFilename() {
    return this.filename;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public void setComment(String comment) {
    this.comment = comment;
  }
  
  public int getOperatingSystem() {
    return this.operatingSystem;
  }
  
  public void setOperatingSystem(int operatingSystem) {
    this.operatingSystem = operatingSystem;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\gzip\GzipParameters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */