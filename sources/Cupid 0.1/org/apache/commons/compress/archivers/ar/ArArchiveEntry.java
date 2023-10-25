package org.apache.commons.compress.archivers.ar;

import java.io.File;
import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveEntry implements ArchiveEntry {
  public static final String HEADER = "!<arch>\n";
  
  public static final String TRAILER = "`\n";
  
  private final String name;
  
  private final int userId;
  
  private final int groupId;
  
  private final int mode;
  
  private static final int DEFAULT_MODE = 33188;
  
  private final long lastModified;
  
  private final long length;
  
  public ArArchiveEntry(String name, long length) {
    this(name, length, 0, 0, 33188, System.currentTimeMillis() / 1000L);
  }
  
  public ArArchiveEntry(String name, long length, int userId, int groupId, int mode, long lastModified) {
    this.name = name;
    this.length = length;
    this.userId = userId;
    this.groupId = groupId;
    this.mode = mode;
    this.lastModified = lastModified;
  }
  
  public ArArchiveEntry(File inputFile, String entryName) {
    this(entryName, inputFile.isFile() ? inputFile.length() : 0L, 0, 0, 33188, inputFile.lastModified() / 1000L);
  }
  
  public long getSize() {
    return getLength();
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getUserId() {
    return this.userId;
  }
  
  public int getGroupId() {
    return this.groupId;
  }
  
  public int getMode() {
    return this.mode;
  }
  
  public long getLastModified() {
    return this.lastModified;
  }
  
  public Date getLastModifiedDate() {
    return new Date(1000L * getLastModified());
  }
  
  public long getLength() {
    return this.length;
  }
  
  public boolean isDirectory() {
    return false;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null || getClass() != obj.getClass())
      return false; 
    ArArchiveEntry other = (ArArchiveEntry)obj;
    if (this.name == null) {
      if (other.name != null)
        return false; 
    } else if (!this.name.equals(other.name)) {
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\ar\ArArchiveEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */