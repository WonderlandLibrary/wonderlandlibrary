package org.lwjgl.openal;

public class OpenALException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public OpenALException() {
   }

   public OpenALException(int var1) {
      super("OpenAL error: " + AL10.alGetString(var1) + " (" + var1 + ")");
   }

   public OpenALException(String var1) {
      super(var1);
   }

   public OpenALException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public OpenALException(Throwable var1) {
      super(var1);
   }
}
