package com.ibm.icu.text;

import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.util.ULocale;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.text.ChoiceFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MessageFormat extends UFormat {
   static final long serialVersionUID = 7136212545847378652L;
   private transient ULocale ulocale;
   private transient MessagePattern msgPattern;
   private transient Map cachedFormatters;
   private transient Set customFormatArgStarts;
   private transient Format stockDateFormatter;
   private transient Format stockNumberFormatter;
   private transient MessageFormat.PluralSelectorProvider pluralProvider;
   private transient MessageFormat.PluralSelectorProvider ordinalProvider;
   private static final String[] typeList = new String[]{"number", "date", "time", "spellout", "ordinal", "duration"};
   private static final int TYPE_NUMBER = 0;
   private static final int TYPE_DATE = 1;
   private static final int TYPE_TIME = 2;
   private static final int TYPE_SPELLOUT = 3;
   private static final int TYPE_ORDINAL = 4;
   private static final int TYPE_DURATION = 5;
   private static final String[] modifierList = new String[]{"", "currency", "percent", "integer"};
   private static final int MODIFIER_EMPTY = 0;
   private static final int MODIFIER_CURRENCY = 1;
   private static final int MODIFIER_PERCENT = 2;
   private static final int MODIFIER_INTEGER = 3;
   private static final String[] dateModifierList = new String[]{"", "short", "medium", "long", "full"};
   private static final int DATE_MODIFIER_EMPTY = 0;
   private static final int DATE_MODIFIER_SHORT = 1;
   private static final int DATE_MODIFIER_MEDIUM = 2;
   private static final int DATE_MODIFIER_LONG = 3;
   private static final int DATE_MODIFIER_FULL = 4;
   private static final Locale rootLocale = new Locale("");
   private static final char SINGLE_QUOTE = '\'';
   private static final char CURLY_BRACE_LEFT = '{';
   private static final char CURLY_BRACE_RIGHT = '}';
   private static final int STATE_INITIAL = 0;
   private static final int STATE_SINGLE_QUOTE = 1;
   private static final int STATE_IN_QUOTE = 2;
   private static final int STATE_MSG_ELEMENT = 3;
   static final boolean $assertionsDisabled = !MessageFormat.class.desiredAssertionStatus();

   public MessageFormat(String var1) {
      this.ulocale = ULocale.getDefault(ULocale.Category.FORMAT);
      this.applyPattern(var1);
   }

   public MessageFormat(String var1, Locale var2) {
      this(var1, ULocale.forLocale(var2));
   }

   public MessageFormat(String var1, ULocale var2) {
      this.ulocale = var2;
      this.applyPattern(var1);
   }

   public void setLocale(Locale var1) {
      this.setLocale(ULocale.forLocale(var1));
   }

   public void setLocale(ULocale var1) {
      String var2 = this.toPattern();
      this.ulocale = var1;
      this.stockNumberFormatter = this.stockDateFormatter = null;
      this.pluralProvider = null;
      this.ordinalProvider = null;
      this.applyPattern(var2);
   }

   public Locale getLocale() {
      return this.ulocale.toLocale();
   }

   public ULocale getULocale() {
      return this.ulocale;
   }

   public void applyPattern(String var1) {
      try {
         if (this.msgPattern == null) {
            this.msgPattern = new MessagePattern(var1);
         } else {
            this.msgPattern.parse(var1);
         }

         this.cacheExplicitFormats();
      } catch (RuntimeException var3) {
         this.resetPattern();
         throw var3;
      }
   }

   public void applyPattern(String var1, MessagePattern.ApostropheMode var2) {
      if (this.msgPattern == null) {
         this.msgPattern = new MessagePattern(var2);
      } else if (var2 != this.msgPattern.getApostropheMode()) {
         this.msgPattern.clearPatternAndSetApostropheMode(var2);
      }

      this.applyPattern(var1);
   }

   public MessagePattern.ApostropheMode getApostropheMode() {
      if (this.msgPattern == null) {
         this.msgPattern = new MessagePattern();
      }

      return this.msgPattern.getApostropheMode();
   }

   public String toPattern() {
      if (this.customFormatArgStarts != null) {
         throw new IllegalStateException("toPattern() is not supported after custom Format objects have been set via setFormat() or similar APIs");
      } else if (this.msgPattern == null) {
         return "";
      } else {
         String var1 = this.msgPattern.getPatternString();
         return var1 == null ? "" : var1;
      }
   }

   private int nextTopLevelArgStart(int var1) {
      if (var1 != 0) {
         var1 = this.msgPattern.getLimitPartIndex(var1);
      }

      MessagePattern.Part.Type var2;
      do {
         ++var1;
         var2 = this.msgPattern.getPartType(var1);
         if (var2 == MessagePattern.Part.Type.ARG_START) {
            return var1;
         }
      } while(var2 != MessagePattern.Part.Type.MSG_LIMIT);

      return -1;
   }

   private String getArgName(int var1) {
      MessagePattern.Part var2 = this.msgPattern.getPart(var1);
      return var2.getType() == MessagePattern.Part.Type.ARG_NAME ? this.msgPattern.getSubstring(var2) : Integer.toString(var2.getValue());
   }

   public void setFormatsByArgumentIndex(Format[] var1) {
      if (this.msgPattern.hasNamedArguments()) {
         throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
      } else {
         int var2 = 0;

         while((var2 = this.nextTopLevelArgStart(var2)) >= 0) {
            int var3 = this.msgPattern.getPart(var2 + 1).getValue();
            if (var3 < var1.length) {
               this.setCustomArgStartFormat(var2, var1[var3]);
            }
         }

      }
   }

   public void setFormatsByArgumentName(Map var1) {
      int var2 = 0;

      while((var2 = this.nextTopLevelArgStart(var2)) >= 0) {
         String var3 = this.getArgName(var2 + 1);
         if (var1.containsKey(var3)) {
            this.setCustomArgStartFormat(var2, (Format)var1.get(var3));
         }
      }

   }

   public void setFormats(Format[] var1) {
      int var2 = 0;

      for(int var3 = 0; var2 < var1.length && (var3 = this.nextTopLevelArgStart(var3)) >= 0; ++var2) {
         this.setCustomArgStartFormat(var3, var1[var2]);
      }

   }

   public void setFormatByArgumentIndex(int var1, Format var2) {
      if (this.msgPattern.hasNamedArguments()) {
         throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
      } else {
         int var3 = 0;

         while((var3 = this.nextTopLevelArgStart(var3)) >= 0) {
            if (this.msgPattern.getPart(var3 + 1).getValue() == var1) {
               this.setCustomArgStartFormat(var3, var2);
            }
         }

      }
   }

   public void setFormatByArgumentName(String var1, Format var2) {
      int var3 = MessagePattern.validateArgumentName(var1);
      if (var3 >= -1) {
         int var4 = 0;

         while((var4 = this.nextTopLevelArgStart(var4)) >= 0) {
            int var10001 = var4 + 1;
            if (var1 == var3) {
               this.setCustomArgStartFormat(var4, var2);
            }
         }

      }
   }

   public void setFormat(int var1, Format var2) {
      int var3 = 0;

      for(int var4 = 0; (var4 = this.nextTopLevelArgStart(var4)) >= 0; ++var3) {
         if (var3 == var1) {
            this.setCustomArgStartFormat(var4, var2);
            return;
         }
      }

      throw new ArrayIndexOutOfBoundsException(var1);
   }

   public Format[] getFormatsByArgumentIndex() {
      if (this.msgPattern.hasNamedArguments()) {
         throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
      } else {
         ArrayList var1 = new ArrayList();
         int var2 = 0;

         while((var2 = this.nextTopLevelArgStart(var2)) >= 0) {
            int var3 = this.msgPattern.getPart(var2 + 1).getValue();

            while(var3 >= var1.size()) {
               var1.add((Object)null);
            }

            var1.set(var3, this.cachedFormatters == null ? null : (Format)this.cachedFormatters.get(var2));
         }

         return (Format[])var1.toArray(new Format[var1.size()]);
      }
   }

   public Format[] getFormats() {
      ArrayList var1 = new ArrayList();
      int var2 = 0;

      while((var2 = this.nextTopLevelArgStart(var2)) >= 0) {
         var1.add(this.cachedFormatters == null ? null : (Format)this.cachedFormatters.get(var2));
      }

      return (Format[])var1.toArray(new Format[var1.size()]);
   }

   public Set getArgumentNames() {
      HashSet var1 = new HashSet();
      int var2 = 0;

      while((var2 = this.nextTopLevelArgStart(var2)) >= 0) {
         var1.add(this.getArgName(var2 + 1));
      }

      return var1;
   }

   public Format getFormatByArgumentName(String var1) {
      if (this.cachedFormatters == null) {
         return null;
      } else {
         int var2 = MessagePattern.validateArgumentName(var1);
         if (var2 < -1) {
            return null;
         } else {
            int var3 = 0;

            do {
               if ((var3 = this.nextTopLevelArgStart(var3)) < 0) {
                  return null;
               }

               int var10001 = var3 + 1;
            } while(var1 != var2);

            return (Format)this.cachedFormatters.get(var3);
         }
      }
   }

   public final StringBuffer format(Object[] var1, StringBuffer var2, FieldPosition var3) {
      this.format(var1, (Map)null, new MessageFormat.AppendableWrapper(var2), var3);
      return var2;
   }

   public final StringBuffer format(Map var1, StringBuffer var2, FieldPosition var3) {
      this.format((Object[])null, var1, new MessageFormat.AppendableWrapper(var2), var3);
      return var2;
   }

   public static String format(String var0, Object... var1) {
      MessageFormat var2 = new MessageFormat(var0);
      return var2.format(var1);
   }

   public static String format(String var0, Map var1) {
      MessageFormat var2 = new MessageFormat(var0);
      return var2.format(var1);
   }

   public boolean usesNamedArguments() {
      return this.msgPattern.hasNamedArguments();
   }

   public final StringBuffer format(Object var1, StringBuffer var2, FieldPosition var3) {
      this.format(var1, new MessageFormat.AppendableWrapper(var2), var3);
      return var2;
   }

   public AttributedCharacterIterator formatToCharacterIterator(Object var1) {
      if (var1 == null) {
         throw new NullPointerException("formatToCharacterIterator must be passed non-null object");
      } else {
         StringBuilder var2 = new StringBuilder();
         MessageFormat.AppendableWrapper var3 = new MessageFormat.AppendableWrapper(var2);
         var3.useAttributes();
         this.format((Object)var1, (MessageFormat.AppendableWrapper)var3, (FieldPosition)null);
         AttributedString var4 = new AttributedString(var2.toString());
         Iterator var5 = MessageFormat.AppendableWrapper.access$000(var3).iterator();

         while(var5.hasNext()) {
            MessageFormat.AttributeAndPosition var6 = (MessageFormat.AttributeAndPosition)var5.next();
            var4.addAttribute(MessageFormat.AttributeAndPosition.access$100(var6), MessageFormat.AttributeAndPosition.access$200(var6), MessageFormat.AttributeAndPosition.access$300(var6), MessageFormat.AttributeAndPosition.access$400(var6));
         }

         return var4.getIterator();
      }
   }

   public Object[] parse(String var1, ParsePosition var2) {
      if (this.msgPattern.hasNamedArguments()) {
         throw new IllegalArgumentException("This method is not available in MessageFormat objects that use named argument.");
      } else {
         int var3 = -1;
         int var4 = 0;

         int var5;
         while((var4 = this.nextTopLevelArgStart(var4)) >= 0) {
            var5 = this.msgPattern.getPart(var4 + 1).getValue();
            if (var5 > var3) {
               var3 = var5;
            }
         }

         Object[] var6 = new Object[var3 + 1];
         var5 = var2.getIndex();
         this.parse(0, var1, var2, var6, (Map)null);
         if (var2.getIndex() == var5) {
            return null;
         } else {
            return var6;
         }
      }
   }

   public Map parseToMap(String var1, ParsePosition var2) {
      HashMap var3 = new HashMap();
      int var4 = var2.getIndex();
      this.parse(0, var1, var2, (Object[])null, var3);
      return var2.getIndex() == var4 ? null : var3;
   }

   public Object[] parse(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      Object[] var3 = this.parse(var1, var2);
      if (var2.getIndex() == 0) {
         throw new ParseException("MessageFormat parse error!", var2.getErrorIndex());
      } else {
         return var3;
      }
   }

   private void parse(int var1, String var2, ParsePosition var3, Object[] var4, Map var5) {
      if (var2 != null) {
         String var6 = this.msgPattern.getPatternString();
         int var7 = this.msgPattern.getPart(var1).getLimit();
         int var8 = var3.getIndex();
         ParsePosition var9 = new ParsePosition(0);
         int var10 = var1 + 1;

         while(true) {
            MessagePattern.Part var11 = this.msgPattern.getPart(var10);
            MessagePattern.Part.Type var12 = var11.getType();
            int var13 = var11.getIndex();
            int var14 = var13 - var7;
            if (var14 != 0 && !var6.regionMatches(var7, var2, var8, var14)) {
               var3.setErrorIndex(var8);
               return;
            }

            var8 += var14;
            int var10000 = var7 + var14;
            if (var12 == MessagePattern.Part.Type.MSG_LIMIT) {
               var3.setIndex(var8);
               return;
            }

            if (var12 != MessagePattern.Part.Type.SKIP_SYNTAX && var12 != MessagePattern.Part.Type.INSERT_CHAR) {
               if (!$assertionsDisabled && var12 != MessagePattern.Part.Type.ARG_START) {
                  throw new AssertionError("Unexpected Part " + var11 + " in parsed message.");
               }

               int var15 = this.msgPattern.getLimitPartIndex(var10);
               MessagePattern.ArgType var16 = var11.getArgType();
               ++var10;
               var11 = this.msgPattern.getPart(var10);
               Object var17 = null;
               int var18 = 0;
               String var19 = null;
               if (var4 != null) {
                  var18 = var11.getValue();
                  var17 = var18;
               } else {
                  if (var11.getType() == MessagePattern.Part.Type.ARG_NAME) {
                     var19 = this.msgPattern.getSubstring(var11);
                  } else {
                     var19 = Integer.toString(var11.getValue());
                  }

                  var17 = var19;
               }

               ++var10;
               Format var20 = null;
               boolean var21 = false;
               Object var22 = null;
               if (this.cachedFormatters != null && (var20 = (Format)this.cachedFormatters.get(var10 - 2)) != null) {
                  var9.setIndex(var8);
                  var22 = var20.parseObject(var2, var9);
                  if (var9.getIndex() == var8) {
                     var3.setErrorIndex(var8);
                     return;
                  }

                  var21 = true;
                  var8 = var9.getIndex();
               } else if (var16 == MessagePattern.ArgType.NONE || this.cachedFormatters != null && this.cachedFormatters.containsKey(var10 - 2)) {
                  String var26 = this.getLiteralStringUntilNextArgument(var15);
                  int var24;
                  if (var26.length() != 0) {
                     var24 = var2.indexOf(var26, var8);
                  } else {
                     var24 = var2.length();
                  }

                  if (var24 < 0) {
                     var3.setErrorIndex(var8);
                     return;
                  }

                  String var25 = var2.substring(var8, var24);
                  if (!var25.equals("{" + var17.toString() + "}")) {
                     var21 = true;
                     var22 = var25;
                  }

                  var8 = var24;
               } else {
                  if (var16 != MessagePattern.ArgType.CHOICE) {
                     if (!var16.hasPluralStyle() && var16 != MessagePattern.ArgType.SELECT) {
                        throw new IllegalStateException("unexpected argType " + var16);
                     }

                     throw new UnsupportedOperationException("Parsing of plural/select/selectordinal argument is not supported.");
                  }

                  var9.setIndex(var8);
                  double var23 = parseChoiceArgument(this.msgPattern, var10, var2, var9);
                  if (var9.getIndex() == var8) {
                     var3.setErrorIndex(var8);
                     return;
                  }

                  var22 = var23;
                  var21 = true;
                  var8 = var9.getIndex();
               }

               if (var21) {
                  if (var4 != null) {
                     var4[var18] = var22;
                  } else if (var5 != null) {
                     var5.put(var19, var22);
                  }
               }

               var7 = this.msgPattern.getPart(var15).getLimit();
               var10 = var15;
            } else {
               var7 = var11.getLimit();
            }

            ++var10;
         }
      }
   }

   public Map parseToMap(String var1) throws ParseException {
      ParsePosition var2 = new ParsePosition(0);
      HashMap var3 = new HashMap();
      this.parse(0, var1, var2, (Object[])null, var3);
      if (var2.getIndex() == 0) {
         throw new ParseException("MessageFormat parse error!", var2.getErrorIndex());
      } else {
         return var3;
      }
   }

   public Object parseObject(String var1, ParsePosition var2) {
      return !this.msgPattern.hasNamedArguments() ? this.parse(var1, var2) : this.parseToMap(var1, var2);
   }

   public Object clone() {
      MessageFormat var1 = (MessageFormat)super.clone();
      Iterator var2;
      if (this.customFormatArgStarts != null) {
         var1.customFormatArgStarts = new HashSet();
         var2 = this.customFormatArgStarts.iterator();

         while(var2.hasNext()) {
            Integer var3 = (Integer)var2.next();
            var1.customFormatArgStarts.add(var3);
         }
      } else {
         var1.customFormatArgStarts = null;
      }

      if (this.cachedFormatters != null) {
         var1.cachedFormatters = new HashMap();
         var2 = this.cachedFormatters.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var4 = (Entry)var2.next();
            var1.cachedFormatters.put(var4.getKey(), var4.getValue());
         }
      } else {
         var1.cachedFormatters = null;
      }

      var1.msgPattern = this.msgPattern == null ? null : (MessagePattern)this.msgPattern.clone();
      var1.stockDateFormatter = this.stockDateFormatter == null ? null : (Format)this.stockDateFormatter.clone();
      var1.stockNumberFormatter = this.stockNumberFormatter == null ? null : (Format)this.stockNumberFormatter.clone();
      var1.pluralProvider = null;
      var1.ordinalProvider = null;
      return var1;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         MessageFormat var2 = (MessageFormat)var1;
         return Utility.objectEquals(this.ulocale, var2.ulocale) && Utility.objectEquals(this.msgPattern, var2.msgPattern) && Utility.objectEquals(this.cachedFormatters, var2.cachedFormatters) && Utility.objectEquals(this.customFormatArgStarts, var2.customFormatArgStarts);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.msgPattern.getPatternString().hashCode();
   }

   private void format(int var1, double var2, Object[] var4, Map var5, MessageFormat.AppendableWrapper var6, FieldPosition var7) {
      String var8 = this.msgPattern.getPatternString();
      int var9 = this.msgPattern.getPart(var1).getLimit();
      int var10 = var1 + 1;

      while(true) {
         MessagePattern.Part var11 = this.msgPattern.getPart(var10);
         MessagePattern.Part.Type var12 = var11.getType();
         int var13 = var11.getIndex();
         var6.append(var8, var9, var13);
         if (var12 == MessagePattern.Part.Type.MSG_LIMIT) {
            return;
         }

         var9 = var11.getLimit();
         if (var12 == MessagePattern.Part.Type.REPLACE_NUMBER) {
            if (this.stockNumberFormatter == null) {
               this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
            }

            var6.formatAndAppend(this.stockNumberFormatter, var2);
         } else if (var12 == MessagePattern.Part.Type.ARG_START) {
            int var14 = this.msgPattern.getLimitPartIndex(var10);
            MessagePattern.ArgType var15 = var11.getArgType();
            ++var10;
            var11 = this.msgPattern.getPart(var10);
            String var17 = null;
            Object var18 = null;
            Object var16;
            int var19;
            if (var4 != null) {
               var19 = var11.getValue();
               if (MessageFormat.AppendableWrapper.access$000(var6) != null) {
                  var18 = var19;
               }

               if (0 <= var19 && var19 < var4.length) {
                  var16 = var4[var19];
               } else {
                  var16 = null;
                  var17 = "{" + var19 + "}";
               }
            } else {
               String var27;
               if (var11.getType() == MessagePattern.Part.Type.ARG_NAME) {
                  var27 = this.msgPattern.getSubstring(var11);
               } else {
                  var27 = Integer.toString(var11.getValue());
               }

               var18 = var27;
               if (var5 != null && var5.containsKey(var27)) {
                  var16 = var5.get(var27);
               } else {
                  var16 = null;
                  var17 = "{" + var27 + "}";
               }
            }

            ++var10;
            var19 = MessageFormat.AppendableWrapper.access$500(var6);
            Format var20 = null;
            if (var17 != null) {
               var6.append((CharSequence)var17);
            } else if (var16 == null) {
               var6.append((CharSequence)"null");
            } else if (this.cachedFormatters != null && (var20 = (Format)this.cachedFormatters.get(var10 - 2)) != null) {
               if (!(var20 instanceof ChoiceFormat) && !(var20 instanceof PluralFormat) && !(var20 instanceof SelectFormat)) {
                  var6.formatAndAppend(var20, var16);
               } else {
                  String var29 = var20.format(var16);
                  if (var29.indexOf(123) < 0 && (var29.indexOf(39) < 0 || this.msgPattern.jdkAposMode())) {
                     if (MessageFormat.AppendableWrapper.access$000(var6) == null) {
                        var6.append((CharSequence)var29);
                     } else {
                        var6.formatAndAppend(var20, var16);
                     }
                  } else {
                     MessageFormat var22 = new MessageFormat(var29, this.ulocale);
                     var22.format(0, 0.0D, var4, var5, var6, (FieldPosition)null);
                  }
               }
            } else if (var15 != MessagePattern.ArgType.NONE && (this.cachedFormatters == null || !this.cachedFormatters.containsKey(var10 - 2))) {
               double var21;
               if (var15 == MessagePattern.ArgType.CHOICE) {
                  if (!(var16 instanceof Number)) {
                     throw new IllegalArgumentException("'" + var16 + "' is not a Number");
                  }

                  var21 = ((Number)var16).doubleValue();
                  int var23 = findChoiceSubMessage(this.msgPattern, var10, var21);
                  this.formatComplexSubMessage(var23, 0.0D, var4, var5, var6);
               } else if (var15.hasPluralStyle()) {
                  if (!(var16 instanceof Number)) {
                     throw new IllegalArgumentException("'" + var16 + "' is not a Number");
                  }

                  var21 = ((Number)var16).doubleValue();
                  MessageFormat.PluralSelectorProvider var30;
                  if (var15 == MessagePattern.ArgType.PLURAL) {
                     if (this.pluralProvider == null) {
                        this.pluralProvider = new MessageFormat.PluralSelectorProvider(this.ulocale, PluralRules.PluralType.CARDINAL);
                     }

                     var30 = this.pluralProvider;
                  } else {
                     if (this.ordinalProvider == null) {
                        this.ordinalProvider = new MessageFormat.PluralSelectorProvider(this.ulocale, PluralRules.PluralType.ORDINAL);
                     }

                     var30 = this.ordinalProvider;
                  }

                  int var24 = PluralFormat.findSubMessage(this.msgPattern, var10, var30, var21);
                  double var25 = this.msgPattern.getPluralOffset(var10);
                  this.formatComplexSubMessage(var24, var21 - var25, var4, var5, var6);
               } else {
                  if (var15 != MessagePattern.ArgType.SELECT) {
                     throw new IllegalStateException("unexpected argType " + var15);
                  }

                  int var28 = SelectFormat.findSubMessage(this.msgPattern, var10, var16.toString());
                  this.formatComplexSubMessage(var28, 0.0D, var4, var5, var6);
               }
            } else if (var16 instanceof Number) {
               if (this.stockNumberFormatter == null) {
                  this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
               }

               var6.formatAndAppend(this.stockNumberFormatter, var16);
            } else if (var16 instanceof Date) {
               if (this.stockDateFormatter == null) {
                  this.stockDateFormatter = DateFormat.getDateTimeInstance(3, 3, (ULocale)this.ulocale);
               }

               var6.formatAndAppend(this.stockDateFormatter, var16);
            } else {
               var6.append((CharSequence)var16.toString());
            }

            var7 = this.updateMetaData(var6, var19, var7, var18);
            var9 = this.msgPattern.getPart(var14).getLimit();
            var10 = var14;
         }

         ++var10;
      }
   }

   private void formatComplexSubMessage(int var1, double var2, Object[] var4, Map var5, MessageFormat.AppendableWrapper var6) {
      if (!this.msgPattern.jdkAposMode()) {
         this.format(var1, var2, var4, var5, var6, (FieldPosition)null);
      } else {
         String var7 = this.msgPattern.getPatternString();
         StringBuilder var9 = null;
         int var10 = this.msgPattern.getPart(var1).getLimit();
         int var11 = var1;

         while(true) {
            while(true) {
               ++var11;
               MessagePattern.Part var12 = this.msgPattern.getPart(var11);
               MessagePattern.Part.Type var13 = var12.getType();
               int var14 = var12.getIndex();
               if (var13 == MessagePattern.Part.Type.MSG_LIMIT) {
                  String var8;
                  if (var9 == null) {
                     var8 = var7.substring(var10, var14);
                  } else {
                     var8 = var9.append(var7, var10, var14).toString();
                  }

                  if (var8.indexOf(123) >= 0) {
                     MessageFormat var15 = new MessageFormat("", this.ulocale);
                     var15.applyPattern(var8, MessagePattern.ApostropheMode.DOUBLE_REQUIRED);
                     var15.format(0, 0.0D, var4, var5, var6, (FieldPosition)null);
                  } else {
                     var6.append((CharSequence)var8);
                  }

                  return;
               }

               if (var13 != MessagePattern.Part.Type.REPLACE_NUMBER && var13 != MessagePattern.Part.Type.SKIP_SYNTAX) {
                  if (var13 == MessagePattern.Part.Type.ARG_START) {
                     if (var9 == null) {
                        var9 = new StringBuilder();
                     }

                     var9.append(var7, var10, var14);
                     var10 = var14;
                     var11 = this.msgPattern.getLimitPartIndex(var11);
                     var14 = this.msgPattern.getPart(var11).getLimit();
                     MessagePattern.appendReducedApostrophes(var7, var10, var14, var9);
                     var10 = var14;
                  }
               } else {
                  if (var9 == null) {
                     var9 = new StringBuilder();
                  }

                  var9.append(var7, var10, var14);
                  if (var13 == MessagePattern.Part.Type.REPLACE_NUMBER) {
                     if (this.stockNumberFormatter == null) {
                        this.stockNumberFormatter = NumberFormat.getInstance(this.ulocale);
                     }

                     var9.append(this.stockNumberFormatter.format(var2));
                  }

                  var10 = var12.getLimit();
               }
            }
         }
      }
   }

   private String getLiteralStringUntilNextArgument(int var1) {
      StringBuilder var2 = new StringBuilder();
      String var3 = this.msgPattern.getPatternString();
      int var4 = this.msgPattern.getPart(var1).getLimit();
      int var5 = var1 + 1;

      while(true) {
         MessagePattern.Part var6 = this.msgPattern.getPart(var5);
         MessagePattern.Part.Type var7 = var6.getType();
         int var8 = var6.getIndex();
         var2.append(var3, var4, var8);
         if (var7 == MessagePattern.Part.Type.ARG_START || var7 == MessagePattern.Part.Type.MSG_LIMIT) {
            return var2.toString();
         }

         if (!$assertionsDisabled && var7 != MessagePattern.Part.Type.SKIP_SYNTAX && var7 != MessagePattern.Part.Type.INSERT_CHAR) {
            throw new AssertionError("Unexpected Part " + var6 + " in parsed message.");
         }

         var4 = var6.getLimit();
         ++var5;
      }
   }

   private FieldPosition updateMetaData(MessageFormat.AppendableWrapper var1, int var2, FieldPosition var3, Object var4) {
      if (MessageFormat.AppendableWrapper.access$000(var1) != null && var2 < MessageFormat.AppendableWrapper.access$500(var1)) {
         MessageFormat.AppendableWrapper.access$000(var1).add(new MessageFormat.AttributeAndPosition(var4, var2, MessageFormat.AppendableWrapper.access$500(var1)));
      }

      if (var3 != null && MessageFormat.Field.ARGUMENT.equals(var3.getFieldAttribute())) {
         var3.setBeginIndex(var2);
         var3.setEndIndex(MessageFormat.AppendableWrapper.access$500(var1));
         return null;
      } else {
         return var3;
      }
   }

   private static int findChoiceSubMessage(MessagePattern var0, int var1, double var2) {
      int var4 = var0.countParts();
      var1 += 2;

      int var5;
      while(true) {
         var5 = var1;
         var1 = var0.getLimitPartIndex(var1);
         ++var1;
         if (var1 >= var4) {
            break;
         }

         MessagePattern.Part var6 = var0.getPart(var1++);
         MessagePattern.Part.Type var7 = var6.getType();
         if (var7 == MessagePattern.Part.Type.ARG_LIMIT) {
            break;
         }

         if (!$assertionsDisabled && !var7.hasNumericValue()) {
            throw new AssertionError();
         }

         double var8 = var0.getNumericValue(var6);
         int var10 = var0.getPatternIndex(var1++);
         char var11 = var0.getPatternString().charAt(var10);
         if (var11 == '<') {
            if (!(var2 > var8)) {
               break;
            }
         } else if (!(var2 >= var8)) {
            break;
         }
      }

      return var5;
   }

   private static double parseChoiceArgument(MessagePattern var0, int var1, String var2, ParsePosition var3) {
      int var4 = var3.getIndex();
      int var5 = var4;
      double var6 = Double.NaN;

      int var10;
      for(double var8 = 0.0D; var0.getPartType(var1) != MessagePattern.Part.Type.ARG_LIMIT; var1 = var10 + 1) {
         var8 = var0.getNumericValue(var0.getPart(var1));
         var1 += 2;
         var10 = var0.getLimitPartIndex(var1);
         int var11 = matchStringUntilLimitPart(var0, var1, var10, var2, var4);
         if (var11 >= 0) {
            int var12 = var4 + var11;
            if (var12 > var5) {
               var5 = var12;
               var6 = var8;
               if (var12 == var2.length()) {
                  break;
               }
            }
         }
      }

      if (var5 == var4) {
         var3.setErrorIndex(var4);
      } else {
         var3.setIndex(var5);
      }

      return var6;
   }

   private static int matchStringUntilLimitPart(MessagePattern var0, int var1, int var2, String var3, int var4) {
      int var5 = 0;
      String var6 = var0.getPatternString();
      int var7 = var0.getPart(var1).getLimit();

      while(true) {
         MessagePattern.Part var8;
         do {
            ++var1;
            var8 = var0.getPart(var1);
         } while(var1 != var2 && var8.getType() != MessagePattern.Part.Type.SKIP_SYNTAX);

         int var9 = var8.getIndex();
         int var10 = var9 - var7;
         if (var10 != 0 && !var3.regionMatches(var4, var6, var7, var10)) {
            return -1;
         }

         var5 += var10;
         if (var1 == var2) {
            return var5;
         }

         var7 = var8.getLimit();
      }
   }

   private void format(Object var1, MessageFormat.AppendableWrapper var2, FieldPosition var3) {
      if (var1 != null && !(var1 instanceof Map)) {
         this.format((Object[])((Object[])var1), (Map)null, var2, var3);
      } else {
         this.format((Object[])null, (Map)var1, var2, var3);
      }

   }

   private void format(Object[] var1, Map var2, MessageFormat.AppendableWrapper var3, FieldPosition var4) {
      if (var1 != null && this.msgPattern.hasNamedArguments()) {
         throw new IllegalArgumentException("This method is not available in MessageFormat objects that use alphanumeric argument names.");
      } else {
         this.format(0, 0.0D, var1, var2, var3, var4);
      }
   }

   private void resetPattern() {
      if (this.msgPattern != null) {
         this.msgPattern.clear();
      }

      if (this.cachedFormatters != null) {
         this.cachedFormatters.clear();
      }

      this.customFormatArgStarts = null;
   }

   private Format createAppropriateFormat(String var1, String var2) {
      Object var3 = null;
      int var4 = findKeyword(var1, typeList);
      RuleBasedNumberFormat var5;
      String var6;
      switch(var4) {
      case 0:
         switch(findKeyword(var2, modifierList)) {
         case 0:
            var3 = NumberFormat.getInstance(this.ulocale);
            return (Format)var3;
         case 1:
            var3 = NumberFormat.getCurrencyInstance(this.ulocale);
            return (Format)var3;
         case 2:
            var3 = NumberFormat.getPercentInstance(this.ulocale);
            return (Format)var3;
         case 3:
            var3 = NumberFormat.getIntegerInstance(this.ulocale);
            return (Format)var3;
         default:
            var3 = new DecimalFormat(var2, new DecimalFormatSymbols(this.ulocale));
            return (Format)var3;
         }
      case 1:
         switch(findKeyword(var2, dateModifierList)) {
         case 0:
            var3 = DateFormat.getDateInstance(2, (ULocale)this.ulocale);
            return (Format)var3;
         case 1:
            var3 = DateFormat.getDateInstance(3, (ULocale)this.ulocale);
            return (Format)var3;
         case 2:
            var3 = DateFormat.getDateInstance(2, (ULocale)this.ulocale);
            return (Format)var3;
         case 3:
            var3 = DateFormat.getDateInstance(1, (ULocale)this.ulocale);
            return (Format)var3;
         case 4:
            var3 = DateFormat.getDateInstance(0, (ULocale)this.ulocale);
            return (Format)var3;
         default:
            var3 = new SimpleDateFormat(var2, this.ulocale);
            return (Format)var3;
         }
      case 2:
         switch(findKeyword(var2, dateModifierList)) {
         case 0:
            var3 = DateFormat.getTimeInstance(2, (ULocale)this.ulocale);
            return (Format)var3;
         case 1:
            var3 = DateFormat.getTimeInstance(3, (ULocale)this.ulocale);
            return (Format)var3;
         case 2:
            var3 = DateFormat.getTimeInstance(2, (ULocale)this.ulocale);
            return (Format)var3;
         case 3:
            var3 = DateFormat.getTimeInstance(1, (ULocale)this.ulocale);
            return (Format)var3;
         case 4:
            var3 = DateFormat.getTimeInstance(0, (ULocale)this.ulocale);
            return (Format)var3;
         default:
            var3 = new SimpleDateFormat(var2, this.ulocale);
            return (Format)var3;
         }
      case 3:
         var5 = new RuleBasedNumberFormat(this.ulocale, 1);
         var6 = var2.trim();
         if (var6.length() != 0) {
            try {
               var5.setDefaultRuleSet(var6);
            } catch (Exception var10) {
            }
         }

         var3 = var5;
         break;
      case 4:
         var5 = new RuleBasedNumberFormat(this.ulocale, 2);
         var6 = var2.trim();
         if (var6.length() != 0) {
            try {
               var5.setDefaultRuleSet(var6);
            } catch (Exception var9) {
            }
         }

         var3 = var5;
         break;
      case 5:
         var5 = new RuleBasedNumberFormat(this.ulocale, 3);
         var6 = var2.trim();
         if (var6.length() != 0) {
            try {
               var5.setDefaultRuleSet(var6);
            } catch (Exception var8) {
            }
         }

         var3 = var5;
         break;
      default:
         throw new IllegalArgumentException("Unknown format type \"" + var1 + "\"");
      }

      return (Format)var3;
   }

   private static final int findKeyword(String var0, String[] var1) {
      var0 = PatternProps.trimWhiteSpace(var0).toLowerCase(rootLocale);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var0.equals(var1[var2])) {
            return var2;
         }
      }

      return -1;
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      var1.defaultWriteObject();
      var1.writeObject(this.ulocale.toLanguageTag());
      if (this.msgPattern == null) {
         this.msgPattern = new MessagePattern();
      }

      var1.writeObject(this.msgPattern.getApostropheMode());
      var1.writeObject(this.msgPattern.getPatternString());
      if (this.customFormatArgStarts != null && !this.customFormatArgStarts.isEmpty()) {
         var1.writeInt(this.customFormatArgStarts.size());
         int var2 = 0;

         for(int var3 = 0; (var3 = this.nextTopLevelArgStart(var3)) >= 0; ++var2) {
            if (this.customFormatArgStarts.contains(var3)) {
               var1.writeInt(var2);
               var1.writeObject(this.cachedFormatters.get(var3));
            }
         }
      } else {
         var1.writeInt(0);
      }

      var1.writeInt(0);
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      String var2 = (String)var1.readObject();
      this.ulocale = ULocale.forLanguageTag(var2);
      MessagePattern.ApostropheMode var3 = (MessagePattern.ApostropheMode)var1.readObject();
      if (this.msgPattern == null || var3 != this.msgPattern.getApostropheMode()) {
         this.msgPattern = new MessagePattern(var3);
      }

      String var4 = (String)var1.readObject();
      if (var4 != null) {
         this.applyPattern(var4);
      }

      int var5;
      for(var5 = var1.readInt(); var5 > 0; --var5) {
         int var6 = var1.readInt();
         Format var7 = (Format)var1.readObject();
         this.setFormat(var6, var7);
      }

      for(var5 = var1.readInt(); var5 > 0; --var5) {
         var1.readInt();
         var1.readObject();
      }

   }

   private void cacheExplicitFormats() {
      if (this.cachedFormatters != null) {
         this.cachedFormatters.clear();
      }

      this.customFormatArgStarts = null;
      int var1 = this.msgPattern.countParts() - 2;

      for(int var2 = 1; var2 < var1; ++var2) {
         MessagePattern.Part var3 = this.msgPattern.getPart(var2);
         if (var3.getType() == MessagePattern.Part.Type.ARG_START) {
            MessagePattern.ArgType var4 = var3.getArgType();
            if (var4 == MessagePattern.ArgType.SIMPLE) {
               int var5 = var2;
               var2 += 2;
               String var6 = this.msgPattern.getSubstring(this.msgPattern.getPart(var2++));
               String var7 = "";
               if ((var3 = this.msgPattern.getPart(var2)).getType() == MessagePattern.Part.Type.ARG_STYLE) {
                  var7 = this.msgPattern.getSubstring(var3);
                  ++var2;
               }

               Format var8 = this.createAppropriateFormat(var6, var7);
               this.setArgStartFormat(var5, var8);
            }
         }
      }

   }

   private void setArgStartFormat(int var1, Format var2) {
      if (this.cachedFormatters == null) {
         this.cachedFormatters = new HashMap();
      }

      this.cachedFormatters.put(var1, var2);
   }

   private void setCustomArgStartFormat(int var1, Format var2) {
      this.setArgStartFormat(var1, var2);
      if (this.customFormatArgStarts == null) {
         this.customFormatArgStarts = new HashSet();
      }

      this.customFormatArgStarts.add(var1);
   }

   public static String autoQuoteApostrophe(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length() * 2);
      byte var2 = 0;
      int var3 = 0;
      int var4 = 0;

      for(int var5 = var0.length(); var4 < var5; ++var4) {
         char var6;
         var6 = var0.charAt(var4);
         label39:
         switch(var2) {
         case 0:
            switch(var6) {
            case '\'':
               var2 = 1;
               break label39;
            case '{':
               var2 = 3;
               ++var3;
            default:
               break label39;
            }
         case 1:
            switch(var6) {
            case '\'':
               var2 = 0;
               break label39;
            case '{':
            case '}':
               var2 = 2;
               break label39;
            default:
               var1.append('\'');
               var2 = 0;
               break label39;
            }
         case 2:
            switch(var6) {
            case '\'':
               var2 = 0;
            default:
               break label39;
            }
         case 3:
            switch(var6) {
            case '{':
               ++var3;
               break;
            case '}':
               --var3;
               if (var3 == 0) {
                  var2 = 0;
               }
            }
         }

         var1.append(var6);
      }

      if (var2 == 1 || var2 == 2) {
         var1.append('\'');
      }

      return new String(var1);
   }

   private static final class AttributeAndPosition {
      private Attribute key;
      private Object value;
      private int start;
      private int limit;

      public AttributeAndPosition(Object var1, int var2, int var3) {
         this.init(MessageFormat.Field.ARGUMENT, var1, var2, var3);
      }

      public AttributeAndPosition(Attribute var1, Object var2, int var3, int var4) {
         this.init(var1, var2, var3, var4);
      }

      public void init(Attribute var1, Object var2, int var3, int var4) {
         this.key = var1;
         this.value = var2;
         this.start = var3;
         this.limit = var4;
      }

      static Attribute access$100(MessageFormat.AttributeAndPosition var0) {
         return var0.key;
      }

      static Object access$200(MessageFormat.AttributeAndPosition var0) {
         return var0.value;
      }

      static int access$300(MessageFormat.AttributeAndPosition var0) {
         return var0.start;
      }

      static int access$400(MessageFormat.AttributeAndPosition var0) {
         return var0.limit;
      }
   }

   private static final class AppendableWrapper {
      private Appendable app;
      private int length;
      private List attributes;

      public AppendableWrapper(StringBuilder var1) {
         this.app = var1;
         this.length = var1.length();
         this.attributes = null;
      }

      public AppendableWrapper(StringBuffer var1) {
         this.app = var1;
         this.length = var1.length();
         this.attributes = null;
      }

      public void useAttributes() {
         this.attributes = new ArrayList();
      }

      public void append(CharSequence var1) {
         try {
            this.app.append(var1);
            this.length += var1.length();
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }

      public void append(CharSequence var1, int var2, int var3) {
         try {
            this.app.append(var1, var2, var3);
            this.length += var3 - var2;
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      }

      public void append(CharacterIterator var1) {
         this.length += append(this.app, var1);
      }

      public static int append(Appendable var0, CharacterIterator var1) {
         try {
            int var2 = var1.getBeginIndex();
            int var3 = var1.getEndIndex();
            int var4 = var3 - var2;
            if (var2 < var3) {
               var0.append(var1.first());

               while(true) {
                  ++var2;
                  if (var2 >= var3) {
                     break;
                  }

                  var0.append(var1.next());
               }
            }

            return var4;
         } catch (IOException var5) {
            throw new RuntimeException(var5);
         }
      }

      public void formatAndAppend(Format var1, Object var2) {
         if (this.attributes == null) {
            this.append((CharSequence)var1.format(var2));
         } else {
            AttributedCharacterIterator var3 = var1.formatToCharacterIterator(var2);
            int var4 = this.length;
            this.append((CharacterIterator)var3);
            var3.first();
            int var5 = var3.getIndex();
            int var6 = var3.getEndIndex();
            int var7 = var4 - var5;

            while(var5 < var6) {
               Map var8 = var3.getAttributes();
               int var9 = var3.getRunLimit();
               if (var8.size() != 0) {
                  Iterator var10 = var8.entrySet().iterator();

                  while(var10.hasNext()) {
                     Entry var11 = (Entry)var10.next();
                     this.attributes.add(new MessageFormat.AttributeAndPosition((Attribute)var11.getKey(), var11.getValue(), var7 + var5, var7 + var9));
                  }
               }

               var5 = var9;
               var3.setIndex(var9);
            }
         }

      }

      static List access$000(MessageFormat.AppendableWrapper var0) {
         return var0.attributes;
      }

      static int access$500(MessageFormat.AppendableWrapper var0) {
         return var0.length;
      }
   }

   private static final class PluralSelectorProvider implements PluralFormat.PluralSelector {
      private ULocale locale;
      private PluralRules rules;
      private PluralRules.PluralType type;

      public PluralSelectorProvider(ULocale var1, PluralRules.PluralType var2) {
         this.locale = var1;
         this.type = var2;
      }

      public String select(double var1) {
         if (this.rules == null) {
            this.rules = PluralRules.forLocale(this.locale, this.type);
         }

         return this.rules.select(var1);
      }
   }

   public static class Field extends java.text.Format.Field {
      private static final long serialVersionUID = 7510380454602616157L;
      public static final MessageFormat.Field ARGUMENT = new MessageFormat.Field("message argument field");

      protected Field(String var1) {
         super(var1);
      }

      protected Object readResolve() throws InvalidObjectException {
         if (this.getClass() != MessageFormat.Field.class) {
            throw new InvalidObjectException("A subclass of MessageFormat.Field must implement readResolve.");
         } else if (this.getName().equals(ARGUMENT.getName())) {
            return ARGUMENT;
         } else {
            throw new InvalidObjectException("Unknown attribute name.");
         }
      }
   }
}
