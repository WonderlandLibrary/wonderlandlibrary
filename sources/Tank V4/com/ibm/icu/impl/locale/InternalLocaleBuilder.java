package com.ibm.icu.impl.locale;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class InternalLocaleBuilder {
   private static final boolean JDKIMPL = false;
   private String _language = "";
   private String _script = "";
   private String _region = "";
   private String _variant = "";
   private static final InternalLocaleBuilder.CaseInsensitiveChar PRIVUSE_KEY = new InternalLocaleBuilder.CaseInsensitiveChar("x".charAt(0));
   private HashMap _extensions;
   private HashSet _uattributes;
   private HashMap _ukeywords;
   static final boolean $assertionsDisabled = !InternalLocaleBuilder.class.desiredAssertionStatus();

   public InternalLocaleBuilder setLanguage(String var1) throws LocaleSyntaxException {
      if (var1 != null && var1.length() != 0) {
         if (!LanguageTag.isLanguage(var1)) {
            throw new LocaleSyntaxException("Ill-formed language: " + var1, 0);
         }

         this._language = var1;
      } else {
         this._language = "";
      }

      return this;
   }

   public InternalLocaleBuilder setScript(String var1) throws LocaleSyntaxException {
      if (var1 != null && var1.length() != 0) {
         if (!LanguageTag.isScript(var1)) {
            throw new LocaleSyntaxException("Ill-formed script: " + var1, 0);
         }

         this._script = var1;
      } else {
         this._script = "";
      }

      return this;
   }

   public InternalLocaleBuilder setRegion(String var1) throws LocaleSyntaxException {
      if (var1 != null && var1.length() != 0) {
         if (!LanguageTag.isRegion(var1)) {
            throw new LocaleSyntaxException("Ill-formed region: " + var1, 0);
         }

         this._region = var1;
      } else {
         this._region = "";
      }

      return this;
   }

   public InternalLocaleBuilder setVariant(String var1) throws LocaleSyntaxException {
      if (var1 != null && var1.length() != 0) {
         String var2 = var1.replaceAll("-", "_");
         int var3 = this.checkVariants(var2, "_");
         if (var3 != -1) {
            throw new LocaleSyntaxException("Ill-formed variant: " + var1, var3);
         }

         this._variant = var2;
      } else {
         this._variant = "";
      }

      return this;
   }

   public InternalLocaleBuilder addUnicodeLocaleAttribute(String var1) throws LocaleSyntaxException {
      if (var1 != null && UnicodeLocaleExtension.isAttribute(var1)) {
         if (this._uattributes == null) {
            this._uattributes = new HashSet(4);
         }

         this._uattributes.add(new InternalLocaleBuilder.CaseInsensitiveString(var1));
         return this;
      } else {
         throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + var1);
      }
   }

   public InternalLocaleBuilder removeUnicodeLocaleAttribute(String var1) throws LocaleSyntaxException {
      if (var1 != null && UnicodeLocaleExtension.isAttribute(var1)) {
         if (this._uattributes != null) {
            this._uattributes.remove(new InternalLocaleBuilder.CaseInsensitiveString(var1));
         }

         return this;
      } else {
         throw new LocaleSyntaxException("Ill-formed Unicode locale attribute: " + var1);
      }
   }

   public InternalLocaleBuilder setUnicodeLocaleKeyword(String var1, String var2) throws LocaleSyntaxException {
      if (!UnicodeLocaleExtension.isKey(var1)) {
         throw new LocaleSyntaxException("Ill-formed Unicode locale keyword key: " + var1);
      } else {
         InternalLocaleBuilder.CaseInsensitiveString var3 = new InternalLocaleBuilder.CaseInsensitiveString(var1);
         if (var2 == null) {
            if (this._ukeywords != null) {
               this._ukeywords.remove(var3);
            }
         } else {
            if (var2.length() != 0) {
               String var4 = var2.replaceAll("_", "-");
               StringTokenIterator var5 = new StringTokenIterator(var4, "-");

               while(!var5.isDone()) {
                  String var6 = var5.current();
                  if (!UnicodeLocaleExtension.isTypeSubtag(var6)) {
                     throw new LocaleSyntaxException("Ill-formed Unicode locale keyword type: " + var2, var5.currentStart());
                  }

                  var5.next();
               }
            }

            if (this._ukeywords == null) {
               this._ukeywords = new HashMap(4);
            }

            this._ukeywords.put(var3, var2);
         }

         return this;
      }
   }

   public InternalLocaleBuilder setExtension(char var1, String var2) throws LocaleSyntaxException {
      boolean var3 = LanguageTag.isPrivateusePrefixChar(var1);
      if (!var3 && !LanguageTag.isExtensionSingletonChar(var1)) {
         throw new LocaleSyntaxException("Ill-formed extension key: " + var1);
      } else {
         boolean var4 = var2 == null || var2.length() == 0;
         InternalLocaleBuilder.CaseInsensitiveChar var5 = new InternalLocaleBuilder.CaseInsensitiveChar(var1);
         if (var4) {
            if (UnicodeLocaleExtension.isSingletonChar(var5.value())) {
               if (this._uattributes != null) {
                  this._uattributes.clear();
               }

               if (this._ukeywords != null) {
                  this._ukeywords.clear();
               }
            } else if (this._extensions != null && this._extensions.containsKey(var5)) {
               this._extensions.remove(var5);
            }
         } else {
            String var6 = var2.replaceAll("_", "-");
            StringTokenIterator var7 = new StringTokenIterator(var6, "-");

            while(!var7.isDone()) {
               String var8 = var7.current();
               boolean var9;
               if (var3) {
                  var9 = LanguageTag.isPrivateuseSubtag(var8);
               } else {
                  var9 = LanguageTag.isExtensionSubtag(var8);
               }

               if (!var9) {
                  throw new LocaleSyntaxException("Ill-formed extension value: " + var8, var7.currentStart());
               }

               var7.next();
            }

            if (UnicodeLocaleExtension.isSingletonChar(var5.value())) {
               this.setUnicodeLocaleExtension(var6);
            } else {
               if (this._extensions == null) {
                  this._extensions = new HashMap(4);
               }

               this._extensions.put(var5, var6);
            }
         }

         return this;
      }
   }

   public InternalLocaleBuilder setExtensions(String var1) throws LocaleSyntaxException {
      if (var1 != null && var1.length() != 0) {
         var1 = var1.replaceAll("_", "-");
         StringTokenIterator var2 = new StringTokenIterator(var1, "-");
         ArrayList var3 = null;
         String var4 = null;

         int var5;
         int var6;
         String var7;
         StringBuilder var9;
         for(var5 = 0; !var2.isDone(); var3.add(var9.toString())) {
            var7 = var2.current();
            if (!LanguageTag.isExtensionSingleton(var7)) {
               break;
            }

            var6 = var2.currentStart();
            var9 = new StringBuilder(var7);
            var2.next();

            while(!var2.isDone()) {
               var7 = var2.current();
               if (!LanguageTag.isExtensionSubtag(var7)) {
                  break;
               }

               var9.append("-").append(var7);
               var5 = var2.currentEnd();
               var2.next();
            }

            if (var5 < var6) {
               throw new LocaleSyntaxException("Incomplete extension '" + var7 + "'", var6);
            }

            if (var3 == null) {
               var3 = new ArrayList(4);
            }
         }

         if (!var2.isDone()) {
            var7 = var2.current();
            if (LanguageTag.isPrivateusePrefix(var7)) {
               var6 = var2.currentStart();
               StringBuilder var8 = new StringBuilder(var7);
               var2.next();

               while(!var2.isDone()) {
                  var7 = var2.current();
                  if (!LanguageTag.isPrivateuseSubtag(var7)) {
                     break;
                  }

                  var8.append("-").append(var7);
                  var5 = var2.currentEnd();
                  var2.next();
               }

               if (var5 <= var6) {
                  throw new LocaleSyntaxException("Incomplete privateuse:" + var1.substring(var6), var6);
               }

               var4 = var8.toString();
            }
         }

         if (!var2.isDone()) {
            throw new LocaleSyntaxException("Ill-formed extension subtags:" + var1.substring(var2.currentStart()), var2.currentStart());
         } else {
            return this.setExtensions(var3, var4);
         }
      } else {
         this.clearExtensions();
         return this;
      }
   }

   private InternalLocaleBuilder setExtensions(List var1, String var2) {
      this.clearExtensions();
      if (var1 != null && var1.size() > 0) {
         HashSet var3 = new HashSet(var1.size());
         Iterator var4 = var1.iterator();

         while(var4.hasNext()) {
            String var5 = (String)var4.next();
            InternalLocaleBuilder.CaseInsensitiveChar var6 = new InternalLocaleBuilder.CaseInsensitiveChar(var5.charAt(0));
            if (!var3.contains(var6)) {
               if (UnicodeLocaleExtension.isSingletonChar(var6.value())) {
                  this.setUnicodeLocaleExtension(var5.substring(2));
               } else {
                  if (this._extensions == null) {
                     this._extensions = new HashMap(4);
                  }

                  this._extensions.put(var6, var5.substring(2));
               }
            }
         }
      }

      if (var2 != null && var2.length() > 0) {
         if (this._extensions == null) {
            this._extensions = new HashMap(1);
         }

         this._extensions.put(new InternalLocaleBuilder.CaseInsensitiveChar(var2.charAt(0)), var2.substring(2));
      }

      return this;
   }

   public InternalLocaleBuilder setLanguageTag(LanguageTag var1) {
      this.clear();
      if (var1.getExtlangs().size() > 0) {
         this._language = (String)var1.getExtlangs().get(0);
      } else {
         String var2 = var1.getLanguage();
         if (!var2.equals(LanguageTag.UNDETERMINED)) {
            this._language = var2;
         }
      }

      this._script = var1.getScript();
      this._region = var1.getRegion();
      List var5 = var1.getVariants();
      if (var5.size() > 0) {
         StringBuilder var3 = new StringBuilder((String)var5.get(0));

         for(int var4 = 1; var4 < var5.size(); ++var4) {
            var3.append("_").append((String)var5.get(var4));
         }

         this._variant = var3.toString();
      }

      this.setExtensions(var1.getExtensions(), var1.getPrivateuse());
      return this;
   }

   public InternalLocaleBuilder setLocale(BaseLocale var1, LocaleExtensions var2) throws LocaleSyntaxException {
      String var3 = var1.getLanguage();
      String var4 = var1.getScript();
      String var5 = var1.getRegion();
      String var6 = var1.getVariant();
      if (var3.length() > 0 && !LanguageTag.isLanguage(var3)) {
         throw new LocaleSyntaxException("Ill-formed language: " + var3);
      } else if (var4.length() > 0 && !LanguageTag.isScript(var4)) {
         throw new LocaleSyntaxException("Ill-formed script: " + var4);
      } else if (var5.length() > 0 && !LanguageTag.isRegion(var5)) {
         throw new LocaleSyntaxException("Ill-formed region: " + var5);
      } else {
         if (var6.length() > 0) {
            int var7 = this.checkVariants(var6, "_");
            if (var7 != -1) {
               throw new LocaleSyntaxException("Ill-formed variant: " + var6, var7);
            }
         }

         this._language = var3;
         this._script = var4;
         this._region = var5;
         this._variant = var6;
         this.clearExtensions();
         Set var14 = var2 == null ? null : var2.getKeys();
         if (var14 != null) {
            Iterator var8 = var14.iterator();

            while(true) {
               while(var8.hasNext()) {
                  Character var9 = (Character)var8.next();
                  Extension var10 = var2.getExtension(var9);
                  if (var10 instanceof UnicodeLocaleExtension) {
                     UnicodeLocaleExtension var11 = (UnicodeLocaleExtension)var10;

                     Iterator var12;
                     String var13;
                     for(var12 = var11.getUnicodeLocaleAttributes().iterator(); var12.hasNext(); this._uattributes.add(new InternalLocaleBuilder.CaseInsensitiveString(var13))) {
                        var13 = (String)var12.next();
                        if (this._uattributes == null) {
                           this._uattributes = new HashSet(4);
                        }
                     }

                     for(var12 = var11.getUnicodeLocaleKeys().iterator(); var12.hasNext(); this._ukeywords.put(new InternalLocaleBuilder.CaseInsensitiveString(var13), var11.getUnicodeLocaleType(var13))) {
                        var13 = (String)var12.next();
                        if (this._ukeywords == null) {
                           this._ukeywords = new HashMap(4);
                        }
                     }
                  } else {
                     if (this._extensions == null) {
                        this._extensions = new HashMap(4);
                     }

                     this._extensions.put(new InternalLocaleBuilder.CaseInsensitiveChar(var9), var10.getValue());
                  }
               }

               return this;
            }
         } else {
            return this;
         }
      }
   }

   public InternalLocaleBuilder clear() {
      this._language = "";
      this._script = "";
      this._region = "";
      this._variant = "";
      this.clearExtensions();
      return this;
   }

   public InternalLocaleBuilder clearExtensions() {
      if (this._extensions != null) {
         this._extensions.clear();
      }

      if (this._uattributes != null) {
         this._uattributes.clear();
      }

      if (this._ukeywords != null) {
         this._ukeywords.clear();
      }

      return this;
   }

   public BaseLocale getBaseLocale() {
      String var1 = this._language;
      String var2 = this._script;
      String var3 = this._region;
      String var4 = this._variant;
      if (this._extensions != null) {
         String var5 = (String)this._extensions.get(PRIVUSE_KEY);
         if (var5 != null) {
            StringTokenIterator var6 = new StringTokenIterator(var5, "-");
            boolean var7 = false;

            int var8;
            for(var8 = -1; !var6.isDone(); var6.next()) {
               if (var7) {
                  var8 = var6.currentStart();
                  break;
               }

               if (AsciiUtil.caseIgnoreMatch(var6.current(), "lvariant")) {
                  var7 = true;
               }
            }

            if (var8 != -1) {
               StringBuilder var9 = new StringBuilder(var4);
               if (var9.length() != 0) {
                  var9.append("_");
               }

               var9.append(var5.substring(var8).replaceAll("-", "_"));
               var4 = var9.toString();
            }
         }
      }

      return BaseLocale.getInstance(var1, var2, var3, var4);
   }

   public LocaleExtensions getLocaleExtensions() {
      return this._extensions != null && this._extensions.size() != 0 || this._uattributes != null && this._uattributes.size() != 0 || this._ukeywords != null && this._ukeywords.size() != 0 ? new LocaleExtensions(this._extensions, this._uattributes, this._ukeywords) : LocaleExtensions.EMPTY_EXTENSIONS;
   }

   static String removePrivateuseVariant(String var0) {
      StringTokenIterator var1 = new StringTokenIterator(var0, "-");
      int var2 = -1;

      boolean var3;
      for(var3 = false; !var1.isDone(); var1.next()) {
         if (var2 != -1) {
            var3 = true;
            break;
         }

         if (AsciiUtil.caseIgnoreMatch(var1.current(), "lvariant")) {
            var2 = var1.currentStart();
         }
      }

      if (!var3) {
         return var0;
      } else if (!$assertionsDisabled && var2 != 0 && var2 <= 1) {
         throw new AssertionError();
      } else {
         return var2 == 0 ? null : var0.substring(0, var2 - 1);
      }
   }

   private int checkVariants(String var1, String var2) {
      StringTokenIterator var3 = new StringTokenIterator(var1, var2);

      while(!var3.isDone()) {
         String var4 = var3.current();
         if (!LanguageTag.isVariant(var4)) {
            return var3.currentStart();
         }

         var3.next();
      }

      return -1;
   }

   private void setUnicodeLocaleExtension(String var1) {
      if (this._uattributes != null) {
         this._uattributes.clear();
      }

      if (this._ukeywords != null) {
         this._ukeywords.clear();
      }

      StringTokenIterator var2 = new StringTokenIterator(var1, "-");

      while(!var2.isDone() && UnicodeLocaleExtension.isAttribute(var2.current())) {
         if (this._uattributes == null) {
            this._uattributes = new HashSet(4);
         }

         this._uattributes.add(new InternalLocaleBuilder.CaseInsensitiveString(var2.current()));
         var2.next();
      }

      InternalLocaleBuilder.CaseInsensitiveString var3 = null;
      int var5 = -1;
      int var6 = -1;

      while(!var2.isDone()) {
         String var4;
         if (var3 != null) {
            if (UnicodeLocaleExtension.isKey(var2.current())) {
               if (!$assertionsDisabled && var5 != -1 && var6 == -1) {
                  throw new AssertionError();
               }

               var4 = var5 == -1 ? "" : var1.substring(var5, var6);
               if (this._ukeywords == null) {
                  this._ukeywords = new HashMap(4);
               }

               this._ukeywords.put(var3, var4);
               InternalLocaleBuilder.CaseInsensitiveString var7 = new InternalLocaleBuilder.CaseInsensitiveString(var2.current());
               var3 = this._ukeywords.containsKey(var7) ? null : var7;
               var6 = -1;
               var5 = -1;
            } else {
               if (var5 == -1) {
                  var5 = var2.currentStart();
               }

               var6 = var2.currentEnd();
            }
         } else if (UnicodeLocaleExtension.isKey(var2.current())) {
            var3 = new InternalLocaleBuilder.CaseInsensitiveString(var2.current());
            if (this._ukeywords != null && this._ukeywords.containsKey(var3)) {
               var3 = null;
            }
         }

         if (!var2.hasNext()) {
            if (var3 != null) {
               if (!$assertionsDisabled && var5 != -1 && var6 == -1) {
                  throw new AssertionError();
               }

               var4 = var5 == -1 ? "" : var1.substring(var5, var6);
               if (this._ukeywords == null) {
                  this._ukeywords = new HashMap(4);
               }

               this._ukeywords.put(var3, var4);
            }
            break;
         }

         var2.next();
      }

   }

   static class CaseInsensitiveChar {
      private char _c;

      CaseInsensitiveChar(char var1) {
         this._c = var1;
      }

      public char value() {
         return this._c;
      }

      public int hashCode() {
         return AsciiUtil.toLower(this._c);
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!(var1 instanceof InternalLocaleBuilder.CaseInsensitiveChar)) {
            return false;
         } else {
            return this._c == AsciiUtil.toLower(((InternalLocaleBuilder.CaseInsensitiveChar)var1).value());
         }
      }
   }

   static class CaseInsensitiveString {
      private String _s;

      CaseInsensitiveString(String var1) {
         this._s = var1;
      }

      public String value() {
         return this._s;
      }

      public int hashCode() {
         return AsciiUtil.toLowerString(this._s).hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else {
            return !(var1 instanceof InternalLocaleBuilder.CaseInsensitiveString) ? false : AsciiUtil.caseIgnoreMatch(this._s, ((InternalLocaleBuilder.CaseInsensitiveString)var1).value());
         }
      }
   }
}
