package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.lang.UCharacter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

class RBBITableBuilder {
   private RBBIRuleBuilder fRB;
   private int fRootIx;
   private List fDStates;

   RBBITableBuilder(RBBIRuleBuilder var1, int var2) {
      this.fRootIx = var2;
      this.fRB = var1;
      this.fDStates = new ArrayList();
   }

   void build() {
      if (this.fRB.fTreeRoots[this.fRootIx] != null) {
         this.fRB.fTreeRoots[this.fRootIx] = this.fRB.fTreeRoots[this.fRootIx].flattenVariables();
         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ftree") >= 0) {
            System.out.println("Parse tree after flattening variable references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
         }

         RBBINode var1;
         if (this.fRB.fSetBuilder.sawBOF()) {
            var1 = new RBBINode(8);
            RBBINode var2 = new RBBINode(3);
            var1.fLeftChild = var2;
            var1.fRightChild = this.fRB.fTreeRoots[this.fRootIx];
            var2.fParent = var1;
            var2.fVal = 2;
            this.fRB.fTreeRoots[this.fRootIx] = var1;
         }

         var1 = new RBBINode(8);
         var1.fLeftChild = this.fRB.fTreeRoots[this.fRootIx];
         this.fRB.fTreeRoots[this.fRootIx].fParent = var1;
         var1.fRightChild = new RBBINode(6);
         var1.fRightChild.fParent = var1;
         this.fRB.fTreeRoots[this.fRootIx] = var1;
         this.fRB.fTreeRoots[this.fRootIx].flattenSets();
         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("stree") >= 0) {
            System.out.println("Parse tree after flattening Unicode Set references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
         }

         this.calcNullable(this.fRB.fTreeRoots[this.fRootIx]);
         this.calcFirstPos(this.fRB.fTreeRoots[this.fRootIx]);
         this.calcLastPos(this.fRB.fTreeRoots[this.fRootIx]);
         this.calcFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("pos") >= 0) {
            System.out.print("\n");
            this.printPosSets(this.fRB.fTreeRoots[this.fRootIx]);
         }

         if (this.fRB.fChainRules) {
            this.calcChainedFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
         }

         if (this.fRB.fSetBuilder.sawBOF()) {
            this.bofFixup();
         }

         this.buildStateTable();
         this.flagAcceptingStates();
         this.flagLookAheadStates();
         this.flagTaggedStates();
         this.mergeRuleStatusVals();
         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("states") >= 0) {
            this.printStates();
         }

      }
   }

   void calcNullable(RBBINode var1) {
      if (var1 != null) {
         if (var1.fType != 0 && var1.fType != 6) {
            if (var1.fType != 4 && var1.fType != 5) {
               this.calcNullable(var1.fLeftChild);
               this.calcNullable(var1.fRightChild);
               if (var1.fType == 9) {
                  var1.fNullable = var1.fLeftChild.fNullable || var1.fRightChild.fNullable;
               } else if (var1.fType == 8) {
                  var1.fNullable = var1.fLeftChild.fNullable && var1.fRightChild.fNullable;
               } else if (var1.fType != 10 && var1.fType != 12) {
                  var1.fNullable = false;
               } else {
                  var1.fNullable = true;
               }

            } else {
               var1.fNullable = true;
            }
         } else {
            var1.fNullable = false;
         }
      }
   }

   void calcFirstPos(RBBINode var1) {
      if (var1 != null) {
         if (var1.fType != 3 && var1.fType != 6 && var1.fType != 4 && var1.fType != 5) {
            this.calcFirstPos(var1.fLeftChild);
            this.calcFirstPos(var1.fRightChild);
            if (var1.fType == 9) {
               var1.fFirstPosSet.addAll(var1.fLeftChild.fFirstPosSet);
               var1.fFirstPosSet.addAll(var1.fRightChild.fFirstPosSet);
            } else if (var1.fType == 8) {
               var1.fFirstPosSet.addAll(var1.fLeftChild.fFirstPosSet);
               if (var1.fLeftChild.fNullable) {
                  var1.fFirstPosSet.addAll(var1.fRightChild.fFirstPosSet);
               }
            } else if (var1.fType == 10 || var1.fType == 12 || var1.fType == 11) {
               var1.fFirstPosSet.addAll(var1.fLeftChild.fFirstPosSet);
            }

         } else {
            var1.fFirstPosSet.add(var1);
         }
      }
   }

   void calcLastPos(RBBINode var1) {
      if (var1 != null) {
         if (var1.fType != 3 && var1.fType != 6 && var1.fType != 4 && var1.fType != 5) {
            this.calcLastPos(var1.fLeftChild);
            this.calcLastPos(var1.fRightChild);
            if (var1.fType == 9) {
               var1.fLastPosSet.addAll(var1.fLeftChild.fLastPosSet);
               var1.fLastPosSet.addAll(var1.fRightChild.fLastPosSet);
            } else if (var1.fType == 8) {
               var1.fLastPosSet.addAll(var1.fRightChild.fLastPosSet);
               if (var1.fRightChild.fNullable) {
                  var1.fLastPosSet.addAll(var1.fLeftChild.fLastPosSet);
               }
            } else if (var1.fType == 10 || var1.fType == 12 || var1.fType == 11) {
               var1.fLastPosSet.addAll(var1.fLeftChild.fLastPosSet);
            }

         } else {
            var1.fLastPosSet.add(var1);
         }
      }
   }

   void calcFollowPos(RBBINode var1) {
      if (var1 != null && var1.fType != 3 && var1.fType != 6) {
         this.calcFollowPos(var1.fLeftChild);
         this.calcFollowPos(var1.fRightChild);
         Iterator var2;
         RBBINode var3;
         if (var1.fType == 8) {
            var2 = var1.fLeftChild.fLastPosSet.iterator();

            while(var2.hasNext()) {
               var3 = (RBBINode)var2.next();
               var3.fFollowPos.addAll(var1.fRightChild.fFirstPosSet);
            }
         }

         if (var1.fType == 10 || var1.fType == 11) {
            var2 = var1.fLastPosSet.iterator();

            while(var2.hasNext()) {
               var3 = (RBBINode)var2.next();
               var3.fFollowPos.addAll(var1.fFirstPosSet);
            }
         }

      }
   }

   void calcChainedFollowPos(RBBINode var1) {
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      var1.findNodes(var2, 6);
      var1.findNodes(var3, 3);
      RBBINode var4 = var1;
      if (this.fRB.fSetBuilder.sawBOF()) {
         var4 = var1.fLeftChild.fRightChild;
      }

      Assert.assrt(var4 != null);
      Set var5 = var4.fFirstPosSet;
      Iterator var6 = var3.iterator();

      while(true) {
         RBBINode var8;
         Iterator var9;
         RBBINode var10;
         int var12;
         do {
            do {
               if (!var6.hasNext()) {
                  return;
               }

               RBBINode var7 = (RBBINode)var6.next();
               var8 = null;
               var9 = var2.iterator();

               while(var9.hasNext()) {
                  var10 = (RBBINode)var9.next();
                  if (var7.fFollowPos.contains(var10)) {
                     var8 = var7;
                     break;
                  }
               }
            } while(var8 == null);

            if (!this.fRB.fLBCMNoChain) {
               break;
            }

            int var11 = this.fRB.fSetBuilder.getFirstChar(var8.fVal);
            if (var11 == -1) {
               break;
            }

            var12 = UCharacter.getIntPropertyValue(var11, 4104);
         } while(var12 == 9);

         var9 = var5.iterator();

         while(var9.hasNext()) {
            var10 = (RBBINode)var9.next();
            if (var10.fType == 3 && var8.fVal == var10.fVal) {
               var8.fFollowPos.addAll(var10.fFollowPos);
            }
         }
      }
   }

   void bofFixup() {
      RBBINode var1 = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fLeftChild;
      Assert.assrt(var1.fType == 3);
      Assert.assrt(var1.fVal == 2);
      Set var2 = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fRightChild.fFirstPosSet;
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         RBBINode var4 = (RBBINode)var3.next();
         if (var4.fType == 3 && var4.fVal == var1.fVal) {
            var1.fFollowPos.addAll(var4.fFollowPos);
         }
      }

   }

   void buildStateTable() {
      int var1 = this.fRB.fSetBuilder.getNumCharCategories() - 1;
      RBBITableBuilder.RBBIStateDescriptor var2 = new RBBITableBuilder.RBBIStateDescriptor(var1);
      this.fDStates.add(var2);
      RBBITableBuilder.RBBIStateDescriptor var3 = new RBBITableBuilder.RBBIStateDescriptor(var1);
      var3.fPositions.addAll(this.fRB.fTreeRoots[this.fRootIx].fFirstPosSet);
      this.fDStates.add(var3);

      while(true) {
         RBBITableBuilder.RBBIStateDescriptor var4 = null;

         for(int var5 = 1; var5 < this.fDStates.size(); ++var5) {
            RBBITableBuilder.RBBIStateDescriptor var6 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var5);
            if (!var6.fMarked) {
               var4 = var6;
               break;
            }
         }

         if (var4 == null) {
            return;
         }

         var4.fMarked = true;

         for(int var12 = 1; var12 <= var1; ++var12) {
            Object var7 = null;
            Iterator var8 = var4.fPositions.iterator();

            while(var8.hasNext()) {
               RBBINode var9 = (RBBINode)var8.next();
               if (var9.fType == 3 && var9.fVal == var12) {
                  if (var7 == null) {
                     var7 = new HashSet();
                  }

                  ((Set)var7).addAll(var9.fFollowPos);
               }
            }

            int var13 = 0;
            boolean var14 = false;
            if (var7 != null) {
               Assert.assrt(((Set)var7).size() > 0);

               RBBITableBuilder.RBBIStateDescriptor var11;
               for(int var10 = 0; var10 < this.fDStates.size(); ++var10) {
                  var11 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var10);
                  if (var7.equals(var11.fPositions)) {
                     var7 = var11.fPositions;
                     var13 = var10;
                     var14 = true;
                     break;
                  }
               }

               if (!var14) {
                  var11 = new RBBITableBuilder.RBBIStateDescriptor(var1);
                  var11.fPositions = (Set)var7;
                  this.fDStates.add(var11);
                  var13 = this.fDStates.size() - 1;
               }

               var4.fDtran[var12] = var13;
            }
         }
      }
   }

   void flagAcceptingStates() {
      ArrayList var1 = new ArrayList();
      this.fRB.fTreeRoots[this.fRootIx].findNodes(var1, 6);

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         RBBINode var2 = (RBBINode)var1.get(var3);

         for(int var4 = 0; var4 < this.fDStates.size(); ++var4) {
            RBBITableBuilder.RBBIStateDescriptor var5 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var4);
            if (var5.fPositions.contains(var2)) {
               if (var5.fAccepting == 0) {
                  var5.fAccepting = var2.fVal;
                  if (var5.fAccepting == 0) {
                     var5.fAccepting = -1;
                  }
               }

               if (var5.fAccepting == -1 && var2.fVal != 0) {
                  var5.fAccepting = var2.fVal;
               }

               if (var2.fLookAheadEnd) {
                  var5.fLookAhead = var5.fAccepting;
               }
            }
         }
      }

   }

   void flagLookAheadStates() {
      ArrayList var1 = new ArrayList();
      this.fRB.fTreeRoots[this.fRootIx].findNodes(var1, 4);

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         RBBINode var2 = (RBBINode)var1.get(var3);

         for(int var4 = 0; var4 < this.fDStates.size(); ++var4) {
            RBBITableBuilder.RBBIStateDescriptor var5 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var4);
            if (var5.fPositions.contains(var2)) {
               var5.fLookAhead = var2.fVal;
            }
         }
      }

   }

   void flagTaggedStates() {
      ArrayList var1 = new ArrayList();
      this.fRB.fTreeRoots[this.fRootIx].findNodes(var1, 5);

      for(int var3 = 0; var3 < var1.size(); ++var3) {
         RBBINode var2 = (RBBINode)var1.get(var3);

         for(int var4 = 0; var4 < this.fDStates.size(); ++var4) {
            RBBITableBuilder.RBBIStateDescriptor var5 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var4);
            if (var5.fPositions.contains(var2)) {
               var5.fTagVals.add(var2.fVal);
            }
         }
      }

   }

   void mergeRuleStatusVals() {
      if (this.fRB.fRuleStatusVals.size() == 0) {
         this.fRB.fRuleStatusVals.add(1);
         this.fRB.fRuleStatusVals.add(0);
         TreeSet var2 = new TreeSet();
         Integer var3 = 0;
         this.fRB.fStatusSets.put(var2, var3);
         TreeSet var4 = new TreeSet();
         var4.add(var3);
         this.fRB.fStatusSets.put(var2, var3);
      }

      for(int var1 = 0; var1 < this.fDStates.size(); ++var1) {
         RBBITableBuilder.RBBIStateDescriptor var5 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var1);
         SortedSet var6 = var5.fTagVals;
         Integer var7 = (Integer)this.fRB.fStatusSets.get(var6);
         if (var7 == null) {
            var7 = this.fRB.fRuleStatusVals.size();
            this.fRB.fStatusSets.put(var6, var7);
            this.fRB.fRuleStatusVals.add(var6.size());
            this.fRB.fRuleStatusVals.addAll(var6);
         }

         var5.fTagsIdx = var7;
      }

   }

   void printPosSets(RBBINode var1) {
      if (var1 != null) {
         RBBINode.printNode(var1);
         System.out.print("         Nullable:  " + var1.fNullable);
         System.out.print("         firstpos:  ");
         this.printSet(var1.fFirstPosSet);
         System.out.print("         lastpos:   ");
         this.printSet(var1.fLastPosSet);
         System.out.print("         followpos: ");
         this.printSet(var1.fFollowPos);
         this.printPosSets(var1.fLeftChild);
         this.printPosSets(var1.fRightChild);
      }
   }

   int getTableSize() {
      boolean var1 = false;
      if (this.fRB.fTreeRoots[this.fRootIx] == null) {
         return 0;
      } else {
         byte var5 = 16;
         int var2 = this.fDStates.size();
         int var3 = this.fRB.fSetBuilder.getNumCharCategories();
         int var4 = 8 + 2 * var3;

         int var6;
         for(var6 = var5 + var2 * var4; var6 % 8 > 0; ++var6) {
         }

         return var6;
      }
   }

   short[] exportTable() {
      if (this.fRB.fTreeRoots[this.fRootIx] == null) {
         return new short[0];
      } else {
         Assert.assrt(this.fRB.fSetBuilder.getNumCharCategories() < 32767 && this.fDStates.size() < 32767);
         int var3 = this.fDStates.size();
         int var4 = 4 + this.fRB.fSetBuilder.getNumCharCategories();
         int var5 = this.getTableSize() / 2;
         short[] var6 = new short[var5];
         var6[0] = (short)(var3 >>> 16);
         var6[1] = (short)(var3 & '\uffff');
         var6[2] = (short)(var4 >>> 16);
         var6[3] = (short)(var4 & '\uffff');
         int var7 = 0;
         if (this.fRB.fLookAheadHardBreak) {
            var7 |= 1;
         }

         if (this.fRB.fSetBuilder.sawBOF()) {
            var7 |= 2;
         }

         var6[4] = (short)(var7 >>> 16);
         var6[5] = (short)(var7 & '\uffff');
         int var8 = this.fRB.fSetBuilder.getNumCharCategories();

         for(int var1 = 0; var1 < var3; ++var1) {
            RBBITableBuilder.RBBIStateDescriptor var9 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var1);
            int var10 = 8 + var1 * var4;
            Assert.assrt(-32768 < var9.fAccepting && var9.fAccepting <= 32767);
            Assert.assrt(-32768 < var9.fLookAhead && var9.fLookAhead <= 32767);
            var6[var10 + 0] = (short)var9.fAccepting;
            var6[var10 + 1] = (short)var9.fLookAhead;
            var6[var10 + 2] = (short)var9.fTagsIdx;

            for(int var2 = 0; var2 < var8; ++var2) {
               var6[var10 + 4 + var2] = (short)var9.fDtran[var2];
            }
         }

         return var6;
      }
   }

   void printSet(Collection var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         RBBINode var3 = (RBBINode)var2.next();
         RBBINode.printInt(var3.fSerialNum, 8);
      }

      System.out.println();
   }

   void printStates() {
      System.out.print("state |           i n p u t     s y m b o l s \n");
      System.out.print("      | Acc  LA    Tag");

      int var1;
      for(var1 = 0; var1 < this.fRB.fSetBuilder.getNumCharCategories(); ++var1) {
         RBBINode.printInt(var1, 3);
      }

      System.out.print("\n");
      System.out.print("      |---------------");

      for(var1 = 0; var1 < this.fRB.fSetBuilder.getNumCharCategories(); ++var1) {
         System.out.print("---");
      }

      System.out.print("\n");

      for(int var2 = 0; var2 < this.fDStates.size(); ++var2) {
         RBBITableBuilder.RBBIStateDescriptor var3 = (RBBITableBuilder.RBBIStateDescriptor)this.fDStates.get(var2);
         RBBINode.printInt(var2, 5);
         System.out.print(" | ");
         RBBINode.printInt(var3.fAccepting, 3);
         RBBINode.printInt(var3.fLookAhead, 4);
         RBBINode.printInt(var3.fTagsIdx, 6);
         System.out.print(" ");

         for(var1 = 0; var1 < this.fRB.fSetBuilder.getNumCharCategories(); ++var1) {
            RBBINode.printInt(var3.fDtran[var1], 3);
         }

         System.out.print("\n");
      }

      System.out.print("\n\n");
   }

   void printRuleStatusTable() {
      boolean var1 = false;
      int var2 = 0;
      List var4 = this.fRB.fRuleStatusVals;
      System.out.print("index |  tags \n");
      System.out.print("-------------------\n");

      while(var2 < var4.size()) {
         int var6 = var2;
         var2 = var2 + (Integer)var4.get(var2) + 1;
         RBBINode.printInt(var6, 7);

         for(int var3 = var6 + 1; var3 < var2; ++var3) {
            int var5 = (Integer)var4.get(var3);
            RBBINode.printInt(var5, 7);
         }

         System.out.print("\n");
      }

      System.out.print("\n\n");
   }

   static class RBBIStateDescriptor {
      boolean fMarked;
      int fAccepting;
      int fLookAhead;
      SortedSet fTagVals = new TreeSet();
      int fTagsIdx;
      Set fPositions = new HashSet();
      int[] fDtran;

      RBBIStateDescriptor(int var1) {
         this.fDtran = new int[var1 + 1];
      }
   }
}
