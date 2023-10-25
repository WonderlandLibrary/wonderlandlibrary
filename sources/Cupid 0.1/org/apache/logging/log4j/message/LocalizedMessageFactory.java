package org.apache.logging.log4j.message;

import java.util.ResourceBundle;

public class LocalizedMessageFactory extends AbstractMessageFactory {
  private static final long serialVersionUID = -1996295808703146741L;
  
  private final transient ResourceBundle resourceBundle;
  
  private final String baseName;
  
  public LocalizedMessageFactory(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
    this.baseName = null;
  }
  
  public LocalizedMessageFactory(String baseName) {
    this.resourceBundle = null;
    this.baseName = baseName;
  }
  
  public String getBaseName() {
    return this.baseName;
  }
  
  public ResourceBundle getResourceBundle() {
    return this.resourceBundle;
  }
  
  public Message newMessage(String key) {
    if (this.resourceBundle == null)
      return new LocalizedMessage(this.baseName, key); 
    return new LocalizedMessage(this.resourceBundle, key);
  }
  
  public Message newMessage(String key, Object... params) {
    if (this.resourceBundle == null)
      return new LocalizedMessage(this.baseName, key, params); 
    return new LocalizedMessage(this.resourceBundle, key, params);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\message\LocalizedMessageFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */