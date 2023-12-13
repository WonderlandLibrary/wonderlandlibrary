package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthenticationException;

@Immutable
public class NTLMEngineException extends AuthenticationException {
   private static final long serialVersionUID = 6027981323731768824L;

   public NTLMEngineException() {
   }

   public NTLMEngineException(String var1) {
      super(var1);
   }

   public NTLMEngineException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
