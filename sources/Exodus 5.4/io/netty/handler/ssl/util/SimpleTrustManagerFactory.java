/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.ssl.util;

import io.netty.util.concurrent.FastThreadLocal;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.Provider;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.TrustManagerFactorySpi;

public abstract class SimpleTrustManagerFactory
extends TrustManagerFactory {
    private static final Provider PROVIDER = new Provider("", 0.0, ""){
        private static final long serialVersionUID = -2680540247105807895L;
    };
    private static final FastThreadLocal<SimpleTrustManagerFactorySpi> CURRENT_SPI = new FastThreadLocal<SimpleTrustManagerFactorySpi>(){

        @Override
        protected SimpleTrustManagerFactorySpi initialValue() {
            return new SimpleTrustManagerFactorySpi();
        }
    };

    protected abstract void engineInit(KeyStore var1) throws Exception;

    protected SimpleTrustManagerFactory(String string) {
        super(CURRENT_SPI.get(), PROVIDER, string);
        CURRENT_SPI.get().init(this);
        CURRENT_SPI.remove();
        if (string == null) {
            throw new NullPointerException("name");
        }
    }

    protected abstract void engineInit(ManagerFactoryParameters var1) throws Exception;

    protected SimpleTrustManagerFactory() {
        this("");
    }

    protected abstract TrustManager[] engineGetTrustManagers();

    static final class SimpleTrustManagerFactorySpi
    extends TrustManagerFactorySpi {
        private SimpleTrustManagerFactory parent;

        @Override
        protected void engineInit(KeyStore keyStore) throws KeyStoreException {
            try {
                this.parent.engineInit(keyStore);
            }
            catch (KeyStoreException keyStoreException) {
                throw keyStoreException;
            }
            catch (Exception exception) {
                throw new KeyStoreException(exception);
            }
        }

        SimpleTrustManagerFactorySpi() {
        }

        void init(SimpleTrustManagerFactory simpleTrustManagerFactory) {
            this.parent = simpleTrustManagerFactory;
        }

        @Override
        protected TrustManager[] engineGetTrustManagers() {
            return this.parent.engineGetTrustManagers();
        }

        @Override
        protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            try {
                this.parent.engineInit(managerFactoryParameters);
            }
            catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                throw invalidAlgorithmParameterException;
            }
            catch (Exception exception) {
                throw new InvalidAlgorithmParameterException(exception);
            }
        }
    }
}

