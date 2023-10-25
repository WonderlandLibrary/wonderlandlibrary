package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public class Zip64RequiredException extends ZipException {
  private static final long serialVersionUID = 20110809L;
  
  static final String ARCHIVE_TOO_BIG_MESSAGE = "archive's size exceeds the limit of 4GByte.";
  
  static final String TOO_MANY_ENTRIES_MESSAGE = "archive contains more than 65535 entries.";
  
  static String getEntryTooBigMessage(ZipArchiveEntry ze) {
    return ze.getName() + "'s size exceeds the limit of 4GByte.";
  }
  
  public Zip64RequiredException(String reason) {
    super(reason);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\Zip64RequiredException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */