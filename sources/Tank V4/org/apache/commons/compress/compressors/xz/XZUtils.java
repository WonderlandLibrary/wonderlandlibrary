package org.apache.commons.compress.compressors.xz;

import java.util.HashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public class XZUtils {
   private static final FileNameUtil fileNameUtil;

   private XZUtils() {
   }

   public static boolean isXZCompressionAvailable() {
      try {
         XZCompressorInputStream.matches((byte[])null, 0);
         return true;
      } catch (NoClassDefFoundError var1) {
         return false;
      }
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
      HashMap var0 = new HashMap();
      var0.put(".txz", ".tar");
      var0.put(".xz", "");
      var0.put("-xz", "");
      fileNameUtil = new FileNameUtil(var0, ".xz");
   }
}
