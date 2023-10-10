package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UnicodeSet;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;

public final class UCharacterName {
   public static final UCharacterName INSTANCE;
   public static final int LINES_PER_GROUP_ = 32;
   public int m_groupcount_ = 0;
   int m_groupsize_ = 0;
   private char[] m_tokentable_;
   private byte[] m_tokenstring_;
   private char[] m_groupinfo_;
   private byte[] m_groupstring_;
   private UCharacterName.AlgorithmName[] m_algorithm_;
   private char[] m_groupoffsets_ = new char[33];
   private char[] m_grouplengths_ = new char[33];
   private static final String NAME_FILE_NAME_ = "data/icudt51b/unames.icu";
   private static final int GROUP_SHIFT_ = 5;
   private static final int GROUP_MASK_ = 31;
   private static final int NAME_BUFFER_SIZE_ = 100000;
   private static final int OFFSET_HIGH_OFFSET_ = 1;
   private static final int OFFSET_LOW_OFFSET_ = 2;
   private static final int SINGLE_NIBBLE_MAX_ = 11;
   private int[] m_nameSet_ = new int[8];
   private int[] m_ISOCommentSet_ = new int[8];
   private StringBuffer m_utilStringBuffer_ = new StringBuffer();
   private int[] m_utilIntBuffer_ = new int[2];
   private int m_maxISOCommentLength_;
   private int m_maxNameLength_;
   private static final String[] TYPE_NAMES_;
   private static final String UNKNOWN_TYPE_NAME_ = "unknown";
   private static final int NON_CHARACTER_ = 30;
   private static final int LEAD_SURROGATE_ = 31;
   private static final int TRAIL_SURROGATE_ = 32;
   static final int EXTENDED_CATEGORY_ = 33;

   public String getName(int var1, int var2) {
      if (var1 >= 0 && var1 <= 1114111 && var2 <= 4) {
         String var3 = null;
         var3 = this.getAlgName(var1, var2);
         if (var3 == null || var3.length() == 0) {
            if (var2 == 2) {
               var3 = this.getExtendedName(var1);
            } else {
               var3 = this.getGroupName(var1, var2);
            }
         }

         return var3;
      } else {
         return null;
      }
   }

   public int getCharFromName(int var1, String var2) {
      if (var1 < 4 && var2 != null && var2.length() != 0) {
         int var3 = getExtendedChar(var2.toLowerCase(Locale.ENGLISH), var1);
         if (var3 >= -1) {
            return var3;
         } else {
            String var4 = var2.toUpperCase(Locale.ENGLISH);
            if (var1 == 0 || var1 == 2) {
               int var5 = 0;
               if (this.m_algorithm_ != null) {
                  var5 = this.m_algorithm_.length;
               }

               --var5;

               while(var5 >= 0) {
                  var3 = this.m_algorithm_[var5].getChar(var4);
                  if (var3 >= 0) {
                     return var3;
                  }

                  --var5;
               }
            }

            if (var1 == 2) {
               var3 = this.getGroupChar(var4, 0);
               if (var3 == -1) {
                  var3 = this.getGroupChar(var4, 3);
               }
            } else {
               var3 = this.getGroupChar(var4, var1);
            }

            return var3;
         }
      } else {
         return -1;
      }
   }

   public int getGroupLengths(int var1, char[] var2, char[] var3) {
      char var4 = '\uffff';
      boolean var5 = false;
      boolean var6 = false;
      var1 *= this.m_groupsize_;
      int var8 = UCharacterUtility.toInt(this.m_groupinfo_[var1 + 1], this.m_groupinfo_[var1 + 2]);
      var2[0] = 0;

      for(int var9 = 0; var9 < 32; ++var8) {
         byte var10 = this.m_groupstring_[var8];

         for(int var7 = 4; var7 >= 0; var7 -= 4) {
            byte var11 = (byte)(var10 >> var7 & 15);
            if (var4 == '\uffff' && var11 > 11) {
               var4 = (char)(var11 - 12 << 4);
            } else {
               if (var4 != '\uffff') {
                  var3[var9] = (char)((var4 | var11) + 12);
               } else {
                  var3[var9] = (char)var11;
               }

               if (var9 < 32) {
                  var2[var9 + 1] = (char)(var2[var9] + var3[var9]);
               }

               var4 = '\uffff';
               ++var9;
            }
         }
      }

      return var8;
   }

