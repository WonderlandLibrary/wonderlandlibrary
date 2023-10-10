/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.Args;

public class SSLContextBuilder {
    static final String TLS = "TLS";
    private String protocol;
    private final Set<KeyManager> keyManagers;
    private String keyManagerFactoryAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
    private String keyStoreType = KeyStore.getDefaultType();
    private final Set<TrustManager> trustManagers;
    private String trustManagerFactoryAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    private SecureRandom secureRandom;
    private Provider provider;

    public static SSLContextBuilder create() {
        return new SSLContextBuilder();
    }

    public SSLContextBuilder() {
        this.keyManagers = new LinkedHashSet<KeyManager>();
        this.trustManagers = new LinkedHashSet<TrustManager>();
    }

    @Deprecated
    public SSLContextBuilder useProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public SSLContextBuilder setProtocol(String protocol) {
        this.protocol = protocol;
        return this;
    }

    public SSLContextBuilder setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
        return this;
    }

    public SSLContextBuilder setProvider(Provider provider) {
        this.provider = provider;
        return this;
    }

    public SSLContextBuilder setProvider(String name) {
        this.provider = Security.getProvider(name);
        return this;
    }

    public SSLContextBuilder setKeyStoreType(String keyStoreType) {
        this.keyStoreType = keyStoreType;
        return this;
    }

    public SSLContextBuilder setKeyManagerFactoryAlgorithm(String keyManagerFactoryAlgorithm) {
        this.keyManagerFactoryAlgorithm = keyManagerFactoryAlgorithm;
        return this;
    }

    public SSLContextBuilder setTrustManagerFactoryAlgorithm(String trustManagerFactoryAlgorithm) {
        this.trustManagerFactoryAlgorithm = trustManagerFactoryAlgorithm;
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(KeyStore truststore, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(this.trustManagerFactoryAlgorithm == null ? TrustManagerFactory.getDefaultAlgorithm() : this.trustManagerFactoryAlgorithm);
        tmfactory.init(truststore);
        TrustManager[] tms = tmfactory.getTrustManagers();
        if (tms != null) {
            if (trustStrategy != null) {
                for (int i2 = 0; i2 < tms.length; ++i2) {
                    TrustManager tm = tms[i2];
                    if (!(tm instanceof X509TrustManager)) continue;
                    tms[i2] = new TrustManagerDelegate((X509TrustManager)tm, trustStrategy);
                }
            }
            for (TrustManager tm : tms) {
                this.trustManagers.add(tm);
            }
        }
        return this;
    }

    public SSLContextBuilder loadTrustMaterial(TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException {
        return this.loadTrustMaterial(null, trustStrategy);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Args.notNull(file, "Truststore file");
        KeyStore trustStore = KeyStore.getInstance(this.keyStoreType);
        FileInputStream instream = new FileInputStream(file);
        try {
            trustStore.load(instream, storePassword);
        }
        finally {
            instream.close();
        }
        return this.loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(File file, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return this.loadTrustMaterial(file, storePassword, null);
    }

    public SSLContextBuilder loadTrustMaterial(File file) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return this.loadTrustMaterial(file, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword, TrustStrategy trustStrategy) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        Args.notNull(url, "Truststore URL");
        KeyStore trustStore = KeyStore.getInstance(this.keyStoreType);
        InputStream instream = url.openStream();
        try {
            trustStore.load(instream, storePassword);
        }
        finally {
            instream.close();
        }
        return this.loadTrustMaterial(trustStore, trustStrategy);
    }

    public SSLContextBuilder loadTrustMaterial(URL url, char[] storePassword) throws NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
        return this.loadTrustMaterial(url, storePassword, null);
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(this.keyManagerFactoryAlgorithm == null ? KeyManagerFactory.getDefaultAlgorithm() : this.keyManagerFactoryAlgorithm);
        kmfactory.init(keystore, keyPassword);
        KeyManager[] kms = kmfactory.getKeyManagers();
        if (kms != null) {
            if (aliasStrategy != null) {
                for (int i2 = 0; i2 < kms.length; ++i2) {
                    KeyManager km = kms[i2];
                    if (!(km instanceof X509ExtendedKeyManager)) continue;
                    kms[i2] = new KeyManagerDelegate((X509ExtendedKeyManager)km, aliasStrategy);
                }
            }
            for (KeyManager km : kms) {
                this.keyManagers.add(km);
            }
        }
        return this;
    }

    public SSLContextBuilder loadKeyMaterial(KeyStore keystore, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException {
        return this.loadKeyMaterial(keystore, keyPassword, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Args.notNull(file, "Keystore file");
        KeyStore identityStore = KeyStore.getInstance(this.keyStoreType);
        FileInputStream instream = new FileInputStream(file);
        try {
            identityStore.load(instream, storePassword);
        }
        finally {
            instream.close();
        }
        return this.loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(File file, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return this.loadKeyMaterial(file, storePassword, keyPassword, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword, PrivateKeyStrategy aliasStrategy) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        Args.notNull(url, "Keystore URL");
        KeyStore identityStore = KeyStore.getInstance(this.keyStoreType);
        InputStream instream = url.openStream();
        try {
            identityStore.load(instream, storePassword);
        }
        finally {
            instream.close();
        }
        return this.loadKeyMaterial(identityStore, keyPassword, aliasStrategy);
    }

    public SSLContextBuilder loadKeyMaterial(URL url, char[] storePassword, char[] keyPassword) throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        return this.loadKeyMaterial(url, storePassword, keyPassword, null);
    }

    protected void initSSLContext(SSLContext sslContext, Collection<KeyManager> keyManagers, Collection<TrustManager> trustManagers, SecureRandom secureRandom) throws KeyManagementException {
        sslContext.init(!keyManagers.isEmpty() ? keyManagers.toArray(new KeyManager[keyManagers.size()]) : null, !trustManagers.isEmpty() ? trustManagers.toArray(new TrustManager[trustManagers.size()]) : null, secureRandom);
    }

    public SSLContext build() throws NoSuchAlgorithmException, KeyManagementException {
        String protocolStr = this.protocol != null ? this.protocol : TLS;
        SSLContext sslContext = this.provider != null ? SSLContext.getInstance(protocolStr, this.provider) : SSLContext.getInstance(protocolStr);
        this.initSSLContext(sslContext, this.keyManagers, this.trustManagers, this.secureRandom);
        return sslContext;
    }

    public String toString() {
        return "[provider=" + this.provider + ", protocol=" + this.protocol + ", keyStoreType=" + this.keyStoreType + ", keyManagerFactoryAlgorithm=" + this.keyManagerFactoryAlgorithm + ", keyManagers=" + this.keyManagers + ", trustManagerFactoryAlgorithm=" + this.trustManagerFactoryAlgorithm + ", trustManagers=" + this.trustManagers + ", secureRandom=" + this.secureRandom + "]";
    }

    static class KeyManagerDelegate
    extends X509ExtendedKeyManager {
        private final X509ExtendedKeyManager keyManager;
        private final PrivateKeyStrategy aliasStrategy;

        KeyManagerDelegate(X509ExtendedKeyManager keyManager, PrivateKeyStrategy aliasStrategy) {
            this.keyManager = keyManager;
            this.aliasStrategy = aliasStrategy;
        }

        @Override
        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return this.keyManager.getClientAliases(keyType, issuers);
        }

        public Map<String, PrivateKeyDetails> getClientAliasMap(String[] keyTypes, Principal[] issuers) {
            HashMap<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
            for (String keyType : keyTypes) {
                String[] aliases = this.keyManager.getClientAliases(keyType, issuers);
                if (aliases == null) continue;
                for (String alias : aliases) {
                    validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
                }
            }
            return validAliases;
        }

        public Map<String, PrivateKeyDetails> getServerAliasMap(String keyType, Principal[] issuers) {
            HashMap<String, PrivateKeyDetails> validAliases = new HashMap<String, PrivateKeyDetails>();
            String[] aliases = this.keyManager.getServerAliases(keyType, issuers);
            if (aliases != null) {
                for (String alias : aliases) {
                    validAliases.put(alias, new PrivateKeyDetails(keyType, this.keyManager.getCertificateChain(alias)));
                }
            }
            return validAliases;
        }

        @Override
        public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
            Map<String, PrivateKeyDetails> validAliases = this.getClientAliasMap(keyTypes, issuers);
            return this.aliasStrategy.chooseAlias(validAliases, socket);
        }

        @Override
        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return this.keyManager.getServerAliases(keyType, issuers);
        }

        @Override
        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            Map<String, PrivateKeyDetails> validAliases = this.getServerAliasMap(keyType, issuers);
            return this.aliasStrategy.chooseAlias(validAliases, socket);
        }

        @Override
        public X509Certificate[] getCertificateChain(String alias) {
            return this.keyManager.getCertificateChain(alias);
        }

        @Override
        public PrivateKey getPrivateKey(String alias) {
            return this.keyManager.getPrivateKey(alias);
        }

        @Override
        public String chooseEngineClientAlias(String[] keyTypes, Principal[] issuers, SSLEngine sslEngine) {
            Map<String, PrivateKeyDetails> validAliases = this.getClientAliasMap(keyTypes, issuers);
            return this.aliasStrategy.chooseAlias(validAliases, null);
        }

        @Override
        public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine sslEngine) {
            Map<String, PrivateKeyDetails> validAliases = this.getServerAliasMap(keyType, issuers);
            return this.aliasStrategy.chooseAlias(validAliases, null);
        }
    }

    static class TrustManagerDelegate
    implements X509TrustManager {
        private final X509TrustManager trustManager;
        private final TrustStrategy trustStrategy;

        TrustManagerDelegate(X509TrustManager trustManager, TrustStrategy trustStrategy) {
            this.trustManager = trustManager;
            this.trustStrategy = trustStrategy;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            this.trustManager.checkClientTrusted(chain, authType);
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (!this.trustStrategy.isTrusted(chain, authType)) {
                this.trustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return this.trustManager.getAcceptedIssuers();
        }
    }
}

