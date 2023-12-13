package com.ibm.icu.text;

import com.ibm.icu.impl.BMPSet;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.RuleCharacterIterator;
import com.ibm.icu.impl.SortedSetRelation;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.UnicodeSetStringSpan;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.CharSequences;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.VersionInfo;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

public class UnicodeSet extends UnicodeFilter implements Iterable, Comparable, Freezable {
   public static final UnicodeSet EMPTY = (new UnicodeSet()).freeze();
   public static final UnicodeSet ALL_CODE_POINTS = (new UnicodeSet(0, 1114111)).freeze();
   private static UnicodeSet.XSymbolTable XSYMBOL_TABLE = null;
   private static final int LOW = 0;
   private static final int HIGH = 1114112;
   public static final int MIN_VALUE = 0;
   public static final int MAX_VALUE = 1114111;
   private int len;
   private int[] list;
   private int[] rangeList;
   private int[] buffer;
   TreeSet strings;
   private String pat;
   private static final int START_EXTRA = 16;
   private static final int GROW_EXTRA = 16;
   private static final String ANY_ID = "ANY";
   private static final String ASCII_ID = "ASCII";
   private static final String ASSIGNED = "Assigned";
   private static UnicodeSet[] INCLUSIONS = null;
   private BMPSet bmpSet;
   private UnicodeSetStringSpan stringSpan;
   private static final VersionInfo NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
   public static final int IGNORE_SPACE = 1;
   public static final int CASE = 2;
   public static final int CASE_INSENSITIVE = 2;
   public static final int ADD_CASE_MAPPINGS = 4;

   public UnicodeSet() {
      this.strings = new TreeSet();
      this.pat = null;
      this.list = new int[17];
      this.list[this.len++] = 1114112;
   }

   public UnicodeSet(UnicodeSet var1) {
      this.strings = new TreeSet();
      this.pat = null;
      this.set(var1);
   }

   public UnicodeSet(int var1, int var2) {
      this();
      this.complement(var1, var2);
   }

   public UnicodeSet(int... var1) {
      this.strings = new TreeSet();
      this.pat = null;
      if ((var1.length & 1) != 0) {
         throw new IllegalArgumentException("Must have even number of integers");
      } else {
         this.list = new int[var1.length + 1];
         this.len = this.list.length;
         int var2 = -1;

         int var10001;
         int var3;
         int var5;
         for(var3 = 0; var3 < var1.length; this.list[var10001] = var5) {
            int var4 = var1[var3];
            if (var2 >= var4) {
               throw new IllegalArgumentException("Must be monotonically increasing.");
            }

            this.list[var3++] = var4;
            var5 = var1[var3] + 1;
            if (var4 >= var5) {
               throw new IllegalArgumentException("Must be monotonically increasing.");
            }

            var10001 = var3++;
            var2 = var5;
         }

         this.list[var3] = 1114112;
      }
   }

