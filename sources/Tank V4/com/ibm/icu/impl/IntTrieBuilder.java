package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class IntTrieBuilder extends TrieBuilder {
   protected int[] m_data_;
   protected int m_initialValue_;
   private int m_leadUnitValue_;

   public IntTrieBuilder(IntTrieBuilder var1) {
      super(var1);
      this.m_data_ = new int[this.m_dataCapacity_];
      System.arraycopy(var1.m_data_, 0, this.m_data_, 0, this.m_dataLength_);
      this.m_initialValue_ = var1.m_initialValue_;
      this.m_leadUnitValue_ = var1.m_leadUnitValue_;
   }

   public IntTrieBuilder(int[] var1, int var2, int var3, int var4, boolean var5) {
      if (var2 >= 32 && (!var5 || var2 >= 1024)) {
         if (var1 != null) {
            this.m_data_ = var1;
         } else {
            this.m_data_ = new int[var2];
         }

         int var6 = 32;
         if (var5) {
            int var7 = 0;

            do {
               this.m_index_[var7++] = var6;
               var6 += 32;
            } while(var7 < 8);
         }

         this.m_dataLength_ = var6;
         Arrays.fill(this.m_data_, 0, this.m_dataLength_, var3);
         this.m_initialValue_ = var3;
         this.m_leadUnitValue_ = var4;
         this.m_dataCapacity_ = var2;
         this.m_isLatin1Linear_ = var5;
         this.m_isCompacted_ = false;
      } else {
         throw new IllegalArgumentException("Argument maxdatalength is too small");
      }
   }

   public int getValue(int var1) {
      if (!this.m_isCompacted_ && var1 <= 1114111 && var1 >= 0) {
         int var2 = this.m_index_[var1 >> 5];
         return this.m_data_[Math.abs(var2) + (var1 & 31)];
      } else {
         return 0;
      }
   }

   public int getValue(int var1, boolean[] var2) {
      if (!this.m_isCompacted_ && var1 <= 1114111 && var1 >= 0) {
         int var3 = this.m_index_[var1 >> 5];
         if (var2 != null) {
            var2[0] = var3 == 0;
         }

         return this.m_data_[Math.abs(var3) + (var1 & 31)];
      } else {
         if (var2 != null) {
            var2[0] = true;
         }

         return 0;
      }
   }

   public IntTrie serialize(TrieBuilder.DataManipulate var1, Trie.DataManipulate var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("Parameters can not be null");
      } else {
         if (!this.m_isCompacted_) {
            this.compact(false);
            this.fold(var1);
            this.compact(true);
            this.m_isCompacted_ = true;
         }

         if (this.m_dataLength_ >= 262144) {
            throw new ArrayIndexOutOfBoundsException("Data length too small");
         } else {
            char[] var3 = new char[this.m_indexLength_];
            int[] var4 = new int[this.m_dataLength_];

            int var5;
            for(var5 = 0; var5 < this.m_indexLength_; ++var5) {
               var3[var5] = (char)(this.m_index_[var5] >>> 2);
            }

            System.arraycopy(this.m_data_, 0, var4, 0, this.m_dataLength_);
            byte var6 = 37;
            var5 = var6 | 256;
            if (this.m_isLatin1Linear_) {
               var5 |= 512;
            }

            return new IntTrie(var3, var4, this.m_initialValue_, var5, var2);
         }
      }
   }

   public int serialize(OutputStream var1, boolean var2, TrieBuilder.DataManipulate var3) throws IOException {
      if (var3 == null) {
         throw new IllegalArgumentException("Parameters can not be null");
      } else {
         if (!this.m_isCompacted_) {
            this.compact(false);
            this.fold(var3);
            this.compact(true);
            this.m_isCompacted_ = true;
         }

         int var4;
         if (var2) {
            var4 = this.m_dataLength_ + this.m_indexLength_;
         } else {
            var4 = this.m_dataLength_;
         }

         if (var4 >= 262144) {
            throw new ArrayIndexOutOfBoundsException("Data length too small");
         } else {
            var4 = 16 + 2 * this.m_indexLength_;
            if (var2) {
               var4 += 2 * this.m_dataLength_;
            } else {
               var4 += 4 * this.m_dataLength_;
            }

            if (var1 == null) {
               return var4;
            } else {
               DataOutputStream var5 = new DataOutputStream(var1);
               var5.writeInt(1416784229);
               int var6 = 37;
               if (!var2) {
                  var6 |= 256;
               }

               if (this.m_isLatin1Linear_) {
                  var6 |= 512;
               }

               var5.writeInt(var6);
               var5.writeInt(this.m_indexLength_);
               var5.writeInt(this.m_dataLength_);
               int var7;
               int var8;
               if (var2) {
                  for(var7 = 0; var7 < this.m_indexLength_; ++var7) {
                     var8 = this.m_index_[var7] + this.m_indexLength_ >>> 2;
                     var5.writeChar(var8);
                  }

                  for(var7 = 0; var7 < this.m_dataLength_; ++var7) {
                     var8 = this.m_data_[var7] & '\uffff';
                     var5.writeChar(var8);
                  }
               } else {
                  for(var7 = 0; var7 < this.m_indexLength_; ++var7) {
                     var8 = this.m_index_[var7] >>> 2;
                     var5.writeChar(var8);
                  }

                  for(var7 = 0; var7 < this.m_dataLength_; ++var7) {
                     var5.writeInt(this.m_data_[var7]);
                  }
               }

               return var4;
            }
         }
      }
   }

   public boolean setRange(int var1, int var2, int var3, boolean var4) {
      if (!this.m_isCompacted_ && var1 >= 0 && var1 <= 1114111 && var2 >= 0 && var2 <= 1114112 && var1 <= var2) {
         if (var1 == var2) {
            return true;
         } else {
            int var5;
            int var6;
            if ((var1 & 31) != 0) {
               var5 = this.getDataBlock(var1);
               if (var5 < 0) {
                  return false;
               }

               var6 = var1 + 32 & -32;
               if (var6 > var2) {
                  this.fillBlock(var5, var1 & 31, var2 & 31, var3, var4);
                  return true;
               }

               this.fillBlock(var5, var1 & 31, 32, var3, var4);
               var1 = var6;
            }

            var5 = var2 & 31;
            var2 &= -32;
            var6 = 0;
            if (var3 != this.m_initialValue_) {
               var6 = -1;
            }

            int var7;
            for(; var1 < var2; var1 += 32) {
               var7 = this.m_index_[var1 >> 5];
               if (var7 > 0) {
                  this.fillBlock(var7, 0, 32, var3, var4);
               } else if (this.m_data_[-var7] != var3 && (var7 == 0 || var4)) {
                  if (var6 >= 0) {
                     this.m_index_[var1 >> 5] = -var6;
                  } else {
                     var6 = this.getDataBlock(var1);
                     if (var6 < 0) {
                        return false;
                     }

                     this.m_index_[var1 >> 5] = -var6;
                     this.fillBlock(var6, 0, 32, var3, true);
                  }
               }
            }

            if (var5 > 0) {
               var7 = this.getDataBlock(var1);
               if (var7 < 0) {
                  return false;
               }

               this.fillBlock(var7, 0, var5, var3, var4);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private int allocDataBlock() {
      int var1 = this.m_dataLength_;
      int var2 = var1 + 32;
      if (var2 > this.m_dataCapacity_) {
         return -1;
      } else {
         this.m_dataLength_ = var2;
         return var1;
      }
   }

   private int getDataBlock(int var1) {
      var1 >>= 5;
      int var2 = this.m_index_[var1];
      if (var2 > 0) {
         return var2;
      } else {
         int var3 = this.allocDataBlock();
         if (var3 < 0) {
            return -1;
         } else {
            this.m_index_[var1] = var3;
            System.arraycopy(this.m_data_, Math.abs(var2), this.m_data_, var3, 128);
            return var3;
         }
      }
   }

   private void compact(boolean var1) {
      if (!this.m_isCompacted_) {
         this.findUnusedBlocks();
         int var2 = 32;
         if (this.m_isLatin1Linear_) {
            var2 += 256;
         }

         int var3 = 32;
         int var5 = var3;

         while(true) {
            int var4;
            while(var5 < this.m_dataLength_) {
               if (this.m_map_[var5 >>> 5] < 0) {
                  var5 += 32;
               } else {
                  if (var5 >= var2) {
                     var4 = findSameDataBlock(this.m_data_, var3, var5, var1 ? 4 : 32);
                     if (var4 >= 0) {
                        this.m_map_[var5 >>> 5] = var4;
                        var5 += 32;
                        continue;
                     }
                  }

                  if (var1 && var5 >= var2) {
                     for(var4 = 28; var4 > 0 && !equal_int(this.m_data_, var3 - var4, var5, var4); var4 -= 4) {
                     }
                  } else {
                     var4 = 0;
                  }

                  if (var4 > 0) {
                     this.m_map_[var5 >>> 5] = var3 - var4;
                     var5 += var4;

                     for(var4 = 32 - var4; var4 > 0; --var4) {
                        this.m_data_[var3++] = this.m_data_[var5++];
                     }
                  } else if (var3 < var5) {
                     this.m_map_[var5 >>> 5] = var3;

                     for(var4 = 32; var4 > 0; --var4) {
                        this.m_data_[var3++] = this.m_data_[var5++];
                     }
                  } else {
                     this.m_map_[var5 >>> 5] = var5;
                     var3 += 32;
                     var5 = var3;
                  }
               }
            }

            for(var4 = 0; var4 < this.m_indexLength_; ++var4) {
               this.m_index_[var4] = this.m_map_[Math.abs(this.m_index_[var4]) >>> 5];
            }

            this.m_dataLength_ = var3;
            return;
         }
      }
   }

   private static final int findSameDataBlock(int[] var0, int var1, int var2, int var3) {
      var1 -= 32;

      for(int var4 = 0; var4 <= var1; var4 += var3) {
         if (equal_int(var0, var4, var2, 32)) {
            return var4;
         }
      }

      return -1;
   }

   private final void fold(TrieBuilder.DataManipulate var1) {
      int[] var2 = new int[32];
      int[] var3 = this.m_index_;
      System.arraycopy(var3, 1728, var2, 0, 32);
      int var4 = 0;
      if (this.m_leadUnitValue_ != this.m_initialValue_) {
         var4 = this.allocDataBlock();
         if (var4 < 0) {
            throw new IllegalStateException("Internal error: Out of memory space");
         }

         this.fillBlock(var4, 0, 32, this.m_leadUnitValue_, true);
         var4 = -var4;
      }

      int var5;
      for(var5 = 1728; var5 < 1760; ++var5) {
         this.m_index_[var5] = var4;
      }

      var5 = 2048;
      int var6 = 65536;

      while(var6 < 1114112) {
         if (var3[var6 >> 5] != 0) {
            var6 &= -1024;
            var4 = findSameIndexBlock(var3, var5, var6 >> 5);
            int var7 = var1.getFoldedValue(var6, var4 + 32);
            if (var7 != this.getValue(UTF16.getLeadSurrogate(var6))) {
               UTF16.getLeadSurrogate(var6);
               if (var7 == 0) {
                  throw new ArrayIndexOutOfBoundsException("Data table overflow");
               }

               if (var4 == var5) {
                  System.arraycopy(var3, var6 >> 5, var3, var5, 32);
                  var5 += 32;
               }
            }

            var6 += 1024;
         } else {
            var6 += 32;
         }
      }

      if (var5 >= 34816) {
         throw new ArrayIndexOutOfBoundsException("Index table overflow");
      } else {
         System.arraycopy(var3, 2048, var3, 2080, var5 - 2048);
         System.arraycopy(var2, 0, var3, 2048, 32);
         var5 += 32;
         this.m_indexLength_ = var5;
      }
   }

   private void fillBlock(int var1, int var2, int var3, int var4, boolean var5) {
      var3 += var1;
      var1 += var2;
      if (var5) {
         while(var1 < var3) {
            this.m_data_[var1++] = var4;
         }
      } else {
         for(; var1 < var3; ++var1) {
            if (this.m_data_[var1] == this.m_initialValue_) {
               this.m_data_[var1] = var4;
            }
         }
      }

   }
}
