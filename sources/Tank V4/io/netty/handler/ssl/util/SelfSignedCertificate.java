package io.netty.handler.ssl.util;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public final class SelfSignedCertificate {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
   static final Date NOT_BEFORE = new Date(System.currentTimeMillis() - 31536000000L);
   static final Date NOT_AFTER = new Date(253402300799000L);
   private final File certificate;
   private final File privateKey;

   public SelfSignedCertificate() throws CertificateException {
      this("example.com");
   }

   public SelfSignedCertificate(String var1) throws CertificateException {
      this(var1, ThreadLocalInsecureRandom.current(), 1024);
   }

   public SelfSignedCertificate(String var1, SecureRandom var2, int var3) throws CertificateException {
      KeyPair var4;
      try {
         KeyPairGenerator var5 = KeyPairGenerator.getInstance("RSA");
         var5.initialize(var3, var2);
         var4 = var5.generateKeyPair();
      } catch (NoSuchAlgorithmException var10) {
         throw new Error(var10);
      }

      String[] var11;
      try {
         var11 = OpenJdkSelfSignedCertGenerator.generate(var1, var4, var2);
      } catch (Throwable var9) {
         logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", var9);

         try {
            var11 = BouncyCastleSelfSignedCertGenerator.generate(var1, var4, var2);
         } catch (Throwable var8) {
            logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", var8);
            throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.");
         }
      }

      this.certificate = new File(var11[0]);
      this.privateKey = new File(var11[1]);
   }

   public File certificate() {
      return this.certificate;
   }

   public File privateKey() {
      return this.privateKey;
   }

   public void delete() {
      safeDelete(this.certificate);
      safeDelete(this.privateKey);
   }

   static String[] newSelfSignedCertificate(String var0, PrivateKey var1, X509Certificate var2) throws IOException, CertificateEncodingException {
      String var3 = "-----BEGIN PRIVATE KEY-----\n" + Base64.encode(Unpooled.wrappedBuffer(var1.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
      File var4 = File.createTempFile("keyutil_" + var0 + '_', ".key");
      var4.deleteOnExit();
      FileOutputStream var5 = new FileOutputStream(var4);
      var5.write(var3.getBytes(CharsetUtil.US_ASCII));
      var5.close();
      var5 = null;
      if (var5 != null) {
         safeClose(var4, var5);
         safeDelete(var4);
      }

      String var6 = "-----BEGIN CERTIFICATE-----\n" + Base64.encode(Unpooled.wrappedBuffer(var2.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
      File var7 = File.createTempFile("keyutil_" + var0 + '_', ".crt");
      var7.deleteOnExit();
      FileOutputStream var8 = new FileOutputStream(var7);
      var8.write(var6.getBytes(CharsetUtil.US_ASCII));
      var8.close();
      var8 = null;
      if (var8 != null) {
         safeClose(var7, var8);
         safeDelete(var7);
         safeDelete(var4);
      }

      return new String[]{var7.getPath(), var4.getPath()};
   }

   private static void safeDelete(File var0) {
      if (!var0.delete()) {
         logger.warn("Failed to delete a file: " + var0);
      }

   }

   private static void safeClose(File var0, OutputStream var1) {
      try {
         var1.close();
      } catch (IOException var3) {
         logger.warn("Failed to close a file: " + var0, (Throwable)var3);
      }

   }
}
