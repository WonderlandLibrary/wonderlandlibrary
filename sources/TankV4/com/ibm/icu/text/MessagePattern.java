package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.util.Freezable;
import java.util.ArrayList;
import java.util.Locale;

public final class MessagePattern implements Cloneable, Freezable {
   public static final int ARG_NAME_NOT_NUMBER = -1;
   public static final int ARG_NAME_NOT_VALID = -2;
   public static final double NO_NUMERIC_VALUE = -1.23456789E8D;
   private static final int MAX_PREFIX_LENGTH = 24;
   private MessagePattern.ApostropheMode aposMode;
   private String msg;
   private ArrayList parts = new ArrayList();
   private ArrayList numericValues;
   private boolean hasArgNames;
   private boolean hasArgNumbers;
   private boolean needsAutoQuoting;
   private boolean frozen;
   private static final MessagePattern.ApostropheMode defaultAposMode = MessagePattern.ApostropheMode.valueOf(ICUConfig.get("com.ibm.icu.text.MessagePattern.ApostropheMode", "DOUBLE_OPTIONAL"));
   private static final MessagePattern.ArgType[] argTypes = MessagePattern.ArgType.values();
   static final boolean $assertionsDisabled = !MessagePattern.class.desiredAssertionStatus();

   public MessagePattern() {
      this.aposMode = defaultAposMode;
   }

   public MessagePattern(MessagePattern.ApostropheMode var1) {
      this.aposMode = var1;
   }

   public MessagePattern(String var1) {
      this.aposMode = defaultAposMode;
      this.parse(var1);
   }

   public MessagePattern parse(String var1) {
      this.preParse(var1);
      this.parseMessage(0, 0, 0, MessagePattern.ArgType.NONE);
      this.postParse();
      return this;
   }

   public MessagePattern parseChoiceStyle(String var1) {
      this.preParse(var1);
      this.parseChoiceStyle(0, 0);
      this.postParse();
      return this;
   }

   public MessagePattern parsePluralStyle(String var1) {
      this.preParse(var1);
      this.parsePluralOrSelectStyle(MessagePattern.ArgType.PLURAL, 0, 0);
      this.postParse();
      return this;
   }

   public MessagePattern parseSelectStyle(String var1) {
      this.preParse(var1);
      this.parsePluralOrSelectStyle(MessagePattern.ArgType.SELECT, 0, 0);
      this.postParse();
      return this;
   }

