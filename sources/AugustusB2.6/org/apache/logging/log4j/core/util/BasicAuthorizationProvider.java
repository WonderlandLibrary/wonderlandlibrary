// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.status.StatusLogger;
import java.net.URLConnection;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.Logger;

public class BasicAuthorizationProvider implements AuthorizationProvider
{
    private static final String[] PREFIXES;
    private static final String AUTH_USER_NAME = "username";
    private static final String AUTH_PASSWORD = "password";
    private static final String AUTH_PASSWORD_DECRYPTOR = "passwordDecryptor";
    public static final String CONFIG_USER_NAME = "log4j2.configurationUserName";
    public static final String CONFIG_PASSWORD = "log4j2.configurationPassword";
    public static final String PASSWORD_DECRYPTOR = "log4j2.passwordDecryptor";
    private static Logger LOGGER;
    private String authString;
    
    public BasicAuthorizationProvider(final PropertiesUtil props) {
        this.authString = null;
        final String userName = props.getStringProperty(BasicAuthorizationProvider.PREFIXES, "username", () -> props.getStringProperty("log4j2.configurationUserName"));
        String password = props.getStringProperty(BasicAuthorizationProvider.PREFIXES, "password", () -> props.getStringProperty("log4j2.configurationPassword"));
        final String decryptor = props.getStringProperty(BasicAuthorizationProvider.PREFIXES, "passwordDecryptor", () -> props.getStringProperty("log4j2.passwordDecryptor"));
        if (decryptor != null) {
            try {
                final Object obj = LoaderUtil.newInstanceOf(decryptor);
                if (obj instanceof PasswordDecryptor) {
                    password = ((PasswordDecryptor)obj).decryptPassword(password);
                }
            }
            catch (Exception ex) {
                BasicAuthorizationProvider.LOGGER.warn("Unable to decrypt password.", ex);
            }
        }
        if (userName != null && password != null) {
            this.authString = "Basic " + Base64Util.encode(userName + ":" + password);
        }
    }
    
    @Override
    public void addAuthorization(final URLConnection urlConnection) {
        if (this.authString != null) {
            urlConnection.setRequestProperty("Authorization", this.authString);
        }
    }
    
    static {
        PREFIXES = new String[] { "log4j2.config.", "logging.auth." };
        BasicAuthorizationProvider.LOGGER = StatusLogger.getLogger();
    }
}
