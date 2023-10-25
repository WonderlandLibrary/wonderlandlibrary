/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public final class LaxHostnameVerifier
implements HostnameVerifier {
    public static final HostnameVerifier INSTANCE = new LaxHostnameVerifier();

    private LaxHostnameVerifier() {
    }

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}

