/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.net.ssl.KeyStoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.KeyStoreConfigurationException;
import org.apache.logging.log4j.core.net.ssl.StoreConfigurationException;
import org.apache.logging.log4j.core.net.ssl.TrustStoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.TrustStoreConfigurationException;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name="ssl", category="Core", printObject=true)
public class SSLConfiguration {
    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private KeyStoreConfiguration keyStoreConfig;
    private TrustStoreConfiguration trustStoreConfig;
    private SSLContext sslContext;

    private SSLConfiguration(KeyStoreConfiguration keyStoreConfig, TrustStoreConfiguration trustStoreConfig) {
        this.keyStoreConfig = keyStoreConfig;
        this.trustStoreConfig = trustStoreConfig;
        this.sslContext = null;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }
        return this.sslContext.getSocketFactory();
    }

    public SSLServerSocketFactory getSSLServerSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }
        return this.sslContext.getServerSocketFactory();
    }

    private SSLContext createSSLContext() {
        SSLContext context = null;
        try {
            context = this.createSSLContextBasedOnConfiguration();
            LOGGER.debug("Creating SSLContext with the given parameters");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createSSLContextWithTrustStoreFailure();
        }
        catch (KeyStoreConfigurationException e) {
            context = this.createSSLContextWithKeyStoreFailure();
        }
        return context;
    }

    private SSLContext createSSLContextWithTrustStoreFailure() {
        SSLContext context;
        try {
            context = this.createSSLContextWithDefaultTrustManagerFactory();
            LOGGER.debug("Creating SSLContext with default truststore");
        }
        catch (KeyStoreConfigurationException e) {
            context = this.createDefaultSSLContext();
            LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }

    private SSLContext createSSLContextWithKeyStoreFailure() {
        SSLContext context;
        try {
            context = this.createSSLContextWithDefaultKeyManagerFactory();
            LOGGER.debug("Creating SSLContext with default keystore");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createDefaultSSLContext();
            LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }

    private SSLContext createSSLContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        return this.createSSLContext(false, false);
    }

    private SSLContext createSSLContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
        try {
            return this.createSSLContext(true, false);
        }
        catch (KeyStoreConfigurationException dummy) {
            LOGGER.debug("Exception occured while using default keystore. This should be a BUG");
            return null;
        }
    }

    private SSLContext createSSLContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
        try {
            return this.createSSLContext(false, true);
        }
        catch (TrustStoreConfigurationException dummy) {
            LOGGER.debug("Exception occured while using default truststore. This should be a BUG");
            return null;
        }
    }

    private SSLContext createDefaultSSLContext() {
        try {
            return SSLContext.getDefault();
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("Failed to create an SSLContext with default configuration");
            return null;
        }
    }

    private SSLContext createSSLContext(boolean loadDefaultKeyManagerFactory, boolean loadDefaultTrustManagerFactory) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        try {
            KeyManager[] kManagers = null;
            TrustManager[] tManagers = null;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            if (!loadDefaultKeyManagerFactory) {
                KeyManagerFactory kmFactory = this.loadKeyManagerFactory();
                kManagers = kmFactory.getKeyManagers();
            }
            if (!loadDefaultTrustManagerFactory) {
                TrustManagerFactory tmFactory = this.loadTrustManagerFactory();
                tManagers = tmFactory.getTrustManagers();
            }
            sslContext.init(kManagers, tManagers, null);
            return sslContext;
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol");
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyManagementException e) {
            LOGGER.error("Failed to initialize the SSLContext");
            throw new KeyStoreConfigurationException(e);
        }
    }

    private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
        KeyStore trustStore = null;
        TrustManagerFactory tmFactory = null;
        if (this.trustStoreConfig == null) {
            throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
        }
        try {
            trustStore = this.trustStoreConfig.getTrustStore();
            tmFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmFactory.init(trustStore);
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyStoreException e) {
            LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new TrustStoreConfigurationException(e);
        }
        catch (StoreConfigurationException e) {
            throw new TrustStoreConfigurationException(e);
        }
        return tmFactory;
    }

    private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
        KeyStore keyStore = null;
        KeyManagerFactory kmFactory = null;
        if (this.keyStoreConfig == null) {
            throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
        }
        try {
            keyStore = this.keyStoreConfig.getKeyStore();
            kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmFactory.init(keyStore, this.keyStoreConfig.getPasswordAsCharArray());
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new KeyStoreConfigurationException(e);
        }
        catch (KeyStoreException e) {
            LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new KeyStoreConfigurationException(e);
        }
        catch (StoreConfigurationException e) {
            throw new KeyStoreConfigurationException(e);
        }
        catch (UnrecoverableKeyException e) {
            LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)");
            throw new KeyStoreConfigurationException(e);
        }
        return kmFactory;
    }

    public boolean equals(SSLConfiguration config) {
        if (config == null) {
            return false;
        }
        boolean keyStoreEquals = false;
        boolean trustStoreEquals = false;
        if (this.keyStoreConfig != null) {
            keyStoreEquals = this.keyStoreConfig.equals(config.keyStoreConfig);
        } else {
            boolean bl = keyStoreEquals = this.keyStoreConfig == config.keyStoreConfig;
        }
        trustStoreEquals = this.trustStoreConfig != null ? this.trustStoreConfig.equals(config.trustStoreConfig) : this.trustStoreConfig == config.trustStoreConfig;
        return keyStoreEquals && trustStoreEquals;
    }

    @PluginFactory
    public static SSLConfiguration createSSLConfiguration(@PluginElement(value="keyStore") KeyStoreConfiguration keyStoreConfig, @PluginElement(value="trustStore") TrustStoreConfiguration trustStoreConfig) {
        return new SSLConfiguration(keyStoreConfig, trustStoreConfig);
    }
}

