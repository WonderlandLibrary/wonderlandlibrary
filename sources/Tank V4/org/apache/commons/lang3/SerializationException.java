package org.apache.commons.lang3;

public class SerializationException extends RuntimeException {
   private static final long serialVersionUID = 4029025366392702726L;

   public SerializationException() {
   }

   public SerializationException(String var1) {
      super(var1);
   }

   public SerializationException(Throwable var1) {
      super(var1);
   }

   public SerializationException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
