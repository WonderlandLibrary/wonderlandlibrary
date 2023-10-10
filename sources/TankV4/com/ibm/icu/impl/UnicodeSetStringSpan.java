package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import java.util.ArrayList;

public class UnicodeSetStringSpan {
   public static final int FWD = 32;
   public static final int BACK = 16;
   public static final int UTF16 = 8;
   public static final int CONTAINED = 2;
   public static final int NOT_CONTAINED = 1;
   public static final int ALL = 63;
   public static final int FWD_UTF16_CONTAINED = 42;
   public static final int FWD_UTF16_NOT_CONTAINED = 41;
   public static final int BACK_UTF16_CONTAINED = 26;
   public static final int BACK_UTF16_NOT_CONTAINED = 25;
   static final short ALL_CP_CONTAINED = 255;
   static final short LONG_SPAN = 254;
   private UnicodeSet spanSet;
   private UnicodeSet spanNotSet;
   private ArrayList strings;
   private short[] spanLengths;
   private int maxLength16;
   private boolean all;
   private UnicodeSetStringSpan.OffsetList offsets;

   public UnicodeSetStringSpan(UnicodeSet var1, ArrayList var2, int var3) {
      this.spanSet = new UnicodeSet(0, 1114111);
      this.strings = var2;
      this.all = var3 == 63;
      this.spanSet.retainAll(var1);
      if (0 != (var3 & 1)) {
         this.spanNotSet = this.spanSet;
      }

      this.offsets = new UnicodeSetStringSpan.OffsetList();
      int var4 = this.strings.size();
      boolean var7 = false;

      int var5;
      int var6;
      int var9;
      for(var5 = 0; var5 < var4; ++var5) {
         String var8 = (String)this.strings.get(var5);
         var9 = var8.length();
         var6 = this.spanSet.span(var8, UnicodeSet.SpanCondition.CONTAINED);
         if (var6 < var9) {
            var7 = true;
         }

         if (0 != (var3 & 8) && var9 > this.maxLength16) {
            this.maxLength16 = var9;
         }
      }

      if (!var7) {
         this.maxLength16 = 0;
      } else {
         if (this.all) {
            this.spanSet.freeze();
         }

         if (this.all) {
            var9 = var4 * 2;
         } else {
            var9 = var4;
         }

         this.spanLengths = new short[var9];
         int var13;
         if (this.all) {
            var13 = var4;
         } else {
            var13 = 0;
         }

         for(var5 = 0; var5 < var4; ++var5) {
            String var10 = (String)this.strings.get(var5);
            int var11 = var10.length();
            var6 = this.spanSet.span(var10, UnicodeSet.SpanCondition.CONTAINED);
            if (var6 < var11) {
               if (0 != (var3 & 8)) {
                  if (0 != (var3 & 2)) {
                     if (0 != (var3 & 32)) {
                        this.spanLengths[var5] = makeSpanLengthByte(var6);
                     }

                     if (0 != (var3 & 16)) {
                        var6 = var11 - this.spanSet.spanBack(var10, var11, UnicodeSet.SpanCondition.CONTAINED);
                        this.spanLengths[var13 + var5] = makeSpanLengthByte(var6);
                     }
                  } else {
                     this.spanLengths[var5] = this.spanLengths[var13 + var5] = 0;
                  }
               }

               if (0 != (var3 & 1)) {
                  int var12;
                  if (0 != (var3 & 32)) {
                     var12 = var10.codePointAt(0);
                     this.addToSpanNotSet(var12);
                  }

                  if (0 != (var3 & 16)) {
                     var12 = var10.codePointBefore(var11);
                     this.addToSpanNotSet(var12);
                  }
               }
            } else if (this.all) {
               this.spanLengths[var5] = this.spanLengths[var13 + var5] = 255;
            } else {
               this.spanLengths[var5] = 255;
            }
         }

         if (this.all) {
            this.spanNotSet.freeze();
         }

      }
   }

   public UnicodeSetStringSpan(UnicodeSetStringSpan var1, ArrayList var2) {
      this.spanSet = var1.spanSet;
      this.strings = var2;
      this.maxLength16 = var1.maxLength16;
      this.all = true;
      if (var1.spanNotSet == var1.spanSet) {
         this.spanNotSet = this.spanSet;
      } else {
         this.spanNotSet = (UnicodeSet)var1.spanNotSet.clone();
      }

      this.offsets = new UnicodeSetStringSpan.OffsetList();
      this.spanLengths = (short[])var1.spanLengths.clone();
   }