   public void clear() {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to clear() a frozen MessagePattern instance.");
      } else {
         this.msg = null;
         this.hasArgNames = this.hasArgNumbers = false;
         this.needsAutoQuoting = false;
         this.parts.clear();
         if (this.numericValues != null) {
            this.numericValues.clear();
         }

      }
   }

   public void clearPatternAndSetApostropheMode(MessagePattern.ApostropheMode var1) {
      this.clear();
      this.aposMode = var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MessagePattern var2 = (MessagePattern)var1;
         boolean var10000;
         if (this.aposMode.equals(var2.aposMode)) {
            label25: {
               if (this.msg == null) {
                  if (var2.msg != null) {
                     break label25;
                  }
               } else if (!this.msg.equals(var2.msg)) {
                  break label25;
               }

               if (this.parts.equals(var2.parts)) {
                  var10000 = true;
                  return var10000;
               }
            }
         }

         var10000 = false;
         return var10000;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return (this.aposMode.hashCode() * 37 + (this.msg != null ? this.msg.hashCode() : 0)) * 37 + this.parts.hashCode();
   }

   public MessagePattern.ApostropheMode getApostropheMode() {
      return this.aposMode;
   }

   boolean jdkAposMode() {
      return this.aposMode == MessagePattern.ApostropheMode.DOUBLE_REQUIRED;
   }

   public String getPatternString() {
      return this.msg;
   }

   public boolean hasNamedArguments() {
      return this.hasArgNames;
   }

   public boolean hasNumberedArguments() {
      return this.hasArgNumbers;
   }

   public String toString() {
      return this.msg;
   }

   public static int validateArgumentName(String var0) {
      return !PatternProps.isIdentifier(var0) ? -2 : parseArgNumber(var0, 0, var0.length());
   }

   public String autoQuoteApostropheDeep() {
      if (!this.needsAutoQuoting) {
         return this.msg;
      } else {
         StringBuilder var1 = null;
         int var2 = this.countParts();
         int var3 = var2;

         while(var3 > 0) {
            --var3;
            MessagePattern.Part var4;
            if ((var4 = this.getPart(var3)).getType() == MessagePattern.Part.Type.INSERT_CHAR) {
               if (var1 == null) {
                  var1 = (new StringBuilder(this.msg.length() + 10)).append(this.msg);
               }

               var1.insert(MessagePattern.Part.access$000(var4), (char)MessagePattern.Part.access$100(var4));
            }
         }

         if (var1 == null) {
            return this.msg;
         } else {
            return var1.toString();
         }
      }
   }

   public int countParts() {
      return this.parts.size();
   }

   public MessagePattern.Part getPart(int var1) {
      return (MessagePattern.Part)this.parts.get(var1);
   }

   public MessagePattern.Part.Type getPartType(int var1) {
      return MessagePattern.Part.access$200((MessagePattern.Part)this.parts.get(var1));
   }

   public int getPatternIndex(int var1) {
      return MessagePattern.Part.access$000((MessagePattern.Part)this.parts.get(var1));
   }

   public String getSubstring(MessagePattern.Part var1) {
      int var2 = MessagePattern.Part.access$000(var1);
      return this.msg.substring(var2, var2 + MessagePattern.Part.access$300(var1));
   }

   public boolean partSubstringMatches(MessagePattern.Part var1, String var2) {
      return this.msg.regionMatches(MessagePattern.Part.access$000(var1), var2, 0, MessagePattern.Part.access$300(var1));
   }

   public double getNumericValue(MessagePattern.Part var1) {
      MessagePattern.Part.Type var2 = MessagePattern.Part.access$200(var1);
      if (var2 == MessagePattern.Part.Type.ARG_INT) {
         return (double)MessagePattern.Part.access$100(var1);
      } else {
         return var2 == MessagePattern.Part.Type.ARG_DOUBLE ? (Double)this.numericValues.get(MessagePattern.Part.access$100(var1)) : -1.23456789E8D;
      }
   }

   public double getPluralOffset(int var1) {
      MessagePattern.Part var2 = (MessagePattern.Part)this.parts.get(var1);
      return MessagePattern.Part.access$200(var2).hasNumericValue() ? this.getNumericValue(var2) : 0.0D;
   }

   public int getLimitPartIndex(int var1) {
      int var2 = MessagePattern.Part.access$400((MessagePattern.Part)this.parts.get(var1));
      return var2 < var1 ? var1 : var2;
   }

   public Object clone() {
      return this.isFrozen() ? this : this.cloneAsThawed();
   }

   public MessagePattern cloneAsThawed() {
      MessagePattern var1;
      try {
         var1 = (MessagePattern)super.clone();
      } catch (CloneNotSupportedException var3) {
         throw new RuntimeException(var3);
      }

      var1.parts = (ArrayList)this.parts.clone();
      if (this.numericValues != null) {
         var1.numericValues = (ArrayList)this.numericValues.clone();
      }

      var1.frozen = false;
      return var1;
   }

   public MessagePattern freeze() {
      this.frozen = true;
      return this;
   }

   public boolean isFrozen() {
      return this.frozen;
   }

   private void preParse(String var1) {
      if (this.isFrozen()) {
         throw new UnsupportedOperationException("Attempt to parse(" + prefix(var1) + ") on frozen MessagePattern instance.");
      } else {
         this.msg = var1;
         this.hasArgNames = this.hasArgNumbers = false;
         this.needsAutoQuoting = false;
         this.parts.clear();
         if (this.numericValues != null) {
            this.numericValues.clear();
         }

      }
   }

   private void postParse() {
   }

   private int parseMessage(int var1, int var2, int var3, MessagePattern.ArgType var4) {
      if (var3 > 32767) {
         throw new IndexOutOfBoundsException();
      } else {
         int var5 = this.parts.size();
         this.addPart(MessagePattern.Part.Type.MSG_START, var1, var2, var3);
         var1 += var2;

         char var6;
         label118:
         do {
            while(true) {
               while(true) {
                  while(var1 < this.msg.length()) {
                     var6 = this.msg.charAt(var1++);
                     if (var6 != '\'') {
                        if (!var4.hasPluralStyle() || var6 != '#') {
                           if (var6 != '{') {
                              continue label118;
                           }

                           var1 = this.parseArg(var1 - 1, 1, var3);
                        } else {
                           this.addPart(MessagePattern.Part.Type.REPLACE_NUMBER, var1 - 1, 1, 0);
                        }
                     } else if (var1 == this.msg.length()) {
                        this.addPart(MessagePattern.Part.Type.INSERT_CHAR, var1, 0, 39);
                        this.needsAutoQuoting = true;
                     } else {
                        var6 = this.msg.charAt(var1);
                        if (var6 == '\'') {
                           this.addPart(MessagePattern.Part.Type.SKIP_SYNTAX, var1++, 1, 0);
                        } else if (this.aposMode == MessagePattern.ApostropheMode.DOUBLE_REQUIRED || var6 == '{' || var6 == '}' || var4 == MessagePattern.ArgType.CHOICE && var6 == '|' || var4.hasPluralStyle() && var6 == '#') {
                           this.addPart(MessagePattern.Part.Type.SKIP_SYNTAX, var1 - 1, 1, 0);

                           while(true) {
                              var1 = this.msg.indexOf(39, var1 + 1);
                              if (var1 < 0) {
                                 var1 = this.msg.length();
                                 this.addPart(MessagePattern.Part.Type.INSERT_CHAR, var1, 0, 39);
                                 this.needsAutoQuoting = true;
                                 break;
                              }

                              if (var1 + 1 >= this.msg.length() || this.msg.charAt(var1 + 1) != '\'') {
                                 this.addPart(MessagePattern.Part.Type.SKIP_SYNTAX, var1++, 1, 0);
                                 break;
                              }

                              ++var1;
                              this.addPart(MessagePattern.Part.Type.SKIP_SYNTAX, var1, 1, 0);
                           }
                        } else {
                           this.addPart(MessagePattern.Part.Type.INSERT_CHAR, var1, 0, 39);
                           this.needsAutoQuoting = true;
                        }
                     }
                  }

                  if (var3 > 0 && var3 == var4) {
                     throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
                  }

                  this.addLimitPart(var5, MessagePattern.Part.Type.MSG_LIMIT, var1, 0, var3);
                  return var1;
               }
            }
         } while((var3 <= 0 || var6 != '}') && (var4 != MessagePattern.ArgType.CHOICE || var6 != '|'));

         int var7 = var4 == MessagePattern.ArgType.CHOICE && var6 == '}' ? 0 : 1;
         this.addLimitPart(var5, MessagePattern.Part.Type.MSG_LIMIT, var1 - 1, var7, var3);
         return var4 == MessagePattern.ArgType.CHOICE ? var1 - 1 : var1;
      }
   }

   private int parseArg(int param1, int param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   private int parseSimpleStyle(int var1) {
      int var2 = var1;
      int var3 = 0;

      while(var1 < this.msg.length()) {
         char var4 = this.msg.charAt(var1++);
         if (var4 == '\'') {
            var1 = this.msg.indexOf(39, var1);
            if (var1 < 0) {
               throw new IllegalArgumentException("Quoted literal argument style text reaches to the end of the message: " + this.prefix(var2));
            }

            ++var1;
         } else if (var4 == '{') {
            ++var3;
         } else if (var4 == '}') {
            if (var3 <= 0) {
               --var1;
               int var5 = var1 - var2;
               if (var5 > 65535) {
                  throw new IndexOutOfBoundsException("Argument style text too long: " + this.prefix(var2));
               }

               this.addPart(MessagePattern.Part.Type.ARG_STYLE, var2, var5, 0);
               return var1;
            }

            --var3;
         }
      }

      throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
   }

   private int parseChoiceStyle(int var1, int var2) {
      int var3 = var1;
      var1 = this.skipWhiteSpace(var1);
      if (var1 != this.msg.length() && this.msg.charAt(var1) != '}') {
         while(true) {
            int var4 = var1;
            var1 = this.skipDouble(var1);
            int var5 = var1 - var4;
            if (var5 == 0) {
               throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(var3));
            }

            if (var5 > 65535) {
               throw new IndexOutOfBoundsException("Choice number too long: " + this.prefix(var4));
            }

            this.parseDouble(var4, var1, true);
            var1 = this.skipWhiteSpace(var1);
            if (var1 == this.msg.length()) {
               throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(var3));
            }

            char var6 = this.msg.charAt(var1);
            if (var6 != '#' && var6 != '<' && var6 != 8804) {
               throw new IllegalArgumentException("Expected choice separator (#<≤) instead of '" + var6 + "' in choice pattern " + this.prefix(var3));
            }

            this.addPart(MessagePattern.Part.Type.ARG_SELECTOR, var1, 1, 0);
            ++var1;
            var1 = this.parseMessage(var1, 0, var2 + 1, MessagePattern.ArgType.CHOICE);
            if (var1 == this.msg.length()) {
               return var1;
            }

            if (this.msg.charAt(var1) == '}') {
               if (var2 <= 0) {
                  throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(var3));
               }

               return var1;
            }

            var1 = this.skipWhiteSpace(var1 + 1);
         }
      } else {
         throw new IllegalArgumentException("Missing choice argument pattern in " + this.prefix());
      }
   }

   private int parsePluralOrSelectStyle(MessagePattern.ArgType var1, int var2, int var3) {
      int var4 = var2;
      boolean var5 = true;
      boolean var6 = false;

      while(true) {
         var2 = this.skipWhiteSpace(var2);
         boolean var7 = var2 == this.msg.length();
         if (!var7 && this.msg.charAt(var2) != '}') {
            int var8 = var2;
            int var9;
            if (var1.hasPluralStyle() && this.msg.charAt(var2) == '=') {
               var2 = this.skipDouble(var2 + 1);
               var9 = var2 - var8;
               if (var9 == 1) {
                  throw new IllegalArgumentException("Bad " + var1.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(var4));
               }

               if (var9 > 65535) {
                  throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(var8));
               }

               this.addPart(MessagePattern.Part.Type.ARG_SELECTOR, var8, var9, 0);
               this.parseDouble(var8 + 1, var2, false);
            } else {
               var2 = this.skipIdentifier(var2);
               var9 = var2 - var8;
               if (var9 == 0) {
                  throw new IllegalArgumentException("Bad " + var1.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(var4));
               }

               if (var1.hasPluralStyle() && var9 == 6 && var2 < this.msg.length() && this.msg.regionMatches(var8, "offset:", 0, 7)) {
                  if (!var5) {
                     throw new IllegalArgumentException("Plural argument 'offset:' (if present) must precede key-message pairs: " + this.prefix(var4));
                  }

                  int var10 = this.skipWhiteSpace(var2 + 1);
                  var2 = this.skipDouble(var10);
                  if (var2 == var10) {
                     throw new IllegalArgumentException("Missing value for plural 'offset:' " + this.prefix(var4));
                  }

                  if (var2 - var10 > 65535) {
                     throw new IndexOutOfBoundsException("Plural offset value too long: " + this.prefix(var10));
                  }

                  this.parseDouble(var10, var2, false);
                  var5 = false;
                  continue;
               }

               if (var9 > 65535) {
                  throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(var8));
               }

               this.addPart(MessagePattern.Part.Type.ARG_SELECTOR, var8, var9, 0);
               if (this.msg.regionMatches(var8, "other", 0, var9)) {
                  var6 = true;
               }
            }

            var2 = this.skipWhiteSpace(var2);
            if (var2 != this.msg.length() && this.msg.charAt(var2) == '{') {
               var2 = this.parseMessage(var2, 1, var3 + 1, var1);
               var5 = false;
               continue;
            }

            throw new IllegalArgumentException("No message fragment after " + var1.toString().toLowerCase(Locale.ENGLISH) + " selector: " + this.prefix(var8));
         }

         if (var3 <= 0) {
            throw new IllegalArgumentException("Bad " + var1.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(var4));
         }

         if (!var6) {
            throw new IllegalArgumentException("Missing 'other' keyword in " + var1.toString().toLowerCase(Locale.ENGLISH) + " pattern in " + this.prefix());
         }

         return var2;
      }
   }

   private static int parseArgNumber(CharSequence var0, int var1, int var2) {
      if (var1 >= var2) {
         return -2;
      } else {
         char var5 = var0.charAt(var1++);
         int var3;
         boolean var4;
         if (var5 == '0') {
            if (var1 == var2) {
               return 0;
            }

            var3 = 0;
            var4 = true;
         } else {
            if ('1' > var5 || var5 > '9') {
               return -1;
            }

            var3 = var5 - 48;
            var4 = false;
         }

         for(; var1 < var2; var3 = var3 * 10 + (var5 - 48)) {
            var5 = var0.charAt(var1++);
            if ('0' > var5 || var5 > '9') {
               return -1;
            }

            if (var3 >= 214748364) {
               var4 = true;
            }
         }

         if (var4) {
            return -2;
         } else {
            return var3;
         }
      }
   }

   private int parseArgNumber(int var1, int var2) {
      return parseArgNumber(this.msg, var1, var2);
   }

   private void parseDouble(int var1, int var2, boolean var3) {
      if (!$assertionsDisabled && var1 >= var2) {
         throw new AssertionError();
      } else {
         int var4 = 0;
         byte var5 = 0;
         int var6 = var1 + 1;
         char var7 = this.msg.charAt(var1);
         if (var7 == '-') {
            var5 = 1;
            if (var6 == var2) {
               throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(var1, var2));
            }

            var7 = this.msg.charAt(var6++);
         } else if (var7 == '+') {
            if (var6 == var2) {
               throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(var1, var2));
            }

            var7 = this.msg.charAt(var6++);
         }

         if (var7 != 8734) {
            while('0' <= var7 && var7 <= '9') {
               var4 = var4 * 10 + (var7 - 48);
               if (var4 > 32767 + var5) {
                  break;
               }

               if (var6 == var2) {
                  this.addPart(MessagePattern.Part.Type.ARG_INT, var1, var2 - var1, var5 != 0 ? -var4 : var4);
                  return;
               }

               var7 = this.msg.charAt(var6++);
            }

            double var8 = Double.parseDouble(this.msg.substring(var1, var2));
            this.addArgDoublePart(var8, var1, var2 - var1);
         } else if (var3 && var6 == var2) {
            this.addArgDoublePart(var5 != 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, var1, var2 - var1);
         } else {
            throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(var1, var2));
         }
      }
   }

   static void appendReducedApostrophes(String var0, int var1, int var2, StringBuilder var3) {
      int var4 = -1;

      while(true) {
         int var5 = var0.indexOf(39, var1);
         if (var5 < 0 || var5 >= var2) {
            var3.append(var0, var1, var2);
            return;
         }

         if (var5 == var4) {
            var3.append('\'');
            ++var1;
            var4 = -1;
         } else {
            var3.append(var0, var1, var5);
            var4 = var1 = var5 + 1;
         }
      }
   }

   private int skipWhiteSpace(int var1) {
      return PatternProps.skipWhiteSpace(this.msg, var1);
   }

   private int skipIdentifier(int var1) {
      return PatternProps.skipIdentifier(this.msg, var1);
   }

   private int skipDouble(int var1) {
      while(true) {
         if (var1 < this.msg.length()) {
            char var2 = this.msg.charAt(var1);
            if ((var2 >= '0' || "+-.".indexOf(var2) >= 0) && (var2 <= '9' || var2 == 'e' || var2 == 'E' || var2 == 8734)) {
               ++var1;
               continue;
            }
         }

         return var1;
      }
   }

   private void addPart(MessagePattern.Part.Type var1, int var2, int var3, int var4) {
      this.parts.add(new MessagePattern.Part(var1, var2, var3, var4));
   }

   private void addLimitPart(int var1, MessagePattern.Part.Type var2, int var3, int var4, int var5) {
      MessagePattern.Part.access$402((MessagePattern.Part)this.parts.get(var1), this.parts.size());
      this.addPart(var2, var3, var4, var5);
   }

   private void addArgDoublePart(double var1, int var3, int var4) {
      int var5;
      if (this.numericValues == null) {
         this.numericValues = new ArrayList();
         var5 = 0;
      } else {
         var5 = this.numericValues.size();
         if (var5 > 32767) {
            throw new IndexOutOfBoundsException("Too many numeric values");
         }
      }

      this.numericValues.add(var1);
      this.addPart(MessagePattern.Part.Type.ARG_DOUBLE, var3, var4, var5);
   }

   private static String prefix(String var0, int var1) {
      StringBuilder var2 = new StringBuilder(44);
      if (var1 == 0) {
         var2.append("\"");
      } else {
         var2.append("[at pattern index ").append(var1).append("] \"");
      }

      int var3 = var0.length() - var1;
      if (var3 <= 24) {
         var2.append(var1 == 0 ? var0 : var0.substring(var1));
      } else {
         int var4 = var1 + 24 - 4;
         if (Character.isHighSurrogate(var0.charAt(var4 - 1))) {
            --var4;
         }

         var2.append(var0, var1, var4).append(" ...");
      }

      return var2.append("\"").toString();
   }

   private static String prefix(String var0) {
      return prefix(var0, 0);
   }

   private String prefix(int var1) {
      return prefix(this.msg, var1);
   }

   private String prefix() {
      return prefix(this.msg, 0);
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static MessagePattern.ArgType[] access$500() {
      return argTypes;
   }

   public static enum ArgType {
      NONE,
      SIMPLE,
      CHOICE,
      PLURAL,
      SELECT,
      SELECTORDINAL;

      private static final MessagePattern.ArgType[] $VALUES = new MessagePattern.ArgType[]{NONE, SIMPLE, CHOICE, PLURAL, SELECT, SELECTORDINAL};

      public boolean hasPluralStyle() {
         return this == PLURAL || this == SELECTORDINAL;
      }
   }

   public static final class Part {
      private static final int MAX_LENGTH = 65535;
      private static final int MAX_VALUE = 32767;
      private final MessagePattern.Part.Type type;
      private final int index;
      private final char length;
      private short value;
      private int limitPartIndex;

      private Part(MessagePattern.Part.Type var1, int var2, int var3, int var4) {
         this.type = var1;
         this.index = var2;
         this.length = (char)var3;
         this.value = (short)var4;
      }

      public MessagePattern.Part.Type getType() {
         return this.type;
      }

      public int getIndex() {
         return this.index;
      }

      public int getLength() {
         return this.length;
      }

      public int getLimit() {
         return this.index + this.length;
      }

      public int getValue() {
         return this.value;
      }

      public MessagePattern.ArgType getArgType() {
         MessagePattern.Part.Type var1 = this.getType();
         return var1 != MessagePattern.Part.Type.ARG_START && var1 != MessagePattern.Part.Type.ARG_LIMIT ? MessagePattern.ArgType.NONE : MessagePattern.access$500()[this.value];
      }

      public String toString() {
         String var1 = this.type != MessagePattern.Part.Type.ARG_START && this.type != MessagePattern.Part.Type.ARG_LIMIT ? Integer.toString(this.value) : this.getArgType().name();
         return this.type.name() + "(" + var1 + ")@" + this.index;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            MessagePattern.Part var2 = (MessagePattern.Part)var1;
            return this.type.equals(var2.type) && this.index == var2.index && this.length == var2.length && this.value == var2.value && this.limitPartIndex == var2.limitPartIndex;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ((this.type.hashCode() * 37 + this.index) * 37 + this.length) * 37 + this.value;
      }

      static int access$000(MessagePattern.Part var0) {
         return var0.index;
      }

      static short access$100(MessagePattern.Part var0) {
         return var0.value;
      }

      static MessagePattern.Part.Type access$200(MessagePattern.Part var0) {
         return var0.type;
      }

      static char access$300(MessagePattern.Part var0) {
         return var0.length;
      }

      static int access$400(MessagePattern.Part var0) {
         return var0.limitPartIndex;
      }

      static short access$102(MessagePattern.Part var0, short var1) {
         return var0.value = var1;
      }

      Part(MessagePattern.Part.Type var1, int var2, int var3, int var4, Object var5) {
         this(var1, var2, var3, var4);
      }

      static int access$402(MessagePattern.Part var0, int var1) {
         return var0.limitPartIndex = var1;
      }

      public static enum Type {
         MSG_START,
         MSG_LIMIT,
         SKIP_SYNTAX,
         INSERT_CHAR,
         REPLACE_NUMBER,
         ARG_START,
         ARG_LIMIT,
         ARG_NUMBER,
         ARG_NAME,
         ARG_TYPE,
         ARG_STYLE,
         ARG_SELECTOR,
         ARG_INT,
         ARG_DOUBLE;

         private static final MessagePattern.Part.Type[] $VALUES = new MessagePattern.Part.Type[]{MSG_START, MSG_LIMIT, SKIP_SYNTAX, INSERT_CHAR, REPLACE_NUMBER, ARG_START, ARG_LIMIT, ARG_NUMBER, ARG_NAME, ARG_TYPE, ARG_STYLE, ARG_SELECTOR, ARG_INT, ARG_DOUBLE};

         public boolean hasNumericValue() {
            return this == ARG_INT || this == ARG_DOUBLE;
         }
      }
   }

   public static enum ApostropheMode {
      DOUBLE_OPTIONAL,
      DOUBLE_REQUIRED;

      private static final MessagePattern.ApostropheMode[] $VALUES = new MessagePattern.ApostropheMode[]{DOUBLE_OPTIONAL, DOUBLE_REQUIRED};
   }
}
