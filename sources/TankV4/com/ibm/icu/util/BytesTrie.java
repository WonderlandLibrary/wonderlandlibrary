package com.ibm.icu.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public final class BytesTrie implements Cloneable, Iterable {
   private static BytesTrie.Result[] valueResults_;
   static final int kMaxBranchLinearSubNodeLength = 5;
   static final int kMinLinearMatch = 16;
   static final int kMaxLinearMatchLength = 16;
   static final int kMinValueLead = 32;
   private static final int kValueIsFinal = 1;
   static final int kMinOneByteValueLead = 16;
   static final int kMaxOneByteValue = 64;
   static final int kMinTwoByteValueLead = 81;
   static final int kMaxTwoByteValue = 6911;
   static final int kMinThreeByteValueLead = 108;
   static final int kFourByteValueLead = 126;
   static final int kMaxThreeByteValue = 1179647;
   static final int kFiveByteValueLead = 127;
   static final int kMaxOneByteDelta = 191;
   static final int kMinTwoByteDeltaLead = 192;
   static final int kMinThreeByteDeltaLead = 240;
   static final int kFourByteDeltaLead = 254;
   static final int kFiveByteDeltaLead = 255;
   static final int kMaxTwoByteDelta = 12287;
   static final int kMaxThreeByteDelta = 917503;
   private byte[] bytes_;
   private int root_;
   private int pos_;
   private int remainingMatchLength_;
   static final boolean $assertionsDisabled = !BytesTrie.class.desiredAssertionStatus();

   public BytesTrie(byte[] var1, int var2) {
      this.bytes_ = var1;
      this.pos_ = this.root_ = var2;
      this.remainingMatchLength_ = -1;
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public BytesTrie reset() {
      this.pos_ = this.root_;
      this.remainingMatchLength_ = -1;
      return this;
   }

   public BytesTrie saveState(BytesTrie.State var1) {
      BytesTrie.State.access$002(var1, this.bytes_);
      BytesTrie.State.access$102(var1, this.root_);
      BytesTrie.State.access$202(var1, this.pos_);
      BytesTrie.State.access$302(var1, this.remainingMatchLength_);
      return this;
   }

   public BytesTrie resetToState(BytesTrie.State var1) {
      if (this.bytes_ == BytesTrie.State.access$000(var1) && this.bytes_ != null && this.root_ == BytesTrie.State.access$100(var1)) {
         this.pos_ = BytesTrie.State.access$200(var1);
         this.remainingMatchLength_ = BytesTrie.State.access$300(var1);
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
         int var2;
         return this.remainingMatchLength_ < 0 && (var2 = this.bytes_[var1] & 255) >= 32 ? valueResults_[var2 & 1] : BytesTrie.Result.NO_VALUE;
      }
   }

   public BytesTrie.Result first(int var1) {
      this.remainingMatchLength_ = -1;
      if (var1 < 0) {
         var1 += 256;
      }

      return this.nextImpl(this.root_, var1);
   }

   public BytesTrie.Result next(int var1) {
      int var2 = this.pos_;
      if (var2 < 0) {
         return BytesTrie.Result.NO_MATCH;
      } else {
         if (var1 < 0) {
            var1 += 256;
         }

         int var3 = this.remainingMatchLength_;
         if (var3 >= 0) {
            if (var1 != (this.bytes_[var2++] & 255)) {
               this.stop();
               return BytesTrie.Result.NO_MATCH;
            } else {
               --var3;
               this.remainingMatchLength_ = var3;
               this.pos_ = var2;
               int var4;
               return var3 < 0 && (var4 = this.bytes_[var2] & 255) >= 32 ? valueResults_[var4 & 1] : BytesTrie.Result.NO_VALUE;
            }
         } else {
            return this.nextImpl(var2, var1);
         }
      }
   }

   public BytesTrie.Result next(byte[] var1, int var2, int var3) {
      if (var2 >= var3) {
         return this.current();
      } else {
         int var4 = this.pos_;
         if (var4 < 0) {
            return BytesTrie.Result.NO_MATCH;
         } else {
            int var5 = this.remainingMatchLength_;

            while(true) {
               int var7;
               label65:
               while(var2 != var3) {
                  byte var6 = var1[var2++];
                  if (var5 < 0) {
                     this.remainingMatchLength_ = var5;

                     while(true) {
                        while(true) {
                           var7 = this.bytes_[var4++] & 255;
                           if (var7 < 16) {
                              BytesTrie.Result var8 = this.branchNext(var4, var7, var6 & 255);
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

                              var6 = var1[var2++];
                              var4 = this.pos_;
                           } else {
                              if (var7 < 32) {
                                 var5 = var7 - 16;
                                 if (var6 != this.bytes_[var4]) {
                                    this.stop();
                                    return BytesTrie.Result.NO_MATCH;
                                 }

                                 ++var4;
                                 --var5;
                                 continue label65;
                              }

                              if ((var7 & 1) != 0) {
                                 this.stop();
                                 return BytesTrie.Result.NO_MATCH;
                              }

                              var4 = skipValue(var4, var7);
                              if (!$assertionsDisabled && (this.bytes_[var4] & 255) >= 32) {
                                 throw new AssertionError();
                              }
                           }
                        }
                     }
                  } else {
                     if (var6 != this.bytes_[var4]) {
                        this.stop();
                        return BytesTrie.Result.NO_MATCH;
                     }

                     ++var4;
                     --var5;
                  }
               }

               this.remainingMatchLength_ = var5;
               this.pos_ = var4;
               return var5 < 0 && (var7 = this.bytes_[var4] & 255) >= 32 ? valueResults_[var7 & 1] : BytesTrie.Result.NO_VALUE;
            }
         }
      }
   }

   public int getValue() {
      int var1 = this.pos_;
      int var2 = this.bytes_[var1++] & 255;
      if (!$assertionsDisabled && var2 < 32) {
         throw new AssertionError();
      } else {
         return readValue(this.bytes_, var1, var2 >> 1);
      }
   }

   public long getUniqueValue() {
      int var1 = this.pos_;
      if (var1 < 0) {
         return 0L;
      } else {
         long var2 = findUniqueValue(this.bytes_, var1 + this.remainingMatchLength_ + 1, 0L);
         return var2 << 31 >> 31;
      }
   }

   public int getNextBytes(Appendable var1) {
      int var2 = this.pos_;
      if (var2 < 0) {
         return 0;
      } else if (this.remainingMatchLength_ >= 0) {
         append(var1, this.bytes_[var2] & 255);
         return 1;
      } else {
         int var3 = this.bytes_[var2++] & 255;
         if (var3 >= 32) {
            if ((var3 & 1) != 0) {
               return 0;
            }

            var2 = skipValue(var2, var3);
            var3 = this.bytes_[var2++] & 255;
            if (!$assertionsDisabled && var3 >= 32) {
               throw new AssertionError();
            }
         }

         if (var3 < 16) {
            if (var3 == 0) {
               var3 = this.bytes_[var2++] & 255;
            }

            ++var3;
            getNextBranchBytes(this.bytes_, var2, var3, var1);
            return var3;
         } else {
            append(var1, this.bytes_[var2] & 255);
            return 1;
         }
      }
   }

   public BytesTrie.Iterator iterator() {
      return new BytesTrie.Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, 0);
   }

   public BytesTrie.Iterator iterator(int var1) {
      return new BytesTrie.Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, var1);
   }

   public static BytesTrie.Iterator iterator(byte[] var0, int var1, int var2) {
      return new BytesTrie.Iterator(var0, var1, -1, var2);
   }

   private void stop() {
      this.pos_ = -1;
   }

   private static int readValue(byte[] var0, int var1, int var2) {
      int var3;
      if (var2 < 81) {
         var3 = var2 - 16;
      } else if (var2 < 108) {
         var3 = var2 - 81 << 8 | var0[var1] & 255;
      } else if (var2 < 126) {
         var3 = var2 - 108 << 16 | (var0[var1] & 255) << 8 | var0[var1 + 1] & 255;
      } else if (var2 == 126) {
         var3 = (var0[var1] & 255) << 16 | (var0[var1 + 1] & 255) << 8 | var0[var1 + 2] & 255;
      } else {
         var3 = var0[var1] << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
      }

      return var3;
   }

   private static int skipValue(int var0, int var1) {
      if (!$assertionsDisabled && var1 < 32) {
         throw new AssertionError();
      } else {
         if (var1 >= 162) {
            if (var1 < 216) {
               ++var0;
            } else if (var1 < 252) {
               var0 += 2;
            } else {
               var0 += 3 + (var1 >> 1 & 1);
            }
         }

         return var0;
      }
   }

   private static int skipValue(byte[] var0, int var1) {
      int var2 = var0[var1++] & 255;
      return skipValue(var1, var2);
   }

   private static int jumpByDelta(byte[] var0, int var1) {
      int var2 = var0[var1++] & 255;
      if (var2 >= 192) {
         if (var2 < 240) {
            var2 = var2 - 192 << 8 | var0[var1++] & 255;
         } else if (var2 < 254) {
            var2 = var2 - 240 << 16 | (var0[var1] & 255) << 8 | var0[var1 + 1] & 255;
            var1 += 2;
         } else if (var2 == 254) {
            var2 = (var0[var1] & 255) << 16 | (var0[var1 + 1] & 255) << 8 | var0[var1 + 2] & 255;
            var1 += 3;
         } else {
            var2 = var0[var1] << 24 | (var0[var1 + 1] & 255) << 16 | (var0[var1 + 2] & 255) << 8 | var0[var1 + 3] & 255;
            var1 += 4;
         }
      }

      return var1 + var2;
   }

   private static int skipDelta(byte[] var0, int var1) {
      int var2 = var0[var1++] & 255;
      if (var2 >= 192) {
         if (var2 < 240) {
            ++var1;
         } else if (var2 < 254) {
            var1 += 2;
         } else {
            var1 += 3 + (var2 & 1);
         }
      }

      return var1;
   }

   private BytesTrie.Result branchNext(int var1, int var2, int var3) {
      if (var2 == 0) {
         var2 = this.bytes_[var1++] & 255;
      }

      ++var2;

      while(var2 > 5) {
         if (var3 < (this.bytes_[var1++] & 255)) {
            var2 >>= 1;
            var1 = jumpByDelta(this.bytes_, var1);
         } else {
            var2 -= var2 >> 1;
            var1 = skipDelta(this.bytes_, var1);
         }
      }

      while(var3 != (this.bytes_[var1++] & 255)) {
         --var2;
         var1 = skipValue(this.bytes_, var1);
         if (var2 <= 1) {
            if (var3 == (this.bytes_[var1++] & 255)) {
               this.pos_ = var1;
               int var4 = this.bytes_[var1] & 255;
               return var4 >= 32 ? valueResults_[var4 & 1] : BytesTrie.Result.NO_VALUE;
            }

            this.stop();
            return BytesTrie.Result.NO_MATCH;
         }
      }

      int var5 = this.bytes_[var1] & 255;
      if (!$assertionsDisabled && var5 < 32) {
         throw new AssertionError();
      } else {
         BytesTrie.Result var7;
         if ((var5 & 1) != 0) {
            var7 = BytesTrie.Result.FINAL_VALUE;
         } else {
            ++var1;
            var5 >>= 1;
            int var6;
            if (var5 < 81) {
               var6 = var5 - 16;
            } else if (var5 < 108) {
               var6 = var5 - 81 << 8 | this.bytes_[var1++] & 255;
            } else if (var5 < 126) {
               var6 = var5 - 108 << 16 | (this.bytes_[var1] & 255) << 8 | this.bytes_[var1 + 1] & 255;
               var1 += 2;
            } else if (var5 == 126) {
               var6 = (this.bytes_[var1] & 255) << 16 | (this.bytes_[var1 + 1] & 255) << 8 | this.bytes_[var1 + 2] & 255;
               var1 += 3;
            } else {
               var6 = this.bytes_[var1] << 24 | (this.bytes_[var1 + 1] & 255) << 16 | (this.bytes_[var1 + 2] & 255) << 8 | this.bytes_[var1 + 3] & 255;
               var1 += 4;
            }

            var1 += var6;
            var5 = this.bytes_[var1] & 255;
            var7 = var5 >= 32 ? valueResults_[var5 & 1] : BytesTrie.Result.NO_VALUE;
         }

         this.pos_ = var1;
         return var7;
      }
   }

   private BytesTrie.Result nextImpl(int var1, int var2) {
      while(true) {
         int var3 = this.bytes_[var1++] & 255;
         if (var3 < 16) {
            return this.branchNext(var1, var3, var2);
         }

         if (var3 < 32) {
            int var4 = var3 - 16;
            if (var2 == (this.bytes_[var1++] & 255)) {
               --var4;
               this.remainingMatchLength_ = var4;
               this.pos_ = var1;
               return var4 < 0 && (var3 = this.bytes_[var1] & 255) >= 32 ? valueResults_[var3 & 1] : BytesTrie.Result.NO_VALUE;
            }
         } else if ((var3 & 1) == 0) {
            var1 = skipValue(var1, var3);
            if ($assertionsDisabled || (this.bytes_[var1] & 255) < 32) {
               continue;
            }

            throw new AssertionError();
         }

         this.stop();
         return BytesTrie.Result.NO_MATCH;
      }
   }

   private static long findUniqueValueFromBranch(byte[] var0, int var1, int var2, long var3) {
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
         int var5 = var0[var1++] & 255;
         boolean var6 = (var5 & 1) != 0;
         int var7 = readValue(var0, var1, var5 >> 1);
         var1 = skipValue(var1, var5);
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

   private static long findUniqueValue(byte[] var0, int var1, long var2) {
      while(true) {
         int var4 = var0[var1++] & 255;
         if (var4 < 16) {
            if (var4 == 0) {
               var4 = var0[var1++] & 255;
            }

            var2 = findUniqueValueFromBranch(var0, var1, var4 + 1, var2);
            if (var2 == 0L) {
               return 0L;
            }

            var1 = (int)(var2 >>> 33);
         } else if (var4 < 32) {
            var1 += var4 - 16 + 1;
         } else {
            boolean var5 = (var4 & 1) != 0;
            int var6 = readValue(var0, var1, var4 >> 1);
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

            var1 = skipValue(var1, var4);
         }
      }
   }

   private static void getNextBranchBytes(byte[] var0, int var1, int var2, Appendable var3) {
      while(var2 > 5) {
         ++var1;
         getNextBranchBytes(var0, jumpByDelta(var0, var1), var2 >> 1, var3);
         var2 -= var2 >> 1;
         var1 = skipDelta(var0, var1);
      }

      do {
         append(var3, var0[var1++] & 255);
         var1 = skipValue(var0, var1);
         --var2;
      } while(var2 > 1);

      append(var3, var0[var1] & 255);
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

   static int access$900(byte[] var0, int var1, int var2) {
      return readValue(var0, var1, var2);
   }

   static int access$1100(int var0, int var1) {
      return skipValue(var0, var1);
   }

   static int access$1200(byte[] var0, int var1) {
      return skipDelta(var0, var1);
   }

   static int access$1300(byte[] var0, int var1) {
      return jumpByDelta(var0, var1);
   }

   static {
      valueResults_ = new BytesTrie.Result[]{BytesTrie.Result.INTERMEDIATE_VALUE, BytesTrie.Result.FINAL_VALUE};
   }

   public static final class Iterator implements java.util.Iterator {
      private byte[] bytes_;
      private int pos_;
      private int initialPos_;
      private int remainingMatchLength_;
      private int initialRemainingMatchLength_;
      private int maxLength_;
      private BytesTrie.Entry entry_;
      private ArrayList stack_;

      private Iterator(byte[] var1, int var2, int var3, int var4) {
         this.stack_ = new ArrayList();
         this.bytes_ = var1;
         this.pos_ = this.initialPos_ = var2;
         this.remainingMatchLength_ = this.initialRemainingMatchLength_ = var3;
         this.maxLength_ = var4;
         this.entry_ = new BytesTrie.Entry(this.maxLength_ != 0 ? this.maxLength_ : 32);
         int var5 = this.remainingMatchLength_;
         if (var5 >= 0) {
            ++var5;
            if (this.maxLength_ > 0 && var5 > this.maxLength_) {
               var5 = this.maxLength_;
            }

            BytesTrie.Entry.access$600(this.entry_, this.bytes_, this.pos_, var5);
            this.pos_ += var5;
            this.remainingMatchLength_ -= var5;
         }

      }

      public BytesTrie.Iterator reset() {
         this.pos_ = this.initialPos_;
         this.remainingMatchLength_ = this.initialRemainingMatchLength_;
         int var1 = this.remainingMatchLength_ + 1;
         if (this.maxLength_ > 0 && var1 > this.maxLength_) {
            var1 = this.maxLength_;
         }

         BytesTrie.Entry.access$700(this.entry_, var1);
         this.pos_ += var1;
         this.remainingMatchLength_ -= var1;
         this.stack_.clear();
         return this;
      }

      public boolean hasNext() {
         return this.pos_ >= 0 || !this.stack_.isEmpty();
      }

      public BytesTrie.Entry next() {
         int var1 = this.pos_;
         if (var1 < 0) {
            if (this.stack_.isEmpty()) {
               throw new NoSuchElementException();
            }

            long var2 = (Long)this.stack_.remove(this.stack_.size() - 1);
            int var4 = (int)var2;
            var1 = (int)(var2 >> 32);
            BytesTrie.Entry.access$700(this.entry_, var4 & '\uffff');
            var4 >>>= 16;
            if (var4 > 1) {
               var1 = this.branchNext(var1, var4);
               if (var1 < 0) {
                  return this.entry_;
               }
            } else {
               BytesTrie.Entry.access$800(this.entry_, this.bytes_[var1++]);
            }
         }

         if (this.remainingMatchLength_ >= 0) {
            return this.truncateAndStop();
         } else {
            while(true) {
               int var5 = this.bytes_[var1++] & 255;
               if (var5 >= 32) {
                  boolean var6 = (var5 & 1) != 0;
                  this.entry_.value = BytesTrie.access$900(this.bytes_, var1, var5 >> 1);
                  if (!var6 && (this.maxLength_ <= 0 || BytesTrie.Entry.access$1000(this.entry_) != this.maxLength_)) {
                     this.pos_ = BytesTrie.access$1100(var1, var5);
                  } else {
                     this.pos_ = -1;
                  }

                  return this.entry_;
               }

               if (this.maxLength_ > 0 && BytesTrie.Entry.access$1000(this.entry_) == this.maxLength_) {
                  return this.truncateAndStop();
               }

               if (var5 < 16) {
                  if (var5 == 0) {
                     var5 = this.bytes_[var1++] & 255;
                  }

                  var1 = this.branchNext(var1, var5 + 1);
                  if (var1 < 0) {
                     return this.entry_;
                  }
               } else {
                  int var3 = var5 - 16 + 1;
                  if (this.maxLength_ > 0 && BytesTrie.Entry.access$1000(this.entry_) + var3 > this.maxLength_) {
                     BytesTrie.Entry.access$600(this.entry_, this.bytes_, var1, this.maxLength_ - BytesTrie.Entry.access$1000(this.entry_));
                     return this.truncateAndStop();
                  }

                  BytesTrie.Entry.access$600(this.entry_, this.bytes_, var1, var3);
                  var1 += var3;
               }
            }
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }

      private BytesTrie.Entry truncateAndStop() {
         this.pos_ = -1;
         this.entry_.value = -1;
         return this.entry_;
      }

      private int branchNext(int var1, int var2) {
         while(var2 > 5) {
            ++var1;
            this.stack_.add((long)BytesTrie.access$1200(this.bytes_, var1) << 32 | (long)(var2 - (var2 >> 1) << 16) | (long)BytesTrie.Entry.access$1000(this.entry_));
            var2 >>= 1;
            var1 = BytesTrie.access$1300(this.bytes_, var1);
         }

         byte var3 = this.bytes_[var1++];
         int var4 = this.bytes_[var1++] & 255;
         boolean var5 = (var4 & 1) != 0;
         int var6 = BytesTrie.access$900(this.bytes_, var1, var4 >> 1);
         var1 = BytesTrie.access$1100(var1, var4);
         this.stack_.add((long)var1 << 32 | (long)(var2 - 1 << 16) | (long)BytesTrie.Entry.access$1000(this.entry_));
         BytesTrie.Entry.access$800(this.entry_, var3);
         if (var5) {
            this.pos_ = -1;
            this.entry_.value = var6;
            return -1;
         } else {
            return var1 + var6;
         }
      }

      public Object next() {
         return this.next();
      }

      Iterator(byte[] var1, int var2, int var3, int var4, Object var5) {
         this(var1, var2, var3, var4);
      }
   }

   public static final class Entry {
      public int value;
      private byte[] bytes;
      private int length;

      private Entry(int var1) {
         this.bytes = new byte[var1];
      }

      public int bytesLength() {
         return this.length;
      }

      public byte byteAt(int var1) {
         return this.bytes[var1];
      }

      public void copyBytesTo(byte[] var1, int var2) {
         System.arraycopy(this.bytes, 0, var1, var2, this.length);
      }

      public ByteBuffer bytesAsByteBuffer() {
         return ByteBuffer.wrap(this.bytes, 0, this.length).asReadOnlyBuffer();
      }

      private void ensureCapacity(int var1) {
         if (this.bytes.length < var1) {
            byte[] var2 = new byte[Math.min(2 * this.bytes.length, 2 * var1)];
            System.arraycopy(this.bytes, 0, var2, 0, this.length);
            this.bytes = var2;
         }

      }

      private void append(byte var1) {
         this.ensureCapacity(this.length + 1);
         this.bytes[this.length++] = var1;
      }

      private void append(byte[] var1, int var2, int var3) {
         this.ensureCapacity(this.length + var3);
         System.arraycopy(var1, var2, this.bytes, this.length, var3);
         this.length += var3;
      }

      private void truncateString(int var1) {
         this.length = var1;
      }

      Entry(int var1, Object var2) {
         this(var1);
      }

      static void access$600(BytesTrie.Entry var0, byte[] var1, int var2, int var3) {
         var0.append(var1, var2, var3);
      }

      static void access$700(BytesTrie.Entry var0, int var1) {
         var0.truncateString(var1);
      }

      static void access$800(BytesTrie.Entry var0, byte var1) {
         var0.append(var1);
      }

      static int access$1000(BytesTrie.Entry var0) {
         return var0.length;
      }
   }

   public static enum Result {
      NO_MATCH,
      NO_VALUE,
      FINAL_VALUE,
      INTERMEDIATE_VALUE;

      private static final BytesTrie.Result[] $VALUES = new BytesTrie.Result[]{NO_MATCH, NO_VALUE, FINAL_VALUE, INTERMEDIATE_VALUE};

      public boolean matches() {
         return this != NO_MATCH;
      }

      public boolean hasValue() {
         return this.ordinal() >= 2;
      }

      public boolean hasNext() {
         return (this.ordinal() & 1) != 0;
      }
   }

   public static final class State {
      private byte[] bytes;
      private int root;
      private int pos;
      private int remainingMatchLength;

      static byte[] access$002(BytesTrie.State var0, byte[] var1) {
         return var0.bytes = var1;
      }

      static int access$102(BytesTrie.State var0, int var1) {
         return var0.root = var1;
      }

      static int access$202(BytesTrie.State var0, int var1) {
         return var0.pos = var1;
      }

      static int access$302(BytesTrie.State var0, int var1) {
         return var0.remainingMatchLength = var1;
      }

      static byte[] access$000(BytesTrie.State var0) {
         return var0.bytes;
      }

      static int access$100(BytesTrie.State var0) {
         return var0.root;
      }

      static int access$200(BytesTrie.State var0) {
         return var0.pos;
      }

      static int access$300(BytesTrie.State var0) {
         return var0.remainingMatchLength;
      }
   }
}
