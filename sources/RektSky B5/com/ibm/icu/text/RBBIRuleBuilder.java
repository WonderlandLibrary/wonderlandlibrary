/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.RBBIDataWrapper;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleScanner;
import com.ibm.icu.text.RBBISetBuilder;
import com.ibm.icu.text.RBBITableBuilder;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class RBBIRuleBuilder {
    String fDebugEnv;
    String fRules;
    StringBuilder fStrippedRules;
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
    List<RBBINode> fUSetNodes;
    RBBITableBuilder fForwardTable;
    Map<Set<Integer>, Integer> fStatusSets = new HashMap<Set<Integer>, Integer>();
    List<Integer> fRuleStatusVals;
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

    RBBIRuleBuilder(String rules) {
        this.fDebugEnv = ICUDebug.enabled("rbbi") ? ICUDebug.value("rbbi") : null;
        this.fRules = rules;
        this.fStrippedRules = new StringBuilder(rules);
        this.fUSetNodes = new ArrayList<RBBINode>();
        this.fRuleStatusVals = new ArrayList<Integer>();
        this.fScanner = new RBBIRuleScanner(this);
        this.fSetBuilder = new RBBISetBuilder(this);
    }

    static final int align8(int i2) {
        return i2 + 7 & 0xFFFFFFF8;
    }

    void flattenData(OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        String strippedRules = RBBIRuleScanner.stripRules(this.fStrippedRules.toString());
        int headerSize = 80;
        int forwardTableSize = RBBIRuleBuilder.align8(this.fForwardTable.getTableSize());
        int reverseTableSize = RBBIRuleBuilder.align8(this.fForwardTable.getSafeTableSize());
        int trieSize = RBBIRuleBuilder.align8(this.fSetBuilder.getTrieSize());
        int statusTableSize = RBBIRuleBuilder.align8(this.fRuleStatusVals.size() * 4);
        int rulesSize = RBBIRuleBuilder.align8(strippedRules.length() * 2);
        int totalSize = headerSize + forwardTableSize + reverseTableSize + statusTableSize + trieSize + rulesSize;
        int outputPos = 0;
        ICUBinary.writeHeader(1114794784, 0x5000000, 0, dos);
        int[] header = new int[20];
        header[0] = 45472;
        header[1] = 0x5000000;
        header[2] = totalSize;
        header[3] = this.fSetBuilder.getNumCharCategories();
        header[4] = headerSize;
        header[5] = forwardTableSize;
        header[6] = header[4] + forwardTableSize;
        header[7] = reverseTableSize;
        header[8] = header[6] + header[7];
        header[9] = this.fSetBuilder.getTrieSize();
        header[12] = header[8] + header[9];
        header[13] = statusTableSize;
        header[10] = header[12] + statusTableSize;
        header[11] = strippedRules.length() * 2;
        for (int i2 = 0; i2 < header.length; ++i2) {
            dos.writeInt(header[i2]);
            outputPos += 4;
        }
        RBBIDataWrapper.RBBIStateTable table = this.fForwardTable.exportTable();
        assert (outputPos == header[4]);
        outputPos += table.put(dos);
        table = this.fForwardTable.exportSafeTable();
        Assert.assrt(outputPos == header[6]);
        Assert.assrt((outputPos += table.put(dos)) == header[8]);
        this.fSetBuilder.serializeTrie(os);
        outputPos += header[9];
        while (outputPos % 8 != 0) {
            dos.write(0);
            ++outputPos;
        }
        Assert.assrt(outputPos == header[12]);
        for (Integer val2 : this.fRuleStatusVals) {
            dos.writeInt(val2);
            outputPos += 4;
        }
        while (outputPos % 8 != 0) {
            dos.write(0);
            ++outputPos;
        }
        Assert.assrt(outputPos == header[10]);
        dos.writeChars(strippedRules);
        outputPos += strippedRules.length() * 2;
        while (outputPos % 8 != 0) {
            dos.write(0);
            ++outputPos;
        }
    }

    static void compileRules(String rules, OutputStream os) throws IOException {
        RBBIRuleBuilder builder = new RBBIRuleBuilder(rules);
        builder.build(os);
    }

    void build(OutputStream os) throws IOException {
        this.fScanner.parse();
        this.fSetBuilder.buildRanges();
        this.fForwardTable = new RBBITableBuilder(this, 0);
        this.fForwardTable.buildForwardTable();
        this.optimizeTables();
        this.fForwardTable.buildSafeReverseTable();
        if (this.fDebugEnv != null && this.fDebugEnv.indexOf("states") >= 0) {
            this.fForwardTable.printStates();
            this.fForwardTable.printRuleStatusTable();
            this.fForwardTable.printReverseTable();
        }
        this.fSetBuilder.buildTrie();
        this.flattenData(os);
    }

    void optimizeTables() {
        IntPair duplPair = new IntPair(3, 0);
        while (this.fForwardTable.findDuplCharClassFrom(duplPair)) {
            this.fSetBuilder.mergeCategories(duplPair);
            this.fForwardTable.removeColumn(duplPair.second);
        }
        this.fForwardTable.removeDuplicateStates();
    }

    static class IntPair {
        int first = 0;
        int second = 0;

        IntPair() {
        }

        IntPair(int f2, int s2) {
            this.first = f2;
            this.second = s2;
        }
    }
}

