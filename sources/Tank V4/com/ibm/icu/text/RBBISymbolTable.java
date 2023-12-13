package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.text.ParsePosition;
import java.util.HashMap;

class RBBISymbolTable implements SymbolTable {
   String fRules;
   HashMap fHashTable;
   RBBIRuleScanner fRuleScanner;
   String ffffString;
   UnicodeSet fCachedSetLookup;

   RBBISymbolTable(RBBIRuleScanner var1, String var2) {
      this.fRules = var2;
      this.fRuleScanner = var1;
      this.fHashTable = new HashMap();
      this.ffffString = "\uffff";
   }

   public char[] lookup(String var1) {
      RBBISymbolTable.RBBISymbolTableEntry var2 = (RBBISymbolTable.RBBISymbolTableEntry)this.fHashTable.get(var1);
      if (var2 == null) {
         return null;
      } else {
         RBBINode var3;
         for(var3 = var2.val; var3.fLeftChild.fType == 2; var3 = var3.fLeftChild) {
         }

         RBBINode var4 = var3.fLeftChild;
         String var6;
         if (var4.fType == 0) {
            RBBINode var5 = var4.fLeftChild;
            this.fCachedSetLookup = var5.fInputSet;
            var6 = this.ffffString;
         } else {
            this.fRuleScanner.error(66063);
            var6 = var4.fText;
            this.fCachedSetLookup = null;
         }

         return var6.toCharArray();
      }
   }

   public UnicodeMatcher lookupMatcher(int var1) {
      UnicodeSet var2 = null;
      if (var1 == 65535) {
         var2 = this.fCachedSetLookup;
         this.fCachedSetLookup = null;
      }

      return var2;
   }

   public String parseReference(String var1, ParsePosition var2, int var3) {
      int var4 = var2.getIndex();
      int var5 = var4;

      String var6;
      int var7;
      for(var6 = ""; var5 < var3; var5 += UTF16.getCharCount(var7)) {
         var7 = UTF16.charAt(var1, var5);
         if (var5 == var4 && !UCharacter.isUnicodeIdentifierStart(var7) || !UCharacter.isUnicodeIdentifierPart(var7)) {
            break;
         }
      }

      if (var5 == var4) {
         return var6;
      } else {
         var2.setIndex(var5);
         var6 = var1.substring(var4, var5);
         return var6;
      }
   }

   RBBINode lookupNode(String var1) {
      RBBINode var2 = null;
      RBBISymbolTable.RBBISymbolTableEntry var3 = (RBBISymbolTable.RBBISymbolTableEntry)this.fHashTable.get(var1);
      if (var3 != null) {
         var2 = var3.val;
      }

      return var2;
   }

   void addEntry(String var1, RBBINode var2) {
      RBBISymbolTable.RBBISymbolTableEntry var3 = (RBBISymbolTable.RBBISymbolTableEntry)this.fHashTable.get(var1);
      if (var3 != null) {
         this.fRuleScanner.error(66055);
      } else {
         var3 = new RBBISymbolTable.RBBISymbolTableEntry();
         var3.key = var1;
         var3.val = var2;
         this.fHashTable.put(var3.key, var3);
      }
   }

   void rbbiSymtablePrint() {
      System.out.print("Variable Definitions\nName               Node Val     String Val\n----------------------------------------------------------------------\n");
      RBBISymbolTable.RBBISymbolTableEntry[] var1 = (RBBISymbolTable.RBBISymbolTableEntry[])this.fHashTable.values().toArray(new RBBISymbolTable.RBBISymbolTableEntry[0]);

      int var2;
      RBBISymbolTable.RBBISymbolTableEntry var3;
      for(var2 = 0; var2 < var1.length; ++var2) {
         var3 = var1[var2];
         System.out.print("  " + var3.key + "  ");
         System.out.print("  " + var3.val + "  ");
         System.out.print(var3.val.fLeftChild.fText);
         System.out.print("\n");
      }

      System.out.println("\nParsed Variable Definitions\n");

      for(var2 = 0; var2 < var1.length; ++var2) {
         var3 = var1[var2];
         System.out.print(var3.key);
         var3.val.fLeftChild.printTree(true);
         System.out.print("\n");
      }

   }

   static class RBBISymbolTableEntry {
      String key;
      RBBINode val;
   }
}
