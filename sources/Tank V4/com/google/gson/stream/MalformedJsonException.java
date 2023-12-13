package com.google.gson.stream;

import java.io.IOException;

public final class MalformedJsonException extends IOException {
   private static final long serialVersionUID = 1L;

   public MalformedJsonException(String var1) {
      super(var1);
   }

   public MalformedJsonException(String var1, Throwable var2) {
      super(var1);
      this.initCause(var2);
   }

   public MalformedJsonException(Throwable var1) {
      this.initCause(var1);
   }
}
