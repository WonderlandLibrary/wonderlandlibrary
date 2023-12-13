package com.sun.media.jfxmediaimpl;

import com.sun.media.jfxmedia.MediaError;
import com.sun.media.jfxmedia.MediaException;
import com.sun.media.jfxmedia.events.MediaErrorListener;
import com.sun.media.jfxmedia.logging.Logger;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ListIterator;

public class MediaUtils {
   public static final int MAX_FILE_SIGNATURE_LENGTH = 22;
   static final String NATIVE_MEDIA_ERROR_FORMAT = "Internal media error: %d";
   static final String NATIVE_MEDIA_WARNING_FORMAT = "Internal media warning: %d";
   public static final String CONTENT_TYPE_AIFF = "audio/x-aiff";
   public static final String CONTENT_TYPE_MP3 = "audio/mp3";
   public static final String CONTENT_TYPE_MPA = "audio/mpeg";
   public static final String CONTENT_TYPE_WAV = "audio/x-wav";
   public static final String CONTENT_TYPE_JFX = "video/x-javafx";
   public static final String CONTENT_TYPE_FLV = "video/x-flv";
   public static final String CONTENT_TYPE_MP4 = "video/mp4";
   public static final String CONTENT_TYPE_M4A = "audio/x-m4a";
   public static final String CONTENT_TYPE_M4V = "video/x-m4v";
   public static final String CONTENT_TYPE_M3U8 = "application/vnd.apple.mpegurl";
   public static final String CONTENT_TYPE_M3U = "audio/mpegurl";
   private static final String FILE_TYPE_AIF = "aif";
   private static final String FILE_TYPE_AIFF = "aiff";
   private static final String FILE_TYPE_FLV = "flv";
   private static final String FILE_TYPE_FXM = "fxm";
   private static final String FILE_TYPE_MPA = "mp3";
   private static final String FILE_TYPE_WAV = "wav";
   private static final String FILE_TYPE_MP4 = "mp4";
   private static final String FILE_TYPE_M4A = "m4a";
   private static final String FILE_TYPE_M4V = "m4v";
   private static final String FILE_TYPE_M3U8 = "m3u8";
   private static final String FILE_TYPE_M3U = "m3u";

   public static String fileSignatureToContentType(byte[] var0, int var1) throws MediaException {
      String var2 = "application/octet-stream";
      if (var1 < 22) {
         throw new MediaException("Empty signature!");
      } else if (var0.length < 22) {
         return var2;
      } else {
         if ((var0[0] & 255) == 70 && (var0[1] & 255) == 76 && (var0[2] & 255) == 86) {
            var2 = "video/x-javafx";
         } else if (((var0[0] & 255) << 24 | (var0[1] & 255) << 16 | (var0[2] & 255) << 8 | var0[3] & 255) == 1380533830 && ((var0[8] & 255) << 24 | (var0[9] & 255) << 16 | (var0[10] & 255) << 8 | var0[11] & 255) == 1463899717 && ((var0[12] & 255) << 24 | (var0[13] & 255) << 16 | (var0[14] & 255) << 8 | var0[15] & 255) == 1718449184) {
            if (((var0[20] & 255) != 1 || (var0[21] & 255) != 0) && ((var0[20] & 255) != 3 || (var0[21] & 255) != 0)) {
               throw new MediaException("Compressed WAVE is not supported!");
            }

            var2 = "audio/x-wav";
         } else if (((var0[0] & 255) << 24 | (var0[1] & 255) << 16 | (var0[2] & 255) << 8 | var0[3] & 255) == 1380533830 && ((var0[8] & 255) << 24 | (var0[9] & 255) << 16 | (var0[10] & 255) << 8 | var0[11] & 255) == 1463899717) {
            var2 = "audio/x-wav";
         } else if (((var0[0] & 255) << 24 | (var0[1] & 255) << 16 | (var0[2] & 255) << 8 | var0[3] & 255) == 1179603533 && ((var0[8] & 255) << 24 | (var0[9] & 255) << 16 | (var0[10] & 255) << 8 | var0[11] & 255) == 1095321158 && ((var0[12] & 255) << 24 | (var0[13] & 255) << 16 | (var0[14] & 255) << 8 | var0[15] & 255) == 1129270605) {
            var2 = "audio/x-aiff";
         } else if ((var0[0] & 255) == 73 && (var0[1] & 255) == 68 && (var0[2] & 255) == 51) {
            var2 = "audio/mpeg";
         } else if ((var0[0] & 255) == 255 && (var0[1] & 224) == 224 && (var0[2] & 24) != 8 && (var0[3] & 6) != 0) {
            var2 = "audio/mpeg";
         } else {
            if (((var0[4] & 255) << 24 | (var0[5] & 255) << 16 | (var0[6] & 255) << 8 | var0[7] & 255) != 1718909296) {
               throw new MediaException("Unrecognized file signature!");
            }

            if ((var0[8] & 255) == 77 && (var0[9] & 255) == 52 && (var0[10] & 255) == 65 && (var0[11] & 255) == 32) {
               var2 = "audio/x-m4a";
            } else if ((var0[8] & 255) == 77 && (var0[9] & 255) == 52 && (var0[10] & 255) == 86 && (var0[11] & 255) == 32) {
               var2 = "video/x-m4v";
            } else if ((var0[8] & 255) == 109 && (var0[9] & 255) == 112 && (var0[10] & 255) == 52 && (var0[11] & 255) == 50) {
               var2 = "video/mp4";
            } else if ((var0[8] & 255) == 105 && (var0[9] & 255) == 115 && (var0[10] & 255) == 111 && (var0[11] & 255) == 109) {
               var2 = "video/mp4";
            } else if ((var0[8] & 255) == 77 && (var0[9] & 255) == 80 && (var0[10] & 255) == 52 && (var0[11] & 255) == 32) {
               var2 = "video/mp4";
            }
         }

         return var2;
      }
   }