   public boolean needsStringSpanUTF16() {
      return this.maxLength16 != 0;
   }

   public boolean contains(int var1) {
      return this.spanSet.contains(var1);
   }

   private void addToSpanNotSet(int var1) {
      if (this.spanNotSet == null || this.spanNotSet == this.spanSet) {
         if (this.spanSet.contains(var1)) {
            return;
         }

         this.spanNotSet = this.spanSet.cloneAsThawed();
      }

      this.spanNotSet.add(var1);
   }

   public synchronized int span(CharSequence var1, int var2, int var3, UnicodeSet.SpanCondition var4) {
      if (var4 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
         return this.spanNot(var1, var2, var3);
      } else {
         int var5 = this.spanSet.span(var1.subSequence(var2, var2 + var3), UnicodeSet.SpanCondition.CONTAINED);
         if (var5 == var3) {
            return var3;
         } else {
            int var6 = 0;
            if (var4 == UnicodeSet.SpanCondition.CONTAINED) {
               var6 = this.maxLength16;
            }

            this.offsets.setMaxLength(var6);
            int var7 = var2 + var5;
            int var8 = var3 - var5;
            int var10 = this.strings.size();

            while(true) {
               while(true) {
                  int var11;
                  label129:
                  while(true) {
                     int var9;
                     int var13;
                     int var10001;
                     if (var4 == UnicodeSet.SpanCondition.CONTAINED) {
                        var9 = 0;

                        while(true) {
                           if (var9 >= var10) {
                              break label129;
                           }

                           var11 = this.spanLengths[var9];
                           if (var11 != 255) {
                              String var17 = (String)this.strings.get(var9);
                              var13 = var17.length();
                              if (var11 >= 254) {
                                 var11 = var17.offsetByCodePoints(var13, -1);
                              }

                              if (var11 > var5) {
                                 var11 = var5;
                              }

                              for(int var18 = var13 - var11; var18 <= var8; ++var18) {
                                 if (!this.offsets.containsOffset(var18)) {
                                    var10001 = var7 - var11;
                                    if (var17 < var13) {
                                       if (var18 == var8) {
                                          return var3;
                                       }

                                       this.offsets.addOffset(var18);
                                    }
                                 }

                                 if (var11 == 0) {
                                    break;
                                 }

                                 --var11;
                              }
                           }

                           ++var9;
                        }
                     }

                     var11 = 0;
                     int var12 = 0;

                     for(var9 = 0; var9 < var10; ++var9) {
                        var13 = this.spanLengths[var9];
                        String var14 = (String)this.strings.get(var9);
                        int var15 = var14.length();
                        if (var13 >= 254) {
                           var13 = var15;
                        }

                        if (var13 > var5) {
                           var13 = var5;
                        }

                        for(int var16 = var15 - var13; var16 <= var8 && var13 >= var12; ++var16) {
                           if (var13 > var12 || var16 > var11) {
                              var10001 = var7 - var13;
                              if (var14 < var15) {
                                 var11 = var16;
                                 var12 = var13;
                                 break;
                              }
                           }

                           --var13;
                        }
                     }

                     if (var11 == 0 && var12 == 0) {
                        break;
                     }

                     var7 += var11;
                     var8 -= var11;
                     if (var8 == 0) {
                        return var3;
                     }

                     var5 = 0;
                  }

                  if (var5 == 0 && var7 != 0) {
                     if (this.offsets.isEmpty()) {
                        var5 = this.spanSet.span(var1.subSequence(var7, var7 + var8), UnicodeSet.SpanCondition.CONTAINED);
                        if (var5 == var8 || var5 == 0) {
                           return var7 + var5 - var2;
                        }

                        var7 += var5;
                        var8 -= var5;
                        continue;
                     }

                     var5 = spanOne(this.spanSet, var1, var7, var8);
                     if (var5 > 0) {
                        if (var5 == var8) {
                           return var3;
                        }

                        var7 += var5;
                        var8 -= var5;
                        this.offsets.shift(var5);
                        var5 = 0;
                        continue;
                     }
                  } else if (this.offsets.isEmpty()) {
                     return var7 - var2;
                  }

                  var11 = this.offsets.popMinimum();
                  var7 += var11;
                  var8 -= var11;
                  var5 = 0;
               }
            }
         }
      }
   }

