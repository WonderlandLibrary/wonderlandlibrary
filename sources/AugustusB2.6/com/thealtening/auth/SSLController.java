// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.auth;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSession;
import java.security.GeneralSecurityException;
import javax.net.ssl.HttpsURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.KeyManager;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.TrustManager;

public final class SSLController
{
    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER;
    private static final HostnameVerifier ALTENING_HOSTING_VERIFIER;
    private final SSLSocketFactory allTrustingFactory;
    private final SSLSocketFactory originalFactory;
    private final HostnameVerifier originalHostVerifier;
    
    public SSLController() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, SSLController.ALL_TRUSTING_TRUST_MANAGER, new SecureRandom());
        }
        catch (NoSuchAlgorithmException | KeyManagementException ex2) {
            final GeneralSecurityException ex;
            final GeneralSecurityException e = ex;
            e.printStackTrace();
        }
        this.allTrustingFactory = sc.getSocketFactory();
        this.originalFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
        this.originalHostVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    }
    
    public void enableCertificateValidation() {
        this.updateCertificateValidation(this.originalFactory, this.originalHostVerifier);
    }
    
    public void disableCertificateValidation() {
        this.updateCertificateValidation(this.allTrustingFactory, SSLController.ALTENING_HOSTING_VERIFIER);
    }
    
    private void updateCertificateValidation(final SSLSocketFactory factory, final HostnameVerifier hostnameVerifier) {
        HttpsURLConnection.setDefaultSSLSocketFactory(factory);
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
    }
    
    static {
        ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                
                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                }
                
                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                }
            } };
        ALTENING_HOSTING_VERIFIER = ((hostname, session) -> hostname.equals("authserver.thealtening.com") || hostname.equals("sessionserver.thealtening.com"));
    }
}
