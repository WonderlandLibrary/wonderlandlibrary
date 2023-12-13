package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import java.text.ParsePosition;
import java.util.HashMap;

class RBBIRuleScanner {
   private static final int kStackSize = 100;
   RBBIRuleBuilder fRB;
   int fScanIndex;
   int fNextIndex;
   boolean fQuoteMode;
   int fLineNum;
   int fCharNum;
   int fLastChar;
   RBBIRuleScanner.RBBIRuleChar fC = new RBBIRuleScanner.RBBIRuleChar();
   String fVarName;
   short[] fStack = new short[100];
   int fStackPtr;
   RBBINode[] fNodeStack = new RBBINode[100];
   int fNodeStackPtr;
   boolean fReverseRule;
   boolean fLookAheadRule;
   RBBISymbolTable fSymbolTable;
   HashMap fSetTable = new HashMap();
   UnicodeSet[] fRuleSets = new UnicodeSet[10];
   int fRuleNum;
   int fOptionStart;
   private static String gRuleSet_rule_char_pattern = "[^[\\p{Z}\\u0020-\\u007f]-[\\p{L}]-[\\p{N}]]";
   private static String gRuleSet_name_char_pattern = "[_\\p{L}\\p{N}]";
   private static String gRuleSet_digit_char_pattern = "[0-9]";
   private static String gRuleSet_name_start_char_pattern = "[_\\p{L}]";
   private static String gRuleSet_white_space_pattern = "[\\p{Pattern_White_Space}]";
   private static String kAny = "any";
   static final int chNEL = 133;
   static final int chLS = 8232;

   RBBIRuleScanner(RBBIRuleBuilder var1) {
      this.fRB = var1;
      this.fLineNum = 1;
      this.fRuleSets[3] = new UnicodeSet(gRuleSet_rule_char_pattern);
      this.fRuleSets[4] = new UnicodeSet(gRuleSet_white_space_pattern);
      this.fRuleSets[1] = new UnicodeSet(gRuleSet_name_char_pattern);
      this.fRuleSets[2] = new UnicodeSet(gRuleSet_name_start_char_pattern);
      this.fRuleSets[0] = new UnicodeSet(gRuleSet_digit_char_pattern);
      this.fSymbolTable = new RBBISymbolTable(this, var1.fRules);
   }

   void error(int var1) {
      String var2 = "Error " + var1 + " at line " + this.fLineNum + " column " + this.fCharNum;
      IllegalArgumentException var3 = new IllegalArgumentException(var2);
      throw var3;
   }

   void fixOpStack(int var1) {
      while(true) {
         RBBINode var2 = this.fNodeStack[this.fNodeStackPtr - 1];
         if (var2.fPrecedence == 0) {
            System.out.print("RBBIRuleScanner.fixOpStack, bad operator node");
            this.error(66049);
            return;
         }

         if (var2.fPrecedence < var1 || var2.fPrecedence <= 2) {
            if (var1 <= 2) {
               if (var2.fPrecedence != var1) {
                  this.error(66056);
               }

               this.fNodeStack[this.fNodeStackPtr - 1] = this.fNodeStack[this.fNodeStackPtr];
               --this.fNodeStackPtr;
            }

            return;
         }

         var2.fRightChild = this.fNodeStack[this.fNodeStackPtr];
         this.fNodeStack[this.fNodeStackPtr].fParent = var2;
         --this.fNodeStackPtr;
      }
   }

   void findSetFor(String var1, RBBINode var2, UnicodeSet var3) {
      RBBIRuleScanner.RBBISetTableEl var4 = (RBBIRuleScanner.RBBISetTableEl)this.fSetTable.get(var1);
      if (var4 != null) {
         var2.fLeftChild = var4.val;
         Assert.assrt(var2.fLeftChild.fType == 1);
      } else {
         if (var3 == null) {
            if (var1.equals(kAny)) {
               var3 = new UnicodeSet(0, 1114111);
            } else {
               int var5 = UTF16.charAt((String)var1, 0);
               var3 = new UnicodeSet(var5, var5);
            }
         }

         RBBINode var6 = new RBBINode(1);
         var6.fInputSet = var3;
         var6.fParent = var2;
         var2.fLeftChild = var6;
         var6.fText = var1;
         this.fRB.fUSetNodes.add(var6);
         var4 = new RBBIRuleScanner.RBBISetTableEl();
         var4.key = var1;
         var4.val = var6;
         this.fSetTable.put(var4.key, var4);
      }
   }

