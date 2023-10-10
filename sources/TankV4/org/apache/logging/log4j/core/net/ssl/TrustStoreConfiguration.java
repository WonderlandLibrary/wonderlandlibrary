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

@Plugin(
   name = "trustStore",
   category = "Core",
   printObject = true
)
public class TrustStoreConfiguration extends StoreConfiguration {
   private KeyStore trustStore = null;
   private String trustStoreType = "JKS";

   public TrustStoreConfiguration(String var1, String var2) {
      super(var1, var2);
   }

   protected void load() throws StoreConfigurationException {
      KeyStore var1 = null;
      FileInputStream var2 = null;
      LOGGER.debug("Loading truststore from file with params(location={})", new Object[]{this.getLocation()});

      try {
         if (this.getLocation() == null) {
            throw new IOException("The location is null");
         }

         var1 = KeyStore.getInstance(this.trustStoreType);
         var2 = new FileInputStream(this.getLocation());
         var1.load(var2, this.getPasswordAsCharArray());
      } catch (CertificateException var7) {
         LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[]{this.trustStoreType});
         throw new StoreConfigurationException(var7);
      } catch (NoSuchAlgorithmException var8) {
         LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
         throw new StoreConfigurationException(var8);
      } catch (KeyStoreException var9) {
         LOGGER.error(var9);
         throw new StoreConfigurationException(var9);
      } catch (FileNotFoundException var10) {
         LOGGER.error("The keystore file({}) is not found", new Object[]{this.getLocation()});
         throw new StoreConfigurationException(var10);
      } catch (IOException var11) {
         LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", new Object[]{var11.getMessage()});
         throw new StoreConfigurationException(var11);
      }

      try {
         if (var2 != null) {
            var2.close();
         }
      } catch (Exception var6) {
         LOGGER.warn("Error closing {}", new Object[]{this.getLocation(), var6});
      }

      this.trustStore = var1;
      LOGGER.debug("Truststore successfully loaded with params(location={})", new Object[]{this.getLocation()});
   }

   public KeyStore getTrustStore() throws StoreConfigurationException {
      if (this.trustStore == null) {
         this.load();
      }

      return this.trustStore;
   }

   @PluginFactory
   public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute("location") String var0, @PluginAttribute("password") String var1) {
      return new TrustStoreConfiguration(var0, var1);
   }
}
