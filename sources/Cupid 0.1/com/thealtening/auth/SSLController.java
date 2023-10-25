package com.thealtening.auth;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class SSLController {
  public SSLController() {
    SSLContext sc = null;
    try {
      sc = SSLContext.getInstance("SSL");
      sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
    } catch (NoSuchAlgorithmException|java.security.KeyManagementException e) {
      e.printStackTrace();
    } 
    this.allTrustingFactory = sc.getSocketFactory();
    this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
    this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
  }
  
  public void enableCertificateValidation() {
    updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
  }
  
  public void disableCertificateValidation() {
    updateCertificateValidation(this.allTrustingFactory, ALTENING_HOSTING_VERIFIER);
  }
  
  private void updateCertificateValidation(SSLSocketFactory factory, HostnameVerifier hostnameVerifier) {
    HttpsURLConnection.setDefaultSSLSocketFactory(factory);
    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
  }
  
  private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
        
        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
        
        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
      } };
  
  private static final HostnameVerifier ALTENING_HOSTING_VERIFIER;
  
  private final SSLSocketFactory allTrustingFactory;
  
  private final SSLSocketFactory originalFactory;
  
  private final HostnameVerifier originalHostVerifier;
  
  static {
    ALTENING_HOSTING_VERIFIER = ((hostname, session) -> 
      (hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com")));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\thealtening\auth\SSLController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */