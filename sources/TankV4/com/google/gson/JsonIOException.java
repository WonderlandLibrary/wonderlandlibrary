package com.google.gson;

public final class JsonIOException extends JsonParseException {
   private static final long serialVersionUID = 1L;

   public JsonIOException(String var1) {
      super(var1);
   }

   public JsonIOException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public JsonIOException(Throwable var1) {
      super(var1);
   }
}
