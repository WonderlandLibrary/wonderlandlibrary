package io.netty.handler.ssl;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import org.apache.tomcat.jni.Pool;
import org.apache.tomcat.jni.SSL;
import org.apache.tomcat.jni.SSLContext;

public final class OpenSslServerContext extends SslContext {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslServerContext.class);
   private static final List DEFAULT_CIPHERS;
   private final long aprPool;
   private final List ciphers;
   private final List unmodifiableCiphers;
   private final long sessionCacheSize;
   private final long sessionTimeout;
   private final List nextProtocols;
   private final long ctx;
   private final OpenSslSessionStats stats;

   public OpenSslServerContext(File var1, File var2) throws SSLException {
      this(var1, var2, (String)null);
   }

   public OpenSslServerContext(File var1, File var2, String var3) throws SSLException {
      this(var1, var2, var3, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public OpenSslServerContext(File var1, File var2, String var3, Iterable var4, Iterable var5, long var6, long var8) throws SSLException {
      this.ciphers = new ArrayList();
      this.unmodifiableCiphers = Collections.unmodifiableList(this.ciphers);
      OpenSsl.ensureAvailability();
      if (var1 == null) {
         throw new NullPointerException("certChainFile");
      } else if (!var1.isFile()) {
         throw new IllegalArgumentException("certChainFile is not a file: " + var1);
      } else if (var2 == null) {
         throw new NullPointerException("keyPath");
      } else if (!var2.isFile()) {
         throw new IllegalArgumentException("keyPath is not a file: " + var2);
      } else {
         if (var4 == null) {
            var4 = DEFAULT_CIPHERS;
         }

         if (var3 == null) {
            var3 = "";
         }

         if (var5 == null) {
            var5 = Collections.emptyList();
         }

         Iterator var10 = ((Iterable)var4).iterator();

         while(var10.hasNext()) {
            String var11 = (String)var10.next();
            if (var11 == null) {
               break;
            }

            this.ciphers.add(var11);
         }

         ArrayList var23 = new ArrayList();
         Iterator var24 = ((Iterable)var5).iterator();

         while(var24.hasNext()) {
            String var12 = (String)var24.next();
            if (var12 == null) {
               break;
            }

            var23.add(var12);
         }

         this.nextProtocols = Collections.unmodifiableList(var23);
         this.aprPool = Pool.create(0L);
         boolean var25 = false;
         Class var26 = OpenSslServerContext.class;
         synchronized(OpenSslServerContext.class){}

         try {
            this.ctx = SSLContext.make(this.aprPool, 6, 1);
         } catch (Exception var18) {
            throw new SSLException("failed to create an SSL_CTX", var18);
         }

         SSLContext.setOptions(this.ctx, 4095);
         SSLContext.setOptions(this.ctx, 16777216);
         SSLContext.setOptions(this.ctx, 4194304);
         SSLContext.setOptions(this.ctx, 524288);
         SSLContext.setOptions(this.ctx, 1048576);
         SSLContext.setOptions(this.ctx, 65536);

         StringBuilder var13;
         Iterator var14;
         String var15;
         try {
            var13 = new StringBuilder();
            var14 = this.ciphers.iterator();

            while(true) {
               if (!var14.hasNext()) {
                  var13.setLength(var13.length() - 1);
                  SSLContext.setCipherSuite(this.ctx, var13.toString());
                  break;
               }

               var15 = (String)var14.next();
               var13.append(var15);
               var13.append(':');
            }
         } catch (SSLException var21) {
            throw var21;
         } catch (Exception var22) {
            throw new SSLException("failed to set cipher suite: " + this.ciphers, var22);
         }

         SSLContext.setVerify(this.ctx, 0, 10);

         try {
            if (!SSLContext.setCertificate(this.ctx, var1.getPath(), var2.getPath(), var3, 0)) {
               throw new SSLException("failed to set certificate: " + var1 + " and " + var2 + " (" + SSL.getLastError() + ')');
            }
         } catch (SSLException var19) {
            throw var19;
         } catch (Exception var20) {
            throw new SSLException("failed to set certificate: " + var1 + " and " + var2, var20);
         }

         if (!SSLContext.setCertificateChainFile(this.ctx, var1.getPath(), true)) {
            String var27 = SSL.getLastError();
            if (!var27.startsWith("error:00000000:")) {
               throw new SSLException("failed to set certificate chain: " + var1 + " (" + SSL.getLastError() + ')');
            }
         }

         if (!var23.isEmpty()) {
            var13 = new StringBuilder();
            var14 = var23.iterator();

            while(var14.hasNext()) {
               var15 = (String)var14.next();
               var13.append(var15);
               var13.append(',');
            }

            var13.setLength(var13.length() - 1);
            SSLContext.setNextProtos(this.ctx, var13.toString());
         }

         if (var6 > 0L) {
            this.sessionCacheSize = var6;
            SSLContext.setSessionCacheSize(this.ctx, var6);
         } else {
            this.sessionCacheSize = var6 = SSLContext.setSessionCacheSize(this.ctx, 20480L);
            SSLContext.setSessionCacheSize(this.ctx, var6);
         }

         if (var8 > 0L) {
            this.sessionTimeout = var8;
            SSLContext.setSessionCacheTimeout(this.ctx, var8);
         } else {
            this.sessionTimeout = var8 = SSLContext.setSessionCacheTimeout(this.ctx, 300L);
            SSLContext.setSessionCacheTimeout(this.ctx, var8);
         }

         var25 = true;
         if (!var25) {
            this.destroyPools();
         }

         this.stats = new OpenSslSessionStats(this.ctx);
      }
   }

   public boolean isClient() {
      return false;
   }

   public List cipherSuites() {
      return this.unmodifiableCiphers;
   }

   public long sessionCacheSize() {
      return this.sessionCacheSize;
   }

   public long sessionTimeout() {
      return this.sessionTimeout;
   }

   public List nextProtocols() {
      return this.nextProtocols;
   }

   public long context() {
      return this.ctx;
   }

   public OpenSslSessionStats stats() {
      return this.stats;
   }

   public SSLEngine newEngine(ByteBufAllocator var1) {
      return this.nextProtocols.isEmpty() ? new OpenSslEngine(this.ctx, var1, (String)null) : new OpenSslEngine(this.ctx, var1, (String)this.nextProtocols.get(this.nextProtocols.size() - 1));
   }

   public SSLEngine newEngine(ByteBufAllocator var1, String var2, int var3) {
      throw new UnsupportedOperationException();
   }

   public void setTicketKeys(byte[] var1) {
      if (var1 == null) {
         throw new NullPointerException("keys");
      } else {
         SSLContext.setSessionTicketKeys(this.ctx, var1);
      }
   }

   protected void finalize() throws Throwable {
      super.finalize();
      Class var1 = OpenSslServerContext.class;
      synchronized(OpenSslServerContext.class){}
      if (this.ctx != 0L) {
         SSLContext.free(this.ctx);
      }

      this.destroyPools();
   }

   private void destroyPools() {
      if (this.aprPool != 0L) {
         Pool.destroy(this.aprPool);
      }

   }

   static {
      ArrayList var0 = new ArrayList();
      Collections.addAll(var0, new String[]{"ECDHE-RSA-AES128-GCM-SHA256", "ECDHE-RSA-RC4-SHA", "ECDHE-RSA-AES128-SHA", "ECDHE-RSA-AES256-SHA", "AES128-GCM-SHA256", "RC4-SHA", "RC4-MD5", "AES128-SHA", "AES256-SHA", "DES-CBC3-SHA"});
      DEFAULT_CIPHERS = Collections.unmodifiableList(var0);
      if (logger.isDebugEnabled()) {
         logger.debug("Default cipher suite (OpenSSL): " + var0);
      }

   }
}
