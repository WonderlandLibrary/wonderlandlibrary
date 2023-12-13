package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class CircularRedirectException extends RedirectException {
   private static final long serialVersionUID = 6830063487001091803L;

   public CircularRedirectException() {
   }

   public CircularRedirectException(String var1) {
      super(var1);
   }

   public CircularRedirectException(String var1, Throwable var2) {
      super(var1, var2);
   }
}
