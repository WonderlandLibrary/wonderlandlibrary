package com.ibm.icu.text;

import com.ibm.icu.impl.ICUBinary;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;

class BreakCTDictionary {
   private BreakCTDictionary.CompactTrieHeader fData;
   private BreakCTDictionary.CompactTrieNodes[] nodes;
   private static final byte[] DATA_FORMAT_ID = new byte[]{84, 114, 68, 99};

   private BreakCTDictionary.CompactTrieNodes getCompactTrieNode(int var1) {
      return this.nodes[var1];
   }

   public BreakCTDictionary(InputStream var1) throws IOException {
      ICUBinary.readHeader(var1, DATA_FORMAT_ID, (ICUBinary.Authenticate)null);
      DataInputStream var2 = new DataInputStream(var1);
      this.fData = new BreakCTDictionary.CompactTrieHeader();
      this.fData.size = var2.readInt();
      this.fData.magic = var2.readInt();
      this.fData.nodeCount = var2.readShort();
      this.fData.root = var2.readShort();
      this.loadBreakCTDictionary(var2);
   }

   private void loadBreakCTDictionary(DataInputStream var1) throws IOException {
      int var2;
      for(var2 = 0; var2 < this.fData.nodeCount; ++var2) {
         var1.readInt();
      }

      this.nodes = new BreakCTDictionary.CompactTrieNodes[this.fData.nodeCount];
      this.nodes[0] = new BreakCTDictionary.CompactTrieNodes();

      for(var2 = 1; var2 < this.fData.nodeCount; ++var2) {
         this.nodes[var2] = new BreakCTDictionary.CompactTrieNodes();
         this.nodes[var2].flagscount = var1.readShort();
         int var3 = this.nodes[var2].flagscount & 4095;
         if (var3 != 0) {
            boolean var4 = (this.nodes[var2].flagscount & 4096) != 0;
            int var5;
            if (var4) {
               this.nodes[var2].vnode = new BreakCTDictionary.CompactTrieVerticalNode();
               this.nodes[var2].vnode.equal = var1.readShort();
               this.nodes[var2].vnode.chars = new char[var3];

               for(var5 = 0; var5 < var3; ++var5) {
                  this.nodes[var2].vnode.chars[var5] = var1.readChar();
               }
            } else {
               this.nodes[var2].hnode = new BreakCTDictionary.CompactTrieHorizontalNode[var3];

               for(var5 = 0; var5 < var3; ++var5) {
                  this.nodes[var2].hnode[var5] = new BreakCTDictionary.CompactTrieHorizontalNode(var1.readChar(), var1.readShort());
               }
            }
         }
      }

   }

   public int matches(CharacterIterator var1, int var2, int[] var3, int[] var4, int var5) {
      BreakCTDictionary.CompactTrieNodes var6 = this.getCompactTrieNode(this.fData.root);
      int var7 = 0;
      char var8 = var1.current();
      int var9 = 0;
      boolean var10 = false;

      while(var6 != null) {
         if (var5 > 0 && (var6.flagscount & 8192) != 0) {
            var3[var7++] = var9;
            --var5;
         }

         if (var9 >= var2) {
            break;
         }

         int var11 = var6.flagscount & 4095;
         if (var11 == 0) {
            break;
         }

         int var13;
         if ((var6.flagscount & 4096) == 0) {
            BreakCTDictionary.CompactTrieHorizontalNode[] var16 = var6.hnode;
            var13 = 0;
            int var14 = var11 - 1;
            var6 = null;

            while(var14 >= var13) {
               int var15 = var14 + var13 >>> 1;
               if (var8 == var16[var15].ch) {
                  var6 = this.getCompactTrieNode(var16[var15].equal);
                  var1.next();
                  var8 = var1.current();
                  ++var9;
                  break;
               }

               if (var8 < var16[var15].ch) {
                  var14 = var15 - 1;
               } else {
                  var13 = var15 + 1;
               }
            }
         } else {
            BreakCTDictionary.CompactTrieVerticalNode var12 = var6.vnode;

            for(var13 = 0; var13 < var11 && var9 < var2; ++var13) {
               if (var8 != var12.chars[var13]) {
                  var10 = true;
                  break;
               }

               var1.next();
               var8 = var1.current();
               ++var9;
            }

            if (var10) {
               break;
            }

            var6 = this.getCompactTrieNode(var12.equal);
         }
      }

      var4[0] = var7;
      return var9;
   }

   static class CompactTrieNodes {
      short flagscount = 0;
      BreakCTDictionary.CompactTrieHorizontalNode[] hnode = null;
      BreakCTDictionary.CompactTrieVerticalNode vnode = null;
   }

   static class CompactTrieVerticalNode {
      int equal = 0;
      char[] chars = null;
   }

   static class CompactTrieHorizontalNode {
      char ch;
      int equal;

      CompactTrieHorizontalNode(char var1, int var2) {
         this.ch = var1;
         this.equal = var2;
      }
   }

   static final class CompactTrieNodeFlags {
      static final int kVerticalNode = 4096;
      static final int kParentEndsWord = 8192;
      static final int kReservedFlag1 = 16384;
      static final int kReservedFlag2 = 32768;
      static final int kCountMask = 4095;
      static final int kFlagMask = 61440;
   }

   static class CompactTrieHeader {
      int size = 0;
      int magic = 0;
      int nodeCount = 0;
      int root = 0;
      int[] offset = null;
   }
}
