package org.apache.logging.log4j.core.config.plugins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Closer;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;

public class PluginManager {
   private static final long NANOS_PER_SECOND = 1000000000L;
   private static ConcurrentMap pluginTypeMap = new ConcurrentHashMap();
   private static final CopyOnWriteArrayList PACKAGES = new CopyOnWriteArrayList();
   private static final String PATH = "org/apache/logging/log4j/core/config/plugins/";
   private static final String FILENAME = "Log4j2Plugins.dat";
   private static final String LOG4J_PACKAGES = "org.apache.logging.log4j.core";
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static String rootDir;
   private Map plugins = new HashMap();
   private final String type;
   private final Class clazz;

   public PluginManager(String var1) {
      this.type = var1;
      this.clazz = null;
   }

   public PluginManager(String var1, Class var2) {
      this.type = var1;
      this.clazz = var2;
   }

   public static void main(String[] var0) throws Exception {
      if (var0 == null || var0.length < 1) {
         System.err.println("A target directory must be specified");
         System.exit(-1);
      }

      rootDir = !var0[0].endsWith("/") && !var0[0].endsWith("\\") ? var0[0] + "/" : var0[0];
      PluginManager var1 = new PluginManager("Core");
      String var2 = var0.length == 2 ? var0[1] : null;
      var1.collectPlugins(false, var2);
      encode(pluginTypeMap);
   }

   public static void addPackage(String var0) {
      if (PACKAGES.addIfAbsent(var0)) {
         pluginTypeMap.clear();
      }

   }

   public PluginType getPluginType(String var1) {
      return (PluginType)this.plugins.get(var1.toLowerCase());
   }

   public Map getPlugins() {
      return this.plugins;
   }

   public void collectPlugins() {
      this.collectPlugins(true, (String)null);
   }

   public void collectPlugins(boolean var1, String var2) {
      if (pluginTypeMap.containsKey(this.type)) {
         this.plugins = (Map)pluginTypeMap.get(this.type);
         var1 = false;
      }

      long var3 = System.nanoTime();
      ResolverUtil var5 = new ResolverUtil();
      ClassLoader var6 = Loader.getClassLoader();
      if (var6 != null) {
         var5.setClassLoader(var6);
      }

      if (var1) {
         ConcurrentMap var7 = decode(var6);
         if (var7 != null) {
            pluginTypeMap = var7;
            this.plugins = (Map)var7.get(this.type);
         } else {
            LOGGER.warn("Plugin preloads not available from class loader {}", var6);
         }
      }

      String var11;
      if (this.plugins == null || this.plugins.size() == 0) {
         if (var2 == null) {
            if (!PACKAGES.contains("org.apache.logging.log4j.core")) {
               PACKAGES.add("org.apache.logging.log4j.core");
            }
         } else {
            String[] var20 = var2.split(",");
            String[] var8 = var20;
            int var9 = var20.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               var11 = var8[var10];
               PACKAGES.add(var11);
            }
         }
      }

      PluginManager.PluginTest var21 = new PluginManager.PluginTest(this.clazz);
      Iterator var22 = PACKAGES.iterator();

      while(var22.hasNext()) {
         String var24 = (String)var22.next();
         var5.findInPackage(var21, var24);
      }

      var22 = var5.getClasses().iterator();