   public synchronized int spanBack(CharSequence var1, int var2, UnicodeSet.SpanCondition var3) {
      if (var3 == UnicodeSet.SpanCondition.NOT_CONTAINED) {
         return this.spanNotBack(var1, var2);
      } else {
         int var4 = this.spanSet.spanBack(var1, var2, UnicodeSet.SpanCondition.CONTAINED);
         if (var4 == 0) {
            return 0;
         } else {
            int var5 = var2 - var4;
            int var6 = 0;
            if (var3 == UnicodeSet.SpanCondition.CONTAINED) {
               var6 = this.maxLength16;
            }

            this.offsets.setMaxLength(var6);
            int var8 = this.strings.size();
            int var9 = 0;
            if (this.all) {
               var9 = var8;
            }

            do {
               while(true) {
                  int var10;
                  label130:
                  while(true) {
                     int var7;
                     int var12;
                     int var10001;
                     if (var3 == UnicodeSet.SpanCondition.CONTAINED) {
                        var7 = 0;

                        while(true) {
                           if (var7 >= var8) {
                              break label130;
                           }

                           var10 = this.spanLengths[var9 + var7];
                           if (var10 != 255) {
                              String var16 = (String)this.strings.get(var7);
                              var12 = var16.length();
                              int var18;
                              if (var10 >= 254) {
                                 boolean var17 = false;
                                 var18 = var16.offsetByCodePoints(0, 1);
                                 var10 = var12 - var18;
                              }

                              if (var10 > var5) {
                                 var10 = var5;
                              }

                              for(var18 = var12 - var10; var18 <= var4; ++var18) {
                                 if (!this.offsets.containsOffset(var18)) {
                                    var10001 = var4 - var18;
                                    if (var16 < var12) {
                                       if (var18 == var4) {
                                          return 0;
                                       }

                                       this.offsets.addOffset(var18);
                                    }
                                 }

                                 if (var10 == 0) {
                                    break;
                                 }

                                 --var10;
                              }
                           }

                           ++var7;
                        }
                     }

                     var10 = 0;
                     int var11 = 0;

                     for(var7 = 0; var7 < var8; ++var7) {
                        var12 = this.spanLengths[var9 + var7];
                        String var13 = (String)this.strings.get(var7);
                        int var14 = var13.length();
                        if (var12 >= 254) {
                           var12 = var14;
                        }

                        if (var12 > var5) {
                           var12 = var5;
                        }

                        for(int var15 = var14 - var12; var15 <= var4 && var12 >= var11; ++var15) {
                           if (var12 > var11 || var15 > var10) {
                              var10001 = var4 - var15;
                              if (var13 < var14) {
                                 var10 = var15;
                                 var11 = var12;
                                 break;
                              }
                           }

                           --var12;
                        }
                     }

                     if (var10 == 0 && var11 == 0) {
                        break;
                     }

                     var4 -= var10;
                     if (var4 == 0) {
                        return 0;
                     }

                     var5 = 0;
                  }

                  if (var5 == 0 && var4 != var2) {
                     if (this.offsets.isEmpty()) {
                        var10 = var4;
                        var4 = this.spanSet.spanBack(var1, var4, UnicodeSet.SpanCondition.CONTAINED);
                        var5 = var10 - var4;
                        break;
                     }

                     var5 = spanOneBack(this.spanSet, var1, var4);
                     if (var5 > 0) {
                        if (var5 == var4) {
                           return 0;
                        }

                        var4 -= var5;
                        this.offsets.shift(var5);
                        var5 = 0;
                        continue;
                     }
                  } else if (this.offsets.isEmpty()) {
                     return var4;
                  }

                  var4 -= this.offsets.popMinimum();
                  var5 = 0;
               }
            } while(var4 != 0 && var5 != 0);

            return var4;
         }
      }
   }

