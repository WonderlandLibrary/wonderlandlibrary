package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CharTrie extends Trie {
   private char m_initialValue_;
   private char[] m_data_;
   static final boolean $assertionsDisabled = !CharTrie.class.desiredAssertionStatus();

   public CharTrie(InputStream var1, Trie.DataManipulate var2) throws IOException {
      super(var1, var2);
      if (!this.isCharTrie()) {
         throw new IllegalArgumentException("Data given does not belong to a char trie.");
      }
   }

   public CharTrie(int var1, int var2, Trie.DataManipulate var3) {
      super(new char[2080], 512, var3);
      short var5 = 256;
      int var4 = 256;
      if (var2 != var1) {
         var4 += 32;
      }

      this.m_data_ = new char[var4];
      this.m_dataLength_ = var4;
      this.m_initialValue_ = (char)var1;

      int var6;
      for(var6 = 0; var6 < var5; ++var6) {
         this.m_data_[var6] = (char)var1;
      }

      if (var2 != var1) {
         char var8 = (char)(var5 >> 2);
         var6 = 1728;

         for(short var7 = 1760; var6 < var7; ++var6) {
            this.m_index_[var6] = var8;
         }

         int var9 = var5 + 32;

         for(var6 = var5; var6 < var9; ++var6) {
            this.m_data_[var6] = (char)var2;
         }
      }

   }

   public final char getCodePointValue(int var1) {
      int var2;
      if (0 <= var1 && var1 < 55296) {
         var2 = (this.m_index_[var1 >> 5] << 2) + (var1 & 31);
         return this.m_data_[var2];
      } else {
         var2 = this.getCodePointOffset(var1);
         return var2 >= 0 ? this.m_data_[var2] : this.m_initialValue_;
      }
   }

   public final char getLeadValue(char var1) {
      return this.m_data_[this.getLeadOffset(var1)];
   }

   public final char getBMPValue(char var1) {
      return this.m_data_[this.getBMPOffset(var1)];
   }

   public final char getSurrogateValue(char var1, char var2) {
      int var3 = this.getSurrogateOffset(var1, var2);
      return var3 > 0 ? this.m_data_[var3] : this.m_initialValue_;
   }

   public final char getTrailValue(int var1, char var2) {
      if (this.m_dataManipulate_ == null) {
         throw new NullPointerException("The field DataManipulate in this Trie is null");
      } else {
         int var3 = this.m_dataManipulate_.getFoldingOffset(var1);
         return var3 > 0 ? this.m_data_[this.getRawOffset(var3, (char)(var2 & 1023))] : this.m_initialValue_;
      }
   }

   public final char getLatin1LinearValue(char var1) {
      return this.m_data_[32 + this.m_dataOffset_ + var1];
   }

   public boolean equals(Object var1) {
      boolean var2 = super.equals(var1);
      if (var2 && var1 instanceof CharTrie) {
         CharTrie var3 = (CharTrie)var1;
         return this.m_initialValue_ == var3.m_initialValue_;
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
      DataInputStream var2 = new DataInputStream(var1);
      int var3 = this.m_dataOffset_ + this.m_dataLength_;
      this.m_index_ = new char[var3];

      for(int var4 = 0; var4 < var3; ++var4) {
         this.m_index_[var4] = var2.readChar();
      }

      this.m_data_ = this.m_index_;
      this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
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
}