   static String stripRules(String var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = var0.length();
      int var3 = 0;

      while(var3 < var2) {
         char var4 = var0.charAt(var3++);
         if (var4 == '#') {
            while(var3 < var2 && var4 != '\r' && var4 != '\n' && var4 != 133) {
               var4 = var0.charAt(var3++);
            }
         }

         if (!UCharacter.isISOControl(var4)) {
            var1.append(var4);
         }
      }

      return var1.toString();
   }

   int nextCharLL() {
      if (this.fNextIndex >= this.fRB.fRules.length()) {
         return -1;
      } else {
         int var1 = UTF16.charAt(this.fRB.fRules, this.fNextIndex);
         this.fNextIndex = UTF16.moveCodePointOffset((String)this.fRB.fRules, this.fNextIndex, 1);
         if (var1 == 13 || var1 == 133 || var1 == 8232 || var1 == 10 && this.fLastChar != 13) {
            ++this.fLineNum;
            this.fCharNum = 0;
            if (this.fQuoteMode) {
               this.error(66057);
               this.fQuoteMode = false;
            }
         } else if (var1 != 10) {
            ++this.fCharNum;
         }

         this.fLastChar = var1;
         return var1;
      }
   }

   void nextChar(RBBIRuleScanner.RBBIRuleChar var1) {
      this.fScanIndex = this.fNextIndex;
      var1.fChar = this.nextCharLL();
      var1.fEscaped = false;
      if (var1.fChar == 39) {
         if (UTF16.charAt(this.fRB.fRules, this.fNextIndex) != 39) {
            this.fQuoteMode = !this.fQuoteMode;
            if (this.fQuoteMode) {
               var1.fChar = 40;
            } else {
               var1.fChar = 41;
            }

            var1.fEscaped = false;
            return;
         }

         var1.fChar = this.nextCharLL();
         var1.fEscaped = true;
      }

      if (this.fQuoteMode) {
         var1.fEscaped = true;
      } else {
         if (var1.fChar == 35) {
            do {
               var1.fChar = this.nextCharLL();
            } while(var1.fChar != -1 && var1.fChar != 13 && var1.fChar != 10 && var1.fChar != 133 && var1.fChar != 8232);
         }

         if (var1.fChar == -1) {
            return;
         }

         if (var1.fChar == 92) {
            var1.fEscaped = true;
            int[] var2 = new int[]{this.fNextIndex};
            var1.fChar = Utility.unescapeAt(this.fRB.fRules, var2);
            if (var2[0] == this.fNextIndex) {
               this.error(66050);
            }

            this.fCharNum += var2[0] - this.fNextIndex;
            this.fNextIndex = var2[0];
         }
      }

   }

   void parse() {
      short var1 = 1;
      this.nextChar(this.fC);

      while(var1 != 0) {
         RBBIRuleParseTable.RBBIRuleTableElement var2 = RBBIRuleParseTable.gRuleParseStateTable[var1];
         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
            System.out.println("char, line, col = ('" + (char)this.fC.fChar + "', " + this.fLineNum + ", " + this.fCharNum + "    state = " + var2.fStateName);
         }

         int var3 = var1;

         while(true) {
            var2 = RBBIRuleParseTable.gRuleParseStateTable[var3];
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
               System.out.print(".");
            }

            if (var2.fCharClass < 127 && !this.fC.fEscaped && var2.fCharClass == this.fC.fChar || var2.fCharClass == 255 || var2.fCharClass == 254 && this.fC.fEscaped || var2.fCharClass == 253 && this.fC.fEscaped && (this.fC.fChar == 80 || this.fC.fChar == 112) || var2.fCharClass == 252 && this.fC.fChar == -1) {
               break;
            }

            if (var2.fCharClass >= 128 && var2.fCharClass < 240 && !this.fC.fEscaped && this.fC.fChar != -1) {
               UnicodeSet var4 = this.fRuleSets[var2.fCharClass - 128];
               if (var4.contains(this.fC.fChar)) {
                  break;
               }
            }

            ++var3;
         }

