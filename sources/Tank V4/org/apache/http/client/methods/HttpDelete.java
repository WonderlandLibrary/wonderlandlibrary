package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class HttpDelete extends HttpRequestBase {
   public static final String METHOD_NAME = "DELETE";

   public HttpDelete() {
   }

   public HttpDelete(URI var1) {
      this.setURI(var1);
   }

   public HttpDelete(String var1) {
      this.setURI(URI.create(var1));
   }

   public String getMethod() {
      return "DELETE";
   }
}
