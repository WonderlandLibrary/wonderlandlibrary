package com.sun.javafx.css;

import com.sun.javafx.css.parser.CSSParser;
import com.sun.javafx.util.Logging;
import java.io.FileNotFoundException;
import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PermissionCollection;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleOrigin;
import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.image.Image;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Window;
import sun.util.logging.PlatformLogger;
import sun.util.logging.PlatformLogger.Level;

public final class StyleManager {
   private static final Object styleLock = new Object();
   private static PlatformLogger LOGGER;
   static final Map cacheContainerMap = new WeakHashMap();
   final List userAgentStylesheetContainers;
   final List platformUserAgentStylesheetContainers;
   boolean hasDefaultUserAgentStylesheet;
   final Map stylesheetContainerMap;
   private final Map imageCache;
   private static final String skinPrefix = "com/sun/javafx/scene/control/skin/";
   private static final String skinUtilsClassName = "com.sun.javafx.scene.control.skin.Utils";
   private Key key;
   private static ObservableList errors = null;
   private static List cacheMapKey;

   private static PlatformLogger getLogger() {
      if (LOGGER == null) {
         LOGGER = Logging.getCSSLogger();
      }

      return LOGGER;
   }

   public static StyleManager getInstance() {
      return StyleManager.InstanceHolder.INSTANCE;
   }

   private StyleManager() {
      this.userAgentStylesheetContainers = new ArrayList();
      this.platformUserAgentStylesheetContainers = new ArrayList();
      this.hasDefaultUserAgentStylesheet = false;
      this.stylesheetContainerMap = new HashMap();
      this.imageCache = new HashMap();
      this.key = null;
   }

   CacheContainer getCacheContainer(Styleable var1, SubScene var2) {
      if (var1 == null && var2 == null) {
         return null;
      } else {
         Parent var3 = null;
         if (var2 != null) {
            var3 = var2.getRoot();
         } else if (var1 instanceof Node) {
            Node var4 = (Node)var1;
            Scene var5 = var4.getScene();
            if (var5 != null) {
               var3 = var5.getRoot();
            }
         } else if (var1 instanceof Window) {
            Scene var9 = ((Window)var1).getScene();
            if (var9 != null) {
               var3 = var9.getRoot();
            }
         }

         if (var3 == null) {
            return null;
         } else {
            synchronized(styleLock) {
               CacheContainer var8 = (CacheContainer)cacheContainerMap.get(var3);
               if (var8 == null) {
                  var8 = new CacheContainer();
                  cacheContainerMap.put(var3, var8);
               }

               return var8;
            }
         }
      }
   }

   public StyleCache getSharedCache(Styleable var1, SubScene var2, StyleCache.Key var3) {
      CacheContainer var4 = this.getCacheContainer(var1, var2);
      if (var4 == null) {
         return null;
      } else {
         Map var5 = var4.getStyleCache();
         if (var5 == null) {
            return null;
         } else {
            StyleCache var6 = (StyleCache)var5.get(var3);
            if (var6 == null) {
               var6 = new StyleCache();
               var5.put(new StyleCache.Key(var3), var6);
            }

            return var6;
         }
      }
   }

   public StyleMap getStyleMap(Styleable var1, SubScene var2, int var3) {
      if (var3 == -1) {
         return StyleMap.EMPTY_MAP;
      } else {
         CacheContainer var4 = this.getCacheContainer(var1, var2);
         return var4 == null ? StyleMap.EMPTY_MAP : var4.getStyleMap(var3);
      }
   }

   public void forget(Scene var1) {
      if (var1 != null) {
         this.forget(var1.getRoot());
         synchronized(styleLock) {
            String var3 = null;
            if (var1.getUserAgentStylesheet() != null && !(var3 = var1.getUserAgentStylesheet().trim()).isEmpty()) {
               for(int var4 = this.userAgentStylesheetContainers.size() - 1; 0 <= var4; --var4) {
                  StylesheetContainer var5 = (StylesheetContainer)this.userAgentStylesheetContainers.get(var4);
                  if (var3.equals(var5.fname)) {
                     var5.parentUsers.remove(var1.getRoot());
                     if (var5.parentUsers.list.size() == 0) {
                        this.userAgentStylesheetContainers.remove(var4);
                     }
                  }
               }
            }

            Set var13 = this.stylesheetContainerMap.entrySet();
            Iterator var14 = var13.iterator();

            label54:
            while(var14.hasNext()) {
               Map.Entry var6 = (Map.Entry)var14.next();
               StylesheetContainer var7 = (StylesheetContainer)var6.getValue();
               Iterator var8 = var7.parentUsers.list.iterator();

               while(true) {
                  Reference var9;
                  Parent var10;
                  do {
                     if (!var8.hasNext()) {
                        if (var7.parentUsers.list.isEmpty()) {
                           var14.remove();
                        }
                        continue label54;
                     }

                     var9 = (Reference)var8.next();
                     var10 = (Parent)var9.get();
                  } while(var10 != null && var10.getScene() != var1 && var10.getScene() != null);

                  var9.clear();
                  var8.remove();
               }
            }

         }
      }
   }

   public void stylesheetsChanged(Scene var1, ListChangeListener.Change var2) {
      synchronized(styleLock) {
         Set var4 = cacheContainerMap.entrySet();
         Iterator var5 = var4.iterator();

         while(var5.hasNext()) {
            Map.Entry var6 = (Map.Entry)var5.next();
            Parent var7 = (Parent)var6.getKey();
            CacheContainer var8 = (CacheContainer)var6.getValue();
            if (var7.getScene() == var1) {
               var8.clearCache();
            }
         }

         var2.reset();

         while(true) {
            do {
               if (!var2.next()) {
                  return;
               }
            } while(!var2.wasRemoved());

            var5 = var2.getRemoved().iterator();

            while(var5.hasNext()) {
               String var11 = (String)var5.next();
               this.stylesheetRemoved(var1, var11);
               StylesheetContainer var12 = (StylesheetContainer)this.stylesheetContainerMap.get(var11);
               if (var12 != null) {
                  var12.invalidateChecksum();
               }
            }
         }
      }
   }

   private void stylesheetRemoved(Scene var1, String var2) {
      this.stylesheetRemoved(var1.getRoot(), var2);
   }