      while(true) {
         Map var12;
         String var13;
         PluginType var14;
         PluginAliases var15;
         Class var25;
         Plugin var26;
         do {
            if (!var22.hasNext()) {
               long var23 = System.nanoTime() - var3;
               this.plugins = (Map)pluginTypeMap.get(this.type);
               StringBuilder var27 = new StringBuilder("Generated plugins");
               var27.append(" in ");
               DecimalFormat var28 = new DecimalFormat("#0");
               long var29 = var23 / 1000000000L;
               var23 %= 1000000000L;
               var27.append(var28.format(var29)).append('.');
               var28 = new DecimalFormat("000000000");
               var27.append(var28.format(var23)).append(" seconds");
               LOGGER.debug(var27.toString());
               return;
            }

            var25 = (Class)var22.next();
            var26 = (Plugin)var25.getAnnotation(Plugin.class);
            var11 = var26.category();
            if (!pluginTypeMap.containsKey(var11)) {
               pluginTypeMap.putIfAbsent(var11, new ConcurrentHashMap());
            }

            var12 = (Map)pluginTypeMap.get(var11);
            var13 = var26.elementType().equals("") ? var26.name() : var26.elementType();
            var14 = new PluginType(var25, var13, var26.printObject(), var26.deferChildren());
            var12.put(var26.name().toLowerCase(), var14);
            var15 = (PluginAliases)var25.getAnnotation(PluginAliases.class);
         } while(var15 == null);

         String[] var16 = var15.value();
         int var17 = var16.length;

         for(int var18 = 0; var18 < var17; ++var18) {
            String var19 = var16[var18];
            var13 = var26.elementType().equals("") ? var19 : var26.elementType();
            var14 = new PluginType(var25, var13, var26.printObject(), var26.deferChildren());
            var12.put(var19.trim().toLowerCase(), var14);
         }
      }
   }

   private static ConcurrentMap decode(ClassLoader var0) {
      Enumeration var1;
      try {
         var1 = var0.getResources("org/apache/logging/log4j/core/config/plugins/Log4j2Plugins.dat");
      } catch (IOException var20) {
         LOGGER.warn((String)"Unable to preload plugins", (Throwable)var20);
         return null;
      }

      ConcurrentHashMap var2;
      DataInputStream var3;
      for(var2 = new ConcurrentHashMap(); var1.hasMoreElements(); Closer.closeSilent((Closeable)var3)) {
         var3 = null;

         InputStream var5;
         try {
            URL var4 = (URL)var1.nextElement();
            LOGGER.debug("Found Plugin Map at {}", var4.toExternalForm());
            var5 = var4.openStream();
            BufferedInputStream var6 = new BufferedInputStream(var5);
            var3 = new DataInputStream(var6);
            int var7 = var3.readInt();

            for(int var8 = 0; var8 < var7; ++var8) {
               String var9 = var3.readUTF();
               int var10 = var3.readInt();
               Object var11 = (ConcurrentMap)var2.get(var9);
               if (var11 == null) {
                  var11 = new ConcurrentHashMap(var7);
               }

               for(int var12 = 0; var12 < var10; ++var12) {
                  String var13 = var3.readUTF();
                  String var14 = var3.readUTF();
                  String var15 = var3.readUTF();
                  boolean var16 = var3.readBoolean();
                  boolean var17 = var3.readBoolean();
                  Class var18 = Class.forName(var14);
                  ((ConcurrentMap)var11).put(var13, new PluginType(var18, var15, var16, var17));
               }

               var2.putIfAbsent(var9, var11);
            }
         } catch (Exception var21) {
            LOGGER.warn((String)"Unable to preload plugins", (Throwable)var21);
            var5 = null;
            Closer.closeSilent((Closeable)var3);
            return var5;
         }
      }

      return var2.size() == 0 ? null : var2;
   }

   private static void encode(ConcurrentMap var0) {
      String var1 = rootDir + "org/apache/logging/log4j/core/config/plugins/" + "Log4j2Plugins.dat";
      DataOutputStream var2 = null;

      try {
         File var3 = new File(rootDir + "org/apache/logging/log4j/core/config/plugins/");
         var3.mkdirs();
         FileOutputStream var4 = new FileOutputStream(var1);
         BufferedOutputStream var5 = new BufferedOutputStream(var4);
         var2 = new DataOutputStream(var5);
         var2.writeInt(var0.size());
         Iterator var6 = var0.entrySet().iterator();

         while(var6.hasNext()) {
            Entry var7 = (Entry)var6.next();
            var2.writeUTF((String)var7.getKey());
            var2.writeInt(((ConcurrentMap)var7.getValue()).size());
            Iterator var8 = ((ConcurrentMap)var7.getValue()).entrySet().iterator();

            while(var8.hasNext()) {
               Entry var9 = (Entry)var8.next();
               var2.writeUTF((String)var9.getKey());
               PluginType var10 = (PluginType)var9.getValue();
               var2.writeUTF(var10.getPluginClass().getName());
               var2.writeUTF(var10.getElementName());
               var2.writeBoolean(var10.isObjectPrintable());
               var2.writeBoolean(var10.isDeferChildren());
            }
         }
      } catch (Exception var12) {
         var12.printStackTrace();
         Closer.closeSilent((Closeable)var2);
         return;
      }

      Closer.closeSilent((Closeable)var2);
   }

   public static class PluginTest extends ResolverUtil.ClassTest {
      private final Class isA;

      public PluginTest(Class var1) {
         this.isA = var1;
      }

      public boolean matches(Class var1) {
         return var1 != null && var1.isAnnotationPresent(Plugin.class) && (this.isA == null || this.isA.isAssignableFrom(var1));
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder("annotated with @" + Plugin.class.getSimpleName());
         if (this.isA != null) {
            var1.append(" is assignable to " + this.isA.getSimpleName());
         }

         return var1.toString();
      }
   }
}