   public static String filenameToContentType(String var0) {
      String var1 = "application/octet-stream";
      int var2 = var0.lastIndexOf(".");
      if (var2 != -1) {
         String var3 = var0.toLowerCase().substring(var2 + 1);
         if (!var3.equals("aif") && !var3.equals("aiff")) {
            if (!var3.equals("flv") && !var3.equals("fxm")) {
               if (var3.equals("mp3")) {
                  var1 = "audio/mpeg";
               } else if (var3.equals("wav")) {
                  var1 = "audio/x-wav";
               } else if (var3.equals("mp4")) {
                  var1 = "video/mp4";
               } else if (var3.equals("m4a")) {
                  var1 = "audio/x-m4a";
               } else if (var3.equals("m4v")) {
                  var1 = "video/x-m4v";
               } else if (var3.equals("m3u8")) {
                  var1 = "application/vnd.apple.mpegurl";
               } else if (var3.equals("m3u")) {
                  var1 = "audio/mpegurl";
               }
            } else {
               var1 = "video/x-javafx";
            }
         } else {
            var1 = "audio/x-aiff";
         }
      }

      return var1;
   }

   public static void warning(Object var0, String var1) {
      if (var0 != null & var1 != null) {
         Logger.logMsg(3, var0.getClass().getName() + ": " + var1);
      }

   }

   public static void error(Object var0, int var1, String var2, Throwable var3) {
      if (var3 != null) {
         StackTraceElement[] var4 = var3.getStackTrace();
         if (var4 != null && var4.length > 0) {
            StackTraceElement var5 = var4[0];
            Logger.logMsg(4, var5.getClassName(), var5.getMethodName(), "( " + var5.getLineNumber() + ") " + var2);
         }
      }

      List var7 = NativeMediaManager.getDefaultInstance().getMediaErrorListeners();
      if (!var7.isEmpty()) {
         ListIterator var9 = var7.listIterator();

         while(var9.hasNext()) {
            MediaErrorListener var6 = (MediaErrorListener)((WeakReference)var9.next()).get();
            if (var6 != null) {
               var6.onError(var0, var1, var2);
            } else {
               var9.remove();
            }
         }

      } else {
         MediaException var8 = var3 instanceof MediaException ? (MediaException)var3 : new MediaException(var2, var3);
         throw var8;
      }
   }

   public static void nativeWarning(Object var0, int var1, String var2) {
      String var3 = String.format("Internal media warning: %d", var1);
      if (var2 != null) {
         var3 = var3 + ": " + var2;
      }

      Logger.logMsg(3, var3);
   }

   public static void nativeError(Object var0, MediaError var1) {
      Logger.logMsg(4, var1.description());
      List var2 = NativeMediaManager.getDefaultInstance().getMediaErrorListeners();
      if (!var2.isEmpty()) {
         ListIterator var3 = var2.listIterator();

         while(var3.hasNext()) {
            MediaErrorListener var4 = (MediaErrorListener)((WeakReference)var3.next()).get();
            if (var4 != null) {
               var4.onError(var0, var1.code(), var1.description());
            } else {
               var3.remove();
            }
         }

      } else {
         throw new MediaException(var1.description(), (Throwable)null, var1);
      }
   }
}
