package com.thealtening.auth;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class SSLController {
   private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{new X509TrustManager() {
      public X509Certificate[] getAcceptedIssuers() {
         return null;
      }

      public void checkClientTrusted(X509Certificate[] var1, String var2) {
      }

      public void checkServerTrusted(X509Certificate[] var1, String var2) {
      }
   }};
   private static final HostnameVerifier ALTENING_HOSTING_VERIFIER = SSLController::lambda$0;
   private final SSLSocketFactory allTrustingFactory;
   private final SSLSocketFactory originalFactory;
   private final HostnameVerifier originalHostVerifier;

   public SSLController() {
      SSLContext var1 = null;

      try {
         var1 = SSLContext.getInstance("SSL");
         var1.init((KeyManager[])null, ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
      } catch (KeyManagementException | NoSuchAlgorithmException var3) {
         var3.printStackTrace();
      }

      this.allTrustingFactory = var1.getSocketFactory();
      this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
      this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
   }

   public void enableCertificateValidation() {
      this.updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
   }

   public void disableCertificateValidation() {
      this.updateCertificateValidation(this.allTrustingFactory, ALTENING_HOSTING_VERIFIER);
   }

   private void updateCertificateValidation(SSLSocketFactory var1, HostnameVerifier var2) {
      HttpsURLConnection.setDefaultSSLSocketFactory(var1);
      HttpsURLConnection.setDefaultHostnameVerifier(var2);
   }

   private static boolean lambda$0(String var0, SSLSession var1) {
      return var0.equals("authserver.thealtening.com") || var0.equals("sessionserver.thealtening.com");
   }
}
