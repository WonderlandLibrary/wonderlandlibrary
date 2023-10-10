/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleBuilder;
import com.ibm.icu.text.RBBIRuleParseTable;
import com.ibm.icu.text.RBBISymbolTable;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
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
    RBBIRuleChar fC = new RBBIRuleChar();
    short[] fStack = new short[100];
    int fStackPtr;
    RBBINode[] fNodeStack = new RBBINode[100];
    int fNodeStackPtr;
    boolean fReverseRule;
    boolean fLookAheadRule;
    boolean fNoChainInRule;
    RBBISymbolTable fSymbolTable;
    HashMap<String, RBBISetTableEl> fSetTable = new HashMap();
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

    RBBIRuleScanner(RBBIRuleBuilder rb) {
        this.fRB = rb;
        this.fLineNum = 1;
        this.fRuleSets[3] = new UnicodeSet(gRuleSet_rule_char_pattern);
        this.fRuleSets[4] = new UnicodeSet(gRuleSet_white_space_pattern);
        this.fRuleSets[1] = new UnicodeSet(gRuleSet_name_char_pattern);
        this.fRuleSets[2] = new UnicodeSet(gRuleSet_name_start_char_pattern);
        this.fRuleSets[0] = new UnicodeSet(gRuleSet_digit_char_pattern);
        this.fSymbolTable = new RBBISymbolTable(this);
    }

    boolean doParseActions(int action) {
        RBBINode n2 = null;
        boolean returnVal = true;
        switch (action) {
            case 11: {
                this.pushNewNode(7);
                ++this.fRuleNum;
                break;
            }
            case 14: {
                this.fNoChainInRule = true;
                break;
            }
            case 9: {
                this.fixOpStack(4);
                RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                RBBINode orNode = this.pushNewNode(9);
                orNode.fLeftChild = operandNode;
                operandNode.fParent = orNode;
                break;
            }
            case 7: {
                this.fixOpStack(4);
                RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                RBBINode catNode = this.pushNewNode(8);
                catNode.fLeftChild = operandNode;
                operandNode.fParent = catNode;
                break;
            }
            case 12: {
                this.pushNewNode(15);
                break;
            }
            case 10: {
                this.fixOpStack(2);
                break;
            }
            case 13: {
                break;
            }
            case 23: {
                n2 = this.fNodeStack[this.fNodeStackPtr - 1];
                n2.fFirstPos = this.fNextIndex;
                this.pushNewNode(7);
                break;
            }
            case 3: {
                this.fixOpStack(1);
                RBBINode startExprNode = this.fNodeStack[this.fNodeStackPtr - 2];
                RBBINode varRefNode = this.fNodeStack[this.fNodeStackPtr - 1];
                RBBINode RHSExprNode = this.fNodeStack[this.fNodeStackPtr];
                RHSExprNode.fFirstPos = startExprNode.fFirstPos;
                RHSExprNode.fLastPos = this.fScanIndex;
                RHSExprNode.fText = this.fRB.fRules.substring(RHSExprNode.fFirstPos, RHSExprNode.fLastPos);
                varRefNode.fLeftChild = RHSExprNode;
                RHSExprNode.fParent = varRefNode;
                this.fSymbolTable.addEntry(varRefNode.fText, varRefNode);
                this.fNodeStackPtr -= 3;
                break;
            }
            case 4: {
                int destRules;
                this.fixOpStack(1);
                if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("rtree") >= 0) {
                    this.printNodeStack("end of rule");
                }
                Assert.assrt(this.fNodeStackPtr == 1);
                RBBINode thisRule = this.fNodeStack[this.fNodeStackPtr];
                if (this.fLookAheadRule) {
                    RBBINode endNode = this.pushNewNode(6);
                    RBBINode catNode = this.pushNewNode(8);
                    this.fNodeStackPtr -= 2;
                    catNode.fLeftChild = thisRule;
                    catNode.fRightChild = endNode;
                    this.fNodeStack[this.fNodeStackPtr] = catNode;
                    endNode.fVal = this.fRuleNum;
                    endNode.fLookAheadEnd = true;
                    thisRule = catNode;
                }
                thisRule.fRuleRoot = true;
                if (this.fRB.fChainRules && !this.fNoChainInRule) {
                    thisRule.fChainIn = true;
                }
                int n3 = destRules = this.fReverseRule ? 3 : this.fRB.fDefaultTree;
                if (this.fRB.fTreeRoots[destRules] != null) {
                    thisRule = this.fNodeStack[this.fNodeStackPtr];
                    RBBINode prevRules = this.fRB.fTreeRoots[destRules];
                    RBBINode orNode = this.pushNewNode(9);
                    orNode.fLeftChild = prevRules;
                    prevRules.fParent = orNode;
                    orNode.fRightChild = thisRule;
                    thisRule.fParent = orNode;
                    this.fRB.fTreeRoots[destRules] = orNode;
                } else {
                    this.fRB.fTreeRoots[destRules] = this.fNodeStack[this.fNodeStackPtr];
                }
                this.fReverseRule = false;
                this.fLookAheadRule = false;
                this.fNoChainInRule = false;
                this.fNodeStackPtr = 0;
                break;
            }
            case 19: {
                this.error(66052);
                returnVal = false;
                break;
            }
            case 32: {
                this.error(66052);
                break;
            }
            case 29: {
                RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                RBBINode plusNode = this.pushNewNode(11);
                plusNode.fLeftChild = operandNode;
                operandNode.fParent = plusNode;
                break;
            }
            case 30: {
                RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                RBBINode qNode = this.pushNewNode(12);
                qNode.fLeftChild = operandNode;
                operandNode.fParent = qNode;
                break;
            }
            case 31: {
                RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                RBBINode starNode = this.pushNewNode(10);
                starNode.fLeftChild = operandNode;
                operandNode.fParent = starNode;
                break;
            }
            case 18: {
                n2 = this.pushNewNode(0);
                String s2 = String.valueOf((char)this.fC.fChar);
                this.findSetFor(s2, n2, null);
                n2.fFirstPos = this.fScanIndex;
                n2.fLastPos = this.fNextIndex;
                n2.fText = this.fRB.fRules.substring(n2.fFirstPos, n2.fLastPos);
                break;
            }
            case 2: {
                n2 = this.pushNewNode(0);
                this.findSetFor(kAny, n2, null);
                n2.fFirstPos = this.fScanIndex;
                n2.fLastPos = this.fNextIndex;
                n2.fText = this.fRB.fRules.substring(n2.fFirstPos, n2.fLastPos);
                break;
            }
            case 22: {
                n2 = this.pushNewNode(4);
                n2.fVal = this.fRuleNum;
                n2.fFirstPos = this.fScanIndex;
                n2.fLastPos = this.fNextIndex;
                n2.fText = this.fRB.fRules.substring(n2.fFirstPos, n2.fLastPos);
                this.fLookAheadRule = true;
                break;
            }
            case 24: {
                n2 = this.pushNewNode(5);
                n2.fVal = 0;
                n2.fFirstPos = this.fScanIndex;
                n2.fLastPos = this.fNextIndex;
                break;
            }
            case 26: {
                n2 = this.fNodeStack[this.fNodeStackPtr];
                int v2 = UCharacter.digit((char)this.fC.fChar, 10);
                n2.fVal = n2.fVal * 10 + v2;
                break;
            }
            case 28: {
                n2 = this.fNodeStack[this.fNodeStackPtr];
                n2.fLastPos = this.fNextIndex;
                n2.fText = this.fRB.fRules.substring(n2.fFirstPos, n2.fLastPos);
                break;
            }
            case 27: {
                this.error(66062);
                returnVal = false;
                break;
            }
            case 16: {
                this.fOptionStart = this.fScanIndex;
                break;
            }
            case 15: {
                String opt = this.fRB.fRules.substring(this.fOptionStart, this.fScanIndex);
                if (opt.equals("chain")) {
                    this.fRB.fChainRules = true;
                    break;
                }
                if (opt.equals("LBCMNoChain")) {
                    this.fRB.fLBCMNoChain = true;
                    break;
                }
                if (opt.equals("forward")) {
                    this.fRB.fDefaultTree = 0;
                    break;
                }
                if (opt.equals("reverse")) {
                    this.fRB.fDefaultTree = 1;
                    break;
                }
                if (opt.equals("safe_forward")) {
                    this.fRB.fDefaultTree = 2;
                    break;
                }
                if (opt.equals("safe_reverse")) {
                    this.fRB.fDefaultTree = 3;
                    break;
                }
                if (opt.equals("lookAheadHardBreak")) {
                    this.fRB.fLookAheadHardBreak = true;
                    break;
                }
                if (opt.equals("quoted_literals_only")) {
                    this.fRuleSets[3].clear();
                    break;
                }
                if (opt.equals("unquoted_literals")) {
                    this.fRuleSets[3].applyPattern(gRuleSet_rule_char_pattern);
                    break;
                }
                this.error(66061);
                break;
            }
            case 17: {
                this.fReverseRule = true;
                break;
            }
            case 25: {
                n2 = this.pushNewNode(2);
                n2.fFirstPos = this.fScanIndex;
                break;
            }
            case 5: {
                n2 = this.fNodeStack[this.fNodeStackPtr];
                if (n2 == null || n2.fType != 2) {
                    this.error(66049);
                    break;
                }
                n2.fLastPos = this.fScanIndex;
                n2.fText = this.fRB.fRules.substring(n2.fFirstPos + 1, n2.fLastPos);
                n2.fLeftChild = this.fSymbolTable.lookupNode(n2.fText);
                break;
            }
            case 1: {
                n2 = this.fNodeStack[this.fNodeStackPtr];
                if (n2.fLeftChild != null) break;
                this.error(66058);
                returnVal = false;
                break;
            }
            case 8: {
                break;
            }
            case 20: {
                this.error(66054);
                returnVal = false;
                break;
            }
            case 6: {
                returnVal = false;
                break;
            }
            case 21: {
                this.scanSet();
                break;
            }
            default: {
                this.error(66049);
                returnVal = false;
            }
        }
        return returnVal;
    }

    void error(int e2) {
        String s2 = "Error " + e2 + " at line " + this.fLineNum + " column " + this.fCharNum;
        IllegalArgumentException ex = new IllegalArgumentException(s2);
        throw ex;
    }

    void fixOpStack(int p2) {
        RBBINode n2;
        while (true) {
            n2 = this.fNodeStack[this.fNodeStackPtr - 1];
            if (n2.fPrecedence == 0) {
                System.out.print("RBBIRuleScanner.fixOpStack, bad operator node");
                this.error(66049);
                return;
            }
            if (n2.fPrecedence < p2 || n2.fPrecedence <= 2) break;
            n2.fRightChild = this.fNodeStack[this.fNodeStackPtr];
            this.fNodeStack[this.fNodeStackPtr].fParent = n2;
            --this.fNodeStackPtr;
        }
        if (p2 <= 2) {
            if (n2.fPrecedence != p2) {
                this.error(66056);
            }
            this.fNodeStack[this.fNodeStackPtr - 1] = this.fNodeStack[this.fNodeStackPtr];
            --this.fNodeStackPtr;
        }
    }

    void findSetFor(String s2, RBBINode node, UnicodeSet setToAdopt) {
        RBBISetTableEl el = this.fSetTable.get(s2);
        if (el != null) {
            node.fLeftChild = el.val;
            Assert.assrt(node.fLeftChild.fType == 1);
            return;
        }
        if (setToAdopt == null) {
            if (s2.equals(kAny)) {
                setToAdopt = new UnicodeSet(0, 0x10FFFF);
            } else {
                int c2 = UTF16.charAt(s2, 0);
                setToAdopt = new UnicodeSet(c2, c2);
            }
        }
        RBBINode usetNode = new RBBINode(1);
        usetNode.fInputSet = setToAdopt;
        usetNode.fParent = node;
        node.fLeftChild = usetNode;
        usetNode.fText = s2;
        this.fRB.fUSetNodes.add(usetNode);
        el = new RBBISetTableEl();
        el.key = s2;
        el.val = usetNode;
        this.fSetTable.put(el.key, el);
    }

    static String stripRules(String rules) {
        StringBuilder strippedRules = new StringBuilder();
        int rulesLength = rules.length();
        boolean skippingSpaces = false;
        int idx = 0;
        while (idx < rulesLength) {
            int cp = rules.codePointAt(idx);
            boolean whiteSpace = UCharacter.hasBinaryProperty(cp, 43);
            if (!skippingSpaces || !whiteSpace) {
                strippedRules.appendCodePoint(cp);
                skippingSpaces = whiteSpace;
            }
            idx = rules.offsetByCodePoints(idx, 1);
        }
        return strippedRules.toString();
    }

    int nextCharLL() {
        if (this.fNextIndex >= this.fRB.fRules.length()) {
            return -1;
        }
        int ch = UTF16.charAt(this.fRB.fRules, this.fNextIndex);
        this.fNextIndex = UTF16.moveCodePointOffset(this.fRB.fRules, this.fNextIndex, 1);
        if (ch == 13 || ch == 133 || ch == 8232 || ch == 10 && this.fLastChar != 13) {
            ++this.fLineNum;
            this.fCharNum = 0;
            if (this.fQuoteMode) {
                this.error(66057);
                this.fQuoteMode = false;
            }
        } else if (ch != 10) {
            ++this.fCharNum;
        }
        this.fLastChar = ch;
        return ch;
    }

    void nextChar(RBBIRuleChar c2) {
        this.fScanIndex = this.fNextIndex;
        c2.fChar = this.nextCharLL();
        c2.fEscaped = false;
        if (c2.fChar == 39) {
            if (UTF16.charAt(this.fRB.fRules, this.fNextIndex) == 39) {
                c2.fChar = this.nextCharLL();
                c2.fEscaped = true;
            } else {
                this.fQuoteMode = !this.fQuoteMode;
                c2.fChar = this.fQuoteMode ? 40 : 41;
                c2.fEscaped = false;
                return;
            }
        }
        if (this.fQuoteMode) {
            c2.fEscaped = true;
        } else {
            if (c2.fChar == 35) {
                int commentStart = this.fScanIndex;
                do {
                    c2.fChar = this.nextCharLL();
                } while (c2.fChar != -1 && c2.fChar != 13 && c2.fChar != 10 && c2.fChar != 133 && c2.fChar != 8232);
                for (int i2 = commentStart; i2 < this.fNextIndex - 1; ++i2) {
                    this.fRB.fStrippedRules.setCharAt(i2, ' ');
                }
            }
            if (c2.fChar == -1) {
                return;
            }
            if (c2.fChar == 92) {
                c2.fEscaped = true;
                int[] unescapeIndex = new int[]{this.fNextIndex};
                c2.fChar = Utility.unescapeAt(this.fRB.fRules, unescapeIndex);
                if (unescapeIndex[0] == this.fNextIndex) {
                    this.error(66050);
                }
                this.fCharNum += unescapeIndex[0] - this.fNextIndex;
                this.fNextIndex = unescapeIndex[0];
            }
        }
    }

    void parse() {
        int state = 1;
        this.nextChar(this.fC);
        while (state != 0) {
            RBBIRuleParseTable.RBBIRuleTableElement tableEl = RBBIRuleParseTable.gRuleParseStateTable[state];
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                System.out.println("char, line, col = ('" + (char)this.fC.fChar + "', " + this.fLineNum + ", " + this.fCharNum + "    state = " + tableEl.fStateName);
            }
            int tableRow = state;
            while (true) {
                UnicodeSet uniset;
                tableEl = RBBIRuleParseTable.gRuleParseStateTable[tableRow];
                if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                    System.out.print(".");
                }
                if (tableEl.fCharClass < 127 && !this.fC.fEscaped && tableEl.fCharClass == this.fC.fChar || tableEl.fCharClass == 255 || tableEl.fCharClass == 254 && this.fC.fEscaped || tableEl.fCharClass == 253 && this.fC.fEscaped && (this.fC.fChar == 80 || this.fC.fChar == 112) || tableEl.fCharClass == 252 && this.fC.fChar == -1 || tableEl.fCharClass >= 128 && tableEl.fCharClass < 240 && !this.fC.fEscaped && this.fC.fChar != -1 && (uniset = this.fRuleSets[tableEl.fCharClass - 128]).contains(this.fC.fChar)) break;
                ++tableRow;
            }
            if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                System.out.println("");
            }
            if (!this.doParseActions(tableEl.fAction)) break;
            if (tableEl.fPushState != 0) {
                ++this.fStackPtr;
                if (this.fStackPtr >= 100) {
                    System.out.println("RBBIRuleScanner.parse() - state stack overflow.");
                    this.error(66049);
                }
                this.fStack[this.fStackPtr] = tableEl.fPushState;
            }
            if (tableEl.fNextChar) {
                this.nextChar(this.fC);
            }
            if (tableEl.fNextState != 255) {
                state = tableEl.fNextState;
                continue;
            }
            state = this.fStack[this.fStackPtr];
            --this.fStackPtr;
            if (this.fStackPtr >= 0) continue;
            System.out.println("RBBIRuleScanner.parse() - state stack underflow.");
            this.error(66049);
        }
        if (this.fRB.fTreeRoots[0] == null) {
            this.error(66052);
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

    void printNodeStack(String title) {
        System.out.println(title + ".  Dumping node stack...\n");
        for (int i2 = this.fNodeStackPtr; i2 > 0; --i2) {
            this.fNodeStack[i2].printTree(true);
        }
    }

    RBBINode pushNewNode(int nodeType) {
        ++this.fNodeStackPtr;
        if (this.fNodeStackPtr >= 100) {
            System.out.println("RBBIRuleScanner.pushNewNode - stack overflow.");
            this.error(66049);
        }
        this.fNodeStack[this.fNodeStackPtr] = new RBBINode(nodeType);
        return this.fNodeStack[this.fNodeStackPtr];
    }

    void scanSet() {
        UnicodeSet uset = null;
        ParsePosition pos = new ParsePosition(this.fScanIndex);
        int startPos = this.fScanIndex;
        try {
            uset = new UnicodeSet(this.fRB.fRules, pos, this.fSymbolTable, 1);
        }
        catch (Exception e2) {
            this.error(66063);
        }
        if (uset.isEmpty()) {
            this.error(66060);
        }
        int i2 = pos.getIndex();
        while (this.fNextIndex < i2) {
            this.nextCharLL();
        }
        RBBINode n2 = this.pushNewNode(0);
        n2.fFirstPos = startPos;
        n2.fLastPos = this.fNextIndex;
        n2.fText = this.fRB.fRules.substring(n2.fFirstPos, n2.fLastPos);
        this.findSetFor(n2.fText, n2, uset);
    }

    static class RBBISetTableEl {
        String key;
        RBBINode val;

        RBBISetTableEl() {
        }
    }

    static class RBBIRuleChar {
        int fChar;
        boolean fEscaped;

        RBBIRuleChar() {
        }
    }
}

