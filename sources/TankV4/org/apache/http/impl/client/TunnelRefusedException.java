package org.apache.http.impl.client;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;

/** @deprecated */
@Deprecated
@Immutable
public class TunnelRefusedException extends HttpException {
   private static final long serialVersionUID = -8646722842745617323L;
   private final HttpResponse response;

   public TunnelRefusedException(String var1, HttpResponse var2) {
      super(var1);
      this.response = var2;
   }

   public HttpResponse getResponse() {
      return this.response;
   }
}
