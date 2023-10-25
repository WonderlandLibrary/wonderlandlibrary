/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.net.ssl.KeyStoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.SslConfiguration;
import org.apache.logging.log4j.core.net.ssl.TrustStoreConfiguration;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class SslConfigurationFactory {
    private static final Logger LOGGER;
    private static SslConfiguration sslConfiguration;
    private static final String trustStorelocation = "log4j2.trustStoreLocation";
    private static final String trustStorePassword = "log4j2.trustStorePassword";
    private static final String trustStorePasswordFile = "log4j2.trustStorePasswordFile";
    private static final String trustStorePasswordEnvVar = "log4j2.trustStorePasswordEnvironmentVariable";
    private static final String trustStoreKeyStoreType = "log4j2.trustStoreKeyStoreType";
    private static final String trustStoreKeyManagerFactoryAlgorithm = "log4j2.trustStoreKeyManagerFactoryAlgorithm";
    private static final String keyStoreLocation = "log4j2.keyStoreLocation";
    private static final String keyStorePassword = "log4j2.keyStorePassword";
    private static final String keyStorePasswordFile = "log4j2.keyStorePasswordFile";
    private static final String keyStorePasswordEnvVar = "log4j2.keyStorePasswordEnvironmentVariable";
    private static final String keyStoreType = "log4j2.keyStoreType";
    private static final String keyStoreKeyManagerFactoryAlgorithm = "log4j2.keyStoreKeyManagerFactoryAlgorithm";
    private static final String verifyHostName = "log4j2.sslVerifyHostName";

    public static SslConfiguration getSslConfiguration() {
        return sslConfiguration;
    }

    static {
        char[] passwordChars;
        String password;
        LOGGER = StatusLogger.getLogger();
        sslConfiguration = null;
        PropertiesUtil props = PropertiesUtil.getProperties();
        KeyStoreConfiguration keyStoreConfiguration = null;
        TrustStoreConfiguration trustStoreConfiguration = null;
        String location = props.getStringProperty(trustStorelocation);
        if (location != null) {
            password = props.getStringProperty(trustStorePassword);
            passwordChars = null;
            if (password != null) {
                passwordChars = password.toCharArray();
            }
            try {
                trustStoreConfiguration = TrustStoreConfiguration.createKeyStoreConfiguration((String)location, (char[])passwordChars, (String)props.getStringProperty(trustStorePasswordEnvVar), (String)props.getStringProperty(trustStorePasswordFile), (String)props.getStringProperty(trustStoreKeyStoreType), (String)props.getStringProperty(trustStoreKeyManagerFactoryAlgorithm));
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to create trust store configuration due to: {} {}", ex.getClass().getName(), ex.getMessage());
            }
        }
        if ((location = props.getStringProperty(keyStoreLocation)) != null) {
            password = props.getStringProperty(keyStorePassword);
            passwordChars = null;
            if (password != null) {
                passwordChars = password.toCharArray();
            }
            try {
                keyStoreConfiguration = KeyStoreConfiguration.createKeyStoreConfiguration((String)location, (char[])passwordChars, (String)props.getStringProperty(keyStorePasswordEnvVar), (String)props.getStringProperty(keyStorePasswordFile), (String)props.getStringProperty(keyStoreType), (String)props.getStringProperty(keyStoreKeyManagerFactoryAlgorithm));
            }
            catch (Exception ex) {
                LOGGER.warn("Unable to create key store configuration due to: {} {}", ex.getClass().getName(), ex.getMessage());
            }
        }
        if (trustStoreConfiguration != null || keyStoreConfiguration != null) {
            boolean isVerifyHostName = props.getBooleanProperty(verifyHostName, false);
            sslConfiguration = SslConfiguration.createSSLConfiguration("https", keyStoreConfiguration, trustStoreConfiguration, isVerifyHostName);
        }
    }
}

