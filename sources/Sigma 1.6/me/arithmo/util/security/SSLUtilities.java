/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util.security;

import com.sun.net.ssl.HostnameVerifier;
import com.sun.net.ssl.HttpsURLConnection;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public final class SSLUtilities {
    private static HostnameVerifier __hostnameVerifier;
    private static com.sun.net.ssl.TrustManager[] __trustManagers;
    private static javax.net.ssl.HostnameVerifier _hostnameVerifier;
    private static TrustManager[] _trustManagers;

    private static void __trustAllHostnames() {
        if (__hostnameVerifier == null) {
            __hostnameVerifier = new _FakeHostnameVerifier();
        }
        HttpsURLConnection.setDefaultHostnameVerifier(__hostnameVerifier);
    }

    private static void __trustAllHttpsCertificates() {
        com.sun.net.ssl.SSLContext context;
        if (__trustManagers == null) {
            __trustManagers = new com.sun.net.ssl.TrustManager[]{new _FakeX509TrustManager()};
        }
        try {
            context = com.sun.net.ssl.SSLContext.getInstance("SSL");
            context.init(null, __trustManagers, new SecureRandom());
        }
        catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    private static boolean isDeprecatedSSLProtocol() {
        return "com.sun.net.ssl.internal.www.protocol".equals(System.getProperty("java.protocol.handler.pkgs"));
    }

    private static void _trustAllHostnames() {
        if (_hostnameVerifier == null) {
            _hostnameVerifier = new FakeHostnameVerifier();
        }
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(_hostnameVerifier);
    }

    private static void _trustAllHttpsCertificates() {
        SSLContext context;
        if (_trustManagers == null) {
            _trustManagers = new TrustManager[]{new FakeX509TrustManager()};
        }
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, _trustManagers, new SecureRandom());
        }
        catch (GeneralSecurityException gse) {
            throw new IllegalStateException(gse.getMessage());
        }
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    }

    public static void trustAllHostnames() {
        if (SSLUtilities.isDeprecatedSSLProtocol()) {
            SSLUtilities.__trustAllHostnames();
        } else {
            SSLUtilities._trustAllHostnames();
        }
    }

    public static void trustAllHttpsCertificates() {
        if (SSLUtilities.isDeprecatedSSLProtocol()) {
            SSLUtilities.__trustAllHttpsCertificates();
        } else {
            SSLUtilities._trustAllHttpsCertificates();
        }
    }

    public static class FakeX509TrustManager
    implements X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[0];

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }
    }

    public static class FakeHostnameVerifier
    implements javax.net.ssl.HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static class _FakeX509TrustManager
    implements com.sun.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[0];

        @Override
        public boolean isClientTrusted(X509Certificate[] chain) {
            return true;
        }

        @Override
        public boolean isServerTrusted(X509Certificate[] chain) {
            return true;
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }
    }

    public static class _FakeHostnameVerifier
    implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, String session) {
            return true;
        }
    }

}

