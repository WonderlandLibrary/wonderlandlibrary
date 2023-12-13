package org.apache.logging.log4j.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.PluginValue;
import org.apache.logging.log4j.core.filter.AbstractFilterable;
import org.apache.logging.log4j.core.helpers.NameUtil;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.MapLookup;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.PropertiesUtil;

public class BaseConfiguration extends AbstractFilterable implements Configuration {
   protected static final Logger LOGGER = StatusLogger.getLogger();
   protected Node rootNode;
   protected final List listeners = new CopyOnWriteArrayList();
   protected ConfigurationMonitor monitor = new DefaultConfigurationMonitor();
   private Advertiser advertiser = new DefaultAdvertiser();
   protected Map advertisedConfiguration;
   private Node advertiserNode = null;
   private Object advertisement;
   protected boolean isShutdownHookEnabled = true;
   private String name;
   private ConcurrentMap appenders = new ConcurrentHashMap();
   private ConcurrentMap loggers = new ConcurrentHashMap();
   private final StrLookup tempLookup = new Interpolator();
   private final StrSubstitutor subst;
   private LoggerConfig root;
   private final boolean started;
   private final ConcurrentMap componentMap;
   protected PluginManager pluginManager;

   protected BaseConfiguration() {
      this.subst = new StrSubstitutor(this.tempLookup);
      this.root = new LoggerConfig();
      this.started = false;
      this.componentMap = new ConcurrentHashMap();
      this.pluginManager = new PluginManager("Core");
      this.rootNode = new Node();
   }

   public Map getProperties() {
      return (Map)this.componentMap.get("ContextProperties");
   }

   public void start() {
      this.pluginManager.collectPlugins();
      this.setup();
      this.setupAdvertisement();
      this.doConfigure();
      Iterator var1 = this.loggers.values().iterator();

      while(var1.hasNext()) {
         LoggerConfig var2 = (LoggerConfig)var1.next();
         var2.startFilter();
      }

      var1 = this.appenders.values().iterator();

      while(var1.hasNext()) {
         Appender var3 = (Appender)var1.next();
         var3.start();
      }

      this.root.startFilter();
      this.startFilter();
   }

   public void stop() {
      Appender[] var1 = (Appender[])this.appenders.values().toArray(new Appender[this.appenders.size()]);

      for(int var2 = var1.length - 1; var2 >= 0; --var2) {
         var1[var2].stop();
      }

      Iterator var4 = this.loggers.values().iterator();

      while(var4.hasNext()) {
         LoggerConfig var3 = (LoggerConfig)var4.next();
         var3.clearAppenders();
         var3.stopFilter();
      }

      this.root.stopFilter();
      this.stopFilter();
      if (this.advertiser != null && this.advertisement != null) {
         this.advertiser.unadvertise(this.advertisement);
      }

   }

   public boolean isShutdownHookEnabled() {
      return this.isShutdownHookEnabled;
   }

   protected void setup() {
   }

   protected Level getDefaultStatus() {
      String var1 = PropertiesUtil.getProperties().getStringProperty("Log4jDefaultStatusLevel", Level.ERROR.name());

      try {
         return Level.toLevel(var1);
      } catch (Exception var3) {
         return Level.ERROR;
      }
   }

   protected void createAdvertiser(String var1, ConfigurationFactory.ConfigurationSource var2, byte[] var3, String var4) {
      if (var1 != null) {
         Node var5 = new Node((Node)null, var1, (PluginType)null);
         Map var6 = var5.getAttributes();
         var6.put("content", new String(var3));
         var6.put("contentType", var4);
         var6.put("name", "configuration");
         if (var2.getLocation() != null) {
            var6.put("location", var2.getLocation());
         }

         this.advertiserNode = var5;
      }

   }

   private void setupAdvertisement() {
      if (this.advertiserNode != null) {
         String var1 = this.advertiserNode.getName();
         PluginType var2 = this.pluginManager.getPluginType(var1);
         if (var2 != null) {
            Class var3 = var2.getPluginClass();

            try {
               this.advertiser = (Advertiser)var3.newInstance();
               this.advertisement = this.advertiser.advertise(this.advertiserNode.getAttributes());
            } catch (InstantiationException var5) {
               System.err.println("InstantiationException attempting to instantiate advertiser: " + var1);
            } catch (IllegalAccessException var6) {
               System.err.println("IllegalAccessException attempting to instantiate advertiser: " + var1);
            }
         }
      }

   }

   public Object getComponent(String var1) {
      return this.componentMap.get(var1);
   }

