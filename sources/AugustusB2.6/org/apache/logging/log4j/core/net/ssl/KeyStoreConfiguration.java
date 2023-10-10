// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;
import java.security.NoSuchAlgorithmException;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Arrays;
import javax.net.ssl.KeyManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "KeyStore", category = "Core", printObject = true)
public class KeyStoreConfiguration extends AbstractKeyStoreConfiguration
{
    private final String keyManagerFactoryAlgorithm;
    
    public KeyStoreConfiguration(final String location, final PasswordProvider passwordProvider, final String keyStoreType, final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        super(location, passwordProvider, keyStoreType);
        this.keyManagerFactoryAlgorithm = ((keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : keyManagerFactoryAlgorithm);
    }
    
    @Deprecated
    public KeyStoreConfiguration(final String location, final char[] password, final String keyStoreType, final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider(password), keyStoreType, keyManagerFactoryAlgorithm);
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }
    
    @Deprecated
    public KeyStoreConfiguration(final String location, final String password, final String keyStoreType, final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider((char[])((password == null) ? null : password.toCharArray())), keyStoreType, keyManagerFactoryAlgorithm);
    }
    
    @PluginFactory
    public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") final String location, @PluginAttribute(value = "password", sensitive = true) final char[] password, @PluginAttribute("passwordEnvironmentVariable") final String passwordEnvironmentVariable, @PluginAttribute("passwordFile") final String passwordFile, @PluginAttribute("type") final String keyStoreType, @PluginAttribute("keyManagerFactoryAlgorithm") final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        if (password != null && passwordEnvironmentVariable != null && passwordFile != null) {
            throw new StoreConfigurationException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'.");
        }
        try {
            final PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
            if (password != null) {
                Arrays.fill(password, '\0');
            }
            return new KeyStoreConfiguration(location, provider, keyStoreType, keyManagerFactoryAlgorithm);
        }
        catch (Exception ex) {
            throw new StoreConfigurationException("Could not configure KeyStore", ex);
        }
    }
    
    @Deprecated
    public static KeyStoreConfiguration createKeyStoreConfiguration(final String location, final char[] password, final String keyStoreType, final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        return createKeyStoreConfiguration(location, password, null, null, keyStoreType, keyManagerFactoryAlgorithm);
    }
    
    @Deprecated
    public static KeyStoreConfiguration createKeyStoreConfiguration(final String location, final String password, final String keyStoreType, final String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
        return createKeyStoreConfiguration(location, (char[])((password == null) ? null : password.toCharArray()), keyStoreType, keyManagerFactoryAlgorithm);
    }
    
    public KeyManagerFactory initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        final KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm);
        final char[] password = this.getPasswordAsCharArray();
        try {
            kmFactory.init(this.getKeyStore(), password);
        }
        finally {
            if (password != null) {
                Arrays.fill(password, '\0');
            }
        }
        return kmFactory;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.keyManagerFactoryAlgorithm == null) ? 0 : this.keyManagerFactoryAlgorithm.hashCode());
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
        final KeyStoreConfiguration other = (KeyStoreConfiguration)obj;
        return Objects.equals(this.keyManagerFactoryAlgorithm, other.keyManagerFactoryAlgorithm);
    }
    
    public String getKeyManagerFactoryAlgorithm() {
        return this.keyManagerFactoryAlgorithm;
    }
}
