package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class ResponseProxyHandler implements InvocationHandler {
   private static final Method CLOSE_METHOD;
   private final HttpResponse original;
   private final ConnectionHolder connHolder;

   ResponseProxyHandler(HttpResponse var1, ConnectionHolder var2) {
      this.original = var1;
      this.connHolder = var2;
      HttpEntity var3 = var1.getEntity();
      if (var3 != null && var3.isStreaming() && var2 != null) {
         this.original.setEntity(new ResponseEntityWrapper(var3, var2));
      }

   }

   public void close() throws IOException {
      if (this.connHolder != null) {
         this.connHolder.abortConnection();
      }

   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.equals(CLOSE_METHOD)) {
         this.close();
         return null;
      } else {
         try {
            return var2.invoke(this.original, var3);
         } catch (InvocationTargetException var6) {
            Throwable var5 = var6.getCause();
            if (var5 != null) {
               throw var5;
            } else {
               throw var6;
            }
         }
      }
   }

   static {
      try {
         CLOSE_METHOD = Closeable.class.getMethod("close");
      } catch (NoSuchMethodException var1) {
         throw new Error(var1);
      }
   }
}