   public void forget(Parent var1) {
      if (var1 != null) {
         synchronized(styleLock) {
            CacheContainer var3 = (CacheContainer)cacheContainerMap.remove(var1);
            if (var3 != null) {
               var3.clearCache();
            }

            ObservableList var4 = var1.getStylesheets();
            Iterator var5;
            if (var4 != null && !var4.isEmpty()) {
               var5 = var4.iterator();

               while(var5.hasNext()) {
                  String var6 = (String)var5.next();
                  this.stylesheetRemoved(var1, var6);
               }
            }

            var5 = this.stylesheetContainerMap.entrySet().iterator();

            while(var5.hasNext()) {
               Map.Entry var11 = (Map.Entry)var5.next();
               StylesheetContainer var7 = (StylesheetContainer)var11.getValue();
               var7.parentUsers.remove(var1);
               if (var7.parentUsers.list.isEmpty()) {
                  var5.remove();
                  if (var7.selectorPartitioning != null) {
                     var7.selectorPartitioning.reset();
                  }

                  String var8 = var7.fname;
                  this.cleanUpImageCache(var8);
               }
            }

         }
      }
   }

   public void stylesheetsChanged(Parent var1, ListChangeListener.Change var2) {
      synchronized(styleLock) {
         var2.reset();

         while(true) {
            do {
               if (!var2.next()) {
                  return;
               }
            } while(!var2.wasRemoved());

            Iterator var4 = var2.getRemoved().iterator();

            while(var4.hasNext()) {
               String var5 = (String)var4.next();
               this.stylesheetRemoved(var1, var5);
               StylesheetContainer var6 = (StylesheetContainer)this.stylesheetContainerMap.get(var5);
               if (var6 != null) {
                  var6.invalidateChecksum();
               }
            }
         }
      }
   }

   private void stylesheetRemoved(Parent var1, String var2) {
      synchronized(styleLock) {
         StylesheetContainer var4 = (StylesheetContainer)this.stylesheetContainerMap.get(var2);
         if (var4 != null) {
            var4.parentUsers.remove(var1);
            if (var4.parentUsers.list.isEmpty()) {
               this.removeStylesheetContainer(var4);
            }

         }
      }
   }

