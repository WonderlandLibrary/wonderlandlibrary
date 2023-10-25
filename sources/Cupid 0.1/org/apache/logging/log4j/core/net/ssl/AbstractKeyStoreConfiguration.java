package org.apache.logging.log4j.core.net.ssl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.util.NetUtils;

public class AbstractKeyStoreConfiguration extends StoreConfiguration<KeyStore> {
  private final KeyStore keyStore;
  
  private final String keyStoreType;
  
  public AbstractKeyStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType) throws StoreConfigurationException {
    super(location, passwordProvider);
    this.keyStoreType = (keyStoreType == null) ? "JKS" : keyStoreType;
    this.keyStore = load();
  }
  
  @Deprecated
  public AbstractKeyStoreConfiguration(String location, char[] password, String keyStoreType) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider(password), keyStoreType);
  }
  
  @Deprecated
  public AbstractKeyStoreConfiguration(String location, String password, String keyStoreType) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType);
  }
  
  protected KeyStore load() throws StoreConfigurationException {
    String loadLocation = getLocation();
    LOGGER.debug("Loading keystore from location {}", loadLocation);
    try {
      if (loadLocation == null)
        throw new IOException("The location is null"); 
      try (InputStream fin = openInputStream(loadLocation)) {
        KeyStore ks = KeyStore.getInstance(this.keyStoreType);
        char[] password = getPasswordAsCharArray();
        try {
          ks.load(fin, password);
        } finally {
          if (password != null)
            Arrays.fill(password, false); 
        } 
        LOGGER.debug("KeyStore successfully loaded from location {}", loadLocation);
        return ks;
      } 
    } catch (CertificateException e) {
      LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {} for location {}", this.keyStoreType, loadLocation, e);
      throw new StoreConfigurationException(loadLocation, e);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found for location {}", loadLocation, e);
      throw new StoreConfigurationException(loadLocation, e);
    } catch (KeyStoreException e) {
      LOGGER.error("KeyStoreException for location {}", loadLocation, e);
      throw new StoreConfigurationException(loadLocation, e);
    } catch (FileNotFoundException e) {
      LOGGER.error("The keystore file {} is not found", loadLocation, e);
      throw new StoreConfigurationException(loadLocation, e);
    } catch (IOException e) {
      LOGGER.error("Something is wrong with the format of the keystore or the given password for location {}", loadLocation, e);
      throw new StoreConfigurationException(loadLocation, e);
    } 
  }
  
  private InputStream openInputStream(String filePathOrUri) {
    return ConfigurationSource.fromUri(NetUtils.toURI(filePathOrUri)).getInputStream();
  }
  
  public KeyStore getKeyStore() {
    return this.keyStore;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = super.hashCode();
    result = 31 * result + ((this.keyStore == null) ? 0 : this.keyStore.hashCode());
    result = 31 * result + ((this.keyStoreType == null) ? 0 : this.keyStoreType.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (!super.equals(obj))
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    AbstractKeyStoreConfiguration other = (AbstractKeyStoreConfiguration)obj;
    if (!Objects.equals(this.keyStore, other.keyStore))
      return false; 
    if (!Objects.equals(this.keyStoreType, other.keyStoreType))
      return false; 
    return true;
  }
  
  public String getKeyStoreType() {
    return this.keyStoreType;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\ssl\AbstractKeyStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */