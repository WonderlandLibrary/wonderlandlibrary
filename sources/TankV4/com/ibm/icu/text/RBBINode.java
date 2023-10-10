package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class RBBINode {
   static final int setRef = 0;
   static final int uset = 1;
   static final int varRef = 2;
   static final int leafChar = 3;
   static final int lookAhead = 4;
   static final int tag = 5;
   static final int endMark = 6;
   static final int opStart = 7;
   static final int opCat = 8;
   static final int opOr = 9;
   static final int opStar = 10;
   static final int opPlus = 11;
   static final int opQuestion = 12;
   static final int opBreak = 13;
   static final int opReverse = 14;
   static final int opLParen = 15;
   static final int nodeTypeLimit = 16;
   static final String[] nodeTypeNames = new String[]{"setRef", "uset", "varRef", "leafChar", "lookAhead", "tag", "endMark", "opStart", "opCat", "opOr", "opStar", "opPlus", "opQuestion", "opBreak", "opReverse", "opLParen"};
   static final int precZero = 0;
   static final int precStart = 1;
   static final int precLParen = 2;
   static final int precOpOr = 3;
   static final int precOpCat = 4;
   int fType;
   RBBINode fParent;
   RBBINode fLeftChild;
   RBBINode fRightChild;
   UnicodeSet fInputSet;
   int fPrecedence = 0;
   String fText;
   int fFirstPos;
   int fLastPos;
   boolean fNullable;
   int fVal;
   boolean fLookAheadEnd;
   Set fFirstPosSet;
   Set fLastPosSet;
   Set fFollowPos;
   int fSerialNum;
   static int gLastSerial;

   RBBINode(int var1) {
      Assert.assrt(var1 < 16);
      this.fSerialNum = ++gLastSerial;
      this.fType = var1;
      this.fFirstPosSet = new HashSet();
      this.fLastPosSet = new HashSet();
      this.fFollowPos = new HashSet();
      if (var1 == 8) {
         this.fPrecedence = 4;
      } else if (var1 == 9) {
         this.fPrecedence = 3;
      } else if (var1 == 7) {
         this.fPrecedence = 1;
      } else if (var1 == 15) {
         this.fPrecedence = 2;
      } else {
         this.fPrecedence = 0;
      }

   }

   RBBINode(RBBINode var1) {
      this.fSerialNum = ++gLastSerial;
      this.fType = var1.fType;
      this.fInputSet = var1.fInputSet;
      this.fPrecedence = var1.fPrecedence;
      this.fText = var1.fText;
      this.fFirstPos = var1.fFirstPos;
      this.fLastPos = var1.fLastPos;
      this.fNullable = var1.fNullable;
      this.fVal = var1.fVal;
      this.fFirstPosSet = new HashSet(var1.fFirstPosSet);
      this.fLastPosSet = new HashSet(var1.fLastPosSet);
      this.fFollowPos = new HashSet(var1.fFollowPos);
   }

   RBBINode cloneTree() {
      RBBINode var1;
      if (this.fType == 2) {
         var1 = this.fLeftChild.cloneTree();
      } else if (this.fType == 1) {
         var1 = this;
      } else {
         var1 = new RBBINode(this);
         if (this.fLeftChild != null) {
            var1.fLeftChild = this.fLeftChild.cloneTree();
            var1.fLeftChild.fParent = var1;
         }

         if (this.fRightChild != null) {
            var1.fRightChild = this.fRightChild.cloneTree();
            var1.fRightChild.fParent = var1;
         }
      }

      return var1;
   }

   RBBINode flattenVariables() {
      if (this.fType == 2) {
         RBBINode var1 = this.fLeftChild.cloneTree();
         return var1;
      } else {
         if (this.fLeftChild != null) {
            this.fLeftChild = this.fLeftChild.flattenVariables();
            this.fLeftChild.fParent = this;
         }

         if (this.fRightChild != null) {
            this.fRightChild = this.fRightChild.flattenVariables();
            this.fRightChild.fParent = this;
         }

         return this;
      }
   }

   void flattenSets() {
      Assert.assrt(this.fType != 0);
      RBBINode var1;
      RBBINode var2;
      RBBINode var3;
      if (this.fLeftChild != null) {
         if (this.fLeftChild.fType == 0) {
            var1 = this.fLeftChild;
            var2 = var1.fLeftChild;
            var3 = var2.fLeftChild;
            this.fLeftChild = var3.cloneTree();
            this.fLeftChild.fParent = this;
         } else {
            this.fLeftChild.flattenSets();
         }
      }

      if (this.fRightChild != null) {
         if (this.fRightChild.fType == 0) {
            var1 = this.fRightChild;
            var2 = var1.fLeftChild;
            var3 = var2.fLeftChild;
            this.fRightChild = var3.cloneTree();
            this.fRightChild.fParent = this;
         } else {
            this.fRightChild.flattenSets();
         }
      }

   }

   void findNodes(List var1, int var2) {
      if (this.fType == var2) {
         var1.add(this);
      }

      if (this.fLeftChild != null) {
         this.fLeftChild.findNodes(var1, var2);
      }

      if (this.fRightChild != null) {
         this.fRightChild.findNodes(var1, var2);
      }

   }

   static void printNode(RBBINode var0) {
      if (var0 == null) {
         System.out.print(" -- null --\n");
      } else {
         printInt(var0.fSerialNum, 10);
         printString(nodeTypeNames[var0.fType], 11);
         printInt(var0.fParent == null ? 0 : var0.fParent.fSerialNum, 11);
         printInt(var0.fLeftChild == null ? 0 : var0.fLeftChild.fSerialNum, 11);
         printInt(var0.fRightChild == null ? 0 : var0.fRightChild.fSerialNum, 12);
         printInt(var0.fFirstPos, 12);
         printInt(var0.fVal, 7);
         if (var0.fType == 2) {
            System.out.print(" " + var0.fText);
         }
      }

      System.out.println("");
   }

   static void printString(String var0, int var1) {
      int var2;
      for(var2 = var1; var2 < 0; ++var2) {
         System.out.print(' ');
      }

      for(var2 = var0.length(); var2 < var1; ++var2) {
         System.out.print(' ');
      }

      System.out.print(var0);
   }

   static void printInt(int var0, int var1) {
      String var2 = Integer.toString(var0);
      printString(var2, Math.max(var1, var2.length() + 1));
   }

   static void printHex(int var0, int var1) {
      String var2 = Integer.toString(var0, 16);
      String var3 = "00000".substring(0, Math.max(0, 5 - var2.length()));
      var2 = var3 + var2;
      printString(var2, var1);
   }

   void printTree(boolean var1) {
      if (var1) {
         System.out.println("-------------------------------------------------------------------");
         System.out.println("    Serial       type     Parent  LeftChild  RightChild    position  value");
      }

      printNode(this);
      if (this.fType != 2) {
         if (this.fLeftChild != null) {
            this.fLeftChild.printTree(false);
         }

         if (this.fRightChild != null) {
            this.fRightChild.printTree(false);
         }
      }

   }
}
