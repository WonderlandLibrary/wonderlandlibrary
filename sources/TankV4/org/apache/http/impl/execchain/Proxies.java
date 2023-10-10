package org.apache.http.impl.execchain;

import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;

@NotThreadSafe
class Proxies {
   static void enhanceEntity(HttpEntityEnclosingRequest var0) {
      HttpEntity var1 = var0.getEntity();
      if (var1 != null && !var1.isRepeatable() && var1 != null) {
         HttpEntity var2 = (HttpEntity)Proxy.newProxyInstance(HttpEntity.class.getClassLoader(), new Class[]{HttpEntity.class}, new RequestEntityExecHandler(var1));
         var0.setEntity(var2);
      }

   }

   static boolean isRepeatable(HttpRequest var0) {
      if (var0 instanceof HttpEntityEnclosingRequest) {
         HttpEntity var1 = ((HttpEntityEnclosingRequest)var0).getEntity();
         if (var1 != null) {
            if (var1 != null) {
               RequestEntityExecHandler var2 = (RequestEntityExecHandler)Proxy.getInvocationHandler(var1);
               if (!var2.isConsumed()) {
                  return true;
               }
            }

            return var1.isRepeatable();
         }
      }

      return true;
   }

   public static CloseableHttpResponse enhanceResponse(HttpResponse var0, ConnectionHolder var1) {
      return (CloseableHttpResponse)Proxy.newProxyInstance(ResponseProxyHandler.class.getClassLoader(), new Class[]{CloseableHttpResponse.class}, new ResponseProxyHandler(var0, var1));
   }
}
