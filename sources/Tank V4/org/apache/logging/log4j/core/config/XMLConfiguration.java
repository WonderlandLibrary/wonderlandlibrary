package org.apache.logging.log4j.core.config;

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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.ResolverUtil;
import org.apache.logging.log4j.core.helpers.FileUtils;
import org.apache.logging.log4j.status.StatusConsoleListener;
import org.apache.logging.log4j.status.StatusListener;
import org.apache.logging.log4j.status.StatusLogger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLConfiguration extends BaseConfiguration implements Reconfigurable {
   private static final String XINCLUDE_FIXUP_LANGUAGE = "http://apache.org/xml/features/xinclude/fixup-language";
   private static final String XINCLUDE_FIXUP_BASE_URIS = "http://apache.org/xml/features/xinclude/fixup-base-uris";
   private static final String[] VERBOSE_CLASSES = new String[]{ResolverUtil.class.getName()};
   private static final String LOG4J_XSD = "Log4j-config.xsd";
   private static final int BUF_SIZE = 16384;
   private final List status = new ArrayList();
   private Element rootElement;
   private boolean strict;
   private String schema;
   private final File configFile;

   static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      DocumentBuilderFactory var0 = DocumentBuilderFactory.newInstance();
      var0.setNamespaceAware(true);
      enableXInclude(var0);
      return var0.newDocumentBuilder();
   }

   private static void enableXInclude(DocumentBuilderFactory var0) {
      try {
         var0.setXIncludeAware(true);
      } catch (UnsupportedOperationException var6) {
         LOGGER.warn((String)("The DocumentBuilderFactory does not support XInclude: " + var0), (Throwable)var6);
      } catch (AbstractMethodError var7) {
         LOGGER.warn("The DocumentBuilderFactory is out of date and does not support XInclude: " + var0);
      }

      try {
         var0.setFeature("http://apache.org/xml/features/xinclude/fixup-base-uris", true);
      } catch (ParserConfigurationException var4) {
         LOGGER.warn((String)("The DocumentBuilderFactory [" + var0 + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-base-uris" + "]"), (Throwable)var4);
      } catch (AbstractMethodError var5) {
         LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + var0);
      }

      try {
         var0.setFeature("http://apache.org/xml/features/xinclude/fixup-language", true);
      } catch (ParserConfigurationException var2) {
         LOGGER.warn((String)("The DocumentBuilderFactory [" + var0 + "] does not support the feature [" + "http://apache.org/xml/features/xinclude/fixup-language" + "]"), (Throwable)var2);
      } catch (AbstractMethodError var3) {
         LOGGER.warn("The DocumentBuilderFactory is out of date and does not support setFeature: " + var0);
      }

   }

   public XMLConfiguration(ConfigurationFactory.ConfigurationSource var1) {
      this.configFile = var1.getFile();
      byte[] var2 = null;

      try {
         ArrayList var3 = new ArrayList();
         InputStream var4 = var1.getInputStream();
         var2 = this.toByteArray(var4);
         var4.close();
         InputSource var5 = new InputSource(new ByteArrayInputStream(var2));
         Document var6 = newDocumentBuilder().parse(var5);
         this.rootElement = var6.getDocumentElement();
         Map var7 = this.processAttributes(this.rootNode, this.rootElement);
         Level var8 = this.getDefaultStatus();
         boolean var9 = false;
         PrintStream var10 = System.out;
         Iterator var11 = var7.entrySet().iterator();

         while(true) {
            String var39;
            while(var11.hasNext()) {
               Entry var12 = (Entry)var11.next();
               if ("status".equalsIgnoreCase((String)var12.getKey())) {
                  Level var34 = Level.toLevel(this.getStrSubstitutor().replace((String)var12.getValue()), (Level)null);
                  if (var34 != null) {
                     var8 = var34;
                  } else {
                     var3.add("Invalid status specified: " + (String)var12.getValue() + ". Defaulting to " + var8);
                  }
               } else {
                  String var33;
                  if ("dest".equalsIgnoreCase((String)var12.getKey())) {
                     var33 = this.getStrSubstitutor().replace((String)var12.getValue());
                     if (var33 != null) {
                        if (var33.equalsIgnoreCase("err")) {
                           var10 = System.err;
                        } else {
                           try {
                              File var35 = FileUtils.fileFromURI(new URI(var33));
                              var39 = Charset.defaultCharset().name();
                              var10 = new PrintStream(new FileOutputStream(var35), true, var39);
                           } catch (URISyntaxException var22) {
                              System.err.println("Unable to write to " + var33 + ". Writing to stdout");
                           }
                        }
                     }
                  } else if ("shutdownHook".equalsIgnoreCase((String)var12.getKey())) {
                     var33 = this.getStrSubstitutor().replace((String)var12.getValue());
                     this.isShutdownHookEnabled = !var33.equalsIgnoreCase("disable");
                  } else if ("verbose".equalsIgnoreCase((String)var12.getKey())) {
                     var9 = Boolean.parseBoolean(this.getStrSubstitutor().replace((String)var12.getValue()));
                  } else if ("packages".equalsIgnoreCase(this.getStrSubstitutor().replace((String)var12.getKey()))) {
                     String[] var32 = ((String)var12.getValue()).split(",");
                     String[] var14 = var32;
                     int var15 = var32.length;

                     for(int var16 = 0; var16 < var15; ++var16) {
                        String var17 = var14[var16];
                        PluginManager.addPackage(var17);
                     }
                  } else if ("name".equalsIgnoreCase((String)var12.getKey())) {
                     this.setName(this.getStrSubstitutor().replace((String)var12.getValue()));
                  } else if ("strict".equalsIgnoreCase((String)var12.getKey())) {
                     this.strict = Boolean.parseBoolean(this.getStrSubstitutor().replace((String)var12.getValue()));
                  } else if ("schema".equalsIgnoreCase((String)var12.getKey())) {
                     this.schema = this.getStrSubstitutor().replace((String)var12.getValue());
                  } else if ("monitorInterval".equalsIgnoreCase((String)var12.getKey())) {
                     int var13 = Integer.parseInt(this.getStrSubstitutor().replace((String)var12.getValue()));
                     if (var13 > 0 && this.configFile != null) {
                        this.monitor = new FileConfigurationMonitor(this, this.configFile, this.listeners, var13);
                     }
                  } else if ("advertiser".equalsIgnoreCase((String)var12.getKey())) {
                     this.createAdvertiser(this.getStrSubstitutor().replace((String)var12.getValue()), var1, var2, "text/xml");
                  }
               }
            }

            var11 = ((StatusLogger)LOGGER).getListeners();
            boolean var31 = false;

            while(var11.hasNext()) {
               StatusListener var36 = (StatusListener)var11.next();
               if (var36 instanceof StatusConsoleListener) {
                  var31 = true;
                  ((StatusConsoleListener)var36).setLevel(var8);
                  if (!var9) {
                     ((StatusConsoleListener)var36).setFilters(VERBOSE_CLASSES);
                  }
               }
            }

            if (!var31 && var8 != Level.OFF) {
               StatusConsoleListener var38 = new StatusConsoleListener(var8, var10);
               if (!var9) {
                  var38.setFilters(VERBOSE_CLASSES);
               }

               ((StatusLogger)LOGGER).registerListener(var38);
               Iterator var37 = var3.iterator();

               while(var37.hasNext()) {
                  var39 = (String)var37.next();
                  LOGGER.error(var39);
               }
            }
            break;
         }
      } catch (SAXException var23) {
         LOGGER.error((String)("Error parsing " + var1.getLocation()), (Throwable)var23);
      } catch (IOException var24) {
         LOGGER.error((String)("Error parsing " + var1.getLocation()), (Throwable)var24);
      } catch (ParserConfigurationException var25) {
         LOGGER.error((String)("Error parsing " + var1.getLocation()), (Throwable)var25);
      }

      if (this.strict && this.schema != null && var2 != null) {
         InputStream var26 = null;

         try {
            var26 = this.getClass().getClassLoader().getResourceAsStream(this.schema);
         } catch (Exception var21) {
            LOGGER.error("Unable to access schema " + this.schema);
         }

         if (var26 != null) {
            StreamSource var27 = new StreamSource(var26, "Log4j-config.xsd");
            SchemaFactory var28 = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema var29 = null;

            try {
               var29 = var28.newSchema(var27);
            } catch (SAXException var20) {
               LOGGER.error((String)"Error parsing Log4j schema", (Throwable)var20);
            }

            if (var29 != null) {
               Validator var30 = var29.newValidator();

               try {
                  var30.validate(new StreamSource(new ByteArrayInputStream(var2)));
               } catch (IOException var18) {
                  LOGGER.error((String)"Error reading configuration for validation", (Throwable)var18);
               } catch (SAXException var19) {
                  LOGGER.error((String)"Error validating configuration", (Throwable)var19);
               }
            }
         }
      }

      if (this.getName() == null) {
         this.setName(var1.getLocation());
      }

   }

   public void setup() {
      if (this.rootElement == null) {
         LOGGER.error("No logging configuration");
      } else {
         this.constructHierarchy(this.rootNode, this.rootElement);
         if (this.status.size() <= 0) {
            this.rootElement = null;
         } else {
            Iterator var1 = this.status.iterator();

            while(var1.hasNext()) {
               XMLConfiguration.Status var2 = (XMLConfiguration.Status)var1.next();
               LOGGER.error("Error processing element " + XMLConfiguration.Status.access$000(var2) + ": " + XMLConfiguration.Status.access$100(var2));
            }

         }
      }
   }

   public Configuration reconfigure() {
      if (this.configFile != null) {
         try {
            ConfigurationFactory.ConfigurationSource var1 = new ConfigurationFactory.ConfigurationSource(new FileInputStream(this.configFile), this.configFile);
            return new XMLConfiguration(var1);
         } catch (FileNotFoundException var2) {
            LOGGER.error((String)("Cannot locate file " + this.configFile), (Throwable)var2);
         }
      }

      return null;
   }

   private void constructHierarchy(Node var1, Element var2) {
      this.processAttributes(var1, var2);
      StringBuilder var3 = new StringBuilder();
      NodeList var4 = var2.getChildNodes();
      List var5 = var1.getChildren();

      for(int var6 = 0; var6 < var4.getLength(); ++var6) {
         org.w3c.dom.Node var7 = var4.item(var6);
         if (var7 instanceof Element) {
            Element var14 = (Element)var7;
            String var9 = this.getType(var14);
            PluginType var10 = this.pluginManager.getPluginType(var9);
            Node var11 = new Node(var1, var9, var10);
            this.constructHierarchy(var11, var14);
            if (var10 == null) {
               String var12 = var11.getValue();
               if (!var11.hasChildren() && var12 != null) {
                  var1.getAttributes().put(var9, var12);
               } else {
                  this.status.add(new XMLConfiguration.Status(this, var9, var2, XMLConfiguration.ErrorType.CLASS_NOT_FOUND));
               }
            } else {
               var5.add(var11);
            }
         } else if (var7 instanceof Text) {
            Text var8 = (Text)var7;
            var3.append(var8.getData());
         }
      }

      String var13 = var3.toString().trim();
      if (var13.length() > 0 || !var1.hasChildren() && !var1.isRoot()) {
         var1.setValue(var13);
      }

   }

   private String getType(Element var1) {
      if (this.strict) {
         NamedNodeMap var2 = var1.getAttributes();

         for(int var3 = 0; var3 < var2.getLength(); ++var3) {
            org.w3c.dom.Node var4 = var2.item(var3);
            if (var4 instanceof Attr) {
               Attr var5 = (Attr)var4;
               if (var5.getName().equalsIgnoreCase("type")) {
                  String var6 = var5.getValue();
                  var2.removeNamedItem(var5.getName());
                  return var6;
               }
            }
         }
      }

      return var1.getTagName();
   }

   private byte[] toByteArray(InputStream var1) throws IOException {
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var4 = new byte[16384];

      int var3;
      while((var3 = var1.read(var4, 0, var4.length)) != -1) {
         var2.write(var4, 0, var3);
      }

      return var2.toByteArray();
   }

   private Map processAttributes(Node var1, Element var2) {
      NamedNodeMap var3 = var2.getAttributes();
      Map var4 = var1.getAttributes();

      for(int var5 = 0; var5 < var3.getLength(); ++var5) {
         org.w3c.dom.Node var6 = var3.item(var5);
         if (var6 instanceof Attr) {
            Attr var7 = (Attr)var6;
            if (!var7.getName().equals("xml:base")) {
               var4.put(var7.getName(), var7.getValue());
            }
         }
      }

      return var4;
   }

   private class Status {
      private final Element element;
      private final String name;
      private final XMLConfiguration.ErrorType errorType;
      final XMLConfiguration this$0;

      public Status(XMLConfiguration var1, String var2, Element var3, XMLConfiguration.ErrorType var4) {
         this.this$0 = var1;
         this.name = var2;
         this.element = var3;
         this.errorType = var4;
      }

      static String access$000(XMLConfiguration.Status var0) {
         return var0.name;
      }

      static XMLConfiguration.ErrorType access$100(XMLConfiguration.Status var0) {
         return var0.errorType;
      }
   }

   private static enum ErrorType {
      CLASS_NOT_FOUND;

      private static final XMLConfiguration.ErrorType[] $VALUES = new XMLConfiguration.ErrorType[]{CLASS_NOT_FOUND};
   }
}
