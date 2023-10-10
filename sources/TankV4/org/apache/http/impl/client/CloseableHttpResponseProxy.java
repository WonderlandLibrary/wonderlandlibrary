package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;

@NotThreadSafe
class CloseableHttpResponseProxy implements InvocationHandler {
   private final HttpResponse original;

   CloseableHttpResponseProxy(HttpResponse var1) {
      this.original = var1;
   }

   public void close() throws IOException {
      HttpEntity var1 = this.original.getEntity();
      EntityUtils.consume(var1);
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      String var4 = var2.getName();
      if (var4.equals("close")) {
         this.close();
         return null;
      } else {
         try {
            return var2.invoke(this.original, var3);
         } catch (InvocationTargetException var7) {
            Throwable var6 = var7.getCause();
            if (var6 != null) {
               throw var6;
            } else {
               throw var7;
            }
         }
      }
   }

   public static CloseableHttpResponse newProxy(HttpResponse var0) {
      return (CloseableHttpResponse)Proxy.newProxyInstance(CloseableHttpResponseProxy.class.getClassLoader(), new Class[]{CloseableHttpResponse.class}, new CloseableHttpResponseProxy(var0));
   }
}
