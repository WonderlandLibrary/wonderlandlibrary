package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ICULocaleService extends ICUService {
   private ULocale fallbackLocale;
   private String fallbackLocaleName;

   public ICULocaleService() {
   }

   public ICULocaleService(String var1) {
      super(var1);
   }

   public Object get(ULocale var1) {
      return this.get(var1, -1, (ULocale[])null);
   }

   public Object get(ULocale var1, int var2) {
      return this.get(var1, var2, (ULocale[])null);
   }

   public Object get(ULocale var1, ULocale[] var2) {
      return this.get(var1, -1, var2);
   }

   public Object get(ULocale var1, int var2, ULocale[] var3) {
      ICUService.Key var4 = this.createKey(var1, var2);
      if (var3 == null) {
         return this.getKey(var4);
      } else {
         String[] var5 = new String[1];
         Object var6 = this.getKey(var4, var5);
         if (var6 != null) {
            int var7 = var5[0].indexOf("/");
            if (var7 >= 0) {
               var5[0] = var5[0].substring(var7 + 1);
            }

            var3[0] = new ULocale(var5[0]);
         }

         return var6;
      }
   }

   public ICUService.Factory registerObject(Object var1, ULocale var2) {
      return this.registerObject(var1, var2, -1, true);
   }

   public ICUService.Factory registerObject(Object var1, ULocale var2, boolean var3) {
      return this.registerObject(var1, var2, -1, var3);
   }

   public ICUService.Factory registerObject(Object var1, ULocale var2, int var3) {
      return this.registerObject(var1, var2, var3, true);
   }

   public ICUService.Factory registerObject(Object var1, ULocale var2, int var3, boolean var4) {
      ICULocaleService.SimpleLocaleKeyFactory var5 = new ICULocaleService.SimpleLocaleKeyFactory(var1, var2, var3, var4);
      return this.registerFactory(var5);
   }

   public Locale[] getAvailableLocales() {
      Set var1 = this.getVisibleIDs();
      Locale[] var2 = new Locale[var1.size()];
      int var3 = 0;

      Locale var6;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2[var3++] = var6) {
         String var5 = (String)var4.next();
         var6 = LocaleUtility.getLocaleFromName(var5);
      }

      return var2;
   }

   public ULocale[] getAvailableULocales() {
      Set var1 = this.getVisibleIDs();
      ULocale[] var2 = new ULocale[var1.size()];
      int var3 = 0;

      String var5;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2[var3++] = new ULocale(var5)) {
         var5 = (String)var4.next();
      }

      return var2;
   }

   public String validateFallbackLocale() {
      ULocale var1 = ULocale.getDefault();
      if (var1 != this.fallbackLocale) {
         synchronized(this){}
         if (var1 != this.fallbackLocale) {
            this.fallbackLocale = var1;
            this.fallbackLocaleName = var1.getBaseName();
            this.clearServiceCache();
         }
      }

      return this.fallbackLocaleName;
   }

   public ICUService.Key createKey(String var1) {
      return ICULocaleService.LocaleKey.createWithCanonicalFallback(var1, this.validateFallbackLocale());
   }

   public ICUService.Key createKey(String var1, int var2) {
      return ICULocaleService.LocaleKey.createWithCanonicalFallback(var1, this.validateFallbackLocale(), var2);
   }

   public ICUService.Key createKey(ULocale var1, int var2) {
      return ICULocaleService.LocaleKey.createWithCanonical(var1, this.validateFallbackLocale(), var2);
   }

   public static class ICUResourceBundleFactory extends ICULocaleService.LocaleKeyFactory {
      protected final String bundleName;

      public ICUResourceBundleFactory() {
         this("com/ibm/icu/impl/data/icudt51b");
      }

      public ICUResourceBundleFactory(String var1) {
         super(true);
         this.bundleName = var1;
      }

      protected Set getSupportedIDs() {
         return ICUResourceBundle.getFullLocaleNameSet(this.bundleName, this.loader());
      }

      public void updateVisibleIDs(Map var1) {
         Set var2 = ICUResourceBundle.getAvailableLocaleNameSet(this.bundleName, this.loader());
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            var1.put(var4, this);
         }

      }

      protected Object handleCreate(ULocale var1, int var2, ICUService var3) {
         return ICUResourceBundle.getBundleInstance(this.bundleName, var1, this.loader());
      }

      protected ClassLoader loader() {
         ClassLoader var1 = this.getClass().getClassLoader();
         if (var1 == null) {
            var1 = Utility.getFallbackClassLoader();
         }

         return var1;
      }

      public String toString() {
         return super.toString() + ", bundle: " + this.bundleName;
      }
   }

   public static class SimpleLocaleKeyFactory extends ICULocaleService.LocaleKeyFactory {
      private final Object obj;
      private final String id;
      private final int kind;

      public SimpleLocaleKeyFactory(Object var1, ULocale var2, int var3, boolean var4) {
         this(var1, var2, var3, var4, (String)null);
      }

      public SimpleLocaleKeyFactory(Object var1, ULocale var2, int var3, boolean var4, String var5) {
         super(var4, var5);
         this.obj = var1;
         this.id = var2.getBaseName();
         this.kind = var3;
      }

      public Object create(ICUService.Key var1, ICUService var2) {
         if (!(var1 instanceof ICULocaleService.LocaleKey)) {
            return null;
         } else {
            ICULocaleService.LocaleKey var3 = (ICULocaleService.LocaleKey)var1;
            if (this.kind != -1 && this.kind != var3.kind()) {
               return null;
            } else {
               return !this.id.equals(var3.currentID()) ? null : this.obj;
            }
         }
      }

      protected boolean isSupportedID(String var1) {
         return this.id.equals(var1);
      }

      public void updateVisibleIDs(Map var1) {
         if (this.visible) {
            var1.put(this.id, this);
         } else {
            var1.remove(this.id);
         }

      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(super.toString());
         var1.append(", id: ");
         var1.append(this.id);
         var1.append(", kind: ");
         var1.append(this.kind);
         return var1.toString();
      }
   }

   public abstract static class LocaleKeyFactory implements ICUService.Factory {
      protected final String name;
      protected final boolean visible;
      public static final boolean VISIBLE = true;
      public static final boolean INVISIBLE = false;

      protected LocaleKeyFactory(boolean var1) {
         this.visible = var1;
         this.name = null;
      }

      protected LocaleKeyFactory(boolean var1, String var2) {
         this.visible = var1;
         this.name = var2;
      }

      public Object create(ICUService.Key var1, ICUService var2) {
         if (var1 != null) {
            ICULocaleService.LocaleKey var3 = (ICULocaleService.LocaleKey)var1;
            int var4 = var3.kind();
            ULocale var5 = var3.currentLocale();
            return this.handleCreate(var5, var4, var2);
         } else {
            return null;
         }
      }

      public void updateVisibleIDs(Map var1) {
         Set var2 = this.getSupportedIDs();
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            if (this.visible) {
               var1.put(var4, this);
            } else {
               var1.remove(var4);
            }
         }

      }

      public String getDisplayName(String var1, ULocale var2) {
         if (var2 == null) {
            return var1;
         } else {
            ULocale var3 = new ULocale(var1);
            return var3.getDisplayName(var2);
         }
      }

      protected Object handleCreate(ULocale var1, int var2, ICUService var3) {
         return null;
      }

      protected boolean isSupportedID(String var1) {
         return this.getSupportedIDs().contains(var1);
      }

      protected Set getSupportedIDs() {
         return Collections.emptySet();
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder(super.toString());
         if (this.name != null) {
            var1.append(", name: ");
            var1.append(this.name);
         }

         var1.append(", visible: ");
         var1.append(this.visible);
         return var1.toString();
      }
   }

   public static class LocaleKey extends ICUService.Key {
      private int kind;
      private int varstart;
      private String primaryID;
      private String fallbackID;
      private String currentID;
      public static final int KIND_ANY = -1;

      public static ICULocaleService.LocaleKey createWithCanonicalFallback(String var0, String var1) {
         return createWithCanonicalFallback(var0, var1, -1);
      }

      public static ICULocaleService.LocaleKey createWithCanonicalFallback(String var0, String var1, int var2) {
         if (var0 == null) {
            return null;
         } else {
            String var3 = ULocale.getName(var0);
            return new ICULocaleService.LocaleKey(var0, var3, var1, var2);
         }
      }

      public static ICULocaleService.LocaleKey createWithCanonical(ULocale var0, String var1, int var2) {
         if (var0 == null) {
            return null;
         } else {
            String var3 = var0.getName();
            return new ICULocaleService.LocaleKey(var3, var3, var1, var2);
         }
      }

      protected LocaleKey(String var1, String var2, String var3, int var4) {
         super(var1);
         this.kind = var4;
         if (var2 != null && !var2.equalsIgnoreCase("root")) {
            int var5 = var2.indexOf(64);
            if (var5 == 4 && var2.regionMatches(true, 0, "root", 0, 4)) {
               this.primaryID = var2.substring(4);
               this.varstart = 0;
               this.fallbackID = null;
            } else {
               this.primaryID = var2;
               this.varstart = var5;
               if (var3 != null && !this.primaryID.equals(var3)) {
                  this.fallbackID = var3;
               } else {
                  this.fallbackID = "";
               }
            }
         } else {
            this.primaryID = "";
            this.fallbackID = null;
         }

         this.currentID = this.varstart == -1 ? this.primaryID : this.primaryID.substring(0, this.varstart);
      }

      public String prefix() {
         return this.kind == -1 ? null : Integer.toString(this.kind());
      }

      public int kind() {
         return this.kind;
      }

      public String canonicalID() {
         return this.primaryID;
      }

      public String currentID() {
         return this.currentID;
      }

      public String currentDescriptor() {
         String var1 = this.currentID();
         if (var1 != null) {
            StringBuilder var2 = new StringBuilder();
            if (this.kind != -1) {
               var2.append(this.prefix());
            }

            var2.append('/');
            var2.append(var1);
            if (this.varstart != -1) {
               var2.append(this.primaryID.substring(this.varstart, this.primaryID.length()));
            }

            var1 = var2.toString();
         }

         return var1;
      }

      public ULocale canonicalLocale() {
         return new ULocale(this.primaryID);
      }

      public ULocale currentLocale() {
         return this.varstart == -1 ? new ULocale(this.currentID) : new ULocale(this.currentID + this.primaryID.substring(this.varstart));
      }

      public boolean fallback() {
         int var1 = this.currentID.lastIndexOf(95);
         if (var1 == -1) {
            if (this.fallbackID != null) {
               this.currentID = this.fallbackID;
               if (this.fallbackID.length() == 0) {
                  this.fallbackID = null;
               } else {
                  this.fallbackID = "";
               }

               return true;
            } else {
               this.currentID = null;
               return false;
            }
         } else {
            do {
               --var1;
            } while(var1 >= 0 && this.currentID.charAt(var1) == '_');

            this.currentID = this.currentID.substring(0, var1 + 1);
            return true;
         }
      }

      public boolean isFallbackOf(String var1) {
         return LocaleUtility.isFallbackOf(this.canonicalID(), var1);
      }
   }
}
