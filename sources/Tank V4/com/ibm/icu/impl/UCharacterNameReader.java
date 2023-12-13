package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

final class UCharacterNameReader implements ICUBinary.Authenticate {
   private DataInputStream m_dataInputStream_;
   private static final int GROUP_INFO_SIZE_ = 3;
   private int m_tokenstringindex_;
   private int m_groupindex_;
   private int m_groupstringindex_;
   private int m_algnamesindex_;
   private static final int ALG_INFO_SIZE_ = 12;
   private static final byte[] DATA_FORMAT_VERSION_ = new byte[]{1, 0, 0, 0};
   private static final byte[] DATA_FORMAT_ID_ = new byte[]{117, 110, 97, 109};

   public boolean isDataVersionAcceptable(byte[] var1) {
      return var1[0] == DATA_FORMAT_VERSION_[0];
   }

   protected UCharacterNameReader(InputStream var1) throws IOException {
      ICUBinary.readHeader(var1, DATA_FORMAT_ID_, this);
      this.m_dataInputStream_ = new DataInputStream(var1);
   }

   protected void read(UCharacterName var1) throws IOException {
      this.m_tokenstringindex_ = this.m_dataInputStream_.readInt();
      this.m_groupindex_ = this.m_dataInputStream_.readInt();
      this.m_groupstringindex_ = this.m_dataInputStream_.readInt();
      this.m_algnamesindex_ = this.m_dataInputStream_.readInt();
      char var2 = this.m_dataInputStream_.readChar();
      char[] var3 = new char[var2];

      for(char var4 = 0; var4 < var2; ++var4) {
         var3[var4] = this.m_dataInputStream_.readChar();
      }

      int var12 = this.m_groupindex_ - this.m_tokenstringindex_;
      byte[] var5 = new byte[var12];
      this.m_dataInputStream_.readFully(var5);
      var1.setToken(var3, var5);
      var2 = this.m_dataInputStream_.readChar();
      var1.setGroupCountSize(var2, 3);
      int var11 = var2 * 3;
      char[] var6 = new char[var11];

      for(int var7 = 0; var7 < var11; ++var7) {
         var6[var7] = this.m_dataInputStream_.readChar();
      }

      var12 = this.m_algnamesindex_ - this.m_groupstringindex_;
      byte[] var13 = new byte[var12];
      this.m_dataInputStream_.readFully(var13);
      var1.setGroup(var6, var13);
      var11 = this.m_dataInputStream_.readInt();
      UCharacterName.AlgorithmName[] var8 = new UCharacterName.AlgorithmName[var11];

      for(int var9 = 0; var9 < var11; ++var9) {
         UCharacterName.AlgorithmName var10 = this.readAlg();
         if (var10 == null) {
            throw new IOException("unames.icu read error: Algorithmic names creation error");
         }

         var8[var9] = var10;
      }

      var1.setAlgorithm(var8);
   }

   protected boolean authenticate(byte[] var1, byte[] var2) {
      return Arrays.equals(DATA_FORMAT_ID_, var1) && Arrays.equals(DATA_FORMAT_VERSION_, var2);
   }

   private UCharacterName.AlgorithmName readAlg() throws IOException {
      UCharacterName.AlgorithmName var1 = new UCharacterName.AlgorithmName();
      int var2 = this.m_dataInputStream_.readInt();
      int var3 = this.m_dataInputStream_.readInt();
      byte var4 = this.m_dataInputStream_.readByte();
      byte var5 = this.m_dataInputStream_.readByte();
      if (!var1.setInfo(var2, var3, var4, var5)) {
         return null;
      } else {
         int var6 = this.m_dataInputStream_.readChar();
         if (var4 == 1) {
            char[] var7 = new char[var5];

            for(int var8 = 0; var8 < var5; ++var8) {
               var7[var8] = this.m_dataInputStream_.readChar();
            }

            var1.setFactor(var7);
            var6 -= var5 << 1;
         }

         StringBuilder var10 = new StringBuilder();

         for(char var11 = (char)(this.m_dataInputStream_.readByte() & 255); var11 != 0; var11 = (char)(this.m_dataInputStream_.readByte() & 255)) {
            var10.append(var11);
         }

         var1.setPrefix(var10.toString());
         var6 -= 12 + var10.length() + 1;
         if (var6 > 0) {
            byte[] var9 = new byte[var6];
            this.m_dataInputStream_.readFully(var9);
            var1.setFactorString(var9);
         }

         return var1;
      }
   }
}
