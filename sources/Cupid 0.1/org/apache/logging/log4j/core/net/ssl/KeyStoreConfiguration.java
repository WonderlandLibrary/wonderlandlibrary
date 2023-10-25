package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Arrays;
import java.util.Objects;
import javax.net.ssl.KeyManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "KeyStore", category = "Core", printObject = true)
public class KeyStoreConfiguration extends AbstractKeyStoreConfiguration {
  private final String keyManagerFactoryAlgorithm;
  
  public KeyStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    super(location, passwordProvider, keyStoreType);
    this.keyManagerFactoryAlgorithm = (keyManagerFactoryAlgorithm == null) ? KeyManagerFactory.getDefaultAlgorithm() : keyManagerFactoryAlgorithm;
  }
  
  @Deprecated
  public KeyStoreConfiguration(String location, char[] password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider(password), keyStoreType, keyManagerFactoryAlgorithm);
    if (password != null)
      Arrays.fill(password, false); 
  }
  
  @Deprecated
  public KeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType, keyManagerFactoryAlgorithm);
  }
  
  @PluginFactory
  public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) char[] password, @PluginAttribute("passwordEnvironmentVariable") String passwordEnvironmentVariable, @PluginAttribute("passwordFile") String passwordFile, @PluginAttribute("type") String keyStoreType, @PluginAttribute("keyManagerFactoryAlgorithm") String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    if (password != null && passwordEnvironmentVariable != null && passwordFile != null)
      throw new StoreConfigurationException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'."); 
    try {
      PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
      if (password != null)
        Arrays.fill(password, false); 
      return new KeyStoreConfiguration(location, provider, keyStoreType, keyManagerFactoryAlgorithm);
    } catch (Exception ex) {
      throw new StoreConfigurationException("Could not configure KeyStore", ex);
    } 
  }
  
  @Deprecated
  public static KeyStoreConfiguration createKeyStoreConfiguration(String location, char[] password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    return createKeyStoreConfiguration(location, password, (String)null, (String)null, keyStoreType, keyManagerFactoryAlgorithm);
  }
  
  @Deprecated
  public static KeyStoreConfiguration createKeyStoreConfiguration(String location, String password, String keyStoreType, String keyManagerFactoryAlgorithm) throws StoreConfigurationException {
    return createKeyStoreConfiguration(location, (password == null) ? null : password
        .toCharArray(), keyStoreType, keyManagerFactoryAlgorithm);
  }
  
  public KeyManagerFactory initKeyManagerFactory() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
    KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm);
    char[] password = getPasswordAsCharArray();
    try {
      kmFactory.init(getKeyStore(), password);
    } finally {
      if (password != null)
        Arrays.fill(password, false); 
    } 
    return kmFactory;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = super.hashCode();
    result = 31 * result + ((this.keyManagerFactoryAlgorithm == null) ? 0 : this.keyManagerFactoryAlgorithm.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (!super.equals(obj))
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    KeyStoreConfiguration other = (KeyStoreConfiguration)obj;
    if (!Objects.equals(this.keyManagerFactoryAlgorithm, other.keyManagerFactoryAlgorithm))
      return false; 
    return true;
  }
  
  public String getKeyManagerFactoryAlgorithm() {
    return this.keyManagerFactoryAlgorithm;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\ssl\KeyStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */