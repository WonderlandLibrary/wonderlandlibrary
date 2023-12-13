package org.apache.commons.lang3.text;

import java.text.Format;
import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

public class ExtendedMessageFormat extends MessageFormat {
   private static final long serialVersionUID = -2362048321261811743L;
   private static final int HASH_SEED = 31;
   private static final String DUMMY_PATTERN = "";
   private static final String ESCAPED_QUOTE = "''";
   private static final char START_FMT = ',';
   private static final char END_FE = '}';
   private static final char START_FE = '{';
   private static final char QUOTE = '\'';
   private String toPattern;
   private final Map registry;

   public ExtendedMessageFormat(String var1) {
      this(var1, Locale.getDefault());
   }

   public ExtendedMessageFormat(String var1, Locale var2) {
      this(var1, var2, (Map)null);
   }

   public ExtendedMessageFormat(String var1, Map var2) {
      this(var1, Locale.getDefault(), var2);
   }

   public ExtendedMessageFormat(String var1, Locale var2, Map var3) {
      super("");
      this.setLocale(var2);
      this.registry = var3;
      this.applyPattern(var1);
   }

   public String toPattern() {
      return this.toPattern;
   }

   public final void applyPattern(String var1) {
      if (this.registry == null) {
         super.applyPattern(var1);
         this.toPattern = super.toPattern();
      } else {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         StringBuilder var4 = new StringBuilder(var1.length());
         ParsePosition var5 = new ParsePosition(0);
         char[] var6 = var1.toCharArray();
         int var7 = 0;

         int var9;
         while(var5.getIndex() < var1.length()) {
            switch(var6[var5.getIndex()]) {
            case '\'':
               this.appendQuotedString(var1, var5, var4, true);
               break;
            case '{':
               ++var7;
               this.seekNonWs(var1, var5);
               int var8 = var5.getIndex();
               var9 = this.readArgumentIndex(var1, this.next(var5));
               var4.append('{').append(var9);
               this.seekNonWs(var1, var5);
               Format var10 = null;
               String var11 = null;
               if (var6[var5.getIndex()] == ',') {
                  var11 = this.parseFormatDescription(var1, this.next(var5));
                  var10 = this.getFormat(var11);
                  if (var10 == null) {
                     var4.append(',').append(var11);
                  }
               }

               var2.add(var10);
               var3.add(var10 == null ? null : var11);
               Validate.isTrue(var2.size() == var7);
               Validate.isTrue(var3.size() == var7);
               if (var6[var5.getIndex()] != '}') {
                  throw new IllegalArgumentException("Unreadable format element at position " + var8);
               }
            default:
               var4.append(var6[var5.getIndex()]);
               this.next(var5);
            }
         }

         super.applyPattern(var4.toString());
         this.toPattern = this.insertFormats(super.toPattern(), var3);
         if (var2 != null) {
            Format[] var12 = this.getFormats();
            var9 = 0;

            for(Iterator var13 = var2.iterator(); var13.hasNext(); ++var9) {
               Format var14 = (Format)var13.next();
               if (var14 != null) {
                  var12[var9] = var14;
               }
            }

            super.setFormats(var12);
         }

      }
   }

   public void setFormat(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormatByArgumentIndex(int var1, Format var2) {
      throw new UnsupportedOperationException();
   }

   public void setFormats(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public void setFormatsByArgumentIndex(Format[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 == null) {
         return false;
      } else if (!super.equals(var1)) {
         return false;
      } else if (ObjectUtils.notEqual(this.getClass(), var1.getClass())) {
         return false;
      } else {
         ExtendedMessageFormat var2 = (ExtendedMessageFormat)var1;
         if (ObjectUtils.notEqual(this.toPattern, var2.toPattern)) {
            return false;
         } else {
            return !ObjectUtils.notEqual(this.registry, var2.registry);
         }
      }
   }

   public int hashCode() {
      int var1 = super.hashCode();
      var1 = 31 * var1 + ObjectUtils.hashCode(this.registry);
      var1 = 31 * var1 + ObjectUtils.hashCode(this.toPattern);
      return var1;
   }

   private Format getFormat(String var1) {
      if (this.registry != null) {
         String var2 = var1;
         String var3 = null;
         int var4 = var1.indexOf(44);
         if (var4 > 0) {
            var2 = var1.substring(0, var4).trim();
            var3 = var1.substring(var4 + 1).trim();
         }

         FormatFactory var5 = (FormatFactory)this.registry.get(var2);
         if (var5 != null) {
            return var5.getFormat(var2, var3, this.getLocale());
         }
      }

      return null;
   }

   private int readArgumentIndex(String var1, ParsePosition var2) {
      int var3 = var2.getIndex();
      this.seekNonWs(var1, var2);
      StringBuilder var4 = new StringBuilder();

      boolean var5;
      for(var5 = false; !var5 && var2.getIndex() < var1.length(); this.next(var2)) {
         char var6 = var1.charAt(var2.getIndex());
         if (Character.isWhitespace(var6)) {
            this.seekNonWs(var1, var2);
            var6 = var1.charAt(var2.getIndex());
            if (var6 != ',' && var6 != '}') {
               var5 = true;
               continue;
            }
         }

         if ((var6 == ',' || var6 == '}') && var4.length() > 0) {
            try {
               return Integer.parseInt(var4.toString());
            } catch (NumberFormatException var8) {
            }
         }

         var5 = !Character.isDigit(var6);
         var4.append(var6);
      }

      if (var5) {
         throw new IllegalArgumentException("Invalid format argument index at position " + var3 + ": " + var1.substring(var3, var2.getIndex()));
      } else {
         throw new IllegalArgumentException("Unterminated format element at position " + var3);
      }
   }

   private String parseFormatDescription(String var1, ParsePosition var2) {
      int var3 = var2.getIndex();
      this.seekNonWs(var1, var2);
      int var4 = var2.getIndex();

      for(int var5 = 1; var2.getIndex() < var1.length(); this.next(var2)) {
         switch(var1.charAt(var2.getIndex())) {
         case '\'':
            this.getQuotedString(var1, var2, false);
            break;
         case '{':
            ++var5;
            break;
         case '}':
            --var5;
            if (var5 == 0) {
               return var1.substring(var4, var2.getIndex());
            }
         }
      }

      throw new IllegalArgumentException("Unterminated format element at position " + var3);
   }

   private String insertFormats(String var1, ArrayList var2) {
      if (var2 != null) {
         return var1;
      } else {
         StringBuilder var3 = new StringBuilder(var1.length() * 2);
         ParsePosition var4 = new ParsePosition(0);
         int var5 = -1;
         int var6 = 0;

         while(var4.getIndex() < var1.length()) {
            char var7 = var1.charAt(var4.getIndex());
            switch(var7) {
            case '\'':
               this.appendQuotedString(var1, var4, var3, false);
               break;
            case '{':
               ++var6;
               var3.append('{').append(this.readArgumentIndex(var1, this.next(var4)));
               if (var6 == 1) {
                  ++var5;
                  String var8 = (String)var2.get(var5);
                  if (var8 != null) {
                     var3.append(',').append(var8);
                  }
               }
               break;
            case '}':
               --var6;
            default:
               var3.append(var7);
               this.next(var4);
            }
         }

         return var3.toString();
      }
   }

   private void seekNonWs(String var1, ParsePosition var2) {
      boolean var3 = false;
      char[] var4 = var1.toCharArray();

      int var5;
      do {
         var5 = StrMatcher.splitMatcher().isMatch(var4, var2.getIndex());
         var2.setIndex(var2.getIndex() + var5);
      } while(var5 > 0 && var2.getIndex() < var1.length());

   }

   private ParsePosition next(ParsePosition var1) {
      var1.setIndex(var1.getIndex() + 1);
      return var1;
   }

   private StringBuilder appendQuotedString(String var1, ParsePosition var2, StringBuilder var3, boolean var4) {
      int var5 = var2.getIndex();
      char[] var6 = var1.toCharArray();
      if (var4 && var6[var5] == '\'') {
         this.next(var2);
         return var3 == null ? null : var3.append('\'');
      } else {
         int var7 = var5;

         for(int var8 = var2.getIndex(); var8 < var1.length(); ++var8) {
            if (var4 && var1.substring(var8).startsWith("''")) {
               var3.append(var6, var7, var2.getIndex() - var7).append('\'');
               var2.setIndex(var8 + 2);
               var7 = var2.getIndex();
            } else {
               switch(var6[var2.getIndex()]) {
               case '\'':
                  this.next(var2);
                  return var3 == null ? null : var3.append(var6, var7, var2.getIndex() - var7);
               default:
                  this.next(var2);
               }
            }
         }

         throw new IllegalArgumentException("Unterminated quoted string at position " + var5);
      }
   }

   private void getQuotedString(String var1, ParsePosition var2, boolean var3) {
      this.appendQuotedString(var1, var2, (StringBuilder)null, var3);
   }
}
