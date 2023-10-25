/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.net.ssl.StoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.StoreConfigurationException;

@Plugin(name="keyStore", category="Core", printObject=true)
public class KeyStoreConfiguration
extends StoreConfiguration {
    private KeyStore keyStore = null;
    private String keyStoreType = "JKS";

    public KeyStoreConfiguration(String location, String password) {
        super(location, password);
    }

    @Override
    protected void load() throws StoreConfigurationException {
        FileInputStream fin = null;
        LOGGER.debug("Loading keystore from file with params(location={})", this.getLocation());
        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }
            fin = new FileInputStream(this.getLocation());
            KeyStore ks = KeyStore.getInstance(this.keyStoreType);
            ks.load(fin, this.getPasswordAsCharArray());
            this.keyStore = ks;
        }
        catch (CertificateException e) {
            LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", this.keyStoreType);
            throw new StoreConfigurationException(e);
        }
        catch (NoSuchAlgorithmException e) {
            LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
            throw new StoreConfigurationException(e);
        }
        catch (KeyStoreException e) {
            LOGGER.error(e);
            throw new StoreConfigurationException(e);
        }
        catch (FileNotFoundException e) {
            LOGGER.error("The keystore file({}) is not found", this.getLocation());
            throw new StoreConfigurationException(e);
        }
        catch (IOException e) {
            LOGGER.error("Something is wrong with the format of the keystore or the given password");
            throw new StoreConfigurationException(e);
        }
        finally {
            try {
                if (fin != null) {
                    fin.close();
                }
            }
            catch (IOException e) {}
        }
        LOGGER.debug("Keystore successfully loaded with params(location={})", this.getLocation());
    }

    public KeyStore getKeyStore() throws StoreConfigurationException {
        if (this.keyStore == null) {
            this.load();
        }
        return this.keyStore;
    }

    @PluginFactory
    public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute(value="location") String location, @PluginAttribute(value="password") String password) {
        return new KeyStoreConfiguration(location, password);
    }
}