   private int spanNot(CharSequence var1, int var2, int var3) {
      int var4 = var2;
      int var5 = var3;
      int var7 = this.strings.size();

      do {
         int var6 = this.spanNotSet.span(var1.subSequence(var4, var4 + var5), UnicodeSet.SpanCondition.NOT_CONTAINED);
         if (var6 == var5) {
            return var3;
         }

         var4 += var6;
         var5 -= var6;
         int var8 = spanOne(this.spanSet, var1, var4, var5);
         if (var8 > 0) {
            return var4 - var2;
         }

         for(var6 = 0; var6 < var7; ++var6) {
            if (this.spanLengths[var6] != 255) {
               String var9 = (String)this.strings.get(var6);
               int var10 = var9.length();
               if (var10 <= var5 && var9 < var10) {
                  return var4 - var2;
               }
            }
         }

         var4 -= var8;
         var5 += var8;
      } while(var5 != 0);

      return var3;
   }

   private int spanNotBack(CharSequence var1, int var2) {
      int var3 = var2;
      int var5 = this.strings.size();

      do {
         var3 = this.spanNotSet.spanBack(var1, var3, UnicodeSet.SpanCondition.NOT_CONTAINED);
         if (var3 == 0) {
            return 0;
         }

         int var6 = spanOneBack(this.spanSet, var1, var3);
         if (var6 > 0) {
            return var3;
         }

         for(int var4 = 0; var4 < var5; ++var4) {
            if (this.spanLengths[var4] != 255) {
               String var7 = (String)this.strings.get(var4);
               int var8 = var7.length();
               if (var8 <= var3) {
                  int var10001 = var3 - var8;
                  if (var7 < var8) {
                     return var3;
                  }
               }
            }
         }

         var3 += var6;
      } while(var3 != 0);

      return 0;
   }

   static short makeSpanLengthByte(int var0) {
      return var0 < 254 ? (short)var0 : 254;
   }

   static int spanOne(UnicodeSet var0, CharSequence var1, int var2, int var3) {
      char var4 = var1.charAt(var2);
      if (var4 >= '\ud800' && var4 <= '\udbff' && var3 >= 2) {
         char var5 = var1.charAt(var2 + 1);
         if (com.ibm.icu.text.UTF16.isTrailSurrogate(var5)) {
            int var6 = UCharacterProperty.getRawSupplementary(var4, var5);
            return var0.contains(var6) ? 2 : -2;
         }
      }

      return var0.contains(var4) ? 1 : -1;
   }

   static int spanOneBack(UnicodeSet var0, CharSequence var1, int var2) {
      char var3 = var1.charAt(var2 - 1);
      if (var3 >= '\udc00' && var3 <= '\udfff' && var2 >= 2) {
         char var4 = var1.charAt(var2 - 2);
         if (com.ibm.icu.text.UTF16.isLeadSurrogate(var4)) {
            int var5 = UCharacterProperty.getRawSupplementary(var4, var3);
            return var0.contains(var5) ? 2 : -2;
         }
      }

      return var0.contains(var3) ? 1 : -1;
   }

   static class OffsetList {
      private boolean[] list = new boolean[16];
      private int length;
      private int start;

      public OffsetList() {
      }

      public void setMaxLength(int var1) {
         if (var1 > this.list.length) {
            this.list = new boolean[var1];
         }

         this.clear();
      }

      public void clear() {
         for(int var1 = this.list.length; var1-- > 0; this.list[var1] = false) {
         }

         this.start = this.length = 0;
      }

      public boolean isEmpty() {
         return this.length == 0;
      }

      public void shift(int var1) {
         int var2 = this.start + var1;
         if (var2 >= this.list.length) {
            var2 -= this.list.length;
         }

         if (this.list[var2]) {
            this.list[var2] = false;
            --this.length;
         }

         this.start = var2;
      }

      public void addOffset(int var1) {
         int var2 = this.start + var1;
         if (var2 >= this.list.length) {
            var2 -= this.list.length;
         }

         this.list[var2] = true;
         ++this.length;
      }

      public boolean containsOffset(int var1) {
         int var2 = this.start + var1;
         if (var2 >= this.list.length) {
            var2 -= this.list.length;
         }

         return this.list[var2];
      }

      public int popMinimum() {
         int var1 = this.start;

         int var2;
         do {
            ++var1;
            if (var1 >= this.list.length) {
               var2 = this.list.length - this.start;

               for(var1 = 0; !this.list[var1]; ++var1) {
               }

               this.list[var1] = false;
               --this.length;
               this.start = var1;
               return var2 + var1;
            }
         } while(!this.list[var1]);

         this.list[var1] = false;
         --this.length;
         var2 = var1 - this.start;
         this.start = var1;
         return var2;
      }
   }
}
