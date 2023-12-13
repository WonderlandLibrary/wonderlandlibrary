package org.apache.http.cookie;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class MalformedCookieException extends ProtocolException {
   private static final long serialVersionUID = -6695462944287282185L;

   public MalformedCookieException() {
   }

   public MalformedCookieException(String var1) {
      super(var1);
   }

   public MalformedCookieException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
