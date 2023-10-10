package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPatch extends HttpEntityEnclosingRequestBase {
   public static final String METHOD_NAME = "PATCH";

   public HttpPatch() {
   }

   public HttpPatch(URI var1) {
      this.setURI(var1);
   }

   public HttpPatch(String var1) {
      this.setURI(URI.create(var1));
   }

   public String getMethod() {
      return "PATCH";
   }
}
