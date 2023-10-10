package org.apache.commons.compress.compressors.gzip;

import java.util.LinkedHashMap;
import org.apache.commons.compress.compressors.FileNameUtil;

public class GzipUtils {
   private static final FileNameUtil fileNameUtil;

   private GzipUtils() {
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
      var0.put(".tgz", ".tar");
      var0.put(".taz", ".tar");
      var0.put(".svgz", ".svg");
      var0.put(".cpgz", ".cpio");
      var0.put(".wmz", ".wmf");
      var0.put(".emz", ".emf");
      var0.put(".gz", "");
      var0.put(".z", "");
      var0.put("-gz", "");
      var0.put("-z", "");
      var0.put("_z", "");
      fileNameUtil = new FileNameUtil(var0, ".gz");
   }
}
