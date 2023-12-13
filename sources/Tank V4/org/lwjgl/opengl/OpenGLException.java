package org.lwjgl.opengl;

public class OpenGLException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public OpenGLException(int var1) {
      this(createErrorMessage(var1));
   }

   private static String createErrorMessage(int var0) {
      String var1 = Util.translateGLErrorString(var0);
      return var1 + " (" + var0 + ")";
   }

   public OpenGLException() {
   }

   public OpenGLException(String var1) {
      super(var1);
   }

   public OpenGLException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public OpenGLException(Throwable var1) {
      super(var1);
   }
}
