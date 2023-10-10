package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public final class UCaseProps {
   private static final byte[] flagsOffset = new byte[]{0, 1, 1, 2, 1, 2, 2, 3, 1, 2, 2, 3, 2, 3, 3, 4, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 1, 2, 2, 3, 2, 3, 3, 4, 2, 3, 3, 4, 3, 4, 4, 5, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 2, 3, 3, 4, 3, 4, 4, 5, 3, 4, 4, 5, 4, 5, 5, 6, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 3, 4, 4, 5, 4, 5, 5, 6, 4, 5, 5, 6, 5, 6, 6, 7, 4, 5, 5, 6, 5, 6, 6, 7, 5, 6, 6, 7, 6, 7, 7, 8};
   public static final int MAX_STRING_LENGTH = 31;
   private static final int LOC_UNKNOWN = 0;
   private static final int LOC_ROOT = 1;
   private static final int LOC_TURKISH = 2;
   private static final int LOC_LITHUANIAN = 3;
   private static final String iDot = "i̇";
   private static final String jDot = "j̇";
   private static final String iOgonekDot = "į̇";
   private static final String iDotGrave = "i̇̀";
   private static final String iDotAcute = "i̇́";
   private static final String iDotTilde = "i̇̃";
   private static final int FOLD_CASE_OPTIONS_MASK = 255;
   private static final int[] rootLocCache = new int[]{1};
   public static final StringBuilder dummyStringBuilder = new StringBuilder();
   private int[] indexes;
   private char[] exceptions;
   private char[] unfold;
   private Trie2_16 trie;
   private static final String DATA_NAME = "ucase";
   private static final String DATA_TYPE = "icu";
   private static final String DATA_FILE_NAME = "ucase.icu";
   private static final byte[] FMT = new byte[]{99, 65, 83, 69};
   private static final int IX_TRIE_SIZE = 2;
   private static final int IX_EXC_LENGTH = 3;
   private static final int IX_UNFOLD_LENGTH = 4;
   private static final int IX_TOP = 16;
   public static final int TYPE_MASK = 3;
   public static final int NONE = 0;
   public static final int LOWER = 1;
   public static final int UPPER = 2;
   public static final int TITLE = 3;
   private static final int SENSITIVE = 8;
   private static final int EXCEPTION = 16;
   private static final int DOT_MASK = 96;
   private static final int SOFT_DOTTED = 32;
   private static final int ABOVE = 64;
   private static final int OTHER_ACCENT = 96;
   private static final int DELTA_SHIFT = 7;
   private static final int EXC_SHIFT = 5;
   private static final int EXC_LOWER = 0;
   private static final int EXC_FOLD = 1;
   private static final int EXC_UPPER = 2;
   private static final int EXC_TITLE = 3;
   private static final int EXC_CLOSURE = 6;
   private static final int EXC_FULL_MAPPINGS = 7;
   private static final int EXC_DOUBLE_SLOTS = 256;
   private static final int EXC_DOT_SHIFT = 7;
   private static final int EXC_CONDITIONAL_SPECIAL = 16384;
   private static final int EXC_CONDITIONAL_FOLD = 32768;
   private static final int FULL_LOWER = 15;
   private static final int CLOSURE_MAX_LENGTH = 15;
   private static final int UNFOLD_ROWS = 0;
   private static final int UNFOLD_ROW_WIDTH = 1;
   private static final int UNFOLD_STRING_WIDTH = 2;
   public static final UCaseProps INSTANCE;

   private UCaseProps() throws IOException {
      InputStream var1 = ICUData.getRequiredStream("data/icudt51b/ucase.icu");
      BufferedInputStream var2 = new BufferedInputStream(var1, 4096);
      this.readData(var2);
      var2.close();
      var1.close();
   }

   private final void readData(InputStream var1) throws IOException {
      DataInputStream var2 = new DataInputStream(var1);
      ICUBinary.readHeader(var2, FMT, new UCaseProps.IsAcceptable());
      int var4 = var2.readInt();
      if (var4 < 16) {
         throw new IOException("indexes[0] too small in ucase.icu");
      } else {
         this.indexes = new int[var4];
         this.indexes[0] = var4;

         int var3;
         for(var3 = 1; var3 < var4; ++var3) {
            this.indexes[var3] = var2.readInt();
         }

         this.trie = Trie2_16.createFromSerialized(var2);
         int var5 = this.indexes[2];
         int var6 = this.trie.getSerializedLength();
         if (var6 > var5) {
            throw new IOException("ucase.icu: not enough bytes for the trie");
         } else {
            var2.skipBytes(var5 - var6);
            var4 = this.indexes[3];
            if (var4 > 0) {
               this.exceptions = new char[var4];

               for(var3 = 0; var3 < var4; ++var3) {
                  this.exceptions[var3] = var2.readChar();
               }
            }

            var4 = this.indexes[4];
            if (var4 > 0) {
               this.unfold = new char[var4];

               for(var3 = 0; var3 < var4; ++var3) {
                  this.unfold[var3] = var2.readChar();
               }
            }

         }
      }
   }

   public final void addPropertyStarts(UnicodeSet var1) {
      Iterator var2 = this.trie.iterator();

      Trie2.Range var3;
      while(var2.hasNext() && !(var3 = (Trie2.Range)var2.next()).leadSurrogate) {
         var1.add(var3.startCodePoint);
      }

   }

   private static final int getExceptionsOffset(int var0) {
      return var0 >> 5;
   }

   private static final byte slotOffset(int var0, int var1) {
      return flagsOffset[var0 & (1 << var1) - 1];
   }

   private final long getSlotValueAndOffset(int var1, int var2, int var3) {
      long var4;
      if ((var1 & 256) == 0) {
         var3 += slotOffset(var1, var2);
         var4 = (long)this.exceptions[var3];
      } else {
         var3 += 2 * slotOffset(var1, var2);
         var4 = (long)this.exceptions[var3++];
         var4 = var4 << 16 | (long)this.exceptions[var3];
      }

      return var4 | (long)var3 << 32;
   }

   private final int getSlotValue(int var1, int var2, int var3) {
      int var4;
      if ((var1 & 256) == 0) {
         var3 += slotOffset(var1, var2);
         var4 = this.exceptions[var3];
      } else {
         var3 += 2 * slotOffset(var1, var2);
         char var5 = this.exceptions[var3++];
         var4 = var5 << 16 | this.exceptions[var3];
      }

      return var4;
   }

   public final int tolower(int var1) {
      int var2 = this.trie.get(var1);
      if (var2 != 0) {
         if (getTypeFromProps(var2) >= 2) {
            var1 += getDelta(var2);
         }
      } else {
         int var3 = getExceptionsOffset(var2);
         char var4 = this.exceptions[var3++];
      }

      return var1;
   }

   public final int toupper(int var1) {
      int var2 = this.trie.get(var1);
      if (var2 != 0) {
         if (getTypeFromProps(var2) == 1) {
            var1 += getDelta(var2);
         }
      } else {
         int var3 = getExceptionsOffset(var2);
         char var4 = this.exceptions[var3++];
      }

      return var1;
   }

   public final int totitle(int var1) {
      int var2 = this.trie.get(var1);
      if (var2 != 0) {
         if (getTypeFromProps(var2) == 1) {
            var1 += getDelta(var2);
         }

         return var1;
      } else {
         int var3 = getExceptionsOffset(var2);
         char var4 = this.exceptions[var3++];
         return var1;
      }
   }

   public final void addCaseClosure(int var1, UnicodeSet var2) {
      switch(var1) {
      case 73:
         var2.add(105);
         return;
      case 105:
         var2.add(73);
         return;
      case 304:
         var2.add((CharSequence)"i̇");
         return;
      case 305:
         return;
      default:
         int var3 = this.trie.get(var1);
         int var4;
         if (var3 != 0) {
            if (getTypeFromProps(var3) != 0) {
               var4 = getDelta(var3);
               if (var4 != 0) {
                  var2.add(var1 + var4);
               }
            }
         } else {
            int var5 = getExceptionsOffset(var3);
            char var7 = this.exceptions[var5++];
            var4 = var5;

            int var8;
            for(var8 = 0; var8 <= 3; ++var8) {
               if (var8 != 0) {
                  var1 = this.getSlotValue(var7, var8, var4);
                  var2.add(var1);
               }
            }

            byte var9 = 0;
            byte var6 = 0;

            for(var8 = 0; var8 < var9; var8 += UTF16.getCharCount(var1)) {
               var1 = UTF16.charAt(this.exceptions, var6, this.exceptions.length, var8);
               var2.add(var1);
            }
         }

      }
   }

   private final int strcmpMax(String var1, int var2, int var3) {
      int var5 = var1.length();
      var3 -= var5;
      int var4 = 0;

      do {
         char var6 = var1.charAt(var4++);
         char var7 = this.unfold[var2++];
         if (var7 == 0) {
            return 1;
         }

         int var8 = var6 - var7;
         if (var8 != 0) {
            return var8;
         }

         --var5;
      } while(var5 > 0);

      if (var3 != 0 && this.unfold[var2] != 0) {
         return -var3;
      } else {
         return 0;
      }
   }

   public final boolean addStringCaseClosure(String var1, UnicodeSet var2) {
      if (this.unfold != null && var1 != null) {
         int var4 = var1.length();
         if (var4 <= 1) {
            return false;
         } else {
            char var9 = this.unfold[0];
            char var10 = this.unfold[1];
            char var11 = this.unfold[2];
            if (var4 > var11) {
               return false;
            } else {
               int var5 = 0;
               int var6 = var9;

               while(var5 < var6) {
                  int var3 = (var5 + var6) / 2;
                  int var8 = (var3 + 1) * var10;
                  int var7 = this.strcmpMax(var1, var8, var11);
                  if (var7 == 0) {
                     int var12;
                     for(var3 = var11; var3 < var10 && this.unfold[var8 + var3] != 0; var3 += UTF16.getCharCount(var12)) {
                        var12 = UTF16.charAt(this.unfold, var8, this.unfold.length, var3);
                        var2.add(var12);
                        this.addCaseClosure(var12, var2);
                     }

                     return true;
                  }

                  if (var7 < 0) {
                     var6 = var3;
                  } else {
                     var5 = var3 + 1;
                  }
               }

               return false;
            }
         }
      } else {
         return false;
      }
   }

   public final int getType(int var1) {
      return getTypeFromProps(this.trie.get(var1));
   }

   public final int getTypeOrIgnorable(int var1) {
      return getTypeAndIgnorableFromProps(this.trie.get(var1));
   }

   public final int getDotType(int var1) {
      int var2 = this.trie.get(var1);
      return var2 != 0 ? var2 & 96 : this.exceptions[getExceptionsOffset(var2)] >> 7 & 96;
   }

   public final boolean isSoftDotted(int var1) {
      return this.getDotType(var1) == 32;
   }

   public final boolean isCaseSensitive(int var1) {
      return (this.trie.get(var1) & 8) != 0;
   }

   private static final int getCaseLocale(ULocale var0, int[] var1) {
      int var2;
      if (var1 != null && (var2 = var1[0]) != 0) {
         return var2;
      } else {
         byte var4 = 1;
         String var3 = var0.getLanguage();
         if (!var3.equals("tr") && !var3.equals("tur") && !var3.equals("az") && !var3.equals("aze")) {
            if (var3.equals("lt") || var3.equals("lit")) {
               var4 = 3;
            }
         } else {
            var4 = 2;
         }

         if (var1 != null) {
            var1[0] = var4;
         }

         return var4;
      }
   }

   public final int toFullLower(int var1, UCaseProps.ContextIterator var2, StringBuilder var3, ULocale var4, int[] var5) {
      int var6 = var1;
      int var7 = this.trie.get(var1);
      if (var7 != 0) {
         if (getTypeFromProps(var7) >= 2) {
            var6 = var1 + getDelta(var7);
         }
      } else {
         int var8 = getExceptionsOffset(var7);
         char var10 = this.exceptions[var8++];
         if ((var10 & 16384) != 0) {
            int var12 = getCaseLocale(var4, var5);
            if (var12 == 3 && ((var1 == 73 || var1 == 74 || var1 == 302) && var2 != null || var1 == 204 || var1 == 205 || var1 == 296)) {
               switch(var1) {
               case 73:
                  var3.append("i̇");
                  return 2;
               case 74:
                  var3.append("j̇");
                  return 2;
               case 204:
                  var3.append("i̇̀");
                  return 3;
               case 205:
                  var3.append("i̇́");
                  return 3;
               case 296:
                  var3.append("i̇̃");
                  return 3;
               case 302:
                  var3.append("į̇");
                  return 2;
               default:
                  return 0;
               }
            }

            if (var12 == 2 && var1 == 304) {
               return 105;
            }

            if (var12 == 2 && var1 == 775 && var2 == null) {
               return 0;
            }

            if (var12 == 2 && var1 == 73 && var2 == null) {
               return 305;
            }

            if (var1 == 304) {
               var3.append("i̇");
               return 2;
            }

            if (var1 == 931 && 1 == null && -1 == null) {
               return 962;
            }
         }
      }

      return var6 == var1 ? ~var6 : var6;
   }

   private final int toUpperOrTitle(int var1, UCaseProps.ContextIterator var2, StringBuilder var3, ULocale var4, int[] var5, boolean var6) {
      int var7 = var1;
      int var8 = this.trie.get(var1);
      if (var8 != 0) {
         if (getTypeFromProps(var8) == 1) {
            var7 = var1 + getDelta(var8);
         }

         return var7 == var1 ? ~var7 : var7;
      } else {
         int var9 = getExceptionsOffset(var8);
         char var11 = this.exceptions[var9++];
         if ((var11 & 16384) != 0) {
            int var14 = getCaseLocale(var4, var5);
            if (var14 == 2 && var1 == 105) {
               return 304;
            }

            if (var14 == 3 && var1 == 775 && var2 == null) {
               return 0;
            }
         }

         if (!var6) {
            ;
         }

         return ~var1;
      }
   }

   public final int toFullUpper(int var1, UCaseProps.ContextIterator var2, StringBuilder var3, ULocale var4, int[] var5) {
      return this.toUpperOrTitle(var1, var2, var3, var4, var5, true);
   }

   public final int toFullTitle(int var1, UCaseProps.ContextIterator var2, StringBuilder var3, ULocale var4, int[] var5) {
      return this.toUpperOrTitle(var1, var2, var3, var4, var5, false);
   }

   public final int fold(int var1, int var2) {
      int var3 = this.trie.get(var1);
      if (var3 != 0) {
         if (getTypeFromProps(var3) >= 2) {
            var1 += getDelta(var3);
         }
      } else {
         int var4 = getExceptionsOffset(var3);
         char var5 = this.exceptions[var4++];
         if ((var5 & '耀') != 0) {
            if ((var2 & 255) == 0) {
               if (var1 == 73) {
                  return 105;
               }

               if (var1 == 304) {
                  return var1;
               }
            } else {
               if (var1 == 73) {
                  return 305;
               }

               if (var1 == 304) {
                  return 105;
               }
            }
         }

         byte var6 = 1;
         var1 = this.getSlotValue(var5, var6, var4);
      }

      return var1;
   }

   public final int toFullFolding(int var1, StringBuilder var2, int var3) {
      int var4 = var1;
      int var5 = this.trie.get(var1);
      if (var5 != 0) {
         if (getTypeFromProps(var5) >= 2) {
            var4 = var1 + getDelta(var5);
         }
      } else {
         int var6 = getExceptionsOffset(var5);
         char var8 = this.exceptions[var6++];
         if ((var8 & '耀') != 0) {
            if ((var3 & 255) == 0) {
               if (var1 == 73) {
                  return 105;
               }

               if (var1 == 304) {
                  var2.append("i̇");
                  return 2;
               }
            } else {
               if (var1 == 73) {
                  return 305;
               }

               if (var1 == 304) {
                  return 105;
               }
            }
         }

         byte var10 = 1;
         var4 = this.getSlotValue(var8, var10, var6);
      }

      return var4 == var1 ? ~var4 : var4;
   }

   public final boolean hasBinaryProperty(int var1, int var2) {
      switch(var2) {
      case 22:
         return 1 == this.getType(var1);
      case 23:
      case 24:
      case 25:
      case 26:
      case 28:
      case 29:
      case 31:
      case 32:
      case 33:
      case 35:
      case 36:
      case 37:
      case 38:
      case 39:
      case 40:
      case 41:
      case 42:
      case 43:
      case 44:
      case 45:
      case 46:
      case 47:
      case 48:
      case 54:
      default:
         return false;
      case 27:
         return this.isSoftDotted(var1);
      case 30:
         return 2 == this.getType(var1);
      case 34:
         return this.isCaseSensitive(var1);
      case 49:
         return 0 != this.getType(var1);
      case 50:
         return this.getTypeOrIgnorable(var1) >> 2 != 0;
      case 51:
         dummyStringBuilder.setLength(0);
         return this.toFullLower(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
      case 52:
         dummyStringBuilder.setLength(0);
         return this.toFullUpper(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
      case 53:
         dummyStringBuilder.setLength(0);
         return this.toFullTitle(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
      case 55:
         dummyStringBuilder.setLength(0);
         return this.toFullLower(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0 || this.toFullUpper(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0 || this.toFullTitle(var1, (UCaseProps.ContextIterator)null, dummyStringBuilder, ULocale.ROOT, rootLocCache) >= 0;
      }
   }

   private static final int getTypeFromProps(int var0) {
      return var0 & 3;
   }

   private static final int getTypeAndIgnorableFromProps(int var0) {
      return var0 & 7;
   }

   private static final int getDelta(int var0) {
      return (short)var0 >> 7;
   }

   static {
      try {
         INSTANCE = new UCaseProps();
      } catch (IOException var1) {
         throw new RuntimeException(var1);
      }
   }

   public interface ContextIterator {
      void reset(int var1);

      int next();
   }

   private static final class IsAcceptable implements ICUBinary.Authenticate {
      private IsAcceptable() {
      }

      public boolean isDataVersionAcceptable(byte[] var1) {
         return var1[0] == 3;
      }

      IsAcceptable(Object var1) {
         this();
      }
   }
}
