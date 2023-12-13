package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class LocaleExtensions {
   private SortedMap _map;
   private String _id;
   private static final SortedMap EMPTY_MAP = Collections.unmodifiableSortedMap(new TreeMap());
   public static final LocaleExtensions EMPTY_EXTENSIONS = new LocaleExtensions();
   public static final LocaleExtensions CALENDAR_JAPANESE;
   public static final LocaleExtensions NUMBER_THAI;
   static final boolean $assertionsDisabled = !LocaleExtensions.class.desiredAssertionStatus();

   private LocaleExtensions() {
   }

   LocaleExtensions(Map var1, Set var2, Map var3) {
      boolean var4 = var1 != null && var1.size() > 0;
      boolean var5 = var2 != null && var2.size() > 0;
      boolean var6 = var3 != null && var3.size() > 0;
      if (!var4 && !var5 && !var6) {
         this._map = EMPTY_MAP;
         this._id = "";
      } else {
         this._map = new TreeMap();
         if (var4) {
            Iterator var7 = var1.entrySet().iterator();

            label77:
            while(true) {
               char var9;
               String var10;
               do {
                  if (!var7.hasNext()) {
                     break label77;
                  }

                  Entry var8 = (Entry)var7.next();
                  var9 = AsciiUtil.toLower(((InternalLocaleBuilder.CaseInsensitiveChar)var8.getKey()).value());
                  var10 = (String)var8.getValue();
                  if (!LanguageTag.isPrivateusePrefixChar(var9)) {
                     break;
                  }

                  var10 = InternalLocaleBuilder.removePrivateuseVariant(var10);
               } while(var10 == null);

               Extension var11 = new Extension(var9, AsciiUtil.toLowerString(var10));
               this._map.put(var9, var11);
            }
         }

         if (var5 || var6) {
            TreeSet var13 = null;
            TreeMap var14 = null;
            Iterator var15;
            if (var5) {
               var13 = new TreeSet();
               var15 = var2.iterator();

               while(var15.hasNext()) {
                  InternalLocaleBuilder.CaseInsensitiveString var17 = (InternalLocaleBuilder.CaseInsensitiveString)var15.next();
                  var13.add(AsciiUtil.toLowerString(var17.value()));
               }
            }

            if (var6) {
               var14 = new TreeMap();
               var15 = var3.entrySet().iterator();

               while(var15.hasNext()) {
                  Entry var18 = (Entry)var15.next();
                  String var19 = AsciiUtil.toLowerString(((InternalLocaleBuilder.CaseInsensitiveString)var18.getKey()).value());
                  String var12 = AsciiUtil.toLowerString((String)var18.getValue());
                  var14.put(var19, var12);
               }
            }

            UnicodeLocaleExtension var16 = new UnicodeLocaleExtension(var13, var14);
            this._map.put('u', var16);
         }

         if (this._map.size() == 0) {
            this._map = EMPTY_MAP;
            this._id = "";
         } else {
            this._id = toID(this._map);
         }

      }
   }

   public Set getKeys() {
      return Collections.unmodifiableSet(this._map.keySet());
   }

   public Extension getExtension(Character var1) {
      return (Extension)this._map.get(AsciiUtil.toLower(var1));
   }

   public String getExtensionValue(Character var1) {
      Extension var2 = (Extension)this._map.get(AsciiUtil.toLower(var1));
      return var2 == null ? null : var2.getValue();
   }

   public Set getUnicodeLocaleAttributes() {
      Extension var1 = (Extension)this._map.get('u');
      if (var1 == null) {
         return Collections.emptySet();
      } else if (!$assertionsDisabled && !(var1 instanceof UnicodeLocaleExtension)) {
         throw new AssertionError();
      } else {
         return ((UnicodeLocaleExtension)var1).getUnicodeLocaleAttributes();
      }
   }

   public Set getUnicodeLocaleKeys() {
      Extension var1 = (Extension)this._map.get('u');
      if (var1 == null) {
         return Collections.emptySet();
      } else if (!$assertionsDisabled && !(var1 instanceof UnicodeLocaleExtension)) {
         throw new AssertionError();
      } else {
         return ((UnicodeLocaleExtension)var1).getUnicodeLocaleKeys();
      }
   }

   public String getUnicodeLocaleType(String var1) {
      Extension var2 = (Extension)this._map.get('u');
      if (var2 == null) {
         return null;
      } else if (!$assertionsDisabled && !(var2 instanceof UnicodeLocaleExtension)) {
         throw new AssertionError();
      } else {
         return ((UnicodeLocaleExtension)var2).getUnicodeLocaleType(AsciiUtil.toLowerString(var1));
      }
   }

   public boolean isEmpty() {
      return this._map.isEmpty();
   }

   public static boolean isValidKey(char var0) {
      return LanguageTag.isExtensionSingletonChar(var0) || LanguageTag.isPrivateusePrefixChar(var0);
   }

   public static boolean isValidUnicodeLocaleKey(String var0) {
      return UnicodeLocaleExtension.isKey(var0);
   }

   private static String toID(SortedMap var0) {
      StringBuilder var1 = new StringBuilder();
      Extension var2 = null;
      Iterator var3 = var0.entrySet().iterator();

      while(var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         char var5 = (Character)var4.getKey();
         Extension var6 = (Extension)var4.getValue();
         if (LanguageTag.isPrivateusePrefixChar(var5)) {
            var2 = var6;
         } else {
            if (var1.length() > 0) {
               var1.append("-");
            }

            var1.append(var6);
         }
      }

      if (var2 != null) {
         if (var1.length() > 0) {
            var1.append("-");
         }

         var1.append(var2);
      }

      return var1.toString();
   }

   public String toString() {
      return this._id;
   }

   public String getID() {
      return this._id;
   }

   public int hashCode() {
      return this._id.hashCode();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else {
         return !(var1 instanceof LocaleExtensions) ? false : this._id.equals(((LocaleExtensions)var1)._id);
      }
   }

   static {
      EMPTY_EXTENSIONS._id = "";
      EMPTY_EXTENSIONS._map = EMPTY_MAP;
      CALENDAR_JAPANESE = new LocaleExtensions();
      CALENDAR_JAPANESE._id = "u-ca-japanese";
      CALENDAR_JAPANESE._map = new TreeMap();
      CALENDAR_JAPANESE._map.put('u', UnicodeLocaleExtension.CA_JAPANESE);
      NUMBER_THAI = new LocaleExtensions();
      NUMBER_THAI._id = "u-nu-thai";
      NUMBER_THAI._map = new TreeMap();
      NUMBER_THAI._map.put('u', UnicodeLocaleExtension.NU_THAI);
   }
}
