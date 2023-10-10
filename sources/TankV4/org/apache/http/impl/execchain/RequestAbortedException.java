package org.apache.http.impl.execchain;

import java.io.InterruptedIOException;
import org.apache.http.annotation.Immutable;

@Immutable
public class RequestAbortedException extends InterruptedIOException {
   private static final long serialVersionUID = 4973849966012490112L;

   public RequestAbortedException(String var1) {
      super(var1);
   }

   public RequestAbortedException(String var1, Throwable var2) {
      super(var1);
      if (var2 != null) {
         this.initCause(var2);
      }

   }
}
