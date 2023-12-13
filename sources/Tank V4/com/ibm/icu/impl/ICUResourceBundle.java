package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

public class ICUResourceBundle extends UResourceBundle {
   protected static final String ICU_DATA_PATH = "com/ibm/icu/impl/";
   public static final String ICU_BUNDLE = "data/icudt51b";
   public static final String ICU_BASE_NAME = "com/ibm/icu/impl/data/icudt51b";
   public static final String ICU_COLLATION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/coll";
   public static final String ICU_BRKITR_NAME = "/brkitr";
   public static final String ICU_BRKITR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/brkitr";
   public static final String ICU_RBNF_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/rbnf";
   public static final String ICU_TRANSLIT_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/translit";
   public static final String ICU_LANG_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/lang";
   public static final String ICU_CURR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/curr";
   public static final String ICU_REGION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/region";
   public static final String ICU_ZONE_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/zone";
   private static final String NO_INHERITANCE_MARKER = "∅∅∅";
   protected String resPath;
   public static final ClassLoader ICU_DATA_CLASS_LOADER;
   protected static final String INSTALLED_LOCALES = "InstalledLocales";
   public static final int FROM_FALLBACK = 1;
   public static final int FROM_ROOT = 2;
   public static final int FROM_DEFAULT = 3;
   public static final int FROM_LOCALE = 4;
   private int loadingStatus = -1;
   private static final String ICU_RESOURCE_INDEX = "res_index";
   private static final String DEFAULT_TAG = "default";
   private static final String FULL_LOCALE_NAMES_LIST = "fullLocaleNames.lst";
   private static final boolean DEBUG;
   private static CacheBase GET_AVAILABLE_CACHE;
   protected String localeID;
   protected String baseName;
   protected ULocale ulocale;
   protected ClassLoader loader;
   protected ICUResourceBundleReader reader;
   protected String key;
   protected int resource;
   public static final int RES_BOGUS = -1;
   public static final int ALIAS = 3;
   public static final int TABLE32 = 4;
   public static final int TABLE16 = 5;
   public static final int STRING_V2 = 6;
   public static final int ARRAY16 = 9;
   private static final int[] gPublicTypes;
   private static final char RES_PATH_SEP_CHAR = '/';
   private static final String RES_PATH_SEP_STR = "/";
   private static final String ICUDATA = "ICUDATA";
   private static final char HYPHEN = '-';
   private static final String LOCALE = "LOCALE";
   protected ICUCache lookup;
   private static final int MAX_INITIAL_LOOKUP_SIZE = 64;
   static final boolean $assertionsDisabled = !ICUResourceBundle.class.desiredAssertionStatus();

   public void setLoadingStatus(int var1) {
      this.loadingStatus = var1;
   }

   public int getLoadingStatus() {
      return this.loadingStatus;
   }

   public void setLoadingStatus(String var1) {
      String var2 = this.getLocaleID();
      if (var2.equals("root")) {
         this.setLoadingStatus(2);
      } else if (var2.equals(var1)) {
         this.setLoadingStatus(4);
      } else {
         this.setLoadingStatus(1);
      }

   }

   public String getResPath() {
      return this.resPath;
   }

