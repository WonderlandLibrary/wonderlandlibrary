package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class IntTrie extends Trie {
   private int m_initialValue_;
   private int[] m_data_;
   static final boolean $assertionsDisabled = !IntTrie.class.desiredAssertionStatus();

   public IntTrie(InputStream var1, Trie.DataManipulate var2) throws IOException {
      super(var1, var2);
      if (!this.isIntTrie()) {
         throw new IllegalArgumentException("Data given does not belong to a int trie.");
      }
   }

   public IntTrie(int var1, int var2, Trie.DataManipulate var3) {
      super(new char[2080], 512, var3);
      short var5 = 256;
      int var4 = 256;
      if (var2 != var1) {
         var4 += 32;
      }

      this.m_data_ = new int[var4];
      this.m_dataLength_ = var4;
      this.m_initialValue_ = var1;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         this.m_data_[var6] = var1;
      }

      if (var2 != var1) {
         char var8 = (char)(var5 >> 2);
         var6 = 1728;

         for(short var7 = 1760; var6 < var7; ++var6) {
            this.m_index_[var6] = var8;
         }

         int var9 = var5 + 32;

         for(var6 = var5; var6 < var9; ++var6) {
            this.m_data_[var6] = var2;
         }
      }

   }

   public final int getCodePointValue(int var1) {
      int var2;
      if (0 <= var1 && var1 < 55296) {
         var2 = (this.m_index_[var1 >> 5] << 2) + (var1 & 31);
         return this.m_data_[var2];
      } else {
         var2 = this.getCodePointOffset(var1);
         return var2 >= 0 ? this.m_data_[var2] : this.m_initialValue_;
      }
   }

   public final int getLeadValue(char var1) {
      return this.m_data_[this.getLeadOffset(var1)];
   }

   public final int getBMPValue(char var1) {
      return this.m_data_[this.getBMPOffset(var1)];
   }

   public final int getSurrogateValue(char var1, char var2) {
      if (UTF16.isLeadSurrogate(var1) && UTF16.isTrailSurrogate(var2)) {
         int var3 = this.getSurrogateOffset(var1, var2);
         return var3 > 0 ? this.m_data_[var3] : this.m_initialValue_;
      } else {
         throw new IllegalArgumentException("Argument characters do not form a supplementary character");
      }
   }

   public final int getTrailValue(int var1, char var2) {
      if (this.m_dataManipulate_ == null) {
         throw new NullPointerException("The field DataManipulate in this Trie is null");
      } else {
         int var3 = this.m_dataManipulate_.getFoldingOffset(var1);
         return var3 > 0 ? this.m_data_[this.getRawOffset(var3, (char)(var2 & 1023))] : this.m_initialValue_;
      }
   }

   public final int getLatin1LinearValue(char var1) {
      return this.m_data_[32 + var1];
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      if (var2 && var1 instanceof IntTrie) {
         IntTrie var3 = (IntTrie)var1;
         return this.m_initialValue_ == var3.m_initialValue_ && Arrays.equals(this.m_data_, var3.m_data_);
      } else {
         return false;
      }
   }

   public int hashCode() {
      if (!$assertionsDisabled) {
         throw new AssertionError("hashCode not designed");
      } else {
         return 42;
      }
   }

   protected final void unserialize(InputStream var1) throws IOException {
      super.unserialize(var1);
      this.m_data_ = new int[this.m_dataLength_];
      DataInputStream var2 = new DataInputStream(var1);

      for(int var3 = 0; var3 < this.m_dataLength_; ++var3) {
         this.m_data_[var3] = var2.readInt();
      }

      this.m_initialValue_ = this.m_data_[0];
   }

   protected final int getSurrogateOffset(char var1, char var2) {
      if (this.m_dataManipulate_ == null) {
         throw new NullPointerException("The field DataManipulate in this Trie is null");
      } else {
         int var3 = this.m_dataManipulate_.getFoldingOffset(this.getLeadValue(var1));
         return var3 > 0 ? this.getRawOffset(var3, (char)(var2 & 1023)) : -1;
      }
   }

   protected final int getValue(int var1) {
      return this.m_data_[var1];
   }

   protected final int getInitialValue() {
      return this.m_initialValue_;
   }

   IntTrie(char[] var1, int[] var2, int var3, int var4, Trie.DataManipulate var5) {
      super(var1, var4, var5);
      this.m_data_ = var2;
      this.m_dataLength_ = this.m_data_.length;
      this.m_initialValue_ = var3;
   }
}
