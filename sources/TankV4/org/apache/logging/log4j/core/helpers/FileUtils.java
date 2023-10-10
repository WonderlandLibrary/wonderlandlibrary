package org.apache.logging.log4j.core.helpers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class FileUtils {
   private static final String PROTOCOL_FILE = "file";
   private static final String JBOSS_FILE = "vfsfile";
   private static final Logger LOGGER = StatusLogger.getLogger();

   private FileUtils() {
   }

   public static File fileFromURI(URI var0) {
      if (var0 != null && (var0.getScheme() == null || "file".equals(var0.getScheme()) || "vfsfile".equals(var0.getScheme()))) {
         if (var0.getScheme() == null) {
            try {
               var0 = (new File(var0.getPath())).toURI();
            } catch (Exception var4) {
               LOGGER.warn("Invalid URI " + var0);
               return null;
            }
         }

         try {
            return new File(URLDecoder.decode(var0.toURL().getFile(), "UTF8"));
         } catch (MalformedURLException var2) {
            LOGGER.warn((String)("Invalid URL " + var0), (Throwable)var2);
         } catch (UnsupportedEncodingException var3) {
            LOGGER.warn((String)"Invalid encoding: UTF8", (Throwable)var3);
         }

         return null;
      } else {
         return null;
      }
   }

   public static boolean isFile(URL var0) {
      return var0 != null && (var0.getProtocol().equals("file") || var0.getProtocol().equals("vfsfile"));
   }

   public static void mkdir(File var0, boolean var1) throws IOException {
      if (!var0.exists()) {
         if (!var1) {
            throw new IOException("The directory " + var0.getAbsolutePath() + " does not exist.");
         }

         if (!var0.mkdirs()) {
            throw new IOException("Could not create directory " + var0.getAbsolutePath());
         }
      }

      if (!var0.isDirectory()) {
         throw new IOException("File " + var0 + " exists and is not a directory. Unable to create directory.");
      }
   }
}
