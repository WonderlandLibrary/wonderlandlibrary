package org.apache.http.client;

import org.apache.http.annotation.Immutable;

@Immutable
public class HttpResponseException extends ClientProtocolException {
   private static final long serialVersionUID = -7186627969477257933L;
   private final int statusCode;

   public HttpResponseException(int var1, String var2) {
      super(var2);
      this.statusCode = var1;
   }

   public int getStatusCode() {
      return this.statusCode;
   }
}
