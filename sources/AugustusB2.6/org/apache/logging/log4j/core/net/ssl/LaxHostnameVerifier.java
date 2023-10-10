// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import javax.net.ssl.SSLSession;
import javax.net.ssl.HostnameVerifier;

public final class LaxHostnameVerifier implements HostnameVerifier
{
    public static final HostnameVerifier INSTANCE;
    
    private LaxHostnameVerifier() {
    }
    
    @Override
    public boolean verify(final String s, final SSLSession sslSession) {
        return true;
    }
    
    static {
        INSTANCE = new LaxHostnameVerifier();
    }
}
