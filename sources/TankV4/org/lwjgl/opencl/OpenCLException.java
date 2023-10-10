package org.lwjgl.opencl;

public class OpenCLException extends RuntimeException {
   private static final long serialVersionUID = 1L;

   public OpenCLException() {
   }

   public OpenCLException(String var1) {
      super(var1);
   }

   public OpenCLException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public OpenCLException(Throwable var1) {
      super(var1);
   }
}
