package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
   name = "ssl",
   category = "Core",
   printObject = true
)
public class SSLConfiguration {
   private static final StatusLogger LOGGER = StatusLogger.getLogger();
   private KeyStoreConfiguration keyStoreConfig;
   private TrustStoreConfiguration trustStoreConfig;
   private SSLContext sslContext;

   private SSLConfiguration(KeyStoreConfiguration var1, TrustStoreConfiguration var2) {
      this.keyStoreConfig = var1;
      this.trustStoreConfig = var2;
      this.sslContext = null;
   }

   public SSLSocketFactory getSSLSocketFactory() {
      if (this.sslContext == null) {
         this.sslContext = this.createSSLContext();
      }

      return this.sslContext.getSocketFactory();
   }

   public SSLServerSocketFactory getSSLServerSocketFactory() {
      if (this.sslContext == null) {
         this.sslContext = this.createSSLContext();
      }

      return this.sslContext.getServerSocketFactory();
   }

   private SSLContext createSSLContext() {
      SSLContext var1 = null;

      try {
         var1 = this.createSSLContextBasedOnConfiguration();
         LOGGER.debug("Creating SSLContext with the given parameters");
      } catch (TrustStoreConfigurationException var3) {
         var1 = this.createSSLContextWithTrustStoreFailure();
      } catch (KeyStoreConfigurationException var4) {
         var1 = this.createSSLContextWithKeyStoreFailure();
      }

      return var1;
   }

   private SSLContext createSSLContextWithTrustStoreFailure() {
      SSLContext var1;
      try {
         var1 = this.createSSLContextWithDefaultTrustManagerFactory();
         LOGGER.debug("Creating SSLContext with default truststore");
      } catch (KeyStoreConfigurationException var3) {
         var1 = this.createDefaultSSLContext();
         LOGGER.debug("Creating SSLContext with default configuration");
      }

      return var1;
   }

   private SSLContext createSSLContextWithKeyStoreFailure() {
      SSLContext var1;
      try {
         var1 = this.createSSLContextWithDefaultKeyManagerFactory();
         LOGGER.debug("Creating SSLContext with default keystore");
      } catch (TrustStoreConfigurationException var3) {
         var1 = this.createDefaultSSLContext();
         LOGGER.debug("Creating SSLContext with default configuration");
      }

      return var1;
   }

   private SSLContext createSSLContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
      return this.createSSLContext(false, false);
   }

   private SSLContext createSSLContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
      try {
         return this.createSSLContext(true, false);
      } catch (KeyStoreConfigurationException var2) {
         LOGGER.debug("Exception occured while using default keystore. This should be a BUG");
         return null;
      }
   }

   private SSLContext createSSLContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
      try {
         return this.createSSLContext(false, true);
      } catch (TrustStoreConfigurationException var2) {
         LOGGER.debug("Exception occured while using default truststore. This should be a BUG");
         return null;
      }
   }

   private SSLContext createDefaultSSLContext() {
      try {
         return SSLContext.getDefault();
      } catch (NoSuchAlgorithmException var2) {
         LOGGER.error("Failed to create an SSLContext with default configuration");
         return null;
      }
   }

   private SSLContext createSSLContext(boolean var1, boolean var2) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
      try {
         KeyManager[] var3 = null;
         TrustManager[] var4 = null;
         SSLContext var5 = SSLContext.getInstance("SSL");
         if (!var1) {
            KeyManagerFactory var6 = this.loadKeyManagerFactory();
            var3 = var6.getKeyManagers();
         }

         if (!var2) {
            TrustManagerFactory var9 = this.loadTrustManagerFactory();
            var4 = var9.getTrustManagers();
         }

         var5.init(var3, var4, (SecureRandom)null);
         return var5;
      } catch (NoSuchAlgorithmException var7) {
         LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol");
         throw new TrustStoreConfigurationException(var7);
      } catch (KeyManagementException var8) {
         LOGGER.error("Failed to initialize the SSLContext");
         throw new KeyStoreConfigurationException(var8);
      }
   }

   private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
      KeyStore var1 = null;
      TrustManagerFactory var2 = null;
      if (this.trustStoreConfig == null) {
         throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
      } else {
         try {
            var1 = this.trustStoreConfig.getTrustStore();
            var2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var2.init(var1);
            return var2;
         } catch (NoSuchAlgorithmException var4) {
            LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new TrustStoreConfigurationException(var4);
         } catch (KeyStoreException var5) {
            LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new TrustStoreConfigurationException(var5);
         } catch (StoreConfigurationException var6) {
            throw new TrustStoreConfigurationException(var6);
         }
      }
   }

   private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
      KeyStore var1 = null;
      KeyManagerFactory var2 = null;
      if (this.keyStoreConfig == null) {
         throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
      } else {
         try {
            var1 = this.keyStoreConfig.getKeyStore();
            var2 = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            var2.init(var1, this.keyStoreConfig.getPasswordAsCharArray());
            return var2;
         } catch (NoSuchAlgorithmException var4) {
            LOGGER.error("The specified algorithm is not available from the specified provider");
            throw new KeyStoreConfigurationException(var4);
         } catch (KeyStoreException var5) {
            LOGGER.error("Failed to initialize the TrustManagerFactory");
            throw new KeyStoreConfigurationException(var5);
         } catch (StoreConfigurationException var6) {
            throw new KeyStoreConfigurationException(var6);
         } catch (UnrecoverableKeyException var7) {
            LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)");
            throw new KeyStoreConfigurationException(var7);
         }
      }
   }

   public boolean equals(SSLConfiguration var1) {
      if (var1 == null) {
         return false;
      } else {
         boolean var2 = false;
         boolean var3 = false;
         if (this.keyStoreConfig != null) {
            var2 = this.keyStoreConfig.equals(var1.keyStoreConfig);
         } else {
            var2 = this.keyStoreConfig == var1.keyStoreConfig;
         }

         if (this.trustStoreConfig != null) {
            var3 = this.trustStoreConfig.equals(var1.trustStoreConfig);
         } else {
            var3 = this.trustStoreConfig == var1.trustStoreConfig;
         }

         return var2 && var3;
      }
   }

   @PluginFactory
   public static SSLConfiguration createSSLConfiguration(@PluginElement("keyStore") KeyStoreConfiguration var0, @PluginElement("trustStore") TrustStoreConfiguration var1) {
      return new SSLConfiguration(var0, var1);
   }
}
