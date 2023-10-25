package org.apache.logging.log4j.core.util;

import java.net.URLConnection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public class BasicAuthorizationProvider implements AuthorizationProvider {
  private static final String[] PREFIXES = new String[] { "log4j2.config.", "logging.auth." };
  
  private static final String AUTH_USER_NAME = "username";
  
  private static final String AUTH_PASSWORD = "password";
  
  private static final String AUTH_PASSWORD_DECRYPTOR = "passwordDecryptor";
  
  public static final String CONFIG_USER_NAME = "log4j2.configurationUserName";
  
  public static final String CONFIG_PASSWORD = "log4j2.configurationPassword";
  
  public static final String PASSWORD_DECRYPTOR = "log4j2.passwordDecryptor";
  
  private static Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private String authString = null;
  
  public BasicAuthorizationProvider(PropertiesUtil props) {
    String userName = props.getStringProperty(PREFIXES, "username", () -> props.getStringProperty("log4j2.configurationUserName"));
    String password = props.getStringProperty(PREFIXES, "password", () -> props.getStringProperty("log4j2.configurationPassword"));
    String decryptor = props.getStringProperty(PREFIXES, "passwordDecryptor", () -> props.getStringProperty("log4j2.passwordDecryptor"));
    if (decryptor != null)
      try {
        Object obj = LoaderUtil.newInstanceOf(decryptor);
        if (obj instanceof PasswordDecryptor)
          password = ((PasswordDecryptor)obj).decryptPassword(password); 
      } catch (Exception ex) {
        LOGGER.warn("Unable to decrypt password.", ex);
      }  
    if (userName != null && password != null)
      this.authString = "Basic " + Base64Util.encode(userName + ":" + password); 
  }
  
  public void addAuthorization(URLConnection urlConnection) {
    if (this.authString != null)
      urlConnection.setRequestProperty("Authorization", this.authString); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\BasicAuthorizationProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */