package javafx.fxml;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.fxml.BeanAdapter;
import com.sun.javafx.fxml.LoadListener;
import com.sun.javafx.fxml.ParseTraceElement;
import com.sun.javafx.fxml.PropertyNotFoundException;
import com.sun.javafx.fxml.expression.Expression;
import com.sun.javafx.fxml.expression.ExpressionValue;
import com.sun.javafx.fxml.expression.KeyPath;
import com.sun.javafx.util.Logging;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.util.Builder;
import javafx.util.BuilderFactory;
import javafx.util.Callback;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

public class FXMLLoader {
   private static final RuntimePermission GET_CLASSLOADER_PERMISSION = new RuntimePermission("getClassLoader");
   private URL location;
   private ResourceBundle resources;
   private ObservableMap namespace;
   private Object root;
   private Object controller;
   private BuilderFactory builderFactory;
   private Callback controllerFactory;
   private Charset charset;
   private final LinkedList loaders;
   private ClassLoader classLoader;
   private boolean staticLoad;
   private LoadListener loadListener;
   private FXMLLoader parentLoader;
   private XMLStreamReader xmlStreamReader;
   private Element current;
   private ScriptEngine scriptEngine;
   private List packages;
   private Map classes;
   private ScriptEngineManager scriptEngineManager;
   private static ClassLoader defaultClassLoader = null;
   private static final Pattern extraneousWhitespacePattern = Pattern.compile("\\s+");
   private static BuilderFactory DEFAULT_BUILDER_FACTORY = new JavaFXBuilderFactory();
   public static final String DEFAULT_CHARSET_NAME = "UTF-8";
   public static final String LANGUAGE_PROCESSING_INSTRUCTION = "language";
   public static final String IMPORT_PROCESSING_INSTRUCTION = "import";
   public static final String FX_NAMESPACE_PREFIX = "fx";
   public static final String FX_CONTROLLER_ATTRIBUTE = "controller";
   public static final String FX_ID_ATTRIBUTE = "id";
   public static final String FX_VALUE_ATTRIBUTE = "value";
   public static final String FX_CONSTANT_ATTRIBUTE = "constant";
   public static final String FX_FACTORY_ATTRIBUTE = "factory";
   public static final String INCLUDE_TAG = "include";
   public static final String INCLUDE_SOURCE_ATTRIBUTE = "source";
   public static final String INCLUDE_RESOURCES_ATTRIBUTE = "resources";
   public static final String INCLUDE_CHARSET_ATTRIBUTE = "charset";
   public static final String SCRIPT_TAG = "script";
   public static final String SCRIPT_SOURCE_ATTRIBUTE = "source";
   public static final String SCRIPT_CHARSET_ATTRIBUTE = "charset";
   public static final String DEFINE_TAG = "define";
   public static final String REFERENCE_TAG = "reference";
   public static final String REFERENCE_SOURCE_ATTRIBUTE = "source";
   public static final String ROOT_TAG = "root";
   public static final String ROOT_TYPE_ATTRIBUTE = "type";
   public static final String COPY_TAG = "copy";
   public static final String COPY_SOURCE_ATTRIBUTE = "source";
   public static final String EVENT_HANDLER_PREFIX = "on";
   public static final String EVENT_KEY = "event";
   public static final String CHANGE_EVENT_HANDLER_SUFFIX = "Change";
   private static final String COLLECTION_HANDLER_NAME = "onChange";
   public static final String NULL_KEYWORD = "null";
   public static final String ESCAPE_PREFIX = "\\";
   public static final String RELATIVE_PATH_PREFIX = "@";
   public static final String RESOURCE_KEY_PREFIX = "%";
   public static final String EXPRESSION_PREFIX = "$";
   public static final String BINDING_EXPRESSION_PREFIX = "${";
   public static final String BINDING_EXPRESSION_SUFFIX = "}";
   public static final String BI_DIRECTIONAL_BINDING_PREFIX = "#{";
   public static final String BI_DIRECTIONAL_BINDING_SUFFIX = "}";
   public static final String ARRAY_COMPONENT_DELIMITER = ",";
   public static final String LOCATION_KEY = "location";
   public static final String RESOURCES_KEY = "resources";
   public static final String CONTROLLER_METHOD_PREFIX = "#";
   public static final String CONTROLLER_KEYWORD = "controller";
   public static final String CONTROLLER_SUFFIX = "Controller";
   public static final String INITIALIZE_METHOD_NAME = "initialize";
   public static final String JAVAFX_VERSION = (String)AccessController.doPrivileged(new PrivilegedAction() {
      public String run() {
         return System.getProperty("javafx.version");
      }
   });
   public static final String FX_NAMESPACE_VERSION = "1";
   private Class callerClass;
   private final ControllerAccessor controllerAccessor;

   private void injectFields(String var1, Object var2) throws LoadException {
      if (this.controller != null && var1 != null) {
         List var3 = (List)this.controllerAccessor.getControllerFields().get(var1);
         if (var3 != null) {
            try {
               Iterator var4 = var3.iterator();

               while(var4.hasNext()) {
                  Field var5 = (Field)var4.next();
                  var5.set(this.controller, var2);
               }
            } catch (IllegalAccessException var6) {
               throw this.constructLoadException((Throwable)var6);
            }
         }
      }

   }

   public FXMLLoader() {
      this((URL)null);
   }

   public FXMLLoader(URL var1) {
      this(var1, (ResourceBundle)null);
   }

   public FXMLLoader(URL var1, ResourceBundle var2) {
      this(var1, var2, (BuilderFactory)null);
   }

   public FXMLLoader(URL var1, ResourceBundle var2, BuilderFactory var3) {
      this(var1, var2, var3, (Callback)null);
   }

   public FXMLLoader(URL var1, ResourceBundle var2, BuilderFactory var3, Callback var4) {
      this(var1, var2, var3, var4, Charset.forName("UTF-8"));
   }

   public FXMLLoader(Charset var1) {
      this((URL)null, (ResourceBundle)null, (BuilderFactory)null, (Callback)null, var1);
   }

   public FXMLLoader(URL var1, ResourceBundle var2, BuilderFactory var3, Callback var4, Charset var5) {
      this(var1, var2, var3, var4, var5, new LinkedList());
   }

   public FXMLLoader(URL var1, ResourceBundle var2, BuilderFactory var3, Callback var4, Charset var5, LinkedList var6) {
      this.namespace = FXCollections.observableHashMap();
      this.root = null;
      this.controller = null;
      this.classLoader = null;
      this.staticLoad = false;
      this.loadListener = null;
      this.xmlStreamReader = null;
      this.current = null;
      this.scriptEngine = null;
      this.packages = new LinkedList();
      this.classes = new HashMap();
      this.scriptEngineManager = null;
      this.controllerAccessor = new ControllerAccessor();
      this.setLocation(var1);
      this.setResources(var2);
      this.setBuilderFactory(var3);
      this.setControllerFactory(var4);
      this.setCharset(var5);
      this.loaders = new LinkedList(var6);
   }

   public URL getLocation() {
      return this.location;
   }

   public void setLocation(URL var1) {
      this.location = var1;
   }

   public ResourceBundle getResources() {
      return this.resources;
   }

   public void setResources(ResourceBundle var1) {
      this.resources = var1;
   }

   public ObservableMap getNamespace() {
      return this.namespace;
   }

   public Object getRoot() {
      return this.root;
   }

