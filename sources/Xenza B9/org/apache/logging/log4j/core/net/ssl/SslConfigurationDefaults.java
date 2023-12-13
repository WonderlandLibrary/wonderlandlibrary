// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyStore;

public class SslConfigurationDefaults
{
    public static final String KEYSTORE_TYPE;
    public static final String PROTOCOL = "TLS";
    
    static {
        KEYSTORE_TYPE = KeyStore.getDefaultType();
    }
}