   public UnicodeSet(String var1) {
      this();
      this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, 1);
   }

   public UnicodeSet(String var1, boolean var2) {
      this();
      this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, var2 ? 1 : 0);
   }

   public UnicodeSet(String var1, int var2) {
      this();
      this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, var2);
   }

   public UnicodeSet(String var1, ParsePosition var2, SymbolTable var3) {
      this();
      this.applyPattern((String)var1, (ParsePosition)var2, (SymbolTable)var3, 1);
   }

   public UnicodeSet(String var1, ParsePosition var2, SymbolTable var3, int var4) {
      this();
      this.applyPattern(var1, var2, var3, var4);
   }

   public Object clone() {
      UnicodeSet var1 = new UnicodeSet(this);
      var1.bmpSet = this.bmpSet;
      var1.stringSpan = this.stringSpan;
      return var1;
   }

   public UnicodeSet set(int var1, int var2) {
      this.checkFrozen();
      this.clear();
      this.complement(var1, var2);
      return this;
   }

   public UnicodeSet set(UnicodeSet var1) {
      this.checkFrozen();
      this.list = (int[])var1.list.clone();
      this.len = var1.len;
      this.pat = var1.pat;
      this.strings = new TreeSet(var1.strings);
      return this;
   }

   public final UnicodeSet applyPattern(String var1) {
      this.checkFrozen();
      return this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, 1);
   }

   public UnicodeSet applyPattern(String var1, boolean var2) {
      this.checkFrozen();
      return this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, var2 ? 1 : 0);
   }

   public UnicodeSet applyPattern(String var1, int var2) {
      this.checkFrozen();
      return this.applyPattern((String)var1, (ParsePosition)null, (SymbolTable)null, var2);
   }

   public static boolean resemblesPattern(String var0, int var1) {
      return var1 + 1 < var0.length() && var0.charAt(var1) == '[' || var0 > var1;
   }

   private static void _appendToPat(StringBuffer var0, String var1, boolean var2) {
      int var3;
      for(int var4 = 0; var4 < var1.length(); var4 += Character.charCount(var3)) {
         var3 = var1.codePointAt(var4);
         _appendToPat(var0, var3, var2);
      }

   }

   private static void _appendToPat(StringBuffer var0, int var1, boolean var2) {
      if (!var2 || !Utility.isUnprintable(var1) || !Utility.escapeUnprintable(var0, var1)) {
         switch(var1) {
         case 36:
         case 38:
         case 45:
         case 58:
         case 91:
         case 92:
         case 93:
         case 94:
         case 123:
         case 125:
            var0.append('\\');
            break;
         default:
            if (PatternProps.isWhiteSpace(var1)) {
               var0.append('\\');
            }
         }

         UTF16.append(var0, var1);
      }
   }

   public String toPattern(boolean var1) {
      StringBuffer var2 = new StringBuffer();
      return this._toPattern(var2, var1).toString();
   }

   private StringBuffer _toPattern(StringBuffer var1, boolean var2) {
      if (this.pat == null) {
         return this._generatePattern(var1, var2, true);
      } else {
         int var4 = 0;
         int var3 = 0;

         while(true) {
            while(var3 < this.pat.length()) {
               int var5 = UTF16.charAt(this.pat, var3);
               var3 += UTF16.getCharCount(var5);
               if (var2 && Utility.isUnprintable(var5)) {
                  if (var4 % 2 != 0) {
                     var1.setLength(var1.length() - 1);
                  }

                  Utility.escapeUnprintable(var1, var5);
                  var4 = 0;
               } else {
                  UTF16.append(var1, var5);
                  if (var5 == 92) {
                     ++var4;
                  } else {
                     var4 = 0;
                  }
               }
            }

            return var1;
         }
      }
   }

   public StringBuffer _generatePattern(StringBuffer var1, boolean var2) {
      return this._generatePattern(var1, var2, true);
   }

   public StringBuffer _generatePattern(StringBuffer var1, boolean var2, boolean var3) {
      var1.append('[');
      int var4 = this.getRangeCount();
      int var5;
      int var6;
      int var7;
      if (var4 > 1 && this.getRangeStart(0) == 0 && this.getRangeEnd(var4 - 1) == 1114111) {
         var1.append('^');

         for(var5 = 1; var5 < var4; ++var5) {
            var6 = this.getRangeEnd(var5 - 1) + 1;
            var7 = this.getRangeStart(var5) - 1;
            _appendToPat(var1, var6, var2);
            if (var6 != var7) {
               if (var6 + 1 != var7) {
                  var1.append('-');
               }

               _appendToPat(var1, var7, var2);
            }
         }
      } else {
         for(var5 = 0; var5 < var4; ++var5) {
            var6 = this.getRangeStart(var5);
            var7 = this.getRangeEnd(var5);
            _appendToPat(var1, var6, var2);
            if (var6 != var7) {
               if (var6 + 1 != var7) {
                  var1.append('-');
               }

               _appendToPat(var1, var7, var2);
            }
         }
      }

      if (var3 && this.strings.size() > 0) {
         Iterator var8 = this.strings.iterator();

         while(var8.hasNext()) {
            String var9 = (String)var8.next();
            var1.append('{');
            _appendToPat(var1, var9, var2);
            var1.append('}');
         }
      }

      return var1.append(']');
   }

   public int size() {
      int var1 = 0;
      int var2 = this.getRangeCount();

      for(int var3 = 0; var3 < var2; ++var3) {
         var1 += this.getRangeEnd(var3) - this.getRangeStart(var3) + 1;
      }

      return var1 + this.strings.size();
   }

   public boolean matchesIndexValue(int var1) {
      int var4;
      for(int var2 = 0; var2 < this.getRangeCount(); ++var2) {
         int var3 = this.getRangeStart(var2);
         var4 = this.getRangeEnd(var2);
         if ((var3 & -256) == (var4 & -256)) {
            if ((var3 & 255) <= var1 && var1 <= (var4 & 255)) {
               return true;
            }
         } else if ((var3 & 255) <= var1 || var1 <= (var4 & 255)) {
            return true;
         }
      }

      if (this.strings.size() != 0) {
         Iterator var5 = this.strings.iterator();

         while(var5.hasNext()) {
            String var6 = (String)var5.next();
            var4 = UTF16.charAt((String)var6, 0);
            if ((var4 & 255) == var1) {
               return true;
            }
         }
      }

      return false;
   }

   public int matches(Replaceable var1, int[] var2, int var3, boolean var4) {
      if (var2[0] == var3) {
         return var4 ? 1 : 2;
      } else {
         if (this.strings.size() != 0) {
            boolean var5 = var2[0] < var3;
            char var6 = var1.charAt(var2[0]);
            int var7 = 0;
            Iterator var8 = this.strings.iterator();

            while(var8.hasNext()) {
               String var9 = (String)var8.next();
               char var10 = var9.charAt(var5 ? 0 : var9.length() - 1);
               if (var5 && var10 > var6) {
                  break;
               }

               if (var10 == var6) {
                  int var11 = matchRest(var1, var2[0], var3, var9);
                  if (var4) {
                     int var12 = var5 ? var3 - var2[0] : var2[0] - var3;
                     if (var11 == var12) {
                        return 1;
                     }
                  }

                  if (var11 == var9.length()) {
                     if (var11 > var7) {
                        var7 = var11;
                     }

                     if (var5 && var11 < var7) {
                        break;
                     }
                  }
               }
            }

            if (var7 != 0) {
               var2[0] += var5 ? var7 : -var7;
               return 2;
            }
         }

         return super.matches(var1, var2, var3, var4);
      }
   }

   private static int matchRest(Replaceable var0, int var1, int var2, String var3) {
      int var5 = var3.length();
      int var4;
      int var6;
      if (var1 < var2) {
         var4 = var2 - var1;
         if (var4 > var5) {
            var4 = var5;
         }

         for(var6 = 1; var6 < var4; ++var6) {
            if (var0.charAt(var1 + var6) != var3.charAt(var6)) {
               return 0;
            }
         }
      } else {
         var4 = var1 - var2;
         if (var4 > var5) {
            var4 = var5;
         }

         --var5;

         for(var6 = 1; var6 < var4; ++var6) {
            if (var0.charAt(var1 - var6) != var3.charAt(var5 - var6)) {
               return 0;
            }
         }
      }

      return var4;
   }

   /** @deprecated */
   public int matchesAt(CharSequence var1, int var2) {
      int var3 = -1;
      if (this.strings.size() != 0) {
         char var4 = var1.charAt(var2);
         String var5 = null;
         Iterator var6 = this.strings.iterator();

         char var7;
         label34:
         do {
            if (!var6.hasNext()) {
               while(true) {
                  int var9 = matchesAt(var1, var2, var5);
                  if (var3 > var9) {
                     break label34;
                  }

                  var3 = var9;
                  if (!var6.hasNext()) {
                     break label34;
                  }

                  var5 = (String)var6.next();
               }
            }

            var5 = (String)var6.next();
            var7 = var5.charAt(0);
         } while(var7 < var4 || var7 <= var4);
      }

      if (var3 < 2) {
         int var8 = UTF16.charAt(var1, var2);
         if (var8 >= 0) {
            var3 = UTF16.getCharCount(var8);
         }
      }

      return var2 + var3;
   }

   private static int matchesAt(CharSequence var0, int var1, CharSequence var2) {
      int var3 = var2.length();
      int var4 = var0.length();
      if (var4 + var1 > var3) {
         return -1;
      } else {
         int var5 = 0;

         for(int var6 = var1; var5 < var3; ++var6) {
            char var7 = var2.charAt(var5);
            char var8 = var0.charAt(var6);
            if (var7 != var8) {
               return -1;
            }

            ++var5;
         }

         return var5;
      }
   }

   public void addMatchSetTo(UnicodeSet var1) {
      var1.addAll(this);
   }

   public int indexOf(int var1) {
      if (var1 >= 0 && var1 <= 1114111) {
         int var2 = 0;
         int var3 = 0;

         while(true) {
            int var4 = this.list[var2++];
            if (var1 < var4) {
               return -1;
            }

            int var5 = this.list[var2++];
            if (var1 < var5) {
               return var3 + var1 - var4;
            }

            var3 += var5 - var4;
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public int charAt(int var1) {
      if (var1 >= 0) {
         int var2 = this.len & -2;

         int var5;
         for(int var3 = 0; var3 < var2; var1 -= var5) {
            int var4 = this.list[var3++];
            var5 = this.list[var3++] - var4;
            if (var1 < var5) {
               return var4 + var1;
            }
         }
      }

      return -1;
   }

   public UnicodeSet add(int var1, int var2) {
      this.checkFrozen();
      return this.add_unchecked(var1, var2);
   }

   public UnicodeSet addAll(int var1, int var2) {
      this.checkFrozen();
      return this.add_unchecked(var1, var2);
   }

   private UnicodeSet add_unchecked(int var1, int var2) {
      if (var1 >= 0 && var1 <= 1114111) {
         if (var2 >= 0 && var2 <= 1114111) {
            if (var1 < var2) {
               this.add(this.range(var1, var2), 2, 0);
            } else if (var1 == var2) {
               this.add(var1);
            }

            return this;
         } else {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var2, 6));
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public final UnicodeSet add(int var1) {
      this.checkFrozen();
      return this.add_unchecked(var1);
   }

   private final UnicodeSet add_unchecked(int var1) {
      if (var1 >= 0 && var1 <= 1114111) {
         int var2 = this.findCodePoint(var1);
         if ((var2 & 1) != 0) {
            return this;
         } else {
            if (var1 == this.list[var2] - 1) {
               this.list[var2] = var1;
               if (var1 == 1114111) {
                  this.ensureCapacity(this.len + 1);
                  this.list[this.len++] = 1114112;
               }

               if (var2 > 0 && var1 == this.list[var2 - 1]) {
                  System.arraycopy(this.list, var2 + 1, this.list, var2 - 1, this.len - var2 - 1);
                  this.len -= 2;
               }
            } else if (var2 > 0 && var1 == this.list[var2 - 1]) {
               int var10002 = this.list[var2 - 1]++;
            } else {
               if (this.len + 2 > this.list.length) {
                  int[] var3 = new int[this.len + 2 + 16];
                  if (var2 != 0) {
                     System.arraycopy(this.list, 0, var3, 0, var2);
                  }

                  System.arraycopy(this.list, var2, var3, var2 + 2, this.len - var2);
                  this.list = var3;
               } else {
                  System.arraycopy(this.list, var2, this.list, var2 + 2, this.len - var2);
               }

               this.list[var2] = var1;
               this.list[var2 + 1] = var1 + 1;
               this.len += 2;
            }

            this.pat = null;
            return this;
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public final UnicodeSet add(CharSequence var1) {
      this.checkFrozen();
      int var2 = getSingleCP(var1);
      if (var2 < 0) {
         this.strings.add(var1.toString());
         this.pat = null;
      } else {
         this.add_unchecked(var2, var2);
      }

      return this;
   }

   private static int getSingleCP(CharSequence var0) {
      if (var0.length() < 1) {
         throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet");
      } else if (var0.length() > 2) {
         return -1;
      } else if (var0.length() == 1) {
         return var0.charAt(0);
      } else {
         int var1 = UTF16.charAt((CharSequence)var0, 0);
         return var1 > 65535 ? var1 : -1;
      }
   }

   public final UnicodeSet addAll(CharSequence var1) {
      this.checkFrozen();

      int var2;
      for(int var3 = 0; var3 < var1.length(); var3 += UTF16.getCharCount(var2)) {
         var2 = UTF16.charAt(var1, var3);
         this.add_unchecked(var2, var2);
      }

      return this;
   }

   public final UnicodeSet retainAll(String var1) {
      return this.retainAll(fromAll(var1));
   }

   public final UnicodeSet complementAll(String var1) {
      return this.complementAll(fromAll(var1));
   }

   public final UnicodeSet removeAll(String var1) {
      return this.removeAll(fromAll(var1));
   }

   public final UnicodeSet removeAllStrings() {
      this.checkFrozen();
      if (this.strings.size() != 0) {
         this.strings.clear();
         this.pat = null;
      }

      return this;
   }

   public static UnicodeSet from(String var0) {
      return (new UnicodeSet()).add((CharSequence)var0);
   }

   public static UnicodeSet fromAll(String var0) {
      return (new UnicodeSet()).addAll((CharSequence)var0);
   }

   public UnicodeSet retain(int var1, int var2) {
      this.checkFrozen();
      if (var1 >= 0 && var1 <= 1114111) {
         if (var2 >= 0 && var2 <= 1114111) {
            if (var1 <= var2) {
               this.retain(this.range(var1, var2), 2, 0);
            } else {
               this.clear();
            }

            return this;
         } else {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var2, 6));
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public final UnicodeSet retain(int var1) {
      return this.retain(var1, var1);
   }

   public final UnicodeSet retain(String var1) {
      int var2 = getSingleCP(var1);
      if (var2 < 0) {
         boolean var3 = this.strings.contains(var1);
         if (var3 && this.size() == 1) {
            return this;
         }

         this.clear();
         this.strings.add(var1);
         this.pat = null;
      } else {
         this.retain(var2, var2);
      }

      return this;
   }

   public UnicodeSet remove(int var1, int var2) {
      this.checkFrozen();
      if (var1 >= 0 && var1 <= 1114111) {
         if (var2 >= 0 && var2 <= 1114111) {
            if (var1 <= var2) {
               this.retain(this.range(var1, var2), 2, 2);
            }

            return this;
         } else {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var2, 6));
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public final UnicodeSet remove(int var1) {
      return this.remove(var1, var1);
   }

   public final UnicodeSet remove(String var1) {
      int var2 = getSingleCP(var1);
      if (var2 < 0) {
         this.strings.remove(var1);
         this.pat = null;
      } else {
         this.remove(var2, var2);
      }

      return this;
   }

   public UnicodeSet complement(int var1, int var2) {
      this.checkFrozen();
      if (var1 >= 0 && var1 <= 1114111) {
         if (var2 >= 0 && var2 <= 1114111) {
            if (var1 <= var2) {
               this.xor(this.range(var1, var2), 2, 0);
            }

            this.pat = null;
            return this;
         } else {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var2, 6));
         }
      } else {
         throw new IllegalArgumentException("Invalid code point U+" + Utility.hex((long)var1, 6));
      }
   }

   public final UnicodeSet complement(int var1) {
      return this.complement(var1, var1);
   }

   public UnicodeSet complement() {
      this.checkFrozen();
      if (this.list[0] == 0) {
         System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
         --this.len;
      } else {
         this.ensureCapacity(this.len + 1);
         System.arraycopy(this.list, 0, this.list, 1, this.len);
         this.list[0] = 0;
         ++this.len;
      }

      this.pat = null;
      return this;
   }

   public final UnicodeSet complement(String var1) {
      this.checkFrozen();
      int var2 = getSingleCP(var1);
      if (var2 < 0) {
         if (this.strings.contains(var1)) {
            this.strings.remove(var1);
         } else {
            this.strings.add(var1);
         }

         this.pat = null;
      } else {
         this.complement(var2, var2);
      }

      return this;
   }

   private final int findCodePoint(int var1) {
      if (var1 < this.list[0]) {
         return 0;
      } else if (this.len >= 2 && var1 >= this.list[this.len - 2]) {
         return this.len - 1;
      } else {
         int var2 = 0;
         int var3 = this.len - 1;

         while(true) {
            int var4 = var2 + var3 >>> 1;
            if (var4 == var2) {
               return var3;
            }

            if (var1 < this.list[var4]) {
               var3 = var4;
            } else {
               var2 = var4;
            }
         }
      }
   }

   public boolean containsAll(UnicodeSet var1) {
      int[] var2 = var1.list;
      boolean var3 = true;
      boolean var4 = true;
      int var5 = 0;
      int var6 = 0;
      int var7 = this.len - 1;
      int var8 = var1.len - 1;
      int var9 = 0;
      int var10 = 0;
      int var11 = 0;
      int var12 = 0;

      while(true) {
         label52: {
            label53: {
               if (var3) {
                  if (var5 >= var7) {
                     if (!var4 || var6 < var8) {
                        return false;
                     }
                     break label53;
                  }

                  var9 = this.list[var5++];
                  var11 = this.list[var5++];
               }

               if (!var4) {
                  break label52;
               }

               if (var6 < var8) {
                  var10 = var2[var6++];
                  var12 = var2[var6++];
                  break label52;
               }
            }

            if (!this.strings.containsAll(var1.strings)) {
               return false;
            }

            return true;
         }

         if (var10 >= var11) {
            var3 = true;
            var4 = false;
         } else {
            if (var10 < var9 || var12 > var11) {
               return false;
            }

            var3 = false;
            var4 = true;
         }
      }
   }

   public boolean containsAll(String var1) {
      int var2;
      for(int var3 = 0; var3 < var1.length(); var3 += UTF16.getCharCount(var2)) {
         var2 = UTF16.charAt(var1, var3);
         if (var2 >= 0) {
            if (this.strings.size() == 0) {
               return false;
            }

            return this.containsAll(var1, 0);
         }
      }

      return true;
   }

   /** @deprecated */
   public String getRegexEquivalent() {
      if (this.strings.size() == 0) {
         return this.toString();
      } else {
         StringBuffer var1 = new StringBuffer("(?:");
         this._generatePattern(var1, true, false);
         Iterator var2 = this.strings.iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            var1.append('|');
            _appendToPat(var1, var3, true);
         }

         return var1.append(")").toString();
      }
   }

   public final boolean containsSome(int var1, int var2) {
      return var2 >= 0;
   }

   public final boolean containsSome(UnicodeSet var1) {
      return var1 != false;
   }

   public final boolean containsSome(String var1) {
      return this == var1;
   }

   public UnicodeSet addAll(UnicodeSet var1) {
      this.checkFrozen();
      this.add(var1.list, var1.len, 0);
      this.strings.addAll(var1.strings);
      return this;
   }

   public UnicodeSet retainAll(UnicodeSet var1) {
      this.checkFrozen();
      this.retain(var1.list, var1.len, 0);
      this.strings.retainAll(var1.strings);
      return this;
   }

   public UnicodeSet removeAll(UnicodeSet var1) {
      this.checkFrozen();
      this.retain(var1.list, var1.len, 2);
      this.strings.removeAll(var1.strings);
      return this;
   }

   public UnicodeSet complementAll(UnicodeSet var1) {
      this.checkFrozen();
      this.xor(var1.list, var1.len, 0);
      SortedSetRelation.doOperation(this.strings, 5, var1.strings);
      return this;
   }

   public UnicodeSet clear() {
      this.checkFrozen();
      this.list[0] = 1114112;
      this.len = 1;
      this.pat = null;
      this.strings.clear();
      return this;
   }

   public int getRangeCount() {
      return this.len / 2;
   }

   public int getRangeStart(int var1) {
      return this.list[var1 * 2];
   }

   public int getRangeEnd(int var1) {
      return this.list[var1 * 2 + 1] - 1;
   }

   public UnicodeSet compact() {
      this.checkFrozen();
      if (this.len != this.list.length) {
         int[] var1 = new int[this.len];
         System.arraycopy(this.list, 0, var1, 0, this.len);
         this.list = var1;
      }

      this.rangeList = null;
      this.buffer = null;
      return this;
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         try {
            UnicodeSet var2 = (UnicodeSet)var1;
            if (this.len != var2.len) {
               return false;
            } else {
               for(int var3 = 0; var3 < this.len; ++var3) {
                  if (this.list[var3] != var2.list[var3]) {
                     return false;
                  }
               }

               if (!this.strings.equals(var2.strings)) {
                  return false;
               } else {
                  return true;
               }
            }
         } catch (Exception var4) {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = this.len;

      for(int var2 = 0; var2 < this.len; ++var2) {
         var1 *= 1000003;
         var1 += this.list[var2];
      }

      return var1;
   }

   public String toString() {
      return this.toPattern(true);
   }

   /** @deprecated */
   public UnicodeSet applyPattern(String var1, ParsePosition var2, SymbolTable var3, int var4) {
      boolean var5 = var2 == null;
      if (var5) {
         var2 = new ParsePosition(0);
      }

      StringBuffer var6 = new StringBuffer();
      RuleCharacterIterator var7 = new RuleCharacterIterator(var1, var3, var2);
      this.applyPattern(var7, var3, var6, var4);
      if (var7.inVariable()) {
         syntaxError(var7, "Extra chars in variable value");
      }

      this.pat = var6.toString();
      if (var5) {
         int var8 = var2.getIndex();
         if ((var4 & 1) != 0) {
            var8 = PatternProps.skipWhiteSpace(var1, var8);
         }

         if (var8 != var1.length()) {
            throw new IllegalArgumentException("Parse of \"" + var1 + "\" failed at " + var8);
         }
      }

      return this;
   }

   void applyPattern(RuleCharacterIterator var1, SymbolTable var2, StringBuffer var3, int var4) {
      int var5 = 3;
      if ((var4 & 1) != 0) {
         var5 |= 4;
      }

      StringBuffer var6 = new StringBuffer();
      StringBuffer var7 = null;
      boolean var8 = false;
      UnicodeSet var9 = null;
      Object var10 = null;
      byte var11 = 0;
      int var12 = 0;
      byte var13 = 0;
      char var14 = 0;
      boolean var15 = false;
      this.clear();

      while(var13 != 2 && !var1.atEnd()) {
         int var16 = 0;
         boolean var17 = false;
         UnicodeSet var18 = null;
         byte var19 = 0;
         if (var1 != var5) {
            var19 = 2;
         } else {
            var10 = var1.getPos(var10);
            var16 = var1.next(var5);
            var17 = var1.isEscaped();
            if (var16 == 91 && !var17) {
               if (var13 == 1) {
                  var1.setPos(var10);
                  var19 = 1;
               } else {
                  var13 = 1;
                  var6.append('[');
                  var10 = var1.getPos(var10);
                  var16 = var1.next(var5);
                  var17 = var1.isEscaped();
                  if (var16 == 94 && !var17) {
                     var15 = true;
                     var6.append('^');
                     var10 = var1.getPos(var10);
                     var16 = var1.next(var5);
                     var17 = var1.isEscaped();
                  }

                  if (var16 != 45) {
                     var1.setPos(var10);
                     continue;
                  }

                  var17 = true;
               }
            } else if (var2 != null) {
               UnicodeMatcher var20 = var2.lookupMatcher(var16);
               if (var20 != null) {
                  try {
                     var18 = (UnicodeSet)var20;
                     var19 = 3;
                  } catch (ClassCastException var22) {
                     syntaxError(var1, "Syntax error");
                  }
               }
            }
         }

         if (var19 != 0) {
            if (var11 == 1) {
               if (var14 != 0) {
                  syntaxError(var1, "Char expected after operator");
               }

               this.add_unchecked(var12, var12);
               _appendToPat(var6, var12, false);
               var14 = 0;
               boolean var23 = false;
            }

            if (var14 == '-' || var14 == '&') {
               var6.append(var14);
            }

            if (var18 == null) {
               if (var9 == null) {
                  var9 = new UnicodeSet();
               }

               var18 = var9;
            }

            switch(var19) {
            case 1:
               var18.applyPattern(var1, var2, var6, var4);
               break;
            case 2:
               var1.skipIgnored(var5);
               var18.applyPropertyPattern(var1, var6, var2);
               break;
            case 3:
               var18._toPattern(var6, false);
            }

            var8 = true;
            if (var13 == 0) {
               this.set(var18);
               var13 = 2;
               break;
            }

            switch(var14) {
            case '\u0000':
               this.addAll(var18);
               break;
            case '&':
               this.retainAll(var18);
               break;
            case '-':
               this.removeAll(var18);
            }

            var14 = 0;
            var11 = 2;
         } else {
            if (var13 == 0) {
               syntaxError(var1, "Missing '['");
            }

            if (!var17) {
               switch(var16) {
               case 36:
                  var10 = var1.getPos(var10);
                  var16 = var1.next(var5);
                  var17 = var1.isEscaped();
                  boolean var21 = var16 == 93 && !var17;
                  if (var2 == null && !var21) {
                     var16 = 36;
                     var1.setPos(var10);
                  } else {
                     if (var21 && var14 == 0) {
                        if (var11 == 1) {
                           this.add_unchecked(var12, var12);
                           _appendToPat(var6, var12, false);
                        }

                        this.add_unchecked(65535);
                        var8 = true;
                        var6.append('$').append(']');
                        var13 = 2;
                        continue;
                     }

                     syntaxError(var1, "Unquoted '$'");
                  }
                  break;
               case 38:
                  if (var11 == 2 && var14 == 0) {
                     var14 = (char)var16;
                     continue;
                  }

                  syntaxError(var1, "'&' not after set");
                  break;
               case 45:
                  if (var14 == 0) {
                     if (var11 != 0) {
                        var14 = (char)var16;
                        continue;
                     }

                     this.add_unchecked(var16, var16);
                     var16 = var1.next(var5);
                     var17 = var1.isEscaped();
                     if (var16 == 93 && !var17) {
                        var6.append("-]");
                        var13 = 2;
                        continue;
                     }
                  }

                  syntaxError(var1, "'-' not after char or set");
                  break;
               case 93:
                  if (var11 == 1) {
                     this.add_unchecked(var12, var12);
                     _appendToPat(var6, var12, false);
                  }

                  if (var14 == '-') {
                     this.add_unchecked(var14, var14);
                     var6.append(var14);
                  } else if (var14 == '&') {
                     syntaxError(var1, "Trailing '&'");
                  }

                  var6.append(']');
                  var13 = 2;
                  continue;
               case 94:
                  syntaxError(var1, "'^' not after '['");
                  break;
               case 123:
                  if (var14 != 0) {
                     syntaxError(var1, "Missing operand after operator");
                  }

                  if (var11 == 1) {
                     this.add_unchecked(var12, var12);
                     _appendToPat(var6, var12, false);
                  }

                  var11 = 0;
                  if (var7 == null) {
                     var7 = new StringBuffer();
                  } else {
                     var7.setLength(0);
                  }

                  boolean var24 = false;

                  while(!var1.atEnd()) {
                     var16 = var1.next(var5);
                     var17 = var1.isEscaped();
                     if (var16 == 125 && !var17) {
                        var24 = true;
                        break;
                     }

                     UTF16.append(var7, var16);
                  }

                  if (var7.length() < 1 || !var24) {
                     syntaxError(var1, "Invalid multicharacter string");
                  }

                  this.add((CharSequence)var7.toString());
                  var6.append('{');
                  _appendToPat(var6, var7.toString(), false);
                  var6.append('}');
                  continue;
               }
            }

            switch(var11) {
            case 0:
               var11 = 1;
               var12 = var16;
               break;
            case 1:
               if (var14 == '-') {
                  if (var12 >= var16) {
                     syntaxError(var1, "Invalid range");
                  }

                  this.add_unchecked(var12, var16);
                  _appendToPat(var6, var12, false);
                  var6.append(var14);
                  _appendToPat(var6, var16, false);
                  var14 = 0;
                  var11 = 0;
               } else {
                  this.add_unchecked(var12, var12);
                  _appendToPat(var6, var12, false);
                  var12 = var16;
               }
               break;
            case 2:
               if (var14 != 0) {
                  syntaxError(var1, "Set expected after operator");
               }

               var12 = var16;
               var11 = 1;
            }
         }
      }

      if (var13 != 2) {
         syntaxError(var1, "Missing ']'");
      }

      var1.skipIgnored(var5);
      if ((var4 & 2) != 0) {
         this.closeOver(2);
      }

      if (var15) {
         this.complement();
      }

      if (var8) {
         var3.append(var6.toString());
      } else {
         this._generatePattern(var3, false, true);
      }

   }

   private static void syntaxError(RuleCharacterIterator var0, String var1) {
      throw new IllegalArgumentException("Error: " + var1 + " at \"" + Utility.escape(var0.toString()) + '"');
   }

   public Collection addAllTo(Collection var1) {
      return addAllTo(this, (Collection)var1);
   }

   public String[] addAllTo(String[] var1) {
      return (String[])addAllTo(this, (Object[])var1);
   }

   public static String[] toArray(UnicodeSet var0) {
      return (String[])addAllTo(var0, (Object[])(new String[var0.size()]));
   }

   public UnicodeSet add(Collection var1) {
      return this.addAll(var1);
   }

   public UnicodeSet addAll(Collection var1) {
      this.checkFrozen();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         this.add((CharSequence)var3.toString());
      }

      return this;
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.list.length) {
         int[] var2 = new int[var1 + 16];
         System.arraycopy(this.list, 0, var2, 0, this.len);
         this.list = var2;
      }
   }

   private void ensureBufferCapacity(int var1) {
      if (this.buffer == null || var1 > this.buffer.length) {
         this.buffer = new int[var1 + 16];
      }
   }

   private int[] range(int var1, int var2) {
      if (this.rangeList == null) {
         this.rangeList = new int[]{var1, var2 + 1, 1114112};
      } else {
         this.rangeList[0] = var1;
         this.rangeList[1] = var2 + 1;
      }

      return this.rangeList;
   }

   private UnicodeSet xor(int[] var1, int var2, int var3) {
      this.ensureBufferCapacity(this.len + var2);
      byte var4 = 0;
      int var5 = 0;
      int var6 = 0;
      int var10 = var4 + 1;
      int var7 = this.list[var4];
      int var8;
      if (var3 != 1 && var3 != 2) {
         var8 = var1[var5++];
      } else {
         var8 = 0;
         if (var1[var5] == 0) {
            ++var5;
            var8 = var1[var5];
         }
      }

      while(true) {
         while(var7 >= var8) {
            if (var8 < var7) {
               this.buffer[var6++] = var8;
               var8 = var1[var5++];
            } else {
               if (var7 == 1114112) {
                  this.buffer[var6++] = 1114112;
                  this.len = var6;
                  int[] var9 = this.list;
                  this.list = this.buffer;
                  this.buffer = var9;
                  this.pat = null;
                  return this;
               }

               var7 = this.list[var10++];
               var8 = var1[var5++];
            }
         }

         this.buffer[var6++] = var7;
         var7 = this.list[var10++];
      }
   }

   private UnicodeSet add(int[] var1, int var2, int var3) {
      this.ensureBufferCapacity(this.len + var2);
      byte var4 = 0;
      byte var5 = 0;
      int var6 = 0;
      int var10 = var4 + 1;
      int var7 = this.list[var4];
      int var11 = var5 + 1;
      int var8 = var1[var5];

      label95:
      while(true) {
         switch(var3) {
         case 0:
            int var10000;
            if (var7 < var8) {
               if (var6 > 0 && var7 <= this.buffer[var6 - 1]) {
                  var10000 = this.list[var10];
                  --var6;
                  var7 = max(var10000, this.buffer[var6]);
               } else {
                  this.buffer[var6++] = var7;
                  var7 = this.list[var10];
               }

               ++var10;
               var3 ^= 1;
            } else if (var8 < var7) {
               if (var6 > 0 && var8 <= this.buffer[var6 - 1]) {
                  var10000 = var1[var11];
                  --var6;
                  var8 = max(var10000, this.buffer[var6]);
               } else {
                  this.buffer[var6++] = var8;
                  var8 = var1[var11];
               }

               ++var11;
               var3 ^= 2;
            } else {
               if (var7 == 1114112) {
                  break label95;
               }

               if (var6 > 0 && var7 <= this.buffer[var6 - 1]) {
                  var10000 = this.list[var10];
                  --var6;
                  var7 = max(var10000, this.buffer[var6]);
               } else {
                  this.buffer[var6++] = var7;
                  var7 = this.list[var10];
               }

               ++var10;
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 1:
            if (var7 < var8) {
               this.buffer[var6++] = var7;
               var7 = this.list[var10++];
               var3 ^= 1;
            } else {
               if (var8 < var7) {
                  var8 = var1[var11++];
                  var3 ^= 2;
                  continue;
               }

               if (var7 == 1114112) {
                  break label95;
               }

               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 2:
            if (var8 < var7) {
               this.buffer[var6++] = var8;
               var8 = var1[var11++];
               var3 ^= 2;
            } else {
               if (var7 < var8) {
                  var7 = this.list[var10++];
                  var3 ^= 1;
                  continue;
               }

               if (var7 == 1114112) {
                  break label95;
               }

               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 3:
            if (var8 <= var7) {
               if (var7 == 1114112) {
                  break label95;
               }

               this.buffer[var6++] = var7;
            } else {
               if (var8 == 1114112) {
                  break label95;
               }

               this.buffer[var6++] = var8;
            }

            var7 = this.list[var10++];
            var3 ^= 1;
            var8 = var1[var11++];
            var3 ^= 2;
         }
      }

      this.buffer[var6++] = 1114112;
      this.len = var6;
      int[] var9 = this.list;
      this.list = this.buffer;
      this.buffer = var9;
      this.pat = null;
      return this;
   }

   private UnicodeSet retain(int[] var1, int var2, int var3) {
      this.ensureBufferCapacity(this.len + var2);
      byte var4 = 0;
      byte var5 = 0;
      int var6 = 0;
      int var10 = var4 + 1;
      int var7 = this.list[var4];
      int var11 = var5 + 1;
      int var8 = var1[var5];

      label64:
      while(true) {
         switch(var3) {
         case 0:
            if (var7 < var8) {
               var7 = this.list[var10++];
               var3 ^= 1;
            } else {
               if (var8 < var7) {
                  var8 = var1[var11++];
                  var3 ^= 2;
                  continue;
               }

               if (var7 == 1114112) {
                  break label64;
               }

               this.buffer[var6++] = var7;
               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 1:
            if (var7 < var8) {
               var7 = this.list[var10++];
               var3 ^= 1;
            } else {
               if (var8 < var7) {
                  this.buffer[var6++] = var8;
                  var8 = var1[var11++];
                  var3 ^= 2;
                  continue;
               }

               if (var7 == 1114112) {
                  break label64;
               }

               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 2:
            if (var8 < var7) {
               var8 = var1[var11++];
               var3 ^= 2;
            } else {
               if (var7 < var8) {
                  this.buffer[var6++] = var7;
                  var7 = this.list[var10++];
                  var3 ^= 1;
                  continue;
               }

               if (var7 == 1114112) {
                  break label64;
               }

               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
            break;
         case 3:
            if (var7 < var8) {
               this.buffer[var6++] = var7;
               var7 = this.list[var10++];
               var3 ^= 1;
            } else if (var8 < var7) {
               this.buffer[var6++] = var8;
               var8 = var1[var11++];
               var3 ^= 2;
            } else {
               if (var7 == 1114112) {
                  break label64;
               }

               this.buffer[var6++] = var7;
               var7 = this.list[var10++];
               var3 ^= 1;
               var8 = var1[var11++];
               var3 ^= 2;
            }
         }
      }

      this.buffer[var6++] = 1114112;
      this.len = var6;
      int[] var9 = this.list;
      this.list = this.buffer;
      this.buffer = var9;
      this.pat = null;
      return this;
   }

   private static final int max(int var0, int var1) {
      return var0 > var1 ? var0 : var1;
   }

   private static synchronized UnicodeSet getInclusions(int var0) {
      if (INCLUSIONS == null) {
         INCLUSIONS = new UnicodeSet[12];
      }

      if (INCLUSIONS[var0] == null) {
         UnicodeSet var1 = new UnicodeSet();
         switch(var0) {
         case 1:
            UCharacterProperty.INSTANCE.addPropertyStarts(var1);
            break;
         case 2:
            UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(var1);
            break;
         case 3:
         default:
            throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + var0 + ")");
         case 4:
            UCaseProps.INSTANCE.addPropertyStarts(var1);
            break;
         case 5:
            UBiDiProps.INSTANCE.addPropertyStarts(var1);
            break;
         case 6:
            UCharacterProperty.INSTANCE.addPropertyStarts(var1);
            UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(var1);
            break;
         case 7:
            Norm2AllModes.getNFCInstance().impl.addPropertyStarts(var1);
            UCaseProps.INSTANCE.addPropertyStarts(var1);
            break;
         case 8:
            Norm2AllModes.getNFCInstance().impl.addPropertyStarts(var1);
            break;
         case 9:
            Norm2AllModes.getNFKCInstance().impl.addPropertyStarts(var1);
            break;
         case 10:
            Norm2AllModes.getNFKC_CFInstance().impl.addPropertyStarts(var1);
            break;
         case 11:
            Norm2AllModes.getNFCInstance().impl.addCanonIterPropertyStarts(var1);
         }

         INCLUSIONS[var0] = var1;
      }

      return INCLUSIONS[var0];
   }

   private UnicodeSet applyFilter(UnicodeSet.Filter var1, int var2) {
      this.clear();
      int var3 = -1;
      UnicodeSet var4 = getInclusions(var2);
      int var5 = var4.getRangeCount();

      for(int var6 = 0; var6 < var5; ++var6) {
         int var7 = var4.getRangeStart(var6);
         int var8 = var4.getRangeEnd(var6);

         for(int var9 = var7; var9 <= var8; ++var9) {
            if (var1.contains(var9)) {
               if (var3 < 0) {
                  var3 = var9;
               }
            } else if (var3 >= 0) {
               this.add_unchecked(var3, var9 - 1);
               var3 = -1;
            }
         }
      }

      if (var3 >= 0) {
         this.add_unchecked(var3, 1114111);
      }

      return this;
   }

   private static String mungeCharName(String var0) {
      var0 = PatternProps.trimWhiteSpace(var0);
      StringBuilder var1 = null;

      for(int var2 = 0; var2 < var0.length(); ++var2) {
         char var3 = var0.charAt(var2);
         if (PatternProps.isWhiteSpace(var3)) {
            if (var1 == null) {
               var1 = (new StringBuilder()).append(var0, 0, var2);
            } else if (var1.charAt(var1.length() - 1) == ' ') {
               continue;
            }

            var3 = ' ';
         }

         if (var1 != null) {
            var1.append(var3);
         }
      }

      return var1 == null ? var0 : var1.toString();
   }

   public UnicodeSet applyIntPropertyValue(int var1, int var2) {
      this.checkFrozen();
      if (var1 == 8192) {
         this.applyFilter(new UnicodeSet.GeneralCategoryMaskFilter(var2), 1);
      } else if (var1 == 28672) {
         this.applyFilter(new UnicodeSet.ScriptExtensionsFilter(var2), 2);
      } else {
         this.applyFilter(new UnicodeSet.IntPropertyFilter(var1, var2), UCharacterProperty.INSTANCE.getSource(var1));
      }

      return this;
   }

   public UnicodeSet applyPropertyAlias(String var1, String var2) {
      return this.applyPropertyAlias(var1, var2, (SymbolTable)null);
   }

   public UnicodeSet applyPropertyAlias(String param1, String param2, SymbolTable param3) {
      // $FF: Couldn't be decompiled
   }

   private UnicodeSet applyPropertyPattern(String var1, ParsePosition var2, SymbolTable var3) {
      int var4 = var2.getIndex();
      if (var4 + 5 > var1.length()) {
         return null;
      } else {
         boolean var5 = false;
         boolean var6 = false;
         boolean var7 = false;
         if (var1.regionMatches(var4, "[:", 0, 2)) {
            var5 = true;
            var4 = PatternProps.skipWhiteSpace(var1, var4 + 2);
            if (var4 < var1.length() && var1.charAt(var4) == '^') {
               ++var4;
               var7 = true;
            }
         } else {
            if (!var1.regionMatches(true, var4, "\\p", 0, 2) && !var1.regionMatches(var4, "\\N", 0, 2)) {
               return null;
            }

            char var8 = var1.charAt(var4 + 1);
            var7 = var8 == 'P';
            var6 = var8 == 'N';
            var4 = PatternProps.skipWhiteSpace(var1, var4 + 2);
            if (var4 == var1.length() || var1.charAt(var4++) != '{') {
               return null;
            }
         }

         int var12 = var1.indexOf(var5 ? ":]" : "}", var4);
         if (var12 < 0) {
            return null;
         } else {
            int var9 = var1.indexOf(61, var4);
            String var10;
            String var11;
            if (var9 >= 0 && var9 < var12 && !var6) {
               var10 = var1.substring(var4, var9);
               var11 = var1.substring(var9 + 1, var12);
            } else {
               var10 = var1.substring(var4, var12);
               var11 = "";
               if (var6) {
                  var11 = var10;
                  var10 = "na";
               }
            }

            this.applyPropertyAlias(var10, var11, var3);
            if (var7) {
               this.complement();
            }

            var2.setIndex(var12 + (var5 ? 2 : 1));
            return this;
         }
      }
   }

   private void applyPropertyPattern(RuleCharacterIterator var1, StringBuffer var2, SymbolTable var3) {
      String var4 = var1.lookahead();
      ParsePosition var5 = new ParsePosition(0);
      this.applyPropertyPattern(var4, var5, var3);
      if (var5.getIndex() == 0) {
         syntaxError(var1, "Invalid property pattern");
      }

      var1.jumpahead(var5.getIndex());
      var2.append(var4.substring(0, var5.getIndex()));
   }

   private static final void addCaseMapping(UnicodeSet var0, int var1, StringBuilder var2) {
      if (var1 >= 0) {
         if (var1 > 31) {
            var0.add(var1);
         } else {
            var0.add((CharSequence)var2.toString());
            var2.setLength(0);
         }
      }

   }

   public UnicodeSet closeOver(int var1) {
      this.checkFrozen();
      if ((var1 & 6) != 0) {
         UCaseProps var2 = UCaseProps.INSTANCE;
         UnicodeSet var3 = new UnicodeSet(this);
         ULocale var4 = ULocale.ROOT;
         if ((var1 & 2) != 0) {
            var3.strings.clear();
         }

         int var5 = this.getRangeCount();
         StringBuilder var7 = new StringBuilder();
         int[] var8 = new int[1];

         for(int var9 = 0; var9 < var5; ++var9) {
            int var10 = this.getRangeStart(var9);
            int var11 = this.getRangeEnd(var9);
            int var12;
            if ((var1 & 2) != 0) {
               for(var12 = var10; var12 <= var11; ++var12) {
                  var2.addCaseClosure(var12, var3);
               }
            } else {
               for(var12 = var10; var12 <= var11; ++var12) {
                  int var6 = var2.toFullLower(var12, (UCaseProps.ContextIterator)null, var7, var4, var8);
                  addCaseMapping(var3, var6, var7);
                  var6 = var2.toFullTitle(var12, (UCaseProps.ContextIterator)null, var7, var4, var8);
                  addCaseMapping(var3, var6, var7);
                  var6 = var2.toFullUpper(var12, (UCaseProps.ContextIterator)null, var7, var4, var8);
                  addCaseMapping(var3, var6, var7);
                  var6 = var2.toFullFolding(var12, var7, 0);
                  addCaseMapping(var3, var6, var7);
               }
            }
         }

         if (!this.strings.isEmpty()) {
            String var17;
            if ((var1 & 2) != 0) {
               Iterator var14 = this.strings.iterator();

               while(var14.hasNext()) {
                  String var16 = (String)var14.next();
                  var17 = UCharacter.foldCase(var16, 0);
                  if (!var2.addStringCaseClosure(var17, var3)) {
                     var3.add((CharSequence)var17);
                  }
               }
            } else {
               BreakIterator var13 = BreakIterator.getWordInstance(var4);
               Iterator var15 = this.strings.iterator();

               while(var15.hasNext()) {
                  var17 = (String)var15.next();
                  var3.add((CharSequence)UCharacter.toLowerCase(var4, var17));
                  var3.add((CharSequence)UCharacter.toTitleCase(var4, var17, var13));
                  var3.add((CharSequence)UCharacter.toUpperCase(var4, var17));
                  var3.add((CharSequence)UCharacter.foldCase(var17, 0));
               }
            }
         }

         this.set(var3);
      }

      return this;
   }

   public UnicodeSet freeze() {
      if (this == null) {
         this.buffer = null;
         if (this.list.length > this.len + 16) {
            int var1 = this.len == 0 ? 1 : this.len;
            int[] var2 = this.list;
            this.list = new int[var1];

            for(int var3 = var1; var3-- > 0; this.list[var3] = var2[var3]) {
            }
         }

         if (!this.strings.isEmpty()) {
            this.stringSpan = new UnicodeSetStringSpan(this, new ArrayList(this.strings), 63);
            if (!this.stringSpan.needsStringSpanUTF16()) {
               this.stringSpan = null;
            }
         }

         if (this.stringSpan == null) {
            this.bmpSet = new BMPSet(this.list, this.len);
         }
      }

      return this;
   }

   public int span(CharSequence var1, UnicodeSet.SpanCondition var2) {
      return this.span(var1, 0, var2);
   }

   public int span(CharSequence var1, int var2, UnicodeSet.SpanCondition var3) {
      int var4 = var1.length();
      if (var2 < 0) {
         var2 = 0;
      } else if (var2 >= var4) {
         return var4;
      }

      if (this.bmpSet != null) {
         return var2 + this.bmpSet.span(var1, var2, var4, var3);
      } else {
         int var5 = var4 - var2;
         if (this.stringSpan != null) {
            return var2 + this.stringSpan.span(var1, var2, var5, var3);
         } else {
            if (!this.strings.isEmpty()) {
               int var6 = var3 == UnicodeSet.SpanCondition.NOT_CONTAINED ? 41 : 42;
               UnicodeSetStringSpan var7 = new UnicodeSetStringSpan(this, new ArrayList(this.strings), var6);
               if (var7.needsStringSpanUTF16()) {
                  return var2 + var7.span(var1, var2, var5, var3);
               }
            }

            boolean var9 = var3 != UnicodeSet.SpanCondition.NOT_CONTAINED;
            int var8 = var2;

            do {
               int var10 = Character.codePointAt(var1, var8);
               if (var10 >= 0) {
                  break;
               }

               var8 = Character.offsetByCodePoints(var1, var8, 1);
            } while(var8 < var4);

            return var8;
         }
      }
   }

   public int spanBack(CharSequence var1, UnicodeSet.SpanCondition var2) {
      return this.spanBack(var1, var1.length(), var2);
   }

   public int spanBack(CharSequence var1, int var2, UnicodeSet.SpanCondition var3) {
      if (var2 <= 0) {
         return 0;
      } else {
         if (var2 > var1.length()) {
            var2 = var1.length();
         }

         if (this.bmpSet != null) {
            return this.bmpSet.spanBack(var1, var2, var3);
         } else if (this.stringSpan != null) {
            return this.stringSpan.spanBack(var1, var2, var3);
         } else {
            if (!this.strings.isEmpty()) {
               int var4 = var3 == UnicodeSet.SpanCondition.NOT_CONTAINED ? 25 : 26;
               UnicodeSetStringSpan var5 = new UnicodeSetStringSpan(this, new ArrayList(this.strings), var4);
               if (var5.needsStringSpanUTF16()) {
                  return var5.spanBack(var1, var2, var3);
               }
            }

            boolean var7 = var3 != UnicodeSet.SpanCondition.NOT_CONTAINED;
            int var6 = var2;

            do {
               int var8 = Character.codePointBefore(var1, var6);
               if (var8 >= 0) {
                  break;
               }

               var6 = Character.offsetByCodePoints(var1, var6, -1);
            } while(var6 > 0);

            return var6;
         }
      }
   }

   public UnicodeSet cloneAsThawed() {
      UnicodeSet var1 = (UnicodeSet)this.clone();
      var1.bmpSet = null;
      var1.stringSpan = null;
      return var1;
   }

   private void checkFrozen() {
      if (this == null) {
         throw new UnsupportedOperationException("Attempt to modify frozen object");
      }
   }

   public Iterator iterator() {
      return new UnicodeSet.UnicodeSetIterator2(this);
   }

   public boolean containsAll(Collection var1) {
      Iterator var2 = var1.iterator();

      String var3;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         var3 = (String)var2.next();
      } while(!(var3 < 0));

      return false;
   }

   public final boolean containsSome(Collection var1) {
      return var1 != false;
   }

   public UnicodeSet addAll(String... var1) {
      this.checkFrozen();
      String[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         this.add((CharSequence)var5);
      }

      return this;
   }

   public UnicodeSet removeAll(Collection var1) {
      this.checkFrozen();
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         this.remove(var3);
      }

      return this;
   }

   public UnicodeSet retainAll(Collection var1) {
      this.checkFrozen();
      UnicodeSet var2 = new UnicodeSet();
      var2.addAll(var1);
      this.retainAll(var2);
      return this;
   }

   public int compareTo(UnicodeSet var1) {
      return this.compareTo(var1, UnicodeSet.ComparisonStyle.SHORTER_FIRST);
   }

   public int compareTo(UnicodeSet var1, UnicodeSet.ComparisonStyle var2) {
      int var3;
      if (var2 != UnicodeSet.ComparisonStyle.LEXICOGRAPHIC) {
         var3 = this.size() - var1.size();
         if (var3 != 0) {
            return var3 < 0 == (var2 == UnicodeSet.ComparisonStyle.SHORTER_FIRST) ? -1 : 1;
         }
      }

      int var4;
      for(var4 = 0; 0 == (var3 = this.list[var4] - var1.list[var4]); ++var4) {
         if (this.list[var4] == 1114112) {
            return compare((Iterable)this.strings, (Iterable)var1.strings);
         }
      }

      String var5;
      if (this.list[var4] == 1114112) {
         if (this.strings.isEmpty()) {
            return 1;
         } else {
            var5 = (String)this.strings.first();
            return compare(var5, var1.list[var4]);
         }
      } else if (var1.list[var4] == 1114112) {
         if (var1.strings.isEmpty()) {
            return -1;
         } else {
            var5 = (String)var1.strings.first();
            return -compare(var5, this.list[var4]);
         }
      } else {
         return (var4 & 1) == 0 ? var3 : -var3;
      }
   }

   public int compareTo(Iterable var1) {
      return compare((Iterable)this, (Iterable)var1);
   }

   public static int compare(String var0, int var1) {
      return CharSequences.compare(var0, var1);
   }

   public static int compare(int var0, String var1) {
      return -CharSequences.compare(var1, var0);
   }

   public static int compare(Iterable var0, Iterable var1) {
      return compare(var0.iterator(), var1.iterator());
   }

   /** @deprecated */
   public static int compare(Iterator var0, Iterator var1) {
      int var4;
      do {
         if (!var0.hasNext()) {
            return var1.hasNext() ? -1 : 0;
         }

         if (!var1.hasNext()) {
            return 1;
         }

         Comparable var2 = (Comparable)var0.next();
         Comparable var3 = (Comparable)var1.next();
         var4 = var2.compareTo(var3);
      } while(var4 == 0);

      return var4;
   }

   public static int compare(Collection var0, Collection var1, UnicodeSet.ComparisonStyle var2) {
      if (var2 != UnicodeSet.ComparisonStyle.LEXICOGRAPHIC) {
         int var3 = var0.size() - var1.size();
         if (var3 != 0) {
            return var3 < 0 == (var2 == UnicodeSet.ComparisonStyle.SHORTER_FIRST) ? -1 : 1;
         }
      }

      return compare((Iterable)var0, (Iterable)var1);
   }

   public static Collection addAllTo(Iterable var0, Collection var1) {
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         var1.add(var3);
      }

      return var1;
   }

   public static Object[] addAllTo(Iterable var0, Object[] var1) {
      int var2 = 0;

      Object var4;
      for(Iterator var3 = var0.iterator(); var3.hasNext(); var1[var2++] = var4) {
         var4 = var3.next();
      }

      return var1;
   }

   public Iterable strings() {
      return Collections.unmodifiableSortedSet(this.strings);
   }

   /** @deprecated */
   public static int getSingleCodePoint(CharSequence var0) {
      return CharSequences.getSingleCodePoint(var0);
   }

   /** @deprecated */
   public UnicodeSet addBridges(UnicodeSet var1) {
      UnicodeSet var2 = (new UnicodeSet(this)).complement();
      UnicodeSetIterator var3 = new UnicodeSetIterator(var2);

      while(var3.nextRange()) {
         if (var3.codepoint != 0 && var3.codepoint != UnicodeSetIterator.IS_STRING && var3.codepointEnd != 1114111) {
            int var10001 = var3.codepoint;
            if (var3.codepointEnd >= 0) {
               this.add(var3.codepoint, var3.codepointEnd);
            }
         }
      }

      return this;
   }

   /** @deprecated */
   public int findIn(CharSequence var1, int var2, boolean var3) {
      while(true) {
         if (var2 < var1.length()) {
            int var4 = UTF16.charAt(var1, var2);
            if (this.contains(var4) == var3) {
               var2 += UTF16.getCharCount(var4);
               continue;
            }
         }

         return var2;
      }
   }

   /** @deprecated */
   public int findLastIn(CharSequence var1, int var2, boolean var3) {
      --var2;

      while(var2 >= 0) {
         int var4 = UTF16.charAt(var1, var2);
         if (this.contains(var4) != var3) {
            break;
         }

         var2 -= UTF16.getCharCount(var4);
      }

      return var2 < 0 ? -1 : var2;
   }

   /** @deprecated */
   public String stripFrom(CharSequence var1, boolean var2) {
      StringBuilder var3 = new StringBuilder();

      int var5;
      for(int var4 = 0; var4 < var1.length(); var4 = this.findIn(var1, var5, var2)) {
         var5 = this.findIn(var1, var4, !var2);
         var3.append(var1.subSequence(var4, var5));
      }

      return var3.toString();
   }

   public static UnicodeSet.XSymbolTable getDefaultXSymbolTable() {
      return XSYMBOL_TABLE;
   }

   public static void setDefaultXSymbolTable(UnicodeSet.XSymbolTable var0) {
      XSYMBOL_TABLE = var0;
   }

   public int compareTo(Object var1) {
      return this.compareTo((UnicodeSet)var1);
   }

   public Object cloneAsThawed() {
      return this.cloneAsThawed();
   }

   public Object freeze() {
      return this.freeze();
   }

   static VersionInfo access$000() {
      return NO_VERSION;
   }

   static int access$100(UnicodeSet var0) {
      return var0.len;
   }

   static int[] access$200(UnicodeSet var0) {
      return var0.list;
   }

   public static enum SpanCondition {
      NOT_CONTAINED,
      CONTAINED,
      SIMPLE,
      CONDITION_COUNT;

      private static final UnicodeSet.SpanCondition[] $VALUES = new UnicodeSet.SpanCondition[]{NOT_CONTAINED, CONTAINED, SIMPLE, CONDITION_COUNT};
   }

   public static enum ComparisonStyle {
      SHORTER_FIRST,
      LEXICOGRAPHIC,
      LONGER_FIRST;

      private static final UnicodeSet.ComparisonStyle[] $VALUES = new UnicodeSet.ComparisonStyle[]{SHORTER_FIRST, LEXICOGRAPHIC, LONGER_FIRST};
   }

   private static class UnicodeSetIterator2 implements Iterator {
      private int[] sourceList;
      private int len;
      private int item;
      private int current;
      private int limit;
      private TreeSet sourceStrings;
      private Iterator stringIterator;
      private char[] buffer;

      UnicodeSetIterator2(UnicodeSet var1) {
         this.len = UnicodeSet.access$100(var1) - 1;
         if (this.item >= this.len) {
            this.stringIterator = var1.strings.iterator();
            this.sourceList = null;
         } else {
            this.sourceStrings = var1.strings;
            this.sourceList = UnicodeSet.access$200(var1);
            this.current = this.sourceList[this.item++];
            this.limit = this.sourceList[this.item++];
         }

      }

      public boolean hasNext() {
         return this.sourceList != null || this.stringIterator.hasNext();
      }

      public String next() {
         if (this.sourceList == null) {
            return (String)this.stringIterator.next();
         } else {
            int var1 = this.current++;
            if (this.current >= this.limit) {
               if (this.item >= this.len) {
                  this.stringIterator = this.sourceStrings.iterator();
                  this.sourceList = null;
               } else {
                  this.current = this.sourceList[this.item++];
                  this.limit = this.sourceList[this.item++];
               }
            }

            if (var1 <= 65535) {
               return String.valueOf((char)var1);
            } else {
               if (this.buffer == null) {
                  this.buffer = new char[2];
               }

               int var2 = var1 - 65536;
               this.buffer[0] = (char)((var2 >>> 10) + '\ud800');
               this.buffer[1] = (char)((var2 & 1023) + '\udc00');
               return String.valueOf(this.buffer);
            }
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      public Object next() {
         return this.next();
      }
   }

   public abstract static class XSymbolTable implements SymbolTable {
      public UnicodeMatcher lookupMatcher(int var1) {
         return null;
      }

      public boolean applyPropertyAlias(String var1, String var2, UnicodeSet var3) {
         return false;
      }

      public char[] lookup(String var1) {
         return null;
      }

      public String parseReference(String var1, ParsePosition var2, int var3) {
         return null;
      }
   }

   private static class VersionFilter implements UnicodeSet.Filter {
      VersionInfo version;

      VersionFilter(VersionInfo var1) {
         this.version = var1;
      }

      public boolean contains(int var1) {
         VersionInfo var2 = UCharacter.getAge(var1);
         return var2 != UnicodeSet.access$000() && var2.compareTo(this.version) <= 0;
      }
   }

   private static class ScriptExtensionsFilter implements UnicodeSet.Filter {
      int script;

      ScriptExtensionsFilter(int var1) {
         this.script = var1;
      }

      public boolean contains(int var1) {
         return UScript.hasScript(var1, this.script);
      }
   }

   private static class IntPropertyFilter implements UnicodeSet.Filter {
      int prop;
      int value;

      IntPropertyFilter(int var1, int var2) {
         this.prop = var1;
         this.value = var2;
      }

      public boolean contains(int var1) {
         return UCharacter.getIntPropertyValue(var1, this.prop) == this.value;
      }
   }

   private static class GeneralCategoryMaskFilter implements UnicodeSet.Filter {
      int mask;

      GeneralCategoryMaskFilter(int var1) {
         this.mask = var1;
      }

      public boolean contains(int var1) {
         return (1 << UCharacter.getType(var1) & this.mask) != 0;
      }
   }

   private static class NumericValueFilter implements UnicodeSet.Filter {
      double value;

      NumericValueFilter(double var1) {
         this.value = var1;
      }

      public boolean contains(int var1) {
         return UCharacter.getUnicodeNumericValue(var1) == this.value;
      }
   }

   private interface Filter {
      boolean contains(int var1);
   }
}
