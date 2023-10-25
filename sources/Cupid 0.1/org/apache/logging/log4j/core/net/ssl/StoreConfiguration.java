package org.apache.logging.log4j.core.net.ssl;

import java.util.Arrays;
import java.util.Objects;
import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration<T> {
  protected static final StatusLogger LOGGER = StatusLogger.getLogger();
  
  private String location;
  
  private PasswordProvider passwordProvider;
  
  public StoreConfiguration(String location, PasswordProvider passwordProvider) {
    this.location = location;
    this.passwordProvider = Objects.<PasswordProvider>requireNonNull(passwordProvider, "passwordProvider");
  }
  
  @Deprecated
  public StoreConfiguration(String location, char[] password) {
    this(location, new MemoryPasswordProvider(password));
  }
  
  @Deprecated
  public StoreConfiguration(String location, String password) {
    this(location, new MemoryPasswordProvider((password == null) ? null : password.toCharArray()));
  }
  
  public void clearSecrets() {
    this.location = null;
    this.passwordProvider = null;
  }
  
  public String getLocation() {
    return this.location;
  }
  
  public void setLocation(String location) {
    this.location = location;
  }
  
  @Deprecated
  public String getPassword() {
    return String.valueOf(this.passwordProvider.getPassword());
  }
  
  public char[] getPasswordAsCharArray() {
    return this.passwordProvider.getPassword();
  }
  
  public void setPassword(char[] password) {
    this.passwordProvider = new MemoryPasswordProvider(password);
  }
  
  @Deprecated
  public void setPassword(String password) {
    this.passwordProvider = new MemoryPasswordProvider((password == null) ? null : password.toCharArray());
  }
  
  protected T load() throws StoreConfigurationException {
    return null;
  }
  
  public int hashCode() {
    int prime = 31;
    int result = 1;
    result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
    result = 31 * result + Arrays.hashCode(this.passwordProvider.getPassword());
    return result;
  }
  
  public boolean equals(Object obj) {
    if (this == obj)
      return true; 
    if (obj == null)
      return false; 
    if (getClass() != obj.getClass())
      return false; 
    StoreConfiguration<?> other = (StoreConfiguration)obj;
    if (!Objects.equals(this.location, other.location))
      return false; 
    if (!Arrays.equals(this.passwordProvider.getPassword(), other.passwordProvider.getPassword()))
      return false; 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\net\ssl\StoreConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */