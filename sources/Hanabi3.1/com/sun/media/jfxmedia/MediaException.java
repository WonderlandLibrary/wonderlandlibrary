package com.sun.media.jfxmedia;

public class MediaException extends RuntimeException {
   private static final long serialVersionUID = 14L;
   private MediaError error = null;

   public MediaException(String var1) {
      super(var1);
   }

   public MediaException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public MediaException(String var1, Throwable var2, MediaError var3) {
      super(var1, var2);
      this.error = var3;
   }

   public MediaError getMediaError() {
      return this.error;
   }
}
