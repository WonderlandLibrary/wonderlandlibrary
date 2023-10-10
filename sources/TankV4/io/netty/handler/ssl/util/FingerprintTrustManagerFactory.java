package io.netty.handler.ssl.util;

import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.util.internal.EmptyArrays;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Pattern;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class FingerprintTrustManagerFactory extends SimpleTrustManagerFactory {
   private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
   private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
   private static final int SHA1_BYTE_LEN = 20;
   private static final int SHA1_HEX_LEN = 40;
   private static final FastThreadLocal tlmd = new FastThreadLocal() {
      protected MessageDigest initialValue() {
         try {
            return MessageDigest.getInstance("SHA1");
         } catch (NoSuchAlgorithmException var2) {
            throw new Error(var2);
         }
      }

      protected Object initialValue() throws Exception {
         return this.initialValue();
      }
   };
   private final TrustManager tm;
   private final byte[][] fingerprints;

   public FingerprintTrustManagerFactory(Iterable var1) {
      this(toFingerprintArray(var1));
   }

   public FingerprintTrustManagerFactory(String... var1) {
      this(toFingerprintArray(Arrays.asList(var1)));
   }

   public FingerprintTrustManagerFactory(byte[]... var1) {
      this.tm = new X509TrustManager(this) {
         final FingerprintTrustManagerFactory this$0;

         {
            this.this$0 = var1;
         }

         public void checkClientTrusted(X509Certificate[] var1, String var2) throws CertificateException {
            this.checkTrusted("client", var1);
         }

         public void checkServerTrusted(X509Certificate[] var1, String var2) throws CertificateException {
            this.checkTrusted("server", var1);
         }

         private void checkTrusted(String var1, X509Certificate[] var2) throws CertificateException {
            X509Certificate var3 = var2[0];
            byte[] var4 = this.fingerprint(var3);
            boolean var5 = false;
            byte[][] var6 = FingerprintTrustManagerFactory.access$000(this.this$0);
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               byte[] var9 = var6[var8];
               if (Arrays.equals(var4, var9)) {
                  var5 = true;
                  break;
               }
            }

            if (!var5) {
               throw new CertificateException(var1 + " certificate with unknown fingerprint: " + var3.getSubjectDN());
            }
         }

         private byte[] fingerprint(X509Certificate var1) throws CertificateEncodingException {
            MessageDigest var2 = (MessageDigest)FingerprintTrustManagerFactory.access$100().get();
            var2.reset();
            return var2.digest(var1.getEncoded());
         }

         public X509Certificate[] getAcceptedIssuers() {
            return EmptyArrays.EMPTY_X509_CERTIFICATES;
         }
      };
      if (var1 == null) {
         throw new NullPointerException("fingerprints");
      } else {
         ArrayList var2 = new ArrayList();
         byte[][] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte[] var6 = var3[var5];
            if (var6 == null) {
               break;
            }

            if (var6.length != 20) {
               throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(var6)) + " (expected: SHA1)");
            }

            var2.add(var6.clone());
         }

         this.fingerprints = (byte[][])var2.toArray(new byte[var2.size()][]);
      }
   }

   private static byte[][] toFingerprintArray(Iterable var0) {
      if (var0 == null) {
         throw new NullPointerException("fingerprints");
      } else {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            if (var3 == null) {
               break;
            }

            if (!FINGERPRINT_PATTERN.matcher(var3).matches()) {
               throw new IllegalArgumentException("malformed fingerprint: " + var3);
            }

            var3 = FINGERPRINT_STRIP_PATTERN.matcher(var3).replaceAll("");
            if (var3.length() != 40) {
               throw new IllegalArgumentException("malformed fingerprint: " + var3 + " (expected: SHA1)");
            }

            byte[] var4 = new byte[20];

            for(int var5 = 0; var5 < var4.length; ++var5) {
               int var6 = var5 << 1;
               var4[var5] = (byte)Integer.parseInt(var3.substring(var6, var6 + 2), 16);
            }
         }

         return (byte[][])var1.toArray(new byte[var1.size()][]);
      }
   }

   protected void engineInit(KeyStore var1) throws Exception {
   }

   protected void engineInit(ManagerFactoryParameters var1) throws Exception {
   }

   protected TrustManager[] engineGetTrustManagers() {
      return new TrustManager[]{this.tm};
   }

   static byte[][] access$000(FingerprintTrustManagerFactory var0) {
      return var0.fingerprints;
   }

   static FastThreadLocal access$100() {
      return tlmd;
   }
}