   public static final ULocale getFunctionalEquivalent(String var0, ClassLoader var1, String var2, String var3, ULocale var4, boolean[] var5, boolean var6) {
      String var7 = var4.getKeywordValue(var3);
      String var8 = var4.getBaseName();
      String var9 = null;
      ULocale var10 = new ULocale(var8);
      ULocale var11 = null;
      boolean var12 = false;
      ULocale var13 = null;
      int var14 = 0;
      int var15 = 0;
      if (var7 == null || var7.length() == 0 || var7.equals("default")) {
         var7 = "";
         var12 = true;
      }

      ICUResourceBundle var16 = null;
      var16 = (ICUResourceBundle)UResourceBundle.getBundleInstance(var0, var10);
      if (var5 != null) {
         var5[0] = false;
         ULocale[] var17 = getAvailEntry(var0, var1).getULocaleList();

         for(int var18 = 0; var18 < var17.length; ++var18) {
            if (var10.equals(var17[var18])) {
               var5[0] = true;
               break;
            }
         }
      }

      ICUResourceBundle var22;
      do {
         try {
            var22 = (ICUResourceBundle)var16.get(var2);
            var9 = var22.getString("default");
            if (var12) {
               var7 = var9;
               var12 = false;
            }

            var11 = var16.getULocale();
         } catch (MissingResourceException var21) {
         }

         if (var11 == null) {
            var16 = (ICUResourceBundle)var16.getParent();
            ++var14;
         }
      } while(var16 != null && var11 == null);

      var10 = new ULocale(var8);
      var16 = (ICUResourceBundle)UResourceBundle.getBundleInstance(var0, var10);

      do {
         try {
            var22 = (ICUResourceBundle)var16.get(var2);
            var22.get(var7);
            var13 = var22.getULocale();
            if (var13 != null && var15 > var14) {
               var9 = var22.getString("default");
               var11 = var16.getULocale();
               var14 = var15;
            }
         } catch (MissingResourceException var20) {
         }

         if (var13 == null) {
            var16 = (ICUResourceBundle)var16.getParent();
            ++var15;
         }
      } while(var16 != null && var13 == null);

      if (var13 == null && var9 != null && !var9.equals(var7)) {
         var7 = var9;
         var10 = new ULocale(var8);
         var16 = (ICUResourceBundle)UResourceBundle.getBundleInstance(var0, var10);
         var15 = 0;

         do {
            try {
               var22 = (ICUResourceBundle)var16.get(var2);
               UResourceBundle var23 = var22.get(var7);
               var13 = var16.getULocale();
               if (!var13.toString().equals(var23.getLocale().toString())) {
                  var13 = null;
               }

               if (var13 != null && var15 > var14) {
                  var9 = var22.getString("default");
                  var11 = var16.getULocale();
                  var14 = var15;
               }
            } catch (MissingResourceException var19) {
            }

            if (var13 == null) {
               var16 = (ICUResourceBundle)var16.getParent();
               ++var15;
            }
         } while(var16 != null && var13 == null);
      }

      if (var13 == null) {
         throw new MissingResourceException("Could not find locale containing requested or default keyword.", var0, var3 + "=" + var7);
      } else {
         return var6 && var9.equals(var7) && var15 <= var14 ? var13 : new ULocale(var13.toString() + "@" + var3 + "=" + var7);
      }
   }

   public static final String[] getKeywordValues(String var0, String var1) {
      HashSet var2 = new HashSet();
      ULocale[] var3 = createULocaleList(var0, ICU_DATA_CLASS_LOADER);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         try {
            UResourceBundle var5 = UResourceBundle.getBundleInstance(var0, var3[var4]);
            ICUResourceBundle var6 = (ICUResourceBundle)((ICUResourceBundle)var5.getObject(var1));
            Enumeration var7 = var6.getKeys();

            while(var7.hasMoreElements()) {
               String var8 = (String)var7.nextElement();
               if (!"default".equals(var8)) {
                  var2.add(var8);
               }
            }
         } catch (Throwable var9) {
         }
      }

      return (String[])var2.toArray(new String[0]);
   }

   public ICUResourceBundle getWithFallback(String var1) throws MissingResourceException {
      ICUResourceBundle var2 = null;
      var2 = findResourceWithFallback(var1, this, (UResourceBundle)null);
      if (var2 == null) {
         throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getType(), var1, this.getKey());
      } else if (var2.getType() == 0 && var2.getString().equals("∅∅∅")) {
         throw new MissingResourceException("Encountered NO_INHERITANCE_MARKER", var1, this.getKey());
      } else {
         return var2;
      }
   }

   public ICUResourceBundle at(int var1) {
      return (ICUResourceBundle)this.handleGet(var1, (HashMap)null, this);
   }

   public ICUResourceBundle at(String var1) {
      return this instanceof ICUResourceBundleImpl.ResourceTable ? (ICUResourceBundle)this.handleGet(var1, (HashMap)null, this) : null;
   }

   public ICUResourceBundle findTopLevel(int var1) {
      return (ICUResourceBundle)super.findTopLevel(var1);
   }

   public ICUResourceBundle findTopLevel(String var1) {
      return (ICUResourceBundle)super.findTopLevel(var1);
   }

   public ICUResourceBundle findWithFallback(String var1) {
      return findResourceWithFallback(var1, this, (UResourceBundle)null);
   }

   public String getStringWithFallback(String var1) throws MissingResourceException {
      return this.getWithFallback(var1).getString();
   }

   public static Set getAvailableLocaleNameSet(String var0, ClassLoader var1) {
      return getAvailEntry(var0, var1).getLocaleNameSet();
   }

   public static Set getFullLocaleNameSet() {
      return getFullLocaleNameSet("com/ibm/icu/impl/data/icudt51b", ICU_DATA_CLASS_LOADER);
   }

   public static Set getFullLocaleNameSet(String var0, ClassLoader var1) {
      return getAvailEntry(var0, var1).getFullLocaleNameSet();
   }

   public static Set getAvailableLocaleNameSet() {
      return getAvailableLocaleNameSet("com/ibm/icu/impl/data/icudt51b", ICU_DATA_CLASS_LOADER);
   }

   public static final ULocale[] getAvailableULocales(String var0, ClassLoader var1) {
      return getAvailEntry(var0, var1).getULocaleList();
   }

   public static final ULocale[] getAvailableULocales() {
      return getAvailableULocales("com/ibm/icu/impl/data/icudt51b", ICU_DATA_CLASS_LOADER);
   }

   public static final Locale[] getAvailableLocales(String var0, ClassLoader var1) {
      return getAvailEntry(var0, var1).getLocaleList();
   }

   public static final Locale[] getAvailableLocales() {
      return getAvailEntry("com/ibm/icu/impl/data/icudt51b", ICU_DATA_CLASS_LOADER).getLocaleList();
   }

   public static final Locale[] getLocaleList(ULocale[] var0) {
      ArrayList var1 = new ArrayList(var0.length);
      HashSet var2 = new HashSet();

      for(int var3 = 0; var3 < var0.length; ++var3) {
         Locale var4 = var0[var3].toLocale();
         if (!var2.contains(var4)) {
            var1.add(var4);
            var2.add(var4);
         }
      }

      return (Locale[])var1.toArray(new Locale[var1.size()]);
   }

   public Locale getLocale() {
      return this.getULocale().toLocale();
   }

   private static final ULocale[] createULocaleList(String var0, ClassLoader var1) {
      ICUResourceBundle var2 = (ICUResourceBundle)UResourceBundle.instantiateBundle(var0, "res_index", var1, true);
      var2 = (ICUResourceBundle)var2.get("InstalledLocales");
      int var3 = var2.getSize();
      int var4 = 0;
      ULocale[] var5 = new ULocale[var3];
      UResourceBundleIterator var6 = var2.getIterator();
      var6.reset();

      while(var6.hasNext()) {
         String var7 = var6.next().getKey();
         if (var7.equals("root")) {
            var5[var4++] = ULocale.ROOT;
         } else {
            var5[var4++] = new ULocale(var7);
         }
      }

      var2 = null;
      return var5;
   }

   private static final Locale[] createLocaleList(String var0, ClassLoader var1) {
      ULocale[] var2 = getAvailEntry(var0, var1).getULocaleList();
      return getLocaleList(var2);
   }

   private static final String[] createLocaleNameArray(String var0, ClassLoader var1) {
      ICUResourceBundle var2 = (ICUResourceBundle)UResourceBundle.instantiateBundle(var0, "res_index", var1, true);
      var2 = (ICUResourceBundle)var2.get("InstalledLocales");
      int var3 = var2.getSize();
      int var4 = 0;
      String[] var5 = new String[var3];
      UResourceBundleIterator var6 = var2.getIterator();
      var6.reset();

      while(var6.hasNext()) {
         String var7 = var6.next().getKey();
         if (var7.equals("root")) {
            var5[var4++] = ULocale.ROOT.toString();
         } else {
            var5[var4++] = var7;
         }
      }

      var2 = null;
      return var5;
   }

   private static final List createFullLocaleNameArray(String var0, ClassLoader var1) {
      List var2 = (List)AccessController.doPrivileged(new PrivilegedAction(var0, var1) {
         final String val$baseName;
         final ClassLoader val$root;

         {
            this.val$baseName = var1;
            this.val$root = var2;
         }

         public List run() {
            String var1 = this.val$baseName.endsWith("/") ? this.val$baseName : this.val$baseName + "/";
            ArrayList var2 = null;
            String var3 = ICUConfig.get("com.ibm.icu.impl.ICUResourceBundle.skipRuntimeLocaleResourceScan", "false");
            if (!var3.equalsIgnoreCase("true")) {
               try {
                  Enumeration var4 = this.val$root.getResources(var1);

                  while(var4.hasMoreElements()) {
                     URL var5 = (URL)var4.nextElement();
                     URLHandler var6 = URLHandler.get(var5);
                     if (var6 != null) {
                        ArrayList var7 = new ArrayList();
                        URLHandler.URLVisitor var8 = new URLHandler.URLVisitor(this, var7) {
                           final List val$lst;
                           final <undefinedtype> this$0;

                           {
                              this.this$0 = var1;
                              this.val$lst = var2;
                           }

                           public void visit(String var1) {
                              if (var1.endsWith(".res")) {
                                 String var2 = var1.substring(0, var1.length() - 4);
                                 if (var2.contains("_") && !var2.equals("res_index")) {
                                    this.val$lst.add(var2);
                                 } else if (var2.length() != 2 && var2.length() != 3) {
                                    if (var2.equalsIgnoreCase("root")) {
                                       this.val$lst.add(ULocale.ROOT.toString());
                                    }
                                 } else {
                                    this.val$lst.add(var2);
                                 }
                              }

                           }
                        };
                        var6.guide(var8, false);
                        if (var2 == null) {
                           var2 = new ArrayList(var7);
                        } else {
                           var2.addAll(var7);
                        }
                     } else if (ICUResourceBundle.access$000()) {
                        System.out.println("handler for " + var5 + " is null");
                     }
                  }
               } catch (IOException var10) {
                  if (ICUResourceBundle.access$000()) {
                     System.out.println("ouch: " + var10.getMessage());
                  }

                  var2 = null;
               }
            }

            if (var2 == null) {
               try {
                  InputStream var11 = this.val$root.getResourceAsStream(var1 + "fullLocaleNames.lst");
                  if (var11 != null) {
                     var2 = new ArrayList();
                     BufferedReader var12 = new BufferedReader(new InputStreamReader(var11, "ASCII"));

                     String var13;
                     while((var13 = var12.readLine()) != null) {
                        if (var13.length() != 0 && !var13.startsWith("#")) {
                           if (var13.equalsIgnoreCase("root")) {
                              var2.add(ULocale.ROOT.toString());
                           } else {
                              var2.add(var13);
                           }
                        }
                     }

                     var12.close();
                  }
               } catch (IOException var9) {
               }
            }

            return var2;
         }

         public Object run() {
            return this.run();
         }
      });
      return var2;
   }

   private static Set createFullLocaleNameSet(String var0, ClassLoader var1) {
      List var2 = createFullLocaleNameArray(var0, var1);
      if (var2 == null) {
         if (DEBUG) {
            System.out.println("createFullLocaleNameArray returned null");
         }

         Set var6 = createLocaleNameSet(var0, var1);
         String var4 = ULocale.ROOT.toString();
         if (!var6.contains(var4)) {
            HashSet var5 = new HashSet(var6);
            var5.add(var4);
            var6 = Collections.unmodifiableSet(var5);
         }

         return var6;
      } else {
         HashSet var3 = new HashSet();
         var3.addAll(var2);
         return Collections.unmodifiableSet(var3);
      }
   }

   private static Set createLocaleNameSet(String var0, ClassLoader var1) {
      try {
         String[] var2 = createLocaleNameArray(var0, var1);
         HashSet var3 = new HashSet();
         var3.addAll(Arrays.asList(var2));
         return Collections.unmodifiableSet(var3);
      } catch (MissingResourceException var4) {
         if (DEBUG) {
            System.out.println("couldn't find index for bundleName: " + var0);
            Thread.dumpStack();
         }

         return Collections.emptySet();
      }
   }

   private static ICUResourceBundle.AvailEntry getAvailEntry(String var0, ClassLoader var1) {
      return (ICUResourceBundle.AvailEntry)GET_AVAILABLE_CACHE.getInstance(var0, var1);
   }

   protected static final ICUResourceBundle findResourceWithFallback(String var0, UResourceBundle var1, UResourceBundle var2) {
      ICUResourceBundle var3 = null;
      if (var2 == null) {
         var2 = var1;
      }

      ICUResourceBundle var4 = (ICUResourceBundle)var1;

      for(String var5 = ((ICUResourceBundle)var1).resPath.length() > 0 ? ((ICUResourceBundle)var1).resPath : ""; var4 != null; var5 = "") {
         if (var0.indexOf(47) == -1) {
            var3 = (ICUResourceBundle)var4.handleGet(var0, (HashMap)null, var2);
            if (var3 != null) {
               break;
            }
         } else {
            ICUResourceBundle var6 = var4;

            for(StringTokenizer var7 = new StringTokenizer(var0, "/"); var7.hasMoreTokens(); var6 = var3) {
               String var8 = var7.nextToken();
               var3 = findResourceWithFallback(var8, var6, var2);
               if (var3 == null) {
                  break;
               }
            }

            if (var3 != null) {
               break;
            }
         }

         var4 = (ICUResourceBundle)var4.getParent();
         var0 = var5.length() > 0 ? var5 + "/" + var0 : var0;
      }

      if (var3 != null) {
         var3.setLoadingStatus(((ICUResourceBundle)var2).getLocaleID());
      }

      return var3;
   }

   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   public static UResourceBundle getBundleInstance(String var0, String var1, ClassLoader var2, boolean var3) {
      UResourceBundle var4 = instantiateBundle(var0, var1, var2, var3);
      if (var4 == null) {
         throw new MissingResourceException("Could not find the bundle " + var0 + "/" + var1 + ".res", "", "");
      } else {
         return var4;
      }
   }

   protected static synchronized UResourceBundle instantiateBundle(String var0, String var1, ClassLoader var2, boolean var3) {
      ULocale var4 = ULocale.getDefault();
      String var5 = var1;
      if (var1.indexOf(64) >= 0) {
         var5 = ULocale.getBaseName(var1);
      }

      String var6 = ICUResourceBundleReader.getFullName(var0, var5);
      ICUResourceBundle var7 = (ICUResourceBundle)loadFromCache(var2, var6, var4);
      String var8 = var0.indexOf(46) == -1 ? "root" : "";
      String var9 = var4.getBaseName();
      if (var5.equals("")) {
         var5 = var8;
      }

      if (DEBUG) {
         System.out.println("Creating " + var6 + " currently b is " + var7);
      }

      if (var7 == null) {
         var7 = createBundle(var0, var5, var2);
         if (DEBUG) {
            System.out.println("The bundle created is: " + var7 + " and disableFallback=" + var3 + " and bundle.getNoFallback=" + (var7 != null && var7.getNoFallback()));
         }

         if (var3 || var7 != null && var7.getNoFallback()) {
            return addToCache(var2, var6, var4, var7);
         }

         if (var7 == null) {
            int var10 = var5.lastIndexOf(95);
            if (var10 != -1) {
               String var11 = var5.substring(0, var10);
               var7 = (ICUResourceBundle)instantiateBundle(var0, var11, var2, var3);
               if (var7 != null && var7.getULocale().getName().equals(var11)) {
                  var7.setLoadingStatus(1);
               }
            } else if (var9.indexOf(var5) == -1) {
               var7 = (ICUResourceBundle)instantiateBundle(var0, var9, var2, var3);
               if (var7 != null) {
                  var7.setLoadingStatus(3);
               }
            } else if (var8.length() != 0) {
               var7 = createBundle(var0, var8, var2);
               if (var7 != null) {
                  var7.setLoadingStatus(2);
               }
            }
         } else {
            UResourceBundle var13 = null;
            var5 = var7.getLocaleID();
            int var14 = var5.lastIndexOf(95);
            var7 = (ICUResourceBundle)addToCache(var2, var6, var4, var7);
            if (var7.getTableResource("%%Parent") != -1) {
               String var12 = var7.getString("%%Parent");
               var13 = instantiateBundle(var0, var12, var2, var3);
            } else if (var14 != -1) {
               var13 = instantiateBundle(var0, var5.substring(0, var14), var2, var3);
            } else if (!var5.equals(var8)) {
               var13 = instantiateBundle(var0, var8, var2, true);
            }

            if (var7 == var13) {
               var7.setParent(var13);
            }
         }
      }

      return var7;
   }

   UResourceBundle get(String var1, HashMap var2, UResourceBundle var3) {
      ICUResourceBundle var4 = (ICUResourceBundle)this.handleGet(var1, var2, var3);
      if (var4 == null) {
         var4 = (ICUResourceBundle)this.getParent();
         if (var4 != null) {
            var4 = (ICUResourceBundle)var4.get(var1, var2, var3);
         }

         if (var4 == null) {
            String var5 = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
            throw new MissingResourceException("Can't find resource for bundle " + var5 + ", key " + var1, this.getClass().getName(), var1);
         }
      }

      var4.setLoadingStatus(((ICUResourceBundle)var3).getLocaleID());
      return var4;
   }

   public static ICUResourceBundle createBundle(String var0, String var1, ClassLoader var2) {
      ICUResourceBundleReader var3 = ICUResourceBundleReader.getReader(var0, var1, var2);
      return var3 == null ? null : getBundle(var3, var0, var1, var2);
   }

   protected String getLocaleID() {
      return this.localeID;
   }

   protected String getBaseName() {
      return this.baseName;
   }

   public ULocale getULocale() {
      return this.ulocale;
   }

   public UResourceBundle getParent() {
      return (UResourceBundle)this.parent;
   }

   protected void setParent(ResourceBundle var1) {
      this.parent = var1;
   }

   public String getKey() {
      return this.key;
   }

   public int getType() {
      return gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(this.resource)];
   }

   private boolean getNoFallback() {
      return this.reader.getNoFallback();
   }

   private static ICUResourceBundle getBundle(ICUResourceBundleReader var0, String var1, String var2, ClassLoader var3) {
      int var5 = var0.getRootResource();
      if (gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(var5)] == 2) {
         ICUResourceBundleImpl.ResourceTable var4 = new ICUResourceBundleImpl.ResourceTable(var0, (String)null, "", var5, (ICUResourceBundleImpl)null);
         var4.baseName = var1;
         var4.localeID = var2;
         var4.ulocale = new ULocale(var2);
         var4.loader = var3;
         UResourceBundle var6 = var4.handleGetImpl("%%ALIAS", (HashMap)null, var4, (int[])null, (boolean[])null);
         return (ICUResourceBundle)(var6 != null ? (ICUResourceBundle)UResourceBundle.getBundleInstance(var1, var6.getString()) : var4);
      } else {
         throw new IllegalStateException("Invalid format error");
      }
   }

   protected ICUResourceBundle(ICUResourceBundleReader var1, String var2, String var3, int var4, ICUResourceBundle var5) {
      this.reader = var1;
      this.key = var2;
      this.resPath = var3;
      this.resource = var4;
      if (var5 != null) {
         this.baseName = var5.baseName;
         this.localeID = var5.localeID;
         this.ulocale = var5.ulocale;
         this.loader = var5.loader;
         this.parent = var5.parent;
      }

   }

   private String getAliasValue(int var1) {
      String var2 = this.reader.getAlias(var1);
      return var2 != null ? var2 : "";
   }

   protected ICUResourceBundle findResource(String var1, String var2, int var3, HashMap var4, UResourceBundle var5) {
      ClassLoader var6 = this.loader;
      String var7 = null;
      String var8 = null;
      String var10 = this.getAliasValue(var3);
      if (var4 == null) {
         var4 = new HashMap();
      }

      if (var4.get(var10) != null) {
         throw new IllegalArgumentException("Circular references in the resource bundles");
      } else {
         var4.put(var10, "");
         String var9;
         int var11;
         if (var10.indexOf(47) == 0) {
            var11 = var10.indexOf(47, 1);
            int var12 = var10.indexOf(47, var11 + 1);
            var9 = var10.substring(1, var11);
            if (var12 < 0) {
               var7 = var10.substring(var11 + 1);
               var8 = var2;
            } else {
               var7 = var10.substring(var11 + 1, var12);
               var8 = var10.substring(var12 + 1, var10.length());
            }

            if (var9.equals("ICUDATA")) {
               var9 = "com/ibm/icu/impl/data/icudt51b";
               var6 = ICU_DATA_CLASS_LOADER;
            } else if (var9.indexOf("ICUDATA") > -1) {
               int var13 = var9.indexOf(45);
               if (var13 > -1) {
                  var9 = "com/ibm/icu/impl/data/icudt51b/" + var9.substring(var13 + 1, var9.length());
                  var6 = ICU_DATA_CLASS_LOADER;
               }
            }
         } else {
            var11 = var10.indexOf(47);
            if (var11 != -1) {
               var7 = var10.substring(0, var11);
               var8 = var10.substring(var11 + 1);
            } else {
               var7 = var10;
               var8 = var2;
            }

            var9 = this.baseName;
         }

         ICUResourceBundle var16 = null;
         ICUResourceBundle var17 = null;
         if (var9.equals("LOCALE")) {
            var9 = this.baseName;
            var8 = var10.substring(8, var10.length());
            var7 = ((ICUResourceBundle)var5).getLocaleID();
            var16 = (ICUResourceBundle)getBundleInstance(var9, var7, var6, false);
            if (var16 != null) {
               var17 = findResourceWithFallback(var8, var16, (UResourceBundle)null);
            }
         } else {
            if (var7 == null) {
               var16 = (ICUResourceBundle)getBundleInstance(var9, "", var6, false);
            } else {
               var16 = (ICUResourceBundle)getBundleInstance(var9, var7, var6, false);
            }

            StringTokenizer var18 = new StringTokenizer(var8, "/");

            for(ICUResourceBundle var14 = var16; var18.hasMoreTokens(); var14 = var17) {
               String var15 = var18.nextToken();
               var17 = (ICUResourceBundle)var14.get(var15, var4, var5);
               if (var17 == null) {
                  break;
               }
            }
         }

         if (var17 == null) {
            throw new MissingResourceException(this.localeID, this.baseName, var1);
         } else {
            return var17;
         }
      }
   }

   protected void createLookupCache() {
      this.lookup = new SimpleCache(1, Math.max(this.getSize() * 2, 64));
   }

   protected UResourceBundle handleGet(String var1, HashMap var2, UResourceBundle var3) {
      UResourceBundle var4 = null;
      if (this.lookup != null) {
         var4 = (UResourceBundle)this.lookup.get(var1);
      }

      if (var4 == null) {
         int[] var5 = new int[1];
         boolean[] var6 = new boolean[1];
         var4 = this.handleGetImpl(var1, var2, var3, var5, var6);
         if (var4 != null && this.lookup != null && !var6[0]) {
            this.lookup.put(var1, var4);
            this.lookup.put(var5[0], var4);
         }
      }

      return var4;
   }

   protected UResourceBundle handleGet(int var1, HashMap var2, UResourceBundle var3) {
      UResourceBundle var4 = null;
      Integer var5 = null;
      if (this.lookup != null) {
         var5 = var1;
         var4 = (UResourceBundle)this.lookup.get(var5);
      }

      if (var4 == null) {
         boolean[] var6 = new boolean[1];
         var4 = this.handleGetImpl(var1, var2, var3, var6);
         if (var4 != null && this.lookup != null && !var6[0]) {
            this.lookup.put(var4.getKey(), var4);
            this.lookup.put(var5, var4);
         }
      }

      return var4;
   }

   protected UResourceBundle handleGetImpl(String var1, HashMap var2, UResourceBundle var3, int[] var4, boolean[] var5) {
      return null;
   }

   protected UResourceBundle handleGetImpl(int var1, HashMap var2, UResourceBundle var3, boolean[] var4) {
      return null;
   }

   /** @deprecated */
   protected int getTableResource(String var1) {
      return -1;
   }

   protected int getTableResource(int var1) {
      return -1;
   }

   /** @deprecated */
   public boolean isAlias(int var1) {
      return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(var1)) == 3;
   }

   /** @deprecated */
   public boolean isAlias() {
      return ICUResourceBundleReader.RES_GET_TYPE(this.resource) == 3;
   }

   /** @deprecated */
   public boolean isAlias(String var1) {
      return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(var1)) == 3;
   }

   /** @deprecated */
   public String getAliasPath(int var1) {
      return this.getAliasValue(this.getTableResource(var1));
   }

   /** @deprecated */
   public String getAliasPath() {
      return this.getAliasValue(this.resource);
   }

   /** @deprecated */
   public String getAliasPath(String var1) {
      return this.getAliasValue(this.getTableResource(var1));
   }

   protected String getKey(int var1) {
      return null;
   }

   /** @deprecated */
   public Enumeration getKeysSafe() {
      if (!ICUResourceBundleReader.URES_IS_TABLE(this.resource)) {
         return this.getKeys();
      } else {
         ArrayList var1 = new ArrayList();
         int var2 = this.getSize();

         for(int var3 = 0; var3 < var2; ++var3) {
            String var4 = this.getKey(var3);
            var1.add(var4);
         }

         return Collections.enumeration(var1);
      }
   }

   protected Enumeration handleGetKeys() {
      return Collections.enumeration(this.handleKeySet());
   }

   protected boolean isTopLevelResource() {
      return this.resPath.length() == 0;
   }

   public UResourceBundle findTopLevel(int var1) {
      return this.findTopLevel(var1);
   }

   public UResourceBundle findTopLevel(String var1) {
      return this.findTopLevel(var1);
   }

   static boolean access$000() {
      return DEBUG;
   }

   static ULocale[] access$100(String var0, ClassLoader var1) {
      return createULocaleList(var0, var1);
   }

   static Locale[] access$200(String var0, ClassLoader var1) {
      return createLocaleList(var0, var1);
   }

   static Set access$300(String var0, ClassLoader var1) {
      return createLocaleNameSet(var0, var1);
   }

   static Set access$400(String var0, ClassLoader var1) {
      return createFullLocaleNameSet(var0, var1);
   }

   static {
      ClassLoader var0 = ICUData.class.getClassLoader();
      if (var0 == null) {
         var0 = Utility.getFallbackClassLoader();
      }

      ICU_DATA_CLASS_LOADER = var0;
      DEBUG = ICUDebug.enabled("localedata");
      GET_AVAILABLE_CACHE = new SoftCache() {
         protected ICUResourceBundle.AvailEntry createInstance(String var1, ClassLoader var2) {
            return new ICUResourceBundle.AvailEntry(var1, var2);
         }

         protected Object createInstance(Object var1, Object var2) {
            return this.createInstance((String)var1, (ClassLoader)var2);
         }
      };
      gPublicTypes = new int[]{0, 1, 2, 3, 2, 2, 0, 7, 8, 8, -1, -1, -1, -1, 14, -1};
   }

   private static final class AvailEntry {
      private String prefix;
      private ClassLoader loader;
      private volatile ULocale[] ulocales;
      private volatile Locale[] locales;
      private volatile Set nameSet;
      private volatile Set fullNameSet;

      AvailEntry(String var1, ClassLoader var2) {
         this.prefix = var1;
         this.loader = var2;
      }

      ULocale[] getULocaleList() {
         if (this.ulocales == null) {
            synchronized(this){}
            if (this.ulocales == null) {
               this.ulocales = ICUResourceBundle.access$100(this.prefix, this.loader);
            }
         }

         return this.ulocales;
      }

      Locale[] getLocaleList() {
         if (this.locales == null) {
            synchronized(this){}
            if (this.locales == null) {
               this.locales = ICUResourceBundle.access$200(this.prefix, this.loader);
            }
         }

         return this.locales;
      }

      Set getLocaleNameSet() {
         if (this.nameSet == null) {
            synchronized(this){}
            if (this.nameSet == null) {
               this.nameSet = ICUResourceBundle.access$300(this.prefix, this.loader);
            }
         }

         return this.nameSet;
      }

      Set getFullLocaleNameSet() {
         if (this.fullNameSet == null) {
            synchronized(this){}
            if (this.fullNameSet == null) {
               this.fullNameSet = ICUResourceBundle.access$400(this.prefix, this.loader);
            }
         }

         return this.fullNameSet;
      }
   }
}