   public void addComponent(String var1, Object var2) {
      this.componentMap.putIfAbsent(var1, var2);
   }

   protected void doConfigure() {
      boolean var1 = false;
      boolean var2 = false;
      Iterator var3 = this.rootNode.getChildren().iterator();

      while(var3.hasNext()) {
         Node var4 = (Node)var3.next();
         this.createConfiguration(var4, (LogEvent)null);
         if (var4.getObject() != null) {
            if (var4.getName().equalsIgnoreCase("Properties")) {
               if (this.tempLookup == this.subst.getVariableResolver()) {
                  this.subst.setVariableResolver((StrLookup)var4.getObject());
               } else {
                  LOGGER.error("Properties declaration must be the first element in the configuration");
               }
            } else {
               if (this.tempLookup == this.subst.getVariableResolver()) {
                  Map var5 = (Map)this.componentMap.get("ContextProperties");
                  MapLookup var6 = var5 == null ? null : new MapLookup(var5);
                  this.subst.setVariableResolver(new Interpolator(var6));
               }

               if (var4.getName().equalsIgnoreCase("Appenders")) {
                  this.appenders = (ConcurrentMap)var4.getObject();
               } else if (var4.getObject() instanceof Filter) {
                  this.addFilter((Filter)var4.getObject());
               } else if (var4.getName().equalsIgnoreCase("Loggers")) {
                  Loggers var10 = (Loggers)var4.getObject();
                  this.loggers = var10.getMap();
                  var2 = true;
                  if (var10.getRoot() != null) {
                     this.root = var10.getRoot();
                     var1 = true;
                  }
               } else {
                  LOGGER.error("Unknown object \"" + var4.getName() + "\" of type " + var4.getObject().getClass().getName() + " is ignored");
               }
            }
         }
      }

      if (!var2) {
         LOGGER.warn("No Loggers were configured, using default. Is the Loggers element missing?");
         this.setToDefault();
      } else {
         if (!var1) {
            LOGGER.warn("No Root logger was configured, creating default ERROR-level Root logger with Console appender");
            this.setToDefault();
         }

         var3 = this.loggers.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var9 = (Entry)var3.next();
            LoggerConfig var11 = (LoggerConfig)var9.getValue();
            Iterator var12 = var11.getAppenderRefs().iterator();

            while(var12.hasNext()) {
               AppenderRef var7 = (AppenderRef)var12.next();
               Appender var8 = (Appender)this.appenders.get(var7.getRef());
               if (var8 != null) {
                  var11.addAppender(var8, var7.getLevel(), var7.getFilter());
               } else {
                  LOGGER.error("Unable to locate appender " + var7.getRef() + " for logger " + var11.getName());
               }
            }
         }

         this.setParents();
      }
   }

   private void setToDefault() {
      this.setName("Default");
      PatternLayout var1 = PatternLayout.createLayout("%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n", (Configuration)null, (RegexReplacement)null, (String)null, (String)null);
      ConsoleAppender var2 = ConsoleAppender.createAppender(var1, (Filter)null, "SYSTEM_OUT", "Console", "false", "true");
      var2.start();
      this.addAppender(var2);
      LoggerConfig var3 = this.getRootLogger();
      var3.addAppender(var2, (Level)null, (Filter)null);
      String var4 = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.level");
      Level var5 = var4 != null && Level.valueOf(var4) != null ? Level.valueOf(var4) : Level.ERROR;
      var3.setLevel(var5);
   }

   public void setName(String var1) {
      this.name = var1;
   }

   public String getName() {
      return this.name;
   }

   public void addListener(ConfigurationListener var1) {
      this.listeners.add(var1);
   }

   public void removeListener(ConfigurationListener var1) {
      this.listeners.remove(var1);
   }

   public Appender getAppender(String var1) {
      return (Appender)this.appenders.get(var1);
   }

   public Map getAppenders() {
      return this.appenders;
   }

   public void addAppender(Appender var1) {
      this.appenders.put(var1.getName(), var1);
   }

   public StrSubstitutor getStrSubstitutor() {
      return this.subst;
   }

   public void setConfigurationMonitor(ConfigurationMonitor var1) {
      this.monitor = var1;
   }

   public ConfigurationMonitor getConfigurationMonitor() {
      return this.monitor;
   }

   public void setAdvertiser(Advertiser var1) {
      this.advertiser = var1;
   }

   public Advertiser getAdvertiser() {
      return this.advertiser;
   }

   public synchronized void addLoggerAppender(org.apache.logging.log4j.core.Logger var1, Appender var2) {
      String var3 = var1.getName();
      this.appenders.putIfAbsent(var2.getName(), var2);
      LoggerConfig var4 = this.getLoggerConfig(var3);
      if (var4.getName().equals(var3)) {
         var4.addAppender(var2, (Level)null, (Filter)null);
      } else {
         LoggerConfig var5 = new LoggerConfig(var3, var4.getLevel(), var4.isAdditive());
         var5.addAppender(var2, (Level)null, (Filter)null);
         var5.setParent(var4);
         this.loggers.putIfAbsent(var3, var5);
         this.setParents();
         var1.getContext().updateLoggers();
      }

   }

   public synchronized void addLoggerFilter(org.apache.logging.log4j.core.Logger var1, Filter var2) {
      String var3 = var1.getName();
      LoggerConfig var4 = this.getLoggerConfig(var3);
      if (var4.getName().equals(var3)) {
         var4.addFilter(var2);
      } else {
         LoggerConfig var5 = new LoggerConfig(var3, var4.getLevel(), var4.isAdditive());
         var5.addFilter(var2);
         var5.setParent(var4);
         this.loggers.putIfAbsent(var3, var5);
         this.setParents();
         var1.getContext().updateLoggers();
      }

   }

   public synchronized void setLoggerAdditive(org.apache.logging.log4j.core.Logger var1, boolean var2) {
      String var3 = var1.getName();
      LoggerConfig var4 = this.getLoggerConfig(var3);
      if (var4.getName().equals(var3)) {
         var4.setAdditive(var2);
      } else {
         LoggerConfig var5 = new LoggerConfig(var3, var4.getLevel(), var2);
         var5.setParent(var4);
         this.loggers.putIfAbsent(var3, var5);
         this.setParents();
         var1.getContext().updateLoggers();
      }

   }

   public synchronized void removeAppender(String var1) {
      Iterator var2 = this.loggers.values().iterator();

      while(var2.hasNext()) {
         LoggerConfig var3 = (LoggerConfig)var2.next();
         var3.removeAppender(var1);
      }

      Appender var4 = (Appender)this.appenders.remove(var1);
      if (var4 != null) {
         var4.stop();
      }

   }

   public LoggerConfig getLoggerConfig(String var1) {
      if (this.loggers.containsKey(var1)) {
         return (LoggerConfig)this.loggers.get(var1);
      } else {
         String var2 = var1;

         do {
            if ((var2 = NameUtil.getSubName(var2)) == null) {
               return this.root;
            }
         } while(!this.loggers.containsKey(var2));

         return (LoggerConfig)this.loggers.get(var2);
      }
   }

   public LoggerConfig getRootLogger() {
      return this.root;
   }

   public Map getLoggers() {
      return Collections.unmodifiableMap(this.loggers);
   }

   public LoggerConfig getLogger(String var1) {
      return (LoggerConfig)this.loggers.get(var1);
   }

   public void addLogger(String var1, LoggerConfig var2) {
      this.loggers.put(var1, var2);
      this.setParents();
   }

   public void removeLogger(String var1) {
      this.loggers.remove(var1);
      this.setParents();
   }

   public void createConfiguration(Node var1, LogEvent var2) {
      PluginType var3 = var1.getType();
      if (var3 != null && var3.isDeferChildren()) {
         var1.setObject(this.createPluginObject(var3, var1, var2));
      } else {
         Iterator var4 = var1.getChildren().iterator();

         while(var4.hasNext()) {
            Node var5 = (Node)var4.next();
            this.createConfiguration(var5, var2);
         }

         if (var3 == null) {
            if (var1.getParent() != null) {
               LOGGER.error("Unable to locate plugin for " + var1.getName());
            }
         } else {
            var1.setObject(this.createPluginObject(var3, var1, var2));
         }
      }

   }

   private Object createPluginObject(PluginType var1, Node var2, LogEvent var3) {
      Class var4 = var1.getPluginClass();
      Iterator var38;
      Node var40;
      if (Map.class.isAssignableFrom(var4)) {
         try {
            Map var36 = (Map)var4.newInstance();
            var38 = var2.getChildren().iterator();

            while(var38.hasNext()) {
               var40 = (Node)var38.next();
               var36.put(var40.getName(), var40.getObject());
            }

            return var36;
         } catch (Exception var34) {
            LOGGER.warn("Unable to create Map for " + var1.getElementName() + " of class " + var4);
         }
      }

      if (List.class.isAssignableFrom(var4)) {
         try {
            List var35 = (List)var4.newInstance();
            var38 = var2.getChildren().iterator();

            while(var38.hasNext()) {
               var40 = (Node)var38.next();
               var35.add(var40.getObject());
            }

            return var35;
         } catch (Exception var33) {
            LOGGER.warn("Unable to create List for " + var1.getElementName() + " of class " + var4);
         }
      }

      Method var5 = null;
      Method[] var6 = var4.getMethods();
      int var7 = var6.length;

      for(int var8 = 0; var8 < var7; ++var8) {
         Method var9 = var6[var8];
         if (var9.isAnnotationPresent(PluginFactory.class)) {
            var5 = var9;
            break;
         }
      }

      if (var5 == null) {
         return null;
      } else {
         Annotation[][] var37 = var5.getParameterAnnotations();
         Class[] var39 = var5.getParameterTypes();
         if (var37.length != var39.length) {
            LOGGER.error("Number of parameter annotations does not equal the number of paramters");
         }

         Object[] var41 = new Object[var39.length];
         int var42 = 0;
         Map var10 = var2.getAttributes();
         List var11 = var2.getChildren();
         StringBuilder var12 = new StringBuilder();
         ArrayList var13 = new ArrayList();
         Annotation[][] var14 = var37;
         int var15 = var37.length;

         for(int var16 = 0; var16 < var15; ++var16) {
            Annotation[] var17 = var14[var16];
            String[] var18 = null;
            Annotation[] var19 = var17;
            int var20 = var17.length;

            int var21;
            Annotation var22;
            for(var21 = 0; var21 < var20; ++var21) {
               var22 = var19[var21];
               if (var22 instanceof PluginAliases) {
                  var18 = ((PluginAliases)var22).value();
               }
            }

            var19 = var17;
            var20 = var17.length;

            for(var21 = 0; var21 < var20; ++var21) {
               var22 = var19[var21];
               if (!(var22 instanceof PluginAliases)) {
                  if (var12.length() == 0) {
                     var12.append(" with params(");
                  } else {
                     var12.append(", ");
                  }

                  if (var22 instanceof PluginNode) {
                     var41[var42] = var2;
                     var12.append("Node=").append(var2.getName());
                  } else if (var22 instanceof PluginConfiguration) {
                     var41[var42] = this;
                     if (this.name != null) {
                        var12.append("Configuration(").append(this.name).append(")");
                     } else {
                        var12.append("Configuration");
                     }
                  } else {
                     String var24;
                     String var52;
                     if (var22 instanceof PluginValue) {
                        String var51 = ((PluginValue)var22).value();
                        var24 = var2.getValue();
                        if (var24 == null) {
                           var24 = this.getAttrValue("value", (String[])null, var10);
                        }

                        var52 = this.subst.replace(var3, var24);
                        var12.append(var51).append("=\"").append(var52).append("\"");
                        var41[var42] = var52;
                     } else if (var22 instanceof PluginAttribute) {
                        PluginAttribute var50 = (PluginAttribute)var22;
                        var24 = var50.value();
                        var52 = this.subst.replace(var3, this.getAttrValue(var24, var18, var10));
                        var12.append(var24).append("=\"").append(var52).append("\"");
                        var41[var42] = var52;
                     } else if (var22 instanceof PluginElement) {
                        PluginElement var23 = (PluginElement)var22;
                        var24 = var23.value();
                        Class var25;
                        if (var39[var42].isArray()) {
                           var25 = var39[var42].getComponentType();
                           ArrayList var53 = new ArrayList();
                           var12.append(var24).append("={");
                           boolean var54 = true;
                           Iterator var55 = var11.iterator();

                           Object var31;
                           while(var55.hasNext()) {
                              Node var57 = (Node)var55.next();
                              PluginType var30 = var57.getType();
                              if (var23.value().equalsIgnoreCase(var30.getElementName()) || var25.isAssignableFrom(var30.getPluginClass())) {
                                 var13.add(var57);
                                 if (!var54) {
                                    var12.append(", ");
                                 }

                                 var54 = false;
                                 var31 = var57.getObject();
                                 if (var31 == null) {
                                    LOGGER.error("Null object returned for " + var57.getName() + " in " + var2.getName());
                                 } else {
                                    if (var31.getClass().isArray()) {
                                       this.printArray(var12, (Object[])((Object[])var31));
                                       var41[var42] = var31;
                                       break;
                                    }

                                    var12.append(var57.toString());
                                    var53.add(var31);
                                 }
                              }
                           }

                           var12.append("}");
                           if (var41[var42] != null) {
                              break;
                           }

                           if (var53.size() > 0 && !var25.isAssignableFrom(var53.get(0).getClass())) {
                              LOGGER.error("Attempted to assign List containing class " + var53.get(0).getClass().getName() + " to array of type " + var25 + " for attribute " + var24);
                              break;
                           }

                           Object[] var56 = (Object[])((Object[])Array.newInstance(var25, var53.size()));
                           int var58 = 0;

                           for(Iterator var59 = var53.iterator(); var59.hasNext(); ++var58) {
                              var31 = var59.next();
                              var56[var58] = var31;
                           }

                           var41[var42] = var56;
                        } else {
                           var25 = var39[var42];
                           boolean var26 = false;
                           Iterator var27 = var11.iterator();

                           while(var27.hasNext()) {
                              Node var28 = (Node)var27.next();
                              PluginType var29 = var28.getType();
                              if (var23.value().equals(var29.getElementName()) || var25.isAssignableFrom(var29.getPluginClass())) {
                                 var12.append(var28.getName()).append("(").append(var28.toString()).append(")");
                                 var26 = true;
                                 var13.add(var28);
                                 var41[var42] = var28.getObject();
                                 break;
                              }
                           }

                           if (!var26) {
                              var12.append("null");
                           }
                        }
                     }
                  }
               }
            }

            ++var42;
         }

         if (var12.length() > 0) {
            var12.append(")");
         }

         String var48;
         if (var10.size() > 0) {
            StringBuilder var43 = new StringBuilder();
            Iterator var46 = var10.keySet().iterator();

            while(var46.hasNext()) {
               var48 = (String)var46.next();
               if (var43.length() == 0) {
                  var43.append(var2.getName());
                  var43.append(" contains ");
                  if (var10.size() == 1) {
                     var43.append("an invalid element or attribute ");
                  } else {
                     var43.append("invalid attributes ");
                  }
               } else {
                  var43.append(", ");
               }

               var43.append("\"");
               var43.append(var48);
               var43.append("\"");
            }

            LOGGER.error(var43.toString());
         }

         if (!var1.isDeferChildren() && var13.size() != var11.size()) {
            Iterator var44 = var11.iterator();

            while(var44.hasNext()) {
               Node var47 = (Node)var44.next();
               if (!var13.contains(var47)) {
                  var48 = var2.getType().getElementName();
                  String var49 = var48.equals(var2.getName()) ? var2.getName() : var48 + " " + var2.getName();
                  LOGGER.error(var49 + " has no parameter that matches element " + var47.getName());
               }
            }
         }

         try {
            int var45 = var5.getModifiers();
            if (!Modifier.isStatic(var45)) {
               LOGGER.error(var5.getName() + " method is not static on class " + var4.getName() + " for element " + var2.getName());
               return null;
            } else {
               LOGGER.debug("Calling {} on class {} for element {}{}", var5.getName(), var4.getName(), var2.getName(), var12.toString());
               return var5.invoke((Object)null, var41);
            }
         } catch (Exception var32) {
            LOGGER.error((String)("Unable to invoke method " + var5.getName() + " in class " + var4.getName() + " for element " + var2.getName()), (Throwable)var32);
            return null;
         }
      }
   }

   private void printArray(StringBuilder var1, Object... var2) {
      boolean var3 = true;
      Object[] var4 = var2;
      int var5 = var2.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Object var7 = var4[var6];
         if (!var3) {
            var1.append(", ");
         }

         var1.append(var7.toString());
         var3 = false;
      }

   }

   private String getAttrValue(String var1, String[] var2, Map var3) {
      Iterator var4 = var3.keySet().iterator();

      while(true) {
         String var5;
         do {
            if (!var4.hasNext()) {
               return null;
            }

            var5 = (String)var4.next();
            if (var5.equalsIgnoreCase(var1)) {
               String var11 = (String)var3.get(var5);
               var3.remove(var5);
               return var11;
            }
         } while(var2 == null);

         String[] var6 = var2;
         int var7 = var2.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            String var9 = var6[var8];
            if (var5.equalsIgnoreCase(var9)) {
               String var10 = (String)var3.get(var5);
               var3.remove(var5);
               return var10;
            }
         }
      }
   }

   private void setParents() {
      Iterator var1 = this.loggers.entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         LoggerConfig var3 = (LoggerConfig)var2.getValue();
         String var4 = (String)var2.getKey();
         if (!var4.equals("")) {
            int var5 = var4.lastIndexOf(46);
            if (var5 > 0) {
               var4 = var4.substring(0, var5);
               LoggerConfig var6 = this.getLoggerConfig(var4);
               if (var6 == null) {
                  var6 = this.root;
               }

               var3.setParent(var6);
            } else {
               var3.setParent(this.root);
            }
         }
      }

   }
}
