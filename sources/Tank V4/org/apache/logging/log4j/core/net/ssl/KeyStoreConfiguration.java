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
   name = "keyStore",
   category = "Core",
   printObject = true
)
public class KeyStoreConfiguration extends StoreConfiguration {
   private KeyStore keyStore = null;
   private String keyStoreType = "JKS";

   public KeyStoreConfiguration(String var1, String var2) {
      super(var1, var2);
   }

   protected void load() throws StoreConfigurationException {
      FileInputStream var1 = null;
      LOGGER.debug("Loading keystore from file with params(location={})", new Object[]{this.getLocation()});

      try {
         if (this.getLocation() == null) {
            throw new IOException("The location is null");
         }

         var1 = new FileInputStream(this.getLocation());
         KeyStore var2 = KeyStore.getInstance(this.keyStoreType);
         var2.load(var1, this.getPasswordAsCharArray());
         this.keyStore = var2;
      } catch (CertificateException var6) {
         LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[]{this.keyStoreType});
         throw new StoreConfigurationException(var6);
      } catch (NoSuchAlgorithmException var7) {
         LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
         throw new StoreConfigurationException(var7);
      } catch (KeyStoreException var8) {
         LOGGER.error(var8);
         throw new StoreConfigurationException(var8);
      } catch (FileNotFoundException var9) {
         LOGGER.error("The keystore file({}) is not found", new Object[]{this.getLocation()});
         throw new StoreConfigurationException(var9);
      } catch (IOException var10) {
         LOGGER.error("Something is wrong with the format of the keystore or the given password");
         throw new StoreConfigurationException(var10);
      }

      try {
         if (var1 != null) {
            var1.close();
         }
      } catch (IOException var5) {
      }

      LOGGER.debug("Keystore successfully loaded with params(location={})", new Object[]{this.getLocation()});
   }

   public KeyStore getKeyStore() throws StoreConfigurationException {
      if (this.keyStore == null) {
         this.load();
      }

      return this.keyStore;
   }

   @PluginFactory
   public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String var0, @PluginAttribute("password") String var1) {
      return new KeyStoreConfiguration(var0, var1);
   }
}
