package org.apache.http.protocol;

import java.io.IOException;
import java.net.InetAddress;
import org.apache.http.HttpConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class RequestTargetHost implements HttpRequestInterceptor {
   public void process(HttpRequest var1, HttpContext var2) throws HttpException, IOException {
      Args.notNull(var1, "HTTP request");
      HttpCoreContext var3 = HttpCoreContext.adapt(var2);
      ProtocolVersion var4 = var1.getRequestLine().getProtocolVersion();
      String var5 = var1.getRequestLine().getMethod();
      if (!var5.equalsIgnoreCase("CONNECT") || !var4.lessEquals(HttpVersion.HTTP_1_0)) {
         if (!var1.containsHeader("Host")) {
            HttpHost var6 = var3.getTargetHost();
            if (var6 == null) {
               HttpConnection var7 = var3.getConnection();
               if (var7 instanceof HttpInetConnection) {
                  InetAddress var8 = ((HttpInetConnection)var7).getRemoteAddress();
                  int var9 = ((HttpInetConnection)var7).getRemotePort();
                  if (var8 != null) {
                     var6 = new HttpHost(var8.getHostName(), var9);
                  }
               }

               if (var6 == null) {
                  if (var4.lessEquals(HttpVersion.HTTP_1_0)) {
                     return;
                  }

                  throw new ProtocolException("Target host missing");
               }
            }

            var1.addHeader("Host", var6.toHostString());
         }

      }
   }
}
