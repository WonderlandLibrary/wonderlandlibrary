package org.apache.http.auth;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class MalformedChallengeException extends ProtocolException {
   private static final long serialVersionUID = 814586927989932284L;

   public MalformedChallengeException() {
   }

   public MalformedChallengeException(String var1) {
      super(var1);
   }

   public MalformedChallengeException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
