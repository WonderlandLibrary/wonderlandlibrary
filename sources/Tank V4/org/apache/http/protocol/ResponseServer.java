package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class ResponseServer implements HttpResponseInterceptor {
   private final String originServer;

   public ResponseServer(String var1) {
      this.originServer = var1;
   }

   public ResponseServer() {
      this((String)null);
   }

   public void process(HttpResponse var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP response");
      if (!var1.containsHeader("Server") && this.originServer != null) {
         var1.addHeader("Server", this.originServer);
      }

   }
}
