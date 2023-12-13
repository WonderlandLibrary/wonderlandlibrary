package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.security.auth.x500.X500Principal;

public final class JdkSslClientContext extends JdkSslContext {
   private final SSLContext ctx;
   private final List nextProtocols;

   public JdkSslClientContext() throws SSLException {
      this((File)null, (TrustManagerFactory)null, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public JdkSslClientContext(File var1) throws SSLException {
      this(var1, (TrustManagerFactory)null);
   }

   public JdkSslClientContext(TrustManagerFactory var1) throws SSLException {
      this((File)null, var1);
   }

   public JdkSslClientContext(File var1, TrustManagerFactory var2) throws SSLException {
      this(var1, var2, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public JdkSslClientContext(File var1, TrustManagerFactory var2, Iterable var3, Iterable var4, long var5, long var7) throws SSLException {
      super(var3);
      if (var4 != null && var4.iterator().hasNext()) {
         if (!JettyNpnSslEngine.isAvailable()) {
            throw new SSLException("NPN/ALPN unsupported: " + var4);
         }

         ArrayList var9 = new ArrayList();
         Iterator var10 = var4.iterator();

         while(var10.hasNext()) {
            String var11 = (String)var10.next();
            if (var11 == null) {
               break;
            }

            var9.add(var11);
         }

         this.nextProtocols = Collections.unmodifiableList(var9);
      } else {
         this.nextProtocols = Collections.emptyList();
      }

      try {
         if (var1 == null) {
            this.ctx = SSLContext.getInstance("TLS");
            if (var2 == null) {
               this.ctx.init((KeyManager[])null, (TrustManager[])null, (SecureRandom)null);
            } else {
               var2.init((KeyStore)null);
               this.ctx.init((KeyManager[])null, var2.getTrustManagers(), (SecureRandom)null);
            }
         } else {
            KeyStore var24 = KeyStore.getInstance("JKS");
            var24.load((InputStream)null, (char[])null);
            CertificateFactory var26 = CertificateFactory.getInstance("X.509");
            ByteBuf[] var27 = PemReader.readCertificates(var1);
            ByteBuf[] var12 = var27;
            int var13 = var27.length;

            int var14;
            ByteBuf var15;
            for(var14 = 0; var14 < var13; ++var14) {
               var15 = var12[var14];
               X509Certificate var16 = (X509Certificate)var26.generateCertificate(new ByteBufInputStream(var15));
               X500Principal var17 = var16.getSubjectX500Principal();
               var24.setCertificateEntry(var17.getName("RFC2253"), var16);
            }

            var12 = var27;
            var13 = var27.length;

            for(var14 = 0; var14 < var13; ++var14) {
               var15 = var12[var14];
               var15.release();
            }

            if (var2 == null) {
               var2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            }

            var2.init(var24);
            this.ctx = SSLContext.getInstance("TLS");
            this.ctx.init((KeyManager[])null, var2.getTrustManagers(), (SecureRandom)null);
         }

         SSLSessionContext var25 = this.ctx.getClientSessionContext();
         if (var5 > 0L) {
            var25.setSessionCacheSize((int)Math.min(var5, 2147483647L));
         }

         if (var7 > 0L) {
            var25.setSessionTimeout((int)Math.min(var7, 2147483647L));
         }

      } catch (Exception var23) {
         throw new SSLException("failed to initialize the server-side SSL context", var23);
      }
   }

   public boolean isClient() {
      return true;
   }

   public List nextProtocols() {
      return this.nextProtocols;
   }

   public SSLContext context() {
      return this.ctx;
   }
}
