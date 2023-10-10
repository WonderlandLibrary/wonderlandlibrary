package com.ibm.icu.util;

import com.ibm.icu.text.UTF16;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public final class CharsTrie implements Cloneable, Iterable {
   private static BytesTrie.Result[] valueResults_;
   static final int kMaxBranchLinearSubNodeLength = 5;
   static final int kMinLinearMatch = 48;
   static final int kMaxLinearMatchLength = 16;
   static final int kMinValueLead = 64;
   static final int kNodeTypeMask = 63;
   static final int kValueIsFinal = 32768;
   static final int kMaxOneUnitValue = 16383;
   static final int kMinTwoUnitValueLead = 16384;
   static final int kThreeUnitValueLead = 32767;
   static final int kMaxTwoUnitValue = 1073676287;
   static final int kMaxOneUnitNodeValue = 255;
   static final int kMinTwoUnitNodeValueLead = 16448;
   static final int kThreeUnitNodeValueLead = 32704;
   static final int kMaxTwoUnitNodeValue = 16646143;
   static final int kMaxOneUnitDelta = 64511;
   static final int kMinTwoUnitDeltaLead = 64512;
   static final int kThreeUnitDeltaLead = 65535;
   static final int kMaxTwoUnitDelta = 67043327;
   private CharSequence chars_;
   private int root_;
   private int pos_;
   private int remainingMatchLength_;
   static final boolean $assertionsDisabled = !CharsTrie.class.desiredAssertionStatus();

   public CharsTrie(CharSequence var1, int var2) {
      this.chars_ = var1;
      this.pos_ = this.root_ = var2;
      this.remainingMatchLength_ = -1;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public CharsTrie reset() {
      this.pos_ = this.root_;
      this.remainingMatchLength_ = -1;
      return this;
   }

   public CharsTrie saveState(CharsTrie.State var1) {
      CharsTrie.State.access$002(var1, this.chars_);
      CharsTrie.State.access$102(var1, this.root_);
      CharsTrie.State.access$202(var1, this.pos_);
      CharsTrie.State.access$302(var1, this.remainingMatchLength_);
      return this;
   }

   public CharsTrie resetToState(CharsTrie.State var1) {
      if (this.chars_ == CharsTrie.State.access$000(var1) && this.chars_ != null && this.root_ == CharsTrie.State.access$100(var1)) {
         this.pos_ = CharsTrie.State.access$200(var1);
         this.remainingMatchLength_ = CharsTrie.State.access$300(var1);
         return this;
      } else {
         throw new IllegalArgumentException("incompatible trie state");
      }
   }

   public BytesTrie.Result current() {
      int var1 = this.pos_;
      if (var1 < 0) {
         return BytesTrie.Result.NO_MATCH;
      } else {
         char var2;
         return this.remainingMatchLength_ < 0 && (var2 = this.chars_.charAt(var1)) >= '@' ? valueResults_[var2 >> 15] : BytesTrie.Result.NO_VALUE;
      }
   }

   public BytesTrie.Result first(int var1) {
      this.remainingMatchLength_ = -1;
      return this.nextImpl(this.root_, var1);
   }

   public BytesTrie.Result firstForCodePoint(int var1) {
      return var1 <= 65535 ? this.first(var1) : (this.first(UTF16.getLeadSurrogate(var1)).hasNext() ? this.next(UTF16.getTrailSurrogate(var1)) : BytesTrie.Result.NO_MATCH);
   }

   public BytesTrie.Result next(int var1) {
      int var2 = this.pos_;
      if (var2 < 0) {
         return BytesTrie.Result.NO_MATCH;
      } else {
         int var3 = this.remainingMatchLength_;
         if (var3 >= 0) {
            if (var1 != this.chars_.charAt(var2++)) {
               this.stop();
               return BytesTrie.Result.NO_MATCH;
            } else {
               --var3;
               this.remainingMatchLength_ = var3;
               this.pos_ = var2;
               char var4;
               return var3 < 0 && (var4 = this.chars_.charAt(var2)) >= '@' ? valueResults_[var4 >> 15] : BytesTrie.Result.NO_VALUE;
            }
         } else {
            return this.nextImpl(var2, var1);
         }
      }
   }

   public BytesTrie.Result nextForCodePoint(int var1) {
      return var1 <= 65535 ? this.next(var1) : (this.next(UTF16.getLeadSurrogate(var1)).hasNext() ? this.next(UTF16.getTrailSurrogate(var1)) : BytesTrie.Result.NO_MATCH);
   }

   public BytesTrie.Result next(CharSequence var1, int var2, int var3) {
      if (var2 >= var3) {
         return this.current();
      } else {
         int var4 = this.pos_;
         if (var4 < 0) {
            return BytesTrie.Result.NO_MATCH;
         } else {
            int var5 = this.remainingMatchLength_;

            while(true) {
               label62:
               while(var2 != var3) {
                  char var6 = var1.charAt(var2++);
                  if (var5 < 0) {
                     this.remainingMatchLength_ = var5;
                     int var7 = this.chars_.charAt(var4++);

                     while(true) {
                        while(var7 >= 48) {
                           if (var7 < 64) {
                              var5 = var7 - 48;
                              if (var6 != this.chars_.charAt(var4)) {
                                 this.stop();
                                 return BytesTrie.Result.NO_MATCH;
                              }

                              ++var4;
                              --var5;
                              continue label62;
                           }

                           if ((var7 & '耀') != 0) {
                              this.stop();
                              return BytesTrie.Result.NO_MATCH;
                           }

                           var4 = skipNodeValue(var4, var7);
                           var7 &= 63;
                        }

                        BytesTrie.Result var8 = this.branchNext(var4, var7, var6);
                        if (var8 == BytesTrie.Result.NO_MATCH) {
                           return BytesTrie.Result.NO_MATCH;
                        }

                        if (var2 == var3) {
                           return var8;
                        }

                        if (var8 == BytesTrie.Result.FINAL_VALUE) {
                           this.stop();
                           return BytesTrie.Result.NO_MATCH;
                        }

                        var6 = var1.charAt(var2++);
                        var4 = this.pos_;
                        var7 = this.chars_.charAt(var4++);
                     }
                  } else {
                     if (var6 != this.chars_.charAt(var4)) {
                        this.stop();
                        return BytesTrie.Result.NO_MATCH;
                     }

                     ++var4;
                     --var5;
                  }
               }

               this.remainingMatchLength_ = var5;
               this.pos_ = var4;
               char var9;
               return var5 < 0 && (var9 = this.chars_.charAt(var4)) >= '@' ? valueResults_[var9 >> 15] : BytesTrie.Result.NO_VALUE;
            }
         }
      }
   }

   public int getValue() {
      int var1 = this.pos_;
      char var2 = this.chars_.charAt(var1++);
      if (!$assertionsDisabled && var2 < '@') {
         throw new AssertionError();
      } else {
         return (var2 & '耀') != 0 ? readValue(this.chars_, var1, var2 & 32767) : readNodeValue(this.chars_, var1, var2);
      }
   }

   public long getUniqueValue() {
      int var1 = this.pos_;
      if (var1 < 0) {
         return 0L;
      } else {
         long var2 = findUniqueValue(this.chars_, var1 + this.remainingMatchLength_ + 1, 0L);
         return var2 << 31 >> 31;
      }
   }

   public int getNextChars(Appendable var1) {
      int var2 = this.pos_;
      if (var2 < 0) {
         return 0;
      } else if (this.remainingMatchLength_ >= 0) {
         append(var1, this.chars_.charAt(var2));
         return 1;
      } else {
         int var3 = this.chars_.charAt(var2++);
         if (var3 >= 64) {
            if ((var3 & '耀') != 0) {
               return 0;
            }

            var2 = skipNodeValue(var2, var3);
            var3 &= 63;
         }

         if (var3 < 48) {
            if (var3 == 0) {
               var3 = this.chars_.charAt(var2++);
            }

            ++var3;
            getNextBranchChars(this.chars_, var2, var3, var1);
            return var3;
         } else {
            append(var1, this.chars_.charAt(var2));
            return 1;
         }
      }
   }

   public CharsTrie.Iterator iterator() {
      return new CharsTrie.Iterator(this.chars_, this.pos_, this.remainingMatchLength_, 0);
   }

   public CharsTrie.Iterator iterator(int var1) {
      return new CharsTrie.Iterator(this.chars_, this.pos_, this.remainingMatchLength_, var1);
   }

   public static CharsTrie.Iterator iterator(CharSequence var0, int var1, int var2) {
      return new CharsTrie.Iterator(var0, var1, -1, var2);
   }

   private void stop() {
      this.pos_ = -1;
   }

   private static int readValue(CharSequence var0, int var1, int var2) {
      int var3;
      if (var2 < 16384) {
         var3 = var2;
      } else if (var2 < 32767) {
         var3 = var2 - 16384 << 16 | var0.charAt(var1);
      } else {
         var3 = var0.charAt(var1) << 16 | var0.charAt(var1 + 1);
      }

      return var3;
   }

   private static int skipValue(int var0, int var1) {
      if (var1 >= 16384) {
         if (var1 < 32767) {
            ++var0;
         } else {
            var0 += 2;
         }
      }

      return var0;
   }

   private static int skipValue(CharSequence var0, int var1) {
      char var2 = var0.charAt(var1++);
      return skipValue(var1, var2 & 32767);
   }

   private static int readNodeValue(CharSequence var0, int var1, int var2) {
      if (!$assertionsDisabled && (64 > var2 || var2 >= 32768)) {
         throw new AssertionError();
      } else {
         int var3;
         if (var2 < 16448) {
            var3 = (var2 >> 6) - 1;
         } else if (var2 < 32704) {
            var3 = (var2 & 32704) - 16448 << 10 | var0.charAt(var1);
         } else {
            var3 = var0.charAt(var1) << 16 | var0.charAt(var1 + 1);
         }

         return var3;
      }
   }

   private static int skipNodeValue(int var0, int var1) {
      if ($assertionsDisabled || 64 <= var1 && var1 < 32768) {
         if (var1 >= 16448) {
            if (var1 < 32704) {
               ++var0;
            } else {
               var0 += 2;
            }
         }

         return var0;
      } else {
         throw new AssertionError();
      }
   }

   private static int jumpByDelta(CharSequence var0, int var1) {
      int var2 = var0.charAt(var1++);
      if (var2 >= 64512) {
         if (var2 == 65535) {
            var2 = var0.charAt(var1) << 16 | var0.charAt(var1 + 1);
            var1 += 2;
         } else {
            var2 = var2 - 'ﰀ' << 16 | var0.charAt(var1++);
         }
      }

      return var1 + var2;
   }

   private static int skipDelta(CharSequence var0, int var1) {
      char var2 = var0.charAt(var1++);
      if (var2 >= 'ﰀ') {
         if (var2 == '\uffff') {
            var1 += 2;
         } else {
            ++var1;
         }
      }

      return var1;
   }

   private BytesTrie.Result branchNext(int var1, int var2, int var3) {
      if (var2 == 0) {
         var2 = this.chars_.charAt(var1++);
      }

      ++var2;

      while(var2 > 5) {
         if (var3 < this.chars_.charAt(var1++)) {
            var2 >>= 1;
            var1 = jumpByDelta(this.chars_, var1);
         } else {
            var2 -= var2 >> 1;
            var1 = skipDelta(this.chars_, var1);
         }
      }

      while(var3 != this.chars_.charAt(var1++)) {
         --var2;
         var1 = skipValue(this.chars_, var1);
         if (var2 <= 1) {
            if (var3 == this.chars_.charAt(var1++)) {
               this.pos_ = var1;
               char var4 = this.chars_.charAt(var1);
               return var4 >= '@' ? valueResults_[var4 >> 15] : BytesTrie.Result.NO_VALUE;
            }

            this.stop();
            return BytesTrie.Result.NO_MATCH;
         }
      }

      char var5 = this.chars_.charAt(var1);
      BytesTrie.Result var7;
      if ((var5 & '耀') != 0) {
         var7 = BytesTrie.Result.FINAL_VALUE;
      } else {
         ++var1;
         int var6;
         if (var5 < 16384) {
            var6 = var5;
         } else if (var5 < 32767) {
            var6 = var5 - 16384 << 16 | this.chars_.charAt(var1++);
         } else {
            var6 = this.chars_.charAt(var1) << 16 | this.chars_.charAt(var1 + 1);
            var1 += 2;
         }

         var1 += var6;
         var5 = this.chars_.charAt(var1);
         var7 = var5 >= '@' ? valueResults_[var5 >> 15] : BytesTrie.Result.NO_VALUE;
      }

      this.pos_ = var1;
      return var7;
   }

   private BytesTrie.Result nextImpl(int var1, int var2) {
      int var3 = this.chars_.charAt(var1++);

      while(true) {
         if (var3 < 48) {
            return this.branchNext(var1, var3, var2);
         }

         if (var3 < 64) {
            int var4 = var3 - 48;
            if (var2 == this.chars_.charAt(var1++)) {
               --var4;
               this.remainingMatchLength_ = var4;
               this.pos_ = var1;
               char var5;
               return var4 < 0 && (var5 = this.chars_.charAt(var1)) >= '@' ? valueResults_[var5 >> 15] : BytesTrie.Result.NO_VALUE;
            }
            break;
         }

         if ((var3 & '耀') != 0) {
            break;
         }

         var1 = skipNodeValue(var1, var3);
         var3 &= 63;
      }

      this.stop();
      return BytesTrie.Result.NO_MATCH;
   }

   private static long findUniqueValueFromBranch(CharSequence var0, int var1, int var2, long var3) {
      while(var2 > 5) {
         ++var1;
         var3 = findUniqueValueFromBranch(var0, jumpByDelta(var0, var1), var2 >> 1, var3);
         if (var3 == 0L) {
            return 0L;
         }

         var2 -= var2 >> 1;
         var1 = skipDelta(var0, var1);
      }

      do {
         ++var1;
         char var5 = var0.charAt(var1++);
         boolean var6 = (var5 & '耀') != 0;
         int var8 = var5 & 32767;
         int var7 = readValue(var0, var1, var8);
         var1 = skipValue(var1, var8);
         if (var6) {
            if (var3 != 0L) {
               if (var7 != (int)(var3 >> 1)) {
                  return 0L;
               }
            } else {
               var3 = (long)var7 << 1 | 1L;
            }
         } else {
            var3 = findUniqueValue(var0, var1 + var7, var3);
            if (var3 == 0L) {
               return 0L;
            }
         }

         --var2;
      } while(var2 > 1);

      return (long)(var1 + 1) << 33 | var3 & 8589934591L;
   }

   private static long findUniqueValue(CharSequence var0, int var1, long var2) {
      int var4 = var0.charAt(var1++);

      while(true) {
         while(var4 >= 48) {
            if (var4 < 64) {
               var1 += var4 - 48 + 1;
               var4 = var0.charAt(var1++);
            } else {
               boolean var5 = (var4 & '耀') != 0;
               int var6;
               if (var5) {
                  var6 = readValue(var0, var1, var4 & 32767);
               } else {
                  var6 = readNodeValue(var0, var1, var4);
               }

               if (var2 != 0L) {
                  if (var6 != (int)(var2 >> 1)) {
                     return 0L;
                  }
               } else {
                  var2 = (long)var6 << 1 | 1L;
               }

               if (var5) {
                  return var2;
               }

               var1 = skipNodeValue(var1, var4);
               var4 &= 63;
            }
         }

         if (var4 == 0) {
            var4 = var0.charAt(var1++);
         }

         var2 = findUniqueValueFromBranch(var0, var1, var4 + 1, var2);
         if (var2 == 0L) {
            return 0L;
         }

         var1 = (int)(var2 >>> 33);
         var4 = var0.charAt(var1++);
      }
   }

   private static void getNextBranchChars(CharSequence var0, int var1, int var2, Appendable var3) {
      while(var2 > 5) {
         ++var1;
         getNextBranchChars(var0, jumpByDelta(var0, var1), var2 >> 1, var3);
         var2 -= var2 >> 1;
         var1 = skipDelta(var0, var1);
      }

      do {
         append(var3, var0.charAt(var1++));
         var1 = skipValue(var0, var1);
         --var2;
      } while(var2 > 1);

      append(var3, var0.charAt(var1));
   }

   private static void append(Appendable var0, int var1) {
      try {
         var0.append((char)var1);
      } catch (IOException var3) {
         throw new RuntimeException(var3);
      }
   }

   public java.util.Iterator iterator() {
      return this.iterator();
   }

   static int access$500(int var0, int var1) {
      return skipNodeValue(var0, var1);
   }

   static int access$600(CharSequence var0, int var1, int var2) {
      return readValue(var0, var1, var2);
   }

   static int access$700(CharSequence var0, int var1, int var2) {
      return readNodeValue(var0, var1, var2);
   }

   static int access$800(CharSequence var0, int var1) {
      return skipDelta(var0, var1);
   }

   static int access$900(CharSequence var0, int var1) {
      return jumpByDelta(var0, var1);
   }

   static int access$1000(int var0, int var1) {
      return skipValue(var0, var1);
   }

   static {
      valueResults_ = new BytesTrie.Result[]{BytesTrie.Result.INTERMEDIATE_VALUE, BytesTrie.Result.FINAL_VALUE};
   }

   public static final class Iterator implements java.util.Iterator {
      private CharSequence chars_;
      private int pos_;
      private int initialPos_;
      private int remainingMatchLength_;
      private int initialRemainingMatchLength_;
      private boolean skipValue_;
      private StringBuilder str_;
      private int maxLength_;
      private CharsTrie.Entry entry_;
      private ArrayList stack_;

      private Iterator(CharSequence var1, int var2, int var3, int var4) {
         this.str_ = new StringBuilder();
         this.entry_ = new CharsTrie.Entry();
         this.stack_ = new ArrayList();
         this.chars_ = var1;
         this.pos_ = this.initialPos_ = var2;
         this.remainingMatchLength_ = this.initialRemainingMatchLength_ = var3;
         this.maxLength_ = var4;
         int var5 = this.remainingMatchLength_;
         if (var5 >= 0) {
            ++var5;
            if (this.maxLength_ > 0 && var5 > this.maxLength_) {
               var5 = this.maxLength_;
            }

            this.str_.append(this.chars_, this.pos_, this.pos_ + var5);
            this.pos_ += var5;
            this.remainingMatchLength_ -= var5;
         }

      }

      public CharsTrie.Iterator reset() {
         this.pos_ = this.initialPos_;
         this.remainingMatchLength_ = this.initialRemainingMatchLength_;
         this.skipValue_ = false;
         int var1 = this.remainingMatchLength_ + 1;
         if (this.maxLength_ > 0 && var1 > this.maxLength_) {
            var1 = this.maxLength_;
         }

         this.str_.setLength(var1);
         this.pos_ += var1;
         this.remainingMatchLength_ -= var1;
         this.stack_.clear();
         return this;
      }

      public boolean hasNext() {
         return this.pos_ >= 0 || !this.stack_.isEmpty();
      }

      public CharsTrie.Entry next() {
         int var1 = this.pos_;
         if (var1 < 0) {
            if (this.stack_.isEmpty()) {
               throw new NoSuchElementException();
            }

            long var2 = (Long)this.stack_.remove(this.stack_.size() - 1);
            int var4 = (int)var2;
            var1 = (int)(var2 >> 32);
            this.str_.setLength(var4 & '\uffff');
            var4 >>>= 16;
            if (var4 > 1) {
               var1 = this.branchNext(var1, var4);
               if (var1 < 0) {
                  return this.entry_;
               }
            } else {
               this.str_.append(this.chars_.charAt(var1++));
            }
         }

         if (this.remainingMatchLength_ >= 0) {
            return this.truncateAndStop();
         } else {
            while(true) {
               int var5 = this.chars_.charAt(var1++);
               if (var5 >= 64) {
                  if (!this.skipValue_) {
                     boolean var6 = (var5 & '耀') != 0;
                     if (var6) {
                        this.entry_.value = CharsTrie.access$600(this.chars_, var1, var5 & 32767);
                     } else {
                        this.entry_.value = CharsTrie.access$700(this.chars_, var1, var5);
                     }

                     if (!var6 && (this.maxLength_ <= 0 || this.str_.length() != this.maxLength_)) {
                        this.pos_ = var1 - 1;
                        this.skipValue_ = true;
                     } else {
                        this.pos_ = -1;
                     }

                     this.entry_.chars = this.str_;
                     return this.entry_;
                  }

                  var1 = CharsTrie.access$500(var1, var5);
                  var5 &= 63;
                  this.skipValue_ = false;
               }

               if (this.maxLength_ > 0 && this.str_.length() == this.maxLength_) {
                  return this.truncateAndStop();
               }

               if (var5 < 48) {
                  if (var5 == 0) {
                     var5 = this.chars_.charAt(var1++);
                  }

                  var1 = this.branchNext(var1, var5 + 1);
                  if (var1 < 0) {
                     return this.entry_;
                  }
               } else {
                  int var3 = var5 - 48 + 1;
                  if (this.maxLength_ > 0 && this.str_.length() + var3 > this.maxLength_) {
                     this.str_.append(this.chars_, var1, var1 + this.maxLength_ - this.str_.length());
                     return this.truncateAndStop();
                  }

                  this.str_.append(this.chars_, var1, var1 + var3);
                  var1 += var3;
               }
            }
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      private CharsTrie.Entry truncateAndStop() {
         this.pos_ = -1;
         this.entry_.chars = this.str_;
         this.entry_.value = -1;
         return this.entry_;
      }

      private int branchNext(int var1, int var2) {
         while(var2 > 5) {
            ++var1;
            this.stack_.add((long)CharsTrie.access$800(this.chars_, var1) << 32 | (long)(var2 - (var2 >> 1) << 16) | (long)this.str_.length());
            var2 >>= 1;
            var1 = CharsTrie.access$900(this.chars_, var1);
         }

         char var3 = this.chars_.charAt(var1++);
         char var4 = this.chars_.charAt(var1++);
         boolean var5 = (var4 & '耀') != 0;
         int var7;
         int var6 = CharsTrie.access$600(this.chars_, var1, var7 = var4 & 32767);
         var1 = CharsTrie.access$1000(var1, var7);
         this.stack_.add((long)var1 << 32 | (long)(var2 - 1 << 16) | (long)this.str_.length());
         this.str_.append(var3);
         if (var5) {
            this.pos_ = -1;
            this.entry_.chars = this.str_;
            this.entry_.value = var6;
            return -1;
         } else {
            return var1 + var6;
         }
      }

      public Object next() {
         return this.next();
      }

      Iterator(CharSequence var1, int var2, int var3, int var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   public static final class Entry {
      public CharSequence chars;
      public int value;

      private Entry() {
      }

      Entry(Object var1) {
         this();
      }
   }

   public static final class State {
      private CharSequence chars;
      private int root;
      private int pos;
      private int remainingMatchLength;

      static CharSequence access$002(CharsTrie.State var0, CharSequence var1) {
         return var0.chars = var1;
      }

      static int access$102(CharsTrie.State var0, int var1) {
         return var0.root = var1;
      }

      static int access$202(CharsTrie.State var0, int var1) {
         return var0.pos = var1;
      }

      static int access$302(CharsTrie.State var0, int var1) {
         return var0.remainingMatchLength = var1;
      }

      static CharSequence access$000(CharsTrie.State var0) {
         return var0.chars;
      }

      static int access$100(CharsTrie.State var0) {
         return var0.root;
      }

      static int access$200(CharsTrie.State var0) {
         return var0.pos;
      }

      static int access$300(CharsTrie.State var0) {
         return var0.remainingMatchLength;
      }
   }
}
