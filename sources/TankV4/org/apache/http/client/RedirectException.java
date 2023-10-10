package org.apache.http.client;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class RedirectException extends ProtocolException {
   private static final long serialVersionUID = 4418824536372559326L;

   public RedirectException() {
   }

   public RedirectException(String var1) {
      super(var1);
   }

   public RedirectException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
