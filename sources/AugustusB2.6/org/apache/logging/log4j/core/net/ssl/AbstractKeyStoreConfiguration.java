// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net.ssl;

import java.util.Objects;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.util.NetUtils;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.io.IOException;
import java.security.KeyStore;

public class AbstractKeyStoreConfiguration extends StoreConfiguration<KeyStore>
{
    private final KeyStore keyStore;
    private final String keyStoreType;
    
    public AbstractKeyStoreConfiguration(final String location, final PasswordProvider passwordProvider, final String keyStoreType) throws StoreConfigurationException {
        super(location, passwordProvider);
        this.keyStoreType = ((keyStoreType == null) ? "JKS" : keyStoreType);
        this.keyStore = this.load();
    }
    
    @Deprecated
    public AbstractKeyStoreConfiguration(final String location, final char[] password, final String keyStoreType) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider(password), keyStoreType);
    }
    
    @Deprecated
    public AbstractKeyStoreConfiguration(final String location, final String password, final String keyStoreType) throws StoreConfigurationException {
        this(location, new MemoryPasswordProvider((char[])((password == null) ? null : password.toCharArray())), keyStoreType);
    }
    
    @Override
    protected KeyStore load() throws StoreConfigurationException {
        final String loadLocation = this.getLocation();
        AbstractKeyStoreConfiguration.LOGGER.debug("Loading keystore from location {}", loadLocation);
        try {
            if (loadLocation == null) {
                throw new IOException("The location is null");
            }
            try (final InputStream fin = this.openInputStream(loadLocation)) {
                final KeyStore ks = KeyStore.getInstance(this.keyStoreType);
                final char[] password = this.getPasswordAsCharArray();
                try {
                    ks.load(fin, password);
                }
                finally {
                    if (password != null) {
                        Arrays.fill(password, '\0');
                    }
                }
                AbstractKeyStoreConfiguration.LOGGER.debug("KeyStore successfully loaded from location {}", loadLocation);
                return ks;
            }
        }
        catch (CertificateException e) {
            AbstractKeyStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {} for location {}", this.keyStoreType, loadLocation, e);
            throw new StoreConfigurationException(loadLocation, e);
        }
        catch (NoSuchAlgorithmException e2) {
            AbstractKeyStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found for location {}", loadLocation, e2);
            throw new StoreConfigurationException(loadLocation, e2);
        }
        catch (KeyStoreException e3) {
            AbstractKeyStoreConfiguration.LOGGER.error("KeyStoreException for location {}", loadLocation, e3);
            throw new StoreConfigurationException(loadLocation, e3);
        }
        catch (FileNotFoundException e4) {
            AbstractKeyStoreConfiguration.LOGGER.error("The keystore file {} is not found", loadLocation, e4);
            throw new StoreConfigurationException(loadLocation, e4);
        }
        catch (IOException e5) {
            AbstractKeyStoreConfiguration.LOGGER.error("Something is wrong with the format of the keystore or the given password for location {}", loadLocation, e5);
            throw new StoreConfigurationException(loadLocation, e5);
        }
    }
    
    private InputStream openInputStream(final String filePathOrUri) {
        return ConfigurationSource.fromUri(NetUtils.toURI(filePathOrUri)).getInputStream();
    }
    
    public KeyStore getKeyStore() {
        return this.keyStore;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.keyStore == null) ? 0 : this.keyStore.hashCode());
        result = 31 * result + ((this.keyStoreType == null) ? 0 : this.keyStoreType.hashCode());
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
        final AbstractKeyStoreConfiguration other = (AbstractKeyStoreConfiguration)obj;
        return Objects.equals(this.keyStore, other.keyStore) && Objects.equals(this.keyStoreType, other.keyStoreType);
    }
    
    public String getKeyStoreType() {
        return this.keyStoreType;
    }
}
