package io.netty.handler.ssl;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PemReader {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(PemReader.class);
   private static final Pattern CERT_PATTERN = Pattern.compile("-+BEGIN\\s+.*CERTIFICATE[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*CERTIFICATE[^-]*-+", 2);
   private static final Pattern KEY_PATTERN = Pattern.compile("-+BEGIN\\s+.*PRIVATE\\s+KEY[^-]*-+(?:\\s|\\r|\\n)+([a-z0-9+/=\\r\\n]+)-+END\\s+.*PRIVATE\\s+KEY[^-]*-+", 2);

   static ByteBuf[] readCertificates(File var0) throws CertificateException {
      String var1;
      try {
         var1 = readContent(var0);
      } catch (IOException var7) {
         throw new CertificateException("failed to read a file: " + var0, var7);
      }

      ArrayList var2 = new ArrayList();
      Matcher var3 = CERT_PATTERN.matcher(var1);

      for(int var4 = 0; var3.find(var4); var4 = var3.end()) {
         ByteBuf var5 = Unpooled.copiedBuffer((CharSequence)var3.group(1), CharsetUtil.US_ASCII);
         ByteBuf var6 = Base64.decode(var5);
         var5.release();
         var2.add(var6);
      }

      if (var2.isEmpty()) {
         throw new CertificateException("found no certificates: " + var0);
      } else {
         return (ByteBuf[])var2.toArray(new ByteBuf[var2.size()]);
      }
   }

   static ByteBuf readPrivateKey(File var0) throws KeyException {
      String var1;
      try {
         var1 = readContent(var0);
      } catch (IOException var5) {
         throw new KeyException("failed to read a file: " + var0, var5);
      }

      Matcher var2 = KEY_PATTERN.matcher(var1);
      if (!var2.find()) {
         throw new KeyException("found no private key: " + var0);
      } else {
         ByteBuf var3 = Unpooled.copiedBuffer((CharSequence)var2.group(1), CharsetUtil.US_ASCII);
         ByteBuf var4 = Base64.decode(var3);
         var3.release();
         return var4;
      }
   }

   private static String readContent(File var0) throws IOException {
      FileInputStream var1 = new FileInputStream(var0);
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[8192];

      while(true) {
         int var4 = var1.read(var3);
         if (var4 < 0) {
            String var6 = var2.toString(CharsetUtil.US_ASCII.name());
            safeClose((InputStream)var1);
            safeClose((OutputStream)var2);
            return var6;
         }

         var2.write(var3, 0, var4);
      }
   }

   private static void safeClose(InputStream var0) {
      try {
         var0.close();
      } catch (IOException var2) {
         logger.warn("Failed to close a stream.", (Throwable)var2);
      }

   }

   private static void safeClose(OutputStream var0) {
      try {
         var0.close();
      } catch (IOException var2) {
         logger.warn("Failed to close a stream.", (Throwable)var2);
      }

   }

   private PemReader() {
   }
}
