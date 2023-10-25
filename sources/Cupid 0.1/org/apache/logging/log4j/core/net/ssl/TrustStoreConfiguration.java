package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "TrustStore", category = "Core", printObject = true)
public class TrustStoreConfiguration extends AbstractKeyStoreConfiguration {
  private final String trustManagerFactoryAlgorithm;
  
  public TrustStoreConfiguration(String location, PasswordProvider passwordProvider, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    super(location, passwordProvider, keyStoreType);
    this
      .trustManagerFactoryAlgorithm = (trustManagerFactoryAlgorithm == null) ? TrustManagerFactory.getDefaultAlgorithm() : trustManagerFactoryAlgorithm;
  }
  
  @Deprecated
  public TrustStoreConfiguration(String location, char[] password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider(password), keyStoreType, trustManagerFactoryAlgorithm);
    if (password != null)
      Arrays.fill(password, false); 
  }
  
  @Deprecated
  public TrustStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()), keyStoreType, trustManagerFactoryAlgorithm);
  }
  
  @PluginFactory
  public static TrustStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String location, @PluginAttribute(value = "password", sensitive = true) char[] password, @PluginAttribute("passwordEnvironmentVariable") String passwordEnvironmentVariable, @PluginAttribute("passwordFile") String passwordFile, @PluginAttribute("type") String keyStoreType, @PluginAttribute("trustManagerFactoryAlgorithm") String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    if (password != null && passwordEnvironmentVariable != null && passwordFile != null)
      throw new IllegalStateException("You MUST set only one of 'password', 'passwordEnvironmentVariable' or 'passwordFile'."); 
    try {
      PasswordProvider provider = (passwordFile != null) ? new FilePasswordProvider(passwordFile) : ((passwordEnvironmentVariable != null) ? new EnvironmentPasswordProvider(passwordEnvironmentVariable) : new MemoryPasswordProvider(password));
      if (password != null)
        Arrays.fill(password, false); 
      return new TrustStoreConfiguration(location, provider, keyStoreType, trustManagerFactoryAlgorithm);
    } catch (Exception ex) {
      throw new StoreConfigurationException("Could not configure TrustStore", ex);
    } 
  }
  
  @Deprecated
  public static TrustStoreConfiguration createKeyStoreConfiguration(String location, char[] password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    return createKeyStoreConfiguration(location, password, null, null, keyStoreType, trustManagerFactoryAlgorithm);
  }
  
  @Deprecated
  public static TrustStoreConfiguration createKeyStoreConfiguration(String location, String password, String keyStoreType, String trustManagerFactoryAlgorithm) throws StoreConfigurationException {
    return createKeyStoreConfiguration(location, (password == null) ? null : password.toCharArray(), null, null, keyStoreType, trustManagerFactoryAlgorithm);
  }
  
  public TrustManagerFactory initTrustManagerFactory() throws NoSuchAlgorithmException, KeyStoreException {
    TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm);
    tmFactory.init(getKeyStore());
    return tmFactory;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = super.hashCode();
    result = 31 * result + ((this.trustManagerFactoryAlgorithm == null) ? 0 : this.trustManagerFactoryAlgorithm.hashCode());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (!super.equals(obj))
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    TrustStoreConfiguration other = (TrustStoreConfiguration)obj;
    if (!Objects.equals(this.trustManagerFactoryAlgorithm, other.trustManagerFactoryAlgorithm))
      return false; 
    return true;
  }
  
  public String getTrustManagerFactoryAlgorithm() {
    return this.trustManagerFactoryAlgorithm;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\ssl\TrustStoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */