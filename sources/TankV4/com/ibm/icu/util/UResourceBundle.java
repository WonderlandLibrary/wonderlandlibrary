package com.ibm.icu.util;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ICUResourceBundleReader;
import com.ibm.icu.impl.ResourceBundleWrapper;
import com.ibm.icu.impl.SimpleCache;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

public abstract class UResourceBundle extends ResourceBundle {
   private static ICUCache BUNDLE_CACHE = new SimpleCache();
   private static final UResourceBundle.ResourceCacheKey cacheKey = new UResourceBundle.ResourceCacheKey();
   private static final int ROOT_MISSING = 0;
   private static final int ROOT_ICU = 1;
   private static final int ROOT_JAVA = 2;
   private static SoftReference ROOT_CACHE = new SoftReference(new ConcurrentHashMap());
   private Set keys = null;
   public static final int NONE = -1;
   public static final int STRING = 0;
   public static final int BINARY = 1;
   public static final int TABLE = 2;
   public static final int INT = 7;
   public static final int ARRAY = 8;
   public static final int INT_VECTOR = 14;

   public static UResourceBundle getBundleInstance(String var0, String var1) {
      return getBundleInstance(var0, var1, ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
   }

   public static UResourceBundle getBundleInstance(String var0, String var1, ClassLoader var2) {
      return getBundleInstance(var0, var1, var2, false);
   }

   protected static UResourceBundle getBundleInstance(String var0, String var1, ClassLoader var2, boolean var3) {
      return instantiateBundle(var0, var1, var2, var3);
   }

   public static UResourceBundle getBundleInstance(ULocale var0) {
      if (var0 == null) {
         var0 = ULocale.getDefault();
      }

      return getBundleInstance("com/ibm/icu/impl/data/icudt51b", var0.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
   }

   public static UResourceBundle getBundleInstance(String var0) {
      if (var0 == null) {
         var0 = "com/ibm/icu/impl/data/icudt51b";
      }

      ULocale var1 = ULocale.getDefault();
      return getBundleInstance(var0, var1.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
   }

   public static UResourceBundle getBundleInstance(String var0, Locale var1) {
      if (var0 == null) {
         var0 = "com/ibm/icu/impl/data/icudt51b";
      }

      ULocale var2 = var1 == null ? ULocale.getDefault() : ULocale.forLocale(var1);
      return getBundleInstance(var0, var2.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
   }

   public static UResourceBundle getBundleInstance(String var0, ULocale var1) {
      if (var0 == null) {
         var0 = "com/ibm/icu/impl/data/icudt51b";
      }

      if (var1 == null) {
         var1 = ULocale.getDefault();
      }

      return getBundleInstance(var0, var1.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
   }

   public static UResourceBundle getBundleInstance(String var0, Locale var1, ClassLoader var2) {
      if (var0 == null) {
         var0 = "com/ibm/icu/impl/data/icudt51b";
      }

      ULocale var3 = var1 == null ? ULocale.getDefault() : ULocale.forLocale(var1);
      return getBundleInstance(var0, var3.toString(), var2, false);
   }

   public static UResourceBundle getBundleInstance(String var0, ULocale var1, ClassLoader var2) {
      if (var0 == null) {
         var0 = "com/ibm/icu/impl/data/icudt51b";
      }

      if (var1 == null) {
         var1 = ULocale.getDefault();
      }

      return getBundleInstance(var0, var1.toString(), var2, false);
   }

   public abstract ULocale getULocale();

   protected abstract String getLocaleID();

   protected abstract String getBaseName();

   protected abstract UResourceBundle getParent();

   public Locale getLocale() {
      return this.getULocale().toLocale();
   }

   /** @deprecated */
   public static void resetBundleCache() {
      BUNDLE_CACHE = new SimpleCache();
   }

   /** @deprecated */
   protected static UResourceBundle addToCache(ClassLoader var0, String var1, ULocale var2, UResourceBundle var3) {
      UResourceBundle.ResourceCacheKey var4;
      synchronized(var4 = cacheKey){}
      UResourceBundle.ResourceCacheKey.access$000(cacheKey, var0, var1, var2);
      UResourceBundle var5 = (UResourceBundle)BUNDLE_CACHE.get(cacheKey);
      if (var5 != null) {
         return var5;
      } else {
         BUNDLE_CACHE.put((UResourceBundle.ResourceCacheKey)cacheKey.clone(), var3);
         return var3;
      }
   }

   /** @deprecated */
   protected static UResourceBundle loadFromCache(ClassLoader var0, String var1, ULocale var2) {
      UResourceBundle.ResourceCacheKey var3;
      synchronized(var3 = cacheKey){}
      UResourceBundle.ResourceCacheKey.access$000(cacheKey, var0, var1, var2);
      return (UResourceBundle)BUNDLE_CACHE.get(cacheKey);
   }

   private static int getRootType(String var0, ClassLoader var1) {
      ConcurrentHashMap var2 = null;
      var2 = (ConcurrentHashMap)ROOT_CACHE.get();
      if (var2 == null) {
         Class var4 = UResourceBundle.class;
         synchronized(UResourceBundle.class){}
         var2 = (ConcurrentHashMap)ROOT_CACHE.get();
         if (var2 == null) {
            var2 = new ConcurrentHashMap();
            ROOT_CACHE = new SoftReference(var2);
         }
      }

      Integer var3 = (Integer)var2.get(var0);
      if (var3 == null) {
         String var10 = var0.indexOf(46) == -1 ? "root" : "";
         byte var5 = 0;

         try {
            ICUResourceBundle.getBundleInstance(var0, var10, var1, true);
            var5 = 1;
         } catch (MissingResourceException var9) {
            try {
               ResourceBundleWrapper.getBundleInstance(var0, var10, var1, true);
               var5 = 2;
            } catch (MissingResourceException var8) {
            }
         }

         var3 = Integer.valueOf(var5);
         var2.putIfAbsent(var0, var3);
      }

      return var3;
   }

   private static void setRootType(String var0, int var1) {
      Integer var2 = var1;
      ConcurrentHashMap var3 = null;
      var3 = (ConcurrentHashMap)ROOT_CACHE.get();
      if (var3 == null) {
         Class var4 = UResourceBundle.class;
         synchronized(UResourceBundle.class){}
         var3 = (ConcurrentHashMap)ROOT_CACHE.get();
         if (var3 == null) {
            var3 = new ConcurrentHashMap();
            ROOT_CACHE = new SoftReference(var3);
         }
      }

      var3.put(var0, var2);
   }

   protected static UResourceBundle instantiateBundle(String var0, String var1, ClassLoader var2, boolean var3) {
      UResourceBundle var4 = null;
      int var5 = getRootType(var0, var2);
      ULocale var6 = ULocale.getDefault();
      switch(var5) {
      case 1:
         if (var3) {
            String var7 = ICUResourceBundleReader.getFullName(var0, var1);
            var4 = loadFromCache(var2, var7, var6);
            if (var4 == null) {
               var4 = ICUResourceBundle.getBundleInstance(var0, var1, var2, var3);
            }
         } else {
            var4 = ICUResourceBundle.getBundleInstance(var0, var1, var2, var3);
         }

         return var4;
      case 2:
         return ResourceBundleWrapper.getBundleInstance(var0, var1, var2, var3);
      default:
         try {
            var4 = ICUResourceBundle.getBundleInstance(var0, var1, var2, var3);
            setRootType(var0, 1);
         } catch (MissingResourceException var8) {
            var4 = ResourceBundleWrapper.getBundleInstance(var0, var1, var2, var3);
            setRootType(var0, 2);
         }

         return var4;
      }
   }

   public ByteBuffer getBinary() {
      throw new UResourceTypeMismatchException("");
   }

   public String getString() {
      throw new UResourceTypeMismatchException("");
   }

   public String[] getStringArray() {
      throw new UResourceTypeMismatchException("");
   }

   public byte[] getBinary(byte[] var1) {
      throw new UResourceTypeMismatchException("");
   }

   public int[] getIntVector() {
      throw new UResourceTypeMismatchException("");
   }

   public int getInt() {
      throw new UResourceTypeMismatchException("");
   }

   public int getUInt() {
      throw new UResourceTypeMismatchException("");
   }

   public UResourceBundle get(String var1) {
      UResourceBundle var2 = this.findTopLevel(var1);
      if (var2 == null) {
         String var3 = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
         throw new MissingResourceException("Can't find resource for bundle " + var3 + ", key " + var1, this.getClass().getName(), var1);
      } else {
         return var2;
      }
   }

   /** @deprecated */
   protected UResourceBundle findTopLevel(String var1) {
      for(UResourceBundle var2 = this; var2 != null; var2 = var2.getParent()) {
         UResourceBundle var3 = var2.handleGet(var1, (HashMap)null, this);
         if (var3 != null) {
            ((ICUResourceBundle)var3).setLoadingStatus(this.getLocaleID());
            return var3;
         }
      }

      return null;
   }

   public String getString(int var1) {
      ICUResourceBundle var2 = (ICUResourceBundle)this.get(var1);
      if (var2.getType() == 0) {
         return var2.getString();
      } else {
         throw new UResourceTypeMismatchException("");
      }
   }

   public UResourceBundle get(int var1) {
      Object var2 = this.handleGet(var1, (HashMap)null, this);
      if (var2 == null) {
         var2 = (ICUResourceBundle)this.getParent();
         if (var2 != null) {
            var2 = ((UResourceBundle)var2).get(var1);
         }

         if (var2 == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getKey(), this.getClass().getName(), this.getKey());
         }
      }

      ((ICUResourceBundle)var2).setLoadingStatus(this.getLocaleID());
      return (UResourceBundle)var2;
   }

   /** @deprecated */
   protected UResourceBundle findTopLevel(int var1) {
      for(UResourceBundle var2 = this; var2 != null; var2 = var2.getParent()) {
         UResourceBundle var3 = var2.handleGet(var1, (HashMap)null, this);
         if (var3 != null) {
            ((ICUResourceBundle)var3).setLoadingStatus(this.getLocaleID());
            return var3;
         }
      }

      return null;
   }

   public Enumeration getKeys() {
      return Collections.enumeration(this.keySet());
   }

   /** @deprecated */
   public Set keySet() {
      if (this.keys == null) {
         if (!this.isTopLevelResource()) {
            return this.handleKeySet();
         }

         TreeSet var1;
         if (this.parent == null) {
            var1 = new TreeSet();
         } else if (this.parent instanceof UResourceBundle) {
            var1 = new TreeSet(((UResourceBundle)this.parent).keySet());
         } else {
            var1 = new TreeSet();
            Enumeration var2 = this.parent.getKeys();

            while(var2.hasMoreElements()) {
               var1.add(var2.nextElement());
            }
         }

         var1.addAll(this.handleKeySet());
         this.keys = Collections.unmodifiableSet(var1);
      }

      return this.keys;
   }

   /** @deprecated */
   protected Set handleKeySet() {
      return Collections.emptySet();
   }

   public int getSize() {
      return 1;
   }

   public int getType() {
      return -1;
   }

   public VersionInfo getVersion() {
      return null;
   }

   public UResourceBundleIterator getIterator() {
      return new UResourceBundleIterator(this);
   }

   public String getKey() {
      return null;
   }

   protected UResourceBundle handleGet(String var1, HashMap var2, UResourceBundle var3) {
      return null;
   }

   protected UResourceBundle handleGet(int var1, HashMap var2, UResourceBundle var3) {
      return null;
   }

   protected String[] handleGetStringArray() {
      return null;
   }

   protected Enumeration handleGetKeys() {
      return null;
   }

   protected Object handleGetObject(String var1) {
      return this.handleGetObjectImpl(var1, this);
   }

   private Object handleGetObjectImpl(String var1, UResourceBundle var2) {
      Object var3 = this.resolveObject(var1, var2);
      if (var3 == null) {
         UResourceBundle var4 = this.getParent();
         if (var4 != null) {
            var3 = var4.handleGetObjectImpl(var1, var2);
         }

         if (var3 == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + var1, this.getClass().getName(), var1);
         }
      }

      return var3;
   }

   private Object resolveObject(String var1, UResourceBundle var2) {
      if (this.getType() == 0) {
         return this.getString();
      } else {
         UResourceBundle var3 = this.handleGet(var1, (HashMap)null, var2);
         if (var3 != null) {
            if (var3.getType() == 0) {
               return var3.getString();
            }

            try {
               if (var3.getType() == 8) {
                  return var3.handleGetStringArray();
               }
            } catch (UResourceTypeMismatchException var5) {
               return var3;
            }
         }

         return var3;
      }
   }

   /** @deprecated */
   protected abstract void setLoadingStatus(int var1);

   /** @deprecated */
   protected boolean isTopLevelResource() {
      return true;
   }

   private static final class ResourceCacheKey implements Cloneable {
      private SoftReference loaderRef;
      private String searchName;
      private ULocale defaultLocale;
      private int hashCodeCache;

      private ResourceCacheKey() {
      }

      public boolean equals(Object var1) {
         if (var1 == null) {
            return false;
         } else if (this == var1) {
            return true;
         } else {
            try {
               UResourceBundle.ResourceCacheKey var2 = (UResourceBundle.ResourceCacheKey)var1;
               if (this.hashCodeCache != var2.hashCodeCache) {
                  return false;
               } else if (!this.searchName.equals(var2.searchName)) {
                  return false;
               } else {
                  if (this.defaultLocale == null) {
                     if (var2.defaultLocale != null) {
                        return false;
                     }
                  } else if (!this.defaultLocale.equals(var2.defaultLocale)) {
                     return false;
                  }

                  if (this.loaderRef == null) {
                     return var2.loaderRef == null;
                  } else {
                     return var2.loaderRef != null && this.loaderRef.get() == var2.loaderRef.get();
                  }
               }
            } catch (NullPointerException var3) {
               return false;
            } catch (ClassCastException var4) {
               return false;
            }
         }
      }

      public int hashCode() {
         return this.hashCodeCache;
      }

      public Object clone() {
         try {
            return super.clone();
         } catch (CloneNotSupportedException var2) {
            throw new IllegalStateException();
         }
      }

      private synchronized void setKeyValues(ClassLoader var1, String var2, ULocale var3) {
         this.searchName = var2;
         this.hashCodeCache = var2.hashCode();
         this.defaultLocale = var3;
         if (var3 != null) {
            this.hashCodeCache ^= var3.hashCode();
         }

         if (var1 == null) {
            this.loaderRef = null;
         } else {
            this.loaderRef = new SoftReference(var1);
            this.hashCodeCache ^= var1.hashCode();
         }

      }

      static void access$000(UResourceBundle.ResourceCacheKey var0, ClassLoader var1, String var2, ULocale var3) {
         var0.setKeyValues(var1, var2, var3);
      }

      ResourceCacheKey(Object var1) {
         this();
      }
   }
}
