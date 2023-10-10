package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public class UnsupportedDigestAlgorithmException extends RuntimeException {
   private static final long serialVersionUID = 319558534317118022L;

   public UnsupportedDigestAlgorithmException() {
   }

   public UnsupportedDigestAlgorithmException(String var1) {
      super(var1);
   }

   public UnsupportedDigestAlgorithmException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
