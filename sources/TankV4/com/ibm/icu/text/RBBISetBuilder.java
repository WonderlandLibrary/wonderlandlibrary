package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.IntTrieBuilder;
import com.ibm.icu.impl.TrieBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class RBBISetBuilder {
   RBBIRuleBuilder fRB;
   RBBISetBuilder.RangeDescriptor fRangeList;
   IntTrieBuilder fTrie;
   int fTrieSize;
   int fGroupCount;
   boolean fSawBOF;
   RBBISetBuilder.RBBIDataManipulate dm = new RBBISetBuilder.RBBIDataManipulate(this);

   RBBISetBuilder(RBBIRuleBuilder var1) {
      this.fRB = var1;
   }

   void build() {
      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("usets") >= 0) {
         this.printSets();
      }

      this.fRangeList = new RBBISetBuilder.RangeDescriptor();
      this.fRangeList.fStartChar = 0;
      this.fRangeList.fEndChar = 1114111;
      Iterator var2 = this.fRB.fUSetNodes.iterator();

      RBBISetBuilder.RangeDescriptor var1;
      while(var2.hasNext()) {
         RBBINode var3 = (RBBINode)var2.next();
         UnicodeSet var4 = var3.fInputSet;
         int var5 = var4.getRangeCount();
         int var6 = 0;
         var1 = this.fRangeList;

         while(var6 < var5) {
            int var7 = var4.getRangeStart(var6);

            int var8;
            for(var8 = var4.getRangeEnd(var6); var1.fEndChar < var7; var1 = var1.fNext) {
            }

            if (var1.fStartChar < var7) {
               var1.split(var7);
            } else {
               if (var1.fEndChar > var8) {
                  var1.split(var8 + 1);
               }

               if (var1.fIncludesSets.indexOf(var3) == -1) {
                  var1.fIncludesSets.add(var3);
               }

               if (var8 == var1.fEndChar) {
                  ++var6;
               }

               var1 = var1.fNext;
            }
         }
      }

      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("range") >= 0) {
         this.printRanges();
      }

      for(var1 = this.fRangeList; var1 != null; var1 = var1.fNext) {
         for(RBBISetBuilder.RangeDescriptor var9 = this.fRangeList; var9 != var1; var9 = var9.fNext) {
            if (var1.fIncludesSets.equals(var9.fIncludesSets)) {
               var1.fNum = var9.fNum;
               break;
            }
         }

         if (var1.fNum == 0) {
            ++this.fGroupCount;
            var1.fNum = this.fGroupCount + 2;
            var1.setDictionaryFlag();
            this.addValToSets(var1.fIncludesSets, this.fGroupCount + 2);
         }
      }

      String var10 = "eof";
      String var11 = "bof";
      Iterator var12 = this.fRB.fUSetNodes.iterator();

      while(var12.hasNext()) {
         RBBINode var13 = (RBBINode)var12.next();
         UnicodeSet var14 = var13.fInputSet;
         if (var14.contains(var10)) {
            this.addValToSet(var13, 1);
         }

         if (var14.contains(var11)) {
            this.addValToSet(var13, 2);
            this.fSawBOF = true;
         }
      }

      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("rgroup") >= 0) {
         this.printRangeGroups();
      }

      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("esets") >= 0) {
         this.printSets();
      }

      this.fTrie = new IntTrieBuilder((int[])null, 100000, 0, 0, true);

      for(var1 = this.fRangeList; var1 != null; var1 = var1.fNext) {
         this.fTrie.setRange(var1.fStartChar, var1.fEndChar + 1, var1.fNum, true);
      }

   }

   int getTrieSize() {
      int var1 = 0;

      try {
         var1 = this.fTrie.serialize((OutputStream)null, true, this.dm);
      } catch (IOException var3) {
         Assert.assrt(false);
      }

      return var1;
   }

   void serializeTrie(OutputStream var1) throws IOException {
      this.fTrie.serialize(var1, true, this.dm);
   }

   void addValToSets(List var1, int var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         RBBINode var4 = (RBBINode)var3.next();
         this.addValToSet(var4, var2);
      }

   }

   void addValToSet(RBBINode var1, int var2) {
      RBBINode var3 = new RBBINode(3);
      var3.fVal = var2;
      if (var1.fLeftChild == null) {
         var1.fLeftChild = var3;
         var3.fParent = var1;
      } else {
         RBBINode var4 = new RBBINode(9);
         var4.fLeftChild = var1.fLeftChild;
         var4.fRightChild = var3;
         var4.fLeftChild.fParent = var4;
         var4.fRightChild.fParent = var4;
         var1.fLeftChild = var4;
         var4.fParent = var1;
      }

   }

   int getNumCharCategories() {
      return this.fGroupCount + 3;
   }

   boolean sawBOF() {
      return this.fSawBOF;
   }

   int getFirstChar(int var1) {
      int var3 = -1;

      for(RBBISetBuilder.RangeDescriptor var2 = this.fRangeList; var2 != null; var2 = var2.fNext) {
         if (var2.fNum == var1) {
            var3 = var2.fStartChar;
            break;
         }
      }

      return var3;
   }

   void printRanges() {
      System.out.print("\n\n Nonoverlapping Ranges ...\n");

      for(RBBISetBuilder.RangeDescriptor var1 = this.fRangeList; var1 != null; var1 = var1.fNext) {
         System.out.print(" " + var1.fNum + "   " + var1.fStartChar + "-" + var1.fEndChar);

         for(int var2 = 0; var2 < var1.fIncludesSets.size(); ++var2) {
            RBBINode var3 = (RBBINode)var1.fIncludesSets.get(var2);
            String var4 = "anon";
            RBBINode var5 = var3.fParent;
            if (var5 != null) {
               RBBINode var6 = var5.fParent;
               if (var6 != null && var6.fType == 2) {
                  var4 = var6.fText;
               }
            }

            System.out.print(var4);
            System.out.print("  ");
         }

         System.out.println("");
      }

   }

   void printRangeGroups() {
      int var4 = 0;
      System.out.print("\nRanges grouped by Unicode Set Membership...\n");

      for(RBBISetBuilder.RangeDescriptor var1 = this.fRangeList; var1 != null; var1 = var1.fNext) {
         int var5 = var1.fNum & '뿿';
         if (var5 > var4) {
            var4 = var5;
            if (var5 < 10) {
               System.out.print(" ");
            }

            System.out.print(var5 + " ");
            if ((var1.fNum & 16384) != 0) {
               System.out.print(" <DICT> ");
            }

            int var3;
            for(var3 = 0; var3 < var1.fIncludesSets.size(); ++var3) {
               RBBINode var6 = (RBBINode)var1.fIncludesSets.get(var3);
               String var7 = "anon";
               RBBINode var8 = var6.fParent;
               if (var8 != null) {
                  RBBINode var9 = var8.fParent;
                  if (var9 != null && var9.fType == 2) {
                     var7 = var9.fText;
                  }
               }

               System.out.print(var7);
               System.out.print(" ");
            }

            var3 = 0;

            for(RBBISetBuilder.RangeDescriptor var2 = var1; var2 != null; var2 = var2.fNext) {
               if (var2.fNum == var1.fNum) {
                  if (var3++ % 5 == 0) {
                     System.out.print("\n    ");
                  }

                  RBBINode.printHex(var2.fStartChar, -1);
                  System.out.print("-");
                  RBBINode.printHex(var2.fEndChar, 0);
               }
            }

            System.out.print("\n");
         }
      }

      System.out.print("\n");
   }

   void printSets() {
      System.out.print("\n\nUnicode Sets List\n------------------\n");

      for(int var1 = 0; var1 < this.fRB.fUSetNodes.size(); ++var1) {
         RBBINode var2 = (RBBINode)this.fRB.fUSetNodes.get(var1);
         RBBINode.printInt(2, var1);
         String var5 = "anonymous";
         RBBINode var3 = var2.fParent;
         if (var3 != null) {
            RBBINode var4 = var3.fParent;
            if (var4 != null && var4.fType == 2) {
               var5 = var4.fText;
            }
         }

         System.out.print("  " + var5);
         System.out.print("   ");
         System.out.print(var2.fText);
         System.out.print("\n");
         if (var2.fLeftChild != null) {
            var2.fLeftChild.printTree(true);
         }
      }

      System.out.print("\n");
   }

   class RBBIDataManipulate implements TrieBuilder.DataManipulate {
      final RBBISetBuilder this$0;

      RBBIDataManipulate(RBBISetBuilder var1) {
         this.this$0 = var1;
      }

      public int getFoldedValue(int var1, int var2) {
         boolean[] var5 = new boolean[1];
         int var4 = var1 + 1024;

         while(var1 < var4) {
            int var3 = this.this$0.fTrie.getValue(var1, var5);
            if (var5[0]) {
               var1 += 32;
            } else {
               if (var3 != 0) {
                  return var2 | '耀';
               }

               ++var1;
            }
         }

         return 0;
      }
   }

   static class RangeDescriptor {
      int fStartChar;
      int fEndChar;
      int fNum;
      List fIncludesSets;
      RBBISetBuilder.RangeDescriptor fNext;

      RangeDescriptor() {
         this.fIncludesSets = new ArrayList();
      }

      RangeDescriptor(RBBISetBuilder.RangeDescriptor var1) {
         this.fStartChar = var1.fStartChar;
         this.fEndChar = var1.fEndChar;
         this.fNum = var1.fNum;
         this.fIncludesSets = new ArrayList(var1.fIncludesSets);
      }

      void split(int var1) {
         Assert.assrt(var1 > this.fStartChar && var1 <= this.fEndChar);
         RBBISetBuilder.RangeDescriptor var2 = new RBBISetBuilder.RangeDescriptor(this);
         var2.fStartChar = var1;
         this.fEndChar = var1 - 1;
         var2.fNext = this.fNext;
         this.fNext = var2;
      }

      void setDictionaryFlag() {
         for(int var1 = 0; var1 < this.fIncludesSets.size(); ++var1) {
            RBBINode var2 = (RBBINode)this.fIncludesSets.get(var1);
            String var3 = "";
            RBBINode var4 = var2.fParent;
            if (var4 != null) {
               RBBINode var5 = var4.fParent;
               if (var5 != null && var5.fType == 2) {
                  var3 = var5.fText;
               }
            }

            if (var3.equals("dictionary")) {
               this.fNum |= 16384;
               break;
            }
         }

      }
   }
}
