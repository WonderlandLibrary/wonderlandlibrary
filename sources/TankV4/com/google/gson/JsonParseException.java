package com.google.gson;

public class JsonParseException extends RuntimeException {
   static final long serialVersionUID = -4086729973971783390L;

   public JsonParseException(String var1) {
      super(var1);
   }

   public JsonParseException(String var1, Throwable var2) {
      super(var1, var2);
   }

   public JsonParseException(Throwable var1) {
      super(var1);
   }
}