   public String getGroupName(int var1, int var2, int var3) {
      if (var3 != 0 && var3 != 2) {
         if (59 < this.m_tokentable_.length && this.m_tokentable_[59] != '\uffff') {
            var2 = 0;
         } else {
            int var4 = var3 == 4 ? 2 : var3;

            do {
               int var5 = var1;
               var1 += UCharacterUtility.skipByteSubString(this.m_groupstring_, var1, var2, (byte)59);
               var2 -= var1 - var5;
               --var4;
            } while(var4 > 0);
         }
      }

      StringBuffer var9;
      synchronized(var9 = this.m_utilStringBuffer_){}
      this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
      int var7 = 0;

      while(var7 < var2) {
         byte var10 = this.m_groupstring_[var1 + var7];
         ++var7;
         if (var10 >= this.m_tokentable_.length) {
            if (var10 == 59) {
               break;
            }

            this.m_utilStringBuffer_.append(var10);
         } else {
            char var6 = this.m_tokentable_[var10 & 255];
            if (var6 == '\ufffe') {
               var6 = this.m_tokentable_[var10 << 8 | this.m_groupstring_[var1 + var7] & 255];
               ++var7;
            }

            if (var6 == '\uffff') {
               if (var10 == 59) {
                  if (this.m_utilStringBuffer_.length() != 0 || var3 != 2) {
                     break;
                  }
               } else {
                  this.m_utilStringBuffer_.append((char)(var10 & 255));
               }
            } else {
               UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, var6);
            }
         }
      }

