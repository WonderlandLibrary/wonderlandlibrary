package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.ICUDebug;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class RBBIRuleBuilder {
   String fDebugEnv = ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null;
   String fRules;
   RBBIRuleScanner fScanner;
   RBBINode[] fTreeRoots = new RBBINode[4];
   static final int fForwardTree = 0;
   static final int fReverseTree = 1;
   static final int fSafeFwdTree = 2;
   static final int fSafeRevTree = 3;
   int fDefaultTree = 0;
   boolean fChainRules;
   boolean fLBCMNoChain;
   boolean fLookAheadHardBreak;
   RBBISetBuilder fSetBuilder;
   List fUSetNodes;
   RBBITableBuilder fForwardTables;
   RBBITableBuilder fReverseTables;
   RBBITableBuilder fSafeFwdTables;
   RBBITableBuilder fSafeRevTables;
   Map fStatusSets = new HashMap();
   List fRuleStatusVals;
   static final int U_BRK_ERROR_START = 66048;
   static final int U_BRK_INTERNAL_ERROR = 66049;
   static final int U_BRK_HEX_DIGITS_EXPECTED = 66050;
   static final int U_BRK_SEMICOLON_EXPECTED = 66051;
   static final int U_BRK_RULE_SYNTAX = 66052;
   static final int U_BRK_UNCLOSED_SET = 66053;
   static final int U_BRK_ASSIGN_ERROR = 66054;
   static final int U_BRK_VARIABLE_REDFINITION = 66055;
   static final int U_BRK_MISMATCHED_PAREN = 66056;
   static final int U_BRK_NEW_LINE_IN_QUOTED_STRING = 66057;
   static final int U_BRK_UNDEFINED_VARIABLE = 66058;
   static final int U_BRK_INIT_ERROR = 66059;
   static final int U_BRK_RULE_EMPTY_SET = 66060;
   static final int U_BRK_UNRECOGNIZED_OPTION = 66061;
   static final int U_BRK_MALFORMED_RULE_TAG = 66062;
   static final int U_BRK_MALFORMED_SET = 66063;
   static final int U_BRK_ERROR_LIMIT = 66064;

   RBBIRuleBuilder(String var1) {
      this.fRules = var1;
      this.fUSetNodes = new ArrayList();
      this.fRuleStatusVals = new ArrayList();
      this.fScanner = new RBBIRuleScanner(this);
      this.fSetBuilder = new RBBISetBuilder(this);
   }

   static final int align8(int var0) {
      return var0 + 7 & -8;
   }

   void flattenData(OutputStream var1) throws IOException {
      DataOutputStream var2 = new DataOutputStream(var1);
      String var4 = RBBIRuleScanner.stripRules(this.fRules);
      byte var5 = 96;
      int var6 = align8(this.fForwardTables.getTableSize());
      int var7 = align8(this.fReverseTables.getTableSize());
      int var8 = align8(this.fSafeFwdTables.getTableSize());
      int var9 = align8(this.fSafeRevTables.getTableSize());
      int var10 = align8(this.fSetBuilder.getTrieSize());
      int var11 = align8(this.fRuleStatusVals.size() * 4);
      int var12 = align8(var4.length() * 2);
      int var13 = var5 + var6 + var7 + var8 + var9 + var11 + var10 + var12;
      int var14 = 0;
      byte[] var15 = new byte[128];
      var2.write(var15);
      int[] var16 = new int[24];
      var16[0] = 45472;
      var16[1] = 50397184;
      var16[2] = var13;
      var16[3] = this.fSetBuilder.getNumCharCategories();
      var16[4] = var5;
      var16[5] = var6;
      var16[6] = var16[4] + var6;
      var16[7] = var7;
      var16[8] = var16[6] + var7;
      var16[9] = var8;
      var16[10] = var16[8] + var8;
      var16[11] = var9;
      var16[12] = var16[10] + var9;
      var16[13] = this.fSetBuilder.getTrieSize();
      var16[16] = var16[12] + var16[13];
      var16[17] = var11;
      var16[14] = var16[16] + var11;
      var16[15] = var4.length() * 2;

      int var3;
      for(var3 = 0; var3 < var16.length; ++var3) {
         var2.writeInt(var16[var3]);
         var14 += 4;
      }

      short[] var17 = this.fForwardTables.exportTable();
      Assert.assrt(var14 == var16[4]);

      for(var3 = 0; var3 < var17.length; ++var3) {
         var2.writeShort(var17[var3]);
         var14 += 2;
      }

      var17 = this.fReverseTables.exportTable();
      Assert.assrt(var14 == var16[6]);

      for(var3 = 0; var3 < var17.length; ++var3) {
         var2.writeShort(var17[var3]);
         var14 += 2;
      }

      Assert.assrt(var14 == var16[8]);
      var17 = this.fSafeFwdTables.exportTable();

      for(var3 = 0; var3 < var17.length; ++var3) {
         var2.writeShort(var17[var3]);
         var14 += 2;
      }

      Assert.assrt(var14 == var16[10]);
      var17 = this.fSafeRevTables.exportTable();

      for(var3 = 0; var3 < var17.length; ++var3) {
         var2.writeShort(var17[var3]);
         var14 += 2;
      }

      Assert.assrt(var14 == var16[12]);
      this.fSetBuilder.serializeTrie(var1);

      for(var14 += var16[13]; var14 % 8 != 0; ++var14) {
         var2.write(0);
      }

      Assert.assrt(var14 == var16[16]);

      for(Iterator var18 = this.fRuleStatusVals.iterator(); var18.hasNext(); var14 += 4) {
         Integer var19 = (Integer)var18.next();
         var2.writeInt(var19);
      }

      while(var14 % 8 != 0) {
         var2.write(0);
         ++var14;
      }

      Assert.assrt(var14 == var16[14]);
      var2.writeChars(var4);

      for(var14 += var4.length() * 2; var14 % 8 != 0; ++var14) {
         var2.write(0);
      }

   }

   static void compileRules(String var0, OutputStream var1) throws IOException {
      RBBIRuleBuilder var2 = new RBBIRuleBuilder(var0);
      var2.fScanner.parse();
      var2.fSetBuilder.build();
      var2.fForwardTables = new RBBITableBuilder(var2, 0);
      var2.fReverseTables = new RBBITableBuilder(var2, 1);
      var2.fSafeFwdTables = new RBBITableBuilder(var2, 2);
      var2.fSafeRevTables = new RBBITableBuilder(var2, 3);
      var2.fForwardTables.build();
      var2.fReverseTables.build();
      var2.fSafeFwdTables.build();
      var2.fSafeRevTables.build();
      if (var2.fDebugEnv != null && var2.fDebugEnv.indexOf("states") >= 0) {
         var2.fForwardTables.printRuleStatusTable();
      }

      var2.flattenData(var1);
   }
}
