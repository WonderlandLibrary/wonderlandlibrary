package org.apache.logging.log4j.core.config;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;

public class JSONConfiguration extends BaseConfiguration implements Reconfigurable {
   private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
   private static final int BUF_SIZE = 16384;
   private final List status = new ArrayList();
   private JsonNode root;
   private final List messages = new ArrayList();
   private final File configFile;

   public JSONConfiguration(ConfigurationFactory.ConfigurationSource var1) {
      this.configFile = var1.getFile();

      try {
         InputStream var3 = var1.getInputStream();
         byte[] var2 = this.toByteArray(var3);
         var3.close();
         ByteArrayInputStream var4 = new ByteArrayInputStream(var2);
         ObjectMapper var5 = (new ObjectMapper()).configure(Feature.ALLOW_COMMENTS, true);
         this.root = var5.readTree(var4);
         if (this.root.size() == 1) {
            Iterator var6 = this.root.elements();
            this.root = (JsonNode)var6.next();
         }

         this.processAttributes(this.rootNode, this.root);
         Level var18 = this.getDefaultStatus();
         boolean var7 = false;
         PrintStream var8 = System.out;
         Iterator var9 = this.rootNode.getAttributes().entrySet().iterator();

         while(true) {
            String var26;
            while(var9.hasNext()) {
               Entry var10 = (Entry)var9.next();
               if ("status".equalsIgnoreCase((String)var10.getKey())) {
                  var18 = Level.toLevel(this.getStrSubstitutor().replace((String)var10.getValue()), (Level)null);
                  if (var18 == null) {
                     var18 = Level.ERROR;
                     this.messages.add("Invalid status specified: " + (String)var10.getValue() + ". Defaulting to ERROR");
                  }
               } else {
                  String var21;
                  if ("dest".equalsIgnoreCase((String)var10.getKey())) {
                     var21 = (String)var10.getValue();
                     if (var21 != null) {
                        if (var21.equalsIgnoreCase("err")) {
                           var8 = System.err;
                        } else {
                           try {
                              File var23 = FileUtils.fileFromURI(new URI(var21));
                              var26 = Charset.defaultCharset().name();
                              var8 = new PrintStream(new FileOutputStream(var23), true, var26);
                           } catch (URISyntaxException var16) {
                              System.err.println("Unable to write to " + var21 + ". Writing to stdout");
                           }
                        }
                     }
                  } else if ("shutdownHook".equalsIgnoreCase((String)var10.getKey())) {
                     var21 = this.getStrSubstitutor().replace((String)var10.getValue());
                     this.isShutdownHookEnabled = !var21.equalsIgnoreCase("disable");
                  } else if ("verbose".equalsIgnoreCase((String)var10.getKey())) {
                     var7 = Boolean.parseBoolean(this.getStrSubstitutor().replace((String)var10.getValue()));
                  } else if ("packages".equalsIgnoreCase((String)var10.getKey())) {
                     String[] var20 = this.getStrSubstitutor().replace((String)var10.getValue()).split(",");
                     String[] var12 = var20;
                     int var13 = var20.length;

                     for(int var14 = 0; var14 < var13; ++var14) {
                        String var15 = var12[var14];
                        PluginManager.addPackage(var15);
                     }
                  } else if ("name".equalsIgnoreCase((String)var10.getKey())) {
                     this.setName(this.getStrSubstitutor().replace((String)var10.getValue()));
                  } else if ("monitorInterval".equalsIgnoreCase((String)var10.getKey())) {
                     int var11 = Integer.parseInt(this.getStrSubstitutor().replace((String)var10.getValue()));
                     if (var11 > 0 && this.configFile != null) {
                        this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, var11);
                     }
                  } else if ("advertiser".equalsIgnoreCase((String)var10.getKey())) {
                     this.createAdvertiser(this.getStrSubstitutor().replace((String)var10.getValue()), var1, var2, "application/json");
                  }
               }
            }

            var9 = ((StatusLogger)LOGGER).getListeners();
            boolean var19 = false;

            while(var9.hasNext()) {
               StatusListener var22 = (StatusListener)var9.next();
               if (var22 instanceof StatusConsoleListener) {
                  var19 = true;
                  ((StatusConsoleListener)var22).setLevel(var18);
                  if (!var7) {
                     ((StatusConsoleListener)var22).setFilters(VERBOSE_CLASSES);
                  }
               }
            }

            if (!var19 && var18 != Level.OFF) {
               StatusConsoleListener var24 = new StatusConsoleListener(var18, var8);
               if (!var7) {
                  var24.setFilters(VERBOSE_CLASSES);
               }

               ((StatusLogger)LOGGER).registerListener(var24);
               Iterator var25 = this.messages.iterator();

               while(var25.hasNext()) {
                  var26 = (String)var25.next();
                  LOGGER.error(var26);
               }
            }

            if (this.getName() == null) {
               this.setName(var1.getLocation());
            }
            break;
         }
      } catch (Exception var17) {
         LOGGER.error((String)("Error parsing " + var1.getLocation()), (Throwable)var17);
         var17.printStackTrace();
      }

   }

   public void stop() {
      super.stop();
   }

   public void setup() {
      Iterator var1 = this.root.fields();
      List var2 = this.rootNode.getChildren();

      while(var1.hasNext()) {
         Entry var3 = (Entry)var1.next();
         JsonNode var4 = (JsonNode)var3.getValue();
         if (var4.isObject()) {
            LOGGER.debug("Processing node for object " + (String)var3.getKey());
            var2.add(this.constructNode((String)var3.getKey(), this.rootNode, var4));
         } else if (var4.isArray()) {
            LOGGER.error("Arrays are not supported at the root configuration.");
         }
      }

      LOGGER.debug("Completed parsing configuration");
      if (this.status.size() > 0) {
         Iterator var5 = this.status.iterator();

         while(var5.hasNext()) {
            JSONConfiguration.Status var6 = (JSONConfiguration.Status)var5.next();
            LOGGER.error("Error processing element " + JSONConfiguration.Status.access$000(var6) + ": " + JSONConfiguration.Status.access$100(var6));
         }

      }
   }

   public Configuration reconfigure() {
      if (this.configFile != null) {
         try {
            ConfigurationFactory.ConfigurationSource var1 = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
            return new JSONConfiguration(var1);
         } catch (FileNotFoundException var2) {
            LOGGER.error((String)("Cannot locate file " + this.configFile), (Throwable)var2);
         }
      }

      return null;
   }

   private Node constructNode(String var1, Node var2, JsonNode var3) {
      PluginType var4 = this.pluginManager.getPluginType(var1);
      Node var5 = new Node(var2, var1, var4);
      this.processAttributes(var5, var3);
      Iterator var6 = var3.fields();
      List var7 = var5.getChildren();

      while(true) {
         while(true) {
            Entry var8;
            JsonNode var9;
            do {
               if (!var6.hasNext()) {
                  String var17;
                  if (var4 == null) {
                     var17 = "null";
                  } else {
                     var17 = var4.getElementName() + ":" + var4.getPluginClass();
                  }

                  String var18 = var5.getParent() == null ? "null" : (var5.getParent().getName() == null ? "root" : var5.getParent().getName());
                  LOGGER.debug("Returning " + var5.getName() + " with parent " + var18 + " of type " + var17);
                  return var5;
               }

               var8 = (Entry)var6.next();
               var9 = (JsonNode)var8.getValue();
            } while(!var9.isArray() && !var9.isObject());

            if (var4 == null) {
               this.status.add(new JSONConfiguration.Status(this, var1, var9, JSONConfiguration.ErrorType.CLASS_NOT_FOUND));
            }

            if (var9.isArray()) {
               LOGGER.debug("Processing node for array " + (String)var8.getKey());

               for(int var10 = 0; var10 < var9.size(); ++var10) {
                  String var11 = this.getType(var9.get(var10), (String)var8.getKey());
                  PluginType var12 = this.pluginManager.getPluginType(var11);
                  Node var13 = new Node(var5, (String)var8.getKey(), var12);
                  this.processAttributes(var13, var9.get(var10));
                  if (var11.equals(var8.getKey())) {
                     LOGGER.debug("Processing " + (String)var8.getKey() + "[" + var10 + "]");
                  } else {
                     LOGGER.debug("Processing " + var11 + " " + (String)var8.getKey() + "[" + var10 + "]");
                  }

                  Iterator var14 = var9.get(var10).fields();
                  List var15 = var13.getChildren();

                  while(var14.hasNext()) {
                     Entry var16 = (Entry)var14.next();
                     if (((JsonNode)var16.getValue()).isObject()) {
                        LOGGER.debug("Processing node for object " + (String)var16.getKey());
                        var15.add(this.constructNode((String)var16.getKey(), var13, (JsonNode)var16.getValue()));
                     }
                  }

                  var7.add(var13);
               }
            } else {
               LOGGER.debug("Processing node for object " + (String)var8.getKey());
               var7.add(this.constructNode((String)var8.getKey(), var5, var9));
            }
         }
      }
   }

   private String getType(JsonNode var1, String var2) {
      Iterator var3 = var1.fields();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         if (((String)var4.getKey()).equalsIgnoreCase("type")) {
            JsonNode var5 = (JsonNode)var4.getValue();
            if (var5.isValueNode()) {
               return var5.asText();
            }
         }
      }

      return var2;
   }

   private void processAttributes(Node var1, JsonNode var2) {
      Map var3 = var1.getAttributes();
      Iterator var4 = var2.fields();

      while(var4.hasNext()) {
         Entry var5 = (Entry)var4.next();
         if (!((String)var5.getKey()).equalsIgnoreCase("type")) {
            JsonNode var6 = (JsonNode)var5.getValue();
            if (var6.isValueNode()) {
               var3.put(var5.getKey(), var6.asText());
            }
         }
      }

   }

   protected byte[] toByteArray(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var4 = new byte[16384];

      int var3;
      while((var3 = var1.read(var4, 0, var4.length)) != -1) {
         var2.write(var4, 0, var3);
      }

      return var2.toByteArray();
   }

   private class Status {
      private final JsonNode node;
      private final String name;
      private final JSONConfiguration.ErrorType errorType;
      final JSONConfiguration this$0;

      public Status(JSONConfiguration var1, String var2, JsonNode var3, JSONConfiguration.ErrorType var4) {
         this.this$0 = var1;
         this.name = var2;
         this.node = var3;
         this.errorType = var4;
      }

      static String access$000(JSONConfiguration.Status var0) {
         return var0.name;
      }

      static JSONConfiguration.ErrorType access$100(JSONConfiguration.Status var0) {
         return var0.errorType;
      }
   }

   private static enum ErrorType {
      CLASS_NOT_FOUND;

      private static final JSONConfiguration.ErrorType[] $VALUES = new JSONConfiguration.ErrorType[]{CLASS_NOT_FOUND};
   }
}
