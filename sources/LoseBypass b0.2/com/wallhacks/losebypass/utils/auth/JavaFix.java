/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.utils.auth;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class JavaFix {
    private static SSLContext context;

    public static SSLContext getFixedContext() {
        try {
            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            try (InputStream in = JavaFix.class.getResourceAsStream("/iasjavafix.jks");){
                ks.load(in, "iasjavafix".toCharArray());
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), new SecureRandom());
            return context;
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
        return context;
    }
}

