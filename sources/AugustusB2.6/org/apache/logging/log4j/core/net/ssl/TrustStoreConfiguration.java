// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Arrays;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TrustStore", category = "Core", printObject = true)
public class TrustStoreConfiguration extends AbstractKeyStoreConfiguration
{
    private final String trustManagerFactoryAlgorithm;
    
    public TrustStoreConfiguration(final String location, final PasswordProvider passwordProvider, final String keyStoreType, final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        super(location, passwordProvider, keyStoreType);
        this.trustManagerFactoryAlgorithm = ((trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : trustManagerFactoryAlgorithm);
    }
    
    @Deprecated
    public TrustStoreConfiguration(final String location, final char[] password, final String keyStoreType, final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider(password), keyStoreType, trustManagerFactoryAlgorithm);
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }
    
    @Deprecated
    public TrustStoreConfiguration(final String location, final String password, final String keyStoreType, final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider((char[])((password == null) ? null : password.toCharArray())), keyStoreType, trustManagerFactoryAlgorithm);
    }
    
    @PluginFactory
    public static TrustStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") final String location, @PluginAttribute(value = "password", sensitive = true) final char[] password, @PluginAttribute("passwordEnvironmentVariable") final String passwordEnvironmentVariable, @PluginAttribute("passwordFile") final String passwordFile, @PluginAttribute("type") final String keyStoreType, @PluginAttribute("trustManagerFactoryAlgorithm") final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        if (password != null && passwordEnvironmentVariable != null && passwordFile != null) {
            throw new IllegalStateException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'.");
        }
        try {
            final PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
            if (password != null) {
                Arrays.fill(password, '\0');
            }
            return new TrustStoreConfiguration(location, provider, keyStoreType, trustManagerFactoryAlgorithm);
        }
        catch (Exception ex) {
            throw new StoreConfigurationException("Could not configure TrustStore", ex);
        }
    }
    
    @Deprecated
    public static TrustStoreConfiguration createKeyStoreConfiguration(final String location, final char[] password, final String keyStoreType, final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        return createKeyStoreConfiguration(location, password, null, null, keyStoreType, trustManagerFactoryAlgorithm);
    }
    
    @Deprecated
    public static TrustStoreConfiguration createKeyStoreConfiguration(final String location, final String password, final String keyStoreType, final String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
        return createKeyStoreConfiguration(location, (char[])((password == null) ? null : password.toCharArray()), null, null, keyStoreType, trustManagerFactoryAlgorithm);
    }
    
    public TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
        final TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm);
        tmFactory.init(this.getKeyStore());
        return tmFactory;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.trustManagerFactoryAlgorithm == null) ? 0 : this.trustManagerFactoryAlgorithm.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final TrustStoreConfiguration other = (TrustStoreConfiguration)obj;
        return Objects.equals(this.trustManagerFactoryAlgorithm, other.trustManagerFactoryAlgorithm);
    }
    
    public String getTrustManagerFactoryAlgorithm() {
        return this.trustManagerFactoryAlgorithm;
    }
}