      return this.m_utilStringBuffer_.length() > 0 ? this.m_utilStringBuffer_.toString() : null;
   }

   public String getExtendedName(int var1) {
      String var2 = this.getName(var1, 0);
      if (var2 == null && var2 == null) {
         var2 = this.getExtendedOr10Name(var1);
      }

      return var2;
   }

   public int getGroup(int var1) {
      int var2 = this.m_groupcount_;
      int var3 = getCodepointMSB(var1);
      int var4 = 0;

      while(var4 < var2 - 1) {
         int var5 = var4 + var2 >> 1;
         if (var3 < this.getGroupMSB(var5)) {
            var2 = var5;
         } else {
            var4 = var5;
         }
      }

      return var4;
   }

   public String getExtendedOr10Name(int var1) {
      String var2 = null;
      if (var2 == null) {
         int var3 = getType(var1);
         if (var3 >= TYPE_NAMES_.length) {
            var2 = "unknown";
         } else {
            var2 = TYPE_NAMES_[var3];
         }

         StringBuffer var4;
         synchronized(var4 = this.m_utilStringBuffer_){}
         this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
         this.m_utilStringBuffer_.append('<');
         this.m_utilStringBuffer_.append(var2);
         this.m_utilStringBuffer_.append('-');
         String var5 = Integer.toHexString(var1).toUpperCase(Locale.ENGLISH);

         for(int var6 = 4 - var5.length(); var6 > 0; --var6) {
            this.m_utilStringBuffer_.append('0');
         }

         this.m_utilStringBuffer_.append(var5);
         this.m_utilStringBuffer_.append('>');
         var2 = this.m_utilStringBuffer_.toString();
      }

      return var2;
   }

   public int getGroupMSB(int var1) {
      return var1 >= this.m_groupcount_ ? -1 : this.m_groupinfo_[var1 * this.m_groupsize_];
   }

   public static int getCodepointMSB(int var0) {
      return var0 >> 5;
   }

   public static int getGroupLimit(int var0) {
      return (var0 << 5) + 32;
   }

   public static int getGroupMin(int var0) {
      return var0 << 5;
   }

   public static int getGroupOffset(int var0) {
      return var0 & 31;
   }

   public static int getGroupMinFromCodepoint(int var0) {
      return var0 & -32;
   }

   public int getAlgorithmLength() {
      return this.m_algorithm_.length;
   }

   public int getAlgorithmStart(int var1) {
      return UCharacterName.AlgorithmName.access$000(this.m_algorithm_[var1]);
   }

   public int getAlgorithmEnd(int var1) {
      return UCharacterName.AlgorithmName.access$100(this.m_algorithm_[var1]);
   }

   public String getAlgorithmName(int var1, int var2) {
      String var3 = null;
      StringBuffer var4;
      synchronized(var4 = this.m_utilStringBuffer_){}
      this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
      this.m_algorithm_[var1].appendName(var2, this.m_utilStringBuffer_);
      var3 = this.m_utilStringBuffer_.toString();
      return var3;
   }

   public synchronized String getGroupName(int var1, int var2) {
      int var3 = getCodepointMSB(var1);
      int var4 = this.getGroup(var1);
      if (var3 == this.m_groupinfo_[var4 * this.m_groupsize_]) {
         int var5 = this.getGroupLengths(var4, this.m_groupoffsets_, this.m_grouplengths_);
         int var6 = var1 & 31;
         return this.getGroupName(var5 + this.m_groupoffsets_[var6], this.m_grouplengths_[var6], var2);
      } else {
         return null;
      }
   }

   public int getMaxCharNameLength() {
      return this > 0 ? this.m_maxNameLength_ : 0;
   }

   public int getMaxISOCommentLength() {
      return this > 0 ? this.m_maxISOCommentLength_ : 0;
   }

   public void getCharNameCharacters(UnicodeSet var1) {
      this.convert(this.m_nameSet_, var1);
   }

   public void getISOCommentCharacters(UnicodeSet var1) {
      this.convert(this.m_ISOCommentSet_, var1);
   }

   boolean setToken(char[] var1, byte[] var2) {
      if (var1 != null && var2 != null && var1.length > 0 && var2.length > 0) {
         this.m_tokentable_ = var1;
         this.m_tokenstring_ = var2;
         return true;
      } else {
         return false;
      }
   }

   boolean setAlgorithm(UCharacterName.AlgorithmName[] var1) {
      if (var1 != null && var1.length != 0) {
         this.m_algorithm_ = var1;
         return true;
      } else {
         return false;
      }
   }

   boolean setGroupCountSize(int var1, int var2) {
      if (var1 > 0 && var2 > 0) {
         this.m_groupcount_ = var1;
         this.m_groupsize_ = var2;
         return true;
      } else {
         return false;
      }
   }

   boolean setGroup(char[] var1, byte[] var2) {
      if (var1 != null && var2 != null && var1.length > 0 && var2.length > 0) {
         this.m_groupinfo_ = var1;
         this.m_groupstring_ = var2;
         return true;
      } else {
         return false;
      }
   }

   private UCharacterName() throws IOException {
      InputStream var1 = ICUData.getRequiredStream("data/icudt51b/unames.icu");
      BufferedInputStream var2 = new BufferedInputStream(var1, 100000);
      UCharacterNameReader var3 = new UCharacterNameReader(var2);
      var3.read(this);
      var2.close();
   }

   private String getAlgName(int var1, int var2) {
      if (var2 == 0 || var2 == 2) {
         StringBuffer var3;
         synchronized(var3 = this.m_utilStringBuffer_){}
         this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());

         for(int var4 = this.m_algorithm_.length - 1; var4 >= 0; --var4) {
            if (this.m_algorithm_[var4].contains(var1)) {
               this.m_algorithm_[var4].appendName(var1, this.m_utilStringBuffer_);
               return this.m_utilStringBuffer_.toString();
            }
         }
      }

      return null;
   }

   private synchronized int getGroupChar(String var1, int var2) {
      for(int var3 = 0; var3 < this.m_groupcount_; ++var3) {
         int var4 = this.getGroupLengths(var3, this.m_groupoffsets_, this.m_grouplengths_);
         int var5 = this.getGroupChar(var4, this.m_grouplengths_, var1, var2);
         if (var5 != -1) {
            return this.m_groupinfo_[var3 * this.m_groupsize_] << 5 | var5;
         }
      }

      return -1;
   }

   private int getGroupChar(int var1, char[] var2, String var3, int var4) {
      boolean var5 = false;
      int var8 = var3.length();

      for(int var11 = 0; var11 <= 32; ++var11) {
         int var9 = 0;
         int var7 = var2[var11];
         if (var4 != 0 && var4 != 2) {
            int var12 = var4 == 4 ? 2 : var4;

            do {
               int var13 = var1;
               var1 += UCharacterUtility.skipByteSubString(this.m_groupstring_, var1, var7, (byte)59);
               var7 -= var1 - var13;
               --var12;
            } while(var12 > 0);
         }

         int var10 = 0;

         while(var10 < var7 && var9 != -1 && var9 < var8) {
            byte var14 = this.m_groupstring_[var1 + var10];
            ++var10;
            if (var14 >= this.m_tokentable_.length) {
               if (var3.charAt(var9++) != (var14 & 255)) {
                  var9 = -1;
               }
            } else {
               char var6 = this.m_tokentable_[var14 & 255];
               if (var6 == '\ufffe') {
                  var6 = this.m_tokentable_[var14 << 8 | this.m_groupstring_[var1 + var10] & 255];
                  ++var10;
               }

               if (var6 == '\uffff') {
                  if (var3.charAt(var9++) != (var14 & 255)) {
                     var9 = -1;
                  }
               } else {
                  var9 = UCharacterUtility.compareNullTermByteSubString(var3, this.m_tokenstring_, var9, var6);
               }
            }
         }

         if (var8 == var9 && (var10 == var7 || this.m_groupstring_[var1 + var10] == 59)) {
            return var11;
         }

         var1 += var7;
      }

      return -1;
   }

   private static int getType(int var0) {
      if (UCharacterUtility.isNonCharacter(var0)) {
         return 30;
      } else {
         int var1 = UCharacter.getType(var0);
         if (var1 == 18) {
            if (var0 <= 56319) {
               var1 = 31;
            } else {
               var1 = 32;
            }
         }

         return var1;
      }
   }

   private static int getExtendedChar(String var0, int var1) {
      if (var0.charAt(0) != '<') {
         return -2;
      } else {
         if (var1 == 2) {
            int var2 = var0.length() - 1;
            if (var0.charAt(var2) == '>') {
               int var3 = var0.lastIndexOf(45);
               if (var3 >= 0) {
                  ++var3;
                  boolean var4 = true;

                  int var9;
                  try {
                     var9 = Integer.parseInt(var0.substring(var3, var2), 16);
                  } catch (NumberFormatException var8) {
                     return -1;
                  }

                  String var5 = var0.substring(1, var3 - 1);
                  int var6 = TYPE_NAMES_.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     if (var5.compareTo(TYPE_NAMES_[var7]) == 0) {
                        if (getType(var9) == var7) {
                           return var9;
                        }
                        break;
                     }
                  }
               }
            }
         }

         return -1;
      }
   }

   private static void add(int[] var0, char var1) {
      var0[var1 >>> 5] |= 1 << (var1 & 31);
   }

   private static int add(int[] var0, String var1) {
      int var2 = var1.length();

      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         add(var0, var1.charAt(var3));
      }

      return var2;
   }

   private static int add(int[] var0, StringBuffer var1) {
      int var2 = var1.length();

      for(int var3 = var2 - 1; var3 >= 0; --var3) {
         add(var0, var1.charAt(var3));
      }

      return var2;
   }

   private int addAlgorithmName(int var1) {
      boolean var2 = false;

      for(int var3 = this.m_algorithm_.length - 1; var3 >= 0; --var3) {
         int var4 = this.m_algorithm_[var3].add(this.m_nameSet_, var1);
         if (var4 > var1) {
            var1 = var4;
         }
      }

      return var1;
   }

   private int addExtendedName(int var1) {
      for(int var2 = TYPE_NAMES_.length - 1; var2 >= 0; --var2) {
         int var3 = 9 + add(this.m_nameSet_, TYPE_NAMES_[var2]);
         if (var3 > var1) {
            var1 = var3;
         }
      }

      return var1;
   }

   private int[] addGroupName(int var1, int var2, byte[] var3, int[] var4) {
      int var5 = 0;
      int var6 = 0;

      while(var6 < var2) {
         char var7 = (char)(this.m_groupstring_[var1 + var6] & 255);
         ++var6;
         if (var7 == ';') {
            break;
         }

         if (var7 >= this.m_tokentable_.length) {
            add(var4, var7);
            ++var5;
         } else {
            char var8 = this.m_tokentable_[var7 & 255];
            if (var8 == '\ufffe') {
               var7 = (char)(var7 << 8 | this.m_groupstring_[var1 + var6] & 255);
               var8 = this.m_tokentable_[var7];
               ++var6;
            }

            if (var8 == '\uffff') {
               add(var4, var7);
               ++var5;
            } else {
               byte var9 = var3[var7];
               if (var9 == 0) {
                  StringBuffer var10;
                  synchronized(var10 = this.m_utilStringBuffer_){}
                  this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                  UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, var8);
                  var9 = (byte)add(var4, this.m_utilStringBuffer_);
                  var3[var7] = var9;
               }

               var5 += var9;
            }
         }
      }

      this.m_utilIntBuffer_[0] = var5;
      this.m_utilIntBuffer_[1] = var6;
      return this.m_utilIntBuffer_;
   }

   private void addGroupName(int var1) {
      int var2 = 0;
      char[] var3 = new char[34];
      char[] var4 = new char[34];
      byte[] var5 = new byte[this.m_tokentable_.length];

      for(int var6 = 0; var6 < this.m_groupcount_; ++var6) {
         int var7 = this.getGroupLengths(var6, var3, var4);

         for(int var8 = 0; var8 < 32; ++var8) {
            int var9 = var7 + var3[var8];
            char var10 = var4[var8];
            if (var10 != 0) {
               int[] var11 = this.addGroupName(var9, var10, var5, this.m_nameSet_);
               if (var11[0] > var1) {
                  var1 = var11[0];
               }

               var9 += var11[1];
               if (var11[1] < var10) {
                  int var12 = var10 - var11[1];
                  var11 = this.addGroupName(var9, var12, var5, this.m_nameSet_);
                  if (var11[0] > var1) {
                     var1 = var11[0];
                  }

                  var9 += var11[1];
                  if (var11[1] < var12) {
                     var12 -= var11[1];
                     var11 = this.addGroupName(var9, var12, var5, this.m_ISOCommentSet_);
                     if (var11[1] > var2) {
                        var2 = var12;
                     }
                  }
               }
            }
         }
      }

      this.m_maxISOCommentLength_ = var2;
      this.m_maxNameLength_ = var1;
   }

   private void convert(int[] var1, UnicodeSet var2) {
      var2.clear();
      if (!(this > 0)) {
         for(char var3 = 255; var3 > 0; --var3) {
            if (var3 != 0) {
               var2.add(var3);
            }
         }

      }
   }

   static int access$200(int[] var0, String var1) {
      return add(var0, var1);
   }

   static int access$300(int[] var0, StringBuffer var1) {
      return add(var0, var1);
   }

   static {
      try {
         INSTANCE = new UCharacterName();
      } catch (IOException var1) {
         throw new MissingResourceException("Could not construct UCharacterName. Missing unames.icu", "", "");
      }

      TYPE_NAMES_ = new String[]{"unassigned", "uppercase letter", "lowercase letter", "titlecase letter", "modifier letter", "other letter", "non spacing mark", "enclosing mark", "combining spacing mark", "decimal digit number", "letter number", "other number", "space separator", "line separator", "paragraph separator", "control", "format", "private use area", "surrogate", "dash punctuation", "start punctuation", "end punctuation", "connector punctuation", "other punctuation", "math symbol", "currency symbol", "modifier symbol", "other symbol", "initial punctuation", "final punctuation", "noncharacter", "lead surrogate", "trail surrogate"};
   }

   static final class AlgorithmName {
      static final int TYPE_0_ = 0;
      static final int TYPE_1_ = 1;
      private int m_rangestart_;
      private int m_rangeend_;
      private byte m_type_;
      private byte m_variant_;
      private char[] m_factor_;
      private String m_prefix_;
      private byte[] m_factorstring_;
      private StringBuffer m_utilStringBuffer_ = new StringBuffer();
      private int[] m_utilIntBuffer_ = new int[256];

      boolean setInfo(int var1, int var2, byte var3, byte var4) {
         if (var1 < 0 || var1 > var2 || var2 > 1114111 || var3 != 0 && var3 != 1) {
            return false;
         } else {
            this.m_rangestart_ = var1;
            this.m_rangeend_ = var2;
            this.m_type_ = var3;
            this.m_variant_ = var4;
            return true;
         }
      }

      boolean setFactor(char[] var1) {
         if (var1.length == this.m_variant_) {
            this.m_factor_ = var1;
            return true;
         } else {
            return false;
         }
      }

      boolean setPrefix(String var1) {
         if (var1 != null && var1.length() > 0) {
            this.m_prefix_ = var1;
            return true;
         } else {
            return false;
         }
      }

      boolean setFactorString(byte[] var1) {
         this.m_factorstring_ = var1;
         return true;
      }

      boolean contains(int var1) {
         return this.m_rangestart_ <= var1 && var1 <= this.m_rangeend_;
      }

      void appendName(int var1, StringBuffer var2) {
         var2.append(this.m_prefix_);
         switch(this.m_type_) {
         case 0:
            var2.append(Utility.hex((long)var1, this.m_variant_));
            break;
         case 1:
            int var3 = var1 - this.m_rangestart_;
            int[] var4 = this.m_utilIntBuffer_;
            int[] var6;
            synchronized(var6 = this.m_utilIntBuffer_){}

            for(int var7 = this.m_variant_ - 1; var7 > 0; --var7) {
               int var5 = this.m_factor_[var7] & 255;
               var4[var7] = var3 % var5;
               var3 /= var5;
            }

            var4[0] = var3;
            var2.append(this.getFactorString(var4, this.m_variant_));
         }

      }

      int getChar(String var1) {
         int var2 = this.m_prefix_.length();
         if (var1.length() >= var2 && this.m_prefix_.equals(var1.substring(0, var2))) {
            int var3;
            switch(this.m_type_) {
            case 0:
               try {
                  var3 = Integer.parseInt(var1.substring(var2), 16);
                  if (this.m_rangestart_ <= var3 && var3 <= this.m_rangeend_) {
                     return var3;
                  }
                  break;
               } catch (NumberFormatException var10) {
                  return -1;
               }
            case 1:
               for(var3 = this.m_rangestart_; var3 <= this.m_rangeend_; ++var3) {
                  int var4 = var3 - this.m_rangestart_;
                  int[] var5 = this.m_utilIntBuffer_;
                  int[] var7;
                  synchronized(var7 = this.m_utilIntBuffer_){}

                  for(int var8 = this.m_variant_ - 1; var8 > 0; --var8) {
                     int var6 = this.m_factor_[var8] & 255;
                     var5[var8] = var4 % var6;
                     var4 /= var6;
                  }

                  var5[0] = var4;
                  byte var10002 = this.m_variant_;
                  if (var2 != null) {
                     return var3;
                  }
               }
            }

            return -1;
         } else {
            return -1;
         }
      }

      int add(int[] var1, int var2) {
         int var3 = UCharacterName.access$200(var1, this.m_prefix_);
         switch(this.m_type_) {
         case 0:
            var3 += this.m_variant_;
            break;
         case 1:
            for(int var4 = this.m_variant_ - 1; var4 > 0; --var4) {
               int var5 = 0;
               int var6 = 0;

               for(int var7 = this.m_factor_[var4]; var7 > 0; --var7) {
                  StringBuffer var8;
                  synchronized(var8 = this.m_utilStringBuffer_){}
                  this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                  var6 = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, var6);
                  UCharacterName.access$300(var1, this.m_utilStringBuffer_);
                  if (this.m_utilStringBuffer_.length() > var5) {
                     var5 = this.m_utilStringBuffer_.length();
                  }
               }

               var3 += var5;
            }
         }

         return var3 > var2 ? var3 : var2;
      }

      private String getFactorString(int[] var1, int var2) {
         int var3 = this.m_factor_.length;
         if (var1 != null && var2 == var3) {
            StringBuffer var4;
            synchronized(var4 = this.m_utilStringBuffer_){}
            this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
            int var5 = 0;
            --var3;

            for(int var7 = 0; var7 <= var3; ++var7) {
               char var6 = this.m_factor_[var7];
               var5 = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, var5, var1[var7]);
               var5 = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, var5);
               if (var7 != var3) {
                  var5 = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, var5, var6 - var1[var7] - 1);
               }
            }

            return this.m_utilStringBuffer_.toString();
         } else {
            return null;
         }
      }

      static int access$000(UCharacterName.AlgorithmName var0) {
         return var0.m_rangestart_;
      }

      static int access$100(UCharacterName.AlgorithmName var0) {
         return var0.m_rangeend_;
      }
   }
}