   public void setRoot(Object var1) {
      this.root = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof FXMLLoader) {
         FXMLLoader var2 = (FXMLLoader)var1;
         if (this.location != null && var2.location != null) {
            return this.location.toExternalForm().equals(var2.location.toExternalForm());
         } else {
            return var2.location == this.location;
         }
      } else {
         return false;
      }
   }

   private boolean isCyclic(FXMLLoader var1, FXMLLoader var2) {
      if (var1 == null) {
         return false;
      } else {
         return var1.equals(var2) ? true : this.isCyclic(var1.parentLoader, var2);
      }
   }

   public Object getController() {
      return this.controller;
   }

   public void setController(Object var1) {
      this.controller = var1;
      if (var1 == null) {
         this.namespace.remove("controller");
      } else {
         this.namespace.put("controller", var1);
      }

      this.controllerAccessor.setController(var1);
   }

   public BuilderFactory getBuilderFactory() {
      return this.builderFactory;
   }

   public void setBuilderFactory(BuilderFactory var1) {
      this.builderFactory = var1;
   }

   public Callback getControllerFactory() {
      return this.controllerFactory;
   }

   public void setControllerFactory(Callback var1) {
      this.controllerFactory = var1;
   }

   public Charset getCharset() {
      return this.charset;
   }

   public void setCharset(Charset var1) {
      if (var1 == null) {
         throw new NullPointerException("charset is null.");
      } else {
         this.charset = var1;
      }
   }

   @CallerSensitive
   public ClassLoader getClassLoader() {
      if (this.classLoader == null) {
         SecurityManager var1 = System.getSecurityManager();
         Class var2 = var1 != null ? Reflection.getCallerClass() : null;
         return getDefaultClassLoader(var2);
      } else {
         return this.classLoader;
      }
   }

   public void setClassLoader(ClassLoader var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.classLoader = var1;
         this.clearImports();
      }
   }

   /** @deprecated */
   public boolean impl_isStaticLoad() {
      return this.staticLoad;
   }

   /** @deprecated */
   public void impl_setStaticLoad(boolean var1) {
      this.staticLoad = var1;
   }

   /** @deprecated */
   public LoadListener impl_getLoadListener() {
      return this.loadListener;
   }

   /** @deprecated */
   public void impl_setLoadListener(LoadListener var1) {
      this.loadListener = var1;
   }

   @CallerSensitive
   public Object load() throws IOException {
      return this.loadImpl(System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   @CallerSensitive
   public Object load(InputStream var1) throws IOException {
      return this.loadImpl(var1, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private Object loadImpl(Class var1) throws IOException {
      if (this.location == null) {
         throw new IllegalStateException("Location is not set.");
      } else {
         InputStream var2 = null;

         Object var3;
         try {
            var2 = this.location.openStream();
            var3 = this.loadImpl(var2, var1);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }

         return var3;
      }
   }

   private Object loadImpl(InputStream var1, Class var2) throws IOException {
      if (var1 == null) {
         throw new NullPointerException("inputStream is null.");
      } else {
         this.callerClass = var2;
         this.controllerAccessor.setCallerClass(var2);

         try {
            this.clearImports();
            this.namespace.put("location", this.location);
            this.namespace.put("resources", this.resources);
            this.scriptEngine = null;

            try {
               XMLInputFactory var3 = XMLInputFactory.newInstance();
               var3.setProperty("javax.xml.stream.isCoalescing", true);
               InputStreamReader var4 = new InputStreamReader(var1, this.charset);
               this.xmlStreamReader = new StreamReaderDelegate(var3.createXMLStreamReader(var4)) {
                  public String getPrefix() {
                     String var1 = super.getPrefix();
                     if (var1 != null && var1.length() == 0) {
                        var1 = null;
                     }

                     return var1;
                  }

                  public String getAttributePrefix(int var1) {
                     String var2 = super.getAttributePrefix(var1);
                     if (var2 != null && var2.length() == 0) {
                        var2 = null;
                     }

                     return var2;
                  }
               };
            } catch (XMLStreamException var16) {
               throw this.constructLoadException((Throwable)var16);
            }

            this.loaders.push(this);

            try {
               while(this.xmlStreamReader.hasNext()) {
                  int var21 = this.xmlStreamReader.next();
                  switch (var21) {
                     case 1:
                        this.processStartElement();
                        break;
                     case 2:
                        this.processEndElement();
                        break;
                     case 3:
                        this.processProcessingInstruction();
                        break;
                     case 4:
                        this.processCharacters();
                        break;
                     case 5:
                        this.processComment();
                  }
               }
            } catch (XMLStreamException var17) {
               throw this.constructLoadException((Throwable)var17);
            }

            if (this.controller != null) {
               if (this.controller instanceof Initializable) {
                  ((Initializable)this.controller).initialize(this.location, this.resources);
               } else {
                  Map var22 = this.controllerAccessor.getControllerFields();
                  this.injectFields("location", this.location);
                  this.injectFields("resources", this.resources);
                  Method var23 = (Method)((Map)this.controllerAccessor.getControllerMethods().get(FXMLLoader.SupportedType.PARAMETERLESS)).get("initialize");
                  if (var23 != null) {
                     try {
                        MethodUtil.invoke(var23, this.controller, new Object[0]);
                     } catch (IllegalAccessException var14) {
                     } catch (InvocationTargetException var15) {
                        throw this.constructLoadException((Throwable)var15);
                     }
                  }
               }
            }
         } catch (LoadException var18) {
            throw var18;
         } catch (Exception var19) {
            throw this.constructLoadException((Throwable)var19);
         } finally {
            this.controllerAccessor.setCallerClass((Class)null);
            this.controllerAccessor.reset();
            this.xmlStreamReader = null;
         }

         return this.root;
      }
   }

   private void clearImports() {
      this.packages.clear();
      this.classes.clear();
   }

   private LoadException constructLoadException(String var1) {
      return new LoadException(var1 + this.constructFXMLTrace());
   }

   private LoadException constructLoadException(Throwable var1) {
      return new LoadException(this.constructFXMLTrace(), var1);
   }

   private LoadException constructLoadException(String var1, Throwable var2) {
      return new LoadException(var1 + this.constructFXMLTrace(), var2);
   }

   private String constructFXMLTrace() {
      StringBuilder var1 = new StringBuilder("\n");

      for(Iterator var2 = this.loaders.iterator(); var2.hasNext(); var1.append("\n")) {
         FXMLLoader var3 = (FXMLLoader)var2.next();
         var1.append(var3.location != null ? var3.location.getPath() : "unknown path");
         if (var3.current != null) {
            var1.append(":");
            var1.append(var3.impl_getLineNumber());
         }
      }

      return var1.toString();
   }

   /** @deprecated */
   public int impl_getLineNumber() {
      return this.xmlStreamReader.getLocation().getLineNumber();
   }

   /** @deprecated */
   public ParseTraceElement[] impl_getParseTrace() {
      ParseTraceElement[] var1 = new ParseTraceElement[this.loaders.size()];
      int var2 = 0;

      FXMLLoader var4;
      for(Iterator var3 = this.loaders.iterator(); var3.hasNext(); var1[var2++] = new ParseTraceElement(var4.location, var4.current != null ? var4.impl_getLineNumber() : -1)) {
         var4 = (FXMLLoader)var3.next();
      }

      return var1;
   }

   private void processProcessingInstruction() throws LoadException {
      String var1 = this.xmlStreamReader.getPITarget().trim();
      if (var1.equals("language")) {
         this.processLanguage();
      } else if (var1.equals("import")) {
         this.processImport();
      }

   }

   private void processLanguage() throws LoadException {
      if (this.scriptEngine != null) {
         throw this.constructLoadException("Page language already set.");
      } else {
         String var1 = this.xmlStreamReader.getPIData();
         if (this.loadListener != null) {
            this.loadListener.readLanguageProcessingInstruction(var1);
         }

         if (!this.staticLoad) {
            ScriptEngineManager var2 = this.getScriptEngineManager();
            this.scriptEngine = var2.getEngineByName(var1);
         }

      }
   }

   private void processImport() throws LoadException {
      String var1 = this.xmlStreamReader.getPIData().trim();
      if (this.loadListener != null) {
         this.loadListener.readImportProcessingInstruction(var1);
      }

      if (var1.endsWith(".*")) {
         this.importPackage(var1.substring(0, var1.length() - 2));
      } else {
         this.importClass(var1);
      }

   }

   private void processComment() throws LoadException {
      if (this.loadListener != null) {
         this.loadListener.readComment(this.xmlStreamReader.getText());
      }

   }

   private void processStartElement() throws IOException {
      this.createElement();
      this.current.processStartElement();
      if (this.root == null) {
         this.root = this.current.value;
      }

   }

   private void createElement() throws IOException {
      String var1 = this.xmlStreamReader.getPrefix();
      String var2 = this.xmlStreamReader.getLocalName();
      if (var1 == null) {
         int var3 = var2.lastIndexOf(46);
         if (Character.isLowerCase(var2.charAt(var3 + 1))) {
            String var4 = var2.substring(var3 + 1);
            if (var3 == -1) {
               if (this.loadListener != null) {
                  this.loadListener.beginPropertyElement(var4, (Class)null);
               }

               this.current = new PropertyElement(var4, (Class)null);
            } else {
               Class var5 = this.getType(var2.substring(0, var3));
               if (var5 != null) {
                  if (this.loadListener != null) {
                     this.loadListener.beginPropertyElement(var4, var5);
                  }

                  this.current = new PropertyElement(var4, var5);
               } else {
                  if (!this.staticLoad) {
                     throw this.constructLoadException(var2 + " is not a valid property.");
                  }

                  if (this.loadListener != null) {
                     this.loadListener.beginUnknownStaticPropertyElement(var2);
                  }

                  this.current = new UnknownStaticPropertyElement();
               }
            }
         } else {
            if (this.current == null && this.root != null) {
               throw this.constructLoadException("Root value already specified.");
            }

            Class var6 = this.getType(var2);
            if (var6 != null) {
               if (this.loadListener != null) {
                  this.loadListener.beginInstanceDeclarationElement(var6);
               }

               this.current = new InstanceDeclarationElement(var6);
            } else {
               if (!this.staticLoad) {
                  throw this.constructLoadException(var2 + " is not a valid type.");
               }

               if (this.loadListener != null) {
                  this.loadListener.beginUnknownTypeElement(var2);
               }

               this.current = new UnknownTypeElement();
            }
         }
      } else {
         if (!var1.equals("fx")) {
            throw this.constructLoadException("Unexpected namespace prefix: " + var1 + ".");
         }

         if (var2.equals("include")) {
            if (this.loadListener != null) {
               this.loadListener.beginIncludeElement();
            }

            this.current = new IncludeElement();
         } else if (var2.equals("reference")) {
            if (this.loadListener != null) {
               this.loadListener.beginReferenceElement();
            }

            this.current = new ReferenceElement();
         } else if (var2.equals("copy")) {
            if (this.loadListener != null) {
               this.loadListener.beginCopyElement();
            }

            this.current = new CopyElement();
         } else if (var2.equals("root")) {
            if (this.loadListener != null) {
               this.loadListener.beginRootElement();
            }

            this.current = new RootElement();
         } else if (var2.equals("script")) {
            if (this.loadListener != null) {
               this.loadListener.beginScriptElement();
            }

            this.current = new ScriptElement();
         } else {
            if (!var2.equals("define")) {
               throw this.constructLoadException(var1 + ":" + var2 + " is not a valid element.");
            }

            if (this.loadListener != null) {
               this.loadListener.beginDefineElement();
            }

            this.current = new DefineElement();
         }
      }

   }

   private void processEndElement() throws IOException {
      this.current.processEndElement();
      if (this.loadListener != null) {
         this.loadListener.endElement(this.current.value);
      }

      this.current = this.current.parent;
   }

   private void processCharacters() throws IOException {
      if (!this.xmlStreamReader.isWhiteSpace()) {
         this.current.processCharacters();
      }

   }

   private void importPackage(String var1) throws LoadException {
      this.packages.add(var1);
   }

   private void importClass(String var1) throws LoadException {
      try {
         this.loadType(var1, true);
      } catch (ClassNotFoundException var3) {
         throw this.constructLoadException((Throwable)var3);
      }
   }

   private Class getType(String var1) throws LoadException {
      Class var2 = null;
      if (Character.isLowerCase(var1.charAt(0))) {
         try {
            var2 = this.loadType(var1, false);
         } catch (ClassNotFoundException var7) {
         }
      } else {
         var2 = (Class)this.classes.get(var1);
         if (var2 == null) {
            Iterator var3 = this.packages.iterator();

            while(var3.hasNext()) {
               String var4 = (String)var3.next();

               try {
                  var2 = this.loadTypeForPackage(var4, var1);
               } catch (ClassNotFoundException var6) {
               }

               if (var2 != null) {
                  break;
               }
            }

            if (var2 != null) {
               this.classes.put(var1, var2);
            }
         }
      }

      return var2;
   }

   private Class loadType(String var1, boolean var2) throws ClassNotFoundException {
      int var3 = var1.indexOf(46);

      int var4;
      for(var4 = var1.length(); var3 != -1 && var3 < var4 && Character.isLowerCase(var1.charAt(var3 + 1)); var3 = var1.indexOf(46, var3 + 1)) {
      }

      if (var3 != -1 && var3 != var4) {
         String var5 = var1.substring(0, var3);
         String var6 = var1.substring(var3 + 1);
         Class var7 = this.loadTypeForPackage(var5, var6);
         if (var2) {
            this.classes.put(var6, var7);
         }

         return var7;
      } else {
         throw new ClassNotFoundException();
      }
   }

   private Class loadTypeForPackage(String var1, String var2) throws ClassNotFoundException {
      return this.getClassLoader().loadClass(var1 + "." + var2.replace('.', '$'));
   }

   private static SupportedType toSupportedType(Method var0) {
      SupportedType[] var1 = FXMLLoader.SupportedType.values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SupportedType var4 = var1[var3];
         if (var4.methodIsOfType(var0)) {
            return var4;
         }
      }

      return null;
   }

   private ScriptEngineManager getScriptEngineManager() {
      if (this.scriptEngineManager == null) {
         this.scriptEngineManager = new ScriptEngineManager();
         this.scriptEngineManager.setBindings(new SimpleBindings(this.namespace));
      }

      return this.scriptEngineManager;
   }

   /** @deprecated */
   public static Class loadType(String var0, String var1) throws ClassNotFoundException {
      return loadType(var0 + "." + var1.replace('.', '$'));
   }

   /** @deprecated */
   public static Class loadType(String var0) throws ClassNotFoundException {
      ReflectUtil.checkPackageAccess(var0);
      return Class.forName(var0, true, getDefaultClassLoader());
   }

   private static boolean needsClassLoaderPermissionCheck(ClassLoader var0, ClassLoader var1) {
      if (var0 == var1) {
         return false;
      } else if (var0 == null) {
         return false;
      } else if (var1 == null) {
         return true;
      } else {
         ClassLoader var2 = var1;

         do {
            var2 = var2.getParent();
            if (var0 == var2) {
               return false;
            }
         } while(var2 != null);

         return true;
      }
   }

   private static ClassLoader getDefaultClassLoader(Class var0) {
      if (defaultClassLoader == null) {
         SecurityManager var1 = System.getSecurityManager();
         if (var1 != null) {
            ClassLoader var2 = var0 != null ? var0.getClassLoader() : null;
            if (needsClassLoaderPermissionCheck(var2, FXMLLoader.class.getClassLoader())) {
               var1.checkPermission(GET_CLASSLOADER_PERMISSION);
            }
         }

         return Thread.currentThread().getContextClassLoader();
      } else {
         return defaultClassLoader;
      }
   }

   @CallerSensitive
   public static ClassLoader getDefaultClassLoader() {
      SecurityManager var0 = System.getSecurityManager();
      Class var1 = var0 != null ? Reflection.getCallerClass() : null;
      return getDefaultClassLoader(var1);
   }

   public static void setDefaultClassLoader(ClassLoader var0) {
      if (var0 == null) {
         throw new NullPointerException();
      } else {
         SecurityManager var1 = System.getSecurityManager();
         if (var1 != null) {
            var1.checkPermission(new AllPermission());
         }

         defaultClassLoader = var0;
      }
   }

   @CallerSensitive
   public static Object load(URL var0) throws IOException {
      return loadImpl(var0, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private static Object loadImpl(URL var0, Class var1) throws IOException {
      return loadImpl(var0, (ResourceBundle)null, var1);
   }

   @CallerSensitive
   public static Object load(URL var0, ResourceBundle var1) throws IOException {
      return loadImpl(var0, var1, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private static Object loadImpl(URL var0, ResourceBundle var1, Class var2) throws IOException {
      return loadImpl(var0, var1, (BuilderFactory)null, var2);
   }

   @CallerSensitive
   public static Object load(URL var0, ResourceBundle var1, BuilderFactory var2) throws IOException {
      return loadImpl(var0, var1, var2, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private static Object loadImpl(URL var0, ResourceBundle var1, BuilderFactory var2, Class var3) throws IOException {
      return loadImpl(var0, var1, var2, (Callback)null, var3);
   }

   @CallerSensitive
   public static Object load(URL var0, ResourceBundle var1, BuilderFactory var2, Callback var3) throws IOException {
      return loadImpl(var0, var1, var2, var3, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private static Object loadImpl(URL var0, ResourceBundle var1, BuilderFactory var2, Callback var3, Class var4) throws IOException {
      return loadImpl(var0, var1, var2, var3, Charset.forName("UTF-8"), var4);
   }

   @CallerSensitive
   public static Object load(URL var0, ResourceBundle var1, BuilderFactory var2, Callback var3, Charset var4) throws IOException {
      return loadImpl(var0, var1, var2, var3, var4, System.getSecurityManager() != null ? Reflection.getCallerClass() : null);
   }

   private static Object loadImpl(URL var0, ResourceBundle var1, BuilderFactory var2, Callback var3, Charset var4, Class var5) throws IOException {
      if (var0 == null) {
         throw new NullPointerException("Location is required.");
      } else {
         FXMLLoader var6 = new FXMLLoader(var0, var1, var2, var3, var4);
         return var6.loadImpl(var5);
      }
   }

   static int compareJFXVersions(String var0, String var1) {
      int var2 = 0;
      if (var0 != null && !"".equals(var0) && var1 != null && !"".equals(var1)) {
         if (var0.equals(var1)) {
            return var2;
         } else {
            int var3 = var0.indexOf("-");
            if (var3 > 0) {
               var0 = var0.substring(0, var3);
            }

            int var4 = var0.indexOf("_");
            if (var4 > 0) {
               var0 = var0.substring(0, var4);
            }

            if (Pattern.matches("^(\\d+)(\\.\\d+)*$", var0) && Pattern.matches("^(\\d+)(\\.\\d+)*$", var1)) {
               StringTokenizer var5 = new StringTokenizer(var1, ".");
               StringTokenizer var6 = new StringTokenizer(var0, ".");
               int var7 = 0;
               boolean var8 = false;

               boolean var9;
               int var10;
               for(var9 = false; var5.hasMoreTokens() && var2 == 0; var2 = var10 - var7) {
                  var7 = Integer.parseInt(var5.nextToken());
                  if (!var6.hasMoreTokens()) {
                     var9 = true;
                     break;
                  }

                  var10 = Integer.parseInt(var6.nextToken());
               }

               if (var6.hasMoreTokens() && var2 == 0) {
                  var10 = Integer.parseInt(var6.nextToken());
                  if (var10 > 0) {
                     var2 = 1;
                  }
               }

               if (var9) {
                  if (var7 > 0) {
                     var2 = -1;
                  } else {
                     while(var5.hasMoreTokens()) {
                        var7 = Integer.parseInt(var5.nextToken());
                        if (var7 > 0) {
                           var2 = -1;
                           break;
                        }
                     }
                  }
               }

               return var2;
            } else {
               return var2;
            }
         }
      } else {
         return var2;
      }
   }

   private static void checkAllPermissions() {
      SecurityManager var0 = System.getSecurityManager();
      if (var0 != null) {
         var0.checkPermission(new AllPermission());
      }

   }

   private static final class ControllerAccessor {
      private static final int PUBLIC = 1;
      private static final int PROTECTED = 2;
      private static final int PACKAGE = 4;
      private static final int PRIVATE = 8;
      private static final int INITIAL_CLASS_ACCESS = 15;
      private static final int INITIAL_MEMBER_ACCESS = 15;
      private static final int METHODS = 0;
      private static final int FIELDS = 1;
      private Object controller;
      private ClassLoader callerClassLoader;
      private Map controllerFields;
      private Map controllerMethods;

      private ControllerAccessor() {
      }

      void setController(Object var1) {
         if (this.controller != var1) {
            this.controller = var1;
            this.reset();
         }

      }

      void setCallerClass(Class var1) {
         ClassLoader var2 = var1 != null ? var1.getClassLoader() : null;
         if (this.callerClassLoader != var2) {
            this.callerClassLoader = var2;
            this.reset();
         }

      }

      void reset() {
         this.controllerFields = null;
         this.controllerMethods = null;
      }

      Map getControllerFields() {
         if (this.controllerFields == null) {
            this.controllerFields = new HashMap();
            if (this.callerClassLoader == null) {
               FXMLLoader.checkAllPermissions();
            }

            this.addAccessibleMembers(this.controller.getClass(), 15, 15, 1);
         }

         return this.controllerFields;
      }

      Map getControllerMethods() {
         if (this.controllerMethods == null) {
            this.controllerMethods = new EnumMap(SupportedType.class);
            SupportedType[] var1 = FXMLLoader.SupportedType.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
               SupportedType var4 = var1[var3];
               this.controllerMethods.put(var4, new HashMap());
            }

            if (this.callerClassLoader == null) {
               FXMLLoader.checkAllPermissions();
            }

            this.addAccessibleMembers(this.controller.getClass(), 15, 15, 0);
         }

         return this.controllerMethods;
      }

      private void addAccessibleMembers(final Class var1, int var2, int var3, final int var4) {
         if (var1 != Object.class) {
            int var5 = var2;
            final int var6 = var3;
            if (this.callerClassLoader != null && var1.getClassLoader() != this.callerClassLoader) {
               var5 = var2 & 1;
               var6 = var3 & 1;
            }

            int var7 = getAccess(var1.getModifiers());
            if ((var7 & var5) != 0) {
               ReflectUtil.checkPackageAccess(var1);
               this.addAccessibleMembers(var1.getSuperclass(), var5, var6, var4);
               AccessController.doPrivileged(new PrivilegedAction() {
                  public Void run() {
                     if (var4 == 1) {
                        ControllerAccessor.this.addAccessibleFields(var1, var6);
                     } else {
                        ControllerAccessor.this.addAccessibleMethods(var1, var6);
                     }

                     return null;
                  }
               });
            }
         }
      }

      private void addAccessibleFields(Class var1, int var2) {
         boolean var3 = Modifier.isPublic(var1.getModifiers());
         Field[] var4 = var1.getDeclaredFields();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            Field var6 = var4[var5];
            int var7 = var6.getModifiers();
            if ((var7 & 24) == 0 && (getAccess(var7) & var2) != 0) {
               if (!var3 || !Modifier.isPublic(var7)) {
                  if (var6.getAnnotation(FXML.class) == null) {
                     continue;
                  }

                  var6.setAccessible(true);
               }

               Object var8 = (List)this.controllerFields.get(var6.getName());
               if (var8 == null) {
                  var8 = new ArrayList(1);
                  this.controllerFields.put(var6.getName(), var8);
               }

               ((List)var8).add(var6);
            }
         }

      }

      private void addAccessibleMethods(Class var1, int var2) {
         boolean var3 = Modifier.isPublic(var1.getModifiers());
         Method[] var4 = var1.getDeclaredMethods();

         for(int var5 = 0; var5 < var4.length; ++var5) {
            Method var6 = var4[var5];
            int var7 = var6.getModifiers();
            if ((var7 & 264) == 0 && (getAccess(var7) & var2) != 0) {
               if (!var3 || !Modifier.isPublic(var7)) {
                  if (var6.getAnnotation(FXML.class) == null) {
                     continue;
                  }

                  var6.setAccessible(true);
               }

               String var8 = var6.getName();
               SupportedType var9;
               if ((var9 = FXMLLoader.toSupportedType(var6)) != null) {
                  ((Map)this.controllerMethods.get(var9)).put(var8, var6);
               }
            }
         }

      }

      private static int getAccess(int var0) {
         int var1 = var0 & 7;
         switch (var1) {
            case 1:
               return 1;
            case 2:
               return 8;
            case 3:
            default:
               return 4;
            case 4:
               return 2;
         }
      }

      // $FF: synthetic method
      ControllerAccessor(Object var1) {
         this();
      }
   }

   private static enum SupportedType {
      PARAMETERLESS {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 0;
         }
      },
      EVENT {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 1 && Event.class.isAssignableFrom(var1.getParameterTypes()[0]);
         }
      },
      LIST_CHANGE_LISTENER {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 1 && var1.getParameterTypes()[0].equals(ListChangeListener.Change.class);
         }
      },
      MAP_CHANGE_LISTENER {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 1 && var1.getParameterTypes()[0].equals(MapChangeListener.Change.class);
         }
      },
      SET_CHANGE_LISTENER {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 1 && var1.getParameterTypes()[0].equals(SetChangeListener.Change.class);
         }
      },
      PROPERTY_CHANGE_LISTENER {
         protected boolean methodIsOfType(Method var1) {
            return var1.getParameterTypes().length == 3 && ObservableValue.class.isAssignableFrom(var1.getParameterTypes()[0]) && var1.getParameterTypes()[1].equals(var1.getParameterTypes()[2]);
         }
      };

      private SupportedType() {
      }

      protected abstract boolean methodIsOfType(Method var1);

      // $FF: synthetic method
      SupportedType(Object var3) {
         this();
      }
   }

   private static class MethodHandler {
      private final Object controller;
      private final Method method;
      private final SupportedType type;

      private MethodHandler(Object var1, Method var2, SupportedType var3) {
         this.method = var2;
         this.controller = var1;
         this.type = var3;
      }

      public void invoke(Object... var1) {
         try {
            if (this.type != FXMLLoader.SupportedType.PARAMETERLESS) {
               MethodUtil.invoke(this.method, this.controller, var1);
            } else {
               MethodUtil.invoke(this.method, this.controller, new Object[0]);
            }

         } catch (InvocationTargetException var3) {
            throw new RuntimeException(var3);
         } catch (IllegalAccessException var4) {
            throw new RuntimeException(var4);
         }
      }

      // $FF: synthetic method
      MethodHandler(Object var1, Method var2, SupportedType var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private static class PropertyChangeAdapter implements ChangeListener {
      public final MethodHandler handler;

      public PropertyChangeAdapter(MethodHandler var1) {
         this.handler = var1;
      }

      public void changed(ObservableValue var1, Object var2, Object var3) {
         this.handler.invoke(var1, var2, var3);
      }
   }

   private static class ObservableSetChangeAdapter implements SetChangeListener {
      public final MethodHandler handler;

      public ObservableSetChangeAdapter(MethodHandler var1) {
         this.handler = var1;
      }

      public void onChanged(SetChangeListener.Change var1) {
         if (this.handler != null) {
            this.handler.invoke(var1);
         }

      }
   }

   private static class ObservableMapChangeAdapter implements MapChangeListener {
      public final MethodHandler handler;

      public ObservableMapChangeAdapter(MethodHandler var1) {
         this.handler = var1;
      }

      public void onChanged(MapChangeListener.Change var1) {
         if (this.handler != null) {
            this.handler.invoke(var1);
         }

      }
   }

   private static class ObservableListChangeAdapter implements ListChangeListener {
      private final MethodHandler handler;

      public ObservableListChangeAdapter(MethodHandler var1) {
         this.handler = var1;
      }

      public void onChanged(ListChangeListener.Change var1) {
         if (this.handler != null) {
            this.handler.invoke(var1);
         }

      }
   }

   private static class ScriptEventHandler implements EventHandler {
      public final String script;
      public final ScriptEngine scriptEngine;

      public ScriptEventHandler(String var1, ScriptEngine var2) {
         this.script = var1;
         this.scriptEngine = var2;
      }

      public void handle(Event var1) {
         Bindings var2 = this.scriptEngine.getBindings(100);
         Bindings var3 = this.scriptEngine.createBindings();
         var3.put("event", var1);
         var3.putAll(var2);
         this.scriptEngine.setBindings(var3, 100);

         try {
            this.scriptEngine.eval(this.script);
         } catch (ScriptException var5) {
            throw new RuntimeException(var5);
         }

         this.scriptEngine.setBindings(var2, 100);
      }
   }

   private static class ControllerMethodEventHandler implements EventHandler {
      private final MethodHandler handler;

      public ControllerMethodEventHandler(MethodHandler var1) {
         this.handler = var1;
      }

      public void handle(Event var1) {
         this.handler.invoke(var1);
      }
   }

   private static class Attribute {
      public final String name;
      public final Class sourceType;
      public final String value;

      public Attribute(String var1, Class var2, String var3) {
         this.name = var1;
         this.sourceType = var2;
         this.value = var3;
      }
   }

   private class DefineElement extends Element {
      private DefineElement() {
         super();
      }

      public boolean isCollection() {
         return true;
      }

      public void add(Object var1) {
      }

      public void processAttribute(String var1, String var2, String var3) throws LoadException {
         throw FXMLLoader.this.constructLoadException("Element does not support attributes.");
      }

      // $FF: synthetic method
      DefineElement(Object var2) {
         this();
      }
   }

   private class ScriptElement extends Element {
      public String source;
      public Charset charset;

      private ScriptElement() {
         super();
         this.source = null;
         this.charset = FXMLLoader.this.charset;
      }

      public boolean isCollection() {
         return false;
      }

      public void processStartElement() throws IOException {
         super.processStartElement();
         if (this.source != null && !FXMLLoader.this.staticLoad) {
            int var1 = this.source.lastIndexOf(".");
            if (var1 == -1) {
               throw FXMLLoader.this.constructLoadException("Cannot determine type of script \"" + this.source + "\".");
            }

            String var2 = this.source.substring(var1 + 1);
            ClassLoader var4 = FXMLLoader.this.getClassLoader();
            ScriptEngine var3;
            if (FXMLLoader.this.scriptEngine != null && FXMLLoader.this.scriptEngine.getFactory().getExtensions().contains(var2)) {
               var3 = FXMLLoader.this.scriptEngine;
            } else {
               ClassLoader var5 = Thread.currentThread().getContextClassLoader();

               try {
                  Thread.currentThread().setContextClassLoader(var4);
                  ScriptEngineManager var6 = FXMLLoader.this.getScriptEngineManager();
                  var3 = var6.getEngineByExtension(var2);
               } finally {
                  Thread.currentThread().setContextClassLoader(var5);
               }
            }

            if (var3 == null) {
               throw FXMLLoader.this.constructLoadException("Unable to locate scripting engine for extension " + var2 + ".");
            }

            try {
               URL var21;
               if (this.source.charAt(0) == '/') {
                  var21 = var4.getResource(this.source.substring(1));
               } else {
                  if (FXMLLoader.this.location == null) {
                     throw FXMLLoader.this.constructLoadException("Base location is undefined.");
                  }

                  var21 = new URL(FXMLLoader.this.location, this.source);
               }

               InputStreamReader var22 = null;

               try {
                  var22 = new InputStreamReader(var21.openStream(), this.charset);
                  var3.eval(var22);
               } catch (ScriptException var17) {
                  var17.printStackTrace();
               } finally {
                  if (var22 != null) {
                     var22.close();
                  }

               }
            } catch (IOException var20) {
               throw FXMLLoader.this.constructLoadException((Throwable)var20);
            }
         }

      }

      public void processEndElement() throws IOException {
         super.processEndElement();
         if (this.value != null && !FXMLLoader.this.staticLoad) {
            try {
               FXMLLoader.this.scriptEngine.eval((String)this.value);
            } catch (ScriptException var2) {
               System.err.println(var2.getMessage());
            }
         }

      }

      public void processCharacters() throws LoadException {
         if (this.source != null) {
            throw FXMLLoader.this.constructLoadException("Script source already specified.");
         } else if (FXMLLoader.this.scriptEngine == null && !FXMLLoader.this.staticLoad) {
            throw FXMLLoader.this.constructLoadException("Page language not specified.");
         } else {
            this.updateValue(FXMLLoader.this.xmlStreamReader.getText());
         }
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null && var2.equals("source")) {
            if (FXMLLoader.this.loadListener != null) {
               FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
            }

            this.source = var3;
         } else {
            if (!var2.equals("charset")) {
               throw FXMLLoader.this.constructLoadException(var1 == null ? var2 : var1 + ":" + var2 + " is not a valid attribute.");
            }

            if (FXMLLoader.this.loadListener != null) {
               FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
            }

            this.charset = Charset.forName(var3);
         }

      }

      // $FF: synthetic method
      ScriptElement(Object var2) {
         this();
      }
   }

   private class UnknownStaticPropertyElement extends Element {
      public UnknownStaticPropertyElement() throws LoadException {
         super();
         if (this.parent == null) {
            throw FXMLLoader.this.constructLoadException("Invalid root element.");
         } else if (this.parent.value == null) {
            throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
         }
      }

      public boolean isCollection() {
         return false;
      }

      public void set(Object var1) {
         this.updateValue(var1);
      }

      public void processCharacters() throws IOException {
         String var1 = FXMLLoader.this.xmlStreamReader.getText();
         var1 = FXMLLoader.extraneousWhitespacePattern.matcher(var1).replaceAll(" ");
         this.updateValue(var1.trim());
      }
   }

   private class PropertyElement extends Element {
      public final String name;
      public final Class sourceType;
      public final boolean readOnly;

      public PropertyElement(String var2, Class var3) throws LoadException {
         super();
         if (this.parent == null) {
            throw FXMLLoader.this.constructLoadException("Invalid root element.");
         } else if (this.parent.value == null) {
            throw FXMLLoader.this.constructLoadException("Parent element does not support property elements.");
         } else {
            this.name = var2;
            this.sourceType = var3;
            if (var3 == null) {
               if (var2.startsWith("on")) {
                  throw FXMLLoader.this.constructLoadException("\"" + var2 + "\" is not a valid element name.");
               }

               Map var4 = this.parent.getProperties();
               if (this.parent.isTyped()) {
                  this.readOnly = this.parent.getValueAdapter().isReadOnly(var2);
               } else {
                  this.readOnly = var4.containsKey(var2);
               }

               if (this.readOnly) {
                  Object var5 = var4.get(var2);
                  if (var5 == null) {
                     throw FXMLLoader.this.constructLoadException("Invalid property.");
                  }

                  this.updateValue(var5);
               }
            } else {
               this.readOnly = false;
            }

         }
      }

      public boolean isCollection() {
         return this.readOnly ? super.isCollection() : false;
      }

      public void add(Object var1) throws LoadException {
         if (this.parent.isTyped()) {
            Type var2 = this.parent.getValueAdapter().getGenericType(this.name);
            var1 = BeanAdapter.coerce(var1, BeanAdapter.getListItemType(var2));
         }

         super.add(var1);
      }

      public void set(Object var1) throws LoadException {
         this.updateValue(var1);
         if (this.sourceType == null) {
            this.parent.getProperties().put(this.name, var1);
         } else if (this.parent.value instanceof Builder) {
            this.parent.staticPropertyElements.add(this);
         } else {
            BeanAdapter.put(this.parent.value, this.sourceType, this.name, var1);
         }

      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (!this.readOnly) {
            throw FXMLLoader.this.constructLoadException("Attributes are not supported for writable property elements.");
         } else {
            super.processAttribute(var1, var2, var3);
         }
      }

      public void processEndElement() throws IOException {
         super.processEndElement();
         if (this.readOnly) {
            this.processInstancePropertyAttributes();
            this.processEventHandlerAttributes();
         }

      }

      public void processCharacters() throws IOException {
         String var1 = FXMLLoader.this.xmlStreamReader.getText();
         var1 = FXMLLoader.extraneousWhitespacePattern.matcher(var1).replaceAll(" ").trim();
         if (this.readOnly) {
            if (this.isCollection()) {
               this.add(var1);
            } else {
               super.processCharacters();
            }
         } else {
            this.set(var1);
         }

      }
   }

   private class RootElement extends ValueElement {
      public String type;

      private RootElement() {
         super(null);
         this.type = null;
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null) {
            if (var2.equals("type")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.type = var3;
            } else {
               super.processAttribute(var1, var2, var3);
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public Object constructValue() throws LoadException {
         if (this.type == null) {
            throw FXMLLoader.this.constructLoadException("type is required.");
         } else {
            Class var1 = FXMLLoader.this.getType(this.type);
            if (var1 == null) {
               throw FXMLLoader.this.constructLoadException(this.type + " is not a valid type.");
            } else {
               Object var2;
               if (FXMLLoader.this.root == null) {
                  if (!FXMLLoader.this.staticLoad) {
                     throw FXMLLoader.this.constructLoadException("Root hasn't been set. Use method setRoot() before load.");
                  }

                  var2 = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(var1);
                  if (var2 == null) {
                     var2 = FXMLLoader.DEFAULT_BUILDER_FACTORY.getBuilder(var1);
                  }

                  if (var2 == null) {
                     try {
                        var2 = ReflectUtil.newInstance(var1);
                     } catch (InstantiationException var4) {
                        throw FXMLLoader.this.constructLoadException((Throwable)var4);
                     } catch (IllegalAccessException var5) {
                        throw FXMLLoader.this.constructLoadException((Throwable)var5);
                     }
                  }

                  FXMLLoader.this.root = var2;
               } else {
                  if (!var1.isAssignableFrom(FXMLLoader.this.root.getClass())) {
                     throw FXMLLoader.this.constructLoadException("Root is not an instance of " + var1.getName() + ".");
                  }

                  var2 = FXMLLoader.this.root;
               }

               return var2;
            }
         }
      }

      // $FF: synthetic method
      RootElement(Object var2) {
         this();
      }
   }

   private class CopyElement extends ValueElement {
      public String source;

      private CopyElement() {
         super(null);
         this.source = null;
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null) {
            if (var2.equals("source")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.source = var3;
            } else {
               super.processAttribute(var1, var2, var3);
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public Object constructValue() throws LoadException {
         if (this.source == null) {
            throw FXMLLoader.this.constructLoadException("source is required.");
         } else {
            KeyPath var1 = KeyPath.parse(this.source);
            if (!Expression.isDefined(FXMLLoader.this.namespace, (KeyPath)var1)) {
               throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            } else {
               Object var2 = Expression.get(FXMLLoader.this.namespace, (KeyPath)var1);
               Class var3 = var2.getClass();
               Constructor var4 = null;

               try {
                  var4 = ConstructorUtil.getConstructor(var3, new Class[]{var3});
               } catch (NoSuchMethodException var10) {
               }

               if (var4 != null) {
                  try {
                     ReflectUtil.checkPackageAccess(var3);
                     Object var5 = var4.newInstance(var2);
                     return var5;
                  } catch (InstantiationException var7) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var7);
                  } catch (IllegalAccessException var8) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var8);
                  } catch (InvocationTargetException var9) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var9);
                  }
               } else {
                  throw FXMLLoader.this.constructLoadException("Can't copy value " + var2 + ".");
               }
            }
         }
      }

      // $FF: synthetic method
      CopyElement(Object var2) {
         this();
      }
   }

   private class ReferenceElement extends ValueElement {
      public String source;

      private ReferenceElement() {
         super(null);
         this.source = null;
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null) {
            if (var2.equals("source")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.source = var3;
            } else {
               super.processAttribute(var1, var2, var3);
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public Object constructValue() throws LoadException {
         if (this.source == null) {
            throw FXMLLoader.this.constructLoadException("source is required.");
         } else {
            KeyPath var1 = KeyPath.parse(this.source);
            if (!Expression.isDefined(FXMLLoader.this.namespace, (KeyPath)var1)) {
               throw FXMLLoader.this.constructLoadException("Value \"" + this.source + "\" does not exist.");
            } else {
               return Expression.get(FXMLLoader.this.namespace, (KeyPath)var1);
            }
         }
      }

      // $FF: synthetic method
      ReferenceElement(Object var2) {
         this();
      }
   }

   private class IncludeElement extends ValueElement {
      public String source;
      public ResourceBundle resources;
      public Charset charset;

      private IncludeElement() {
         super(null);
         this.source = null;
         this.resources = FXMLLoader.this.resources;
         this.charset = FXMLLoader.this.charset;
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null) {
            if (var2.equals("source")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.source = var3;
            } else if (var2.equals("resources")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.resources = ResourceBundle.getBundle(var3, Locale.getDefault(), FXMLLoader.this.resources.getClass().getClassLoader());
            } else if (var2.equals("charset")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readInternalAttribute(var2, var3);
               }

               this.charset = Charset.forName(var3);
            } else {
               super.processAttribute(var1, var2, var3);
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public Object constructValue() throws IOException {
         if (this.source == null) {
            throw FXMLLoader.this.constructLoadException("source is required.");
         } else {
            ClassLoader var2 = FXMLLoader.this.getClassLoader();
            URL var1;
            if (this.source.charAt(0) == '/') {
               var1 = var2.getResource(this.source.substring(1));
               if (var1 == null) {
                  throw FXMLLoader.this.constructLoadException("Cannot resolve path: " + this.source);
               }
            } else {
               if (FXMLLoader.this.location == null) {
                  throw FXMLLoader.this.constructLoadException("Base location is undefined.");
               }

               var1 = new URL(FXMLLoader.this.location, this.source);
            }

            FXMLLoader var3 = new FXMLLoader(var1, this.resources, FXMLLoader.this.builderFactory, FXMLLoader.this.controllerFactory, this.charset, FXMLLoader.this.loaders);
            var3.parentLoader = FXMLLoader.this;
            if (FXMLLoader.this.isCyclic(FXMLLoader.this, var3)) {
               throw new IOException(String.format("Including \"%s\" in \"%s\" created cyclic reference.", var3.location.toExternalForm(), FXMLLoader.this.location.toExternalForm()));
            } else {
               var3.setClassLoader(var2);
               var3.impl_setStaticLoad(FXMLLoader.this.staticLoad);
               Object var4 = var3.loadImpl(FXMLLoader.this.callerClass);
               if (this.fx_id != null) {
                  String var5 = this.fx_id + "Controller";
                  Object var6 = var3.getController();
                  FXMLLoader.this.namespace.put(var5, var6);
                  FXMLLoader.this.injectFields(var5, var6);
               }

               return var4;
            }
         }
      }

      // $FF: synthetic method
      IncludeElement(Object var2) {
         this();
      }
   }

   private class UnknownTypeElement extends ValueElement {
      private UnknownTypeElement() {
         super(null);
      }

      public void processEndElement() throws IOException {
      }

      public Object constructValue() throws LoadException {
         return new UnknownValueMap();
      }

      // $FF: synthetic method
      UnknownTypeElement(Object var2) {
         this();
      }

      @DefaultProperty("items")
      public class UnknownValueMap extends AbstractMap {
         private ArrayList items = new ArrayList();
         private HashMap values = new HashMap();

         public Object get(Object var1) {
            if (var1 == null) {
               throw new NullPointerException();
            } else {
               return var1.equals(((DefaultProperty)this.getClass().getAnnotation(DefaultProperty.class)).value()) ? this.items : this.values.get(var1);
            }
         }

         public Object put(String var1, Object var2) {
            if (var1 == null) {
               throw new NullPointerException();
            } else if (var1.equals(((DefaultProperty)this.getClass().getAnnotation(DefaultProperty.class)).value())) {
               throw new IllegalArgumentException();
            } else {
               return this.values.put(var1, var2);
            }
         }

         public Set entrySet() {
            return Collections.emptySet();
         }
      }
   }

   private class InstanceDeclarationElement extends ValueElement {
      public Class type;
      public String constant = null;
      public String factory = null;

      public InstanceDeclarationElement(Class var2) throws LoadException {
         super(null);
         this.type = var2;
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 != null && var1.equals("fx")) {
            if (var2.equals("value")) {
               this.value = var3;
            } else if (var2.equals("constant")) {
               this.constant = var3;
            } else if (var2.equals("factory")) {
               this.factory = var3;
            } else {
               super.processAttribute(var1, var2, var3);
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public Object constructValue() throws IOException {
         Object var1;
         if (this.value != null) {
            var1 = BeanAdapter.coerce(this.value, this.type);
         } else if (this.constant != null) {
            var1 = BeanAdapter.getConstantValue(this.type, this.constant);
         } else if (this.factory != null) {
            Method var2;
            try {
               var2 = MethodUtil.getMethod(this.type, this.factory, new Class[0]);
            } catch (NoSuchMethodException var8) {
               throw FXMLLoader.this.constructLoadException((Throwable)var8);
            }

            try {
               var1 = MethodUtil.invoke(var2, (Object)null, new Object[0]);
            } catch (IllegalAccessException var6) {
               throw FXMLLoader.this.constructLoadException((Throwable)var6);
            } catch (InvocationTargetException var7) {
               throw FXMLLoader.this.constructLoadException((Throwable)var7);
            }
         } else {
            var1 = FXMLLoader.this.builderFactory == null ? null : FXMLLoader.this.builderFactory.getBuilder(this.type);
            if (var1 == null) {
               var1 = FXMLLoader.DEFAULT_BUILDER_FACTORY.getBuilder(this.type);
            }

            if (var1 == null) {
               try {
                  var1 = ReflectUtil.newInstance(this.type);
               } catch (InstantiationException var4) {
                  throw FXMLLoader.this.constructLoadException((Throwable)var4);
               } catch (IllegalAccessException var5) {
                  throw FXMLLoader.this.constructLoadException((Throwable)var5);
               }
            }
         }

         return var1;
      }
   }

   private abstract class ValueElement extends Element {
      public String fx_id;

      private ValueElement() {
         super();
         this.fx_id = null;
      }

      public void processStartElement() throws IOException {
         super.processStartElement();
         this.updateValue(this.constructValue());
         if (this.value instanceof Builder) {
            this.processInstancePropertyAttributes();
         } else {
            this.processValue();
         }

      }

      public void processEndElement() throws IOException {
         super.processEndElement();
         if (this.value instanceof Builder) {
            Builder var1 = (Builder)this.value;
            this.updateValue(var1.build());
            this.processValue();
         } else {
            this.processInstancePropertyAttributes();
         }

         this.processEventHandlerAttributes();
         Iterator var3;
         if (this.staticPropertyAttributes.size() > 0) {
            var3 = this.staticPropertyAttributes.iterator();

            while(var3.hasNext()) {
               Attribute var2 = (Attribute)var3.next();
               this.processPropertyAttribute(var2);
            }
         }

         if (this.staticPropertyElements.size() > 0) {
            var3 = this.staticPropertyElements.iterator();

            while(var3.hasNext()) {
               PropertyElement var4 = (PropertyElement)var3.next();
               BeanAdapter.put(this.value, var4.sourceType, var4.name, var4.value);
            }
         }

         if (this.parent != null) {
            if (this.parent.isCollection()) {
               this.parent.add(this.value);
            } else {
               this.parent.set(this.value);
            }
         }

      }

      private Object getListValue(Element var1, String var2, Object var3) {
         if (var1.isTyped()) {
            Type var4 = var1.getValueAdapter().getGenericType(var2);
            if (var4 != null) {
               Type var5 = BeanAdapter.getGenericListItemType(var4);
               if (var5 instanceof ParameterizedType) {
                  var5 = ((ParameterizedType)var5).getRawType();
               }

               var3 = BeanAdapter.coerce(var3, (Class)var5);
            }
         }

         return var3;
      }

      private void processValue() throws LoadException {
         if (this.parent == null) {
            FXMLLoader.this.root = this.value;
            String var1 = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI("fx");
            String var2;
            if (var1 != null) {
               var2 = var1.substring(var1.lastIndexOf("/") + 1);
               if (FXMLLoader.compareJFXVersions("1", var2) < 0) {
                  throw FXMLLoader.this.constructLoadException("Loading FXML document of version " + var2 + " by JavaFX runtime supporting version " + "1");
               }
            }

            var2 = FXMLLoader.this.xmlStreamReader.getNamespaceContext().getNamespaceURI("");
            if (var2 != null) {
               String var3 = var2.substring(var2.lastIndexOf("/") + 1);
               if (FXMLLoader.compareJFXVersions(FXMLLoader.JAVAFX_VERSION, var3) < 0) {
                  Logging.getJavaFXLogger().warning("Loading FXML document with JavaFX API of version " + var3 + " by JavaFX runtime of version " + FXMLLoader.JAVAFX_VERSION);
               }
            }
         }

         if (this.fx_id != null) {
            FXMLLoader.this.namespace.put(this.fx_id, this.value);
            IDProperty var4 = (IDProperty)this.value.getClass().getAnnotation(IDProperty.class);
            if (var4 != null) {
               Map var5 = this.getProperties();
               if (var5.get(var4.value()) == null) {
                  var5.put(var4.value(), this.fx_id);
               }
            }

            FXMLLoader.this.injectFields(this.fx_id, this.value);
         }

      }

      public void processCharacters() throws LoadException {
         Class var1 = this.value.getClass();
         DefaultProperty var2 = (DefaultProperty)var1.getAnnotation(DefaultProperty.class);
         if (var2 == null) {
            throw FXMLLoader.this.constructLoadException(var1.getName() + " does not have a default property.");
         } else {
            String var3 = FXMLLoader.this.xmlStreamReader.getText();
            var3 = FXMLLoader.extraneousWhitespacePattern.matcher(var3).replaceAll(" ");
            String var4 = var2.value();
            BeanAdapter var5 = this.getValueAdapter();
            if (var5.isReadOnly(var4) && List.class.isAssignableFrom(var5.getType(var4))) {
               List var6 = (List)var5.get((Object)var4);
               var6.add(this.getListValue(this, var4, var3));
            } else {
               var5.put((String)var4, var3.trim());
            }

         }
      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 != null && var1.equals("fx")) {
            if (var2.equals("id")) {
               if (var3.equals("null")) {
                  throw FXMLLoader.this.constructLoadException("Invalid identifier.");
               }

               int var4 = 0;

               for(int var5 = var3.length(); var4 < var5; ++var4) {
                  if (!Character.isJavaIdentifierPart(var3.charAt(var4))) {
                     throw FXMLLoader.this.constructLoadException("Invalid identifier.");
                  }
               }

               this.fx_id = var3;
            } else {
               if (!var2.equals("controller")) {
                  throw FXMLLoader.this.constructLoadException("Invalid attribute.");
               }

               if (FXMLLoader.this.current.parent != null) {
                  throw FXMLLoader.this.constructLoadException("fx:controller can only be applied to root element.");
               }

               if (FXMLLoader.this.controller != null) {
                  throw FXMLLoader.this.constructLoadException("Controller value already specified.");
               }

               if (!FXMLLoader.this.staticLoad) {
                  Class var9;
                  try {
                     var9 = FXMLLoader.this.getClassLoader().loadClass(var3);
                  } catch (ClassNotFoundException var8) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var8);
                  }

                  try {
                     if (FXMLLoader.this.controllerFactory == null) {
                        FXMLLoader.this.setController(ReflectUtil.newInstance(var9));
                     } else {
                        FXMLLoader.this.setController(FXMLLoader.this.controllerFactory.call(var9));
                     }
                  } catch (InstantiationException var6) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var6);
                  } catch (IllegalAccessException var7) {
                     throw FXMLLoader.this.constructLoadException((Throwable)var7);
                  }
               }
            }
         } else {
            super.processAttribute(var1, var2, var3);
         }

      }

      public abstract Object constructValue() throws IOException;

      // $FF: synthetic method
      ValueElement(Object var2) {
         this();
      }
   }

   private abstract class Element {
      public final Element parent;
      public Object value = null;
      private BeanAdapter valueAdapter = null;
      public final LinkedList eventHandlerAttributes = new LinkedList();
      public final LinkedList instancePropertyAttributes = new LinkedList();
      public final LinkedList staticPropertyAttributes = new LinkedList();
      public final LinkedList staticPropertyElements = new LinkedList();

      public Element() {
         this.parent = FXMLLoader.this.current;
      }

      public boolean isCollection() {
         boolean var1;
         if (this.value instanceof List) {
            var1 = true;
         } else {
            Class var2 = this.value.getClass();
            DefaultProperty var3 = (DefaultProperty)var2.getAnnotation(DefaultProperty.class);
            if (var3 != null) {
               var1 = this.getProperties().get(var3.value()) instanceof List;
            } else {
               var1 = false;
            }
         }

         return var1;
      }

      public void add(Object var1) throws LoadException {
         List var2;
         if (this.value instanceof List) {
            var2 = (List)this.value;
         } else {
            Class var3 = this.value.getClass();
            DefaultProperty var4 = (DefaultProperty)var3.getAnnotation(DefaultProperty.class);
            String var5 = var4.value();
            var2 = (List)this.getProperties().get(var5);
            if (!Map.class.isAssignableFrom(var3)) {
               Type var6 = this.getValueAdapter().getGenericType(var5);
               var1 = BeanAdapter.coerce(var1, BeanAdapter.getListItemType(var6));
            }
         }

         var2.add(var1);
      }

      public void set(Object var1) throws LoadException {
         if (this.value == null) {
            throw FXMLLoader.this.constructLoadException("Cannot set value on this element.");
         } else {
            Class var2 = this.value.getClass();
            DefaultProperty var3 = (DefaultProperty)var2.getAnnotation(DefaultProperty.class);
            if (var3 == null) {
               throw FXMLLoader.this.constructLoadException("Element does not define a default property.");
            } else {
               this.getProperties().put(var3.value(), var1);
            }
         }
      }

      public void updateValue(Object var1) {
         this.value = var1;
         this.valueAdapter = null;
      }

      public boolean isTyped() {
         return !(this.value instanceof Map);
      }

      public BeanAdapter getValueAdapter() {
         if (this.valueAdapter == null) {
            this.valueAdapter = new BeanAdapter(this.value);
         }

         return this.valueAdapter;
      }

      public Map getProperties() {
         return (Map)(this.isTyped() ? this.getValueAdapter() : (Map)this.value);
      }

      public void processStartElement() throws IOException {
         int var1 = 0;

         for(int var2 = FXMLLoader.this.xmlStreamReader.getAttributeCount(); var1 < var2; ++var1) {
            String var3 = FXMLLoader.this.xmlStreamReader.getAttributePrefix(var1);
            String var4 = FXMLLoader.this.xmlStreamReader.getAttributeLocalName(var1);
            String var5 = FXMLLoader.this.xmlStreamReader.getAttributeValue(var1);
            if (FXMLLoader.this.loadListener != null && var3 != null && var3.equals("fx")) {
               FXMLLoader.this.loadListener.readInternalAttribute(var3 + ":" + var4, var5);
            }

            this.processAttribute(var3, var4, var5);
         }

      }

      public void processEndElement() throws IOException {
      }

      public void processCharacters() throws IOException {
         throw FXMLLoader.this.constructLoadException("Unexpected characters in input stream.");
      }

      public void processInstancePropertyAttributes() throws IOException {
         if (this.instancePropertyAttributes.size() > 0) {
            Iterator var1 = this.instancePropertyAttributes.iterator();

            while(var1.hasNext()) {
               Attribute var2 = (Attribute)var1.next();
               this.processPropertyAttribute(var2);
            }
         }

      }

      public void processAttribute(String var1, String var2, String var3) throws IOException {
         if (var1 == null) {
            if (var2.startsWith("on")) {
               if (FXMLLoader.this.loadListener != null) {
                  FXMLLoader.this.loadListener.readEventHandlerAttribute(var2, var3);
               }

               this.eventHandlerAttributes.add(new Attribute(var2, (Class)null, var3));
            } else {
               int var4 = var2.lastIndexOf(46);
               if (var4 == -1) {
                  if (FXMLLoader.this.loadListener != null) {
                     FXMLLoader.this.loadListener.readPropertyAttribute(var2, (Class)null, var3);
                  }

                  this.instancePropertyAttributes.add(new Attribute(var2, (Class)null, var3));
               } else {
                  String var5 = var2.substring(var4 + 1);
                  Class var6 = FXMLLoader.this.getType(var2.substring(0, var4));
                  if (var6 != null) {
                     if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readPropertyAttribute(var5, var6, var3);
                     }

                     this.staticPropertyAttributes.add(new Attribute(var5, var6, var3));
                  } else {
                     if (!FXMLLoader.this.staticLoad) {
                        throw FXMLLoader.this.constructLoadException(var2 + " is not a valid attribute.");
                     }

                     if (FXMLLoader.this.loadListener != null) {
                        FXMLLoader.this.loadListener.readUnknownStaticPropertyAttribute(var2, var3);
                     }
                  }
               }
            }

         } else {
            throw FXMLLoader.this.constructLoadException(var1 + ":" + var2 + " is not a valid attribute.");
         }
      }

      public void processPropertyAttribute(Attribute var1) throws IOException {
         String var2 = var1.value;
         if (this.isBindingExpression(var2)) {
            if (var1.sourceType != null) {
               throw FXMLLoader.this.constructLoadException("Cannot bind to static property.");
            }

            if (!this.isTyped()) {
               throw FXMLLoader.this.constructLoadException("Cannot bind to untyped object.");
            }

            if (this.value instanceof Builder) {
               throw FXMLLoader.this.constructLoadException("Cannot bind to builder property.");
            }

            if (!FXMLLoader.this.impl_isStaticLoad()) {
               var2 = var2.substring("${".length(), var2.length() - 1);
               Expression var3 = Expression.valueOf(var2);
               BeanAdapter var4 = new BeanAdapter(this.value);
               ObservableValue var5 = var4.getPropertyModel(var1.name);
               Class var6 = var4.getType(var1.name);
               if (var5 instanceof Property) {
                  ((Property)var5).bind(new ExpressionValue(FXMLLoader.this.namespace, var3, var6));
               }
            }
         } else {
            if (this.isBidirectionalBindingExpression(var2)) {
               throw FXMLLoader.this.constructLoadException((Throwable)(new UnsupportedOperationException("This feature is not currently enabled.")));
            }

            this.processValue(var1.sourceType, var1.name, var2);
         }

      }

      private boolean isBindingExpression(String var1) {
         return var1.startsWith("${") && var1.endsWith("}");
      }

      private boolean isBidirectionalBindingExpression(String var1) {
         return var1.startsWith("#{");
      }

      private boolean processValue(Class var1, String var2, String var3) throws LoadException {
         boolean var4 = false;
         if (var1 == null && this.isTyped()) {
            BeanAdapter var5 = this.getValueAdapter();
            Class var6 = var5.getType(var2);
            if (var6 == null) {
               throw new PropertyNotFoundException("Property \"" + var2 + "\" does not exist or is read-only.");
            }

            if (List.class.isAssignableFrom(var6) && var5.isReadOnly(var2)) {
               this.populateListFromString(var5, var2, var3);
               var4 = true;
            } else if (var6.isArray()) {
               this.applyProperty(var2, var1, this.populateArrayFromString(var6, var3));
               var4 = true;
            }
         }

         if (!var4) {
            this.applyProperty(var2, var1, this.resolvePrefixedValue(var3));
            var4 = true;
         }

         return var4;
      }

      private Object resolvePrefixedValue(String var1) throws LoadException {
         if (var1.startsWith("\\")) {
            var1 = var1.substring("\\".length());
            if (var1.length() != 0 && (var1.startsWith("\\") || var1.startsWith("@") || var1.startsWith("%") || var1.startsWith("$") || var1.startsWith("#{"))) {
               return var1;
            } else {
               throw FXMLLoader.this.constructLoadException("Invalid escape sequence.");
            }
         } else {
            if (var1.startsWith("@")) {
               var1 = var1.substring("@".length());
               if (var1.length() == 0) {
                  throw FXMLLoader.this.constructLoadException("Missing relative path.");
               }

               if (var1.startsWith("@")) {
                  this.warnDeprecatedEscapeSequence("@");
                  return var1;
               }

               if (var1.charAt(0) == '/') {
                  URL var2 = FXMLLoader.this.getClassLoader().getResource(var1.substring(1));
                  if (var2 == null) {
                     throw FXMLLoader.this.constructLoadException("Invalid resource: " + var1 + " not found on the classpath");
                  }

                  return var2.toString();
               }

               try {
                  return (new URL(FXMLLoader.this.location, var1)).toString();
               } catch (MalformedURLException var3) {
                  System.err.println(FXMLLoader.this.location + "/" + var1);
               }
            } else {
               if (var1.startsWith("%")) {
                  var1 = var1.substring("%".length());
                  if (var1.length() == 0) {
                     throw FXMLLoader.this.constructLoadException("Missing resource key.");
                  }

                  if (var1.startsWith("%")) {
                     this.warnDeprecatedEscapeSequence("%");
                     return var1;
                  }

                  if (FXMLLoader.this.resources == null) {
                     throw FXMLLoader.this.constructLoadException("No resources specified.");
                  }

                  if (!FXMLLoader.this.resources.containsKey(var1)) {
                     throw FXMLLoader.this.constructLoadException("Resource \"" + var1 + "\" not found.");
                  }

                  return FXMLLoader.this.resources.getString(var1);
               }

               if (var1.startsWith("$")) {
                  var1 = var1.substring("$".length());
                  if (var1.length() == 0) {
                     throw FXMLLoader.this.constructLoadException("Missing expression.");
                  }

                  if (var1.startsWith("$")) {
                     this.warnDeprecatedEscapeSequence("$");
                     return var1;
                  }

                  if (var1.equals("null")) {
                     return null;
                  }

                  return Expression.get(FXMLLoader.this.namespace, (KeyPath)KeyPath.parse(var1));
               }
            }

            return var1;
         }
      }

      private Object populateArrayFromString(Class var1, String var2) throws LoadException {
         Object var3 = null;
         Class var4 = var1.getComponentType();
         if (var2.length() > 0) {
            String[] var5 = var2.split(",");
            var3 = Array.newInstance(var4, var5.length);

            for(int var6 = 0; var6 < var5.length; ++var6) {
               Array.set(var3, var6, BeanAdapter.coerce(this.resolvePrefixedValue(var5[var6].trim()), var1.getComponentType()));
            }
         } else {
            var3 = Array.newInstance(var4, 0);
         }

         return var3;
      }

      private void populateListFromString(BeanAdapter var1, String var2, String var3) throws LoadException {
         List var4 = (List)var1.get((Object)var2);
         Type var5 = var1.getGenericType(var2);
         Object var6 = (Class)BeanAdapter.getGenericListItemType(var5);
         if (var6 instanceof ParameterizedType) {
            var6 = ((ParameterizedType)var6).getRawType();
         }

         if (var3.length() > 0) {
            String[] var7 = var3.split(",");
            String[] var8 = var7;
            int var9 = var7.length;

            for(int var10 = 0; var10 < var9; ++var10) {
               String var11 = var8[var10];
               var11 = var11.trim();
               var4.add(BeanAdapter.coerce(this.resolvePrefixedValue(var11), (Class)var6));
            }
         }

      }

      public void warnDeprecatedEscapeSequence(String var1) {
         System.err.println(var1 + var1 + " is a deprecated escape sequence. Please use \\" + var1 + " instead.");
      }

      public void applyProperty(String var1, Class var2, Object var3) {
         if (var2 == null) {
            this.getProperties().put(var1, var3);
         } else {
            BeanAdapter.put(this.value, var2, var1, var3);
         }

      }

      private Object getExpressionObject(String var1) throws LoadException {
         if (var1.startsWith("$")) {
            var1 = var1.substring("$".length());
            if (var1.length() == 0) {
               throw FXMLLoader.this.constructLoadException("Missing expression reference.");
            } else {
               Object var2 = Expression.get(FXMLLoader.this.namespace, (KeyPath)KeyPath.parse(var1));
               if (var2 == null) {
                  throw FXMLLoader.this.constructLoadException("Unable to resolve expression : $" + var1);
               } else {
                  return var2;
               }
            }
         } else {
            return null;
         }
      }

      private Object getExpressionObjectOfType(String var1, Class var2) throws LoadException {
         Object var3 = this.getExpressionObject(var1);
         if (var3 != null) {
            if (var2.isInstance(var3)) {
               return var3;
            } else {
               throw FXMLLoader.this.constructLoadException("Error resolving \"" + var1 + "\" expression.Does not point to a " + var2.getName());
            }
         } else {
            return null;
         }
      }

      private MethodHandler getControllerMethodHandle(String var1, SupportedType... var2) throws LoadException {
         if (var1.startsWith("#")) {
            var1 = var1.substring("#".length());
            if (!var1.startsWith("#")) {
               if (var1.length() == 0) {
                  throw FXMLLoader.this.constructLoadException("Missing controller method.");
               }

               if (FXMLLoader.this.controller == null) {
                  throw FXMLLoader.this.constructLoadException("No controller specified.");
               }

               SupportedType[] var3 = var2;
               int var4 = var2.length;

               for(int var5 = 0; var5 < var4; ++var5) {
                  SupportedType var6 = var3[var5];
                  Method var7 = (Method)((Map)FXMLLoader.this.controllerAccessor.getControllerMethods().get(var6)).get(var1);
                  if (var7 != null) {
                     return new MethodHandler(FXMLLoader.this.controller, var7, var6);
                  }
               }

               Method var8 = (Method)((Map)FXMLLoader.this.controllerAccessor.getControllerMethods().get(FXMLLoader.SupportedType.PARAMETERLESS)).get(var1);
               if (var8 != null) {
                  return new MethodHandler(FXMLLoader.this.controller, var8, FXMLLoader.SupportedType.PARAMETERLESS);
               }

               return null;
            }
         }

         return null;
      }

      public void processEventHandlerAttributes() throws LoadException {
         if (this.eventHandlerAttributes.size() > 0 && !FXMLLoader.this.staticLoad) {
            Iterator var1 = this.eventHandlerAttributes.iterator();

            while(true) {
               while(var1.hasNext()) {
                  Attribute var2 = (Attribute)var1.next();
                  String var3 = var2.value;
                  if (this.value instanceof ObservableList && var2.name.equals("onChange")) {
                     this.processObservableListHandler(var3);
                  } else if (this.value instanceof ObservableMap && var2.name.equals("onChange")) {
                     this.processObservableMapHandler(var3);
                  } else if (this.value instanceof ObservableSet && var2.name.equals("onChange")) {
                     this.processObservableSetHandler(var3);
                  } else if (var2.name.endsWith("Change")) {
                     this.processPropertyHandler(var2.name, var3);
                  } else {
                     Object var4 = null;
                     MethodHandler var5 = this.getControllerMethodHandle(var3, FXMLLoader.SupportedType.EVENT);
                     if (var5 != null) {
                        var4 = new ControllerMethodEventHandler(var5);
                     }

                     if (var4 == null) {
                        var4 = (EventHandler)this.getExpressionObjectOfType(var3, EventHandler.class);
                     }

                     if (var4 == null) {
                        if (var3.length() == 0 || FXMLLoader.this.scriptEngine == null) {
                           throw FXMLLoader.this.constructLoadException("Error resolving " + var2.name + "='" + var2.value + "', either the event handler is not in the Namespace or there is an error in the script.");
                        }

                        var4 = new ScriptEventHandler(var3, FXMLLoader.this.scriptEngine);
                     }

                     this.getValueAdapter().put(var2.name, var4);
                  }
               }

               return;
            }
         }
      }

      private void processObservableListHandler(String var1) throws LoadException {
         ObservableList var2 = (ObservableList)this.value;
         if (var1.startsWith("#")) {
            MethodHandler var3 = this.getControllerMethodHandle(var1, FXMLLoader.SupportedType.LIST_CHANGE_LISTENER);
            if (var3 == null) {
               throw FXMLLoader.this.constructLoadException("Controller method \"" + var1 + "\" not found.");
            }

            var2.addListener(new ObservableListChangeAdapter(var3));
         } else if (var1.startsWith("$")) {
            Object var4 = this.getExpressionObject(var1);
            if (var4 instanceof ListChangeListener) {
               var2.addListener((ListChangeListener)var4);
            } else {
               if (!(var4 instanceof InvalidationListener)) {
                  throw FXMLLoader.this.constructLoadException("Error resolving \"" + var1 + "\" expression.Must be either ListChangeListener or InvalidationListener");
               }

               var2.addListener((InvalidationListener)var4);
            }
         }

      }

      private void processObservableMapHandler(String var1) throws LoadException {
         ObservableMap var2 = (ObservableMap)this.value;
         if (var1.startsWith("#")) {
            MethodHandler var3 = this.getControllerMethodHandle(var1, FXMLLoader.SupportedType.MAP_CHANGE_LISTENER);
            if (var3 == null) {
               throw FXMLLoader.this.constructLoadException("Controller method \"" + var1 + "\" not found.");
            }

            var2.addListener(new ObservableMapChangeAdapter(var3));
         } else if (var1.startsWith("$")) {
            Object var4 = this.getExpressionObject(var1);
            if (var4 instanceof MapChangeListener) {
               var2.addListener((MapChangeListener)var4);
            } else {
               if (!(var4 instanceof InvalidationListener)) {
                  throw FXMLLoader.this.constructLoadException("Error resolving \"" + var1 + "\" expression.Must be either MapChangeListener or InvalidationListener");
               }

               var2.addListener((InvalidationListener)var4);
            }
         }

      }

      private void processObservableSetHandler(String var1) throws LoadException {
         ObservableSet var2 = (ObservableSet)this.value;
         if (var1.startsWith("#")) {
            MethodHandler var3 = this.getControllerMethodHandle(var1, FXMLLoader.SupportedType.SET_CHANGE_LISTENER);
            if (var3 == null) {
               throw FXMLLoader.this.constructLoadException("Controller method \"" + var1 + "\" not found.");
            }

            var2.addListener(new ObservableSetChangeAdapter(var3));
         } else if (var1.startsWith("$")) {
            Object var4 = this.getExpressionObject(var1);
            if (var4 instanceof SetChangeListener) {
               var2.addListener((SetChangeListener)var4);
            } else {
               if (!(var4 instanceof InvalidationListener)) {
                  throw FXMLLoader.this.constructLoadException("Error resolving \"" + var1 + "\" expression.Must be either SetChangeListener or InvalidationListener");
               }

               var2.addListener((InvalidationListener)var4);
            }
         }

      }

      private void processPropertyHandler(String var1, String var2) throws LoadException {
         int var3 = "on".length();
         int var4 = var1.length() - "Change".length();
         if (var3 != var4) {
            String var5 = Character.toLowerCase(var1.charAt(var3)) + var1.substring(var3 + 1, var4);
            ObservableValue var6 = this.getValueAdapter().getPropertyModel(var5);
            if (var6 == null) {
               throw FXMLLoader.this.constructLoadException(this.value.getClass().getName() + " does not define a property model for \"" + var5 + "\".");
            }

            if (var2.startsWith("#")) {
               final MethodHandler var7 = this.getControllerMethodHandle(var2, FXMLLoader.SupportedType.PROPERTY_CHANGE_LISTENER, FXMLLoader.SupportedType.EVENT);
               if (var7 == null) {
                  throw FXMLLoader.this.constructLoadException("Controller method \"" + var2 + "\" not found.");
               }

               if (var7.type == FXMLLoader.SupportedType.EVENT) {
                  var6.addListener(new ChangeListener() {
                     public void changed(ObservableValue var1, Object var2, Object var3) {
                        var7.invoke(new Event(Element.this.value, (EventTarget)null, Event.ANY));
                     }
                  });
               } else {
                  var6.addListener(new PropertyChangeAdapter(var7));
               }
            } else if (var2.startsWith("$")) {
               Object var8 = this.getExpressionObject(var2);
               if (var8 instanceof ChangeListener) {
                  var6.addListener((ChangeListener)var8);
               } else {
                  if (!(var8 instanceof InvalidationListener)) {
                     throw FXMLLoader.this.constructLoadException("Error resolving \"" + var2 + "\" expression.Must be either ChangeListener or InvalidationListener");
                  }

                  var6.addListener((InvalidationListener)var8);
               }
            }
         }

      }
   }
}
