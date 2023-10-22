package cn.hanabi.utils.jprocess.jpowershell;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

final class PowerShellConfig {
   private static final String CONFIG_FILENAME = "jpowershell.properties";
   private static Properties config;

   public static Properties getConfig() {
      if (config == null) {
         config = new Properties();

         try {
            config.load(PowerShellConfig.class.getClassLoader().getResourceAsStream("jpowershell.properties"));
         } catch (IOException var1) {
            Logger.getLogger(PowerShell.class.getName()).log(Level.SEVERE, "Cannot read config values from file:jpowershell.properties", var1);
         }
      }

      return config;
   }
}
