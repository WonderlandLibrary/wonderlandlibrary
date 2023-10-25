/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl.util;

import io.netty.handler.ssl.util.SimpleTrustManagerFactory;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class InsecureTrustManagerFactory
extends SimpleTrustManagerFactory {
    public static final TrustManagerFactory INSTANCE;
    private static final InternalLogger logger;
    private static final TrustManager tm;

    @Override
    protected TrustManager[] engineGetTrustManagers() {
        return new TrustManager[]{tm};
    }

    private InsecureTrustManagerFactory() {
    }

    @Override
    protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception {
    }

    @Override
    protected void engineInit(KeyStore keyStore) throws Exception {
    }

    static {
        logger = InternalLoggerFactory.getInstance(InsecureTrustManagerFactory.class);
        INSTANCE = new InsecureTrustManagerFactory();
        tm = new X509TrustManager(){

            @Override
            public void checkClientTrusted(X509Certificate[] x509CertificateArray, String string) {
                logger.debug("Accepting a client certificate: " + x509CertificateArray[0].getSubjectDN());
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return EmptyArrays.EMPTY_X509_CERTIFICATES;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509CertificateArray, String string) {
                logger.debug("Accepting a server certificate: " + x509CertificateArray[0].getSubjectDN());
            }
        };
    }
}

