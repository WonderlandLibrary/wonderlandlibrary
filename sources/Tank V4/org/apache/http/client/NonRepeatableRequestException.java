package org.apache.http.client;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class NonRepeatableRequestException extends ProtocolException {
   private static final long serialVersionUID = 82685265288806048L;

   public NonRepeatableRequestException() {
   }

   public NonRepeatableRequestException(String var1) {
      super(var1);
   }

   public NonRepeatableRequestException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
