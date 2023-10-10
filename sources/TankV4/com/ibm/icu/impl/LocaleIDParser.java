package com.ibm.icu.impl;

import com.ibm.icu.impl.locale.AsciiUtil;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

public final class LocaleIDParser {
   private char[] id;
   private int index;
   private StringBuilder buffer;
   private boolean canonicalize;
   private boolean hadCountry;
   Map keywords;
   String baseName;
   private static final char KEYWORD_SEPARATOR = '@';
   private static final char HYPHEN = '-';
   private static final char KEYWORD_ASSIGN = '=';
   private static final char COMMA = ',';
   private static final char ITEM_SEPARATOR = ';';
   private static final char DOT = '.';
   private static final char UNDERSCORE = '_';
   private static final char DONE = '\uffff';

   public LocaleIDParser(String var1) {
      this(var1, false);
   }

   public LocaleIDParser(String var1, boolean var2) {
      this.id = var1.toCharArray();
      this.index = 0;
      this.buffer = new StringBuilder(this.id.length + 5);
      this.canonicalize = var2;
   }

   private void reset() {
      this.index = 0;
      this.buffer = new StringBuilder(this.id.length + 5);
   }

   private void append(char var1) {
      this.buffer.append(var1);
   }

   private void addSeparator() {
      this.append('_');
   }

   private String getString(int var1) {
      return this.buffer.substring(var1);
   }

   private void set(int var1, String var2) {
      this.buffer.delete(var1, this.buffer.length());
      this.buffer.insert(var1, var2);
   }

   private void append(String var1) {
      this.buffer.append(var1);
   }

   private char next() {
      if (this.index == this.id.length) {
         ++this.index;
         return '\uffff';
      } else {
         return this.id[this.index++];
      }
   }

   private void skipUntilTerminatorOrIDSeparator() {
      while(this != this.next()) {
      }

      --this.index;
   }

   private int parseLanguage() {
      // $FF: Couldn't be decompiled
   }

   private void skipLanguage() {
      // $FF: Couldn't be decompiled
   }

   private int parseScript() {
      // $FF: Couldn't be decompiled
   }

   private void skipScript() {
      // $FF: Couldn't be decompiled
   }

   private int parseCountry() {
      // $FF: Couldn't be decompiled
   }

   private void skipCountry() {
      // $FF: Couldn't be decompiled
   }

   private int parseVariant() {
      // $FF: Couldn't be decompiled
   }

   public String getLanguage() {
      this.reset();
      return this.getString(this.parseLanguage());
   }

   public String getScript() {
      this.reset();
      this.skipLanguage();
      return this.getString(this.parseScript());
   }

   public String getCountry() {
      this.reset();
      this.skipLanguage();
      this.skipScript();
      return this.getString(this.parseCountry());
   }

   public String getVariant() {
      this.reset();
      this.skipLanguage();
      this.skipScript();
      this.skipCountry();
      return this.getString(this.parseVariant());
   }

   public String[] getLanguageScriptCountryVariant() {
      this.reset();
      return new String[]{this.getString(this.parseLanguage()), this.getString(this.parseScript()), this.getString(this.parseCountry()), this.getString(this.parseVariant())};
   }

   public void setBaseName(String var1) {
      this.baseName = var1;
   }

   public void parseBaseName() {
      if (this.baseName != null) {
         this.set(0, this.baseName);
      } else {
         this.reset();
         this.parseLanguage();
         this.parseScript();
         this.parseCountry();
         this.parseVariant();
         int var1 = this.buffer.length();
         if (var1 > 0 && this.buffer.charAt(var1 - 1) == '_') {
            this.buffer.deleteCharAt(var1 - 1);
         }
      }

   }

   public String getBaseName() {
      if (this.baseName != null) {
         return this.baseName;
      } else {
         this.parseBaseName();
         return this.getString(0);
      }
   }

   public String getName() {
      this.parseBaseName();
      this.parseKeywords();
      return this.getString(0);
   }

   private String getKeyword() {
      // $FF: Couldn't be decompiled
   }

   private String getValue() {
      // $FF: Couldn't be decompiled
   }

   private Comparator getKeyComparator() {
      Comparator var1 = new Comparator(this) {
         final LocaleIDParser this$0;

         {
            this.this$0 = var1;
         }

         public int compare(String var1, String var2) {
            return var1.compareTo(var2);
         }

         public int compare(Object var1, Object var2) {
            return this.compare((String)var1, (String)var2);
         }
      };
      return var1;
   }

   public Map getKeywordMap() {
      // $FF: Couldn't be decompiled
   }

   private int parseKeywords() {
      int var1 = this.buffer.length();
      Map var2 = this.getKeywordMap();
      if (!var2.isEmpty()) {
         boolean var3 = true;
         Iterator var4 = var2.entrySet().iterator();

         while(var4.hasNext()) {
            Entry var5 = (Entry)var4.next();
            this.append((char)(var3 ? '@' : ';'));
            var3 = false;
            this.append((String)var5.getKey());
            this.append('=');
            this.append((String)var5.getValue());
         }

         if (!var3) {
            ++var1;
         }
      }

      return var1;
   }

   public Iterator getKeywords() {
      Map var1 = this.getKeywordMap();
      return var1.isEmpty() ? null : var1.keySet().iterator();
   }

   public String getKeywordValue(String var1) {
      Map var2 = this.getKeywordMap();
      return var2.isEmpty() ? null : (String)var2.get(AsciiUtil.toLowerString(var1.trim()));
   }

   public void defaultKeywordValue(String var1, String var2) {
      this.setKeywordValue(var1, var2, false);
   }

   public void setKeywordValue(String var1, String var2) {
      this.setKeywordValue(var1, var2, true);
   }

   private void setKeywordValue(String var1, String var2, boolean var3) {
      if (var1 == null) {
         if (var3) {
            this.keywords = Collections.emptyMap();
         }
      } else {
         var1 = AsciiUtil.toLowerString(var1.trim());
         if (var1.length() == 0) {
            throw new IllegalArgumentException("keyword must not be empty");
         }

         if (var2 != null) {
            var2 = var2.trim();
            if (var2.length() == 0) {
               throw new IllegalArgumentException("value must not be empty");
            }
         }

         Map var4 = this.getKeywordMap();
         if (var4.isEmpty()) {
            if (var2 != null) {
               this.keywords = new TreeMap(this.getKeyComparator());
               this.keywords.put(var1, var2.trim());
            }
         } else if (var3 || !var4.containsKey(var1)) {
            if (var2 != null) {
               var4.put(var1, var2);
            } else {
               var4.remove(var1);
               if (var4.isEmpty()) {
                  this.keywords = Collections.emptyMap();
               }
            }
         }
      }

   }
}
