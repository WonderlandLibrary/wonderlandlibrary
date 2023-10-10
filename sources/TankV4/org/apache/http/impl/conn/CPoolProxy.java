package org.apache.http.impl.conn;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
class CPoolProxy implements InvocationHandler {
   private static final Method CLOSE_METHOD;
   private static final Method SHUTDOWN_METHOD;
   private static final Method IS_OPEN_METHOD;
   private static final Method IS_STALE_METHOD;
   private volatile CPoolEntry poolEntry;

   CPoolProxy(CPoolEntry var1) {
      this.poolEntry = var1;
   }

   CPoolEntry getPoolEntry() {
      return this.poolEntry;
   }

   CPoolEntry detach() {
      CPoolEntry var1 = this.poolEntry;
      this.poolEntry = null;
      return var1;
   }

   HttpClientConnection getConnection() {
      CPoolEntry var1 = this.poolEntry;
      return var1 == null ? null : (HttpClientConnection)var1.getConnection();
   }

   public void close() throws IOException {
      CPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         var1.closeConnection();
      }

   }

   public void shutdown() throws IOException {
      CPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         var1.shutdownConnection();
      }

   }

   public boolean isOpen() {
      CPoolEntry var1 = this.poolEntry;
      if (var1 != null) {
         return !var1.isClosed();
      } else {
         return false;
      }
   }

   public boolean isStale() {
      HttpClientConnection var1 = this.getConnection();
      return var1 != null ? var1.isStale() : true;
   }

   public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
      if (var2.equals(CLOSE_METHOD)) {
         this.close();
         return null;
      } else if (var2.equals(SHUTDOWN_METHOD)) {
         this.shutdown();
         return null;
      } else if (var2.equals(IS_OPEN_METHOD)) {
         return this.isOpen();
      } else if (var2.equals(IS_STALE_METHOD)) {
         return this.isStale();
      } else {
         HttpClientConnection var4 = this.getConnection();
         if (var4 == null) {
            throw new ConnectionShutdownException();
         } else {
            try {
               return var2.invoke(var4, var3);
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
   }

   public static HttpClientConnection newProxy(CPoolEntry var0) {
      return (HttpClientConnection)Proxy.newProxyInstance(CPoolProxy.class.getClassLoader(), new Class[]{ManagedHttpClientConnection.class, HttpContext.class}, new CPoolProxy(var0));
   }

   private static CPoolProxy getHandler(HttpClientConnection var0) {
      InvocationHandler var1 = Proxy.getInvocationHandler(var0);
      if (!CPoolProxy.class.isInstance(var1)) {
         throw new IllegalStateException("Unexpected proxy handler class: " + var1);
      } else {
         return (CPoolProxy)CPoolProxy.class.cast(var1);
      }
   }

   public static CPoolEntry getPoolEntry(HttpClientConnection var0) {
      CPoolEntry var1 = getHandler(var0).getPoolEntry();
      if (var1 == null) {
         throw new ConnectionShutdownException();
      } else {
         return var1;
      }
   }

   public static CPoolEntry detach(HttpClientConnection var0) {
      return getHandler(var0).detach();
   }

   static {
      try {
         CLOSE_METHOD = HttpConnection.class.getMethod("close");
         SHUTDOWN_METHOD = HttpConnection.class.getMethod("shutdown");
         IS_OPEN_METHOD = HttpConnection.class.getMethod("isOpen");
         IS_STALE_METHOD = HttpConnection.class.getMethod("isStale");
      } catch (NoSuchMethodException var1) {
         throw new Error(var1);
      }
   }
}