   public void forget(SubScene var1) {
      if (var1 != null) {
         Parent var2 = var1.getRoot();
         if (var2 != null) {
            this.forget(var2);
            synchronized(styleLock) {
               String var4 = null;
               if (var1.getUserAgentStylesheet() != null && !(var4 = var1.getUserAgentStylesheet().trim()).isEmpty()) {
                  Iterator var5 = this.userAgentStylesheetContainers.iterator();

                  while(var5.hasNext()) {
                     StylesheetContainer var6 = (StylesheetContainer)var5.next();
                     if (var4.equals(var6.fname)) {
                        var6.parentUsers.remove(var1.getRoot());
                        if (var6.parentUsers.list.size() == 0) {
                           var5.remove();
                        }
                     }
                  }
               }

               ArrayList var14 = new ArrayList(this.stylesheetContainerMap.values());
               Iterator var15 = var14.iterator();

               label59:
               while(var15.hasNext()) {
                  StylesheetContainer var7 = (StylesheetContainer)var15.next();
                  Iterator var8 = var7.parentUsers.list.iterator();

                  while(true) {
                     while(true) {
                        Reference var9;
                        Parent var10;
                        do {
                           if (!var8.hasNext()) {
                              continue label59;
                           }

                           var9 = (Reference)var8.next();
                           var10 = (Parent)var9.get();
                        } while(var10 == null);

                        for(Parent var11 = var10; var11 != null; var11 = var11.getParent()) {
                           if (var2 == var11.getParent()) {
                              var9.clear();
                              var8.remove();
                              this.forget(var10);
                              break;
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   private void removeStylesheetContainer(StylesheetContainer var1) {
      if (var1 != null) {
         synchronized(styleLock) {
            String var3 = var1.fname;
            this.stylesheetContainerMap.remove(var3);
            if (var1.selectorPartitioning != null) {
               var1.selectorPartitioning.reset();
            }

            Iterator var4 = cacheContainerMap.entrySet().iterator();

            while(true) {
               CacheContainer var6;
               ArrayList var7;
               Iterator var8;
               label83:
               do {
                  do {
                     do {
                        do {
                           if (!var4.hasNext()) {
                              this.cleanUpImageCache(var3);
                              List var13 = var1.parentUsers.list;

                              for(int var14 = var13.size() - 1; 0 <= var14; --var14) {
                                 Reference var15 = (Reference)var13.remove(var14);
                                 Parent var16 = (Parent)var15.get();
                                 var15.clear();
                                 if (var16 != null && var16.getScene() != null) {
                                    var16.impl_reapplyCSS();
                                 }
                              }

                              return;
                           }

                           Map.Entry var5 = (Map.Entry)var4.next();
                           var6 = (CacheContainer)var5.getValue();
                        } while(var6 == null);
                     } while(var6.cacheMap == null);
                  } while(var6.cacheMap.isEmpty());

                  var7 = new ArrayList();
                  var8 = var6.cacheMap.entrySet().iterator();

                  while(true) {
                     List var10;
                     while(true) {
                        if (!var8.hasNext()) {
                           continue label83;
                        }

                        Map.Entry var9 = (Map.Entry)var8.next();
                        var10 = (List)var9.getKey();
                        if (var10 != null) {
                           if (var10.contains(var3)) {
                              break;
                           }
                        } else if (var3 == null) {
                           break;
                        }
                     }

                     var7.add(var10);
                  }
               } while(var7.isEmpty());

               var8 = var7.iterator();

               while(var8.hasNext()) {
                  List var17 = (List)var8.next();
                  Map var18 = (Map)var6.cacheMap.remove(var17);
                  if (var18 != null) {
                     var18.clear();
                  }
               }
            }
         }
      }
   }

   public Image getCachedImage(String var1) {
      synchronized(styleLock) {
         Image var3 = null;
         if (this.imageCache.containsKey(var1)) {
            var3 = (Image)this.imageCache.get(var1);
         } else {
            PlatformLogger var5;
            try {
               var3 = new Image(var1);
               if (var3.isError()) {
                  PlatformLogger var4 = getLogger();
                  if (var4 != null && var4.isLoggable(Level.WARNING)) {
                     var4.warning("Error loading image: " + var1);
                  }

                  var3 = null;
               }

               this.imageCache.put(var1, var3);
            } catch (IllegalArgumentException var7) {
               var5 = getLogger();
               if (var5 != null && var5.isLoggable(Level.WARNING)) {
                  var5.warning(var7.getLocalizedMessage());
               }
            } catch (NullPointerException var8) {
               var5 = getLogger();
               if (var5 != null && var5.isLoggable(Level.WARNING)) {
                  var5.warning(var8.getLocalizedMessage());
               }
            }
         }

         return var3;
      }
   }

   private void cleanUpImageCache(String var1) {
      synchronized(styleLock) {
         if (var1 != null || !this.imageCache.isEmpty()) {
            String var3 = var1.trim();
            if (!var3.isEmpty()) {
               int var4 = var3.lastIndexOf(47);
               String var5 = var4 > 0 ? var3.substring(0, var4) : var3;
               int var6 = var5.length();
               String[] var7 = new String[this.imageCache.size()];
               int var8 = 0;
               Set var9 = this.imageCache.entrySet();
               Iterator var10 = var9.iterator();

               while(var10.hasNext()) {
                  Map.Entry var11 = (Map.Entry)var10.next();
                  String var12 = (String)var11.getKey();
                  var4 = var12.lastIndexOf(47);
                  String var13 = var4 > 0 ? var12.substring(0, var4) : var12;
                  int var14 = var13.length();
                  boolean var15 = var14 > var6 ? var13.startsWith(var5) : var5.startsWith(var13);
                  if (var15) {
                     var7[var8++] = var12;
                  }
               }

               for(int var18 = 0; var18 < var8; ++var18) {
                  Image var19 = (Image)this.imageCache.remove(var7[var18]);
               }

            }
         }
      }
   }

   private static URL getURL(String var0) {
      if (var0 != null && !var0.trim().isEmpty()) {
         try {
            URI var1 = new URI(var0.trim());
            if (var1.isAbsolute()) {
               return var1.toURL();
            } else {
               ClassLoader var2;
               if (!var0.startsWith("com/sun/javafx/scene/control/skin/") || !var0.endsWith(".css") && !var0.endsWith(".bss")) {
                  var2 = Thread.currentThread().getContextClassLoader();
                  String var8 = var1.getPath();
                  URL var9 = null;
                  if (var2 != null) {
                     if (var8.startsWith("/")) {
                        var9 = var2.getResource(var8.substring(1));
                     } else {
                        var9 = var2.getResource(var8);
                     }
                  }

                  return var9;
               } else {
                  try {
                     var2 = StyleManager.class.getClassLoader();
                     Class var3 = Class.forName("com.sun.javafx.scene.control.skin.Utils", true, var2);
                     Method var4 = var3.getMethod("getResource", String.class);
                     return (URL)var4.invoke((Object)null, var0.substring("com/sun/javafx/scene/control/skin/".length()));
                  } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var5) {
                     var5.printStackTrace();
                     return null;
                  }
               }
            }
         } catch (MalformedURLException var6) {
            return null;
         } catch (URISyntaxException var7) {
            return null;
         }
      } else {
         return null;
      }
   }

   static byte[] calculateCheckSum(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         try {
            URL var1 = getURL(var0);
            if (var1 != null && "file".equals(var1.getProtocol())) {
               InputStream var2 = var1.openStream();
               Throwable var3 = null;

               try {
                  DigestInputStream var4 = new DigestInputStream(var2, MessageDigest.getInstance("MD5"));
                  Throwable var5 = null;

                  try {
                     Object var6;
                     try {
                        var4.getMessageDigest().reset();

                        while(var4.read() != -1) {
                        }

                        var6 = var4.getMessageDigest().digest();
                        return (byte[])var6;
                     } catch (Throwable var31) {
                        var6 = var31;
                        var5 = var31;
                        throw var31;
                     }
                  } finally {
                     if (var4 != null) {
                        if (var5 != null) {
                           try {
                              var4.close();
                           } catch (Throwable var30) {
                              var5.addSuppressed(var30);
                           }
                        } else {
                           var4.close();
                        }
                     }

                  }
               } catch (Throwable var33) {
                  var3 = var33;
                  throw var33;
               } finally {
                  if (var2 != null) {
                     if (var3 != null) {
                        try {
                           var2.close();
                        } catch (Throwable var29) {
                           var3.addSuppressed(var29);
                        }
                     } else {
                        var2.close();
                     }
                  }

               }
            }
         } catch (NoSuchAlgorithmException | IOException | SecurityException | IllegalArgumentException var35) {
         }

         return new byte[0];
      } else {
         return new byte[0];
      }
   }

   public static Stylesheet loadStylesheet(String var0) {
      try {
         return loadStylesheetUnPrivileged(var0);
      } catch (AccessControlException var16) {
         if (var0.length() < 7 && var0.indexOf("!/") < var0.length() - 7) {
            return null;
         } else {
            try {
               URI var2 = new URI(var0);
               if ("jar".equals(var2.getScheme())) {
                  URI var3 = (URI)AccessController.doPrivileged(() -> {
                     return StyleManager.class.getProtectionDomain().getCodeSource().getLocation().toURI();
                  });
                  String var4 = var3.getSchemeSpecificPart();
                  String var5 = var2.getSchemeSpecificPart();
                  String var6 = var5.substring(var5.indexOf(47), var5.indexOf("!/"));
                  if (var4.equals(var6)) {
                     String var7 = var0.substring(var0.indexOf("!/") + 2);
                     if (var0.endsWith(".css") || var0.endsWith(".bss")) {
                        FilePermission var8 = new FilePermission(var4, "read");
                        PermissionCollection var9 = var8.newPermissionCollection();
                        var9.add(var8);
                        AccessControlContext var10 = new AccessControlContext(new ProtectionDomain[]{new ProtectionDomain((CodeSource)null, var9)});
                        JarFile var11 = null;

                        try {
                           var11 = (JarFile)AccessController.doPrivileged(() -> {
                              return new JarFile(var4);
                           }, var10);
                        } catch (PrivilegedActionException var13) {
                           return null;
                        }

                        if (var11 != null) {
                           JarEntry var12 = var11.getJarEntry(var7);
                           if (var12 != null) {
                              return (Stylesheet)AccessController.doPrivileged(() -> {
                                 return loadStylesheetUnPrivileged(var0);
                              }, var10);
                           }
                        }
                     }
                  }
               }

               return null;
            } catch (URISyntaxException var14) {
               return null;
            } catch (PrivilegedActionException var15) {
               return null;
            }
         }
      }
   }

   private static Stylesheet loadStylesheetUnPrivileged(String var0) {
      synchronized(styleLock) {
         Boolean var2 = (Boolean)AccessController.doPrivileged(() -> {
            String var1 = System.getProperty("binary.css");
            return !var0.endsWith(".bss") && var1 != null ? !Boolean.valueOf(var1) : Boolean.FALSE;
         });

         CssError var4;
         try {
            String var3 = var2 ? ".css" : ".bss";
            var4 = null;
            Stylesheet var5 = null;
            URL var16;
            if (!var0.endsWith(".css") && !var0.endsWith(".bss")) {
               var16 = getURL(var0);
               var2 = true;
            } else {
               String var6 = var0.substring(0, var0.length() - 4);
               var16 = getURL(var6 + var3);
               if (var16 == null && var2 = !var2) {
                  var16 = getURL(var6 + ".css");
               }

               if (var16 != null && !var2) {
                  try {
                     var5 = Stylesheet.loadBinary(var16);
                  } catch (IOException var12) {
                     var5 = null;
                  }

                  if (var5 == null && var2 = !var2) {
                     var16 = getURL(var0);
                  }
               }
            }

            if (var16 != null && var2) {
               var5 = (new CSSParser()).parse(var16);
            }

            if (var5 == null) {
               if (errors != null) {
                  CssError var17 = new CssError("Resource \"" + var0 + "\" not found.");
                  errors.add(var17);
               }

               if (getLogger().isLoggable(Level.WARNING)) {
                  getLogger().warning(String.format("Resource \"%s\" not found.", var0));
               }
            }

            if (var5 != null) {
               Iterator var18 = var5.getFontFaces().iterator();

               label111:
               while(true) {
                  while(true) {
                     if (!var18.hasNext()) {
                        break label111;
                     }

                     FontFace var7 = (FontFace)var18.next();
                     Iterator var8 = var7.getSources().iterator();

                     while(var8.hasNext()) {
                        FontFace.FontFaceSrc var9 = (FontFace.FontFaceSrc)var8.next();
                        if (var9.getType() == FontFace.FontFaceSrcType.URL) {
                           Font var10 = Font.loadFont(var9.getSrc(), 10.0);
                           if (var10 == null) {
                              getLogger().info("Could not load @font-face font [" + var9.getSrc() + "]");
                           }
                           break;
                        }
                     }
                  }
               }
            }

            Stylesheet var10000 = var5;
            return var10000;
         } catch (FileNotFoundException var13) {
            if (errors != null) {
               var4 = new CssError("Stylesheet \"" + var0 + "\" not found.");
               errors.add(var4);
            }

            if (getLogger().isLoggable(Level.INFO)) {
               getLogger().info("Could not find stylesheet: " + var0);
            }
         } catch (IOException var14) {
            if (errors != null) {
               var4 = new CssError("Could not load stylesheet: " + var0);
               errors.add(var4);
            }

            if (getLogger().isLoggable(Level.INFO)) {
               getLogger().info("Could not load stylesheet: " + var0);
            }
         }

         return null;
      }
   }

   public void setUserAgentStylesheets(List var1) {
      if (var1 != null && var1.size() != 0) {
         synchronized(styleLock) {
            boolean var3;
            int var4;
            int var5;
            String var6;
            String var7;
            if (var1.size() == this.platformUserAgentStylesheetContainers.size()) {
               var3 = true;
               var4 = 0;

               for(var5 = var1.size(); var4 < var5 && var3; ++var4) {
                  var6 = (String)var1.get(var4);
                  var7 = var6 != null ? var6.trim() : null;
                  if (var7 == null || var7.isEmpty()) {
                     break;
                  }

                  StylesheetContainer var8 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var4);
                  if (var3 = var7.equals(var8.fname)) {
                     String var9 = var8.stylesheet.getUrl();
                     byte[] var10 = calculateCheckSum(var9);
                     var3 = Arrays.equals(var10, var8.checksum);
                  }
               }

               if (var3) {
                  return;
               }
            }

            var3 = false;
            var4 = 0;

            for(var5 = var1.size(); var4 < var5; ++var4) {
               var6 = (String)var1.get(var4);
               var7 = var6 != null ? var6.trim() : null;
               if (var7 != null && !var7.isEmpty()) {
                  if (!var3) {
                     this.platformUserAgentStylesheetContainers.clear();
                     var3 = true;
                  }

                  if (var4 == 0) {
                     this._setDefaultUserAgentStylesheet(var7);
                  } else {
                     this._addUserAgentStylesheet(var7);
                  }
               }
            }

            if (var3) {
               this.userAgentStylesheetsChanged();
            }

         }
      }
   }

   public void addUserAgentStylesheet(String var1) {
      this.addUserAgentStylesheet((Scene)null, (String)var1);
   }

   public void addUserAgentStylesheet(Scene var1, String var2) {
      String var3 = var2 != null ? var2.trim() : null;
      if (var3 != null && !var3.isEmpty()) {
         synchronized(styleLock) {
            CssError.setCurrentScene(var1);
            if (this._addUserAgentStylesheet(var3)) {
               this.userAgentStylesheetsChanged();
            }

            CssError.setCurrentScene((Scene)null);
         }
      }
   }

   private boolean _addUserAgentStylesheet(String var1) {
      synchronized(styleLock) {
         int var3 = 0;

         for(int var4 = this.platformUserAgentStylesheetContainers.size(); var3 < var4; ++var3) {
            StylesheetContainer var5 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var3);
            if (var1.equals(var5.fname)) {
               return false;
            }
         }

         Stylesheet var8 = loadStylesheet(var1);
         if (var8 == null) {
            return false;
         } else {
            var8.setOrigin(StyleOrigin.USER_AGENT);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(var1, var8));
            return true;
         }
      }
   }

   public void addUserAgentStylesheet(Scene var1, Stylesheet var2) {
      if (var2 == null) {
         throw new IllegalArgumentException("null arg ua_stylesheet");
      } else {
         String var3 = var2.getUrl();
         String var4 = var3 != null ? var3.trim() : "";
         synchronized(styleLock) {
            int var6 = 0;

            for(int var7 = this.platformUserAgentStylesheetContainers.size(); var6 < var7; ++var6) {
               StylesheetContainer var8 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var6);
               if (var4.equals(var8.fname)) {
                  return;
               }
            }

            CssError.setCurrentScene(var1);
            this.platformUserAgentStylesheetContainers.add(new StylesheetContainer(var4, var2));
            if (var2 != null) {
               var2.setOrigin(StyleOrigin.USER_AGENT);
            }

            this.userAgentStylesheetsChanged();
            CssError.setCurrentScene((Scene)null);
         }
      }
   }

   public void setDefaultUserAgentStylesheet(String var1) {
      this.setDefaultUserAgentStylesheet((Scene)null, var1);
   }

   public void setDefaultUserAgentStylesheet(Scene var1, String var2) {
      String var3 = var2 != null ? var2.trim() : null;
      if (var3 != null && !var3.isEmpty()) {
         synchronized(styleLock) {
            CssError.setCurrentScene(var1);
            if (this._setDefaultUserAgentStylesheet(var3)) {
               this.userAgentStylesheetsChanged();
            }

            CssError.setCurrentScene((Scene)null);
         }
      }
   }

   private boolean _setDefaultUserAgentStylesheet(String var1) {
      synchronized(styleLock) {
         int var3 = 0;

         for(int var4 = this.platformUserAgentStylesheetContainers.size(); var3 < var4; ++var3) {
            StylesheetContainer var5 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var3);
            if (var1.equals(var5.fname)) {
               if (var3 > 0) {
                  this.platformUserAgentStylesheetContainers.remove(var3);
                  if (this.hasDefaultUserAgentStylesheet) {
                     this.platformUserAgentStylesheetContainers.set(0, var5);
                  } else {
                     this.platformUserAgentStylesheetContainers.add(0, var5);
                  }
               }

               return var3 > 0;
            }
         }

         Stylesheet var8 = loadStylesheet(var1);
         if (var8 == null) {
            return false;
         } else {
            var8.setOrigin(StyleOrigin.USER_AGENT);
            StylesheetContainer var9 = new StylesheetContainer(var1, var8);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
               this.platformUserAgentStylesheetContainers.add(var9);
            } else if (this.hasDefaultUserAgentStylesheet) {
               this.platformUserAgentStylesheetContainers.set(0, var9);
            } else {
               this.platformUserAgentStylesheetContainers.add(0, var9);
            }

            this.hasDefaultUserAgentStylesheet = true;
            return true;
         }
      }
   }

   public void removeUserAgentStylesheet(String var1) {
      String var2 = var1 != null ? var1.trim() : null;
      if (var2 != null && !var2.isEmpty()) {
         synchronized(styleLock) {
            boolean var4 = false;

            for(int var5 = this.platformUserAgentStylesheetContainers.size() - 1; var5 >= 0; --var5) {
               if (!var2.equals(Application.getUserAgentStylesheet())) {
                  StylesheetContainer var6 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var5);
                  if (var2.equals(var6.fname)) {
                     this.platformUserAgentStylesheetContainers.remove(var5);
                     var4 = true;
                  }
               }
            }

            if (var4) {
               this.userAgentStylesheetsChanged();
            }

         }
      }
   }

