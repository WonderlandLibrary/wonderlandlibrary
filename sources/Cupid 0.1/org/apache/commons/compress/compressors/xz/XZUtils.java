package org.apache.commons.compress.compressors.xz;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.compressors.FileNameUtil;

public class XZUtils {
  private static final FileNameUtil fileNameUtil;
  
  static {
    Map<String, String> uncompressSuffix = new HashMap<String, String>();
    uncompressSuffix.put(".txz", ".tar");
    uncompressSuffix.put(".xz", "");
    uncompressSuffix.put("-xz", "");
    fileNameUtil = new FileNameUtil(uncompressSuffix, ".xz");
  }
  
  public static boolean isXZCompressionAvailable() {
    try {
      XZCompressorInputStream.matches(null, 0);
      return true;
    } catch (NoClassDefFoundError error) {
      return false;
    } 
  }
  
  public static boolean isCompressedFilename(String filename) {
    return fileNameUtil.isCompressedFilename(filename);
  }
  
  public static String getUncompressedFilename(String filename) {
    return fileNameUtil.getUncompressedFilename(filename);
  }
  
  public static String getCompressedFilename(String filename) {
    return fileNameUtil.getCompressedFilename(filename);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\compressors\xz\XZUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */