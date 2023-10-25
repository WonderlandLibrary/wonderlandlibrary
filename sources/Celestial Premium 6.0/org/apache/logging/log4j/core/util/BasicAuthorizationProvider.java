/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.util;

import java.net.URLConnection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.AuthorizationProvider;
import org.apache.logging.log4j.core.util.PasswordDecryptor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Base64Util;
import org.apache.logging.log4j.util.LoaderUtil;
import org.apache.logging.log4j.util.PropertiesUtil;

public class BasicAuthorizationProvider
implements AuthorizationProvider {
    private static final String[] PREFIXES = new String[]{"log4j2.config.", "logging.auth."};
    private static final String AUTH_USER_NAME = "username";
    private static final String AUTH_PASSWORD = "password";
    private static final String AUTH_PASSWORD_DECRYPTOR = "passwordDecryptor";
    public static final String CONFIG_USER_NAME = "log4j2.configurationUserName";
    public static final String CONFIG_PASSWORD = "log4j2.configurationPassword";
    public static final String PASSWORD_DECRYPTOR = "log4j2.passwordDecryptor";
    private static Logger LOGGER = StatusLogger.getLogger();
    private String authString = null;

    public BasicAuthorizationProvider(PropertiesUtil props) {
        String userName = props.getStringProperty(PREFIXES, AUTH_USER_NAME, () -> props.getStringProperty(CONFIG_USER_NAME));
        String password = props.getStringProperty(PREFIXES, AUTH_PASSWORD, () -> props.getStringProperty(CONFIG_PASSWORD));
        String decryptor = props.getStringProperty(PREFIXES, AUTH_PASSWORD_DECRYPTOR, () -> props.getStringProperty(PASSWORD_DECRYPTOR));
        if (decryptor != null) {
            try {
                Object obj = LoaderUtil.newInstanceOf(decryptor);
                if (obj instanceof PasswordDecryptor) {
                    password = ((PasswordDecryptor)obj).decryptPassword(password);
                }
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to decrypt password.", (Throwable)ex);
            }
        }
        if (userName != null && password != null) {
            this.authString = "Basic " + Base64Util.encode(userName + ":" + password);
        }
    }

    @Override
    public void addAuthorization(URLConnection urlConnection) {
        if (this.authString != null) {
            urlConnection.setRequestProperty("Authorization", this.authString);
        }
    }
}

