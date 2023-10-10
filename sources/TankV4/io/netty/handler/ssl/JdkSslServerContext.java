package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.TrustManager;

public final class JdkSslServerContext extends JdkSslContext {
   private final SSLContext ctx;
   private final List nextProtocols;

   public JdkSslServerContext(File var1, File var2) throws SSLException {
      this(var1, var2, (String)null);
   }

   public JdkSslServerContext(File var1, File var2, String var3) throws SSLException {
      this(var1, var2, var3, (Iterable)null, (Iterable)null, 0L, 0L);
   }

   public JdkSslServerContext(File var1, File var2, String var3, Iterable var4, Iterable var5, long var6, long var8) throws SSLException {
      super(var4);
      if (var1 == null) {
         throw new NullPointerException("certChainFile");
      } else if (var2 == null) {
         throw new NullPointerException("keyFile");
      } else {
         if (var3 == null) {
            var3 = "";
         }

         if (var5 != null && var5.iterator().hasNext()) {
            if (!JettyNpnSslEngine.isAvailable()) {
               throw new SSLException("NPN/ALPN unsupported: " + var5);
            }

            ArrayList var10 = new ArrayList();
            Iterator var11 = var5.iterator();

            while(var11.hasNext()) {
               String var12 = (String)var11.next();
               if (var12 == null) {
                  break;
               }

               var10.add(var12);
            }

            this.nextProtocols = Collections.unmodifiableList(var10);
         } else {
            this.nextProtocols = Collections.emptyList();
         }

         String var35 = Security.getProperty("ssl.KeyManagerFactory.algorithm");
         if (var35 == null) {
            var35 = "SunX509";
         }

         try {
            KeyStore var36 = KeyStore.getInstance("JKS");
            var36.load((InputStream)null, (char[])null);
            CertificateFactory var37 = CertificateFactory.getInstance("X.509");
            KeyFactory var13 = KeyFactory.getInstance("RSA");
            KeyFactory var14 = KeyFactory.getInstance("DSA");
            ByteBuf var15 = PemReader.readPrivateKey(var2);
            byte[] var16 = new byte[var15.readableBytes()];
            var15.readBytes(var16).release();
            char[] var17 = var3.toCharArray();
            PKCS8EncodedKeySpec var18 = generateKeySpec(var17, var16);

            PrivateKey var19;
            try {
               var19 = var13.generatePrivate(var18);
            } catch (InvalidKeySpecException var31) {
               var19 = var14.generatePrivate(var18);
            }

            ArrayList var20 = new ArrayList();
            ByteBuf[] var21 = PemReader.readCertificates(var1);
            ByteBuf[] var22 = var21;
            int var23 = var21.length;

            int var24;
            ByteBuf var25;
            for(var24 = 0; var24 < var23; ++var24) {
               var25 = var22[var24];
               var20.add(var37.generateCertificate(new ByteBufInputStream(var25)));
            }

            var22 = var21;
            var23 = var21.length;

            for(var24 = 0; var24 < var23; ++var24) {
               var25 = var22[var24];
               var25.release();
            }

            var36.setKeyEntry("key", var19, var17, (Certificate[])var20.toArray(new Certificate[var20.size()]));
            KeyManagerFactory var33 = KeyManagerFactory.getInstance(var35);
            var33.init(var36, var17);
            this.ctx = SSLContext.getInstance("TLS");
            this.ctx.init(var33.getKeyManagers(), (TrustManager[])null, (SecureRandom)null);
            SSLSessionContext var34 = this.ctx.getServerSessionContext();
            if (var6 > 0L) {
               var34.setSessionCacheSize((int)Math.min(var6, 2147483647L));
            }

            if (var8 > 0L) {
               var34.setSessionTimeout((int)Math.min(var8, 2147483647L));
            }

         } catch (Exception var32) {
            throw new SSLException("failed to initialize the server-side SSL context", var32);
         }
      }
   }

   public boolean isClient() {
      return false;
   }

   public List nextProtocols() {
      return this.nextProtocols;
   }

   public SSLContext context() {
      return this.ctx;
   }

   private static PKCS8EncodedKeySpec generateKeySpec(char[] var0, byte[] var1) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, InvalidAlgorithmParameterException {
      if (var0 != null && var0.length != 0) {
         EncryptedPrivateKeyInfo var2 = new EncryptedPrivateKeyInfo(var1);
         SecretKeyFactory var3 = SecretKeyFactory.getInstance(var2.getAlgName());
         PBEKeySpec var4 = new PBEKeySpec(var0);
         SecretKey var5 = var3.generateSecret(var4);
         Cipher var6 = Cipher.getInstance(var2.getAlgName());
         var6.init(2, var5, var2.getAlgParameters());
         return var2.getKeySpec(var6);
      } else {
         return new PKCS8EncodedKeySpec(var1);
      }
   }
}
