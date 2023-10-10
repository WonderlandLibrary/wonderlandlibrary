package com.ibm.icu.impl;

import java.util.Arrays;

public class TrieBuilder {
   public static final int DATA_BLOCK_LENGTH = 32;
   protected int[] m_index_ = new int['蠀'];
   protected int m_indexLength_;
   protected int m_dataCapacity_;
   protected int m_dataLength_;
   protected boolean m_isLatin1Linear_;
   protected boolean m_isCompacted_;
   protected int[] m_map_;
   protected static final int SHIFT_ = 5;
   protected static final int MAX_INDEX_LENGTH_ = 34816;
   protected static final int BMP_INDEX_LENGTH_ = 2048;
   protected static final int SURROGATE_BLOCK_COUNT_ = 32;
   protected static final int MASK_ = 31;
   protected static final int INDEX_SHIFT_ = 2;
   protected static final int MAX_DATA_LENGTH_ = 262144;
   protected static final int OPTIONS_INDEX_SHIFT_ = 4;
   protected static final int OPTIONS_DATA_IS_32_BIT_ = 256;
   protected static final int OPTIONS_LATIN1_IS_LINEAR_ = 512;
   protected static final int DATA_GRANULARITY_ = 4;
   private static final int MAX_BUILD_TIME_DATA_LENGTH_ = 1115168;

   public boolean isInZeroBlock(int var1) {
      if (!this.m_isCompacted_ && var1 <= 1114111 && var1 >= 0) {
         return this.m_index_[var1 >> 5] == 0;
      } else {
         return true;
      }
   }

   protected TrieBuilder() {
      this.m_map_ = new int['蠡'];
      this.m_isLatin1Linear_ = false;
      this.m_isCompacted_ = false;
      this.m_indexLength_ = 34816;
   }

   protected TrieBuilder(TrieBuilder var1) {
      this.m_indexLength_ = var1.m_indexLength_;
      System.arraycopy(var1.m_index_, 0, this.m_index_, 0, this.m_indexLength_);
      this.m_dataCapacity_ = var1.m_dataCapacity_;
      this.m_dataLength_ = var1.m_dataLength_;
      this.m_map_ = new int[var1.m_map_.length];
      System.arraycopy(var1.m_map_, 0, this.m_map_, 0, this.m_map_.length);
      this.m_isLatin1Linear_ = var1.m_isLatin1Linear_;
      this.m_isCompacted_ = var1.m_isCompacted_;
   }

   protected void findUnusedBlocks() {
      Arrays.fill(this.m_map_, 255);

      for(int var1 = 0; var1 < this.m_indexLength_; ++var1) {
         this.m_map_[Math.abs(this.m_index_[var1]) >> 5] = 0;
      }

      this.m_map_[0] = 0;
   }

   protected static final int findSameIndexBlock(int[] var0, int var1, int var2) {
      short var3 = 2048;
      return var3 < var1 ? var3 : var1;
   }

   public interface DataManipulate {
      int getFoldedValue(int var1, int var2);
   }
}
