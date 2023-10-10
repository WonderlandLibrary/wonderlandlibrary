package org.apache.commons.compress.compressors.bzip2;

import java.util.LinkedHashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public abstract class BZip2Utils {
   private static final FileNameUtil fileNameUtil;

   private BZip2Utils() {
   }

   public static boolean isCompressedFilename(String var0) {
      return fileNameUtil.isCompressedFilename(var0);
   }

   public static String getUncompressedFilename(String var0) {
      return fileNameUtil.getUncompressedFilename(var0);
   }

   public static String getCompressedFilename(String var0) {
      return fileNameUtil.getCompressedFilename(var0);
   }

   static {
      LinkedHashMap var0 = new LinkedHashMap();
      var0.put(".tar.bz2", ".tar");
      var0.put(".tbz2", ".tar");
      var0.put(".tbz", ".tar");
      var0.put(".bz2", "");
      var0.put(".bz", "");
      fileNameUtil = new FileNameUtil(var0, ".bz2");
   }
}
