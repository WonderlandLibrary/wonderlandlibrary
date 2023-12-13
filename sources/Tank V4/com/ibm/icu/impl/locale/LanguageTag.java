package com.ibm.icu.impl.locale;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LanguageTag {
   private static final boolean JDKIMPL = false;
   public static final String SEP = "-";
   public static final String PRIVATEUSE = "x";
   public static String UNDETERMINED = "und";
   public static final String PRIVUSE_VARIANT_PREFIX = "lvariant";
   private String _language = "";
   private String _script = "";
   private String _region = "";
   private String _privateuse = "";
   private List _extlangs = Collections.emptyList();
   private List _variants = Collections.emptyList();
   private List _extensions = Collections.emptyList();
   private static final Map GRANDFATHERED = new HashMap();

   private LanguageTag() {
   }

   public static LanguageTag parse(String var0, ParseStatus var1) {
      if (var1 == null) {
         var1 = new ParseStatus();
      } else {
         var1.reset();
      }

      String[] var3 = (String[])GRANDFATHERED.get(new AsciiUtil.CaseInsensitiveKey(var0));
      StringTokenIterator var2;
      if (var3 != null) {
         var2 = new StringTokenIterator(var3[1], "-");
      } else {
         var2 = new StringTokenIterator(var0, "-");
      }

      LanguageTag var4 = new LanguageTag();
      if (var1 == false) {
         var4.parseExtlangs(var2, var1);
         var4.parseScript(var2, var1);
         var4.parseRegion(var2, var1);
         var4.parseVariants(var2, var1);
         var4.parseExtensions(var2, var1);
      }

      var4.parsePrivateuse(var2, var1);
      if (!var2.isDone() && !var1.isError()) {
         String var5 = var2.current();
         var1._errorIndex = var2.currentStart();
         if (var5.length() == 0) {
            var1._errorMsg = "Empty subtag";
         } else {
            var1._errorMsg = "Invalid subtag: " + var5;
         }
      }

      return var4;
   }

   private boolean parseExtlangs(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean parseScript(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean parseRegion(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean parseVariants(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean parseExtensions(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean parsePrivateuse(StringTokenIterator param1, ParseStatus param2) {
      // $FF: Couldn't be decompiled
   }

   public static LanguageTag parseLocale(BaseLocale param0, LocaleExtensions param1) {
      // $FF: Couldn't be decompiled
   }

   public String getLanguage() {
      return this._language;
   }

   public List getExtlangs() {
      return Collections.unmodifiableList(this._extlangs);
   }

   public String getScript() {
      return this._script;
   }

   public String getRegion() {
      return this._region;
   }

   public List getVariants() {
      return Collections.unmodifiableList(this._variants);
   }

   public List getExtensions() {
      return Collections.unmodifiableList(this._extensions);
   }

   public String getPrivateuse() {
      return this._privateuse;
   }

   public static boolean isExtensionSingletonChar(char var0) {
      return isExtensionSingleton(String.valueOf(var0));
   }

   public static boolean isPrivateusePrefixChar(char var0) {
      return AsciiUtil.caseIgnoreMatch("x", String.valueOf(var0));
   }

   public static String canonicalizeLanguage(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizeExtlang(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizeScript(String var0) {
      return AsciiUtil.toTitleString(var0);
   }

   public static String canonicalizeRegion(String var0) {
      return AsciiUtil.toUpperString(var0);
   }

   public static String canonicalizeVariant(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizeExtension(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizeExtensionSingleton(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizeExtensionSubtag(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizePrivateuse(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public static String canonicalizePrivateuseSubtag(String var0) {
      return AsciiUtil.toLowerString(var0);
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this._language.length() > 0) {
         var1.append(this._language);
         Iterator var2 = this._extlangs.iterator();

         String var3;
         while(var2.hasNext()) {
            var3 = (String)var2.next();
            var1.append("-").append(var3);
         }

         if (this._script.length() > 0) {
            var1.append("-").append(this._script);
         }

         if (this._region.length() > 0) {
            var1.append("-").append(this._region);
         }

         var2 = this._extlangs.iterator();

         while(var2.hasNext()) {
            var3 = (String)var2.next();
            var1.append("-").append(var3);
         }

         var2 = this._extensions.iterator();

         while(var2.hasNext()) {
            var3 = (String)var2.next();
            var1.append("-").append(var3);
         }
      }

      if (this._privateuse.length() > 0) {
         if (var1.length() > 0) {
            var1.append("-");
         }

         var1.append(this._privateuse);
      }

      return var1.toString();
   }

   static {
      String[][] var0 = new String[][]{{"art-lojban", "jbo"}, {"cel-gaulish", "xtg-x-cel-gaulish"}, {"en-GB-oed", "en-GB-x-oed"}, {"i-ami", "ami"}, {"i-bnn", "bnn"}, {"i-default", "en-x-i-default"}, {"i-enochian", "und-x-i-enochian"}, {"i-hak", "hak"}, {"i-klingon", "tlh"}, {"i-lux", "lb"}, {"i-mingo", "see-x-i-mingo"}, {"i-navajo", "nv"}, {"i-pwn", "pwn"}, {"i-tao", "tao"}, {"i-tay", "tay"}, {"i-tsu", "tsu"}, {"no-bok", "nb"}, {"no-nyn", "nn"}, {"sgn-BE-FR", "sfb"}, {"sgn-BE-NL", "vgt"}, {"sgn-CH-DE", "sgg"}, {"zh-guoyu", "cmn"}, {"zh-hakka", "hak"}, {"zh-min", "nan-x-zh-min"}, {"zh-min-nan", "nan"}, {"zh-xiang", "hsn"}};
      String[][] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String[] var4 = var1[var3];
         GRANDFATHERED.put(new AsciiUtil.CaseInsensitiveKey(var4[0]), var4);
      }

   }
}
