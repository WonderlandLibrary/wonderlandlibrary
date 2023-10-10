// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.security.UnrecoverableKeyException;
import java.security.KeyStoreException;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.KeyManager;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLContext;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Ssl", category = "Core", printObject = true)
public class SslConfiguration
{
    private static final StatusLogger LOGGER;
    private final KeyStoreConfiguration keyStoreConfig;
    private final TrustStoreConfiguration trustStoreConfig;
    private final SSLContext sslContext;
    private final String protocol;
    private final boolean verifyHostName;
    
    private SslConfiguration(final String protocol, final KeyStoreConfiguration keyStoreConfig, final TrustStoreConfiguration trustStoreConfig, final boolean verifyHostName) {
        this.keyStoreConfig = keyStoreConfig;
        this.trustStoreConfig = trustStoreConfig;
        this.protocol = ((protocol == null) ? "SSL" : protocol);
        this.sslContext = this.createSslContext();
        this.verifyHostName = verifyHostName;
    }
    
    public void clearSecrets() {
        if (this.keyStoreConfig != null) {
            this.keyStoreConfig.clearSecrets();
        }
        if (this.trustStoreConfig != null) {
            this.trustStoreConfig.clearSecrets();
        }
    }
    
    public SSLSocketFactory getSslSocketFactory() {
        return this.sslContext.getSocketFactory();
    }
    
    public SSLServerSocketFactory getSslServerSocketFactory() {
        return this.sslContext.getServerSocketFactory();
    }
    
    private SSLContext createSslContext() {
        SSLContext context = null;
        try {
            context = this.createSslContextBasedOnConfiguration();
            SslConfiguration.LOGGER.debug("Creating SSLContext with the given parameters");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createSslContextWithTrustStoreFailure();
        }
        catch (KeyStoreConfigurationException e2) {
            context = this.createSslContextWithKeyStoreFailure();
        }
        return context;
    }
    
    private SSLContext createSslContextWithTrustStoreFailure() {
        SSLContext context;
        try {
            context = this.createSslContextWithDefaultTrustManagerFactory();
            SslConfiguration.LOGGER.debug("Creating SSLContext with default truststore");
        }
        catch (KeyStoreConfigurationException e) {
            context = this.createDefaultSslContext();
            SslConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }
    
    private SSLContext createSslContextWithKeyStoreFailure() {
        SSLContext context;
        try {
            context = this.createSslContextWithDefaultKeyManagerFactory();
            SslConfiguration.LOGGER.debug("Creating SSLContext with default keystore");
        }
        catch (TrustStoreConfigurationException e) {
            context = this.createDefaultSslContext();
            SslConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }
        return context;
    }
    
    private SSLContext createSslContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        return this.createSslContext(false, false);
    }
    
    private SSLContext createSslContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
        try {
            return this.createSslContext(true, false);
        }
        catch (KeyStoreConfigurationException dummy) {
            SslConfiguration.LOGGER.debug("Exception occurred while using default keystore. This should be a BUG");
            return null;
        }
    }
    
    private SSLContext createSslContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
        try {
            return this.createSslContext(false, true);
        }
        catch (TrustStoreConfigurationException dummy) {
            SslConfiguration.LOGGER.debug("Exception occurred while using default truststore. This should be a BUG");
            return null;
        }
    }
    
    private SSLContext createDefaultSslContext() {
        try {
            return SSLContext.getDefault();
        }
        catch (NoSuchAlgorithmException e) {
            SslConfiguration.LOGGER.error("Failed to create an SSLContext with default configuration", e);
            return null;
        }
    }
    
    private SSLContext createSslContext(final boolean loadDefaultKeyManagerFactory, final boolean loadDefaultTrustManagerFactory) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        try {
            KeyManager[] kManagers = null;
            TrustManager[] tManagers = null;
            final SSLContext newSslContext = SSLContext.getInstance(this.protocol);
            if (!loadDefaultKeyManagerFactory) {
                final KeyManagerFactory kmFactory = this.loadKeyManagerFactory();
                kManagers = kmFactory.getKeyManagers();
            }
            if (!loadDefaultTrustManagerFactory) {
                final TrustManagerFactory tmFactory = this.loadTrustManagerFactory();
                tManagers = tmFactory.getTrustManagers();
            }
            newSslContext.init(kManagers, tManagers, null);
            return newSslContext;
        }
        catch (NoSuchAlgorithmException e) {
            SslConfiguration.LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol", e);
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyManagementException e2) {
            SslConfiguration.LOGGER.error("Failed to initialize the SSLContext", e2);
            throw new KeyStoreConfigurationException(e2);
        }
    }
    
    private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
        if (this.trustStoreConfig == null) {
            throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
        }
        try {
            return this.trustStoreConfig.initTrustManagerFactory();
        }
        catch (NoSuchAlgorithmException e) {
            SslConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider", e);
            throw new TrustStoreConfigurationException(e);
        }
        catch (KeyStoreException e2) {
            SslConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory", e2);
            throw new TrustStoreConfigurationException(e2);
        }
    }
    
    private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
        if (this.keyStoreConfig == null) {
            throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
        }
        try {
            return this.keyStoreConfig.initKeyManagerFactory();
        }
        catch (NoSuchAlgorithmException e) {
            SslConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider", e);
            throw new KeyStoreConfigurationException(e);
        }
        catch (KeyStoreException e2) {
            SslConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory", e2);
            throw new KeyStoreConfigurationException(e2);
        }
        catch (UnrecoverableKeyException e3) {
            SslConfiguration.LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)", e3);
            throw new KeyStoreConfigurationException(e3);
        }
    }
    
    @PluginFactory
    public static SslConfiguration createSSLConfiguration(@PluginAttribute("protocol") final String protocol, @PluginElement("KeyStore") final KeyStoreConfiguration keyStoreConfig, @PluginElement("TrustStore") final TrustStoreConfiguration trustStoreConfig) {
        return new SslConfiguration(protocol, keyStoreConfig, trustStoreConfig, false);
    }
    
    public static SslConfiguration createSSLConfiguration(@PluginAttribute("protocol") final String protocol, @PluginElement("KeyStore") final KeyStoreConfiguration keyStoreConfig, @PluginElement("TrustStore") final TrustStoreConfiguration trustStoreConfig, @PluginAttribute("verifyHostName") final boolean verifyHostName) {
        return new SslConfiguration(protocol, keyStoreConfig, trustStoreConfig, verifyHostName);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.keyStoreConfig, this.protocol, this.sslContext, this.trustStoreConfig);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SslConfiguration other = (SslConfiguration)obj;
        return Objects.equals(this.keyStoreConfig, other.keyStoreConfig) && Objects.equals(this.protocol, other.protocol) && Objects.equals(this.sslContext, other.sslContext) && Objects.equals(this.trustStoreConfig, other.trustStoreConfig);
    }
    
    public KeyStoreConfiguration getKeyStoreConfig() {
        return this.keyStoreConfig;
    }
    
    public TrustStoreConfiguration getTrustStoreConfig() {
        return this.trustStoreConfig;
    }
    
    public SSLContext getSslContext() {
        return this.sslContext;
    }
    
    public String getProtocol() {
        return this.protocol;
    }
    
    public boolean isVerifyHostName() {
        return this.verifyHostName;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
