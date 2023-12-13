package javafx.scene.media;

import com.sun.media.jfxmedia.MediaError;
import java.net.UnknownHostException;

public final class MediaException extends RuntimeException {
   private final Type type;

   static Type errorCodeToType(int var0) {
      Type var1;
      if (var0 == MediaError.ERROR_LOCATOR_CONNECTION_LOST.code()) {
         var1 = MediaException.Type.MEDIA_INACCESSIBLE;
      } else if (var0 != MediaError.ERROR_GSTREAMER_SOURCEFILE_NONEXISTENT.code() && var0 != MediaError.ERROR_GSTREAMER_SOURCEFILE_NONREGULAR.code()) {
         if (var0 != MediaError.ERROR_MEDIA_AUDIO_FORMAT_UNSUPPORTED.code() && var0 != MediaError.ERROR_MEDIA_UNKNOWN_PIXEL_FORMAT.code() && var0 != MediaError.ERROR_MEDIA_VIDEO_FORMAT_UNSUPPORTED.code() && var0 != MediaError.ERROR_LOCATOR_CONTENT_TYPE_NULL.code() && var0 != MediaError.ERROR_LOCATOR_UNSUPPORTED_MEDIA_FORMAT.code() && var0 != MediaError.ERROR_LOCATOR_UNSUPPORTED_TYPE.code() && var0 != MediaError.ERROR_GSTREAMER_UNSUPPORTED_PROTOCOL.code() && var0 != MediaError.ERROR_MEDIA_MP3_FORMAT_UNSUPPORTED.code() && var0 != MediaError.ERROR_MEDIA_AAC_FORMAT_UNSUPPORTED.code() && var0 != MediaError.ERROR_MEDIA_H264_FORMAT_UNSUPPORTED.code() && var0 != MediaError.ERROR_MEDIA_HLS_FORMAT_UNSUPPORTED.code()) {
            if (var0 == MediaError.ERROR_MEDIA_CORRUPTED.code()) {
               var1 = MediaException.Type.MEDIA_CORRUPTED;
            } else if ((var0 & MediaError.ERROR_BASE_GSTREAMER.code()) != MediaError.ERROR_BASE_GSTREAMER.code() && (var0 & MediaError.ERROR_BASE_JNI.code()) != MediaError.ERROR_BASE_JNI.code()) {
               var1 = MediaException.Type.UNKNOWN;
            } else {
               var1 = MediaException.Type.PLAYBACK_ERROR;
            }
         } else {
            var1 = MediaException.Type.MEDIA_UNSUPPORTED;
         }
      } else {
         var1 = MediaException.Type.MEDIA_UNAVAILABLE;
      }

      return var1;
   }

   static MediaException exceptionToMediaException(Exception var0) {
      Type var1 = MediaException.Type.UNKNOWN;
      if (var0.getCause() instanceof UnknownHostException) {
         var1 = MediaException.Type.MEDIA_UNAVAILABLE;
      } else if (var0.getCause() instanceof IllegalArgumentException) {
         var1 = MediaException.Type.MEDIA_UNSUPPORTED;
      } else if (var0 instanceof com.sun.media.jfxmedia.MediaException) {
         com.sun.media.jfxmedia.MediaException var2 = (com.sun.media.jfxmedia.MediaException)var0;
         MediaError var3 = var2.getMediaError();
         if (var3 != null) {
            var1 = errorCodeToType(var3.code());
         }
      }

      return new MediaException(var1, var0);
   }

   static MediaException haltException(String var0) {
      return new MediaException(MediaException.Type.PLAYBACK_HALTED, var0);
   }

   static MediaException getMediaException(Object var0, int var1, String var2) {
      String var3 = MediaError.getFromCode(var1).description();
      String var4 = "[" + var0 + "] " + var2 + ": " + var3;
      Type var5 = errorCodeToType(var1);
      return new MediaException(var5, var4);
   }

   MediaException(Type var1, Throwable var2) {
      super(var2);
      this.type = var1;
   }

   MediaException(Type var1, String var2, Throwable var3) {
      super(var2, var3);
      this.type = var1;
   }

   MediaException(Type var1, String var2) {
      super(var2);
      this.type = var1;
   }

   public Type getType() {
      return this.type;
   }

   public String toString() {
      String var1 = "MediaException: " + this.type;
      if (this.getMessage() != null) {
         var1 = var1 + " : " + this.getMessage();
      }

      if (this.getCause() != null) {
         var1 = var1 + " : " + this.getCause();
      }

      return var1;
   }

   public static enum Type {
      MEDIA_CORRUPTED,
      MEDIA_INACCESSIBLE,
      MEDIA_UNAVAILABLE,
      MEDIA_UNSPECIFIED,
      MEDIA_UNSUPPORTED,
      OPERATION_UNSUPPORTED,
      PLAYBACK_ERROR,
      PLAYBACK_HALTED,
      UNKNOWN;
   }
}
