package team.massacre.api.manager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.io.FilenameUtils;

public final class ConfigManager extends Manager<Config> {
   public static final File CONFIGS_DIR = new File("Massacre", "configs");
   public static final String EXTENSION = ".json";

   public ConfigManager() {
      super(loadConfigs());
      if (!CONFIGS_DIR.exists()) {
         boolean var1 = CONFIGS_DIR.mkdirs();
      }

   }

   public boolean loadConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config = this.findConfig(configName);
         if (config == null) {
            return false;
         } else {
            try {
               FileReader reader = new FileReader(config.getFile());
               JsonParser parser = new JsonParser();
               JsonObject object = (JsonObject)parser.parse(reader);
               config.load(object);
               return true;
            } catch (FileNotFoundException var6) {
               return false;
            }
         }
      }
   }

   public boolean saveConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config;
         if ((config = this.findConfig(configName)) == null) {
            Config newConfig = config = new Config(configName);
            this.getElements().add(newConfig);
         }

         return GsonUtils.write(config.getFile(), config.save());
      }
   }

   public Config findConfig(String configName) {
      if (configName == null) {
         return null;
      } else {
         Iterator var2 = this.getElements().iterator();

         Config config;
         do {
            if (!var2.hasNext()) {
               if ((new File(CONFIGS_DIR, configName + ".json")).exists()) {
                  return new Config(configName);
               }

               return null;
            }

            config = (Config)var2.next();
         } while(!config.getName().equalsIgnoreCase(configName));

         return config;
      }
   }

   public boolean deleteConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config;
         if ((config = this.findConfig(configName)) == null) {
            return false;
         } else {
            File f = config.getFile();
            this.getElements().remove(config);
            return f.exists() && f.delete();
         }
      }
   }

   private static ArrayList<Config> loadConfigs() {
      ArrayList<Config> loadedConfigs = new ArrayList();
      File[] files = CONFIGS_DIR.listFiles();
      if (files != null) {
         File[] var2 = files;
         int var3 = files.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File file = var2[var4];
            if (FilenameUtils.getExtension(file.getName()).equals("json")) {
               loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
            }
         }
      }

      return loadedConfigs;
   }
}
