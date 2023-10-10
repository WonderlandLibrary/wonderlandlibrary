package com.google.gson;

public final class JsonSyntaxException extends JsonParseException {
   private static final long serialVersionUID = 1L;

   public JsonSyntaxException(String var1) {
      super(var1);
   }

   public JsonSyntaxException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public JsonSyntaxException(Throwable var1) {
      super(var1);
   }
}
