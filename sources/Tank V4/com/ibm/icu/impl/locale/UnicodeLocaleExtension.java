package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

public class UnicodeLocaleExtension extends Extension {
   public static final char SINGLETON = 'u';
   private static final SortedSet EMPTY_SORTED_SET = new TreeSet();
   private static final SortedMap EMPTY_SORTED_MAP = new TreeMap();
   private SortedSet _attributes;
   private SortedMap _keywords;
   public static final UnicodeLocaleExtension CA_JAPANESE = new UnicodeLocaleExtension();
   public static final UnicodeLocaleExtension NU_THAI;

   private UnicodeLocaleExtension() {
      super('u');
      this._attributes = EMPTY_SORTED_SET;
      this._keywords = EMPTY_SORTED_MAP;
   }

   UnicodeLocaleExtension(SortedSet var1, SortedMap var2) {
      this();
      if (var1 != null && var1.size() > 0) {
         this._attributes = var1;
      }

      if (var2 != null && var2.size() > 0) {
         this._keywords = var2;
      }

      if (this._attributes.size() > 0 || this._keywords.size() > 0) {
         StringBuilder var3 = new StringBuilder();
         Iterator var4 = this._attributes.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            var3.append("-").append(var5);
         }

         var4 = this._keywords.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var8 = (Entry)var4.next();
            String var6 = (String)var8.getKey();
            String var7 = (String)var8.getValue();
            var3.append("-").append(var6);
            if (var7.length() > 0) {
               var3.append("-").append(var7);
            }
         }

         this._value = var3.substring(1);
      }

   }

   public Set getUnicodeLocaleAttributes() {
      return Collections.unmodifiableSet(this._attributes);
   }

   public Set getUnicodeLocaleKeys() {
      return Collections.unmodifiableSet(this._keywords.keySet());
   }

   public String getUnicodeLocaleType(String var1) {
      return (String)this._keywords.get(var1);
   }

   public static boolean isSingletonChar(char var0) {
      return 'u' == AsciiUtil.toLower(var0);
   }

   public static boolean isAttribute(String var0) {
      return var0.length() >= 3 && var0.length() <= 8 && AsciiUtil.isAlphaNumericString(var0);
   }

   public static boolean isKey(String var0) {
      return var0.length() == 2 && AsciiUtil.isAlphaNumericString(var0);
   }

   public static boolean isTypeSubtag(String var0) {
      return var0.length() >= 3 && var0.length() <= 8 && AsciiUtil.isAlphaNumericString(var0);
   }

   static {
      CA_JAPANESE._keywords = new TreeMap();
      CA_JAPANESE._keywords.put("ca", "japanese");
      CA_JAPANESE._value = "ca-japanese";
      NU_THAI = new UnicodeLocaleExtension();
      NU_THAI._keywords = new TreeMap();
      NU_THAI._keywords.put("nu", "thai");
      NU_THAI._value = "nu-thai";
   }
}
