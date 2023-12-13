package org.apache.http.impl.execchain;

import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
class RequestEntityExecHandler implements InvocationHandler {
   private static final Method WRITE_TO_METHOD;
   private final HttpEntity original;
   private boolean consumed = false;

   RequestEntityExecHandler(HttpEntity var1) {
      this.original = var1;
   }

   public HttpEntity getOriginal() {
      return this.original;
   }

   public boolean isConsumed() {
      return this.consumed;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      try {
         if (var2.equals(WRITE_TO_METHOD)) {
            this.consumed = true;
         }

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

   static {
      try {
         WRITE_TO_METHOD = HttpEntity.class.getMethod("writeTo", OutputStream.class);
      } catch (NoSuchMethodException var1) {
         throw new Error(var1);
      }
   }
}
