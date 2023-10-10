package io.netty.handler.ssl.util;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class InsecureTrustManagerFactory extends SimpleTrustManagerFactory {
   private static final InternalLogger logger = InternalLoggerFactory.getInstance(InsecureTrustManagerFactory.class);
   public static final TrustManagerFactory INSTANCE = new InsecureTrustManagerFactory();
   private static final TrustManager tm = new X509TrustManager() {
      public void checkClientTrusted(X509Certificate[] var1, String var2) {
         InsecureTrustManagerFactory.access$000().debug("Accepting a client certificate: " + var1[0].getSubjectDN());
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) {
         InsecureTrustManagerFactory.access$000().debug("Accepting a server certificate: " + var1[0].getSubjectDN());
      }

      public X509Certificate[] getAcceptedIssuers() {
         return EmptyArrays.EMPTY_X509_CERTIFICATES;
      }
   };

   private InsecureTrustManagerFactory() {
   }

   protected void engineInit(KeyStore var1) throws Exception {
   }

   protected void engineInit(ManagerFactoryParameters var1) throws Exception {
   }

   protected TrustManager[] engineGetTrustManagers() {
      return new TrustManager[]{tm};
   }

   static InternalLogger access$000() {
      return logger;
   }
}
