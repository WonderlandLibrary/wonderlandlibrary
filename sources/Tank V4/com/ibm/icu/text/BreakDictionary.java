package com.ibm.icu.text;

import com.ibm.icu.util.CompactByteArray;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

class BreakDictionary {
   private char[] reverseColumnMap = null;
   private CompactByteArray columnMap = null;
   private int numCols;
   private short[] table = null;
   private short[] rowIndex = null;
   private int[] rowIndexFlags = null;
   private short[] rowIndexFlagsIndex = null;
   private byte[] rowIndexShifts = null;

   static void writeToFile(String var0, String var1) throws FileNotFoundException, UnsupportedEncodingException, IOException {
      BreakDictionary var2 = new BreakDictionary(new FileInputStream(var0));
      PrintWriter var3 = null;
      if (var1 != null) {
         var3 = new PrintWriter(new OutputStreamWriter(new FileOutputStream(var1), "UnicodeLittle"));
      }

      var2.printWordList("", 0, var3);
      if (var3 != null) {
         var3.close();
      }

   }

   void printWordList(String var1, int var2, PrintWriter var3) throws IOException {
      if (var2 == 65535) {
         System.out.println(var1);
         if (var3 != null) {
            var3.println(var1);
         }
      } else {
         for(int var4 = 0; var4 < this.numCols; ++var4) {
            int var5 = this.at(var2, var4) & '\uffff';
            if (var5 != 0) {
               char var6 = this.reverseColumnMap[var4];
               String var7 = var1;
               if (var6 != 0) {
                  var7 = var1 + var6;
               }

               this.printWordList(var7, var5, var3);
            }
         }
      }

   }

   BreakDictionary(InputStream var1) throws IOException {
      this.readDictionaryFile(new DataInputStream(var1));
   }

   void readDictionaryFile(DataInputStream var1) throws IOException {
      var1.readInt();
      int var2 = var1.readInt();
      char[] var3 = new char[var2];

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = (char)var1.readShort();
      }

      var2 = var1.readInt();
      byte[] var7 = new byte[var2];

      int var5;
      for(var5 = 0; var5 < var7.length; ++var5) {
         var7[var5] = var1.readByte();
      }

      this.columnMap = new CompactByteArray(var3, var7);
      this.numCols = var1.readInt();
      var1.readInt();
      var2 = var1.readInt();
      this.rowIndex = new short[var2];

      for(var5 = 0; var5 < this.rowIndex.length; ++var5) {
         this.rowIndex[var5] = var1.readShort();
      }

      var2 = var1.readInt();
      this.rowIndexFlagsIndex = new short[var2];

      for(var5 = 0; var5 < this.rowIndexFlagsIndex.length; ++var5) {
         this.rowIndexFlagsIndex[var5] = var1.readShort();
      }

      var2 = var1.readInt();
      this.rowIndexFlags = new int[var2];

      for(var5 = 0; var5 < this.rowIndexFlags.length; ++var5) {
         this.rowIndexFlags[var5] = var1.readInt();
      }

      var2 = var1.readInt();
      this.rowIndexShifts = new byte[var2];

      for(var5 = 0; var5 < this.rowIndexShifts.length; ++var5) {
         this.rowIndexShifts[var5] = var1.readByte();
      }

      var2 = var1.readInt();
      this.table = new short[var2];

      for(var5 = 0; var5 < this.table.length; ++var5) {
         this.table[var5] = var1.readShort();
      }

      this.reverseColumnMap = new char[this.numCols];

      for(char var8 = 0; var8 < '\uffff'; ++var8) {
         byte var6 = this.columnMap.elementAt(var8);
         if (var6 != 0) {
            this.reverseColumnMap[var6] = var8;
         }
      }

      var1.close();
   }

   final short at(int var1, char var2) {
      byte var3 = this.columnMap.elementAt(var2);
      return this.at(var1, (int)var3);
   }

   final short at(int var1, int var2) {
      return var2 < 0 ? this.internalAt(this.rowIndex[var1], var2 + this.rowIndexShifts[var1]) : 0;
   }

   private final short internalAt(int var1, int var2) {
      return this.table[var1 * this.numCols + var2];
   }
}
