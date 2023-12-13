package org.apache.http.impl.client;

import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Immutable
public class DefaultConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
   public static final DefaultConnectionKeepAliveStrategy INSTANCE = new DefaultConnectionKeepAliveStrategy();

   public long getKeepAliveDuration(HttpResponse var1, HttpContext var2) {
      Args.notNull(var1, "HTTP response");
      BasicHeaderElementIterator var3 = new BasicHeaderElementIterator(var1.headerIterator("Keep-Alive"));

      while(true) {
         String var5;
         String var6;
         do {
            do {
               if (!var3.hasNext()) {
                  return -1L;
               }

               HeaderElement var4 = var3.nextElement();
               var5 = var4.getName();
               var6 = var4.getValue();
            } while(var6 == null);
         } while(!var5.equalsIgnoreCase("timeout"));

         try {
            return Long.parseLong(var6) * 1000L;
         } catch (NumberFormatException var8) {
         }
      }
   }
}
