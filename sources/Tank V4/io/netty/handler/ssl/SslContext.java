package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import java.io.File;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;

public abstract class SslContext {
   public static SslProvider defaultServerProvider() {
      return OpenSsl.isAvailable() ? SslProvider.OPENSSL : SslProvider.JDK;
   }

   public static SslProvider defaultClientProvider() {
      return SslProvider.JDK;
   }

   public static SslContext newServerContext(File var0, File var1) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, (String)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(File var0, File var1, String var2) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, var2, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(File var0, File var1, String var2, Iterable var3, Iterable var4, long var5, long var7) throws SSLException {
      return newServerContext((SslProvider)null, var0, var1, var2, var3, var4, var5, var7);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2) throws SSLException {
      return newServerContext(var0, var1, var2, (String)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2, String var3) throws SSLException {
      return newServerContext(var0, var1, var2, var3, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newServerContext(SslProvider var0, File var1, File var2, String var3, Iterable var4, Iterable var5, long var6, long var8) throws SSLException {
      if (var0 == null) {
         var0 = OpenSsl.isAvailable() ? SslProvider.OPENSSL : SslProvider.JDK;
      }

      switch(var0) {
      case JDK:
         return new JdkSslServerContext(var1, var2, var3, var4, var5, var6, var8);
      case OPENSSL:
         return new OpenSslServerContext(var1, var2, var3, var4, var5, var6, var8);
      default:
         throw new Error(var0.toString());
      }
   }

   public static SslContext newClientContext() throws SSLException {
      return newClientContext((SslProvider)null, (File)null, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0) throws SSLException {
      return newClientContext((SslProvider)null, var0, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(TrustManagerFactory var0) throws SSLException {
      return newClientContext((SslProvider)null, (File)null, var0, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0, TrustManagerFactory var1) throws SSLException {
      return newClientContext((SslProvider)null, var0, var1, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(File var0, TrustManagerFactory var1, Iterable var2, Iterable var3, long var4, long var6) throws SSLException {
      return newClientContext((SslProvider)null, var0, var1, var2, var3, var4, var6);
   }

   public static SslContext newClientContext(SslProvider var0) throws SSLException {
      return newClientContext(var0, (File)null, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1) throws SSLException {
      return newClientContext(var0, var1, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, TrustManagerFactory var1) throws SSLException {
      return newClientContext(var0, (File)null, var1, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1, TrustManagerFactory var2) throws SSLException {
      return newClientContext(var0, var1, var2, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public static SslContext newClientContext(SslProvider var0, File var1, TrustManagerFactory var2, Iterable var3, Iterable var4, long var5, long var7) throws SSLException {
      if (var0 != null && var0 != SslProvider.JDK) {
         throw new SSLException("client context unsupported for: " + var0);
      } else {
         return new JdkSslClientContext(var1, var2, var3, var4, var5, var7);
      }
   }

   SslContext() {
   }

   public final boolean isServer() {
      return !this.isClient();
   }

   public abstract boolean isClient();

   public abstract List cipherSuites();

   public abstract long sessionCacheSize();

   public abstract long sessionTimeout();

   public abstract List nextProtocols();

   public abstract SSLEngine newEngine(ByteBufAllocator var1);

   public abstract SSLEngine newEngine(ByteBufAllocator var1, String var2, int var3);

   public final SslHandler newHandler(ByteBufAllocator var1) {
      return newHandler(this.newEngine(var1));
   }

   public final SslHandler newHandler(ByteBufAllocator var1, String var2, int var3) {
      return newHandler(this.newEngine(var1, var2, var3));
   }

   private static SslHandler newHandler(SSLEngine var0) {
      return new SslHandler(var0);
   }
}
