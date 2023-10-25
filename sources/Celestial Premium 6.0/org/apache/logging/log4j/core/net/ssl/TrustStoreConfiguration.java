/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.net.ssl.StoreConfiguration;
import org.apache.logging.log4j.core.net.ssl.StoreConfigurationException;

@Plugin(name="trustStore", category="Core", printObject=true)
public class TrustStoreConfiguration
extends StoreConfiguration {
    private KeyStore trustStore = null;
    private String trustStoreType = "JKS";

    public TrustStoreConfiguration(String location, String password) {
        super(location, password);
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    protected void load() throws StoreConfigurationException {
        KeyStore ts;
        block14: {
            ts = null;
            InputStream in = null;
            LOGGER.debug("Loading truststore from file with params(location={})", this.getLocation());
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }
            ts = KeyStore.getInstance(this.trustStoreType);
            in = new FileInputStream(this.getLocation());
            ts.load(in, this.getPasswordAsCharArray());
            try {
                if (in != null) {
                    in.close();
                }
                break block14;
            }
            catch (Exception e) {
                LOGGER.warn("Error closing {}", this.getLocation(), e);
            }
            break block14;
            catch (CertificateException e) {
                try {
                    LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", this.trustStoreType);
                    throw new StoreConfigurationException(e);
                    catch (NoSuchAlgorithmException e2) {
                        LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
                        throw new StoreConfigurationException(e2);
                    }
                    catch (KeyStoreException e3) {
                        LOGGER.error(e3);
                        throw new StoreConfigurationException(e3);
                    }
                    catch (FileNotFoundException e4) {
                        LOGGER.error("The keystore file({}) is not found", this.getLocation());
                        throw new StoreConfigurationException(e4);
                    }
                    catch (IOException e5) {
                        LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", e5.getMessage());
                        throw new StoreConfigurationException(e5);
                    }
                }
                catch (Throwable throwable) {
                    try {
                        if (in == null) throw throwable;
                        in.close();
                        throw throwable;
                    }
                    catch (Exception e6) {
                        LOGGER.warn("Error closing {}", this.getLocation(), e6);
                    }
                    throw throwable;
                }
            }
        }
        this.trustStore = ts;
        LOGGER.debug("Truststore successfully loaded with params(location={})", this.getLocation());
    }

    public KeyStore getTrustStore() throws StoreConfigurationException {
        if (this.trustStore == null) {
            this.load();
        }
        return this.trustStore;
    }

    @PluginFactory
    public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute(value="location") String location, @PluginAttribute(value="password") String password) {
        return new TrustStoreConfiguration(location, password);
    }
}

