package com.ibm.icu.impl.locale;

public final class BaseLocale {
   private static final boolean JDKIMPL = false;
   public static final String SEP = "_";
   private static final BaseLocale.Cache CACHE = new BaseLocale.Cache();
   public static final BaseLocale ROOT = getInstance("", "", "", "");
   private String _language;
   private String _script;
   private String _region;
   private String _variant;
   private transient volatile int _hash;

   private BaseLocale(String var1, String var2, String var3, String var4) {
      this._language = "";
      this._script = "";
      this._region = "";
      this._variant = "";
      this._hash = 0;
      if (var1 != null) {
         this._language = AsciiUtil.toLowerString(var1).intern();
      }

      if (var2 != null) {
         this._script = AsciiUtil.toTitleString(var2).intern();
      }

      if (var3 != null) {
         this._region = AsciiUtil.toUpperString(var3).intern();
      }

      if (var4 != null) {
         this._variant = AsciiUtil.toUpperString(var4).intern();
      }

   }

   public static BaseLocale getInstance(String var0, String var1, String var2, String var3) {
      BaseLocale.Key var4 = new BaseLocale.Key(var0, var1, var2, var3);
      BaseLocale var5 = (BaseLocale)CACHE.get(var4);
      return var5;
   }

   public String getLanguage() {
      return this._language;
   }

   public String getScript() {
      return this._script;
   }

   public String getRegion() {
      return this._region;
   }

   public String getVariant() {
      return this._variant;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof BaseLocale)) {
         return false;
      } else {
         BaseLocale var2 = (BaseLocale)var1;
         return this.hashCode() == var2.hashCode() && this._language.equals(var2._language) && this._script.equals(var2._script) && this._region.equals(var2._region) && this._variant.equals(var2._variant);
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this._language.length() > 0) {
         var1.append("language=");
         var1.append(this._language);
      }

      if (this._script.length() > 0) {
         if (var1.length() > 0) {
            var1.append(", ");
         }

         var1.append("script=");
         var1.append(this._script);
      }

      if (this._region.length() > 0) {
         if (var1.length() > 0) {
            var1.append(", ");
         }

         var1.append("region=");
         var1.append(this._region);
      }

      if (this._variant.length() > 0) {
         if (var1.length() > 0) {
            var1.append(", ");
         }

         var1.append("variant=");
         var1.append(this._variant);
      }

      return var1.toString();
   }

   public int hashCode() {
      int var1 = this._hash;
      if (var1 == 0) {
         int var2;
         for(var2 = 0; var2 < this._language.length(); ++var2) {
            var1 = 31 * var1 + this._language.charAt(var2);
         }

         for(var2 = 0; var2 < this._script.length(); ++var2) {
            var1 = 31 * var1 + this._script.charAt(var2);
         }

         for(var2 = 0; var2 < this._region.length(); ++var2) {
            var1 = 31 * var1 + this._region.charAt(var2);
         }

         for(var2 = 0; var2 < this._variant.length(); ++var2) {
            var1 = 31 * var1 + this._variant.charAt(var2);
         }

         this._hash = var1;
      }

      return var1;
   }

   BaseLocale(String var1, String var2, String var3, String var4, Object var5) {
      this(var1, var2, var3, var4);
   }

   private static class Cache extends LocaleObjectCache {
      public Cache() {
      }

      protected BaseLocale.Key normalizeKey(BaseLocale.Key var1) {
         return BaseLocale.Key.normalize(var1);
      }

      protected BaseLocale createObject(BaseLocale.Key var1) {
         return new BaseLocale(BaseLocale.Key.access$000(var1), BaseLocale.Key.access$100(var1), BaseLocale.Key.access$200(var1), BaseLocale.Key.access$300(var1));
      }

      protected Object normalizeKey(Object var1) {
         return this.normalizeKey((BaseLocale.Key)var1);
      }

      protected Object createObject(Object var1) {
         return this.createObject((BaseLocale.Key)var1);
      }
   }

   private static class Key implements Comparable {
      private String _lang = "";
      private String _scrt = "";
      private String _regn = "";
      private String _vart = "";
      private volatile int _hash;

      public Key(String var1, String var2, String var3, String var4) {
         if (var1 != null) {
            this._lang = var1;
         }

         if (var2 != null) {
            this._scrt = var2;
         }

         if (var3 != null) {
            this._regn = var3;
         }

         if (var4 != null) {
            this._vart = var4;
         }

      }

      public boolean equals(Object var1) {
         return this == var1 || var1 instanceof BaseLocale.Key && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)var1)._lang, this._lang) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)var1)._scrt, this._scrt) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)var1)._regn, this._regn) && AsciiUtil.caseIgnoreMatch(((BaseLocale.Key)var1)._vart, this._vart);
      }

      public int compareTo(BaseLocale.Key var1) {
         int var2 = AsciiUtil.caseIgnoreCompare(this._lang, var1._lang);
         if (var2 == 0) {
            var2 = AsciiUtil.caseIgnoreCompare(this._scrt, var1._scrt);
            if (var2 == 0) {
               var2 = AsciiUtil.caseIgnoreCompare(this._regn, var1._regn);
               if (var2 == 0) {
                  var2 = AsciiUtil.caseIgnoreCompare(this._vart, var1._vart);
               }
            }
         }

         return var2;
      }

      public int hashCode() {
         int var1 = this._hash;
         if (var1 == 0) {
            int var2;
            for(var2 = 0; var2 < this._lang.length(); ++var2) {
               var1 = 31 * var1 + AsciiUtil.toLower(this._lang.charAt(var2));
            }

            for(var2 = 0; var2 < this._scrt.length(); ++var2) {
               var1 = 31 * var1 + AsciiUtil.toLower(this._scrt.charAt(var2));
            }

            for(var2 = 0; var2 < this._regn.length(); ++var2) {
               var1 = 31 * var1 + AsciiUtil.toLower(this._regn.charAt(var2));
            }

            for(var2 = 0; var2 < this._vart.length(); ++var2) {
               var1 = 31 * var1 + AsciiUtil.toLower(this._vart.charAt(var2));
            }

            this._hash = var1;
         }

         return var1;
      }

      public static BaseLocale.Key normalize(BaseLocale.Key var0) {
         String var1 = AsciiUtil.toLowerString(var0._lang).intern();
         String var2 = AsciiUtil.toTitleString(var0._scrt).intern();
         String var3 = AsciiUtil.toUpperString(var0._regn).intern();
         String var4 = AsciiUtil.toUpperString(var0._vart).intern();
         return new BaseLocale.Key(var1, var2, var3, var4);
      }

      public int compareTo(Object var1) {
         return this.compareTo((BaseLocale.Key)var1);
      }

      static String access$000(BaseLocale.Key var0) {
         return var0._lang;
      }

      static String access$100(BaseLocale.Key var0) {
         return var0._scrt;
      }

      static String access$200(BaseLocale.Key var0) {
         return var0._regn;
      }

      static String access$300(BaseLocale.Key var0) {
         return var0._vart;
      }
   }
}
