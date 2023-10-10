package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpPut extends HttpEntityEnclosingRequestBase {
   public static final String METHOD_NAME = "PUT";

   public HttpPut() {
   }

   public HttpPut(URI var1) {
      this.setURI(var1);
   }

   public HttpPut(String var1) {
      this.setURI(URI.create(var1));
   }

   public String getMethod() {
      return "PUT";
   }
}