   public void setDefaultUserAgentStylesheet(Stylesheet var1) {
      if (var1 != null) {
         String var2 = var1.getUrl();
         String var3 = var2 != null ? var2.trim() : "";
         synchronized(styleLock) {
            int var5 = 0;

            for(int var6 = this.platformUserAgentStylesheetContainers.size(); var5 < var6; ++var5) {
               StylesheetContainer var7 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var5);
               if (var3.equals(var7.fname)) {
                  if (var5 > 0) {
                     this.platformUserAgentStylesheetContainers.remove(var5);
                     if (this.hasDefaultUserAgentStylesheet) {
                        this.platformUserAgentStylesheetContainers.set(0, var7);
                     } else {
                        this.platformUserAgentStylesheetContainers.add(0, var7);
                     }
                  }

                  return;
               }
            }

            StylesheetContainer var10 = new StylesheetContainer(var3, var1);
            if (this.platformUserAgentStylesheetContainers.size() == 0) {
               this.platformUserAgentStylesheetContainers.add(var10);
            } else if (this.hasDefaultUserAgentStylesheet) {
               this.platformUserAgentStylesheetContainers.set(0, var10);
            } else {
               this.platformUserAgentStylesheetContainers.add(0, var10);
            }

            this.hasDefaultUserAgentStylesheet = true;
            var1.setOrigin(StyleOrigin.USER_AGENT);
            this.userAgentStylesheetsChanged();
         }
      }
   }

   private void userAgentStylesheetsChanged() {
      ArrayList var1 = new ArrayList();
      synchronized(styleLock) {
         Iterator var3 = cacheContainerMap.values().iterator();

         while(var3.hasNext()) {
            CacheContainer var4 = (CacheContainer)var3.next();
            var4.clearCache();
         }

         StyleConverterImpl.clearCache();
         var3 = cacheContainerMap.keySet().iterator();

         while(true) {
            if (!var3.hasNext()) {
               break;
            }

            Parent var8 = (Parent)var3.next();
            if (var8 != null) {
               var1.add(var8);
            }
         }
      }

      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Parent var7 = (Parent)var2.next();
         var7.impl_reapplyCSS();
      }

   }

   private List processStylesheets(List var1, Parent var2) {
      synchronized(styleLock) {
         ArrayList var4 = new ArrayList();
         int var5 = 0;

         for(int var6 = var1.size(); var5 < var6; ++var5) {
            String var7 = (String)var1.get(var5);
            StylesheetContainer var8 = null;
            if (this.stylesheetContainerMap.containsKey(var7)) {
               var8 = (StylesheetContainer)this.stylesheetContainerMap.get(var7);
               if (!var4.contains(var8)) {
                  if (var8.checksumInvalid) {
                     byte[] var9 = calculateCheckSum(var7);
                     if (!Arrays.equals(var9, var8.checksum)) {
                        this.removeStylesheetContainer(var8);
                        Stylesheet var10 = loadStylesheet(var7);
                        var8 = new StylesheetContainer(var7, var10, var9);
                        this.stylesheetContainerMap.put(var7, var8);
                     } else {
                        var8.checksumInvalid = false;
                     }
                  }

                  var4.add(var8);
               }

               var8.parentUsers.add(var2);
            } else {
               Stylesheet var13 = loadStylesheet(var7);
               var8 = new StylesheetContainer(var7, var13);
               var8.parentUsers.add(var2);
               this.stylesheetContainerMap.put(var7, var8);
               var4.add(var8);
            }
         }

         return var4;
      }
   }

   private List gatherParentStylesheets(Parent var1) {
      if (var1 == null) {
         return Collections.emptyList();
      } else {
         List var2 = var1.impl_getAllParentStylesheets();
         if (var2 != null && !var2.isEmpty()) {
            synchronized(styleLock) {
               CssError.setCurrentScene(var1.getScene());
               List var4 = this.processStylesheets(var2, var1);
               CssError.setCurrentScene((Scene)null);
               return var4;
            }
         } else {
            return Collections.emptyList();
         }
      }
   }

   private List gatherSceneStylesheets(Scene var1) {
      if (var1 == null) {
         return Collections.emptyList();
      } else {
         ObservableList var2 = var1.getStylesheets();
         if (var2 != null && !var2.isEmpty()) {
            synchronized(styleLock) {
               CssError.setCurrentScene(var1);
               List var4 = this.processStylesheets(var2, var1.getRoot());
               CssError.setCurrentScene((Scene)null);
               return var4;
            }
         } else {
            return Collections.emptyList();
         }
      }
   }

   public StyleMap findMatchingStyles(Node var1, SubScene var2, Set[] var3) {
      Scene var4 = var1.getScene();
      if (var4 == null) {
         return StyleMap.EMPTY_MAP;
      } else {
         CacheContainer var5 = this.getCacheContainer(var1, var2);
         if (var5 == null) {
            assert false : var1.toString();

            return StyleMap.EMPTY_MAP;
         } else {
            synchronized(styleLock) {
               Parent var7 = var1 instanceof Parent ? (Parent)var1 : var1.getParent();
               List var8 = this.gatherParentStylesheets(var7);
               boolean var9 = !var8.isEmpty();
               List var10 = this.gatherSceneStylesheets(var4);
               boolean var11 = !var10.isEmpty();
               String var12 = var1.getStyle();
               boolean var13 = var12 != null && !var12.trim().isEmpty();
               String var14 = var4.getUserAgentStylesheet();
               boolean var15 = var14 != null && !var14.trim().isEmpty();
               String var16 = var2 != null ? var2.getUserAgentStylesheet() : null;
               boolean var17 = var16 != null && !var16.trim().isEmpty();
               String var18 = null;

               Object var19;
               for(var19 = var1; var19 != null; var19 = ((Node)var19).getParent()) {
                  var18 = var19 instanceof Region ? ((Region)var19).getUserAgentStylesheet() : null;
                  if (var18 != null) {
                     break;
                  }
               }

               boolean var20 = var18 != null && !var18.trim().isEmpty();
               if (!var13 && !var9 && !var11 && !var15 && !var17 && !var20 && this.platformUserAgentStylesheetContainers.isEmpty()) {
                  return StyleMap.EMPTY_MAP;
               } else {
                  String var21 = var1.getTypeSelector();
                  String var22 = var1.getId();
                  ObservableList var23 = var1.getStyleClass();
                  if (this.key == null) {
                     this.key = new Key();
                  }

                  this.key.className = var21;
                  this.key.id = var22;
                  int var24 = 0;

                  for(int var25 = var23.size(); var24 < var25; ++var24) {
                     String var26 = (String)var23.get(var24);
                     if (var26 != null && !var26.isEmpty()) {
                        this.key.styleClasses.add(StyleClassSet.getStyleClass(var26));
                     }
                  }

                  Map var33 = var5.getCacheMap(var8, var18);
                  Cache var34 = (Cache)var33.get(this.key);
                  if (var34 != null) {
                     this.key.styleClasses.clear();
                  } else {
                     ArrayList var35 = new ArrayList();
                     int var29;
                     int var37;
                     int var40;
                     StylesheetContainer var42;
                     List var43;
                     if (!var17 && !var15) {
                        if (!this.platformUserAgentStylesheetContainers.isEmpty()) {
                           var37 = 0;

                           for(var40 = this.platformUserAgentStylesheetContainers.size(); var37 < var40; ++var37) {
                              var42 = (StylesheetContainer)this.platformUserAgentStylesheetContainers.get(var37);
                              if (var42 != null && var42.selectorPartitioning != null) {
                                 var43 = var42.selectorPartitioning.match(var22, var21, this.key.styleClasses);
                                 var35.addAll(var43);
                              }
                           }
                        }
                     } else {
                        String var27 = var17 ? var2.getUserAgentStylesheet().trim() : var4.getUserAgentStylesheet().trim();
                        StylesheetContainer var28 = null;
                        var29 = 0;

                        for(int var30 = this.userAgentStylesheetContainers.size(); var29 < var30; ++var29) {
                           var28 = (StylesheetContainer)this.userAgentStylesheetContainers.get(var29);
                           if (var27.equals(var28.fname)) {
                              break;
                           }

                           var28 = null;
                        }

                        if (var28 == null) {
                           Stylesheet var39 = loadStylesheet(var27);
                           if (var39 != null) {
                              var39.setOrigin(StyleOrigin.USER_AGENT);
                           }

                           var28 = new StylesheetContainer(var27, var39);
                           this.userAgentStylesheetContainers.add(var28);
                        }

                        if (var28.selectorPartitioning != null) {
                           Parent var41 = var17 ? var2.getRoot() : var4.getRoot();
                           var28.parentUsers.add(var41);
                           var43 = var28.selectorPartitioning.match(var22, var21, this.key.styleClasses);
                           var35.addAll(var43);
                        }
                     }

                     if (var20) {
                        StylesheetContainer var38 = null;
                        var40 = 0;

                        for(var29 = this.userAgentStylesheetContainers.size(); var40 < var29; ++var40) {
                           var38 = (StylesheetContainer)this.userAgentStylesheetContainers.get(var40);
                           if (var18.equals(var38.fname)) {
                              break;
                           }

                           var38 = null;
                        }

                        if (var38 == null) {
                           Stylesheet var44 = loadStylesheet(var18);
                           if (var44 != null) {
                              var44.setOrigin(StyleOrigin.USER_AGENT);
                           }

                           var38 = new StylesheetContainer(var18, var44);
                           this.userAgentStylesheetContainers.add(var38);
                        }

                        if (var38.selectorPartitioning != null) {
                           var38.parentUsers.add((Parent)var19);
                           List var45 = var38.selectorPartitioning.match(var22, var21, this.key.styleClasses);
                           var35.addAll(var45);
                        }
                     }

                     if (!var10.isEmpty()) {
                        var37 = 0;

                        for(var40 = var10.size(); var37 < var40; ++var37) {
                           var42 = (StylesheetContainer)var10.get(var37);
                           if (var42 != null && var42.selectorPartitioning != null) {
                              var43 = var42.selectorPartitioning.match(var22, var21, this.key.styleClasses);
                              var35.addAll(var43);
                           }
                        }
                     }

                     if (var9) {
                        var37 = var8 == null ? 0 : var8.size();

                        for(var40 = 0; var40 < var37; ++var40) {
                           var42 = (StylesheetContainer)var8.get(var40);
                           if (var42.selectorPartitioning != null) {
                              var43 = var42.selectorPartitioning.match(var22, var21, this.key.styleClasses);
                              var35.addAll(var43);
                           }
                        }
                     }

                     var34 = new Cache(var35);
                     var33.put(this.key, var34);
                     this.key = null;
                  }

                  StyleMap var36 = var34.getStyleMap(var5, var1, var3, var13);
                  return var36;
               }
            }
         }
      }
   }

   public static ObservableList errorsProperty() {
      if (errors == null) {
         errors = FXCollections.observableArrayList();
      }

      return errors;
   }

   public static ObservableList getErrors() {
      return errors;
   }

   // $FF: synthetic method
   StyleManager(Object var1) {
      this();
   }

   private static class Key {
      String className;
      String id;
      final StyleClassSet styleClasses;

      private Key() {
         this.styleClasses = new StyleClassSet();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof Key)) {
            return true;
         } else {
            Key var2 = (Key)var1;
            if (this.className == null) {
               if (var2.className != null) {
                  return false;
               }
            } else if (!this.className.equals(var2.className)) {
               return false;
            }

            if (this.id == null) {
               if (var2.id != null) {
                  return false;
               }
            } else if (!this.id.equals(var2.id)) {
               return false;
            }

            return this.styleClasses.equals(var2.styleClasses);
         }
      }

      public int hashCode() {
         int var1 = 7;
         var1 = 29 * var1 + (this.className != null ? this.className.hashCode() : 0);
         var1 = 29 * var1 + (this.id != null ? this.id.hashCode() : 0);
         var1 = 29 * var1 + this.styleClasses.hashCode();
         return var1;
      }

      // $FF: synthetic method
      Key(Object var1) {
         this();
      }
   }

   private static class Cache {
      private final List selectors;
      private final Map cache;

      Cache(List var1) {
         this.selectors = var1;
         this.cache = new HashMap();
      }

      private StyleMap getStyleMap(CacheContainer var1, Node var2, Set[] var3, boolean var4) {
         if ((this.selectors == null || this.selectors.isEmpty()) && !var4) {
            return StyleMap.EMPTY_MAP;
         } else {
            int var5 = this.selectors.size();
            long[] var6 = new long[var5 / 64 + 1];
            boolean var7 = true;

            for(int var8 = 0; var8 < var5; ++var8) {
               Selector var9 = (Selector)this.selectors.get(var8);
               if (var9.applies(var2, var3, 0)) {
                  int var10 = var8 / 64;
                  long var11 = var6[var10] | 1L << var8;
                  var6[var10] = var11;
                  var7 = false;
               }
            }

            if (var7 && !var4) {
               return StyleMap.EMPTY_MAP;
            } else {
               String var17 = var2.getStyle();
               Key var18 = new Key(var6, var17);
               if (this.cache.containsKey(var18)) {
                  Integer var20 = (Integer)this.cache.get(var18);
                  StyleMap var24 = var20 != null ? var1.getStyleMap(var20) : StyleMap.EMPTY_MAP;
                  return var24;
               } else {
                  ArrayList var19 = new ArrayList();
                  if (var4) {
                     Selector var21 = var1.getInlineStyleSelector(var17);
                     if (var21 != null) {
                        var19.add(var21);
                     }
                  }

                  int var22;
                  for(var22 = 0; var22 < var6.length; ++var22) {
                     if (var6[var22] != 0L) {
                        int var12 = var22 * 64;

                        for(int var13 = 0; var13 < 64; ++var13) {
                           long var14 = 1L << var13;
                           if ((var14 & var6[var22]) == var14) {
                              Selector var16 = (Selector)this.selectors.get(var12 + var13);
                              var19.add(var16);
                           }
                        }
                     }
                  }

                  var22 = var1.nextSmapId();
                  this.cache.put(var18, var22);
                  StyleMap var23 = new StyleMap(var22, var19);
                  var1.addStyleMap(var23);
                  return var23;
               }
            }
         }
      }

      private static class Key {
         final long[] key;
         final String inlineStyle;

         Key(long[] var1, String var2) {
            this.key = var1;
            this.inlineStyle = var2 != null && var2.trim().isEmpty() ? null : var2;
         }

         public String toString() {
            return Arrays.toString(this.key) + (this.inlineStyle != null ? "* {" + this.inlineStyle + "}" : "");
         }

         public int hashCode() {
            int var1 = 3;
            var1 = 17 * var1 + Arrays.hashCode(this.key);
            if (this.inlineStyle != null) {
               var1 = 17 * var1 + this.inlineStyle.hashCode();
            }

            return var1;
         }

         public boolean equals(Object var1) {
            if (var1 == null) {
               return false;
            } else if (this.getClass() != var1.getClass()) {
               return false;
            } else {
               Key var2 = (Key)var1;
               if (this.inlineStyle == null) {
                  if (var2.inlineStyle == null) {
                     return Arrays.equals(this.key, var2.key);
                  }
               } else if (this.inlineStyle.equals(var2.inlineStyle)) {
                  return Arrays.equals(this.key, var2.key);
               }

               return false;
            }
         }
      }
   }

   static class CacheContainer {
      private Map styleCache;
      private Map cacheMap;
      private List styleMapList;
      private Map inlineStylesCache;
      private int styleMapId = 0;
      private int baseStyleMapId = 0;

      private Map getStyleCache() {
         if (this.styleCache == null) {
            this.styleCache = new HashMap();
         }

         return this.styleCache;
      }

      private Map getCacheMap(List var1, String var2) {
         if (this.cacheMap == null) {
            this.cacheMap = new HashMap();
         }

         synchronized(StyleManager.styleLock) {
            if ((var1 == null || var1.isEmpty()) && (var2 == null || var2.isEmpty())) {
               Object var9 = (Map)this.cacheMap.get((Object)null);
               if (var9 == null) {
                  var9 = new HashMap();
                  this.cacheMap.put((Object)null, var9);
               }

               return (Map)var9;
            } else {
               int var4 = var1.size();
               if (StyleManager.cacheMapKey == null) {
                  StyleManager.cacheMapKey = new ArrayList(var4);
               }

               for(int var5 = 0; var5 < var4; ++var5) {
                  StylesheetContainer var6 = (StylesheetContainer)var1.get(var5);
                  if (var6 != null && var6.fname != null && !var6.fname.isEmpty()) {
                     StyleManager.cacheMapKey.add(var6.fname);
                  }
               }

               if (var2 != null) {
                  StyleManager.cacheMapKey.add(var2);
               }

               Object var10 = (Map)this.cacheMap.get(StyleManager.cacheMapKey);
               if (var10 == null) {
                  var10 = new HashMap();
                  this.cacheMap.put(StyleManager.cacheMapKey, var10);
                  StyleManager.cacheMapKey = null;
               } else {
                  StyleManager.cacheMapKey.clear();
               }

               return (Map)var10;
            }
         }
      }

      private List getStyleMapList() {
         if (this.styleMapList == null) {
            this.styleMapList = new ArrayList();
         }

         return this.styleMapList;
      }

      private int nextSmapId() {
         this.styleMapId = this.baseStyleMapId + this.getStyleMapList().size();
         return this.styleMapId;
      }

      private void addStyleMap(StyleMap var1) {
         this.getStyleMapList().add(var1);
      }

      public StyleMap getStyleMap(int var1) {
         int var2 = var1 - this.baseStyleMapId;
         return 0 <= var2 && var2 < this.getStyleMapList().size() ? (StyleMap)this.getStyleMapList().get(var2) : StyleMap.EMPTY_MAP;
      }

      private void clearCache() {
         if (this.cacheMap != null) {
            this.cacheMap.clear();
         }

         if (this.styleCache != null) {
            this.styleCache.clear();
         }

         if (this.styleMapList != null) {
            this.styleMapList.clear();
         }

         this.baseStyleMapId = this.styleMapId;
         if (this.baseStyleMapId > 1879048185) {
            this.baseStyleMapId = this.styleMapId = 0;
         }

      }

      private Selector getInlineStyleSelector(String var1) {
         if (var1 != null && !var1.trim().isEmpty()) {
            if (this.inlineStylesCache != null && this.inlineStylesCache.containsKey(var1)) {
               return (Selector)this.inlineStylesCache.get(var1);
            } else {
               if (this.inlineStylesCache == null) {
                  this.inlineStylesCache = new HashMap();
               }

               Stylesheet var2 = (new CSSParser()).parse("*{" + var1 + "}");
               if (var2 != null) {
                  var2.setOrigin(StyleOrigin.INLINE);
                  List var3 = var2.getRules();
                  Rule var4 = var3 != null && !var3.isEmpty() ? (Rule)var3.get(0) : null;
                  List var5 = var4 != null ? var4.getUnobservedSelectorList() : null;
                  Selector var6 = var5 != null && !var5.isEmpty() ? (Selector)var5.get(0) : null;
                  if (var6 != null) {
                     var6.setOrdinal(-1);
                     this.inlineStylesCache.put(var1, var6);
                     return var6;
                  }
               }

               this.inlineStylesCache.put(var1, (Object)null);
               return null;
            }
         } else {
            return null;
         }
      }
   }

   static class RefList {
      final List list = new ArrayList();

      void add(Object var1) {
         for(int var2 = this.list.size() - 1; 0 <= var2; --var2) {
            Reference var3 = (Reference)this.list.get(var2);
            Object var4 = var3.get();
            if (var4 == null) {
               this.list.remove(var2);
            } else if (var4 == var1) {
               return;
            }
         }

         this.list.add(new WeakReference(var1));
      }

      void remove(Object var1) {
         for(int var2 = this.list.size() - 1; 0 <= var2; --var2) {
            Reference var3 = (Reference)this.list.get(var2);
            Object var4 = var3.get();
            if (var4 == null) {
               this.list.remove(var2);
            } else if (var4 == var1) {
               this.list.remove(var2);
               return;
            }
         }

      }

      boolean contains(Object var1) {
         for(int var2 = this.list.size() - 1; 0 <= var2; --var2) {
            Reference var3 = (Reference)this.list.get(var2);
            Object var4 = var3.get();
            if (var4 == var1) {
               return true;
            }
         }

         return false;
      }
   }

   static class StylesheetContainer {
      final String fname;
      final Stylesheet stylesheet;
      final SelectorPartitioning selectorPartitioning;
      final RefList parentUsers;
      final List imageCache;
      final int hash;
      final byte[] checksum;
      boolean checksumInvalid;

      StylesheetContainer(String var1, Stylesheet var2) {
         this(var1, var2, var2 != null ? StyleManager.calculateCheckSum(var2.getUrl()) : new byte[0]);
      }

      StylesheetContainer(String var1, Stylesheet var2, byte[] var3) {
         this.checksumInvalid = false;
         this.fname = var1;
         this.hash = var1 != null ? var1.hashCode() : 127;
         this.stylesheet = var2;
         if (var2 != null) {
            this.selectorPartitioning = new SelectorPartitioning();
            List var4 = var2.getRules();
            int var5 = var4 != null && !var4.isEmpty() ? var4.size() : 0;

            for(int var6 = 0; var6 < var5; ++var6) {
               Rule var7 = (Rule)var4.get(var6);
               List var8 = var7.getUnobservedSelectorList();
               int var9 = var8 != null && !var8.isEmpty() ? var8.size() : 0;

               for(int var10 = 0; var10 < var9; ++var10) {
                  Selector var11 = (Selector)var8.get(var10);
                  this.selectorPartitioning.partition(var11);
               }
            }
         } else {
            this.selectorPartitioning = null;
         }

         this.parentUsers = new RefList();
         this.imageCache = new ArrayList();
         this.checksum = var3;
      }

      void invalidateChecksum() {
         this.checksumInvalid = this.checksum.length > 0;
      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object var1) {
         if (var1 == null) {
            return false;
         } else if (this.getClass() != var1.getClass()) {
            return false;
         } else {
            StylesheetContainer var2 = (StylesheetContainer)var1;
            if (this.fname == null) {
               if (var2.fname != null) {
                  return false;
               }
            } else if (!this.fname.equals(var2.fname)) {
               return false;
            }

            return true;
         }
      }

      public String toString() {
         return this.fname;
      }
   }

   private static class InstanceHolder {
      static final StyleManager INSTANCE = new StyleManager();
   }
}
