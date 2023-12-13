package org.apache.logging.log4j.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public abstract class ConfigurationFactory {
   public static final String CONFIGURATION_FACTORY_PROPERTY = "log4j.configurationFactory";
   public static final String CONFIGURATION_FILE_PROPERTY = "log4j.configurationFile";
   protected static final Logger LOGGER = StatusLogger.getLogger();
   protected static final String TEST_PREFIX = "log4j2-test";
   protected static final String DEFAULT_PREFIX = "log4j2";
   private static final String CLASS_LOADER_SCHEME = "classloader";
   private static final int CLASS_LOADER_SCHEME_LENGTH = 12;
   private static final String CLASS_PATH_SCHEME = "classpath";
   private static final int CLASS_PATH_SCHEME_LENGTH = 10;
   private static volatile List factories = null;
   private static ConfigurationFactory configFactory = new ConfigurationFactory.Factory();
   protected final StrSubstitutor substitutor = new StrSubstitutor(new Interpolator());

   public static ConfigurationFactory getInstance() {
      if (factories == null) {
         String var0 = "log4j2-test";
         synchronized("log4j2-test"){}
         if (factories == null) {
            ArrayList var1 = new ArrayList();
            String var2 = PropertiesUtil.getProperties().getStringProperty("log4j.configurationFactory");
            if (var2 != null) {
               addFactory(var1, (String)var2);
            }

            PluginManager var3 = new PluginManager("ConfigurationFactory");
            var3.collectPlugins();
            Map var4 = var3.getPlugins();
            TreeSet var5 = new TreeSet();
            Iterator var6 = var4.values().iterator();

            while(var6.hasNext()) {
               PluginType var7 = (PluginType)var6.next();

               try {
                  Class var8 = var7.getPluginClass();
                  Order var9 = (Order)var8.getAnnotation(Order.class);
                  if (var9 != null) {
                     int var10 = var9.value();
                     var5.add(new ConfigurationFactory.WeightedFactory(var10, var8));
                  }
               } catch (Exception var12) {
                  LOGGER.warn("Unable to add class " + var7.getPluginClass());
               }
            }

            var6 = var5.iterator();

            while(var6.hasNext()) {
               ConfigurationFactory.WeightedFactory var13 = (ConfigurationFactory.WeightedFactory)var6.next();
               addFactory(var1, (Class)ConfigurationFactory.WeightedFactory.access$100(var13));
            }

            factories = Collections.unmodifiableList(var1);
         }
      }

      return configFactory;
   }

   private static void addFactory(List var0, String var1) {
      try {
         addFactory(var0, Class.forName(var1));
      } catch (ClassNotFoundException var3) {
         LOGGER.error((String)("Unable to load class " + var1), (Throwable)var3);
      } catch (Exception var4) {
         LOGGER.error((String)("Unable to load class " + var1), (Throwable)var4);
      }

   }

   private static void addFactory(List var0, Class var1) {
      try {
         var0.add(var1.newInstance());
      } catch (Exception var3) {
         LOGGER.error((String)("Unable to create instance of " + var1.getName()), (Throwable)var3);
      }

   }

   public static void setConfigurationFactory(ConfigurationFactory var0) {
      configFactory = var0;
   }

   public static void resetConfigurationFactory() {
      configFactory = new ConfigurationFactory.Factory();
   }

   public static void removeConfigurationFactory(ConfigurationFactory var0) {
      if (configFactory == var0) {
         configFactory = new ConfigurationFactory.Factory();
      }

   }

   protected abstract String[] getSupportedTypes();

   protected boolean isActive() {
      return true;
   }

   public abstract Configuration getConfiguration(ConfigurationFactory.ConfigurationSource var1);

   public Configuration getConfiguration(String var1, URI var2) {
      if (!this.isActive()) {
         return null;
      } else {
         if (var2 != null) {
            ConfigurationFactory.ConfigurationSource var3 = this.getInputFromURI(var2);
            if (var3 != null) {
               return this.getConfiguration(var3);
            }
         }

         return null;
      }
   }

   protected ConfigurationFactory.ConfigurationSource getInputFromURI(URI var1) {
      File var2 = FileUtils.fileFromURI(var1);
      if (var2 != null && var2.exists() && var2.canRead()) {
         try {
            return new ConfigurationFactory.ConfigurationSource(new FileInputStream(var2), var2);
         } catch (FileNotFoundException var12) {
            LOGGER.error((String)("Cannot locate file " + var1.getPath()), (Throwable)var12);
         }
      }

      String var3 = var1.getScheme();
      boolean var4 = var3 != null && var3.equals("classloader");
      boolean var5 = var3 != null && !var4 && var3.equals("classpath");
      if (var3 == null || var4 || var5) {
         ClassLoader var6 = this.getClass().getClassLoader();
         String var7;
         if (var4) {
            var7 = var1.toString().substring(CLASS_LOADER_SCHEME_LENGTH);
         } else if (var5) {
            var7 = var1.toString().substring(CLASS_PATH_SCHEME_LENGTH);
         } else {
            var7 = var1.getPath();
         }

         ConfigurationFactory.ConfigurationSource var8 = this.getInputFromResource(var7, var6);
         if (var8 != null) {
            return var8;
         }
      }

      try {
         return new ConfigurationFactory.ConfigurationSource(var1.toURL().openStream(), var1.getPath());
      } catch (MalformedURLException var9) {
         LOGGER.error((String)("Invalid URL " + var1.toString()), (Throwable)var9);
      } catch (IOException var10) {
         LOGGER.error((String)("Unable to access " + var1.toString()), (Throwable)var10);
      } catch (Exception var11) {
         LOGGER.error((String)("Unable to access " + var1.toString()), (Throwable)var11);
      }

      return null;
   }

   protected ConfigurationFactory.ConfigurationSource getInputFromString(String var1, ClassLoader var2) {
      try {
         URL var3 = new URL(var1);
         return new ConfigurationFactory.ConfigurationSource(var3.openStream(), FileUtils.fileFromURI(var3.toURI()));
      } catch (Exception var7) {
         ConfigurationFactory.ConfigurationSource var4 = this.getInputFromResource(var1, var2);
         if (var4 == null) {
            try {
               File var5 = new File(var1);
               return new ConfigurationFactory.ConfigurationSource(new FileInputStream(var5), var5);
            } catch (FileNotFoundException var6) {
            }
         }

         return var4;
      }
   }

   protected ConfigurationFactory.ConfigurationSource getInputFromResource(String var1, ClassLoader var2) {
      URL var3 = Loader.getResource(var1, var2);
      if (var3 == null) {
         return null;
      } else {
         InputStream var4 = null;

         try {
            var4 = var3.openStream();
         } catch (IOException var6) {
            return null;
         }

         if (var4 == null) {
            return null;
         } else {
            if (FileUtils.isFile(var3)) {
               try {
                  return new ConfigurationFactory.ConfigurationSource(var4, FileUtils.fileFromURI(var3.toURI()));
               } catch (URISyntaxException var7) {
               }
            }

            return new ConfigurationFactory.ConfigurationSource(var4, var1);
         }
      }
   }

   static List access$200() {
      return factories;
   }

   public static class ConfigurationSource {
      private File file;
      private String location;
      private InputStream stream;

      public ConfigurationSource() {
      }

      public ConfigurationSource(InputStream var1) {
         this.stream = var1;
         this.file = null;
         this.location = null;
      }

      public ConfigurationSource(InputStream var1, File var2) {
         this.stream = var1;
         this.file = var2;
         this.location = var2.getAbsolutePath();
      }

      public ConfigurationSource(InputStream var1, String var2) {
         this.stream = var1;
         this.location = var2;
         this.file = null;
      }

      public File getFile() {
         return this.file;
      }

      public void setFile(File var1) {
         this.file = var1;
      }

      public String getLocation() {
         return this.location;
      }

      public void setLocation(String var1) {
         this.location = var1;
      }

      public InputStream getInputStream() {
         return this.stream;
      }

      public void setInputStream(InputStream var1) {
         this.stream = var1;
      }
   }

   private static class Factory extends ConfigurationFactory {
      private Factory() {
      }

      public Configuration getConfiguration(String var1, URI var2) {
         if (var2 == null) {
            String var3 = this.substitutor.replace(PropertiesUtil.getProperties().getStringProperty("log4j.configurationFile"));
            if (var3 != null) {
               ConfigurationFactory.ConfigurationSource var4 = null;

               try {
                  var4 = this.getInputFromURI(new URI(var3));
               } catch (Exception var13) {
               }

               if (var4 == null) {
                  ClassLoader var5 = this.getClass().getClassLoader();
                  var4 = this.getInputFromString(var3, var5);
               }

               if (var4 != null) {
                  Iterator var16 = ConfigurationFactory.access$200().iterator();

                  label94:
                  while(true) {
                     ConfigurationFactory var6;
                     String[] var7;
                     do {
                        if (!var16.hasNext()) {
                           break label94;
                        }

                        var6 = (ConfigurationFactory)var16.next();
                        var7 = var6.getSupportedTypes();
                     } while(var7 == null);

                     String[] var8 = var7;
                     int var9 = var7.length;

                     for(int var10 = 0; var10 < var9; ++var10) {
                        String var11 = var8[var10];
                        if (var11.equals("*") || var3.endsWith(var11)) {
                           Configuration var12 = var6.getConfiguration(var4);
                           if (var12 != null) {
                              return var12;
                           }
                        }
                     }
                  }
               }
            }
         } else {
            Iterator var14 = ConfigurationFactory.access$200().iterator();

            label75:
            while(true) {
               ConfigurationFactory var17;
               String[] var18;
               do {
                  if (!var14.hasNext()) {
                     break label75;
                  }

                  var17 = (ConfigurationFactory)var14.next();
                  var18 = var17.getSupportedTypes();
               } while(var18 == null);

               String[] var19 = var18;
               int var20 = var18.length;

               for(int var21 = 0; var21 < var20; ++var21) {
                  String var22 = var19[var21];
                  if (var22.equals("*") || var2.toString().endsWith(var22)) {
                     Configuration var23 = var17.getConfiguration(var1, var2);
                     if (var23 != null) {
                        return var23;
                     }
                  }
               }
            }
         }

         Configuration var15 = this.getConfiguration(true, var1);
         if (var15 == null) {
            var15 = this.getConfiguration(true, (String)null);
            if (var15 == null) {
               var15 = this.getConfiguration(false, var1);
               if (var15 == null) {
                  var15 = this.getConfiguration(false, (String)null);
               }
            }
         }

         return (Configuration)(var15 != null ? var15 : new DefaultConfiguration());
      }

      private Configuration getConfiguration(boolean var1, String var2) {
         boolean var3 = var2 != null && var2.length() > 0;
         ClassLoader var4 = this.getClass().getClassLoader();
         Iterator var5 = ConfigurationFactory.access$200().iterator();

         while(true) {
            ConfigurationFactory var6;
            String var8;
            String[] var9;
            do {
               if (!var5.hasNext()) {
                  return null;
               }

               var6 = (ConfigurationFactory)var5.next();
               var8 = var1 ? "log4j2-test" : "log4j2";
               var9 = var6.getSupportedTypes();
            } while(var9 == null);

            String[] var10 = var9;
            int var11 = var9.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               String var13 = var10[var12];
               if (!var13.equals("*")) {
                  String var7 = var3 ? var8 + var2 + var13 : var8 + var13;
                  ConfigurationFactory.ConfigurationSource var14 = this.getInputFromResource(var7, var4);
                  if (var14 != null) {
                     return var6.getConfiguration(var14);
                  }
               }
            }
         }
      }

      public String[] getSupportedTypes() {
         return null;
      }

      public Configuration getConfiguration(ConfigurationFactory.ConfigurationSource var1) {
         if (var1 != null) {
            String var2 = var1.getLocation();
            Iterator var3 = ConfigurationFactory.access$200().iterator();

            label41:
            while(true) {
               ConfigurationFactory var4;
               String[] var5;
               do {
                  if (!var3.hasNext()) {
                     break label41;
                  }

                  var4 = (ConfigurationFactory)var3.next();
                  var5 = var4.getSupportedTypes();
               } while(var5 == null);

               String[] var6 = var5;
               int var7 = var5.length;

               for(int var8 = 0; var8 < var7; ++var8) {
                  String var9 = var6[var8];
                  if (var9.equals("*") || var2 != null && var2.endsWith(var9)) {
                     Configuration var10 = var4.getConfiguration(var1);
                     if (var10 != null) {
                        return var10;
                     } else {
                        LOGGER.error("Cannot determine the ConfigurationFactory to use for {}", var2);
                        return null;
                     }
                  }
               }
            }
         }

         LOGGER.error("Cannot process configuration, input source is null");
         return null;
      }

      Factory(Object var1) {
         this();
      }
   }

   private static class WeightedFactory implements Comparable {
      private final int weight;
      private final Class factoryClass;

      public WeightedFactory(int var1, Class var2) {
         this.weight = var1;
         this.factoryClass = var2;
      }

      public int compareTo(ConfigurationFactory.WeightedFactory var1) {
         int var2 = var1.weight;
         if (this.weight == var2) {
            return 0;
         } else {
            return this.weight > var2 ? -1 : 1;
         }
      }

      public int compareTo(Object var1) {
         return this.compareTo((ConfigurationFactory.WeightedFactory)var1);
      }

      static Class access$100(ConfigurationFactory.WeightedFactory var0) {
         return var0.factoryClass;
      }
   }
}
