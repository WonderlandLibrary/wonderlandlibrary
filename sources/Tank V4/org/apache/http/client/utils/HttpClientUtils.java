package org.apache.http.client.utils;

import java.io.Closeable;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {
   private HttpClientUtils() {
   }

   public static void closeQuietly(HttpResponse var0) {
      if (var0 != null) {
         HttpEntity var1 = var0.getEntity();
         if (var1 != null) {
            try {
               EntityUtils.consume(var1);
            } catch (IOException var3) {
            }
         }
      }

   }

   public static void closeQuietly(CloseableHttpResponse var0) {
      if (var0 != null) {
         try {
            EntityUtils.consume(var0.getEntity());
            var0.close();
         } catch (IOException var2) {
         }
      }

   }

   public static void closeQuietly(HttpClient var0) {
      if (var0 != null && var0 instanceof Closeable) {
         try {
            ((Closeable)var0).close();
         } catch (IOException var2) {
         }
      }

   }
}