         if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
            System.out.println("");
         }

         if (var2.fAction != null) {
            break;
         }

         if (var2.fPushState != 0) {
            ++this.fStackPtr;
            if (this.fStackPtr >= 100) {
               System.out.println("RBBIRuleScanner.parse() - state stack overflow.");
               this.error(66049);
            }

            this.fStack[this.fStackPtr] = var2.fPushState;
         }

         if (var2.fNextChar) {
            this.nextChar(this.fC);
         }

         if (var2.fNextState != 255) {
            var1 = var2.fNextState;
         } else {
            var1 = this.fStack[this.fStackPtr];
            --this.fStackPtr;
            if (this.fStackPtr < 0) {
               System.out.println("RBBIRuleScanner.parse() - state stack underflow.");
               this.error(66049);
            }
         }
      }

      if (this.fRB.fTreeRoots[1] == null) {
         this.fRB.fTreeRoots[1] = this.pushNewNode(10);
         RBBINode var5 = this.pushNewNode(0);
         this.findSetFor(kAny, var5, (UnicodeSet)null);
         this.fRB.fTreeRoots[1].fLeftChild = var5;
         var5.fParent = this.fRB.fTreeRoots[1];
         this.fNodeStackPtr -= 2;
      }

      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("symbols") >= 0) {
         this.fSymbolTable.rbbiSymtablePrint();
      }

      if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ptree") >= 0) {
         System.out.println("Completed Forward Rules Parse Tree...");
         this.fRB.fTreeRoots[0].printTree(true);
         System.out.println("\nCompleted Reverse Rules Parse Tree...");
         this.fRB.fTreeRoots[1].printTree(true);
         System.out.println("\nCompleted Safe Point Forward Rules Parse Tree...");
         if (this.fRB.fTreeRoots[2] == null) {
            System.out.println("  -- null -- ");
         } else {
            this.fRB.fTreeRoots[2].printTree(true);
         }

         System.out.println("\nCompleted Safe Point Reverse Rules Parse Tree...");
         if (this.fRB.fTreeRoots[3] == null) {
            System.out.println("  -- null -- ");
         } else {
            this.fRB.fTreeRoots[3].printTree(true);
         }
      }

   }

   void printNodeStack(String var1) {
      System.out.println(var1 + ".  Dumping node stack...\n");

      for(int var2 = this.fNodeStackPtr; var2 > 0; --var2) {
         this.fNodeStack[var2].printTree(true);
      }

   }

   RBBINode pushNewNode(int var1) {
      ++this.fNodeStackPtr;
      if (this.fNodeStackPtr >= 100) {
         System.out.println("RBBIRuleScanner.pushNewNode - stack overflow.");
         this.error(66049);
      }

      this.fNodeStack[this.fNodeStackPtr] = new RBBINode(var1);
      return this.fNodeStack[this.fNodeStackPtr];
   }

   void scanSet() {
      UnicodeSet var1 = null;
      ParsePosition var3 = new ParsePosition(this.fScanIndex);
      int var2 = this.fScanIndex;

      try {
         var1 = new UnicodeSet(this.fRB.fRules, var3, this.fSymbolTable, 1);
      } catch (Exception var6) {
         this.error(66063);
      }

      if (var1.isEmpty()) {
         this.error(66060);
      }

      int var4 = var3.getIndex();

      while(this.fNextIndex < var4) {
         this.nextCharLL();
      }

      RBBINode var5 = this.pushNewNode(0);
      var5.fFirstPos = var2;
      var5.fLastPos = this.fNextIndex;
      var5.fText = this.fRB.fRules.substring(var5.fFirstPos, var5.fLastPos);
      this.findSetFor(var5.fText, var5, var1);
   }

   static class RBBISetTableEl {
      String key;
      RBBINode val;
   }

   static class RBBIRuleChar {
      int fChar;
      boolean fEscaped;
   }
}
